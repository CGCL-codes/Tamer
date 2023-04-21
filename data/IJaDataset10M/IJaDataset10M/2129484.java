package com.dustedpixels.jasmin.unit.math;

import com.dustedpixels.jasmin.unit.Unit;

/**
 * Checks if input is zero.
 * 
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class ZeroChecker32 implements Unit {

    public int IN;

    public int OUT;

    public void updateEndPoints() {
        OUT = (IN == 0) ? 0 : 1;
    }
}
