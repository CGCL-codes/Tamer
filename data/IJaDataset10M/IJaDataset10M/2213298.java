package org.omg.CORBA.FT;

/**
 *	Generated from IDL definition of alias "Locations"
 *	@author JacORB IDL compiler 
 */
public final class LocationsHolder implements org.omg.CORBA.portable.Streamable {

    public org.omg.CosNaming.NameComponent[][] value;

    public LocationsHolder() {
    }

    public LocationsHolder(final org.omg.CosNaming.NameComponent[][] initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return LocationsHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = LocationsHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        LocationsHelper.write(out, value);
    }
}
