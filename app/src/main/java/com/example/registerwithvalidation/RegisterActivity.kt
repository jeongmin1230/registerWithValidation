package com.example.registerwithvalidation

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
    }
    private fun createUser(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "회원가입 성공\n다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                } else { // 형식에 맞지 않을 경우

                    if(password.toString().length < 8) {
                        tvValidationPassword.text = "비밀번호를 8글자 이상으로 설정 해 주세요.\n"
                        tvValidationPassword.setTextColor(Color.RED)
                    } else if(password.toString().length >= 8) {
                        tvValidationPassword.text = "8글자 이상의 안전한 비밀번호입니다."
                        tvValidationPassword.setTextColor(Color.BLUE)
                    }
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()) {
                        tvValidationEmail.text = "올바른 이메일 형식이 아닙니다.\n"
                        tvValidationEmail.setTextColor(Color.RED)
                    } else if(android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text).matches()) {
                        tvValidationEmail.text = "올바른 이메일 형식입니다.\n"
                        tvValidationEmail.setTextColor(Color.BLUE)
                    }
                    updateUI(null)
                }
            }
            .addOnFailureListener { // 파이어베이스의 데이터와 중복됐을 경우
                Toast.makeText(this, "[회원가입 실패]", Toast.LENGTH_SHORT).show()
            }
        return
    }

    private fun updateUI(user : FirebaseUser?) {
        user?.let{
            tvResult.text = "Email : ${user.email}\nUid : ${user.uid}"
        }
    }

    fun onClickRegistration(view: View) {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()


        if(email == "" || password == "") { // 공백에러
            Toast.makeText(this, "이메일, 비밀번호는 반드시 입력해야합니다.", Toast.LENGTH_SHORT).show()

        } else { // 공백이 아닐 경우 계정 생성
            createUser(email, password)
        }
    }
}