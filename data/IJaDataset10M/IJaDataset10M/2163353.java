package com.wzg.generator;

public class SetModel {

    private String examModelType = "";

    private String examCourse = "";

    private String examTotalPoint = "";

    private String examKnowledgePoint = "";

    private String[] examTypeSelected = new String[9];

    private String[] examPointInput = new String[9];

    private String[] examCountInput = new String[9];

    private String[] easy = new String[9];

    private String[] easyToo = new String[9];

    private String[] midd = new String[9];

    private String[] hard = new String[9];

    private String[] hardToo = new String[9];

    private String[] lowDistinct = new String[9];

    private String[] middDistinct = new String[9];

    private String[] highDistinct = new String[9];

    private String[] lowRequire = new String[9];

    private String[] middRequire = new String[9];

    private String[] highRequire = new String[9];

    public String[] getEasy() {
        return easy;
    }

    public void setEasy(String[] easy) {
        this.easy = easy;
    }

    public String[] getEasyToo() {
        return easyToo;
    }

    public void setEasyToo(String[] easyToo) {
        this.easyToo = easyToo;
    }

    public String[] getExamCountInput() {
        return examCountInput;
    }

    public void setExamCountInput(String[] examCountInput) {
        this.examCountInput = examCountInput;
    }

    public String getExamCourse() {
        return examCourse;
    }

    public void setExamCourse(String examCourse) {
        this.examCourse = examCourse;
    }

    public String getExamKnowledgePoint() {
        return examKnowledgePoint;
    }

    public void setExamKnowledgePoint(String examKnowledgePoint) {
        this.examKnowledgePoint = examKnowledgePoint;
    }

    public String getExamModelType() {
        return examModelType;
    }

    public void setExamModelType(String examModelType) {
        this.examModelType = examModelType;
    }

    public String[] getExamPointInput() {
        return examPointInput;
    }

    public void setExamPointInput(String[] examPointInput) {
        this.examPointInput = examPointInput;
    }

    public String getExamTotalPoint() {
        return examTotalPoint;
    }

    public void setExamTotalPoint(String examTotalPoint) {
        this.examTotalPoint = examTotalPoint;
    }

    public String[] getExamTypeSelected() {
        return examTypeSelected;
    }

    public void setExamTypeSelected(String[] examTypeSelected) {
        this.examTypeSelected = examTypeSelected;
    }

    public String[] getHard() {
        return hard;
    }

    public void setHard(String[] hard) {
        this.hard = hard;
    }

    public String[] getHardToo() {
        return hardToo;
    }

    public void setHardToo(String[] hardToo) {
        this.hardToo = hardToo;
    }

    public String[] getHighDistinct() {
        return highDistinct;
    }

    public void setHighDistinct(String[] highDistinct) {
        this.highDistinct = highDistinct;
    }

    public String[] getHighRequire() {
        return highRequire;
    }

    public void setHighRequire(String[] highRequire) {
        this.highRequire = highRequire;
    }

    public String[] getLowDistinct() {
        return lowDistinct;
    }

    public void setLowDistinct(String[] lowDistinct) {
        this.lowDistinct = lowDistinct;
    }

    public String[] getLowRequire() {
        return lowRequire;
    }

    public void setLowRequire(String[] lowRequire) {
        this.lowRequire = lowRequire;
    }

    public String[] getMidd() {
        return midd;
    }

    public void setMidd(String[] midd) {
        this.midd = midd;
    }

    public String[] getMiddDistinct() {
        return middDistinct;
    }

    public void setMiddDistinct(String[] middDistinct) {
        this.middDistinct = middDistinct;
    }

    public String[] getMiddRequire() {
        return middRequire;
    }

    public void setMiddRequire(String[] middRequire) {
        this.middRequire = middRequire;
    }
}
