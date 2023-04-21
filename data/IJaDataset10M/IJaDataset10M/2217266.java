package com.ibm.wala.dila.tests.data.callgraph;

public class Exception6 {

    public static void main(String[] args) {
        try {
            BB6.bar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class BB6 {

    public static void bar() {
        CC6.zap();
    }
}

class CC6 {

    public static void zap() {
        DD6.zip();
    }
}

class DD6 {

    public static void zip() {
        EE6.foo();
    }
}

class EE6 {

    public static void foo() {
        throw new IllegalArgumentException();
    }
}
