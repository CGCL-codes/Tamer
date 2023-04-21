package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.RegisterEntity;
import com.medcentrex.bridge.interfaces.RegisterEntityData;
import com.medcentrex.bridge.interfaces.RegisterEntityHome;
import com.medcentrex.bridge.interfaces.RegisterEntityPK;
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
 * The Entity bean represents a RegisterEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="RegisterEntity"
 *           display-name="RegisterEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/RegisterEntity"
 * 			schema="Register"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="register"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * ref-name="com/medcentrex/bridge/SequenceGenerator"

 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.RegisterEntity findByPerson_ID( java.lang.Integer pPerson_ID)"
 * query="SELECT OBJECT(ob) FROM Register ob WHERE ob.person_ID=?1"
 *
 * @ejb:data-object extends="com.medcentrex.bridge.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @jboss:table-name table-name="register"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class RegisterEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(RegisterEntityData pRegisterEntity) throws InvalidValueException {
        if (pRegisterEntity == null) {
            throw new InvalidValueException("object.undefined", "RegisterEntity");
        }
        if (pRegisterEntity.getPerson_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "RegisterEntity", "Id" });
        }
        setPerson_ID(pRegisterEntity.getPerson_ID());
        setOpen_Date(pRegisterEntity.getOpen_Date());
        setOpen_Balance(pRegisterEntity.getOpen_Balance());
        setClose_Date(pRegisterEntity.getClose_Date());
    }

    /**
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(RegisterEntityData pRegisterEntity) throws InvalidValueException {
        if (pRegisterEntity == null) {
            throw new InvalidValueException("object.undefined", "RegisterEntity");
        }
        if (pRegisterEntity.getPerson_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "RegisterEntity", "Id" });
        }
        setOpen_Date(pRegisterEntity.getOpen_Date());
        setOpen_Balance(pRegisterEntity.getOpen_Balance());
        setClose_Date(pRegisterEntity.getClose_Date());
    }

    /**
   * Create and return a RegisterEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a RegisterEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public RegisterEntityData getValueObject() {
        RegisterEntityData lData = new RegisterEntityData();
        lData.setPerson_ID(getPerson_ID());
        lData.setOpen_Date(getOpen_Date());
        lData.setOpen_Balance(getOpen_Balance());
        lData.setClose_Date(getClose_Date());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "RegisterEntityBean [ " + getValueObject() + " ]";
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
            lUniqueId = lBean.getNextNumber(lSequenceName, "Register_ID");
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
   * @ejb:persistent-field
   * @ejb:pk-field
   * @jboss:column-name name="Person_ID"
   **/
    public abstract Integer getPerson_ID();

    public abstract void setPerson_ID(java.lang.Integer Person_ID);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Open_Date"
   **/
    public abstract java.sql.Date getOpen_Date();

    public abstract void setOpen_Date(java.sql.Date Open_Date);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Close_Date"
   **/
    public abstract java.sql.Date getClose_Date();

    public abstract void setClose_Date(java.sql.Date Close_Date);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Open_Balance"
   **/
    public abstract Double getOpen_Balance();

    public abstract void setOpen_Balance(Double Open_Balance);

    /**
   * Create a RegisterEntity based on the supplied RegisterEntity Value Object.
   *
   * @param pRegisterEntity The data used to create the RegisterEntity.
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
    public RegisterEntityPK ejbCreate(RegisterEntityData pRegisterEntity) throws InvalidValueException, CreateException {
        RegisterEntityData lData = (RegisterEntityData) pRegisterEntity.clone();
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(RegisterEntityData pRegisterEntity) {
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
