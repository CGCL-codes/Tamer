package org.modelibra.modeler.model.ref;

import java.io.Serializable;

/**
 * 
 * @author Dzenan Ridjanovic
 * @version 2006-02-18
 */
public class Oid implements Serializable {

    static final long serialVersionUID = -2708621541086072273L;

    private long timeStamp;

    private int sequence;

    private static int counter = 0;

    public Oid() {
        timeStamp = System.currentTimeMillis();
        sequence = counter++;
    }

    public Oid(int aSequence) {
        timeStamp = System.currentTimeMillis();
        sequence = aSequence;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long aTimeStamp) {
        timeStamp = aTimeStamp;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int aSequence) {
        sequence = aSequence;
    }

    public long getOidUniqueNumber() {
        long uniqueNumber = timeStamp + sequence;
        return uniqueNumber;
    }

    public String getOidUniqueNumberString() {
        long uniqueNumber = getOidUniqueNumber();
        return new Long(uniqueNumber).toString();
    }

    public String toString() {
        return new Long(timeStamp).toString() + " " + new Integer(sequence).toString();
    }

    public boolean equals(Oid anOid) {
        if (this.timeStamp == anOid.getTimeStamp() && this.sequence == anOid.getSequence()) {
            return true;
        }
        return false;
    }

    public boolean equals(int aSequence) {
        if (this.sequence == aSequence) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Oid oid = new Oid();
        System.out.println("Time stamp: " + oid.getTimeStamp());
        System.out.println("Sequence: " + oid.getSequence());
        System.out.println("Oid unique number: " + oid.getOidUniqueNumber());
    }
}
