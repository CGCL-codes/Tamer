package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.Billing_RuleEntity;
import com.medcentrex.bridge.interfaces.Billing_RuleEntityData;
import com.medcentrex.bridge.interfaces.Billing_RuleEntityHome;
import com.medcentrex.bridge.interfaces.Billing_RuleEntityPK;
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
 * The Entity bean represents a Billing_RuleEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Billing_RuleEntity"
 *           display-name="Billing_RuleEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/Billing_RuleEntity"
 * 			schema="Billing_Rule"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"

 *
 * @ejb:env-entry name="SequenceName"
 *                value="billing_rule"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.Billing_RuleEntity findByBilling_Rule_ID( java.lang.Integer pBilling_Rule_ID)"
 * query="SELECT OBJECT(ob) FROM Billing_Rule ob WHERE ob.billing_Rule_ID=?1"
 *
 * @ejb:finder signature="java.util.Collection findByStatement_Form_ID( java.lang.Integer pStatement_Form_ID)"
 * query="SELECT OBJECT(ob) FROM Billing_Rule ob WHERE ob.statement_Form_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @jboss:table-name table-name="billing_rule"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Billing_RuleEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pBilling_RuleEntity The Value Object containing the Billing_RuleEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Billing_RuleEntityData pBilling_RuleEntity) throws InvalidValueException {
        if (pBilling_RuleEntity == null) {
            throw new InvalidValueException("object.undefined", "Billing_RuleEntity");
        }
        if (pBilling_RuleEntity.getBilling_Rule_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Billing_RuleEntity", "Id" });
        }
        setBilling_Rule_ID(pBilling_RuleEntity.getBilling_Rule_ID());
        setCustomer_ID(pBilling_RuleEntity.getCustomer_ID());
        setStatement_Form_ID(pBilling_RuleEntity.getStatement_Form_ID());
        setBilling_Rule_Name(pBilling_RuleEntity.getBilling_Rule_Name());
        setBilling_Rule_Description(pBilling_RuleEntity.getBilling_Rule_Description());
        setBilling_Rule_Condition(pBilling_RuleEntity.getBilling_Rule_Condition());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pBilling_RuleEntity The Value Object containing the Billing_RuleEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(Billing_RuleEntityData pBilling_RuleEntity) throws InvalidValueException {
        if (pBilling_RuleEntity == null) {
            throw new InvalidValueException("object.undefined", "Billing_RuleEntity");
        }
        if (pBilling_RuleEntity.getBilling_Rule_ID() == null) {
            throw new InvalidValueException("id.invalid", new String[] { "Billing_RuleEntity", "Id" });
        }
        if (pBilling_RuleEntity.getBilling_Rule_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Billing_RuleEntity", "Id" });
        }
        setCustomer_ID(pBilling_RuleEntity.getCustomer_ID());
        setStatement_Form_ID(pBilling_RuleEntity.getStatement_Form_ID());
        setBilling_Rule_Name(pBilling_RuleEntity.getBilling_Rule_Name());
        setBilling_Rule_Description(pBilling_RuleEntity.getBilling_Rule_Description());
        setBilling_Rule_Condition(pBilling_RuleEntity.getBilling_Rule_Condition());
    }

    /**
   * Create and return a Billing_RuleEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Billing_RuleEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Billing_RuleEntityData getValueObject() {
        Billing_RuleEntityData lData = new Billing_RuleEntityData();
        lData.setBilling_Rule_ID(getBilling_Rule_ID());
        lData.setCustomer_ID(getCustomer_ID());
        lData.setStatement_Form_ID(getStatement_Form_ID());
        lData.setBilling_Rule_Name(getBilling_Rule_Name());
        lData.setBilling_Rule_Description(getBilling_Rule_Description());
        lData.setBilling_Rule_Condition(getBilling_Rule_Condition());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Billing_RuleEntityBean [ " + getValueObject() + " ]";
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
        String lFieldName = "Billing_Rule_ID";
        try {
            Context lContext = new InitialContext();
            String lSequenceName = (java.lang.String) lContext.lookup("java:comp/env/SequenceName");
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/bridge/SequenceGenerator"), SequenceGeneratorHome.class);
            SequenceGenerator lBean = (SequenceGenerator) lHome.create();
            lUniqueId = lBean.getNextNumber(lSequenceName, lFieldName);
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
   * Retrieve the Billing_RuleEntity's Billing_Rule_ID.
   *
   * @return Returns an Integer representing the Billing_Rule_ID of this Billing_RuleEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Billing_Rule_ID"
   **/
    public abstract Integer getBilling_Rule_ID();

    /**
   * Set the Billing_RuleEntity's Billing_Rule_ID.
   *
   * @param pBilling_Rule_ID The Billing_Rule_ID of this Billing_RuleEntity. Is set at creation time.
   **/
    public abstract void setBilling_Rule_ID(java.lang.Integer pBilling_Rule_ID);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Customer_ID"
	*/
    public abstract Integer getCustomer_ID();

    public abstract void setCustomer_ID(java.lang.Integer pCustomer_ID);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Statement_Form_ID"
	*/
    public abstract Integer getStatement_Form_ID();

    public abstract void setStatement_Form_ID(java.lang.Integer pCustomer_ID);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Billing_Rule_Name"
	*/
    public abstract String getBilling_Rule_Name();

    public abstract void setBilling_Rule_Name(java.lang.String pBilling_Rule_Name);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Billing_Rule_Description"
	*/
    public abstract String getBilling_Rule_Description();

    public abstract void setBilling_Rule_Description(java.lang.String pBilling_Rule_Description);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Billing_Rule_Condition"
	*/
    public abstract String getBilling_Rule_Condition();

    public abstract void setBilling_Rule_Condition(java.lang.String pBilling_Rule_Condition);

    /**
   * Create a Billing_RuleEntity based on the supplied Billing_RuleEntity Value Object.
   *
   * @param pBilling_RuleEntity The data used to create the Billing_RuleEntity.
   *
   * @throws InvalidValueException
   *
   * @throws CreateException Because we have to do so (EJB spec.)
   *
   * @ejb:create-method view-type="remote"
   **/
    public Billing_RuleEntityPK ejbCreate(Billing_RuleEntityData pBilling_RuleEntity) throws InvalidValueException, EJBException, CreateException {
        Billing_RuleEntityData lData = (Billing_RuleEntityData) pBilling_RuleEntity.clone();
        try {
            lData.setBilling_Rule_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Billing_RuleEntityData pBilling_RuleEntity) {
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
