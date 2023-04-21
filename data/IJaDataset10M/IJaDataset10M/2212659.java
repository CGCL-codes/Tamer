package com.enerjy.analyzer.java.rules.testfiles.T0130;

public class FTest09 {

    private int x = 0;

    void fail() {
        new FTest09().x++;
    }
}
