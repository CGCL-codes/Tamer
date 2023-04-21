package com.ch.speed.bean;

public class QuestionAnswerBean {

    private ReadTestBean readTest;

    private LevelBean level;

    private Long displayFactor;

    private String answer;

    private Long point;

    public QuestionAnswerBean(ReadTestBean readTest, LevelBean level, Long displayFactor) {
        this.readTest = readTest;
        this.level = level;
        this.displayFactor = displayFactor;
    }

    public ReadTestBean getReadTest() {
        return readTest;
    }

    public LevelBean getLevel() {
        return level;
    }

    public Long getDisplayFactor() {
        return displayFactor;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isMatch() {
        if (readTest.getTestText().equalsIgnoreCase(answer)) {
            return true;
        }
        return false;
    }

    public Long getDisplayTotal() {
        if (displayFactor != null && readTest != null && readTest.getSpeedDisplay() != null) {
            return new Long(displayFactor.longValue() * readTest.getSpeedDisplay().longValue());
        }
        return new Long(3000);
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
}
