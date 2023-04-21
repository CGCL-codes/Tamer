package com.peterhi.quiz;

import java.io.Serializable;

public class MCQ extends Question implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3025778700081965889L;

    private String text;

    private String[] choices;

    private char[] answer;

    private boolean multi;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public char[] getAnswer() {
        return answer;
    }

    public void setAnswer(char[] answer) {
        this.answer = answer;
    }

    public boolean isMulti() {
        return multi;
    }

    public void setMulti(boolean multi) {
        this.multi = multi;
    }
}
