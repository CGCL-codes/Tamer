package com.exm.chp06;

/**
 *
 * @author Supervisor
 */
public class Outer {

    private static int counter;

    public int x;

    private int y;

    static {
        counter = 0;
    }

    public Outer(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void doit() {
        System.out.println("doit method");
        Inner in = new Inner();
        System.out.println("Subtract x - y: " + in.sub());
        System.out.println("Inner a: " + in.a + " b: " + in.b);
    }

    class Inner {

        public int a;

        private int b;

        public int sub() {
            return (x - y);
        }
    }
}
