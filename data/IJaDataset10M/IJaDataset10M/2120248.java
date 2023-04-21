package com.ibm.wala.dila.tests.data.callgraph;

public class CallGraph1H {

    public static void main(String[] args) {
        A a = new B();
        a.zap(new String[] { "hah!", "ho" });
    }

    static class A {

        void zap(String[] s) {
        }
    }

    static class B extends A {

        @Override
        void zap(String[] s) {
        }
    }
}
