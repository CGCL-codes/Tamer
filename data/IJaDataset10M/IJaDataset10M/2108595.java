package com.ohioedge.j2ee.api.org.doc.ejb;

import javax.ejb.*;

/**
 * @(#)DirectoryEJB.java	1.350 01/12/03
 * @version 1.3.1
 * @see     org.j2eebuilder.view.ValueObjectFactory#getDataVO(
 *			java.lang.Object, java.lang.Class)
 * @since OEC1.2
 */
public abstract class DirectoryEJB extends org.j2eebuilder.model.ejb.SignatureAbstract implements EntityBean {

    public org.j2eebuilder.model.ManagedTransientObject getDataVO() throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        return org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.getDataVO(this.ctx.getPrimaryKey(), this, com.ohioedge.j2ee.api.org.doc.DirectoryBean.class);
    }

    public void setDataVO(org.j2eebuilder.model.ManagedTransientObject valueObject, org.j2eebuilder.ComponentDefinition componentDefinition, Integer mechanismID) throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.setDataVO(this, valueObject, componentDefinition);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(mechanismID);
    }

    public void setName(String name, Integer modifiedBy) {
        setName(name);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setDescription(String description, Integer modifiedBy) {
        setDescription(description);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public void setPath(String path, Integer modifiedBy) {
        setPath(path);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public DirectoryEJB() {
    }

    public DirectoryPK ejbCreate(Integer orgID, Integer mechanismID, Integer id, String name, String description, String path, Integer createdBy) throws CreateException {
        setOrganizationID(orgID);
        setMechanismID(mechanismID);
        setDirectoryID(id);
        setName(name);
        setDescription(description);
        setPath(path);
        setCreatedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setCreatedBy(createdBy);
        return null;
    }

    public void ejbPostCreate(Integer orgID, Integer mechanismID, Integer id, String name, String description, String path, Integer createdBy) throws CreateException {
    }

    public void ejbActivate() {
    }

    public void ejbLoad() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() throws RemoveException {
    }

    public void ejbStore() {
    }

    public void setEntityContext(EntityContext ctx) {
        this.ctx = ctx;
    }

    public void unsetEntityContext() {
        this.ctx = null;
    }

    private EntityContext ctx;

    public abstract Integer getDirectoryID();

    public abstract void setDirectoryID(Integer directoryID);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getPath();

    public abstract void setPath(String path);

    public abstract Integer getOrganizationID();

    public abstract void setOrganizationID(Integer organizationID);

    public abstract Integer getMechanismID();

    public abstract void setMechanismID(Integer mechanismID);
}
