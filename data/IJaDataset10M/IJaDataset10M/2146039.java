package ij.plugin;

import java.awt.*;
import ij.*;

/**ImageJ plugin for measuring the speed of various Java operations.*/
public class Timer implements PlugIn {

    int j = 0;

    long startTime, nullLoopTime;

    int numLoops;

    public void run(String arg) {
        int j = 0, k;
        int[] a = new int[10];
        long endTime;
        numLoops = 10000;
        do {
            numLoops = (int) (numLoops * 1.33);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < numLoops; i++) {
            }
            nullLoopTime = System.currentTimeMillis() - startTime;
        } while (nullLoopTime < 250);
        IJ.write("");
        IJ.write("Timer: " + numLoops + " iterations (" + nullLoopTime + "ms)");
        Timer2 o = new Timer2();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
        }
        showTime("null loop");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = o.getJ();
        }
        showTime("i=o.getJ()");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = o.getJFinal();
        }
        showTime("i=o.getJ() (final)");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = o.getJClass();
        }
        showTime("i=o.getJ() (static)");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = o.j;
        }
        showTime("i=o.j");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = Timer2.k;
        }
        showTime("i=o.j (static)");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = j;
        }
        showTime("i=j");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            k = a[j];
        }
        showTime("i=a[j]");
    }

    void showTime(String s) {
        long elapsedTime = System.currentTimeMillis() - startTime - nullLoopTime;
        IJ.write("  " + s + ": " + (elapsedTime * 1000000) / numLoops + " ns");
    }
}

class Timer2 {

    int j = 0;

    static int k = 0;

    public int getJ() {
        return j;
    }

    public final int getJFinal() {
        return j;
    }

    public static int getJClass() {
        return k;
    }
}
