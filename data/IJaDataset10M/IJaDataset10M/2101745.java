package com.dustedpixels.jasmin.unit.math;

import com.dustedpixels.jasmin.unit.Unit;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class ALU32 implements Unit {

    public int IN1;

    public int IN2;

    public int CTRL;

    public int OUT;

    public int OVERFLOW;

    private final int bits;

    private final int mask;

    public ALU32(int bits) {
        this.bits = bits;
        this.mask = (1 << bits) - 1;
    }

    public void updateEndPoints() {
        if ((CTRL & 0x08) == 0) {
            if ((CTRL & 0x04) == 0x00) {
                if ((CTRL & 0x02) == 0) {
                    OUT = IN1 & IN2;
                } else {
                    OUT = IN1 | IN2;
                }
            } else {
                if ((CTRL & 0x02) == 0) {
                    OUT = IN1 ^ IN2;
                } else {
                    OUT = ~(IN1 ^ IN2);
                }
            }
        } else {
            int in1;
            int in2;
            if ((CTRL & 0x02) == 0) {
                in2 = 0;
            } else {
                in2 = IN2;
            }
            if ((CTRL & 0x04) == 0) {
            }
            OUT = IN1 + in2 + (CTRL & 0x01);
        }
    }
}
