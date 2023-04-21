package com.trackerdogs.ui.servlet;

import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.*;

/**
 *
 *
 * Nescecerry because program ends before all threads are finished
 */
public class ThreadWaiter extends Thread {

    ThreadWaiter() {
    }

    void waitOnThread(Thread thr) {
        try {
            while (thr.isAlive()) {
                this.sleep(1000);
            }
        } catch (InterruptedException ex) {
            System.out.println("error in ThreadWaiter.waitOnThread()");
        }
    }
}
