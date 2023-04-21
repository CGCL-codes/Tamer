package org.wf.lab.grammar.annotation;

public class Foo {

    @Test
    public static void m1() {
        System.out.println("m1");
    }

    public static void m2() {
    }

    @Test
    public static void m3() {
        throw new RuntimeException("m3");
    }

    public static void m4() {
    }

    @Test
    public static void m5() {
        System.out.println("m5");
    }

    public static void m6() {
    }

    @Test
    public static void m7() {
        throw new RuntimeException("m7");
    }

    public static void m8() {
    }
}
