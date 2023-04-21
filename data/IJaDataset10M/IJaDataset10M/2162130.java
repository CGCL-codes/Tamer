package de.jassda.util.test;

public class TestClass {

    public static void main(String[] args) {
        System.out.println("TEST_CLASS - MAIN:START");
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        TestClass tmp = new TestClass();
        int result = tmp.mul(a, b);
        System.out.println(a + "*" + b + "=" + result);
    }

    public int mul(int a, int b) {
        try {
            System.out.println("TEST_CLASS - MUL:START");
            if (a == 0) return 0;
            if (a == 1) return b;
            return b + mul(decrement(a), b);
        } catch (Exception e) {
            return -1;
        }
    }

    public int decrement(int a) throws Exception {
        if (true) throw new Exception("ba");
        return a - 1;
    }
}
