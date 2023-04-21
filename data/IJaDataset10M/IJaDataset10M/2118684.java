package org.asam.ods;

/**
 *	Generated from IDL interface "ApplicationElement"
 *	@author JacORB IDL compiler V 2.2.3, 10-Dec-2005
 */
public interface ApplicationElementOperations {

    org.asam.ods.ApplicationAttribute createAttribute() throws org.asam.ods.AoException;

    org.asam.ods.InstanceElement createInstance(java.lang.String ieName) throws org.asam.ods.AoException;

    org.asam.ods.ApplicationElement[] getAllRelatedElements() throws org.asam.ods.AoException;

    org.asam.ods.ApplicationRelation[] getAllRelations() throws org.asam.ods.AoException;

    org.asam.ods.ApplicationAttribute getAttributeByBaseName(java.lang.String baName) throws org.asam.ods.AoException;

    org.asam.ods.ApplicationAttribute getAttributeByName(java.lang.String aaName) throws org.asam.ods.AoException;

    org.asam.ods.ApplicationAttribute[] getAttributes(java.lang.String aaPattern) throws org.asam.ods.AoException;

    org.asam.ods.BaseElement getBaseElement() throws org.asam.ods.AoException;

    org.asam.ods.T_LONGLONG getId() throws org.asam.ods.AoException;

    org.asam.ods.InstanceElement getInstanceById(org.asam.ods.T_LONGLONG ieId) throws org.asam.ods.AoException;

    org.asam.ods.InstanceElement getInstanceByName(java.lang.String ieName) throws org.asam.ods.AoException;

    org.asam.ods.InstanceElementIterator getInstances(java.lang.String iePattern) throws org.asam.ods.AoException;

    java.lang.String getName() throws org.asam.ods.AoException;

    org.asam.ods.ApplicationElement[] getRelatedElementsByRelationship(org.asam.ods.Relationship aeRelationship) throws org.asam.ods.AoException;

    org.asam.ods.ApplicationRelation[] getRelationsByType(org.asam.ods.RelationType aeRelationType) throws org.asam.ods.AoException;

    java.lang.String[] listAllRelatedElements() throws org.asam.ods.AoException;

    java.lang.String[] listAttributes(java.lang.String aaPattern) throws org.asam.ods.AoException;

    org.asam.ods.NameIterator listInstances(java.lang.String aaPattern) throws org.asam.ods.AoException;

    java.lang.String[] listRelatedElementsByRelationship(org.asam.ods.Relationship aeRelationship) throws org.asam.ods.AoException;

    void removeAttribute(org.asam.ods.ApplicationAttribute applAttr) throws org.asam.ods.AoException;

    void removeInstance(org.asam.ods.T_LONGLONG ieId, boolean recursive) throws org.asam.ods.AoException;

    void setBaseElement(org.asam.ods.BaseElement baseElem) throws org.asam.ods.AoException;

    void setName(java.lang.String aeName) throws org.asam.ods.AoException;

    void setRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;

    org.asam.ods.ACL[] getRights() throws org.asam.ods.AoException;

    org.asam.ods.InitialRight[] getInitialRights() throws org.asam.ods.AoException;

    void setInitialRights(org.asam.ods.InstanceElement usergroup, int rights, org.asam.ods.T_LONGLONG refAid, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;

    void setInitialRightRelation(org.asam.ods.ApplicationRelation applRel, boolean set) throws org.asam.ods.AoException;

    org.asam.ods.ApplicationRelation[] getInitialRightRelations() throws org.asam.ods.AoException;

    int getSecurityLevel() throws org.asam.ods.AoException;

    void setSecurityLevel(int secLevel, org.asam.ods.RightsSet set) throws org.asam.ods.AoException;

    org.asam.ods.ApplicationStructure getApplicationStructure() throws org.asam.ods.AoException;

    org.asam.ods.InstanceElement[] createInstances(org.asam.ods.NameValueSeqUnit[] attributes, org.asam.ods.ApplicationRelationInstanceElementSeq[] relatedInstances) throws org.asam.ods.AoException;
}
