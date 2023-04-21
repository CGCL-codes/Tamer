package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.PlacesEntity;
import com.medcentrex.interfaces.PlacesEntityData;
import com.medcentrex.interfaces.PlacesEntityHome;
import com.medcentrex.interfaces.PlacesEntityPK;
import com.medcentrex.interfaces.ServiceUnavailableException;
import com.medcentrex.interfaces.SequenceGenerator;
import com.medcentrex.interfaces.SequenceGeneratorHome;
import java.sql.Date;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.sql.DataSource;
import com.medcentrex.interfaces.OIDEntity;
import com.medcentrex.interfaces.OIDEntityHome;
import com.medcentrex.interfaces.OIDEntityData;

/**
 * The Entity bean represents a PlacesEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="PlacesEntity"
 *           display-name="PlacesEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/PlacesEntity"
 * 			schema="places"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="Places"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:ejb-ref ejb-name="OIDEntity"
 *				ref-name="com/medcentrex/OIDEntity"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.interfaces.PlacesEntity findByPlace_ID(java.lang.Integer pPerson_ID)"
 * query="select object(ob) from places ob where ob.place_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="places"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class PlacesEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPlacesEntity The Value Object containing the PlacesEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(PlacesEntityData pPlacesEntity) throws InvalidValueException {
        if (pPlacesEntity == null) {
            throw new InvalidValueException("object.undefined", "PlacesEntity");
        }
        if (pPlacesEntity.getPlace_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "PlacesEntity", "Id" });
        }
        setPlace_ID(pPlacesEntity.getPlace_ID());
        setDescription(pPlacesEntity.getDescription());
        setPlace_Name(pPlacesEntity.getPlace_Name());
        setAddress1(pPlacesEntity.getAddress1());
        setAddress2(pPlacesEntity.getAddress2());
        setCity(pPlacesEntity.getCity());
        setZip(pPlacesEntity.getZip());
        setState(pPlacesEntity.getState());
        setCountry_ID(pPlacesEntity.getCountry_ID());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPlacesEntity The Value Object containing the PlacesEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(PlacesEntityData pPlacesEntity) throws InvalidValueException {
        if (pPlacesEntity == null) {
            throw new InvalidValueException("object.undefined", "PlacesEntity");
        }
        if (pPlacesEntity.getPlace_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "PlacesEntity", "Id" });
        }
        setDescription(pPlacesEntity.getDescription());
        setPlace_Name(pPlacesEntity.getPlace_Name());
        setAddress1(pPlacesEntity.getAddress1());
        setAddress2(pPlacesEntity.getAddress2());
        setCity(pPlacesEntity.getCity());
        setZip(pPlacesEntity.getZip());
        setState(pPlacesEntity.getState());
        setCountry_ID(pPlacesEntity.getCountry_ID());
    }

    /**
   * Create and return a PlacesEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a PlacesEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public PlacesEntityData getValueObject() {
        PlacesEntityData lData = new PlacesEntityData();
        lData.setPlace_ID(getPlace_ID());
        lData.setDescription(getDescription());
        lData.setPlace_Name(getPlace_Name());
        lData.setAddress1(getAddress1());
        lData.setAddress2(getAddress2());
        lData.setCity(getCity());
        lData.setZip(getZip());
        lData.setState(getState());
        lData.setCountry_ID(getCountry_ID());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "PlacesEntityBean [ " + getValueObject() + " ]";
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
    private Integer generateUniqueId(PlacesEntityData pPlacesEntity) throws ServiceUnavailableException {
        Integer lUniqueId = new Integer(-1);
        try {
            Context lContext = new InitialContext();
            OIDEntityHome lHome = (OIDEntityHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/OIDEntity"), OIDEntityHome.class);
            OIDEntityData lOIDData = new OIDEntityData();
            lOIDData.setCustomer_ID(pPlacesEntity.getLoginSession().getCustomer_ID());
            OIDEntity lOID = lHome.create(lOIDData);
            lUniqueId = lOID.getValueObject().getOID();
        } catch (NamingException ne) {
            throw new ServiceUnavailableException("Naming lookup failure: " + ne.getMessage());
        } catch (CreateException ce) {
            throw new ServiceUnavailableException("Failure while creating a generator session bean: " + ce.getMessage());
        } catch (InvalidValueException ive) {
            throw new ServiceUnavailableException("Failure during creating bean due to Invalid Value: " + ive.getMessage());
        } catch (RemoteException rte) {
            throw new ServiceUnavailableException("Remote exception occured while accessing generator session bean: " + rte.getMessage());
        }
        System.out.println("OID::" + lUniqueId);
        return lUniqueId;
    }

    /**
   * Retrieve the PlacesEntity's Place_ID.
   *
   * @return Returns an Integer representing the Place_ID of this PlacesEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Place_ID"
   **/
    public abstract Integer getPlace_ID();

    /**
   * Set the PlacesEntity's Place_ID.
   *
   * @param pPlace_ID The Place_ID of this PlacesEntity. Is set at creation time.
   **/
    public abstract void setPlace_ID(java.lang.Integer pPlace_ID);

    /**
   * Retrieve the PlacesEntity's Place_Name.
   *
   * @return Returns an Integer representing the Place_Name of this PlacesEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Place_Name"
   **/
    public abstract String getPlace_Name();

    /**
   * Set the PlacesEntity's Place_Name.
   *
   * @param pPlace_Name The Place_Name of this PlacesEntity.  Is set at creation time.
   **/
    public abstract void setPlace_Name(java.lang.String pPlace_Name);

    /**
   * Retrieve the PlacesEntity's Description.
   *
   * @return Returns an Integer representing the Description of this PlacesEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Description"
   **/
    public abstract String getDescription();

    /**
   * Set the PlacesEntity's Description.
   *
   * @param pDescription The Description of this PlacesEntity.  Is set at creation time.
   **/
    public abstract void setDescription(java.lang.String pDescription);

    /**
   * @return Returns the Address1 of this PlacesEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address1"
   **/
    public abstract String getAddress1();

    /**
   * Specify the Address1 of this PlacesEntity
   *
   * @param pAddress1 Address1 of this PlacesEntity
   **/
    public abstract void setAddress1(java.lang.String pAddress1);

    /**
   * @return Returns the Address2 of this PlacesEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address2"
   **/
    public abstract String getAddress2();

    /**
   * Specify the Address2 of this PlacesEntity
   *
   * @param pAddress2 Address2 of this PlacesEntity
   **/
    public abstract void setAddress2(java.lang.String pAddress2);

    /**
   * @return Returns the City of this PlacesEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="City"
   **/
    public abstract String getCity();

    /**
   * Specify the City of this PlacesEntity
   *
   * @param pCity City of this PlacesEntity
   **/
    public abstract void setCity(java.lang.String pCity);

    /**
   * @return Returns the ZIP of this PlacesEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Zip"
   **/
    public abstract String getZip();

    /**
   * Specify the Zip of this PlacesEntity
   *
   * @param pZIP Zip of this PlacesEntity
   **/
    public abstract void setZip(java.lang.String pZIP);

    /**
   * @return Returns the State of this PlacesEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="State"
   **/
    public abstract String getState();

    /**
   * Specify the State of this PlacesEntity
   *
   * @param pState State of this PlacesEntity
   **/
    public abstract void setState(java.lang.String pState);

    /**
   * @return Returns the Country_ID of this PlacesEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Country_ID"
   **/
    public abstract Integer getCountry_ID();

    /**
   * Specify the Country_ID of this PlacesEntity
   *
   * @param pCountry Country_ID of this PlacesEntity
   **/
    public abstract void setCountry_ID(java.lang.Integer pCountry);

    /**
   * Create a PlacesEntity based on the supplied PlacesEntity Value Object.
   *
   * @param pPlacesEntity The data used to create the PlacesEntity.
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
    public PlacesEntityPK ejbCreate(PlacesEntityData pPlacesEntity) throws InvalidValueException, EJBException, CreateException {
        PlacesEntityData lData = (PlacesEntityData) pPlacesEntity.clone();
        try {
            lData.setPlace_ID(generateUniqueId(pPlacesEntity));
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(PlacesEntityData pPlacesEntity) {
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
