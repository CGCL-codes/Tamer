package com.ohioedge.j2ee.api.org.proc.ejb;

import com.ohioedge.j2ee.api.org.proc.ejb.ActivityType;
import com.ohioedge.j2ee.api.org.proc.ejb.ActivityTypeHome;
import com.ohioedge.j2ee.api.org.proc.ejb.ActivityTypePK;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.*;
import java.util.Set;

/**
 * @(#)RelationEJB.java	1.350 01/12/03
 * Relation is the activityType on the right. There can be multiple
 * Relation for ActivityType. For example, ActivityType Followup
 * call may goto ActivityType - Literature sent or from ActivityType - Folloup
 * Call Person Not Found. Call Again.
 *
 * @version 1.3.1
 * @see     org.j2eebuilder.view.ManagedComponentObjectFactory#getDataVO(
 *			java.lang.Object, java.lang.Class)
 * @since OEC1.2
*/
public abstract class RelationEJB extends org.j2eebuilder.model.ejb.SignatureAbstract implements EntityBean {

    public org.j2eebuilder.model.ManagedTransientObject getDataVO() throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        return org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.getDataVO(this.ctx.getPrimaryKey(), this, com.ohioedge.j2ee.api.org.proc.RelationBean.class);
    }

    public void setDataVO(org.j2eebuilder.model.ManagedTransientObject valueObject, org.j2eebuilder.ComponentDefinition componentDefinition, Integer mechanismID) throws org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerException {
        org.j2eebuilder.model.ejb.ManagedTransientObjectHandlerHelper.setDataVO(this, valueObject, componentDefinition);
        setLastModifiedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setLastModifiedBy(mechanismID);
    }

    public RelationEJB() {
    }

    public RelationPK ejbCreate(Integer activityTypeID, Integer destinationID, Integer createdBy) throws CreateException {
        if (activityTypeID == null || destinationID == null) throw new CreateException("ejbCreate:Primary key can not be null.");
        setActivityTypeID(activityTypeID);
        setDestinationID(destinationID);
        setCreatedOn(new java.sql.Timestamp((new java.util.Date()).getTime()));
        setCreatedBy(createdBy);
        return null;
    }

    public void ejbPostCreate(Integer activityTypeID, Integer destinationID, Integer createdBy) throws CreateException {
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

    public abstract ActivityType getActivityType();

    public abstract void setActivityType(ActivityType activityType);

    private EntityContext ctx;

    public abstract Integer getActivityTypeID();

    public abstract void setActivityTypeID(Integer activityTypeID);

    public abstract Integer getDestinationID();

    public abstract void setDestinationID(Integer destinationID);
}
