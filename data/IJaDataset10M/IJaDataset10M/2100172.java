package org.omg.CosPropertyService;

public class PropertySetFactoryPOATie extends PropertySetFactoryPOA {

    private PropertySetFactoryOperations delegate_;

    private org.omg.PortableServer.POA poa_;

    public PropertySetFactoryPOATie(PropertySetFactoryOperations delegate) {
        delegate_ = delegate;
    }

    public PropertySetFactoryPOATie(PropertySetFactoryOperations delegate, org.omg.PortableServer.POA poa) {
        delegate_ = delegate;
        poa_ = poa;
    }

    public PropertySetFactoryOperations _delegate() {
        return delegate_;
    }

    public void _delegate(PropertySetFactoryOperations delegate) {
        delegate_ = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (poa_ != null) return poa_; else return super._default_POA();
    }

    public PropertySet create_propertyset() {
        return delegate_.create_propertyset();
    }

    public PropertySet create_constrained_propertyset(org.omg.CORBA.TypeCode[] _ob_a0, Property[] _ob_a1) throws ConstraintNotSupported {
        return delegate_.create_constrained_propertyset(_ob_a0, _ob_a1);
    }

    public PropertySet create_initial_propertyset(Property[] _ob_a0) throws MultipleExceptions {
        return delegate_.create_initial_propertyset(_ob_a0);
    }
}
