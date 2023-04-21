package com.amd.aparapi.test;

public class ForBooleanToggle {

    public void run() {
        boolean pass = false;
        for (int i = 0; i > 2 && i < 10; i++) {
            pass = !pass;
        }
    }
}
