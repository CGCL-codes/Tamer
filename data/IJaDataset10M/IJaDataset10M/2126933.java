package com.enerjy.analyzer.java.rules.testfiles.T0125;

public class FTest2 {

    void fail() {
        Again: while (true) {
            continue Again;
        }
    }
}
