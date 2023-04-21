package org.redwood.business.usermanagement.websitegroup;

import java.rmi.RemoteException;
import java.util.*;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;
import org.redwood.business.usermanagement.wswsg.*;
import org.redwood.business.usermanagement.person.*;
import org.redwood.business.usermanagement.website.*;
import org.redwood.business.usermanagement.report.*;
import org.redwood.business.usermanagement.reportwsg.*;
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
public class WebSiteGroupBean implements EntityBean, WebSiteGroup {

    public static final boolean VERBOSE = false;

    protected EntityContext entityContext;

    public String rw_id;

    public String rw_name;

    public String rw_personID;

    public WebSiteGroupBean() {
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
    public WebSiteGroupPK ejbCreate(String id, String name, String personID) throws RemoteException {
        this.rw_id = id;
        this.rw_name = name;
        this.rw_personID = personID;
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
    public WebSiteGroupPK ejbCreate(String id) throws RemoteException {
        this.rw_id = id;
        return null;
    }

    /**
   * Each ejbCreate method should have a matching ejbPostCreate method
   */
    public void ejbPostCreate(String id) throws RemoteException {
    }

    public void ejbPostCreate(String id, String name, String personID) throws RemoteException {
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

    public String getRw_personID() throws RemoteException {
        return this.rw_personID;
    }

    public void setRw_personID(String personID) throws RemoteException {
        this.rw_personID = personID;
    }

    public Collection getWebSites() throws RemoteException {
        Collection webSites = new Vector();
        try {
            Context initialContext = new InitialContext();
            WsWsgHome home = (WsWsgHome) PortableRemoteObject.narrow(initialContext.lookup(WsWsgHome.COMP_NAME), WsWsgHome.class);
            Collection wsWsgs = home.findByRw_webSiteGroupID(this.getRw_id());
            Iterator iter = wsWsgs.iterator();
            while (iter.hasNext()) webSites.add(((WsWsg) iter.next()).getWebSite());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webSites;
    }

    public Collection getWebSiteIDs() throws RemoteException {
        Collection webSiteIDs = new Vector();
        try {
            Context initialContext = new InitialContext();
            WsWsgHome home = (WsWsgHome) PortableRemoteObject.narrow(initialContext.lookup(WsWsgHome.COMP_NAME), WsWsgHome.class);
            Collection wsWsgs = home.findByRw_webSiteGroupID(this.getRw_id());
            Iterator iter = wsWsgs.iterator();
            WebSite webSite;
            while (iter.hasNext()) {
                webSite = ((WsWsg) iter.next()).getWebSite();
                webSiteIDs.add(webSite.getRw_id());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webSiteIDs;
    }

    public Collection getReports() throws RemoteException {
        Collection reports = new Vector();
        try {
            Context initialContext = new InitialContext();
            ReportWsgHome home = (ReportWsgHome) PortableRemoteObject.narrow(initialContext.lookup(ReportWsgHome.COMP_NAME), ReportWsgHome.class);
            Collection reportWsgs = home.findByRw_webSiteGroupID(this.getRw_id());
            Iterator iter = reportWsgs.iterator();
            while (iter.hasNext()) reports.add(((ReportWsg) iter.next()).getReport());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    public Person getPerson() throws RemoteException {
        Person person = null;
        try {
            Context initialContext = new InitialContext();
            PersonHome home = (PersonHome) PortableRemoteObject.narrow(initialContext.lookup(PersonHome.COMP_NAME), PersonHome.class);
            person = home.findByPrimaryKey(new PersonPK(this.getRw_personID()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }

    /**
   * Adds a WebSite to this WebSiteGroup.
   * @param webSite The WebSite that should be added
   * @throws RemoteException
   */
    public void addWebSite(WebSite webSite) throws RemoteException {
        try {
            Context initialContext = new InitialContext();
            WsWsgHome home = (WsWsgHome) PortableRemoteObject.narrow(initialContext.lookup(WsWsgHome.COMP_NAME), WsWsgHome.class);
            WsWsg wsWsg = home.create(webSite.getRw_id() + this.getRw_id(), webSite.getRw_id(), this.getRw_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWebSite(String webSiteID) throws RemoteException {
        try {
            Context initialContext = new InitialContext();
            WsWsgHome home = (WsWsgHome) PortableRemoteObject.narrow(initialContext.lookup(WsWsgHome.COMP_NAME), WsWsgHome.class);
            WsWsg wsWsg = home.create(webSiteID + this.getRw_id(), webSiteID, this.getRw_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Removes a WebSite from this WebSiteGroup. The website itself will not be deleted,
   * only the allocation.
   * @param webSiteID The id of the WebSite
   * @throws Remoete Exception
   */
    public void removeWebSite(String webSiteID) throws RemoteException {
        try {
            Context initialContext = new InitialContext();
            WsWsgHome home = (WsWsgHome) PortableRemoteObject.narrow(initialContext.lookup(WsWsgHome.COMP_NAME), WsWsgHome.class);
            Collection sites = home.findByRw_webSiteGroupID(this.rw_id);
            Iterator iter = sites.iterator();
            while (iter.hasNext()) {
                WsWsgObject alloc = (WsWsgObject) iter.next();
                if (alloc.getRw_webSiteID().equals(webSiteID)) {
                    alloc.remove();
                    if (VERBOSE) System.out.println("Removed a webSite from a webSiteGroup!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
