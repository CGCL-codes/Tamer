package net.sf.nebulacards.netpkt;

import java.io.*;

/**
 * Packet to represent a request to pass cards from your hand to another.
 *
 * @author James Ranson
 */
public class PassNotifyPkt implements Serializable, Cloneable {

    private int who, howmany;

    public PassNotifyPkt(int h, int w) {
        who = w;
        howmany = h;
    }

    public int getWho() {
        return who;
    }

    public void setWho(int n) {
        who = n;
    }

    public int getHowMany() {
        return howmany;
    }

    public void setHowMany(int h) {
        howmany = h;
    }
}
