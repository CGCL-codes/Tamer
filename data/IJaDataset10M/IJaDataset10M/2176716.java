package com.ohioedge.j2ee.api.org.proc.ejb;

import java.rmi.RemoteException;
import javax.ejb.*;
import java.util.Set;

/**
 * @(#)AssignmentStatusTypeEJB.java	1.350 01/12/03
 * AssignmentStatusType is an independent value set. Currently,
 * supported levels are "Release" and "Hold"
 *
 * @version 1.3.1
 * @see     org.j2eebuilder.view.ManagedComponentObjectFactory#getDataVO(
 *			java.lang.Object, java.lang.Class)
 * @since OEC1.2
 */
public abstract class AssignmentStatusTypeEJB extends org.j2eebuilder.model.ejb.SignatureAbstract implements EntityBean {

    public org.j2eebuilder.model.ManagedTransientObject getDataVO() throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        return org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.getDataVO(this.ctx.getPrimaryKey(), this, com.ohioedge.j2ee.api.org.proc.AssignmentStatusTypeBean.class);
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

    public void setLevel(Integer level, Integer modifiedBy) {
        setLevel(level);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(modifiedBy);
    }

    public AssignmentStatusTypeEJB() {
    }

    public AssignmentStatusTypePK ejbCreate(Integer id, String name, String description, Integer privLevel, Integer createdBy) throws CreateException {
        setAssignmentStatusTypeID(id);
        setName(name);
        setDescription(description);
        setLevel(privLevel);
        setCreatedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setCreatedBy(createdBy);
        return null;
    }

    public void ejbPostCreate(Integer assignmentStatusTypeID, String name, String description, Integer privLevel, Integer createdBy) throws CreateException {
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

    public abstract Set getAssignments();

    public abstract void setAssignments(Set assignments);

    public abstract Set getPrivileges();

    public abstract void setPrivileges(Set privileges);

    private EntityContext ctx;

    public abstract Integer getAssignmentStatusTypeID();

    public abstract void setAssignmentStatusTypeID(Integer assignmentStatusTypeID);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract Integer getLevel();

    public abstract void setLevel(Integer level);
}
