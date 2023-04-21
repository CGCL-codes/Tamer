package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.User_LocationEntity;
import com.medcentrex.interfaces.User_LocationEntityData;
import com.medcentrex.interfaces.User_LocationEntityHome;
import com.medcentrex.interfaces.User_LocationEntityPK;
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
 * The Entity bean represents a User_LocationEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="User_LocationEntity"
 *           display-name="User_LocationEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/User_LocationEntity"
 * 			schema="user_location"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="User_Location"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:finder signature="java.util.Collection findByPerson_ID(java.lang.Integer pPerson_ID)"
 * query="select object(ob) from user_location ob where ob.person_ID=?1"

 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="user_location"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class User_LocationEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pUser_LocationEntity The Value Object containing the User_LocationEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(User_LocationEntityData pUser_LocationEntity) throws InvalidValueException {
        if (pUser_LocationEntity == null) {
            throw new InvalidValueException("object.undefined", "User_LocationEntity");
        }
        if (pUser_LocationEntity.getUser_Location_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "User_LocationEntity", "Id" });
        }
        setUser_Location_ID(pUser_LocationEntity.getUser_Location_ID());
        setPerson_ID(pUser_LocationEntity.getPerson_ID());
        setPlace_ID(pUser_LocationEntity.getPlace_ID());
    }

    /**
   * Create and return a User_LocationEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a User_LocationEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public User_LocationEntityData getValueObject() {
        User_LocationEntityData lData = new User_LocationEntityData();
        lData.setUser_Location_ID(getUser_Location_ID());
        lData.setPerson_ID(getPerson_ID());
        lData.setPlace_ID(getPlace_ID());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "User_LocationEntityBean [ " + getValueObject() + " ]";
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
            lUniqueId = lBean.getNextNumber(lSequenceName, "User_Location_ID");
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
   * Retrieve the User_LocationEntity's User_Location_ID.
   *
   * @return Returns an Integer representing the User_Location_ID of this User_LocationEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="User_Location_ID"
   **/
    public abstract Integer getUser_Location_ID();

    /**
   * Set the User_LocationEntity's User_Location_ID.
   *
   * @param pUser_Location_ID The User_Location_ID of this User_LocationEntity. Is set at creation time.
   **/
    public abstract void setUser_Location_ID(java.lang.Integer pUser_Location_ID);

    /**
   * Retrieve the User_LocationEntity's Person_ID.
   *
   * @return Returns an Integer representing the Person_ID of this User_LocationEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Person_ID"
   **/
    public abstract Integer getPerson_ID();

    /**
   * Set the User_LocationEntity's Person_ID.
   *
   * @param pPerson_ID The Person_ID of this User_LocationEntity. Is set at creation time.
   **/
    public abstract void setPerson_ID(java.lang.Integer pPerson_ID);

    /**
   * Retrieve the User_LocationEntity's Place_ID.
   *
   * @return Returns an Integer representing the Place_ID of this User_LocationEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Place_ID"
   **/
    public abstract Integer getPlace_ID();

    /**
   * Set the User_LocationEntity's Place_ID.
   *
   * @param pPlace_ID The Place_ID of this User_LocationEntity. Is set at creation time.
   **/
    public abstract void setPlace_ID(java.lang.Integer pPlace_ID);

    /**
   * Create a User_LocationEntity based on the supplied User_LocationEntity Value Object.
   *
   * @param pUser_LocationEntity The data used to create the User_LocationEntity.
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
    public User_LocationEntityPK ejbCreate(User_LocationEntityData pUser_LocationEntity) throws InvalidValueException, EJBException, CreateException {
        User_LocationEntityData lData = (User_LocationEntityData) pUser_LocationEntity.clone();
        try {
            lData.setUser_Location_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(User_LocationEntityData pUser_LocationEntity) {
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
