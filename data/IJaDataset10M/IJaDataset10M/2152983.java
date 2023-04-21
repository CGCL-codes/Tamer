package logic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Question {

    private String textQuestion;

    private List<Answer> answers;

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Answer... ans) {
        answers = new LinkedList<Answer>();
        for (Answer a : ans) {
            answers.add(a);
        }
        Collections.shuffle(answers);
    }

    /**
	 * @param textQuestion
	 * @param answers
	 */
    public Question(String textQuestion, Answer[] answers) {
        setTextQuestion(textQuestion);
        setAnswers(answers);
    }

    @Override
    public String toString() {
        StringBuilder my = new StringBuilder(textQuestion + "\n");
        int i = 1;
        for (Answer a : answers) {
            my.append(i++ + ") " + a + "\n");
        }
        return my.toString();
    }
}
