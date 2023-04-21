package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.BL_InvoiceOtherInsuranceInfoEntity;
import com.medcentrex.interfaces.BL_InvoiceOtherInsuranceInfoEntityData;
import com.medcentrex.interfaces.BL_InvoiceOtherInsuranceInfoEntityHome;
import com.medcentrex.interfaces.BL_InvoiceOtherInsuranceInfoEntityPK;
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
 * The Entity bean represents a BL_InvoiceOtherInsuranceInfoEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="BL_InvoiceOtherInsuranceInfoEntity"
 *           display-name="BL_InvoiceOtherInsuranceInfoEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/BL_InvoiceOtherInsuranceInfoEntity"
 * 			schema="bl_invoiceotherinsuranceinfo"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="BL_InvoiceOtherInsuranceInfo"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findByClaim_OID( java.lang.Integer pClaim_ID )"
 * query="select object(ob) from bl_invoiceotherinsuranceinfo ob where ob.claim_OID=?1"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="bl_invoiceotherinsuranceinfo"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class BL_InvoiceOtherInsuranceInfoEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pBL_InvoiceOtherInsuranceInfoEntity The Value Object containing the BL_InvoiceOtherInsuranceInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(BL_InvoiceOtherInsuranceInfoEntityData pBL_InvoiceOtherInsuranceInfoEntity) throws InvalidValueException {
        if (pBL_InvoiceOtherInsuranceInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "BL_InvoiceOtherInsuranceInfoEntity");
        }
        if (pBL_InvoiceOtherInsuranceInfoEntity.getClaim_OID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "BL_InvoiceOtherInsuranceInfoEntity", "Id" });
        }
        setClaim_OID(pBL_InvoiceOtherInsuranceInfoEntity.getClaim_OID());
        setCustOrgUnit_OID(pBL_InvoiceOtherInsuranceInfoEntity.getCustOrgUnit_OID());
        setOtherInsuranceDOB(pBL_InvoiceOtherInsuranceInfoEntity.getOtherInsuranceDOB());
        setPolicyNumber(pBL_InvoiceOtherInsuranceInfoEntity.getPolicyNumber());
        setSex(pBL_InvoiceOtherInsuranceInfoEntity.getSex());
        setEmployerName(pBL_InvoiceOtherInsuranceInfoEntity.getEmployerName());
        setInsuranceCoName(pBL_InvoiceOtherInsuranceInfoEntity.getInsuranceCoName());
        setOtherInsuredName(pBL_InvoiceOtherInsuranceInfoEntity.getOtherInsuredName());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pBL_InvoiceOtherInsuranceInfoEntity The Value Object containing the BL_InvoiceOtherInsuranceInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(BL_InvoiceOtherInsuranceInfoEntityData pBL_InvoiceOtherInsuranceInfoEntity) throws InvalidValueException {
        if (pBL_InvoiceOtherInsuranceInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "BL_InvoiceOtherInsuranceInfoEntity");
        }
        if (pBL_InvoiceOtherInsuranceInfoEntity.getClaim_OID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "BL_InvoiceOtherInsuranceInfoEntity", "Id" });
        }
        setCustOrgUnit_OID(pBL_InvoiceOtherInsuranceInfoEntity.getCustOrgUnit_OID());
        setOtherInsuranceDOB(pBL_InvoiceOtherInsuranceInfoEntity.getOtherInsuranceDOB());
        setPolicyNumber(pBL_InvoiceOtherInsuranceInfoEntity.getPolicyNumber());
        setSex(pBL_InvoiceOtherInsuranceInfoEntity.getSex());
        setEmployerName(pBL_InvoiceOtherInsuranceInfoEntity.getEmployerName());
        setInsuranceCoName(pBL_InvoiceOtherInsuranceInfoEntity.getInsuranceCoName());
        setOtherInsuredName(pBL_InvoiceOtherInsuranceInfoEntity.getOtherInsuredName());
    }

    /**
   * Create and return a BL_InvoiceOtherInsuranceInfoEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a BL_InvoiceOtherInsuranceInfoEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public BL_InvoiceOtherInsuranceInfoEntityData getValueObject() {
        BL_InvoiceOtherInsuranceInfoEntityData lData = new BL_InvoiceOtherInsuranceInfoEntityData();
        lData.setClaim_OID(getClaim_OID());
        lData.setCustOrgUnit_OID(getCustOrgUnit_OID());
        lData.setPolicyNumber(getPolicyNumber());
        lData.setSex(getSex());
        lData.setEmployerName(getEmployerName());
        lData.setInsuranceCoName(getInsuranceCoName());
        lData.setOtherInsuredName(getOtherInsuredName());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "BL_InvoiceOtherInsuranceInfoEntityBean [ " + getValueObject() + " ]";
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
   * Retrieve the BL_InvoiceInsuredInfoEntity's Claim_OID.
   *
   * @return Returns an Integer representing the Claim_OID of this BL_InvoiceInsuredInfoEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Claim_OID"
   **/
    public abstract Integer getClaim_OID();

    /**
   * Set the BL_InvoiceInsuredInfoEntity's Claim_OID.
   *
   * @param pClaim_OID The Claim_OID of this BL_InvoiceInsuredInfoEntity. Is set at creation time.
   **/
    public abstract void setClaim_OID(java.lang.Integer pClaim_OID);

    /**
   * Retrieve the BL_InvoiceInsuredInfoEntity's CustOrgUnit_OID.
   *
   * @return Returns an Integer representing the CustOrgUnit_OID of this BL_InvoiceInsuredInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="CustOrgUnit_OID"
   **/
    public abstract Integer getCustOrgUnit_OID();

    /**
   * Set the BL_InvoiceInsuredInfoEntity's CustOrgUnit_OID.
   *
   * @param pCustOrgUnit_OID The CustOrgUnit_OID of this BL_InvoiceInsuredInfoEntity.  Is set at creation time.
   **/
    public abstract void setCustOrgUnit_OID(java.lang.Integer pCustOrgUnit_OID);

    /**
   * Retrieve the BL_InvoiceInsuredInfoEntity's PolicyNumber.
   *
   * @return Returns an Integer representing the PolicyNumber of this BL_InvoiceInsuredInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PolicyNumber"
   **/
    public abstract String getPolicyNumber();

    /**
   * Set the BL_InvoiceInsuredInfoEntity's PolicyNumber.
   *
   * @param pPolicyNumber The PolicyNumber of this BL_InvoiceInsuredInfoEntity.  Is set at creation time.
   **/
    public abstract void setPolicyNumber(java.lang.String pPolicyNumber);

    /**
   * Retrieve the BL_InvoiceOtherInsuranceInfoEntity's Sex.
   *
   * @return Returns an Integer representing the Sex of this BL_InvoiceOtherInsuranceInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Sex"
   **/
    public abstract String getSex();

    /**
   * Set the BL_InvoiceOtherInsuranceInfoEntity's Sex.
   *
   * @param pSex The Sex of this BL_InvoiceOtherInsuranceInfoEntity.  Is set at creation time.
   **/
    public abstract void setSex(java.lang.String pSex);

    /**
   * Retrieve the BL_InvoiceOtherInsuranceInfoEntity's EmployerName.
   *
   * @return Returns an Integer representing the EmployerName of this BL_InvoiceOtherInsuranceInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="EmployerName"
   **/
    public abstract String getEmployerName();

    /**
   * Set the BL_InvoiceOtherInsuranceInfoEntity's EmployerName.
   *
   * @param pEmployerName The EmployerName of this BL_InvoiceOtherInsuranceInfoEntity.  Is set at creation time.
   **/
    public abstract void setEmployerName(java.lang.String pEmployerName);

    /**
   * @return Returns the 1InsuranceCoName of this BL_InvoiceOtherInsuranceInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="InsuranceCoName"
   **/
    public abstract String getInsuranceCoName();

    /**
   * Specify the InsuranceCoName of this BL_InvoiceOtherInsuranceInfoEntity
   *
   * @param pInsuranceCoName InsuranceCoName of this BL_InvoiceOtherInsuranceInfoEntity
   **/
    public abstract void setInsuranceCoName(java.lang.String pInsuranceCoName);

    /**
   * @return Returns the OtherInsuredName of this BL_InvoiceOtherInsuranceInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="OtherInsuredName"
   **/
    public abstract String getOtherInsuredName();

    /**
   * Specify the OtherInsuredName of this BL_InvoiceOtherInsuranceInfoEntity
   *
   * @param pOtherInsuredName OtherInsuredName of this BL_InvoiceOtherInsuranceInfoEntity
   **/
    public abstract void setOtherInsuredName(java.lang.String pOtherInsuredName);

    /**
   * @return Returns the OtherInsuranceDOB of this BL_InvoiceOtherInsuranceInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="OtherInsuranceDOB"
   **/
    public abstract Date getOtherInsuranceDOB();

    /**
   * Specify the OtherInsuranceDOB date of this BL_InvoiceOtherInsuranceInfoEntity
   *
   * @param pOtherInsuranceDOB Date of the modification of this BL_InvoiceOtherInsuranceInfoEntity
   **/
    public abstract void setOtherInsuranceDOB(Date pOtherInsuranceDOB);

    /**
   * Create a BL_InvoiceOtherInsuranceInfoEntity based on the supplied BL_InvoiceOtherInsuranceInfoEntity Value Object.
   *
   * @param pBL_InvoiceOtherInsuranceInfoEntity The data used to create the BL_InvoiceOtherInsuranceInfoEntity.
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
    public BL_InvoiceOtherInsuranceInfoEntityPK ejbCreate(BL_InvoiceOtherInsuranceInfoEntityData pBL_InvoiceOtherInsuranceInfoEntity) throws InvalidValueException, EJBException, CreateException {
        BL_InvoiceOtherInsuranceInfoEntityData lData = (BL_InvoiceOtherInsuranceInfoEntityData) pBL_InvoiceOtherInsuranceInfoEntity.clone();
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(BL_InvoiceOtherInsuranceInfoEntityData pBL_InvoiceOtherInsuranceInfoEntity) {
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
