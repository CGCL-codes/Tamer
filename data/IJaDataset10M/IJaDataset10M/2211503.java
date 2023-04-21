package org.redwood.business.usermanagement.website;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import org.redwood.business.usermanagement.criterionselection.*;
import org.redwood.business.usermanagement.datasource.*;
import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;
import javax.transaction.UserTransaction;

/**
 * The business object bean class.
 *
 * @author  
 * @version 1.0
 */
public class WebSiteBean implements EntityBean, WebSite {

    protected EntityContext entityContext;

    public String rw_id;

    public String rw_name;

    public int rw_deltaTime;

    public String rw_customerID;

    public int rw_active;

    public int rw_dnsresolve;

    public int rw_periodicityType;

    public java.sql.Timestamp rw_date;

    public int rw_savefiltereddata;

    public WebSiteBean() {
    }

    /**
   * There must be one ejbCreate() method per create() method on the Home interface,
   * and with the same signature.
   *
   * @param id           primary key id.
   *
   * @return pk primary key set to null
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container
   */
    public WebSitePK ejbCreate(String id) throws RemoteException {
        this.rw_id = id;
        this.rw_active = 0;
        this.rw_dnsresolve = 0;
        this.rw_periodicityType = 0;
        this.rw_date = null;
        this.rw_savefiltereddata = 1;
        return null;
    }

    /**
   * There must be one ejbCreate() method per create() method on the Home interface,
   * and with the same signature.
   *
   * @param id           primary key id.
   *
   * @return pk primary key set to null
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container
   */
    public WebSitePK ejbCreate(String id, String name, int deltaTime, String customerID, Date date) throws RemoteException {
        this.rw_id = id;
        this.rw_name = name;
        this.rw_deltaTime = deltaTime;
        this.rw_customerID = customerID;
        this.rw_active = 0;
        this.rw_dnsresolve = 0;
        this.rw_periodicityType = 0;
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        this.rw_date = sqlDate;
        this.rw_date.setNanos(0);
        this.rw_savefiltereddata = 1;
        return null;
    }

    /**
   * There must be one ejbCreate() method per create() method on the Home interface,
   * and with the same signature.
   *
   * @param id           primary key id.
   *
   * @return pk primary key set to null
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container
   */
    public WebSitePK ejbCreate(String id, String name, int deltaTime, String customerID, int dnsresolve, Date date) throws RemoteException {
        this.rw_id = id;
        this.rw_name = name;
        this.rw_deltaTime = deltaTime;
        this.rw_customerID = customerID;
        this.rw_active = 0;
        this.rw_dnsresolve = dnsresolve;
        this.rw_periodicityType = 0;
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        this.rw_date = sqlDate;
        this.rw_date.setNanos(0);
        this.rw_savefiltereddata = 1;
        return null;
    }

    /**
   * Each ejbCreate method should have a matching ejbPostCreate method
   */
    public void ejbPostCreate(String id) throws RemoteException {
    }

    public void ejbPostCreate(String id, String name, int deltaTime, String customerID, Date date) throws RemoteException {
    }

    public void ejbPostCreate(String id, String name, int deltaTime, String customerID, int dnsresolve, Date date) throws RemoteException {
    }

    /**
   * A container invokes this method when the instance is taken out of the pool
   * of available instances to become associated with a specific EJB object.
   * This method transitions the instance to the ready state.
   *
   * This method executes in an unspecified transaction context.
   *
   * @exception RemoteException If the instance could not perform the function
   *            requested by the container because of an system-level error.
   */
    public void ejbActivate() throws RemoteException {
    }

    /**
   * A container invokes this method to instruct the instance to synchronize
   * its state by loading it state from the underlying database.
   * This method always executes in the proper transaction context.
   *
   * @exception RemoteException  If the instance could not perform the function
   *            requested by the container because of a system-level error.
   *
   */
    public void ejbLoad() throws RemoteException {
    }

    /**
   * A container invokes this method on an instance before the instance becomes
   * disassociated with a specific EJB object. After this method completes, the
   * container will place the instance into the pool of available instances.
   *
   * This method executes in an unspecified transaction context.
   *
   * @exception RemoteException  If the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
    public void ejbPassivate() throws RemoteException {
    }

    /**
   * A container invokes this method before it removes the EJB object
   * that is currently associated with the instance. This method is invoked
   * when a client invokes a remove operation on the enterprise Bean's home
   * interface or the EJB object's remote interface. This method transitions
   * the instance from the ready state to the pool of available instances.
   *
   * This method is called in the transaction context of the remove operation.
   *
   * @exception RemoteException  Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   * @exception RemoveException  The enterprise Bean does not allow destruction
   *            of the object.
   */
    public void ejbRemove() throws RemoteException, RemoveException {
    }

    /**
   * A container invokes this method to instruct the instance to synchronize
   * its state by storing it to the underlying database.
   *
   * This method always executes in the proper transaction context.
   *
   * @exception: RemoteException  Thrown if the instance could not perform the function
   *             requested by the container because of a system-level error.
   */
    public void ejbStore() throws RemoteException {
    }

    /**
   * Sets the associated entity context. The container invokes this method
   * on an instance after the instance has been created.
   *
   * This method is called in an unspecified transaction context.
   *
   * @param ctx - An EntityContext interface for the instance. The instance should
   *              store the reference to the context in an instance variable.
   * @exception RemoteException  Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
    public void setEntityContext(EntityContext ctx) throws RemoteException {
        entityContext = ctx;
    }

    /**
   * Unsets the associated entity context. The container calls this method
   * before removing the instance. This is the last method that the container
   * invokes on the instance. The Java garbage collector will eventually invoke
   * the finalize() method on the instance.
   *
   * This method is called in an unspecified transaction context.
   *
   * @exception RemoteException  Thrown if the instance could not perform the function
   *            requested by the container because of a system-level error.
   */
    public void unsetEntityContext() {
        entityContext = null;
    }

    public String getRw_id() throws RemoteException {
        return this.rw_id;
    }

    public String getRw_name() throws RemoteException {
        return this.rw_name;
    }

    public void setRw_name(String name) throws RemoteException {
        this.rw_name = name;
    }

    public String getRw_customerID() throws RemoteException {
        return this.rw_customerID;
    }

    public void setRw_customerID(String customerID) throws RemoteException {
        this.rw_customerID = customerID;
    }

    public Object getCustomer() throws RemoteException {
        return null;
    }

    /**
   * Returns all the criterionSelections
   */
    public Collection getDataSources() throws RemoteException {
        Collection dataSources = new Vector();
        try {
            Context initialContext = new InitialContext();
            DataSourceHome home = (DataSourceHome) PortableRemoteObject.narrow(initialContext.lookup(DataSourceHome.COMP_NAME), DataSourceHome.class);
            dataSources = home.findByRw_webSiteID(this.getRw_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSources;
    }

    public int getRw_deltaTime() throws RemoteException {
        return this.rw_deltaTime;
    }

    public void setRw_deltaTime(int deltaTime) throws RemoteException {
        this.rw_deltaTime = deltaTime;
    }

    /**
   * Returns all the criterionSelections
   */
    public Collection getCriterionSelections() throws RemoteException {
        Collection criterionSelections = null;
        try {
            Context initialContext = new InitialContext();
            CriterionSelectionHome home = (CriterionSelectionHome) PortableRemoteObject.narrow(initialContext.lookup(CriterionSelectionHome.COMP_NAME), CriterionSelectionHome.class);
            criterionSelections = home.findByRw_webSiteID(rw_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return criterionSelections;
    }

    /**
   *
   */
    public void setRw_active(int active) throws RemoteException {
        this.rw_active = active;
    }

    public int getRw_active() throws RemoteException {
        return this.rw_active;
    }

    /**
   */
    public void setRw_dnsresolve(int resolve) throws RemoteException {
        this.rw_dnsresolve = resolve;
    }

    /**
   */
    public int getRw_dnsresolve() throws RemoteException {
        return this.rw_dnsresolve;
    }

    public void setRw_periodicityType(int type) throws RemoteException {
        this.rw_periodicityType = type;
    }

    public int getRw_periodicityType() throws RemoteException {
        return this.rw_periodicityType;
    }

    public Date getRw_date() throws RemoteException {
        return this.rw_date;
    }

    public void setRw_date(Date date) throws RemoteException {
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        this.rw_date = sqlDate;
        this.rw_date.setNanos(0);
    }

    public void setRw_savefiltereddata(int flag) throws RemoteException {
        this.rw_savefiltereddata = flag;
    }

    public int getRw_savefiltereddata() throws RemoteException {
        return this.rw_savefiltereddata;
    }
}
