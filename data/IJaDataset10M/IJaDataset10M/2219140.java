package com.amd.aparapi.test;

public class AndOrPrecedence {

    public void run() {
        boolean a = true;
        boolean b = false;
        boolean c = false;
        @SuppressWarnings("unused") boolean pass = false;
        if (a || b && c) {
            pass = true;
        }
    }
}
