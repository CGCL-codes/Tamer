package org.asam.ods;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "ApplicationRelation"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public class ApplicationRelationPOATie extends ApplicationRelationPOA {

    private ApplicationRelationOperations _delegate;

    private POA _poa;

    public ApplicationRelationPOATie(ApplicationRelationOperations delegate) {
        _delegate = delegate;
    }

    public ApplicationRelationPOATie(ApplicationRelationOperations delegate, POA poa) {
        _delegate = delegate;
        _poa = poa;
    }

    public org.asam.ods.ApplicationRelation _this() {
        return org.asam.ods.ApplicationRelationHelper.narrow(_this_object());
    }

    public org.asam.ods.ApplicationRelation _this(org.omg.CORBA.ORB orb) {
        return org.asam.ods.ApplicationRelationHelper.narrow(_this_object(orb));
    }

    public ApplicationRelationOperations _delegate() {
        return _delegate;
    }

    public void _delegate(ApplicationRelationOperations delegate) {
        _delegate = delegate;
    }

    public POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public void setRelationRange(org.asam.ods.RelationRange arRelationRange) throws org.asam.ods.AoException {
        _delegate.setRelationRange(arRelationRange);
    }

    public org.asam.ods.BaseRelation getBaseRelation() throws org.asam.ods.AoException {
        return _delegate.getBaseRelation();
    }

    public void setRelationType(org.asam.ods.RelationType arRelationType) throws org.asam.ods.AoException {
        _delegate.setRelationType(arRelationType);
    }

    public void setBaseRelation(org.asam.ods.BaseRelation baseRel) throws org.asam.ods.AoException {
        _delegate.setBaseRelation(baseRel);
    }

    public org.asam.ods.ApplicationElement getElem1() throws org.asam.ods.AoException {
        return _delegate.getElem1();
    }

    public org.asam.ods.RelationRange getInverseRelationRange() throws org.asam.ods.AoException {
        return _delegate.getInverseRelationRange();
    }

    public void setInverseRelationRange(org.asam.ods.RelationRange arRelationRange) throws org.asam.ods.AoException {
        _delegate.setInverseRelationRange(arRelationRange);
    }

    public org.asam.ods.Relationship getInverseRelationship() throws org.asam.ods.AoException {
        return _delegate.getInverseRelationship();
    }

    public void setElem2(org.asam.ods.ApplicationElement applElem) throws org.asam.ods.AoException {
        _delegate.setElem2(applElem);
    }

    public org.asam.ods.Relationship getRelationship() throws org.asam.ods.AoException {
        return _delegate.getRelationship();
    }

    public org.asam.ods.RelationRange getRelationRange() throws org.asam.ods.AoException {
        return _delegate.getRelationRange();
    }

    public void setInverseRelationName(java.lang.String arInvName) throws org.asam.ods.AoException {
        _delegate.setInverseRelationName(arInvName);
    }

    public void setElem1(org.asam.ods.ApplicationElement applElem) throws org.asam.ods.AoException {
        _delegate.setElem1(applElem);
    }

    public java.lang.String getInverseRelationName() throws org.asam.ods.AoException {
        return _delegate.getInverseRelationName();
    }

    public java.lang.String getRelationName() throws org.asam.ods.AoException {
        return _delegate.getRelationName();
    }

    public org.asam.ods.RelationType getRelationType() throws org.asam.ods.AoException {
        return _delegate.getRelationType();
    }

    public org.asam.ods.ApplicationElement getElem2() throws org.asam.ods.AoException {
        return _delegate.getElem2();
    }

    public void setRelationName(java.lang.String arName) throws org.asam.ods.AoException {
        _delegate.setRelationName(arName);
    }
}
