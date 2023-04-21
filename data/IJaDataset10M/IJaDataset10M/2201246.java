package gr.academic.city.msc.industrial.mobileclickers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AnswerQuestionActivity extends Activity {

    public static final String NUMBER_OF_ANSWERS_EXTRAS = "number_of_answers";

    public static final String QUESTION_CODE = "question_code";

    private String questionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_question);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        int numberOfAnswers = intent.getExtras().getInt(NUMBER_OF_ANSWERS_EXTRAS);
        questionCode = intent.getExtras().getString(QUESTION_CODE);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.answers_radio_button_group);
        char c = 'A';
        for (int i = 0; i < numberOfAnswers; i++) {
            RadioButton radioButton = new RadioButton(getApplicationContext());
            radioButton.setId(c);
            radioButton.setText(c++);
            radioButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            radioGroup.addView(radioButton);
        }
    }

    public void submitAnswer(View v) {
    }
}
