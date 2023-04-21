package com.dustedpixels.jasmin.chips;

/**
 * Byte resistor.
 * 
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class ByteResistor implements Chip {

    public byte D1 = 0;

    public boolean D1_ACTIVE = false;

    public byte D2 = 0;

    public boolean D2_ACTIVE = false;

    public void update() {
        if (D1_ACTIVE) {
            if (D2_ACTIVE) {
            } else {
                D2 = D1;
                D2_ACTIVE = true;
            }
        } else {
            if (D2_ACTIVE) {
                D1 = D2;
                D1_ACTIVE = D2_ACTIVE;
            } else {
            }
        }
    }
}
