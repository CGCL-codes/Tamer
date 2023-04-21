package org.omg.CosTrading.RegisterPackage;

public final class NoMatchingOffersHolder implements org.omg.CORBA.portable.Streamable {

    public NoMatchingOffers value;

    public NoMatchingOffersHolder() {
    }

    public NoMatchingOffersHolder(NoMatchingOffers initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = NoMatchingOffersHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        NoMatchingOffersHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return NoMatchingOffersHelper.type();
    }
}
