package org.systemsbiology.chem.app;

import java.io.*;

/**
 * Represents a single fire counter item.
 * This item is used to store the values of how may times a reaction fires, and its name.
 * 
 * @author      Luca Cacchiani
 * @version     0.1
 * @date     	11/02/07
 */
public class FireCounter implements Serializable {

    /** The name of the reaction. */
    private String mName;

    /** How many times the reaction is fired. */
    private long mCounter;

    /**
     * Creates a new item.
     * 
     * @param pName  the name of the reaction.
     */
    public FireCounter(String pName) {
        mCounter = 0;
        mName = pName;
    }

    /**
     * Returns the name of the reaction.
     * 
     * @return The name
     */
    public String getName() {
        return mName;
    }

    /**
     * Returns the number of times that a reaction is fired.
     * 
     * @return The number
     */
    public long getCounter() {
        return mCounter;
    }

    /**
     * Increments the number of fired
     */
    public void incCounter() {
        mCounter = mCounter + 1;
    }
}
