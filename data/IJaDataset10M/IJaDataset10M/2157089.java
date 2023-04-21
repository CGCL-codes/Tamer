package org.apache.jetspeed.om.registry.base;

import org.apache.jetspeed.om.registry.MediaTypeRegistry;
import org.apache.jetspeed.om.registry.MediaTypeEntry;
import org.apache.jetspeed.om.registry.RegistryEntry;
import org.apache.jetspeed.om.registry.InvalidEntryException;
import org.apache.jetspeed.om.registry.RegistryException;
import org.apache.jetspeed.services.Registry;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.capability.CapabilityMap;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

/**
 * Extends BaseRegistry implementation to override object creation
 * method
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @version $Id: BaseMediaTypeRegistry.java,v 1.7 2004/02/23 03:08:26 jford Exp $
 */
public class BaseMediaTypeRegistry extends BaseOrderedRegistry implements MediaTypeRegistry {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(BaseMediaTypeRegistry.class.getName());

    public Iterator findForCapability(CapabilityMap cm) {
        if (cm == null) {
            return null;
        }
        String type = cm.getPreferredType().getContentType();
        List result = new ArrayList();
        if (logger.isDebugEnabled()) {
            logger.debug("MediaTypeRegistry: looking for type " + type);
        }
        if (type == null) {
            return result.iterator();
        }
        try {
            Enumeration en = getEntries();
            while (en.hasMoreElements()) {
                MediaTypeEntry mte = (MediaTypeEntry) en.nextElement();
                if (logger.isDebugEnabled()) {
                    logger.debug("MediaTypeRegistry: found MediaTypeEntry for type " + mte.getMimeType());
                }
                if (type.equals(mte.getMimeType())) {
                    result.add(mte);
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("MediaTypeRegistry: found " + result.size() + " entries.");
        }
        return result.iterator();
    }

    /**
    @see Registry#setEntry
    */
    public void setEntry(RegistryEntry entry) throws InvalidEntryException {
        try {
            Registry.addEntry(Registry.MEDIA_TYPE, entry);
        } catch (RegistryException e) {
            logger.error("Exception", e);
        }
    }

    /**
    @see Registry#addEntry
    */
    public void addEntry(RegistryEntry entry) throws InvalidEntryException {
        try {
            Registry.addEntry(Registry.MEDIA_TYPE, entry);
        } catch (RegistryException e) {
            logger.error("Exception", e);
        }
    }

    /**
    @see Registry#removeEntry
    */
    public void removeEntry(String name) {
        Registry.removeEntry(Registry.MEDIA_TYPE, name);
    }

    /**
    @see Registry#removeEntry
    */
    public void removeEntry(RegistryEntry entry) {
        if (entry != null) {
            Registry.removeEntry(Registry.MEDIA_TYPE, entry.getName());
        }
    }

    /**
     * Creates a new RegistryEntry instance compatible with the current
     * Registry instance implementation
     *
     * @return the newly created RegistryEntry
     */
    public RegistryEntry createEntry() {
        return new BaseMediaTypeEntry();
    }
}
