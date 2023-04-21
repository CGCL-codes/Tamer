package org.furthurnet.datastructures;

public class ThrottleManager {

    private static long currentSec = -1;

    private static long bitsSent = 0;

    private static long maxBitsThisQuantum;

    private static long maxBitsLastQuantum = 0;

    private static double toleranceFactor = 1.01;

    private static double currentUpstream = 0;

    public static double getCurrentUpstream() {
        return currentUpstream / 1000;
    }

    public static void throttle(int numBits, int maxBits) {
        while (!okToWrite(numBits, maxBits)) {
            try {
                Thread.sleep(getWaitTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void control(int numBits) {
        bitsSent += numBits;
    }

    private static synchronized boolean okToWrite(int numBits, int maxBits) {
        if (maxBits <= 0) return true;
        long oldSec;
        long currTime = System.currentTimeMillis();
        if ((currentSec < 0) || (currTime > currentSec + 1000)) {
            oldSec = currentSec;
            currentSec = currTime;
            if (oldSec < 0) {
                maxBitsLastQuantum = maxBits;
                maxBitsThisQuantum = maxBits;
            } else {
                maxBitsLastQuantum = maxBitsThisQuantum;
                maxBitsThisQuantum = ((currentSec - oldSec) * maxBits) / 1000;
            }
            currentUpstream = (double) bitsSent / (double) maxBitsLastQuantum * (double) maxBits;
            bitsSent -= maxBitsLastQuantum;
            if (bitsSent < 0) bitsSent = 0;
        }
        if (numBits > maxBitsThisQuantum) {
            return false;
        }
        if ((double) (bitsSent + numBits) >= (maxBitsThisQuantum * toleranceFactor)) return false; else {
            bitsSent += numBits;
            return true;
        }
    }

    private static synchronized int getWaitTime() {
        long waitTime = 1000 - (System.currentTimeMillis() - currentSec);
        if (waitTime < 0) return 0;
        return (int) waitTime;
    }
}
