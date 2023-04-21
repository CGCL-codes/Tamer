package com.enerjy.analyzer.java.rules.testfiles.T0130;

@SuppressWarnings("all")
public class PTest33 {

    private static int x = 0;

    public static class Inner extends PTest33 {

        void fail() {
            super.x++;
        }
    }
}
