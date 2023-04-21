package com.enerjy.analyzer.java.rules.testfiles.T0073;

public class FTest08 {

    class Inner {

        void method(float f) {
        }
    }

    class Derived extends Inner {

        void method(float f) {
            int x = 2;
            super.method(x / 3);
        }
    }
}
