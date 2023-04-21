package soc.robot;

import soc.game.SOCResourceSet;

/**
 * this class holds a SOCResourceSet and a building type
 */
public class SOCResSetBuildTimePair {

    /**
     * the resource set
     */
    SOCResourceSet resources;

    /**
     * number of rolls
     */
    int rolls;

    /**
     * the constructor
     *
     * @param rs  the resource set
     * @param bt  the building time
     */
    public SOCResSetBuildTimePair(SOCResourceSet rs, int bt) {
        resources = rs;
        rolls = bt;
    }

    /**
     * @return the resource set
     */
    public SOCResourceSet getResources() {
        return resources;
    }

    /**
     * @return the building time
     */
    public int getRolls() {
        return rolls;
    }

    /**
     * @return a hashcode for this pair
     */
    public int hashCode() {
        String tmp = resources.toString() + rolls;
        return tmp.hashCode();
    }

    /**
     * @return true if the argument contains the same data
     *
     * @param anObject  the object in question
     */
    public boolean equals(Object anObject) {
        if ((anObject instanceof SOCResSetBuildTimePair) && (((SOCResSetBuildTimePair) anObject).getRolls() == rolls) && (((SOCResSetBuildTimePair) anObject).getResources().equals(resources))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return a human readable form of this object
     */
    public String toString() {
        String str = "ResTime:res=" + resources + "|rolls=" + rolls;
        return str;
    }
}
