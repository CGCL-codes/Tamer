package com.dm.thread;

class MyThread extends Thread {

    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("say hello by thread!");
        }
    }
}

class MyThread1 implements Runnable {

    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("say hello by thread implements Runnable!");
        }
    }
}

public class TestThread {

    /**
	 * @param args
	 * @throws InterruptedException
	 */
    public static void main(String[] args) throws InterruptedException {
        MyThread1 m = new MyThread1();
        MyThread mt = new MyThread();
        mt.start();
        Thread mt1 = new Thread(m);
        mt1.start();
        mt1.join();
        System.out.println("say hello by main!");
    }
}
