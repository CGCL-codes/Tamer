package com.hs.test.thread;

public class ThreadUserRunnable implements Runnable {

    Thread thread2 = new Thread(this);

    public void run() {
        System.out.println("我是实现Runnable接口的线程！");
        System.out.println("我将挂起一秒！");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return;
        }
    }
}
