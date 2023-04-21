package se.jayway.millionaire.dao;

/**
 * Represents the solution consisting of a question and the correct answer.
 */
public class Solution {

    private final String question;

    private final String correctAnswer;

    private final String[] answerAlternatives;

    public Solution(String question, String[] answerAlternatives, String correctAnswer) {
        this.question = question;
        this.answerAlternatives = answerAlternatives;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getAnswerAlternatives() {
        return answerAlternatives;
    }
}
