package edu.webteach.test.quizz;

import java.util.*;
import edu.webteach.data.*;

public class Quizz2 {

    public Quizz2(String title, List<Question> questions, int portionSize) {
        this.portionSize = portionSize;
        this.title = title;
        this.questions = questions;
        this.questionsLeft = questions.size();
        for (Question question : questions) {
            passed.put(question, false);
        }
    }

    protected int portionSize = 10;

    protected List<Question> lastPortion = null;

    protected int questionsLeft = 0;

    protected int marker = 0;

    protected String title;

    protected List<Question> questions;

    protected Map<Question, Boolean> passed = new HashMap<Question, Boolean>();

    protected Map<Question, Boolean> correct = new HashMap<Question, Boolean>();

    protected Map<Question, String> htmlView = new HashMap<Question, String>();

    protected Map<Question, History> histories = new HashMap<Question, History>();

    public String getTitle() {
        return title;
    }

    public int getPortionSize() {
        return portionSize;
    }

    public List<Question> getLastPortion() {
        return lastPortion;
    }

    public List<Question> getNextPortion() {
        lastPortion = getPortion(portionSize);
        return lastPortion;
    }

    protected List<Question> getPortion(int portionSize) {
        if (portionSize <= 0) return questions;
        List<Question> portion = new ArrayList<Question>(portionSize);
        while (portion.size() != portionSize && portion.size() < questionsLeft) {
            if (!isPassed(questions.get(marker))) portion.add(questions.get(marker));
            if (marker < questions.size() - 1) marker++; else marker = 0;
        }
        return portion;
    }

    public Question getQuestion(int i) {
        return questions.get(i);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int size() {
        return questions.size();
    }

    public boolean isQuizzPassed() {
        return (questionsLeft == 0);
    }

    public boolean isPassed(Question question) {
        return passed.get(question);
    }

    public void setPassed(Question question) {
        if (!isPassed(question)) {
            questionsLeft--;
            this.passed.put(question, true);
        }
    }

    public boolean getCorrect(Question question) {
        return correct.get(question);
    }

    public void setCorrect(Question question, boolean correctness) {
        setPassed(question);
        this.correct.put(question, correctness);
    }

    public String getHtmlView(Question question) {
        return htmlView.get(question);
    }

    public void setHtmlView(Question question, String htmlView) {
        this.htmlView.put(question, htmlView);
    }

    public History getHistory(Question question) {
        return histories.get(question);
    }

    public void setHistory(Question question, History history) {
        this.histories.put(question, history);
    }

    @Override
    public String toString() {
        String s = "";
        s += "Title: " + title + "\n";
        s += "Marker: " + marker + "\n";
        s += "QuestionsLeft: " + questionsLeft + "\n";
        return s;
    }
}
