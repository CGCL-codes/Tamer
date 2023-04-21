package edu.java.lectures.lec11.threads.basics;

import edu.java.lectures.lec11.threads.basics.creation.MyRunnable;

public class ThreadStaticMethodsTest {

    public static void main(String[] args) {
        MyRunnable task = new MyRunnable("task");
        Thread thread = new Thread(task, "myThread");
        thread.start();
        thread.interrupt();
    }
}
