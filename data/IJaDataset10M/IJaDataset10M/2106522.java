package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Physician_Insurance_CompanyEntity;
import com.medcentrex.bridge.interfaces.Physician_Insurance_CompanyEntityData;
import com.medcentrex.bridge.interfaces.Physician_Insurance_CompanyEntityHome;
import com.medcentrex.bridge.interfaces.Physician_Insurance_CompanyEntityPK;
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
 * The Entity bean represents a Physician_Insurance_CompanyEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Physician_Insurance_CompanyEntity"
 *           display-name="Physician_Insurance_CompanyEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Physician_Insurance_CompanyEntity"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="physician_insurance_company"
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
 * @jboss:table-name table-name="physician_insurance_company"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Physician_Insurance_CompanyEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPhysician_Insurance_CompanyEntity The Value Object containing the Physician_Insurance_CompanyEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Physician_Insurance_CompanyEntityData pPhysician_Insurance_CompanyEntity) throws InvalidValueException {
        if (pPhysician_Insurance_CompanyEntity == null) {
            throw new InvalidValueException("object.undefined", "Physician_Insurance_CompanyEntity");
        }
        if (pPhysician_Insurance_CompanyEntity.getPhysician_Insurance_Company_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Physician_Insurance_CompanyEntity", "Id" });
        }
        setPhysician_Insurance_Company_ID(pPhysician_Insurance_CompanyEntity.getPhysician_Insurance_Company_ID());
        setPerson_ID(pPhysician_Insurance_CompanyEntity.getPerson_ID());
        setInsurance_Company_Plan_ID(pPhysician_Insurance_CompanyEntity.getInsurance_Company_Plan_ID());
        setSigOnFile(pPhysician_Insurance_CompanyEntity.getSigOnFile());
        setSigOnFile_Date(pPhysician_Insurance_CompanyEntity.getSigOnFile_Date());
    }

    /**
   * Create and return a Physician_Insurance_CompanyEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Physician_Insurance_CompanyEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Physician_Insurance_CompanyEntityData getValueObject() {
        Physician_Insurance_CompanyEntityData lData = new Physician_Insurance_CompanyEntityData();
        lData.setPhysician_Insurance_Company_ID(getPhysician_Insurance_Company_ID());
        lData.setPerson_ID(getPerson_ID());
        lData.setInsurance_Company_Plan_ID(getInsurance_Company_Plan_ID());
        lData.setSigOnFile(getSigOnFile());
        lData.setSigOnFile_Date(getSigOnFile_Date());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Physician_Insurance_CompanyEntityBean [ " + getValueObject() + " ]";
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
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/bridge/SequenceGenerator"), SequenceGeneratorHome.class);
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
   * Retrieve the Physician_Insurance_CompanyEntity's Physician_Insurance_Company_ID.
   *
   * @return Returns an Integer representing the Physician_Insurance_Company_ID of this Physician_Insurance_CompanyEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Physician_Insurance_Company_ID"
   **/
    public abstract Integer getPhysician_Insurance_Company_ID();

    /**
   * Set the Physician_Insurance_CompanyEntity's Physician_Insurance_Company_ID.
   *
   * @param pPhysician_Insurance_Company_ID The Physician_Insurance_Company_ID of this Physician_Insurance_CompanyEntity. Is set at creation time.
   **/
    public abstract void setPhysician_Insurance_Company_ID(java.lang.Integer pPhysician_Insurance_Company_ID);

    /**
   * Retrieve the Physician_Insurance_CompanyEntity's Person_ID.
   *
   * @return Returns an Integer representing the Person_ID of this Physician_Insurance_CompanyEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Person_ID"
   **/
    public abstract Integer getPerson_ID();

    /**
   * Set the Physician_Insurance_CompanyEntity's Person_ID.
   *
   * @param pPerson_ID The Person_ID of this Physician_Insurance_CompanyEntity. Is set at creation time.
   **/
    public abstract void setPerson_ID(java.lang.Integer pPerson_ID);

    /**
   * Retrieve the Physician_Insurance_CompanyEntity's Insurance_Company_Plan_ID.
   *
   * @return Returns an Integer representing the Insurance_Company_Plan_ID of this Physician_Insurance_CompanyEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Insurance_Company_Plan_ID"
   **/
    public abstract Integer getInsurance_Company_Plan_ID();

    /**
   * Set the Physician_Insurance_CompanyEntity's Insurance_Company_Plan_ID.
   *
   * @param pInsurance_Company_Plan_ID The Insurance_Company_Plan_ID of this Physician_Insurance_CompanyEntity. Is set at creation time.
   **/
    public abstract void setInsurance_Company_Plan_ID(java.lang.Integer pInsurance_Company_Plan_ID);

    /**
   * Retrieve the Physician_Insurance_CompanyEntity's SigOnFile.
   *
   * @return Returns an boolean representing the SigOnFile of this Physician_Insurance_CompanyEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="SigOnFile"
   **/
    public abstract boolean getSigOnFile();

    /**
   * Set the Physician_Insurance_CompanyEntity's SigOnFile.
   *
   * @param pSigOnFile The SigOnFile of this Physician_Insurance_CompanyEntity. Is set at creation time.
   **/
    public abstract void setSigOnFile(boolean pSigOnFile);

    /**
   * @return Returns the SigOnFile_Date of this Physician_Insurance_CompanyEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="SigOnFile_Date"
   **/
    public abstract Date getSigOnFile_Date();

    /**
   * Specify the SigOnFile_Date of this Physician_Insurance_CompanyEntity
   *
   * @param pSigOnFile_Date Date of the SigOnFile of this Physician_Insurance_CompanyEntity
   **/
    public abstract void setSigOnFile_Date(Date pSigOnFile_Date);

    /**
   * Create a Physician_Insurance_CompanyEntity based on the supplied Physician_Insurance_CompanyEntity Value Object.
   *
   * @param pPhysician_Insurance_CompanyEntity The data used to create the Physician_Insurance_CompanyEntity.
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
    public Physician_Insurance_CompanyEntityPK ejbCreate(Physician_Insurance_CompanyEntityData pPhysician_Insurance_CompanyEntity) throws InvalidValueException, EJBException, CreateException {
        Physician_Insurance_CompanyEntityData lData = (Physician_Insurance_CompanyEntityData) pPhysician_Insurance_CompanyEntity.clone();
        try {
            lData.setPhysician_Insurance_Company_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Physician_Insurance_CompanyEntityData pPhysician_Insurance_CompanyEntity) {
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
