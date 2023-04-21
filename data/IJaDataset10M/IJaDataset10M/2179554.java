package com.ibm.wala.dila.tests.data.hierarchy;

public class H8 {

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            bar(new A());
        }
        bar(new B());
        bar(new C());
    }

    static void bar(A a) {
        a.foo();
    }

    static class A {

        public void foo() {
        }
    }

    static class B extends A {

        @Override
        public void foo() {
        }
    }

    static class C extends A {

        @Override
        public void foo() {
        }
    }
}
