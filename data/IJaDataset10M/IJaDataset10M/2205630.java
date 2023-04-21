package aquest.model;

import java.util.Vector;

/**
 * @author aurelio
 */
public class MultiValueQuestion extends Question {

    private Vector<String> correct;

    private Vector<String> wrong;

    /**
	 * @param answer
	 */
    public MultiValueQuestion(QuestionBundle parent, String text, Vector<String> correct, Vector<String> wrong) {
        super(parent, text);
        this.correct = correct;
        this.wrong = wrong;
    }

    public String toString() {
        return getText() + " \nCORRECT:\n " + correct + " \nWRONG:\n " + wrong + "\n";
    }

    public Vector<String> getCorrect() {
        return correct;
    }

    public Vector<String> getWrong() {
        return wrong;
    }
}
