package com.xxx.test1.thread;

import java.io.IOException;

public class ThreadTest {

    static Object str1 = new Object();

    static Object str2 = new Object();

    public static void main(String[] args) throws InterruptedException, IOException {
        ThreadA a = new ThreadA();
        ThreadB b = new ThreadB();
        Thread t1 = new Thread(a);
        Thread t2 = new Thread(b);
        t1.start();
        t2.start();
    }
}

class ThreadA implements Runnable {

    int i = 0;

    @Override
    public void run() {
        synchronized (ThreadTest.str1) {
            try {
                System.out.println("A�õ�STR1����Ȼ����Ϣ5��");
                Thread.sleep(5000);
                System.out.println("A�ȴ�STR2����");
                synchronized (ThreadTest.str2) {
                    System.out.println("A�õ�str2����");
                    System.out.println("Aִ�����");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ThreadB implements Runnable {

    int i = 0;

    public void run() {
        synchronized (ThreadTest.str2) {
            try {
                System.out.println("B�õ�STR2����Ȼ����Ϣ5��");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B�ȴ�STR1����");
            {
                System.out.println("b�õ�str1����");
                System.out.println("Bִ�����");
            }
        }
    }
}
