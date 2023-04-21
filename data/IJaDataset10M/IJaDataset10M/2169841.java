package org.exteca.pattern;

import org.exteca.language.Spans;

/**
 * RuleTrigger represents the runtime information of a Rule regarding its
 * evaluation status, which includes the score and the positions in the document
 * that have triggered.
 * 
 * @author Llewelyn Fernandes
 * @author Mauro Talevi
 */
public class RuleTrigger {

    /** The flag to determined if Rule has already been evaluated */
    private boolean evaluated;

    /** The resultant score after the rule evaluation */
    private int score;

    /** The positions in the document that have triggered */
    private Spans spans;

    /** 
	 * Creates RuleTrigger 
	 */
    public RuleTrigger() {
        evaluated = false;
        score = 0;
        spans = new Spans();
    }

    /**
	 * Determines if the rule has been evaluated
	 * 
	 * @return The boolean
	 */
    public boolean isEvaluated() {
        return evaluated;
    }

    /**
	 * Sets evaluated flag
	 * 
	 * @param evaluated The boolean to set.
	 */
    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    /**
	 * Returns the evaluation score
	 * 
	 * @return The int
	 */
    public int getScore() {
        return score;
    }

    /**
	 * Sets the evaluation score
	 * 
	 * @param score The int to set.
	 */
    public void setScore(int score) {
        this.score = score;
    }

    /**
	 * Returns the Spans that have triggered
	 * 
	 * @return The Spans
	 */
    public Spans getSpans() {
        return spans;
    }

    /**
	 * Adds Spans to the trigger
	 * 
	 * @param spans The Spans to add.
	 */
    public void addSpans(Spans spans) {
        this.spans.add(spans);
    }

    /**
	 * String representation
	 * 
	 * @return The String representation
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[RuleTrigger evaluated=");
        sb.append(evaluated);
        sb.append(", score=");
        sb.append(score);
        sb.append(", spans=");
        sb.append(spans);
        sb.append("]");
        return sb.toString();
    }
}
