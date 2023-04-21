package com.enerjy.analyzer.java.rules.testfiles.T0096;

@SuppressWarnings("all")
public class PTest {

    int wontBeHidden;

    private static class Inner {

        int willBeHidden;
    }
}
