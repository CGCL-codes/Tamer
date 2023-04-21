package com.enerjy.analyzer.java.rules.testfiles.T0029;

class PTest3 {

    private void theMethod() {
    }

    public void publicMethod() {
        PTest3 other = new PTest3();
        other.theMethod();
    }
}
