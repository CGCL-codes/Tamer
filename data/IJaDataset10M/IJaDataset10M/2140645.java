package com.ibm.wala.dila.tests.data.callgraph;

public class CallGraph1K {

    public static void main(String[] args) {
        A a = new B();
        a.zap(new String[] { "foo", "bar" }, 12);
    }

    static class A {

        void zap(String[] s, int i) {
        }
    }

    static class B extends A {

        @Override
        void zap(String[] s, int i) {
        }
    }
}
