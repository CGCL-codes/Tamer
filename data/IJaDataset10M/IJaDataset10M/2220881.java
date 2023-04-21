package org.omg.DsObservationAccess;

/**
 * Struct definition: ObservationId.
 * 
 * @author OpenORB Compiler
*/
public final class ObservationId implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member code
     */
    public String code;

    /**
     * Struct member opaque
     */
    public String opaque;

    /**
     * Default constructor
     */
    public ObservationId() {
    }

    /**
     * Constructor with fields initialization
     * @param code code struct member
     * @param opaque opaque struct member
     */
    public ObservationId(String code, String opaque) {
        this.code = code;
        this.opaque = opaque;
    }
}
