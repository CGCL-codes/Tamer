package com.enerjy.analyzer.java.rules.testfiles.T0175;

public class FTest2 {

    void theMethod() {
        @SuppressWarnings("unused") long l;
        l = 3;
        if (System.currentTimeMillis() < 30) {
            l = 4;
        } else {
            l = 5;
        }
    }
}
