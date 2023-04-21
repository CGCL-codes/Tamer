package com.timothy.chart;

public class Pie2D extends BaseChart {

    private int tlStartX;

    private int tlStartY;

    private int ovalStartX;

    private int ovalStartY;

    private int ovalHeight;

    private int ovalWidth;

    private float startAngle;

    private float sweepAngle;

    public Pie2D() {
    }

    public int getTlStartX() {
        return tlStartX;
    }

    public void setTlStartX(int tlStartX) {
        this.tlStartX = tlStartX;
    }

    public int getTlStartY() {
        return tlStartY;
    }

    public void setTlStartY(int tlStartY) {
        this.tlStartY = tlStartY;
    }

    public int getOvalStartX() {
        return ovalStartX;
    }

    public void setOvalStartX(int ovalStartX) {
        this.ovalStartX = ovalStartX;
    }

    public int getOvalStartY() {
        return ovalStartY;
    }

    public void setOvalStartY(int ovalStartY) {
        this.ovalStartY = ovalStartY;
    }

    public int getOvalHeight() {
        return ovalHeight;
    }

    public void setOvalHeight(int ovalHeight) {
        this.ovalHeight = ovalHeight;
    }

    public int getOvalWidth() {
        return ovalWidth;
    }

    public void setOvalWidth(int ovalWidth) {
        this.ovalWidth = ovalWidth;
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
    }

    public float getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(float sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    @Override
    public String toString() {
        return "Pie2D [tlStartX=" + tlStartX + ", tlStartY=" + tlStartY + ", ovalStartX=" + ovalStartX + ", ovalStartY=" + ovalStartY + ", ovalHeight=" + ovalHeight + ", ovalWidth=" + ovalWidth + ", startAngle=" + startAngle + ", sweepAngle=" + sweepAngle + ", getIndex()=" + getIndex() + ", getVotes()=" + getVotes() + ", getShare()=" + getShare() + ", getFullColor()=" + getFullColor() + ", getChoiceText()=" + getChoiceText() + ", getChoiceTextSize()=" + getChoiceTextSize() + ", getChoiceTextColor()=" + getChoiceTextColor() + ", getChoiceTextOrdinateX()=" + getChoiceTextOrdinateX() + ", getChoiceTextOrdinateY()=" + getChoiceTextOrdinateY() + ", getAnswerType()=" + getAnswerType() + ", getAnswerText()=" + getAnswerText() + ", getAnswerTextSize()=" + getAnswerTextSize() + ", getAnswerTextColor()=" + getAnswerTextColor() + ", getAnswerTextOrdinateX()=" + getAnswerTextOrdinateX() + ", getAnswerTextOrdinateY()=" + getAnswerTextOrdinateY() + "]";
    }
}
