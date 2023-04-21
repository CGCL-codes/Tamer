package tests.signature;

import java.util.*;

public class MethodFailure1 {

    public static String call(A obj) {
        return "A";
    }

    public static String call(I obj) {
        return "I";
    }

    public static interface I {
    }

    public static class A {
    }

    public static class B extends A implements I {
    }

    public static A getA() {
        return new A();
    }

    public static B getB() {
        return new B();
    }

    public static I getI() {
        return new B();
    }
}
