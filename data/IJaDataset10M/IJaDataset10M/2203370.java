package net.sf.opencet.engine.evaluation;

import net.sf.opencet.functional.Answer;

/**
 * An interface that is implemented by all comparator classes. A comparator class
 * wraps the method used during the evaluation process for comparing two answers.
 * 
 */
public interface AnswerComparator {

    public static final double code = 0.000001;

    /**
     * The method that evaluates an individual answer in a solution. Its inputs are the number of 
     * choices for the question and the two answers that are compared (the actual one and the
     * correct one). The result is the score for the question. 
     * Note that the question itself is not needed because this method doesn't have any capabilities
     * for a semantic analysis, it only compares two existing answers of which one is considered
     * to be correct.
     * @param noChoices the total number of choices of the question whose answer is being
     * evaluated (this is needed to instantiate QuestionScore and may be also useful for some 
     * special types of scoring)
     * @param actual the answer that is being evaluated
     * @param correct the answer considered to be correct
     * @return a QuestionScore object that stores the detailed score for the question (i.e. the score for
     * each choice, when this is the case, and the total score for the question)
     */
    public QuestionScore compareAnswers(int noChoices, Answer actual, Answer correct);
}
