package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Message_TypeEntity;
import com.medcentrex.bridge.interfaces.Message_TypeEntityData;
import com.medcentrex.bridge.interfaces.Message_TypeEntityHome;
import com.medcentrex.bridge.interfaces.Message_TypeEntityPK;
import com.medcentrex.bridge.interfaces.ServiceUnavailableException;
import com.medcentrex.bridge.interfaces.SequenceGenerator;
import com.medcentrex.bridge.interfaces.SequenceGeneratorHome;
import java.sql.Date;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * The Entity bean represents a Message_TypeEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Message_TypeEntity"
 *           display-name="Message_TypeEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Message_TypeEntity"
 * 			schema="Message_Type"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="message_type"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"

 *
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.Message_TypeEntity findByMessage_Type_ID( java.lang.Integer pMsg_Type_ID )"
 * query="SELECT OBJECT(ob) FROM Message_Type ob WHERE ob.message_Type_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="message_type"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Message_TypeEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pMessage_TypeEntity The Value Object containing the Message_TypeEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Message_TypeEntityData pMessage_TypeEntity) throws InvalidValueException {
        if (pMessage_TypeEntity == null) {
            throw new InvalidValueException("object.undefined", "Message_TypeEntity");
        }
        if (pMessage_TypeEntity.getMessage_Type_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Message_TypeEntity", "Id" });
        }
        setMessage_Type_ID(pMessage_TypeEntity.getMessage_Type_ID());
        setMessage_Type(pMessage_TypeEntity.getMessage_Type());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pMessage_TypeEntity The Value Object containing the Message_TypeEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(Message_TypeEntityData pMessage_TypeEntity) throws InvalidValueException {
        if (pMessage_TypeEntity == null) {
            throw new InvalidValueException("object.undefined", "Message_TypeEntity");
        }
        if (pMessage_TypeEntity.getMessage_Type_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Message_TypeEntity", "Id" });
        }
        setMessage_Type(pMessage_TypeEntity.getMessage_Type());
    }

    /**
   * Create and return a Message_TypeEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Message_TypeEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Message_TypeEntityData getValueObject() {
        Message_TypeEntityData lData = new Message_TypeEntityData();
        lData.setMessage_Type_ID(getMessage_Type_ID());
        lData.setMessage_Type(getMessage_Type());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Message_TypeEntityBean [ " + getValueObject() + " ]";
    }

    /**
   * Retrive a unique creation id to use for this bean.  This will end up
   * demarcating this bean from others when it is stored as a record
   * in the database.
   *
   * @return Returns an integer that can be used as a unique creation id.
   *
   * @throws ServiceUnavailableException Indicating that it was not possible
   *                                     to retrieve a new unqiue ID because
   *                                     the service is not available
   **/
    private Integer generateUniqueId() throws ServiceUnavailableException {
        Integer lUniqueId = new Integer(-1);
        String lSequenceField = "Message_Type_ID";
        try {
            Context lContext = new InitialContext();
            String lSequenceName = (java.lang.String) lContext.lookup("java:comp/env/SequenceName");
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/bridge/SequenceGenerator"), SequenceGeneratorHome.class);
            SequenceGenerator lBean = (SequenceGenerator) lHome.create();
            lUniqueId = lBean.getNextNumber(lSequenceName, lSequenceField);
            lBean.remove();
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("Naming lookup failure: " + ne.getMessage());
        } catch (CreateException ce) {
            throw new ServiceUnavailableException("Failure while creating a generator session bean: " + ce.getMessage());
        } catch (RemoveException re) {
        } catch (RemoteException rte) {
            throw new ServiceUnavailableException("Remote exception occured while accessing generator session bean: " + rte.getMessage());
        }
        return lUniqueId;
    }

    /**
   * Retrieve the Message_TypeEntity's Message_Type_ID.
   *
   * @return Returns an Integer representing the Message_Type_ID of this Message_TypeEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Message_Type_ID"
   **/
    public abstract Integer getMessage_Type_ID();

    /**
   * Set the Message_TypeEntity's Message_Type_ID.
   *
   * @param pMessage_Type_ID The Message_Type_ID of this Message_TypeEntity. Is set at creation time.
   **/
    public abstract void setMessage_Type_ID(java.lang.Integer pMessage_Type_ID);

    /**
   * Retrieve the Message_TypeEntity's Message_Type.
   *
   * @return Returns an Integer representing the Message_Type of this Message_TypeEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Message_Type"
   **/
    public abstract String getMessage_Type();

    /**
   * Set the Message_TypeEntity's Message_Type.
   *
   * @param pPlace_Name The Message_Type of this Message_TypeEntity.  Is set at creation time.
   **/
    public abstract void setMessage_Type(java.lang.String pMessage_Type);

    /**
   * Create a Message_TypeEntity based on the supplied Message_TypeEntity Value Object.
   *
   * @param pMessage_TypeEntity The data used to create the Message_TypeEntity.
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @throws EJBException If no new unique ID could be retrieved this will
   *                      rollback the transaction because there is no
   *                      hope to try again
   * @throws CreateException Because we have to do so (EJB spec.)
   *
   * @ejb:create-method view-type="remote"
   **/
    public Message_TypeEntityPK ejbCreate(Message_TypeEntityData pMessage_TypeEntity) throws InvalidValueException, EJBException, CreateException {
        Message_TypeEntityData lData = (Message_TypeEntityData) pMessage_TypeEntity.clone();
        try {
            lData.setMessage_Type_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Message_TypeEntityData pMessage_TypeEntity) {
    }

    public void setEntityContext(EntityContext lContext) {
        mContext = lContext;
    }

    public void unsetEntityContext() {
        mContext = null;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbLoad() {
    }

    public void ejbStore() {
    }

    public void ejbRemove() throws RemoveException {
    }
}
