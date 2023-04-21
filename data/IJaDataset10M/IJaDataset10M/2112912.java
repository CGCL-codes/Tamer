package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.Statement_FormEntity;
import com.medcentrex.interfaces.Statement_FormEntityData;
import com.medcentrex.interfaces.Statement_FormEntityHome;
import com.medcentrex.interfaces.Statement_FormEntityPK;
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
 * The Entity bean represents a Statement_FormEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Statement_FormEntity"
 *           display-name="Statement_FormEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/Statement_FormEntity"
 * 			schema="statement_form"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 *
 * @ejb:env-entry name="SequenceName"
 *                value="Statement_Form"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.interfaces.Statement_FormEntity findByStatement_Form_ID(java.lang.Integer pStatement_Form_ID)"
 * query="select object(ob) from statement_form ob where ob.statement_Form_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @jboss:table-name table-name="statement_form"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Statement_FormEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pStatement_FormEntity The Value Object containing the Statement_FormEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Statement_FormEntityData pStatement_FormEntity) throws InvalidValueException {
        if (pStatement_FormEntity == null) {
            throw new InvalidValueException("object.undefined", "Statement_FormEntity");
        }
        if (pStatement_FormEntity.getStatement_Form_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Statement_FormEntity", "Id" });
        }
        setStatement_Form_ID(pStatement_FormEntity.getStatement_Form_ID());
        setCustomer_ID(pStatement_FormEntity.getCustomer_ID());
        setStatement_Form_Name(pStatement_FormEntity.getStatement_Form_Name());
        setStatement_Form_Description(pStatement_FormEntity.getStatement_Form_Description());
        setStatement_Form_Text(pStatement_FormEntity.getStatement_Form_Text());
        setStatement_Form_Type_ID(pStatement_FormEntity.getStatement_Form_Type_ID());
        setCreate_Date(pStatement_FormEntity.getCreate_Date());
        setTemplate_ID(pStatement_FormEntity.getTemplate_ID());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pStatement_FormEntity The Value Object containing the Statement_FormEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(Statement_FormEntityData pStatement_FormEntity) throws InvalidValueException {
        if (pStatement_FormEntity == null) {
            throw new InvalidValueException("object.undefined", "Statement_FormEntity");
        }
        if (pStatement_FormEntity.getStatement_Form_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Statement_FormEntity", "Id" });
        }
        setCustomer_ID(pStatement_FormEntity.getCustomer_ID());
        setStatement_Form_Name(pStatement_FormEntity.getStatement_Form_Name());
        setStatement_Form_Description(pStatement_FormEntity.getStatement_Form_Description());
        setStatement_Form_Text(pStatement_FormEntity.getStatement_Form_Text());
        setStatement_Form_Type_ID(pStatement_FormEntity.getStatement_Form_Type_ID());
        setCreate_Date(pStatement_FormEntity.getCreate_Date());
        setTemplate_ID(pStatement_FormEntity.getTemplate_ID());
    }

    /**
   * Create and return a Statement_FormEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Statement_FormEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Statement_FormEntityData getValueObject() {
        Statement_FormEntityData lData = new Statement_FormEntityData();
        lData.setStatement_Form_ID(getStatement_Form_ID());
        lData.setCustomer_ID(getCustomer_ID());
        lData.setStatement_Form_Name(getStatement_Form_Name());
        lData.setStatement_Form_Description(getStatement_Form_Description());
        lData.setStatement_Form_Text(getStatement_Form_Text());
        lData.setStatement_Form_Type_ID(getStatement_Form_Type_ID());
        lData.setCreate_Date(getCreate_Date());
        lData.setTemplate_ID(getTemplate_ID());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Statement_FormEntityBean [ " + getValueObject() + " ]";
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
        String lFieldName = "Statement_Form_ID";
        try {
            Context lContext = new InitialContext();
            String lSequenceName = (java.lang.String) lContext.lookup("java:comp/env/SequenceName");
            SequenceGeneratorHome lHome = (SequenceGeneratorHome) PortableRemoteObject.narrow(lContext.lookup("java:comp/env/ejb/com/medcentrex/SequenceGenerator"), SequenceGeneratorHome.class);
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
   * Retrieve the Statement_FormEntity's Statement_Form_ID.
   *
   * @return Returns an Integer representing the Statement_Form_ID of this Statement_FormEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Statement_Form_ID"
   **/
    public abstract Integer getStatement_Form_ID();

    /**
   * Set the Statement_FormEntity's Statement_Form_ID.
   *
   * @param pStatement_Form_ID The Statement_Form_ID of this Statement_FormEntity. Is set at creation time.
   **/
    public abstract void setStatement_Form_ID(java.lang.Integer pStatement_Form_ID);

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
	* @jboss:column-name name="Statement_Form_Name"
	*/
    public abstract String getStatement_Form_Name();

    public abstract void setStatement_Form_Name(java.lang.String pStatement_Form_Name);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Statement_Form_Description"
	*/
    public abstract String getStatement_Form_Description();

    public abstract void setStatement_Form_Description(java.lang.String pStatement_Form_Description);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Statement_Form_Text"
	*/
    public abstract String getStatement_Form_Text();

    public abstract void setStatement_Form_Text(java.lang.String pStatement_Form_Text);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Statement_Form_Type_ID"
	*/
    public abstract Integer getStatement_Form_Type_ID();

    public abstract void setStatement_Form_Type_ID(java.lang.Integer pStatment_Form_Type_ID);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Create_Date"
	*/
    public abstract Date getCreate_Date();

    public abstract void setCreate_Date(Date pCreate_Date);

    /**
	* @return
	* @ejb:persistent-field
	* @jboss:column-name name="Template_ID"
	*/
    public abstract Integer getTemplate_ID();

    public abstract void setTemplate_ID(java.lang.Integer pTemplate_ID);

    /**
   * Create a Statement_FormEntity based on the supplied Statement_FormEntity Value Object.
   *
   * @param pStatement_FormEntity The data used to create the Statement_FormEntity.
   *
   * @throws InvalidValueException
   *
   * @throws CreateException Because we have to do so (EJB spec.)
   *
   * @ejb:create-method view-type="remote"
   **/
    public Statement_FormEntityPK ejbCreate(Statement_FormEntityData pStatement_FormEntity) throws InvalidValueException, EJBException, CreateException {
        Statement_FormEntityData lData = (Statement_FormEntityData) pStatement_FormEntity.clone();
        try {
            lData.setStatement_Form_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Statement_FormEntityData pStatement_FormEntity) {
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
