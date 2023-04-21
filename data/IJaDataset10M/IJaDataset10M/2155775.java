package edu.java.homework.hw07.test0606b;

class A {

    int a = 1;
}

class B extends A {

    int a = 2;
}

class C extends B {

    int a = 3;
}

public class Test0606b {

    public static void main(String args[]) {
        C c = new C();
        System.out.println(c.a);
        System.out.println(((B) c).a);
        System.out.println(((A) c).a);
    }
}
