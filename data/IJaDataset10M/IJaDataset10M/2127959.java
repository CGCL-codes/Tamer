package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.CT_InvoicePatientInfoEntity;
import com.medcentrex.interfaces.CT_InvoicePatientInfoEntityData;
import com.medcentrex.interfaces.CT_InvoicePatientInfoEntityHome;
import com.medcentrex.interfaces.CT_InvoicePatientInfoEntityPK;
import com.medcentrex.interfaces.ServiceUnavailableException;
import com.medcentrex.interfaces.SequenceGenerator;
import com.medcentrex.interfaces.SequenceGeneratorHome;
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
 * The Entity bean represents a CT_InvoicePatientInfoEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="CT_InvoicePatientInfoEntity"
 *           display-name="CT_InvoicePatientInfoEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/CT_InvoicePatientInfoEntity"
 * 			schema="ct_invoicepatientinfo"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="CT_InvoicePatientInfo"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findByChargeTicket_ID( java.lang.Integer pChargeTicket_ID )"
 * query="select object(ob) from ct_invoicepatientinfo ob where ob.chargeTicket_ID=?1"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="ct_invoicepatientinfo"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class CT_InvoicePatientInfoEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pCT_InvoicePatientInfoEntity The Value Object containing the CT_InvoicePatientInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(CT_InvoicePatientInfoEntityData pCT_InvoicePatientInfoEntity) throws InvalidValueException {
        if (pCT_InvoicePatientInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "CT_InvoicePatientInfoEntity");
        }
        if (pCT_InvoicePatientInfoEntity.getChargeTicket_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "CT_InvoicePatientInfoEntity", "Id" });
        }
        setChargeTicket_ID(pCT_InvoicePatientInfoEntity.getChargeTicket_ID());
        setPatientDOB(pCT_InvoicePatientInfoEntity.getPatientDOB());
        setAddress1(pCT_InvoicePatientInfoEntity.getAddress1());
        setAddress2(pCT_InvoicePatientInfoEntity.getAddress2());
        setCity(pCT_InvoicePatientInfoEntity.getCity());
        setZipCode(pCT_InvoicePatientInfoEntity.getZipCode());
        setState(pCT_InvoicePatientInfoEntity.getState());
        setHomePhone(pCT_InvoicePatientInfoEntity.getHomePhone());
        setPatientStatusMarried(pCT_InvoicePatientInfoEntity.getPatientStatusMarried());
        setPatientStatusEmployType(pCT_InvoicePatientInfoEntity.getPatientStatusEmployType());
        setPatient_id(pCT_InvoicePatientInfoEntity.getPatient_id());
        setPatientSex(pCT_InvoicePatientInfoEntity.getPatientSex());
        setMRN(pCT_InvoicePatientInfoEntity.getMRN());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pCT_InvoicePatientInfoEntity The Value Object containing the CT_InvoicePatientInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(CT_InvoicePatientInfoEntityData pCT_InvoicePatientInfoEntity) throws InvalidValueException {
        if (pCT_InvoicePatientInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "CT_InvoicePatientInfoEntity");
        }
        if (pCT_InvoicePatientInfoEntity.getChargeTicket_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "CT_InvoicePatientInfoEntity", "Id" });
        }
        setPatientDOB(pCT_InvoicePatientInfoEntity.getPatientDOB());
        setAddress1(pCT_InvoicePatientInfoEntity.getAddress1());
        setAddress2(pCT_InvoicePatientInfoEntity.getAddress2());
        setCity(pCT_InvoicePatientInfoEntity.getCity());
        setZipCode(pCT_InvoicePatientInfoEntity.getZipCode());
        setState(pCT_InvoicePatientInfoEntity.getState());
        setHomePhone(pCT_InvoicePatientInfoEntity.getHomePhone());
        setPatientStatusMarried(pCT_InvoicePatientInfoEntity.getPatientStatusMarried());
        setPatientStatusEmployType(pCT_InvoicePatientInfoEntity.getPatientStatusEmployType());
        setPatient_id(pCT_InvoicePatientInfoEntity.getPatient_id());
        setPatientSex(pCT_InvoicePatientInfoEntity.getPatientSex());
        setMRN(pCT_InvoicePatientInfoEntity.getMRN());
    }

    /**
   * Create and return a CT_InvoicePatientInfoEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a CT_InvoicePatientInfoEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public CT_InvoicePatientInfoEntityData getValueObject() {
        CT_InvoicePatientInfoEntityData lData = new CT_InvoicePatientInfoEntityData();
        lData.setChargeTicket_ID(getChargeTicket_ID());
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
        lData.setMRN(getMRN());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "CT_InvoicePatientInfoEntityBean [ " + getValueObject() + " ]";
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
        String lSequenceField = "ChargeTicket_ID";
        try {
            Context lContext = new InitialContext();
            String lSequenceName = (java.lang.String) lContext.lookup("java:comp/env/SequenceName");
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/SequenceGenerator"), SequenceGeneratorHome.class);
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
   * Retrieve the CT_InvoicePatientInfoEntity's ChargeTicket_ID.
   *
   * @return Returns an Integer representing the ChargeTicket_ID of this CT_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="ChargeTicket_ID"
   **/
    public abstract Integer getChargeTicket_ID();

    /**
   * Set the CT_InvoicePatientInfoEntity's ChargeTicket_ID.
   *
   * @param pChargeTicket_ID The ChargeTicket_ID of this CT_InvoicePatientInfoEntity.
   **/
    public abstract void setChargeTicket_ID(java.lang.Integer ChargeTicket_ID);

    /**
   * Retrieve the CT_InvoicePatientInfoEntity's PatientDOB.
   *
   * @return Returns an Integer representing the PatientDOB of this CT_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientDOB"
   **/
    public abstract Date getPatientDOB();

    /**
   * Set the CT_InvoicePatientInfoEntity's PatientDOB.
   *
   * @param pPatientDOB The PatientDOB of this CT_InvoicePatientInfoEntity.  Is set at creation time.
   **/
    public abstract void setPatientDOB(Date pPatientDOB);

    /**
   * @return Returns the 1Address of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address1"
   **/
    public abstract String getAddress1();

    /**
   * Specify the Address1 of this CT_InvoicePatientInfoEntity
   *
   * @param pAddress1 Address1 of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setAddress1(java.lang.String pAddress1);

    /**
   * @return Returns the Address2 of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address2"
   **/
    public abstract String getAddress2();

    /**
   * Specify the Address2 of this CT_InvoicePatientInfoEntity
   *
   * @param pAddress2 Address2 of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setAddress2(java.lang.String pAddress2);

    /**
   * @return Returns the City of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="City"
   **/
    public abstract String getCity();

    /**
   * Specify the City of this CT_InvoicePatientInfoEntity
   *
   * @param pCity City of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setCity(java.lang.String pCity);

    /**
   * @return Returns the ZIPCode of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="ZipCode"
   **/
    public abstract String getZipCode();

    /**
   * Specify the ZipCode of this CT_InvoicePatientInfoEntity
   *
   * @param pZIPCode ZipCode of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setZipCode(java.lang.String pZIPCode);

    /**
   * @return Returns the State of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="State"
   **/
    public abstract String getState();

    /**
   * Specify the State of this CT_InvoicePatientInfoEntity
   *
   * @param pState State of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setState(java.lang.String pState);

    /**
   * @return Returns the HomePhone of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="HomePhone"
   **/
    public abstract String getHomePhone();

    /**
   * Specify the HomePhone of this CT_InvoicePatientInfoEntity
   *
   * @param pHomePhone HomePhone of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setHomePhone(java.lang.String pHomePhone);

    /**
   * @return Returns the PatientStatusMarried of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientStatusMarried"
   **/
    public abstract String getPatientStatusMarried();

    /**
   * Specify the PatientStatusMarried of this CT_InvoicePatientInfoEntity
   *
   * @param pPatientStatusMarried PatientStatusMarried of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setPatientStatusMarried(java.lang.String pPatientStatusMarried);

    /**
   * @return Returns the PatientStatusEmployType of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientStatusEmployType"
   **/
    public abstract String getPatientStatusEmployType();

    /**
   * Specify the PatientStatusEmployType of this CT_InvoicePatientInfoEntity
   *
   * @param pPatientStatusEmployType PatientStatusEmployType of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setPatientStatusEmployType(java.lang.String pPatientStatusEmployType);

    /**
   * Retrieve the CT_InvoicePatientInfoEntity's patient_id.
   *
   * @return Returns an Integer representing the patient_id of this CT_InvoicePatientInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="patient_id"
   **/
    public abstract Integer getPatient_id();

    /**
   * Set the CT_InvoicePatientInfoEntity's patient_id.
   *
   * @param ppatient_id The patient_id of this CT_InvoicePatientInfoEntity.
   **/
    public abstract void setPatient_id(java.lang.Integer patient_id);

    /**
   * @return Returns the PatientSex of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PatientSex"
   **/
    public abstract String getPatientSex();

    /**
   * Specify the PatientSex of this CT_InvoicePatientInfoEntity
   *
   * @param pPatientSex PatientStatusEmployType of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setPatientSex(java.lang.String pPatientSex);

    /**
   * @return Returns the mrn of this CT_InvoicePatientInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="mrn"
   **/
    public abstract String getMRN();

    /**
   * Specify the mrn of this CT_InvoicePatientInfoEntity
   *
   * @param pMRN PatientStatusEmployType of this CT_InvoicePatientInfoEntity
   **/
    public abstract void setMRN(java.lang.String pMRN);

    /**
   * Create a CT_InvoicePatientInfoEntity based on the supplied CT_InvoicePatientInfoEntity Value Object.
   *
   * @param pCT_InvoicePatientInfoEntity The data used to create the CT_InvoicePatientInfoEntity.
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
    public CT_InvoicePatientInfoEntityPK ejbCreate(CT_InvoicePatientInfoEntityData pCT_InvoicePatientInfoEntity) throws InvalidValueException, EJBException, CreateException {
        CT_InvoicePatientInfoEntityData lData = (CT_InvoicePatientInfoEntityData) pCT_InvoicePatientInfoEntity.clone();
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(CT_InvoicePatientInfoEntityData pCT_InvoicePatientInfoEntity) {
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
