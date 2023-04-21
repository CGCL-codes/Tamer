package org.omg.CosTradingRepos.ServiceTypeRepositoryPackage;

public final class HasSubTypesHolder implements org.omg.CORBA.portable.Streamable {

    public HasSubTypes value;

    public HasSubTypesHolder() {
    }

    public HasSubTypesHolder(HasSubTypes initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = HasSubTypesHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        HasSubTypesHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return HasSubTypesHelper.type();
    }
}
