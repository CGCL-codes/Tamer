package chapter5;

public class StaticOverrideTest {

    public static void main(String[] args) {
        B b = new B();
    }
}

class A {

    static void runme() {
        System.out.println("runme from class A");
    }
}

class B extends A {

    void runme() {
    }
}
