package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.AI_Location_MapEntity;
import com.medcentrex.bridge.interfaces.AI_Location_MapEntityData;
import com.medcentrex.bridge.interfaces.AI_Location_MapEntityHome;
import com.medcentrex.bridge.interfaces.AI_Location_MapEntityPK;
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
 * The Entity bean represents a AI_Location_MapEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="AI_Location_MapEntity"
 *           display-name="AI_Location_MapEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/AI_Location_MapEntity"
 *           schema="AI_Location_Map"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="ai_location_map"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.AI_Location_MapEntity findByCustomer_ID(java.lang.Integer pCustomer_ID)"
 * query="SELECT OBJECT(ob) FROM AI_Location_Map ob WHERE ob.customer_ID=?1"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.AI_Location_MapEntity findLocByCustomer_Map_ID(java.lang.Integer pPlace_ID,java.lang.Integer pAI_Customer_Map_ID )"
 * query="SELECT OBJECT(a) from AI_Location_Map AS a where a.place_ID = ?1 AND a.aI_Customer_Map_ID = ?2"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.AI_Location_MapEntity findLocByCustomer_ID(java.lang.Integer pPlace_ID,java.lang.Integer pAI_Customer_ID )"
 * query="SELECT OBJECT(a) from AI_Location_Map AS a where a.place_ID = ?1 AND a.customer_ID = ?2"
 *
 * @jboss:table-name table-name="ai_location_map"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class AI_Location_MapEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pAI_Location_MapEntity The Value Object containing the AI_Location_MapEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(AI_Location_MapEntityData pAI_Location_MapEntity) throws InvalidValueException {
        if (pAI_Location_MapEntity == null) {
            throw new InvalidValueException("object.undefined", "AI_Location_MapEntity");
        }
        if (pAI_Location_MapEntity.getAI_Location_Map_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "AI_Location_MapEntity", "Id" });
        }
        setAI_Location_Map_ID(pAI_Location_MapEntity.getAI_Location_Map_ID());
        setCustomer_ID(pAI_Location_MapEntity.getCustomer_ID());
        setPlace_ID(pAI_Location_MapEntity.getPlace_ID());
        setVendor_ID(pAI_Location_MapEntity.getVendor_ID());
        setVen_Loc_ID(pAI_Location_MapEntity.getVen_Loc_ID());
        setAI_Customer_Map_ID(pAI_Location_MapEntity.getAI_Customer_Map_ID());
    }

    /**
   * Create and return a AI_Location_MapEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a AI_Location_MapEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public AI_Location_MapEntityData getValueObject() {
        AI_Location_MapEntityData lData = new AI_Location_MapEntityData();
        lData.setAI_Location_Map_ID(getAI_Location_Map_ID());
        lData.setCustomer_ID(getCustomer_ID());
        lData.setPlace_ID(getPlace_ID());
        lData.setVendor_ID(getVendor_ID());
        lData.setVen_Loc_ID(getVen_Loc_ID());
        lData.setAI_Customer_Map_ID(getAI_Customer_Map_ID());
        return lData;
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pAI_Location_MapEntity The Value Object containing the AI_Location_MapEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(AI_Location_MapEntityData pAI_Location_MapEntity) throws InvalidValueException {
        if (pAI_Location_MapEntity == null) {
            throw new InvalidValueException("object.undefined", "AI_Location_MapEntity");
        }
        if (pAI_Location_MapEntity.getAI_Location_Map_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "AI_Location_MapEntity", "Id" });
        }
        setCustomer_ID(pAI_Location_MapEntity.getCustomer_ID());
        setVendor_ID(pAI_Location_MapEntity.getVendor_ID());
        setPlace_ID(pAI_Location_MapEntity.getPlace_ID());
        setVen_Loc_ID(pAI_Location_MapEntity.getVen_Loc_ID());
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "AI_Location_MapEntityBean [ " + getValueObject() + " ]";
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
        String lSequenceField = "AI_Location_Map_ID";
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
   * Retrieve the AI_Location_MapEntity's AI_Location_Map_ID.
   *
   * @return Returns an Integer representing the AI_Location_Map_ID of this AI_Location_MapEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="AI_Location_Map_ID"
   **/
    public abstract Integer getAI_Location_Map_ID();

    /**
   * Set the AI_Location_MapEntity's AI_Location_Map_ID.
   *
   * @param pAI_Location_Map_ID The AI_Location_Map_ID of this AI_Location_MapEntity. Is set at creation time.
   **/
    public abstract void setAI_Location_Map_ID(java.lang.Integer pAI_Location_Map_ID);

    /**
   * Retrieve the AI_Location_MapEntity's Customer_ID.
   *
   * @return Returns an Integer representing the Customer_ID of this AI_Location_MapEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Customer_ID"
   **/
    public abstract Integer getCustomer_ID();

    /**
   * Set the AI_Location_MapEntity's Customer_ID.
   *
   * @param pAI_Location_Map_ID The Customer_ID of this AI_Location_MapEntity.
   **/
    public abstract void setCustomer_ID(java.lang.Integer pCustomer_ID);

    /**
   * Retrieve the AI_Location_MapEntity's Vendor_ID.
   *
   * @return Returns an Integer representing the Vendor_ID of this AI_Location_MapEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Vendor_ID"
   **/
    public abstract Integer getVendor_ID();

    /**
   * Set the AI_Location_MapEntity's Vendor_ID.
   *
   * @param pAI_Location_Map_ID The Vendor_ID of this AI_Location_MapEntity.
   **/
    public abstract void setVendor_ID(java.lang.Integer pVendor_ID);

    /**
   * Retrieve the AI_Location_MapEntity's AI_Customer_Map_ID.
   *
   * @return Returns an Integer representing the AI_Customer_Map_ID of this AI_Location_MapEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="AI_Customer_Map_ID"
   **/
    public abstract Integer getAI_Customer_Map_ID();

    /**
   * Set the AI_Location_MapEntity's AI_Customer_Map_ID.
   *
   * @param pAI_Customer_Map_ID The AI_Customer_Map_ID of this AI_Location_MapEntity. Is set at creation time.
   **/
    public abstract void setAI_Customer_Map_ID(java.lang.Integer pAI_Customer_Map_ID);

    /**
   * Retrieve the AI_Location_MapEntity's Place_ID.
   *
   * @return Returns an Integer representing the Place_ID of this AI_Location_MapEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Place_ID"
   **/
    public abstract Integer getPlace_ID();

    /**
   * Set the AI_Location_MapEntity's Place_ID.
   *
   * @param pAI_Location_Map_ID The Place_ID of this AI_Location_MapEntity.
   **/
    public abstract void setPlace_ID(java.lang.Integer pPlace_ID);

    /**
   * Retrieve the AI_Location_MapEntity's Ven_Loc_ID.
   *
   * @return Returns an Integer representing the Ven_Loc_ID of this AI_Location_MapEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Ven_Loc_ID"
   **/
    public abstract String getVen_Loc_ID();

    /**
   * Set the AI_Location_MapEntity's Ven_Loc_ID.
   *
   * @param pAI_Location_Map_ID The Ven_Loc_ID of this AI_Location_MapEntity.
   **/
    public abstract void setVen_Loc_ID(java.lang.String pVen_Loc_ID);

    /**
   * Create a AI_Location_MapEntity based on the supplied AI_Location_MapEntity Value Object.
   *
   * @param pAI_Location_MapEntity The data used to create the AI_Location_MapEntity.
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
    public AI_Location_MapEntityPK ejbCreate(AI_Location_MapEntityData pAI_Location_MapEntity) throws InvalidValueException, EJBException, CreateException {
        AI_Location_MapEntityData lData = (AI_Location_MapEntityData) pAI_Location_MapEntity.clone();
        try {
            lData.setAI_Location_Map_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(AI_Location_MapEntityData pAI_Location_MapEntity) {
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
