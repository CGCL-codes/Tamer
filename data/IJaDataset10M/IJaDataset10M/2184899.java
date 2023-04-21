package com.atosorigin.nl.jspring2008.buzzword.questions;

import java.io.Serializable;

/**
 * @author Jeroen Benckhuijsen (jeroen.benckhuijsen@gmail.com)
 * 
 */
public class Question implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3675451753501485976L;

    private String question;

    private String answerPicture;

    /**
	 * 
	 */
    public Question() {
        super();
    }

    /**
	 * @return the question
	 */
    public String getQuestion() {
        return question;
    }

    /**
	 * @param question
	 *            the question to set
	 */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
	 * @return the answerPicture
	 */
    public String getAnswerPicture() {
        return answerPicture;
    }

    /**
	 * @param answerPicture
	 *            the answerPicture to set
	 */
    public void setAnswerPicture(String answerPicture) {
        this.answerPicture = answerPicture;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Question: ").append(question).append(" has answer stored in picture ").append(this.answerPicture);
        return builder.toString();
    }
}
