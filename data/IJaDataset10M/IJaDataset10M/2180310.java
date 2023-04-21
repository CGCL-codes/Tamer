package com.java.laba5;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Diary implements Comparable<Diary> {

    private Date diaryDay;

    private String note;

    public Date getDiaryDay() {
        return diaryDay;
    }

    public void setDiaryDay(Date diaryDay) {
        this.diaryDay = diaryDay;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return "Diary [diaryDay = " + formatter.format(diaryDay) + ", note = " + note + "]";
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Diary(Date diaryDay, String note) {
        this.diaryDay = diaryDay;
        this.note = note;
    }

    public int compareTo(Diary d) {
        int result = this.diaryDay.compareTo(d.diaryDay);
        return result;
    }
}
