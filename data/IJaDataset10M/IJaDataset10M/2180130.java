package com.dukesoftware.utils.thread;

import com.dukesoftware.utils.thread.pattern.workerthread.Channel;

/**
 * 
 * 
 * 
 * 
 *
 *
 */
public class SwitchRequestChannel extends Channel<Data> {

    private final Data[] data;

    private int counter = 0;

    public SwitchRequestChannel(int size) {
        super(size);
        data = new Data[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = new Data();
        }
    }

    public void update() {
        data[counter].prepare();
        put(data[counter]);
        counter = (counter + 1) % data.length;
    }
}
