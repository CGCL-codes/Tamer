package org.omg.CosNotification;

/**
 *	Generated from IDL definition of struct "Property"
 *	@author JacORB IDL compiler 
 */
public final class Property implements org.omg.CORBA.portable.IDLEntity {

    public Property() {
    }

    public java.lang.String name;

    public org.omg.CORBA.Any value;

    public Property(java.lang.String name, org.omg.CORBA.Any value) {
        this.name = name;
        this.value = value;
    }
}
