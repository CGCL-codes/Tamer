package cn.openlab.thread;

import java.util.Random;

public class Test {

    public static void main(String[] args) {
        Thread t = new MyThread();
        t.start();
        MyThread2 r = new MyThread2();
        Thread t2 = new Thread(r);
        t2.start();
        Random random = new Random();
        random.nextInt(5);
    }
}
