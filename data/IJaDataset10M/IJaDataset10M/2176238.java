package com.enerjy.analyzer.java.rules.testfiles.T0283;

public class FTest10 {

    public void aMethod() {
        Object control = new Object();
        while (control != null) {
            System.out.println(control);
        }
    }
}
