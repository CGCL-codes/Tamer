package com.medcentrex.bridge.entity;

import com.medcentrex.bridge.interfaces.InvalidValueException;
import com.medcentrex.bridge.interfaces.PageEntity;
import com.medcentrex.bridge.interfaces.PageEntityData;
import com.medcentrex.bridge.interfaces.PageEntityHome;
import com.medcentrex.bridge.interfaces.PageEntityPK;
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
 * The Entity bean represents a PageEntity
 *
 * @author Mona Sehgal
 * @version $Revision: 1.1 $
 *
 * @ejb:bean name="PageEntity"
 *           display-name="PageEntity working on projects to support clients"
 *           type="CMP"
 *           jndi-name="ejb/com/medcentrex/bridge/PageEntity"
 *           schema="Page"
 *
 * @ejb:env-entry name="SequenceName"
 *                value="page"
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
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.PageEntity findByPage_ID(java.lang.Integer pPage_ID)"
 * query="SELECT OBJECT(ob) FROM Page ob WHERE ob.page_ID=?1"
 *
 * @ejb:finder signature="com.medcentrex.bridge.interfaces.PageEntity findByModulePage_ID(java.lang.Integer pModule_ID,java.lang.Integer pPage_ID )"
 * query="SELECT OBJECT(a) from Page AS a where a.module_ID = ?1 AND a.page_ID = ?2"
 *
 * @ejb:finder signature="java.util.Collection findByModule_ID( java.lang.Integer pModule_ID )"
 * query="SELECT OBJECT(ob) FROM Page ob WHERE ob.page_ID=?1"
 *
 *
 * @jboss:table-name table-name="page"
 *
 * @jboss:create-table create="false"
 *
 * @jboss:remove-table remove="false"
 **/
public abstract class PageEntityBean implements EntityBean {

    public EntityContext mContext;

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPageEntity The Value Object containing the PageEntity values
   *
   * @ejb:interface-method view-type="remote"
   **/
    public void setValueObject(PageEntityData pPageEntity) throws InvalidValueException {
        if (pPageEntity == null) {
            throw new InvalidValueException("object.undefined", "PageEntity");
        }
        if (pPageEntity.getPage_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "PageEntity", "Id" });
        }
        setPage_ID(pPageEntity.getPage_ID());
        setPageName(pPageEntity.getPageName());
        setModule_ID(pPageEntity.getModule_ID());
        setURL(pPageEntity.getURL());
    }

    /**
   * Store the data within the provided data object into this bean.
   *
   * @param pPageEntity The Value Object containing the PageEntity values
   *
   * @throws InvalidValueException if Page_id is not valid
   *
   * @ejb:interface-method view-type="remote"
   **/
    public void updateValueObject(PageEntityData pPageEntity) throws InvalidValueException {
        if (pPageEntity == null) {
            throw new InvalidValueException("object.undefined", "PageEntity");
        }
        if (pPageEntity.getPage_ID().intValue() <= 0) {
            throw new InvalidValueException("id.invalid", new String[] { "PageEntity", "Id" });
        }
        setPageName(pPageEntity.getPageName());
        setModule_ID(pPageEntity.getModule_ID());
        setURL(pPageEntity.getURL());
    }

    /**
   * Create and return a PageEntity data object populated with the data from
   * this bean.
   *
   * @return Returns a PageEntity value object containing the data within this
   *  bean.
   *
   * @ejb:interface-method view-type="remote"
   **/
    public PageEntityData getValueObject() {
        PageEntityData lData = new PageEntityData();
        lData.setPage_ID(getPage_ID());
        lData.setPageName(getPageName());
        lData.setModule_ID(getModule_ID());
        lData.setURL(getURL());
        return lData;
    }

    /**
   * Describes the instance and its content for debugging purpose
   *
   * @return Debugging information about the instance and its content
   **/
    public String toString() {
        return "PageEntityBean [ " + getValueObject() + " ]";
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
   * Retrieve the PageEntity's Page_ID.
   *
   * @return Returns an Integer representing the Page_ID of this PageEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Page_ID"
   **/
    public abstract Integer getPage_ID();

    /**
   * Set the PageEntity's Page_ID.
   *
   * @param pPage_ID The Page_ID of this PageEntity. Is set at creation time.
   **/
    public abstract void setPage_ID(java.lang.Integer pPage_ID);

    /**
   * Retrieve the PageEntity's module_ID.
   *
   * @return Returns an Integer representing the Module_ID of this PageEntity.
   *
   * @ejb:persistent-field
   * @ejb:pk-field
   *
   * @jboss:column-name name="Module_ID"
   **/
    public abstract Integer getModule_ID();

    /**
   * Set the PageEntity's Module_ID.
   *
   * @param pModule_ID The Module_ID of this PageEntity. Is set at creation time.
   **/
    public abstract void setModule_ID(java.lang.Integer pModule_ID);

    /**
   * @return Returns the PageName of this PageEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="PageName"
   **/
    public abstract String getPageName();

    /**
   * Specify the PageName of this PageEntity
   *
   * @param pPageName PageName of this PageEntity
   **/
    public abstract void setPageName(java.lang.String pPageName);

    /**
   * @return Returns the URL of this PageEntity
   *
   * @ejb:persistent-field
   *
   * @jboss:column-name name="URL"
   **/
    public abstract String getURL();

    /**
   * Specify the URL of this PageEntity
   *
   * @param pURL URL of this PageEntity
   **/
    public abstract void setURL(java.lang.String pURL);

    /**
   * Create a PageEntity based on the supplied PageEntity Value Object.
   *
   * @param pPageEntity The data used to create the PageEntity.
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
    public PageEntityPK ejbCreate(PageEntityData pPageEntity) throws InvalidValueException, EJBException, CreateException {
        PageEntityData lData = (PageEntityData) pPageEntity.clone();
        lData.setPage_ID(pPageEntity.getPage_ID());
        setValueObject(lData);
        return null;
    }

    public void ejbPostCreate(PageEntityData pPageEntity) {
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
