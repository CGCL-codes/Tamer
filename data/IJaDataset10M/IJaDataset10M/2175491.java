package com.medcentrex.entity;

import com.medcentrex.interfaces.InvalidValueException;
import com.medcentrex.interfaces.Object_IdentifierEntity;
import com.medcentrex.interfaces.Object_IdentifierEntityData;
import com.medcentrex.interfaces.Object_IdentifierEntityHome;
import com.medcentrex.interfaces.Object_IdentifierEntityPK;
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
 * The Entity bean represents a Object_IdentifierEntity
 *
 * @author Andreas Schaefer
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="Object_IdentifierEntity"
 *           display-name="Object_IdentifierEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/Object_IdentifierEntity"
 * 			 schema="object_identifier"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="Object_Identifier"
 *
 * @ejb:ejb-ref ejb-name="SequenceGenerator"
 * 				ref-name="com/medcentrex/SequenceGenerator"
 *
 * @ejb:transaction type="Required"
 *
 * @ejb:data-object extends="com.medcentrex.interfaces.AbstractData"
 *                  setdata="false"
 *
 * @ejb:finder signature="Object_IdentifierEntity findByObject_Identifier_ID(java.lang.Integer Object_Identifier_ID)"
 * 	query="select OBJECT(ob) from object_identifier ob where ob.object_Identifier_ID=?1"
 *
 * @jboss:table-name table-name="object_identifier"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class Object_IdentifierEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pObject_IdentifierEntity The Value Object containing the Object_IdentifierEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(Object_IdentifierEntityData pObject_IdentifierEntity) throws InvalidValueException {
        if (pObject_IdentifierEntity == null) {
            throw new InvalidValueException("object.undefined", "Object_IdentifierEntity");
        }
        if (pObject_IdentifierEntity.getObject_Identifier_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Object_IdentifierEntity", "Id" });
        }
        setObject_Identifier_ID(pObject_IdentifierEntity.getObject_Identifier_ID());
        setParent(pObject_IdentifierEntity.getParent());
        setChild(pObject_IdentifierEntity.getChild());
        setIdentifier_Description(pObject_IdentifierEntity.getIdentifier_Description());
        setIdentifier(pObject_IdentifierEntity.getIdentifier());
        setIdentifier_Type(pObject_IdentifierEntity.getIdentifier_Type());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pObject_IdentifierEntity The Value Object containing the Object_IdentifierEntity values
   *
   *
   * @throws InvalidValueException If one of the values are not correct,
   *                               this will not roll back the transaction
   *                               because the caller has the chance to
   *                               fix the problem and try again
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(Object_IdentifierEntityData pObject_IdentifierEntity) throws InvalidValueException {
        if (pObject_IdentifierEntity == null) {
            throw new InvalidValueException("object.undefined", "Object_IdentifierEntity");
        }
        if (pObject_IdentifierEntity.getObject_Identifier_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "Object_IdentifierEntity", "Id" });
        }
        setParent(pObject_IdentifierEntity.getParent());
        setChild(pObject_IdentifierEntity.getChild());
        setIdentifier_Description(pObject_IdentifierEntity.getIdentifier_Description());
        setIdentifier(pObject_IdentifierEntity.getIdentifier());
        setIdentifier_Type(pObject_IdentifierEntity.getIdentifier_Type());
    }

    /**
   * Create and return a Object_IdentifierEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a Object_IdentifierEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public Object_IdentifierEntityData getValueObject() {
        Object_IdentifierEntityData lData = new Object_IdentifierEntityData();
        lData.setObject_Identifier_ID(getObject_Identifier_ID());
        lData.setParent(getParent());
        lData.setChild(getChild());
        lData.setIdentifier_Description(getIdentifier_Description());
        lData.setIdentifier(getIdentifier());
        lData.setIdentifier_Type(getIdentifier_Type());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "Object_IdentifierEntityBean [ " + getValueObject() + " ]";
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
            lUniqueId = lBean.getNextNumber(lSequenceName, "Object_Identifier_ID");
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
   * @jboss:column-name name="Object_Identifier_ID"
   **/
    public abstract Integer getObject_Identifier_ID();

    public abstract void setObject_Identifier_ID(java.lang.Integer pObject_Identifier_ID);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Parent"
   **/
    public abstract Integer getParent();

    public abstract void setParent(java.lang.Integer pParent);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Child"
   **/
    public abstract Integer getChild();

    public abstract void setChild(java.lang.Integer pChild);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Identifier_Description"
   **/
    public abstract String getIdentifier_Description();

    public abstract void setIdentifier_Description(java.lang.String Identifier_Description);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Identifier"
   **/
    public abstract String getIdentifier();

    public abstract void setIdentifier(java.lang.String Identifier);

    /**
   * @ejb:persistent-field
   * @jboss:column-name name="Identifier_Type"
   **/
    public abstract String getIdentifier_Type();

    public abstract void setIdentifier_Type(java.lang.String Identifier_Type);

    /**
   * Create a Object_IdentifierEntity based on the supplied Object_IdentifierEntity Value Object.
   *
   * @param pObject_IdentifierEntity The data used to create the Object_IdentifierEntity.
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
    public Object_IdentifierEntityPK ejbCreate(Object_IdentifierEntityData pObject_IdentifierEntity) throws InvalidValueException, EJBException, CreateException {
        Object_IdentifierEntityData lData = (Object_IdentifierEntityData) pObject_IdentifierEntity.clone();
        try {
            lData.setObject_Identifier_ID(generateUniqueId());
        } catch (ServiceUnavailableException se) {
            throw new EJBException(se.getMessage());
        }
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(Object_IdentifierEntityData pObject_IdentifierEntity) {
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
