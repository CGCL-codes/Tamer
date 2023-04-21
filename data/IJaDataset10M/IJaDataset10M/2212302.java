package org.apache.jetspeed.om.registry.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.jetspeed.om.registry.RegistryEntry;
import org.apache.jetspeed.om.registry.InvalidEntryException;
import org.apache.jetspeed.om.registry.RegistryException;
import org.apache.jetspeed.om.registry.SecurityEntry;
import org.apache.jetspeed.services.Registry;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;

/**
 * Extends BaseRegistry implementation to override object creation
 * method and ensure Registry object is synchronized with its
 * persistence backend by delegating actual addition/deletion of objects
 * to the registry service.
 * <p>To avoid loops, a RegistryService implementation using this class
 * nees to call the addLocalEntry/removeLocalEntry methods to modify
 * the in memory state of this Registry</p>
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: BaseSecurityRegistry.java,v 1.6 2004/02/23 03:08:26 jford Exp $
 */
public class BaseSecurityRegistry extends BaseRegistry {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(BaseSecurityRegistry.class.getName());

    /**
    @see Registry#setEntry
    */
    public void setEntry(RegistryEntry entry) throws InvalidEntryException {
        try {
            Registry.addEntry(Registry.SECURITY, entry);
        } catch (RegistryException e) {
            logger.error("Exception", e);
        }
    }

    /**
    @see Registry#addEntry
    */
    public void addEntry(RegistryEntry entry) throws InvalidEntryException {
        try {
            Registry.addEntry(Registry.SECURITY, entry);
        } catch (RegistryException e) {
            logger.error("Exception", e);
        }
    }

    /**
    @see Registry#removeEntry
    */
    public void removeEntry(String name) {
        Registry.removeEntry(Registry.SECURITY, name);
    }

    /**
    @see Registry#removeEntry
    */
    public void removeEntry(RegistryEntry entry) {
        if (entry != null) {
            Registry.removeEntry(Registry.SECURITY, entry.getName());
        }
    }

    /**
     * Creates a new RegistryEntry instance compatible with the current
     * Registry instance implementation
     *
     * @return the newly created RegistryEntry
     */
    public RegistryEntry createEntry() {
        return new BaseSecurityEntry();
    }

    /**
     * returns a security entry from the registry based on the name provided
     * @param String name Name of security entry we want.
     * @return SecurityEntry SecurityEntry matching the <code>name</code>
     * argument or null if no such entry exists.
     */
    public SecurityEntry getSecurityEntry(String name) {
        try {
            return (SecurityEntry) this.getEntry(name);
        } catch (InvalidEntryException e) {
            logger.error("Exception", e);
        }
        return null;
    }

    /**
     * @return SecurityEntry a new SecurityEntry instance
     */
    public SecurityEntry createSecurityEntry() {
        return (SecurityEntry) this.createEntry();
    }

    /**
     * Makes an exact copy of the named role, but changing the nmae attribute
     * to the value of <code>newName</code>
     * @param String original Name of the entry we want to clone
     * @param String newName Name to give the cloned entry
     * @return SecurityEntry The cloned entry.
     */
    public SecurityEntry cloneSecurityEntry(String original, String newName) {
        SecurityEntry baseEntry = getSecurityEntry(original);
        if (baseEntry != null) {
            SecurityEntry newEntry = cloneEntry(baseEntry);
            newEntry.setName(newName);
            return newEntry;
        }
        return null;
    }

    /**
     * Makes an indentical copy of the SecurityEntry provided using 
     * serialize/de-serialize logic to make a  clean reference
     * @param SecurityEntry secEntry the entry to clone
     * @return SecurityEntry the cloned entry.
     */
    private static SecurityEntry cloneEntry(SecurityEntry secEntry) {
        SecurityEntry clonedEntry = null;
        try {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(100);
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(bytearrayoutputstream);
            objectoutputstream.writeObject(secEntry);
            byte abyte0[] = bytearrayoutputstream.toByteArray();
            objectoutputstream.close();
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
            ObjectInputStream objectinputstream = new ObjectInputStream(bytearrayinputstream);
            clonedEntry = (SecurityEntry) objectinputstream.readObject();
            objectinputstream.close();
        } catch (Exception exception) {
        }
        return clonedEntry;
    }
}
