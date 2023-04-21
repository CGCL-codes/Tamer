package jcontrol.jini.basic;

import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.UnicastRemoteObject;
import java.io.IOException;
import jcontrol.util.persist.PersistentException;
import jcontrol.util.persist.PersistentWriteException;
import net.jini.lookup.JoinManager;
import net.jini.discovery.DiscoveryGroupManagement;
import net.jini.discovery.DiscoveryLocatorManagement;
import net.jini.core.entry.Entry;
import net.jini.core.discovery.LookupLocator;

/**
 * This class provides a server-side implementation of the 
 * <code>BasicAdmin</code> interface.  It extends 
 * <code>UnicastRemoteObject</code>, and thus is an RMI server object.  The
 * admininstrative proxy object returned to clients via 
 * <code>getAdmin()</code> will call back to this object via RMI.<P>
 *
 * @author Bjoern Hornburg
 */
public class BasicAdminImpl extends UnicastRemoteObject implements BasicAdmin {

    protected BasicUnicastService service;

    /**
     * Constructor for BasicAdminImpl.  You pass in a reference to the
     * service wrapper for your service.
     *
     * @param service Your service wrapper.  The wrapper should have a
     * valid Persistifier that will be used by this code.
     * @exception RemoteException If there is a problem exporting the
     * BasicAdminImpl remote server object.
     */
    protected BasicAdminImpl(BasicUnicastService service) throws RemoteException {
        this.service = service;
    }

    /**
     * Destroy is an irrevocable operation.  Not only does it terminate the
     * join process and shut down the service, it also deletes the service's
     * persistent storage.
     */
    public synchronized void destroy() throws RemoteException {
        service.cleanup();
    }

    /**
     * Sets the storage location to the specified string. 
     *
     * @param loc Denotes the new storage location.  This should be a
     * valid string for the given type of persistifier in use by the
     * service.
     * @exception RemoteException If there was a problem setting the
     * storage location.
     */
    public synchronized void setStorageLocation(String loc) {
    }

    /**
    * Returns the storage location currently in use by the service's
    * persistifier.
    * 
    * @return The current storage location, as a string.
    */
    public String getStorageLocation() {
        return "";
    }

    /**
     * Returns the attributes currently in use by the service wrapper's
     * JoinManager.
     *
     * @return The current attribute set for the service.
     */
    public Entry[] getLookupAttributes() {
        return service.getJoinManager().getAttributes();
    }

    /**
     * Adds new attributes to the service's set of attributes.  The
     * semantics of this method are the same as those of
     * JoinManager.addAttributes().
     *
     * @param attrSets An array of the new attributes to be added.
     * @exception RemoteException If there was a problem adding the
     * attributes.
     */
    public void addLookupAttributes(Entry[] attrSets) throws UnexpectedException {
        service.getJoinManager().addAttributes(attrSets, true);
        try {
            service.attributesCheckpoint();
        } catch (PersistentException e) {
            throw new UnexpectedException("Cannot add attributes persistently.", e);
        }
    }

    /**
     * Modifies the set of attributes associated with the service's
     * registrations.  The semantics of this method are the same as those
     * of the JoinManager.modifyAttributes() method.
     *
     * @param attrSetTemplates An array of attributes used to determine which
     * attributes from the service's current registrations should be
     * modified.
     * @param attrSets An array of attributes that represent the actual
     * modifications to make to attributes matching the templates.
     * @exception RemoteException If there was a problem modifying the
     * attributes.
     */
    public void modifyLookupAttributes(Entry[] attrSetTemplates, Entry[] attrSets) throws UnexpectedException {
        service.getJoinManager().modifyAttributes(attrSetTemplates, attrSets, true);
        try {
            service.attributesCheckpoint();
        } catch (PersistentException e) {
            throw new UnexpectedException("Cannot modify attributes persistently.", e);
        }
    }

    /**
     * Returns the set of groups the service is currently attempting to join.
     *
     * @return An array containing the current groups.
     */
    public String[] getLookupGroups() {
        return service.getDiscoveryGroupManagement().getGroups();
    }

    /**
     * Adds new groups to the set of groups the service is attempting to join.
     *
     * @param groups An array indicating which groups should be added to
     * the join list.
     * @exception RemoteException If there is a problem modifying the group
     * list.
     */
    public void addLookupGroups(String[] groups) throws UnexpectedException {
        IOException ioEx = null;
        try {
            service.getDiscoveryGroupManagement().addGroups(groups);
        } catch (IOException e) {
            ioEx = e;
        }
        try {
            service.groupsCheckpoint();
        } catch (PersistentWriteException e) {
            if (ioEx == null) {
                throw new UnexpectedException("Not able to add groups persistently.", e);
            } else {
                throw new UnexpectedException("Groups not added persistently and failure during re-discovery.", ioEx);
            }
        }
        if (ioEx != null) {
            throw new UnexpectedException("Groups added, but failure during re-discovery.", ioEx);
        }
    }

    /**
     * Removes the indicated groups from the set of groups to be joined.
     *
     * @param groups The groups to remove from the join set.
     * @exception RemoteException If there was a problem modifying the
     * join set.
     */
    public void removeLookupGroups(String[] groups) throws UnexpectedException {
        service.getDiscoveryGroupManagement().removeGroups(groups);
        try {
            service.groupsCheckpoint();
        } catch (PersistentWriteException e) {
            throw new UnexpectedException("Not able to remove groups persistently.", e);
        }
    }

    /**
     * Replaces the set of groups the service is currently attempting to join
     * with the indicated set.
     *
     * @param groups The new groups to install as the join set.
     * @exception RemoteException If there was a problem modifying the join
     * set.
     */
    public void setLookupGroups(String[] groups) throws UnexpectedException {
        IOException ioEx = null;
        try {
            service.getDiscoveryGroupManagement().addGroups(groups);
        } catch (IOException e) {
            ioEx = e;
        }
        try {
            service.groupsCheckpoint();
        } catch (PersistentWriteException e) {
            if (ioEx == null) {
                throw new UnexpectedException("Not able to set new groups persistently.", e);
            } else {
                throw new UnexpectedException("New groups not set persistently and failure during re-discovery.", ioEx);
            }
        }
        if (ioEx != null) {
            throw new UnexpectedException("New groups set, but failure during re-discovery.", ioEx);
        }
    }

    /**
     * Returns the set of lookup locator URLs that the service is currently
     * attempting to join.
     *
     * @return An array containing the locators the service is attempting
     * to join.
     */
    public LookupLocator[] getLookupLocators() {
        return service.getDiscoveryLocatorManagement().getLocators();
    }

    /**
     * Adds new locators to the set of locators the service is attempting to join.
     *
     * @param locators An array indicating which locators should be added to
     * the join list.
     * @exception RemoteException If there is a problem modifying the join
     * list.
     */
    public void addLookupLocators(LookupLocator[] locators) throws UnexpectedException {
        service.getDiscoveryLocatorManagement().addLocators(locators);
        try {
            service.locatorsCheckpoint();
        } catch (PersistentWriteException e) {
            throw new UnexpectedException("Not able to add locators persistently.", e);
        }
    }

    /**
     * Removes the indicated locators from the set of locators to be joined.
     *
     * @param locators The locators to remove from the join set.
     * @exception RemoteException If there was a problem modifying the
     * join set.
     */
    public void removeLookupLocators(LookupLocator[] locators) throws UnexpectedException {
        service.getDiscoveryLocatorManagement().removeLocators(locators);
        try {
            service.locatorsCheckpoint();
        } catch (PersistentWriteException e) {
            throw new UnexpectedException("Not able to remove locators persistently.", e);
        }
    }

    /**
     * Replaces the set of locators the service is currently attempting to join
     * with the indicated set.
     *
     * @param locators The new locators to install as the join set.
     * @exception RemoteException If there was a problem modifying the join
     * set.
     */
    public void setLookupLocators(LookupLocator[] locators) throws UnexpectedException {
        service.getDiscoveryLocatorManagement().setLocators(locators);
        try {
            service.locatorsCheckpoint();
        } catch (PersistentWriteException e) {
            throw new UnexpectedException("Not able to set new locators persistently.", e);
        }
    }
}
