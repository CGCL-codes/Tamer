package com.amd.aparapi.test;

public class PostIncLocalTwice {

    public void run() {
        @SuppressWarnings("unused") boolean pass = false;
        int i = 0;
        if (i++ + i++ == 1) pass = true;
    }
}
