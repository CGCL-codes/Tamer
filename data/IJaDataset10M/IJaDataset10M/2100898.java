package com.enerjy.analyzer.java.rules.testfiles.T0272;

public class FTest01 {

    public void aMethod() {
        Thread thread = new Thread() {

            public void run() {
                super.run();
            }
        };
        thread.run();
    }
}
