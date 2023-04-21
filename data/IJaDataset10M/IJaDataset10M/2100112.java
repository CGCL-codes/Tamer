package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Appt_StatusEntity;
import com.medcentrex.bridge.interfaces.Appt_StatusEntityData;
import com.medcentrex.bridge.interfaces.Appt_StatusEntityHome;
import com.medcentrex.bridge.interfaces.Appt_StatusEntityPK;
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
 * The Entity bean represents a Appt_StatusEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Appt_StatusEntity"
 *           display-name="Appt_StatusEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Appt_StatusEntity"
 * 			 schema="Appt_Status"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="appt_status"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.Appt_StatusEntity findByAppt_Status_ID( java.lang.Integer pAcct_Status_ID )"
 * query="SELECT OBJECT(ob) FROM Appt_Status ob WHERE ob.appt_Status_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="appt_status"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Appt_StatusEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pAppt_StatusEntity The Value Object containing the Appt_StatusEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Appt_StatusEntityData pAppt_StatusEntity) throws InvalidValueException {
        if (pAppt_StatusEntity == null) {
            throw new InvalidValueException("object.undefined", "Appt_StatusEntity");
        }
        if (pAppt_StatusEntity.getAppt_Status_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Appt_StatusEntity", "Id" });
        }
        setAppt_Status_ID(pAppt_StatusEntity.getAppt_Status_ID());
        setAppt_Status(pAppt_StatusEntity.getAppt_Status());
    }

    /**
   * Create and return a Appt_StatusEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Appt_StatusEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Appt_StatusEntityData getValueObject() {
        Appt_StatusEntityData lData = new Appt_StatusEntityData();
        lData.setAppt_Status_ID(getAppt_Status_ID());
        lData.setAppt_Status(getAppt_Status());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Appt_StatusEntityBean [ " + getValueObject() + " ]";
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
        String lSequenceField = "Appt_Status_ID";
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
   * Retrieve the Appt_StatusEntity's Appt_Status_ID.
   *
   * @return Returns an Integer representing the Appt_Status_ID of this Appt_StatusEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Appt_Status_ID"
   **/
    public abstract Integer getAppt_Status_ID();

    /**
   * Set the Appt_StatusEntity's Appt_Status_ID.
   *
   * @param pAppt_Status_ID The Appt_Status_ID of this Appt_StatusEntity. Is set at creation time.
   **/
    public abstract void setAppt_Status_ID(java.lang.Integer pAppt_Status_ID);

    /**
   * Retrieve the Appt_StatusEntity's Appt_Status.
   *
   * @return Returns an Integer representing the Appt_Status of this Appt_StatusEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Appt_Status"
   **/
    public abstract String getAppt_Status();

    /**
   * Set the Appt_StatusEntity's Appt_Status.
   *
   * @param pPlace_Name The Appt_Status of this Appt_StatusEntity.  Is set at creation time.
   **/
    public abstract void setAppt_Status(java.lang.String pAppt_Status);

    /**
   * Create a Appt_StatusEntity based on the supplied Appt_StatusEntity Value Object.
   *
   * @param pAppt_StatusEntity The data used to create the Appt_StatusEntity.
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
    public Appt_StatusEntityPK ejbCreate(Appt_StatusEntityData pAppt_StatusEntity) throws InvalidValueException, EJBException, CreateException {
        Appt_StatusEntityData lData = (Appt_StatusEntityData) pAppt_StatusEntity.clone();
        try {
            lData.setAppt_Status_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Appt_StatusEntityData pAppt_StatusEntity) {
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
