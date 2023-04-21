package aquest.model;

/**
 * Defines a generic question
 * @author aurelio
 */
public abstract class Question {

    private static final int PROBABILITY_FACTOR = 100;

    private QuestionBundle parent = null;

    private String text;

    private int probability;

    public Question(QuestionBundle parent, String textStr) {
        this.text = textStr;
        this.probability = 210;
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFactor() {
        return probability;
    }

    public void increaseProbability() {
        probability += Question.PROBABILITY_FACTOR;
    }

    public void decreaseProbability() {
        probability -= Question.PROBABILITY_FACTOR / 2;
        if (probability < 1) {
            probability = 1;
        }
    }

    public QuestionBundle getParent() {
        return parent;
    }

    public void setParent(QuestionBundle parent) {
        this.parent = parent;
    }
}
