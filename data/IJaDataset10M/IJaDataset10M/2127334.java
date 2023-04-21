package edu.java.homework.hw07.test0607;

class A {

    int max(int x, int y) {
        System.out.println("A.max()");
        if (x > y) return x; else return y;
    }
}

class B extends A {

    int max(int x, int y) {
        System.out.println("B.max()");
        return super.max(x, y) - 10;
    }
}

class C extends B {

    int max(int x, int y) {
        System.out.println("C.max()");
        return super.max(x, y) + 10;
    }
}

public class Test0607 {

    public static void main(String[] args) {
        C c = new C();
        B b = new B();
        A a = new A();
        System.out.println(((A) c).max(13, 29));
        System.out.println();
        System.out.println(b.max(13, 29));
        System.out.println();
        System.out.println(a.max(13, 29));
    }
}
