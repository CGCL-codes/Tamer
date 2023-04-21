package com.dukesoftware.utils.thread.multi.sync;

import com.dukesoftware.utils.thread.multi.sync.IJoinedTask;

public class CounterTask implements IJoinedTask {

    private volatile int counter;

    private final int max;

    public CounterTask(int max) {
        this.max = max;
    }

    @Override
    public boolean execute() {
        if (counter >= max) {
            return true;
        }
        counter++;
        return false;
    }
}
