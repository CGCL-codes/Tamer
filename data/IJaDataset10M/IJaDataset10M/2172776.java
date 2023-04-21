package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.BL_InvoicePatientInfoEntity;
import com.medcentrex.bridge.interfaces.BL_InvoicePatientInfoEntityData;
import com.medcentrex.bridge.interfaces.BL_InvoicePatientInfoEntityHome;
import com.medcentrex.bridge.interfaces.BL_InvoicePatientInfoEntityPK;
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
 * The Entity bean represents a BL_InvoicePatientInfoEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="BL_InvoicePatientInfoEntity"
 *           display-name="BL_InvoicePatientInfoEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/BL_InvoicePatientInfoEntity"
 * 			schema="BL_InvoicePatientInfo"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="bl_invoicepatientinfo"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"

 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findByClaim_ID( java.lang.Integer pClaim_ID )"
 * query="SELECT OBJECT(ob) FROM BL_InvoicePatientInfo ob WHERE ob.claim_ID=?1"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="bl_invoicepatientinfo"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class BL_InvoicePatientInfoEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pBL_InvoicePatientInfoEntity The Value Object containing the BL_InvoicePatientInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(BL_InvoicePatientInfoEntityData pBL_InvoicePatientInfoEntity) throws InvalidValueException {
        if (pBL_InvoicePatientInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "BL_InvoicePatientInfoEntity");
        }
        if (pBL_InvoicePatientInfoEntity.getClaim_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "BL_InvoicePatientInfoEntity", "Id" });
        }
        setClaim_ID(pBL_InvoicePatientInfoEntity.getClaim_ID());
        setCustOrgUnit_OID(pBL_InvoicePatientInfoEntity.getCustOrgUnit_OID());
        setPatientDOB(pBL_InvoicePatientInfoEntity.getPatientDOB());
        setAddress1(pBL_InvoicePatientInfoEntity.getAddress1());
        setAddress2(pBL_InvoicePatientInfoEntity.getAddress2());
        setCity(pBL_InvoicePatientInfoEntity.getCity());
        setZipCode(pBL_InvoicePatientInfoEntity.getZipCode());
        setState(pBL_InvoicePatientInfoEntity.getState());
        setHomePhone(pBL_InvoicePatientInfoEntity.getHomePhone());
        setPatientStatusMarried(pBL_InvoicePatientInfoEntity.getPatientStatusMarried());
        setPatientStatusEmployType(pBL_InvoicePatientInfoEntity.getPatientStatusEmployType());
        setPatient_id(pBL_InvoicePatientInfoEntity.getPatient_id());
        setPatientSex(pBL_InvoicePatientInfoEntity.getPatientSex());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pBL_InvoicePatientInfoEntity The Value Object containing the BL_InvoicePatientInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(BL_InvoicePatientInfoEntityData pBL_InvoicePatientInfoEntity) throws InvalidValueException {
        if (pBL_InvoicePatientInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "BL_InvoicePatientInfoEntity");
        }
        if (pBL_InvoicePatientInfoEntity.getClaim_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "BL_InvoicePatientInfoEntity", "Id" });
        }
        setCustOrgUnit_OID(pBL_InvoicePatientInfoEntity.getCustOrgUnit_OID());
        setPatientDOB(pBL_InvoicePatientInfoEntity.getPatientDOB());
        setAddress1(pBL_InvoicePatientInfoEntity.getAddress1());
        setAddress2(pBL_InvoicePatientInfoEntity.getAddress2());
        setCity(pBL_InvoicePatientInfoEntity.getCity());
        setZipCode(pBL_InvoicePatientInfoEntity.getZipCode());
        setState(pBL_InvoicePatientInfoEntity.getState());
        setHomePhone(pBL_InvoicePatientInfoEntity.getHomePhone());
        setPatientStatusMarried(pBL_InvoicePatientInfoEntity.getPatientStatusMarried());
        setPatientStatusEmployType(pBL_InvoicePatientInfoEntity.getPatientStatusEmployType());
        setPatient_id(pBL_InvoicePatientInfoEntity.getPatient_id());
        setPatientSex(pBL_InvoicePatientInfoEntity.getPatientSex());
    }

    /**
   * Create and return a BL_InvoicePatientInfoEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a BL_InvoicePatientInfoEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public BL_InvoicePatientInfoEntityData getValueObject() {
        BL_InvoicePatientInfoEntityData lData = new BL_InvoicePatientInfoEntityData();
        lData.setClaim_ID(getClaim_ID());
        lData.setCustOrgUnit_OID(getCustOrgUnit_OID());
        lData.setPatientDOB(getPatientDOB());
        lData.setAddress1(getAddress1());
        lData.setAddress2(getAddress2());
        lData.setCity(getCity());
        lData.setZipCode(getZipCode());
        lData.setState(getState());
        lData.setHomePhone(getHomePhone());
        lData.setPatientStatusMarried(getPatientStatusMarried());
        lData.setPatientStatusEmployType(getPatientStatusEmployType());
        lData.setPatient_id(getPatient_id());
        lData.setPatientSex(getPatientSex());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "BL_InvoicePatientInfoEntityBean [ " + getValueObject() + " ]";
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
        String lSequenceField = "Patient_History_ID";
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
   * Retrieve the BL_InvoicePatientInfoEntity's Claim_ID.
   *
   * @return Returns an Integer representing the Claim_ID of this BL_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Claim_ID"
   **/
    public abstract Integer getClaim_ID();

    /**
   * Set the BL_InvoicePatientInfoEntity's Claim_ID.
   *
   * @param pClaim_ID The Claim_ID of this BL_InvoicePatientInfoEntity.
   **/
    public abstract void setClaim_ID(java.lang.Integer Claim_ID);

    /**
   * Retrieve the BL_InvoicePatientInfoEntity's CustOrgUnit_OID.
   *
   * @return Returns an Integer representing the CustOrgUnit_OID of this BL_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="CustOrgUnit_OID"
   **/
    public abstract Integer getCustOrgUnit_OID();

    /**
   * Set the BL_InvoicePatientInfoEntity's CustOrgUnit_OID.
   *
   * @param pCustOrgUnit_OID The CustOrgUnit_OID of this BL_InvoicePatientInfoEntity.  Is set at creation time.
   **/
    public abstract void setCustOrgUnit_OID(java.lang.Integer pCustOrgUnit_OID);

    /**
   * Retrieve the BL_InvoicePatientInfoEntity's PatientDOB.
   *
   * @return Returns an Integer representing the PatientDOB of this BL_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientDOB"
   **/
    public abstract Date getPatientDOB();

    /**
   * Set the BL_InvoicePatientInfoEntity's PatientDOB.
   *
   * @param pPatientDOB The PatientDOB of this BL_InvoicePatientInfoEntity.  Is set at creation time.
   **/
    public abstract void setPatientDOB(Date pPatientDOB);

    /**
   * @return Returns the 1Address of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address1"
   **/
    public abstract String getAddress1();

    /**
   * Specify the Address1 of this BL_InvoicePatientInfoEntity
   *
   * @param pAddress1 Address1 of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setAddress1(java.lang.String pAddress1);

    /**
   * @return Returns the Address2 of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address2"
   **/
    public abstract String getAddress2();

    /**
   * Specify the Address2 of this BL_InvoicePatientInfoEntity
   *
   * @param pAddress2 Address2 of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setAddress2(java.lang.String pAddress2);

    /**
   * @return Returns the City of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="City"
   **/
    public abstract String getCity();

    /**
   * Specify the City of this BL_InvoicePatientInfoEntity
   *
   * @param pCity City of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setCity(java.lang.String pCity);

    /**
   * @return Returns the ZIPCode of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="ZipCode"
   **/
    public abstract String getZipCode();

    /**
   * Specify the ZipCode of this BL_InvoicePatientInfoEntity
   *
   * @param pZIPCode ZipCode of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setZipCode(java.lang.String pZIPCode);

    /**
   * @return Returns the State of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="State"
   **/
    public abstract String getState();

    /**
   * Specify the State of this BL_InvoicePatientInfoEntity
   *
   * @param pState State of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setState(java.lang.String pState);

    /**
   * @return Returns the HomePhone of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="HomePhone"
   **/
    public abstract String getHomePhone();

    /**
   * Specify the HomePhone of this BL_InvoicePatientInfoEntity
   *
   * @param pHomePhone HomePhone of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setHomePhone(java.lang.String pHomePhone);

    /**
   * @return Returns the PatientStatusMarried of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientStatusMarried"
   **/
    public abstract String getPatientStatusMarried();

    /**
   * Specify the PatientStatusMarried of this BL_InvoicePatientInfoEntity
   *
   * @param pPatientStatusMarried PatientStatusMarried of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setPatientStatusMarried(java.lang.String pPatientStatusMarried);

    /**
   * @return Returns the PatientStatusEmployType of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientStatusEmployType"
   **/
    public abstract String getPatientStatusEmployType();

    /**
   * Specify the PatientStatusEmployType of this BL_InvoicePatientInfoEntity
   *
   * @param pPatientStatusEmployType PatientStatusEmployType of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setPatientStatusEmployType(java.lang.String pPatientStatusEmployType);

    /**
   * Retrieve the BL_InvoicePatientInfoEntity's patient_id.
   *
   * @return Returns an Integer representing the patient_id of this BL_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="patient_id"
   **/
    public abstract Integer getPatient_id();

    /**
   * Set the BL_InvoicePatientInfoEntity's patient_id.
   *
   * @param ppatient_id The patient_id of this BL_InvoicePatientInfoEntity.
   **/
    public abstract void setPatient_id(java.lang.Integer patient_id);

    /**
   * @return Returns the PatientSex of this BL_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientSex"
   **/
    public abstract String getPatientSex();

    /**
   * Specify the PatientSex of this BL_InvoicePatientInfoEntity
   *
   * @param pPatientSex PatientStatusEmployType of this BL_InvoicePatientInfoEntity
   **/
    public abstract void setPatientSex(java.lang.String pPatientSex);

    /**
   * Create a BL_InvoicePatientInfoEntity based on the supplied BL_InvoicePatientInfoEntity Value Object.
   *
   * @param pBL_InvoicePatientInfoEntity The data used to create the BL_InvoicePatientInfoEntity.
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
    public BL_InvoicePatientInfoEntityPK ejbCreate(BL_InvoicePatientInfoEntityData pBL_InvoicePatientInfoEntity) throws InvalidValueException, EJBException, CreateException {
        BL_InvoicePatientInfoEntityData lData = (BL_InvoicePatientInfoEntityData) pBL_InvoicePatientInfoEntity.clone();
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(BL_InvoicePatientInfoEntityData pBL_InvoicePatientInfoEntity) {
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
