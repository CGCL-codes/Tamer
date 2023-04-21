package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Medical_GroupEntity;
import com.medcentrex.bridge.interfaces.Medical_GroupEntityData;
import com.medcentrex.bridge.interfaces.Medical_GroupEntityHome;
import com.medcentrex.bridge.interfaces.Medical_GroupEntityPK;
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
 * The Entity bean represents a Medical_GroupEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Medical_GroupEntity"
 *           display-name="Medical_GroupEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Medical_GroupEntity"
 * 			schema="Medical_Group"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="medical_group"
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
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.Medical_GroupEntity findByPlace_ID( java.lang.Integer pPlace_ID)"
 * query="SELECT OBJECT(ob) FROM Medical_Group ob WHERE ob.place_ID=?1"
 *
* @jboss:table-name table-name="medical_group"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Medical_GroupEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pMedical_GroupEntity The Value Object containing the Medical_GroupEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Medical_GroupEntityData pMedical_GroupEntity) throws InvalidValueException {
        if (pMedical_GroupEntity == null) {
            throw new InvalidValueException("object.undefined", "Medical_GroupEntity");
        }
        if (pMedical_GroupEntity.getPlace_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Medical_GroupEntity", "Id" });
        }
        setPlace_ID(pMedical_GroupEntity.getPlace_ID());
        setFederal_Tax_ID(pMedical_GroupEntity.getFederal_Tax_ID());
        setEIN(pMedical_GroupEntity.getEIN());
        setPinOrGrp(pMedical_GroupEntity.getPinOrGrp());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pMedical_GroupEntity The Value Object containing the Medical_GroupEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(Medical_GroupEntityData pMedical_GroupEntity) throws InvalidValueException {
        if (pMedical_GroupEntity == null) {
            throw new InvalidValueException("object.undefined", "Medical_GroupEntity");
        }
        if (pMedical_GroupEntity.getPlace_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Medical_GroupEntity", "Id" });
        }
        setFederal_Tax_ID(pMedical_GroupEntity.getFederal_Tax_ID());
        setEIN(pMedical_GroupEntity.getEIN());
        setPinOrGrp(pMedical_GroupEntity.getPinOrGrp());
    }

    /**
   * Create and return a Medical_GroupEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Medical_GroupEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Medical_GroupEntityData getValueObject() {
        Medical_GroupEntityData lData = new Medical_GroupEntityData();
        lData.setPlace_ID(getPlace_ID());
        lData.setFederal_Tax_ID(getFederal_Tax_ID());
        lData.setEIN(getEIN());
        lData.setPinOrGrp(getPinOrGrp());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Medical_GroupEntityBean [ " + getValueObject() + " ]";
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
   * Retrieve the Medical_GroupEntity's Place_ID.
   *
   * @return Returns an Integer representing the Place_ID of this Medical_GroupEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Place_ID"
   **/
    public abstract Integer getPlace_ID();

    /**
   * Set the Medical_GroupEntity's Place_ID.
   *
   * @param pPlace_ID The Place_ID of this Medical_GroupEntity. Is set at creation time.
   **/
    public abstract void setPlace_ID(java.lang.Integer pPlace_ID);

    /**
   * @return Returns the Federal_Tax_ID of this Medical_GroupEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Federal_Tax_ID"
   **/
    public abstract String getFederal_Tax_ID();

    /**
   * Specify the Federal_Tax_ID of this Medical_GroupEntity
   *
   * @param pFederal_Tax_ID Federal_Tax_ID of this Medical_GroupEntity
   **/
    public abstract void setFederal_Tax_ID(java.lang.String pFederal_Tax_ID);

    /**
   * @return Returns the EIN of this Medical_GroupEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="EIN"
   **/
    public abstract String getEIN();

    /**
   * Specify the EIN of this Medical_GroupEntity
   *
   * @param pEIN EINty of this Medical_GroupEntity
   **/
    public abstract void setEIN(java.lang.String pEIN);

    /**
	* 0=print to PIN#, 1=print to GRP#
	*
   * @ejb:persistent-field
   * @jboss:column-name name="PinOrGrp"
   **/
    public abstract Boolean getPinOrGrp();

    public abstract void setPinOrGrp(Boolean PinOrGrp);

    /**
   * Create a Medical_GroupEntity based on the supplied Medical_GroupEntity Value Object.
   *
   * @param pMedical_GroupEntity The data used to create the Medical_GroupEntity.
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
    public Medical_GroupEntityPK ejbCreate(Medical_GroupEntityData pMedical_GroupEntity) throws InvalidValueException, EJBException, CreateException {
        Medical_GroupEntityData lData = (Medical_GroupEntityData) pMedical_GroupEntity.clone();
        lData.setPlace_ID(pMedical_GroupEntity.getPlace_ID());
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Medical_GroupEntityData pMedical_GroupEntity) {
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
