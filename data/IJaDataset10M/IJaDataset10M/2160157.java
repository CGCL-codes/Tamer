package com.enerjy.analyzer.java.rules.testfiles.T0165;

@SuppressWarnings("all")
public class FTest2 {

    int theMethod() {
        try {
            try {
                return 1;
            } finally {
            }
        } catch (Exception e) {
        } finally {
            return 3;
        }
    }
}
