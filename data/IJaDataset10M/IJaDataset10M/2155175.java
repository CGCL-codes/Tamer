package cn.openlab.thread;

public class Counter {

    private int count;

    public int getCount() {
        return count;
    }

    public synchronized void increase() {
        count++;
    }
}
