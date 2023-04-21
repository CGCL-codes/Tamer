package marla.ide.operation;

import org.jdom.Element;
import marla.ide.problem.InternalMarlaException;

/**
 * Numeric information prompt
 * @author Ryan Morehart
 */
public class OperationInfoNumeric extends OperationInformation {

    /**
	 * Minimum value for the answer
	 */
    private final double min;

    /**
	 * Maximum value for the answer
	 */
    private final double max;

    /**
	 * Current answer to the question. Null if there is none
	 */
    private Double answer = null;

    /**
	 * Constructs a new numeric input with the given options.
	 * @param op Operation that this question applies to
	 * @param name Unique reference name for this prompt
	 * @param prompt User-visible prompt
	 */
    public OperationInfoNumeric(Operation op, String name, String prompt) {
        super(op, name, prompt, PromptType.NUMERIC);
        this.min = Double.MIN_VALUE;
        this.max = Double.MAX_VALUE;
    }

    /**
	 * Constructs a new numeric input with the given options.
	 * @param op Operation that this question applies to
	 * @param name Unique reference name for this prompt
	 * @param prompt User-visible prompt
	 * @param min Minimum (inclusive) value which the user may enter for this value
	 * @param max Maximum (inclusive) value which the user may enter for this value
	 */
    public OperationInfoNumeric(Operation op, String name, String prompt, double min, double max) {
        super(op, name, prompt, PromptType.NUMERIC);
        this.min = min;
        this.max = max;
        if (max < min) throw new OperationException("Minimum for query is greater than the maximum");
    }

    /**
	 * Copy constructor
	 * @param parent Operation this information belongs to. Does not actually
	 *		place the information in that operation!
	 * @param org Information to copy
	 */
    protected OperationInfoNumeric(Operation parent, OperationInfoNumeric org) {
        super(parent, org);
        answer = org.answer;
        min = org.min;
        max = org.max;
    }

    @Override
    public Double getAnswer() {
        return answer;
    }

    @Override
    public Double setAnswer(Object newAnswer) {
        Double oldAnswer = answer;
        if (newAnswer == null) throw new InternalMarlaException("Info may only be cleared by calling clearAnswer()");
        changeBeginning("question " + getName() + " answer");
        Double a;
        if (newAnswer instanceof Double) a = (Double) newAnswer; else {
            try {
                a = Double.valueOf(newAnswer.toString());
            } catch (NumberFormatException ex) {
                throw new OperationInfoRequiredException("'" + newAnswer + "' not a number", getOperation());
            }
        }
        if (a < min) throw new OperationInfoRequiredException("Set answer is below the minimum of " + min, getOperation());
        if (a > max) throw new OperationInfoRequiredException("Set answer is above the maximum of " + max, getOperation());
        answer = a;
        getOperation().checkDisplayName();
        getOperation().markDirty();
        markUnsaved();
        return oldAnswer;
    }

    @Override
    public void clearAnswer() {
        changeBeginning("clearing question " + getName() + " answer");
        answer = null;
        getOperation().checkDisplayName();
        getOperation().markDirty();
        getOperation().markUnsaved();
    }

    @Override
    public boolean autoAnswer() {
        if (min == max) {
            try {
                setAnswer(min);
                return true;
            } catch (OperationInfoRequiredException ex) {
                throw new InternalMarlaException("Numeric information was attempted to be autofilled but failed to set properly");
            }
        } else return false;
    }

    /**
	 * Returns the minimum value this prompt will accept. If there is none, returns
	 * Double.MIN_VALUE
	 * @return Minimum value for user answer
	 */
    public double getMin() {
        return min;
    }

    /**
	 * Returns the maximum value this prompt will accept. If there is none, returns
	 * Double.MAX_VALUE
	 * @return Maximum value for user answer
	 */
    public double getMax() {
        return max;
    }

    @Override
    protected void toXmlAnswer(Element saveEl) {
        if (answer != null) saveEl.setAttribute("answer", answer.toString());
    }

    @Override
    protected void fromXmlAnswer(Element answerEl) {
        try {
            String answerStr = answerEl.getAttributeValue("answer");
            if (answerStr != null) setAnswer(Double.valueOf(answerStr));
        } catch (OperationInfoRequiredException ex) {
            clearAnswer();
        }
    }

    @Override
    OperationInformation clone(Operation parent) {
        return new OperationInfoNumeric(parent, this);
    }
}
