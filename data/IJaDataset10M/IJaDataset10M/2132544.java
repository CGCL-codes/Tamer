package org.omg.DsObservationValue;

/**
 * Struct definition: Numeric.
 * 
 * @author OpenORB Compiler
*/
public final class Numeric implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member units
     */
    public String units;

    /**
     * Struct member value
     */
    public float value;

    /**
     * Default constructor
     */
    public Numeric() {
    }

    /**
     * Constructor with fields initialization
     * @param units units struct member
     * @param value value struct member
     */
    public Numeric(String units, float value) {
        this.units = units;
        this.value = value;
    }
}
