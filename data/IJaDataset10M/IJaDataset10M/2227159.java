package org.thaispec.demo;

import java.util.ArrayList;
import org.thaispec.Thai;

@Thai("คนแขวนคอ")
public class Hangman {

    @Thai("คำ")
    private String word;

    @Thai("ผิด")
    private int wrong;

    @Thai("เดามากสุด")
    private int maxguess;

    @Thai("คำซ่อน")
    private ArrayList hiddenWords;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    @Thai("ลอง")
    public void doTry(String a) {
    }

    @Thai("เริ่มเกมใหม่")
    public void newGame() {
    }
}
