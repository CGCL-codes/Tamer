package com.enerjy.analyzer.java.rules.testfiles.T0272;

public class PTest02 {

    public void aMethod() {
        Thread thread = new Thread();
        thread.run();
    }

    public static class Thread {

        public void run() {
        }
    }
}
