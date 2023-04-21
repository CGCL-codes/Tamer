package com.chungco.core;

/**
 * An triple
 * 
 * @author Marc Chung <mchung@gmail.com>
 * @param <X>
 * @param <Y>
 * @param <Z>
 */
public class Triple<X, Y, Z> {

    private final X mx;

    private final Y my;

    private final Z mz;

    public Triple(final X mx, final Y my, final Z mz) {
        this.mx = mx;
        this.my = my;
        this.mz = mz;
    }

    public X getX() {
        return mx;
    }

    public Y getY() {
        return my;
    }

    public Z getZ() {
        return mz;
    }
}
