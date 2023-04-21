package org.chernovia.net.iccbots.analyzer;

public class Ply {

    public String smove, amove;

    public int eval, wclock, bclock, movenum;

    public Ply(int m, String s, String a, int wc, int bc, int e) {
        movenum = m;
        smove = s;
        amove = a;
        wclock = wc;
        bclock = bc;
        eval = e;
    }

    @Override
    public String toString() {
        return movenum + " " + smove + " " + amove + " " + wclock + " " + bclock + " " + eval;
    }
}
