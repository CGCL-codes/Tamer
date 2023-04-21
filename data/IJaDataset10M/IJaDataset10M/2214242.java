package com.pavisoft.istudy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Robert Morris
 * 
 * Creates new Flashcard, capable of holding "question"
 * and one or more "answers"
 */
public class Flashcard implements Comparable<Flashcard> {

    private String question;

    private ArrayList<String> answers = new ArrayList<String>();

    private boolean askConfirmation = false;

    public Flashcard() {
        this("", "");
    }

    public Flashcard(String question, String answer) {
        this(question, new String[] { answer });
    }

    public Flashcard(String question, String[] answers) {
        this.question = question;
        this.answers = new ArrayList<String>(answers.length);
        for (String a : answers) this.answers.add(a);
    }

    /** Sets whether or not, when presented to the user, this flaschard
     * should always ask the user afterwards if he or she answered the 
     * question correctly (it is recommended that both the user's answer
     * and the primary answer of the flashcard be shown for comparison).
     * This option should be used when answers may vary and human determination
     * is necessary. The default is <code>false</code>.
     * @param ask If <code>true</code>, will ask; if <code>false</code>, will not.
     */
    public void setAskConfirmation(boolean ask) {
        this.askConfirmation = ask;
    }

    /** Gets whether or not, when presented to the user, this flaschard
     * should always ask the user afterwards if he or she answered the 
     * question correctly (it is recommended that both the user's answer
     * and the primary answer of the flashcard be shown for comparison).
     * This default is <code>false</code>.
     * @returns<code>true</code> if should ask; <code>false</code> if not.
     */
    public boolean getAskConfirmation() {
        return this.askConfirmation;
    }

    public void addAnswer(String answer) {
        this.answers.add(answer);
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return this.answers;
    }

    public String getPrimaryAnswer() {
        if (answers.size() > 0) return this.answers.get(0);
        return "";
    }

    public void setPrimaryAnswer(String primaryAnswer) {
        this.answers.set(0, primaryAnswer);
    }

    public void setAnswers(List<String> answers) {
        this.answers = (ArrayList<String>) answers;
    }

    public void setSecondaryAnswers(String[] answers) {
        String primaryAnswer = this.getPrimaryAnswer();
        this.answers.clear();
        this.answers.add(primaryAnswer);
        this.answers.addAll(java.util.Arrays.asList(answers));
    }

    public List<String> getSecondaryAnswers() {
        List<String> ret = new ArrayList<String>();
        for (int i = 1; i < answers.size(); i++) {
            ret.add(answers.get(i));
        }
        return ret;
    }

    /** Searches through all answers and removes the answer matching the
     * String <code>answer</code>.
     * @param answer The string to match
     * @return <code>true</code> if found and successfully removed; otherwise,
     * <code>false</code>.
     */
    public boolean removeAnswerByString(String answer) {
        return this.answers.remove(answer);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Flashcard)) return false;
        Flashcard f = (Flashcard) obj;
        return f.question.equals(this.question);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.question != null ? this.question.hashCode() : 0;
        return hash;
    }

    public int compareTo(Flashcard f) {
        return this.question.compareTo(f.question);
    }
}
