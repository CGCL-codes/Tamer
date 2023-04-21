package com.amd.aparapi.test;

public class MultipleAssign {

    public void run() {
        @SuppressWarnings("unused") int a = 0;
        @SuppressWarnings("unused") int b = 0;
        @SuppressWarnings("unused") int c = 0;
        a = b = c = 4;
    }
}
