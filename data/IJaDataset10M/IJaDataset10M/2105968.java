package com.pz.net.test;

import com.pz.util.Log;

/**
 *
 * @author jannek
 */
public class TimeSpeedTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        a();
        b();
        c();
        long start = System.nanoTime();
        a();
        long mid = System.nanoTime();
        b();
        long end = System.nanoTime();
        c();
        long test = System.nanoTime();
        Log.d("ms: " + (mid - start));
        Log.d("ns: " + (end - mid));
        Log.d("..: " + (test - end));
    }

    public static void a() {
        for (int i = 0; i < 100000; i++) {
            System.currentTimeMillis();
        }
    }

    public static void b() {
        for (int i = 0; i < 100000; i++) {
            System.nanoTime();
        }
    }

    public static void c() {
        int x;
        for (int i = 0; i < 100000; i++) {
            x = i;
        }
    }
}
