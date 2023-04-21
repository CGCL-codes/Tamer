package org.omg.CosTrading;

public final class OfferIdIteratorHolder implements org.omg.CORBA.portable.Streamable {

    public OfferIdIterator value;

    public OfferIdIteratorHolder() {
    }

    public OfferIdIteratorHolder(OfferIdIterator initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = OfferIdIteratorHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        OfferIdIteratorHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return OfferIdIteratorHelper.type();
    }
}
