package org.omg.DsObservationValue;

/**
 * Struct definition: Range.
 * 
 * @author OpenORB Compiler
*/
public final class Range implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member units
     */
    public String units;

    /**
     * Struct member lower
     */
    public float lower;

    /**
     * Struct member upper
     */
    public float upper;

    /**
     * Default constructor
     */
    public Range() {
    }

    /**
     * Constructor with fields initialization
     * @param units units struct member
     * @param lower lower struct member
     * @param upper upper struct member
     */
    public Range(String units, float lower, float upper) {
        this.units = units;
        this.lower = lower;
        this.upper = upper;
    }
}
