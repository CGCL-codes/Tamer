package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.CT_ClassEntity;
import com.medcentrex.interfaces.CT_ClassEntityData;
import com.medcentrex.interfaces.CT_ClassEntityHome;
import com.medcentrex.interfaces.CT_ClassEntityPK;
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
 * The Entity bean represents a CT_ClassEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="CT_ClassEntity"
 *           display-name="CT_ClassEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/CT_ClassEntity"
 * 			schema="ct_class"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="CT_Class"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.interfaces.CT_ClassEntity findByCT_Class_ID(java.lang.Integer pAcct_Class_ID )"
 * query="select object(ob) from ct_class ob where ob.cT_Class_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="ct_class"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class CT_ClassEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pCT_ClassEntity The Value Object containing the CT_ClassEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(CT_ClassEntityData pCT_ClassEntity) throws InvalidValueException {
        if (pCT_ClassEntity == null) {
            throw new InvalidValueException("object.undefined", "CT_ClassEntity");
        }
        if (pCT_ClassEntity.getCT_Class_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "CT_ClassEntity", "Id" });
        }
        setCT_Class_ID(pCT_ClassEntity.getCT_Class_ID());
        setCT_Class(pCT_ClassEntity.getCT_Class());
    }

    /**
   * Create and return a CT_ClassEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a CT_ClassEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public CT_ClassEntityData getValueObject() {
        CT_ClassEntityData lData = new CT_ClassEntityData();
        lData.setCT_Class_ID(getCT_Class_ID());
        lData.setCT_Class(getCT_Class());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "CT_ClassEntityBean [ " + getValueObject() + " ]";
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
        String lSequenceField = "CT_Class_ID";
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
   * Retrieve the CT_ClassEntity's CT_Class_ID.
   *
   * @return Returns an Integer representing the CT_Class_ID of this CT_ClassEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="CT_Class_ID"
   **/
    public abstract Integer getCT_Class_ID();

    /**
   * Set the CT_ClassEntity's CT_Class_ID.
   *
   * @param pCT_Class_ID The CT_Class_ID of this CT_ClassEntity.
   **/
    public abstract void setCT_Class_ID(java.lang.Integer pCT_Class_ID);

    /**
   * Retrieve the CT_ClassEntity's CT_Class.
   *
   * @return Returns a String representing the CT_Class of this CT_ClassEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="CT_Class"
   **/
    public abstract String getCT_Class();

    /**
   * Set the CT_ClassEntity's CT_Class.
   *
   * @param pCT_Class The CT_Class of this CT_ClassEntity.
   **/
    public abstract void setCT_Class(java.lang.String pCT_Class);

    /**
   * Create a CT_ClassEntity based on the supplied CT_ClassEntity Value Object.
   *
   * @param pCT_ClassEntity The data used to create the CT_ClassEntity.
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
    public CT_ClassEntityPK ejbCreate(CT_ClassEntityData pCT_ClassEntity) throws InvalidValueException, EJBException, CreateException {
        CT_ClassEntityData lData = (CT_ClassEntityData) pCT_ClassEntity.clone();
        try {
            lData.setCT_Class_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(CT_ClassEntityData pCT_ClassEntity) {
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
