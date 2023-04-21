package org.redwood.business.usermanagement.criterionselection;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import org.redwood.business.usermanagement.selectioncriterion.*;
import org.redwood.business.usermanagement.website.*;
import org.redwood.tools.*;
import javax.naming.*;
import javax.rmi.*;

/**
 * The business object bean class.
 *
 * @author  
 * @version 1.0
 */
public class CriterionSelectionBean implements EntityBean, CriterionSelection {

    protected EntityContext entityContext;

    public String rw_id;

    public String rw_webSiteID;

    public String rw_selectionCriterionID;

    public String rw_value;

    public CriterionSelectionBean() {
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
    public CriterionSelectionPK ejbCreate(String id) throws RemoteException {
        this.rw_id = id;
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
    public CriterionSelectionPK ejbCreate(String id, String webSiteID, String selectionCriterionID, String value) throws RemoteException {
        this.rw_id = id;
        this.rw_value = value;
        this.rw_webSiteID = webSiteID;
        this.rw_selectionCriterionID = selectionCriterionID;
        return null;
    }

    /**
   * Each ejbCreate method should have a matching ejbPostCreate method
   */
    public void ejbPostCreate(String id) throws RemoteException {
    }

    public void ejbPostCreate(String id, String webSiteID, String selectionCriterionID, String value) throws RemoteException {
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

    public String getRw_webSiteID() throws RemoteException {
        return this.rw_webSiteID;
    }

    public void setRw_webSiteID(String webSiteID) throws RemoteException {
        this.rw_webSiteID = webSiteID;
    }

    public String getRw_selectionCriterionID() throws RemoteException {
        return this.rw_selectionCriterionID;
    }

    public void setRw_selectionCriterionID(String selectionCriterionID) throws RemoteException {
        this.rw_selectionCriterionID = selectionCriterionID;
    }

    public SelectionCriterion getSelectionCriterion() throws RemoteException {
        SelectionCriterion selectionCriterion = null;
        try {
            Context initialContext = new InitialContext();
            SelectionCriterionHome home = (SelectionCriterionHome) PortableRemoteObject.narrow(initialContext.lookup(SelectionCriterionHome.COMP_NAME), SelectionCriterionHome.class);
            selectionCriterion = home.findByPrimaryKey(new SelectionCriterionPK(this.getRw_selectionCriterionID()));
        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return selectionCriterion;
    }

    /**
   * Returns the website
   */
    public WebSite getWebSite() throws RemoteException {
        WebSite webSite = null;
        try {
            Context initialContext = new InitialContext();
            WebSiteHome home = (WebSiteHome) PortableRemoteObject.narrow(initialContext.lookup(WebSiteHome.COMP_NAME), WebSiteHome.class);
            webSite = home.findByPrimaryKey(new WebSitePK(this.getRw_webSiteID()));
        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
        return webSite;
    }

    public String getRw_value() throws RemoteException {
        return this.rw_value;
    }

    public void setRw_value(String value) throws RemoteException {
        this.rw_value = value;
    }
}
