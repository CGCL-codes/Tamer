package com.gwt.tirso.gymkhana.client;

import java.io.Serializable;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Question implements Serializable {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private String id;

    @Persistent
    private int num;

    @Persistent
    private String questionText;

    @Persistent
    private String answerText;

    @Persistent
    private String clueText;

    @Persistent
    private int time;

    @Persistent
    private int points = 10;

    @Persistent
    private String code;

    @Persistent
    private boolean isInitial;

    @Persistent
    private String gymkId;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public String getClueText() {
        return clueText;
    }

    public void setClueText(String clueText) {
        this.clueText = clueText;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String codeT) {
        this.code = codeT;
    }

    public boolean isInitial() {
        return isInitial;
    }

    public void setInitial(boolean isInitial) {
        this.isInitial = isInitial;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setGymkId(String gymkId) {
        this.gymkId = gymkId;
    }

    public String getGymkId() {
        return gymkId;
    }
}
