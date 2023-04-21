package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.MyScopesEntity;
import com.medcentrex.interfaces.MyScopesEntityData;
import com.medcentrex.interfaces.MyScopesEntityHome;
import com.medcentrex.interfaces.MyScopesEntityPK;
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
 * The Entity bean represents a MyScopesEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="MyScopesEntity"
 *           display-name="MyScopesEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/MyScopesEntity"
 * 			schema="myscopes"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="MyScopes"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="com.medcentrex.interfaces.MyScopesEntity findByMyScopes_ID(java.lang.Integer pMyScopes_ID)"
 * query="select object(ob) from myscopes ob where ob.myScopes_ID=?1"
 *
 * @jboss:table-name table-name="myscopes"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class MyScopesEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pMyScopesEntity The Value Object containing the MyScopesEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(MyScopesEntityData pMyScopesEntity) throws InvalidValueException {
        if (pMyScopesEntity == null) {
            throw new InvalidValueException("object.undefined", "MyScopesEntity");
        }
        if (pMyScopesEntity.getMyScopes_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "MyScopesEntity", "Id" });
        }
        setMyScopes_ID(pMyScopesEntity.getMyScopes_ID());
        setPerson_ID(pMyScopesEntity.getPerson_ID());
        setURI(pMyScopesEntity.getURI());
    }

    /**
   * Create and return a MyScopesEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a MyScopesEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public MyScopesEntityData getValueObject() {
        MyScopesEntityData lData = new MyScopesEntityData();
        lData.setMyScopes_ID(getMyScopes_ID());
        lData.setPerson_ID(getPerson_ID());
        lData.setURI(getURI());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "MyScopesEntityBean [ " + getValueObject() + " ]";
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
            lUniqueId = lBean.getNextNumber(lSequenceName, "MyScopes_ID");
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
   * @jboss:column-name name="MyScopes_ID"
   **/
    public abstract Integer getMyScopes_ID();

    public abstract void setMyScopes_ID(java.lang.Integer pMyScopes_ID);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Person_ID"
   **/
    public abstract Integer getPerson_ID();

    public abstract void setPerson_ID(java.lang.Integer pPerson_ID);

    /**
	* @ejb:persistent-field
	* @jboss:column-name name="URI"
	*/
    public abstract String getURI();

    public abstract void setURI(java.lang.String pURI);

    /**
   * Create a MyScopesEntity based on the supplied MyScopesEntity Value Object.
   *
   * @param pMyScopesEntity The data used to create the MyScopesEntity.
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
    public MyScopesEntityPK ejbCreate(MyScopesEntityData pMyScopesEntity) throws InvalidValueException, EJBException, CreateException {
        MyScopesEntityData lData = (MyScopesEntityData) pMyScopesEntity.clone();
        try {
            lData.setMyScopes_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(MyScopesEntityData pMyScopesEntity) {
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
