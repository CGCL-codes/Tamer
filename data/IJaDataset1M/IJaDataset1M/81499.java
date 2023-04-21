package com.ohioedge.j2ee.api.doc.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;

/**
 * @(#)MimeTypeEJB.java	1.350 01/12/03
 * Specifies MimeType EJB
 * @author Sandeep Dixit
 * @version 1.350, 01/12/03
 * @see     org.j2eebuilder.view.ValueObjectFactory#getDataVO(
 *			java.lang.Object, java.lang.Class)
 * @since OEC1.2
 */
public abstract class MimeTypeEJB extends org.j2eebuilder.model.ejb.SignatureAbstract implements EntityBean {

    public org.j2eebuilder.model.ManagedTransientObject getDataVO() throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        return org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.getDataVO(this.ctx.getPrimaryKey(), this, com.ohioedge.j2ee.api.doc.MimeTypeBean.class);
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

    public MimeTypeEJB() {
    }

    public MimeTypePK ejbCreate(Integer id, String name, String description, Integer createdBy) throws CreateException {
        setMimeTypeID(id);
        setName(name);
        setDescription(description);
        setCreatedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setCreatedBy(createdBy);
        return null;
    }

    public void ejbPostCreate(Integer id, String name, String description, Integer createdBy) throws CreateException {
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

    public abstract Integer getMimeTypeID();

    public abstract void setMimeTypeID(Integer mimeTypeID);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDescription();

    public abstract void setDescription(String description);
}
