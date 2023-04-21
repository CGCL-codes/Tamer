package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "QueryConstants"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public class QueryConstantsPOATie extends QueryConstantsPOA {

    private QueryConstantsOperations _delegate;

    private POA _poa;

    public QueryConstantsPOATie(QueryConstantsOperations delegate) {
        _delegate = delegate;
    }

    public QueryConstantsPOATie(QueryConstantsOperations delegate, POA poa) {
        _delegate = delegate;
        _poa = poa;
    }

    public org.asam.ods.QueryConstants _this() {
        return org.asam.ods.QueryConstantsHelper.narrow(_this_object());
    }

    public org.asam.ods.QueryConstants _this(org.omg.CORBA.ORB orb) {
        return org.asam.ods.QueryConstantsHelper.narrow(_this_object(orb));
    }

    public QueryConstantsOperations _delegate() {
        return _delegate;
    }

    public void _delegate(QueryConstantsOperations delegate) {
        _delegate = delegate;
    }

    public POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }
}
