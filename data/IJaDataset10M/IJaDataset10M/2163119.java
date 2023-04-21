package org.jcryptool.visual.zeroknowledge.algorithm;

import java.math.BigInteger;
import java.util.Observable;

/**
 * Modell zum Speichern von p und q.
 * 
 * @author Mareike Paul
 *@version 1.0.0
 */
public class Modell extends Observable {

    private BigInteger p = BigInteger.ZERO;

    private BigInteger q = BigInteger.ZERO;

    /**
     * gibt p zurück
     * 
     * @return Wert für p
     */
    public BigInteger getP() {
        return p;
    }

    /**
     * gibt q zurück
     * 
     * @return Wert für q
     */
    public BigInteger getQ() {
        return q;
    }

    /**
     * setzt p und q auf 0
     */
    public void reset() {
        p = q = BigInteger.ZERO;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * setzt p neu
     * 
     * @param p neuer Wert für p
     */
    public void setP(BigInteger p) {
        this.p = p;
        this.setChanged();
        this.notifyObservers();
    }

    /**
     * setzt q neu
     * 
     * @param q neuer Wert für q
     */
    public void setQ(BigInteger q) {
        this.q = q;
        this.setChanged();
        this.notifyObservers();
    }
}
