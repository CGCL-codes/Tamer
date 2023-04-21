package org.eledge;

import java.sql.ResultSet;

/**
 * 
 * @author robertz
 * @deprecated use org.eledge.domain.Question instead.
 * 
 */
@Deprecated
public interface IQuestion {

    /***************************************************************************
     * Loads question data from a result set
     */
    public void loadQuestionData(ResultSet rs);

    /***************************************************************************
     * prints the question; used when previewing and taking assessments
     */
    public String print();

    /***************************************************************************
     * This is going to replace the print() call in the assessments (at least
     * for now). The idea is, again, to offload as much work as possible to the
     * question This should make it much easier to add "unconventional" question
     * types like essays, ImageQuestions, etc. At least, that's what I'm hoping. ;)
     */
    public void printForAssessment(PrintAssessmentWrapper wrapper);

    /***************************************************************************
     * prints the question out with myResponse as the appropriate response.
     * (Used in PeerReview...)
     * 
     * @param myResponse
     *            The user response
     */
    public String printEdit(String myResponse);

    /***************************************************************************
     * prints the correct answer, or "answered correctlY" if correct
     */
    public String printCorrection(String myResponse);

    /***************************************************************************
     * Only used in peer review. Prints it out for others to be able to view the
     * question, but not edit the answers
     */
    public String printView(String myResponse);

    /***************************************************************************
     * Prints the question out in a manner appropriate for an instructor edit
     * the question, answers, etc.
     */
    public String edit();

    /***************************************************************************
     * returns the QuestionType of the currrent question
     */
    public int getQuestionType();

    public String getQuestionType(int type);

    /***************************************************************************
     * returns the ID of the current question
     */
    public String getID();

    /***************************************************************************
     * determines the correctness of a user supplied answer.
     * 
     * @param answer
     *            String value of the user's answer
     */
    public boolean isCorrect(String answer);

    /**
     * returns the integer point value for a question
     */
    public int getPointValue();

    /**
     * returns whether or not the question has been graded
     */
    public boolean getQuestionGraded();

    /**
     * returns the correct answer for a question
     */
    public String getCorrectAnswer();

    /***************************************************************************
     * 
     * Let's let question grading be the responsibility of the question, and NOT
     * the exam. Hrm. But... for /that/ one... what exactly are we going to
     * need? And what is going to be represented?? Preferrably, the HTTP request
     * associated with the question would NOT be passed as an argument becase
     * that ties the question to HTTP servlet level, which is something we DON'T
     * want to have happen.
     * 
     */
    public void gradeQuestion(String answer, AssessmentWrapper wrapper);

    /***************************************************************************
     * assignment type property. Eg: exam, quiz, etc.,etc., etc. This property
     * is NOT persisted; questions should be able to pulled from a generic test
     * bank. The question doesn't care, except for some questions which may need
     * to know this to store infomration/save stuff/etc in appropriate places.
     */
    public void setAssignmentType(String at);

    public void setCode(int code);

    public String getAssignmentType();

    public String updateQuestion(String choiceA, String choiceB, String choiceC, String choiceD, String choiceE, String answer1[], String answer2[], String qtext, String numChoices, String assignmentNum, String subjectA, String pointV, String questionTag, String precision, String section);

    public void newQuestionForm(StringBuffer buf, int assignmentNum);

    public void appendAddQuestionSql(StringBuffer sqlInsertString, String assignmentNum, String subjectA, int pointV, String qtext, String section, String choiceA, String choiceB, String choiceC, String choiceD, String choiceE, String[] answer1, String[] answer2, String units, String precision, String qTag, int numChoices);
}
