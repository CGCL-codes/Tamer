package com.googlecode.usc.folder.compression.utils;

/**
 *
 * @author ShunLi
 */
public class PrintTimerTask extends java.util.TimerTask {

    int count;

    @Override
    public void run() {
        System.out.print("·");
        if ((++count) % 60 == 0) {
            System.out.println();
        }
    }
}
