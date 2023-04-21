package org.omg.DsObservationValue;

/**
 * Struct definition: UniversalResourceIdentifier.
 * 
 * @author OpenORB Compiler
*/
public final class UniversalResourceIdentifier implements org.omg.CORBA.portable.IDLEntity {

    /**
     * Struct member protocol
     */
    public String protocol;

    /**
     * Struct member address
     */
    public String address;

    /**
     * Default constructor
     */
    public UniversalResourceIdentifier() {
    }

    /**
     * Constructor with fields initialization
     * @param protocol protocol struct member
     * @param address address struct member
     */
    public UniversalResourceIdentifier(String protocol, String address) {
        this.protocol = protocol;
        this.address = address;
    }
}
