package com.enerjy.analyzer.java.rules.testfiles.T0025;

class FTest2 {

    static class Base {

        void theMethod() {
        }
    }

    static class Middle extends Base {
    }

    static class Derived extends Middle {

        void theMethod() {
        }
    }
}
