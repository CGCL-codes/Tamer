package org.fudaa.dodico.corba.reflux3d;

public final class IResultatsReflux3dHolder implements org.omg.CORBA.portable.Streamable {

    public org.fudaa.dodico.corba.reflux3d.IResultatsReflux3d value = null;

    public IResultatsReflux3dHolder() {
    }

    public IResultatsReflux3dHolder(org.fudaa.dodico.corba.reflux3d.IResultatsReflux3d initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.fudaa.dodico.corba.reflux3d.IResultatsReflux3dHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.fudaa.dodico.corba.reflux3d.IResultatsReflux3dHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.fudaa.dodico.corba.reflux3d.IResultatsReflux3dHelper.type();
    }
}
