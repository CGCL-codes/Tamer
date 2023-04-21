package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.PrefixEntity;
import com.medcentrex.interfaces.PrefixEntityData;
import com.medcentrex.interfaces.PrefixEntityHome;
import com.medcentrex.interfaces.PrefixEntityPK;
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
 * The Entity bean represents a PrefixEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="PrefixEntity"
 *           display-name="PrefixEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/PrefixEntity"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="PrefixEntity"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 *				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="java.util.Collection findAll()"
 *
 * @jboss:table-name table-name="prefix"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class PrefixEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPrefixEntity The Value Object containing the PrefixEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(PrefixEntityData pPrefixEntity) throws InvalidValueException {
        if (pPrefixEntity == null) {
            throw new InvalidValueException("object.undefined", "PrefixEntity");
        }
        if (pPrefixEntity.getPrefix_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "PrefixEntity", "Id" });
        }
        setPrefix_ID(pPrefixEntity.getPrefix_ID());
        setPrefix(pPrefixEntity.getPrefix());
    }

    /**
   * Create and return a PrefixEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a PrefixEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public PrefixEntityData getValueObject() {
        PrefixEntityData lData = new PrefixEntityData();
        lData.setPrefix_ID(getPrefix_ID());
        lData.setPrefix(getPrefix());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "PrefixEntityBean [ " + getValueObject() + " ]";
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
   * Retrieve the PrefixEntity's Prefix_ID.
   *
   * @return Returns an Integer representing the Prefix_ID of this PrefixEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Prefix_ID"
   **/
    public abstract Integer getPrefix_ID();

    /**
   * Set the PrefixEntity's Prefix_ID.
   *
   * @param pPrefix_ID The Prefix_ID of this PrefixEntity. Is set at creation time.
   **/
    public abstract void setPrefix_ID(java.lang.Integer pPrefix_ID);

    /**
   * Retrieve the PrefixEntity's Prefix.
   *
   * @return Returns an String representing the Prefix of this PrefixEntity.
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="Prefix"
   **/
    public abstract String getPrefix();

    /**
   * Set the PrefixEntity's Prefix.
   *
   * @param pPrefix The Prefix of this PrefixEntity.  Is set at creation time.
   **/
    public abstract void setPrefix(java.lang.String pPrefix);

    /**
   * Create a PrefixEntity based on the supplied PrefixEntity Value Object.
   *
   * @param pPrefixEntity The data used to create the PrefixEntity.
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
    public PrefixEntityPK ejbCreate(PrefixEntityData pPrefixEntity) throws InvalidValueException, EJBException, CreateException {
        PrefixEntityData lData = (PrefixEntityData) pPrefixEntity.clone();
        try {
            lData.setPrefix_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(PrefixEntityData pPrefixEntity) {
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
