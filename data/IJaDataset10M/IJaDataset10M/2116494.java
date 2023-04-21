package org.hl7.CTSMAPI;

/**
 * CTS Specification Version Identifier
 */
public final class CTSVersionId implements org.omg.CORBA.portable.IDLEntity {

    public org.hl7.types.INT major = null;

    public org.hl7.types.INT minor = null;

    public CTSVersionId() {
    }

    public CTSVersionId(org.hl7.types.INT _major, org.hl7.types.INT _minor) {
        major = _major;
        minor = _minor;
    }
}
