package org.omg.CosTypedEventChannelAdmin;

public class TypedProxyPullSupplierPOATie extends TypedProxyPullSupplierPOA {

    private TypedProxyPullSupplierOperations delegate_;

    private org.omg.PortableServer.POA poa_;

    public TypedProxyPullSupplierPOATie(TypedProxyPullSupplierOperations delegate) {
        delegate_ = delegate;
    }

    public TypedProxyPullSupplierPOATie(TypedProxyPullSupplierOperations delegate, org.omg.PortableServer.POA poa) {
        delegate_ = delegate;
        poa_ = poa;
    }

    public TypedProxyPullSupplierOperations _delegate() {
        return delegate_;
    }

    public void _delegate(TypedProxyPullSupplierOperations delegate) {
        delegate_ = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (poa_ != null) return poa_; else return super._default_POA();
    }

    public void connect_pull_consumer(org.omg.CosEventComm.PullConsumer _ob_a0) throws org.omg.CosEventChannelAdmin.AlreadyConnected {
        delegate_.connect_pull_consumer(_ob_a0);
    }

    public org.omg.CORBA.Any pull() throws org.omg.CosEventComm.Disconnected {
        return delegate_.pull();
    }

    public org.omg.CORBA.Any try_pull(org.omg.CORBA.BooleanHolder _ob_ah0) throws org.omg.CosEventComm.Disconnected {
        return delegate_.try_pull(_ob_ah0);
    }

    public void disconnect_pull_supplier() {
        delegate_.disconnect_pull_supplier();
    }

    public org.omg.CORBA.Object get_typed_supplier() {
        return delegate_.get_typed_supplier();
    }
}
