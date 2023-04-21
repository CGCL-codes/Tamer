package com.ibm.wala.dila.tests.data.callgraph;

public class CallGraph2 {

    public static void main(String[] args) {
        A2 a = new A2();
        a.zap();
    }
}

class A2 {

    void zap() {
    }

    static void foo() {
    }

    static {
        foo();
    }
}
