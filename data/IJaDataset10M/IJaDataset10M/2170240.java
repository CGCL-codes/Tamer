package com.imi.threads;

public class JLock {

    String lock = new String("");

    boolean pass = false;

    public synchronized void lock() {
        pass = false;
    }

    public synchronized void unlock() {
        try {
            pass = true;
            lock.notifyAll();
        } catch (Exception ex) {
        }
    }

    public void canPass(long time) {
        if (!pass) {
            try {
                lock.wait(time);
            } catch (Exception e) {
            }
        }
    }
}
