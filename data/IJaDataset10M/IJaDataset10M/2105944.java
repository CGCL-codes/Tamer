package com.dm;

public class AnonymousInner {

    class A {
    }

    public B test() {
        return new B() {

            private int i = 1;

            public int value() {
                return i;
            }
        };
    }
}

interface B {

    public int value();
}
