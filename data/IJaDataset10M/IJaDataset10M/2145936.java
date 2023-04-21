package org.omg.CosNotifyChannelAdmin;

public final class ProxyPushSupplierHolder implements org.omg.CORBA.portable.Streamable {

    public org.omg.CosNotifyChannelAdmin.ProxyPushSupplier value = null;

    public ProxyPushSupplierHolder() {
    }

    public ProxyPushSupplierHolder(org.omg.CosNotifyChannelAdmin.ProxyPushSupplier initialValue) {
        value = initialValue;
    }

    public void _read(org.omg.CORBA.portable.InputStream i) {
        value = org.omg.CosNotifyChannelAdmin.ProxyPushSupplierHelper.read(i);
    }

    public void _write(org.omg.CORBA.portable.OutputStream o) {
        org.omg.CosNotifyChannelAdmin.ProxyPushSupplierHelper.write(o, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return org.omg.CosNotifyChannelAdmin.ProxyPushSupplierHelper.type();
    }
}
