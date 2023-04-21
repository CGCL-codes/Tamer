package com.dustedpixels.jasmin.chips;

/**
 * @author micapolos@gmail.com (Michal Pociecha-Los)
 */
public final class Incrementor implements Chip {

    public int ADDRESS = 0;

    public boolean ADDRESS_ACTIVE = false;

    public byte DATA = 0;

    public boolean DATA_ACTIVE = false;

    public boolean WR = false;

    public boolean WR_ACTIVE = false;

    public boolean REQ = false;

    public boolean REQ_ACTIVE = false;

    private int address = 0;

    private boolean read = false;

    private int counter;

    public Incrementor(boolean runFirst) {
        counter = runFirst ? 0 : 2;
    }

    public void update() {
        if ((counter++ & 0x02) == 0) {
            if (read) {
                ADDRESS_ACTIVE = true;
                ADDRESS = address;
                DATA_ACTIVE = false;
                WR_ACTIVE = true;
                WR = false;
                REQ_ACTIVE = true;
                REQ = true;
            } else {
                ADDRESS_ACTIVE = true;
                ADDRESS = address++;
                DATA_ACTIVE = true;
                DATA++;
                WR_ACTIVE = true;
                WR = true;
                REQ_ACTIVE = true;
                REQ = true;
            }
            read = !read;
        } else {
            ADDRESS_ACTIVE = false;
            DATA_ACTIVE = false;
            WR_ACTIVE = false;
            REQ_ACTIVE = false;
        }
    }
}
