package org.susan.java.classes;

class Demo {

    int i;
}

public class DemoTester extends Demo {

    public void run(Demo a) {
        int x = a.i;
        int y = i;
        System.out.println(x);
        System.out.println(y);
    }

    public static void main(String args[]) {
        Demo a = new Demo();
        new DemoTester().run(a);
    }
}
