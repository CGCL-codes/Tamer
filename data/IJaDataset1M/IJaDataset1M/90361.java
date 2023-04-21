package org.omg.CosNotifyChannelAdmin;

public final class SequenceProxyPullSupplierHolder implements org.omg.CORBA.portable.Streamable {

    public org.omg.CosNotifyChannelAdmin.SequenceProxyPullSupplier value = null;

    public SequenceProxyPullSupplierHolder() {
    }

    public SequenceProxyPullSupplierHolder(org.omg.CosNotifyChannelAdmin.SequenceProxyPullSupplier initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.omg.CosNotifyChannelAdmin.SequenceProxyPullSupplierHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.omg.CosNotifyChannelAdmin.SequenceProxyPullSupplierHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.omg.CosNotifyChannelAdmin.SequenceProxyPullSupplierHelper.type();
    }
}
