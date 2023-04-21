package com.amd.aparapi.test;

public class ForAnd {

    public void run() {
        @SuppressWarnings("unused") boolean pass = false;
        for (int i = 0; i > 2 && i < 10; i++) {
            pass = true;
        }
    }
}
