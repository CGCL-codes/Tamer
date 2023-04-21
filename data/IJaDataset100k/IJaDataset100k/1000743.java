package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.GuarantorEntity;
import com.medcentrex.bridge.interfaces.GuarantorEntityData;
import com.medcentrex.bridge.interfaces.GuarantorEntityHome;
import com.medcentrex.bridge.interfaces.GuarantorEntityPK;
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
 * The Entity bean represents a GuarantorEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="GuarantorEntity"
 *           display-name="GuarantorEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/GuarantorEntity"
 * 			schema="Guarantor"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="guarantor"
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
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.GuarantorEntity findByPerson_ID(java.lang.Integer pPerson_ID)"
 * query="SELECT OBJECT(ob) FROM Guarantor ob WHERE ob.person_ID=?1"
 *
 * @jboss:table-name table-name="guarantor"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class GuarantorEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pGuarantorEntity The Value Object containing the GuarantorEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(GuarantorEntityData pGuarantorEntity) throws InvalidValueException {
        if (pGuarantorEntity == null) {
            throw new InvalidValueException("object.undefined", "GuarantorEntity");
        }
        if (pGuarantorEntity.getPerson_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "GuarantorEntity", "Id" });
        }
        setPerson_ID(pGuarantorEntity.getPerson_ID());
    }

    /**
   * Create and return a GuarantorEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a GuarantorEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public GuarantorEntityData getValueObject() {
        GuarantorEntityData lData = new GuarantorEntityData();
        lData.setPerson_ID(getPerson_ID());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "GuarantorEntityBean [ " + getValueObject() + " ]";
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
   * Retrieve the GuarantorEntity's Person_ID.
   *
   * @return Returns an Integer representing the Person_ID of this GuarantorEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Person_ID"
   **/
    public abstract Integer getPerson_ID();

    /**
   * Set the GuarantorEntity's Person_ID.
   *
   * @param pPerson_ID The Person_ID of this GuarantorEntity. Is set at creation time.
   **/
    public abstract void setPerson_ID(java.lang.Integer pPerson_ID);

    /**
   * Create a GuarantorEntity based on the supplied GuarantorEntity Value Object.
   *
   * @param pGuarantorEntity The data used to create the GuarantorEntity.
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
    public GuarantorEntityPK ejbCreate(GuarantorEntityData pGuarantorEntity) throws InvalidValueException, EJBException, CreateException {
        GuarantorEntityData lData = (GuarantorEntityData) pGuarantorEntity.clone();
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(GuarantorEntityData pGuarantorEntity) {
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
