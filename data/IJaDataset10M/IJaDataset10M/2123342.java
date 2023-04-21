package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.CT_InvoiceProviderBillingInfoEntity;
import com.medcentrex.interfaces.CT_InvoiceProviderBillingInfoEntityData;
import com.medcentrex.interfaces.CT_InvoiceProviderBillingInfoEntityHome;
import com.medcentrex.interfaces.CT_InvoiceProviderBillingInfoEntityPK;
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
 * The Entity bean represents a CT_InvoiceProviderBillingInfoEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="CT_InvoiceProviderBillingInfoEntity"
 *           display-name="CT_InvoiceProviderBillingInfoEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/CT_InvoiceProviderBillingInfoEntity"
 * 			schema="ct_invoiceproviderbillinginfo"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="CT_InvoiceProviderBillingInfo"
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
 * query="select object(ob) from ct_invoiceproviderbillinginfo ob where ob.chargeTicket_ID=?1"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="ct_invoiceproviderbillinginfo"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class CT_InvoiceProviderBillingInfoEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pCT_InvoiceProviderBillingInfoEntity The Value Object containing the CT_InvoiceProviderBillingInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(CT_InvoiceProviderBillingInfoEntityData pCT_InvoiceProviderBillingInfoEntity) throws InvalidValueException {
        if (pCT_InvoiceProviderBillingInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "CT_InvoiceProviderBillingInfoEntity");
        }
        if (pCT_InvoiceProviderBillingInfoEntity.getChargeTicket_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "CT_InvoiceProviderBillingInfoEntity", "Id" });
        }
        setChargeTicket_ID(pCT_InvoiceProviderBillingInfoEntity.getChargeTicket_ID());
        setProviderName(pCT_InvoiceProviderBillingInfoEntity.getProviderName());
        setHomePhoneNo(pCT_InvoiceProviderBillingInfoEntity.getHomePhoneNo());
        setAddress1(pCT_InvoiceProviderBillingInfoEntity.getAddress1());
        setAddress2(pCT_InvoiceProviderBillingInfoEntity.getAddress2());
        setCity(pCT_InvoiceProviderBillingInfoEntity.getCity());
        setZipCode(pCT_InvoiceProviderBillingInfoEntity.getZipCode());
        setState(pCT_InvoiceProviderBillingInfoEntity.getState());
        setPIN(pCT_InvoiceProviderBillingInfoEntity.getPIN());
        setGRPNo(pCT_InvoiceProviderBillingInfoEntity.getGRPNo());
        setProvider_ID(pCT_InvoiceProviderBillingInfoEntity.getProvider_ID());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pCT_InvoiceProviderBillingInfoEntity The Value Object containing the CT_InvoiceProviderBillingInfoEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(CT_InvoiceProviderBillingInfoEntityData pCT_InvoiceProviderBillingInfoEntity) throws InvalidValueException {
        if (pCT_InvoiceProviderBillingInfoEntity == null) {
            throw new InvalidValueException("object.undefined", "CT_InvoiceProviderBillingInfoEntity");
        }
        if (pCT_InvoiceProviderBillingInfoEntity.getChargeTicket_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "CT_InvoiceProviderBillingInfoEntity", "Id" });
        }
        setProviderName(pCT_InvoiceProviderBillingInfoEntity.getProviderName());
        setHomePhoneNo(pCT_InvoiceProviderBillingInfoEntity.getHomePhoneNo());
        setAddress1(pCT_InvoiceProviderBillingInfoEntity.getAddress1());
        setAddress2(pCT_InvoiceProviderBillingInfoEntity.getAddress2());
        setCity(pCT_InvoiceProviderBillingInfoEntity.getCity());
        setZipCode(pCT_InvoiceProviderBillingInfoEntity.getZipCode());
        setState(pCT_InvoiceProviderBillingInfoEntity.getState());
        setPIN(pCT_InvoiceProviderBillingInfoEntity.getPIN());
        setGRPNo(pCT_InvoiceProviderBillingInfoEntity.getGRPNo());
        setProvider_ID(pCT_InvoiceProviderBillingInfoEntity.getProvider_ID());
    }

    /**
   * Create and return a CT_InvoiceProviderBillingInfoEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a CT_InvoiceProviderBillingInfoEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public CT_InvoiceProviderBillingInfoEntityData getValueObject() {
        CT_InvoiceProviderBillingInfoEntityData lData = new CT_InvoiceProviderBillingInfoEntityData();
        lData.setChargeTicket_ID(getChargeTicket_ID());
        lData.setProviderName(getProviderName());
        lData.setHomePhoneNo(getHomePhoneNo());
        lData.setAddress1(getAddress1());
        lData.setAddress2(getAddress2());
        lData.setCity(getCity());
        lData.setZipCode(getZipCode());
        lData.setState(getState());
        lData.setPIN(getPIN());
        lData.setGRPNo(getGRPNo());
        lData.setProvider_ID(getProvider_ID());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "CT_InvoiceProviderBillingInfoEntityBean [ " + getValueObject() + " ]";
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
        try {
            Context lContext = new InitialContext();
            String lSequenceName = (java.lang.String) lContext.lookup("java:comp/env/SequenceName");
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/SequenceGenerator"), SequenceGeneratorHome.class);
            SequenceGenerator lBean = (SequenceGenerator) lHome.create();
            lUniqueId = lBean.getNextNumber(lSequenceName);
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
   * Retrieve the CT_InvoiceProviderBillingInfoEntity's ChargeTicket_ID.
   *
   * @return Returns an Integer representing the ChargeTicket_ID of this CT_InvoiceProviderBillingInfoEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="ChargeTicket_ID"
   **/
    public abstract Integer getChargeTicket_ID();

    /**
   * Set the CT_InvoiceProviderBillingInfoEntity's ChargeTicket_ID.
   *
   * @param pChargeTicket_ID The ChargeTicket_ID of this CT_InvoiceProviderBillingInfoEntity.
   **/
    public abstract void setChargeTicket_ID(java.lang.Integer pChargeTicket_ID);

    /**
   * Retrieve the CT_InvoiceProviderBillingInfoEntity's ProviderName.
   *
   * @return Returns an Integer representing the ProviderName of this CT_InvoiceProviderBillingInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="ProviderName"
   **/
    public abstract String getProviderName();

    /**
   * Set the CT_InvoiceProviderBillingInfoEntity's ProviderName.
   *
   * @param pPlace_Name The ProviderName of this CT_InvoiceProviderBillingInfoEntity.
   **/
    public abstract void setProviderName(java.lang.String pProviderName);

    /**
   * Retrieve the CT_InvoiceProviderBillingInfoEntity's HomePhoneNo.
   *
   * @return Returns an Integer representing the HomePhoneNo of this CT_InvoiceProviderBillingInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="HomePhoneNo"
   **/
    public abstract String getHomePhoneNo();

    /**
   * Set the CT_InvoiceProviderBillingInfoEntity's HomePhoneNo.
   *
   * @param pHomePhoneNo The HomePhoneNo of this CT_InvoiceProviderBillingInfoEntity.
   **/
    public abstract void setHomePhoneNo(java.lang.String pHomePhoneNo);

    /**
   * Retrieve the CT_InvoiceProviderBillingInfoEntity's GRPNo.
   *
   * @return Returns an String representing the GRPNo of this CT_InvoiceProviderBillingInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="GRPNo"
   **/
    public abstract String getGRPNo();

    /**
   * Set the CT_InvoiceProviderBillingInfoEntity's GRPNo.
   *
   * @param pGRPNo The GRPNo of this CT_InvoiceProviderBillingInfoEntity.
   **/
    public abstract void setGRPNo(java.lang.String pGRPNo);

    /**
   * Retrieve the CT_InvoiceProviderBillingInfoEntity's PIN.
   *
   * @return Returns an String representing the PIN of this CT_InvoiceProviderBillingInfoEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PIN"
   **/
    public abstract String getPIN();

    /**
   * Set the CT_InvoiceProviderBillingInfoEntity's PIN.
   *
   * @param pPIN The PIN of this CT_InvoiceProviderBillingInfoEntity.
   **/
    public abstract void setPIN(java.lang.String pPIN);

    /**
   * @return Returns the 1Address of this CT_InvoiceProviderBillingInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address1"
   **/
    public abstract String getAddress1();

    /**
   * Specify the Address1 of this CT_InvoiceProviderBillingInfoEntity
   *
   * @param pAddress1 Address1 of this CT_InvoiceProviderBillingInfoEntity
   **/
    public abstract void setAddress1(java.lang.String pAddress1);

    /**
   * @return Returns the Address2 of this CT_InvoiceProviderBillingInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Address2"
   **/
    public abstract String getAddress2();

    /**
   * Specify the Address2 of this CT_InvoiceProviderBillingInfoEntity
   *
   * @param pAddress2 Address2 of this CT_InvoiceProviderBillingInfoEntity
   **/
    public abstract void setAddress2(java.lang.String pAddress2);

    /**
   * @return Returns the City of this CT_InvoiceProviderBillingInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="City"
   **/
    public abstract String getCity();

    /**
   * Specify the City of this CT_InvoiceProviderBillingInfoEntity
   *
   * @param pCity City of this CT_InvoiceProviderBillingInfoEntity
   **/
    public abstract void setCity(java.lang.String pCity);

    /**
   * @return Returns the ZIPCode of this CT_InvoiceProviderBillingInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="ZipCode"
   **/
    public abstract String getZipCode();

    /**
   * Specify the ZipCode of this CT_InvoiceProviderBillingInfoEntity
   *
   * @param pZIP ZipCode of this CT_InvoiceProviderBillingInfoEntity
   **/
    public abstract void setZipCode(java.lang.String pZIPCode);

    /**
   * @return Returns the State of this CT_InvoiceProviderBillingInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="State"
   **/
    public abstract String getState();

    /**
   * Specify the State of this CT_InvoiceProviderBillingInfoEntity
   *
   * @param pState State of this CT_InvoiceProviderBillingInfoEntity
   **/
    public abstract void setState(java.lang.String pState);

    /**
   * @return Returns the Provider_ID of this CT_InvoiceProviderBillingInfoEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="provider_id"
   **/
    public abstract Integer getProvider_ID();

    /**
   * Specify the Provider_ID of this CT_InvoiceProviderBillingInfoEntity
   *
   * @param pProvider_ID Provider_ID of this CT_InvoiceProviderBillingInfoEntity
   **/
    public abstract void setProvider_ID(java.lang.Integer pProvider_ID);

    /**
   * Create a CT_InvoiceProviderBillingInfoEntity based on the supplied CT_InvoiceProviderBillingInfoEntity Value Object.
   *
   * @param pCT_InvoiceProviderBillingInfoEntity The data used to create the CT_InvoiceProviderBillingInfoEntity.
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
    public CT_InvoiceProviderBillingInfoEntityPK ejbCreate(CT_InvoiceProviderBillingInfoEntityData pCT_InvoiceProviderBillingInfoEntity) throws InvalidValueException, EJBException, CreateException {
        CT_InvoiceProviderBillingInfoEntityData lData = (CT_InvoiceProviderBillingInfoEntityData) pCT_InvoiceProviderBillingInfoEntity.clone();
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(CT_InvoiceProviderBillingInfoEntityData pCT_InvoiceProviderBillingInfoEntity) {
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
