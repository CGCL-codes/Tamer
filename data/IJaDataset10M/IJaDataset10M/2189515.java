package org.webgraphlab.algorithm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL definition of interface "AlgorithmComponentHome"
 *	@author JacORB IDL compiler 
 */
public class AlgorithmComponentHomePOATie extends AlgorithmComponentHomePOA {

    private AlgorithmComponentHomeOperations _delegate;

    private POA _poa;

    public AlgorithmComponentHomePOATie(AlgorithmComponentHomeOperations delegate) {
        _delegate = delegate;
    }

    public AlgorithmComponentHomePOATie(AlgorithmComponentHomeOperations delegate, POA poa) {
        _delegate = delegate;
        _poa = poa;
    }

    public org.webgraphlab.algorithm.AlgorithmComponentHome _this() {
        return org.webgraphlab.algorithm.AlgorithmComponentHomeHelper.narrow(_this_object());
    }

    public org.webgraphlab.algorithm.AlgorithmComponentHome _this(org.omg.CORBA.ORB orb) {
        return org.webgraphlab.algorithm.AlgorithmComponentHomeHelper.narrow(_this_object(orb));
    }

    public AlgorithmComponentHomeOperations _delegate() {
        return _delegate;
    }

    public void _delegate(AlgorithmComponentHomeOperations delegate) {
        _delegate = delegate;
    }

    public POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public org.omg.CORBA.IRObject get_component_def() {
        return _delegate.get_component_def();
    }

    public java.lang.String version() {
        return _delegate.version();
    }

    public java.lang.String description() {
        return _delegate.description();
    }

    public java.lang.String shortName() {
        return _delegate.shortName();
    }

    public void remove_home() throws org.omg.Components.RemoveFailure {
        _delegate.remove_home();
    }

    public org.omg.CORBA.IRObject get_home_def() {
        return _delegate.get_home_def();
    }

    public org.webgraphlab.algorithm.AlgorithmComponent create() throws org.omg.Components.CreateFailure {
        return _delegate.create();
    }

    public org.omg.Components.Deployment.Container get_container() {
        return _delegate.get_container();
    }

    public void remove_component(org.omg.Components.CCMObject comp) throws org.omg.Components.RemoveFailure {
        _delegate.remove_component(comp);
    }

    public java.lang.String author() {
        return _delegate.author();
    }

    public org.omg.Components.CCMObject create_component() throws org.omg.Components.CreateFailure {
        return _delegate.create_component();
    }

    public org.omg.Components.CCMObject[] get_components() {
        return _delegate.get_components();
    }
}
