package com.amd.aparapi.test;

public class IfElseNot__OrOr_And_ {

    public void run() {
        int testValue = 10;
        @SuppressWarnings("unused") boolean pass = false;
        if (!((testValue % 2 == 0 || testValue <= 0 || testValue >= 100) && testValue % 4 == 0)) {
            pass = true;
        } else {
            pass = false;
        }
    }
}
