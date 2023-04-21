package org.retro.gis.tools;

/**
 * This class does nothing, it is used with the spiritbot-release
 * scripts.
 * 
 * @author Berlin Brown
 * 
 */
public class NullSleep extends Thread {

    private static final int maxSleep = 12;

    private int theSleep = 4;

    public NullSleep() {
        theSleep = maxSleep;
    }

    public NullSleep(int v) {
        theSleep = v;
    }

    public void run() {
        int i = 0;
        try {
            for (i = 0; i < theSleep; i++) {
                sleep(1000);
                System.out.println("..... waiting-client-launch ... [ in " + (maxSleep - i) + " ] ");
            }
        } catch (InterruptedException e) {
            System.err.println("..... Interrupted .." + e.getMessage());
        }
    }

    public static void main(String[] args) {
        NullSleep _sleeper;
        if (args.length > 0) {
            _sleeper = new NullSleep(Integer.parseInt(args[0]));
        } else {
            _sleeper = new NullSleep();
        }
        _sleeper.start();
    }
}
