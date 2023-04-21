package com.google.code.fuzzops.webfuzzer.controller;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadWatcher implements Runnable {

    HashSet<Thread> set;

    ConcurrentLinkedQueue<Thread> queue;

    boolean flag;

    public ThreadWatcher(HashSet<Thread> set, ConcurrentLinkedQueue<Thread> queue) {
        this.set = set;
        this.queue = queue;
        flag = true;
    }

    public void toggleFlag() {
        flag = !flag;
    }

    public void run() {
        while (flag) {
            while (set.size() < 7 && queue.size() > 0) {
                try {
                    if (queue.peek() != null) {
                        Thread nextFuzz = queue.remove();
                        nextFuzz.start();
                        set.add(nextFuzz);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to add to set from queue");
                }
            }
        }
    }
}
