package com.ibm.wala.dila.tests.data.callgraph;

public class CallGraphChiantiInnerClasses2 {

    public static void main(String[] args) {
        new A().m2();
    }

    static interface I {

        public void foo();
    }

    static class A {

        public void m1() {
            I i = new I() {

                public void foo() {
                }
            };
            i.foo();
        }

        public void m2() {
            new B() {

                @Override
                public void bar() {
                }
            }.bar();
        }

        public void m3() {
            class TempI implements I {

                public void foo() {
                }
            }
            ;
            new TempI().foo();
        }

        public void m4() {
            class TempB extends B {

                @Override
                public void bar() {
                }
            }
            ;
            B b = new TempB();
            b.bar();
        }
    }

    abstract static class B {

        public abstract void bar();
    }
}
