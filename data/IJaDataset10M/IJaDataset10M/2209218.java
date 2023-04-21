package net.sf.nxqd.xmldb;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.nxqd.NxqdManager;
import net.sf.nxqd.NxqdConsumer;
import net.sf.nxqd.NxqdException;
import net.sf.nxqd.NxqdContainer;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.base.ErrorCodes;

/**
 * The class <code>NxqdDatabase</code> is the Xml:DB driver for
 * communicating with the  Nxqd Apache module.
 *
 * @see org.xmldb.api.base.Database
 *
 * @author <a href="mailto:webhiker@sourceforge.net">webhiker</a>
 * @version 1.0
 */
public class NxqdDatabase extends NxqdConsumer implements Database {

    /**
     * The variable <code>logger</code> is used for logging events.
     *
     */
    private static Logger logger = Logger.getLogger(NxqdDatabase.class.getName());

    /**
     * The <code>NXQD_NAME</code> constant is used to construct a Xml:DB URI
     * to connect to the Nxqd server.
     */
    public static final String NXQD_NAME = "nxqd";

    public static final String NXQD_ROOT = "db";

    public static final String NXQD_INTERNAL_SEP = "-+-";

    /**
     * The a new <code>NxqdDatabase</code> constructor creates a new instance
     * of this driver.
     *
     */
    public NxqdDatabase() {
        super();
    }

    /**
     * @see org.xmldb.api.base.Database#getName()
     */
    public final String getName() throws XMLDBException {
        return new String(NXQD_NAME);
    }

    /**
     * @see org.xmldb.api.base.Database#getCollection(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    public final Collection getCollection(final String uri, final String userName, final String passWord) throws XMLDBException {
        try {
            logger.fine("getCollection(" + uri + "," + userName + "," + passWord + ")");
            setNxqdManager(new NxqdManager(parseHost(uri), parsePort(uri)));
            String collectionName = extractCollectionName(uri);
            String containerName = encodePath(extractCollectionPath(uri));
            NxqdContainer container;
            if (collectionName.equals(NXQD_ROOT)) {
                if (getNxqdManager().containerExists(containerName)) {
                    container = getNxqdManager().getContainer(containerName);
                } else {
                    container = getNxqdManager().createContainer(containerName);
                }
            } else {
                container = getNxqdManager().getContainer(containerName);
            }
            return new NxqdCollection(collectionName, this, container, getParentCollectionFromContainerName(containerName));
        } catch (NxqdException ne) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Error getting Collection :" + ne.getMessage(), ne);
        }
    }

    protected Collection createCollection(String collectionName, NxqdCollection parent) throws XMLDBException {
        try {
            logger.fine("createCollection(" + collectionName + "," + parent.getName() + ")");
            NxqdContainer container = getNxqdManager().createContainer(parent.getContainer().getName() + NXQD_INTERNAL_SEP + collectionName);
            NxqdCollection newCollection = new NxqdCollection(collectionName, this, container, parent);
            return newCollection;
        } catch (NxqdException ne) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Error creating Collection :" + ne.getMessage(), ne);
        }
    }

    protected Collection getChildCollection(String collectionName, NxqdCollection parent) throws XMLDBException {
        try {
            logger.fine("getChildCollection(" + collectionName + "," + parent.getName() + ")");
            NxqdContainer container;
            if (collectionName.equals(NXQD_ROOT)) {
                container = getNxqdManager().getContainer(NXQD_ROOT);
            } else {
                container = getNxqdManager().getContainer(parent.getContainer().getName() + NXQD_INTERNAL_SEP + collectionName);
            }
            return new NxqdCollection(collectionName, this, container, parent);
        } catch (NxqdException ne) {
            return null;
        }
    }

    /**
     * The <code>getCollectionFromContainerName</code> method returns the Collection specified by
     * the containerName. Note, this collectionName is the actual name name of the
     * underlying container object.
     *
     * @param containerName a <code>String</code> value
     * @return a <code>Collection</code> value
     * @exception XMLDBException if an error occurs
     */
    protected NxqdCollection getCollectionFromContainerName(final String containerName) throws XMLDBException {
        try {
            logger.fine("getCollectionFromContainerName(" + containerName + ")");
            NxqdContainer container = getNxqdManager().getContainer(containerName);
            String collectionName;
            if (containerName.equals(NXQD_ROOT)) {
                collectionName = NXQD_ROOT;
            } else {
                collectionName = containerName.substring(containerName.lastIndexOf(NXQD_INTERNAL_SEP) + NXQD_INTERNAL_SEP.length(), containerName.length());
            }
            return new NxqdCollection(collectionName, this, container, getParentCollectionFromContainerName(containerName));
        } catch (NxqdException ne) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Error getting Collection :" + ne.getMessage(), ne);
        }
    }

    /**
     * The <code>getCollectionFromContainerName</code> method returns the Collection specified by
     * the containerName. Note, this collectionName is the actual name name of the
     * underlying container object.
     *
     * @param containerName a <code>String</code> value
     * @return a <code>Collection</code> value
     * @exception XMLDBException if an error occurs
     */
    protected NxqdCollection getParentCollectionFromContainerName(final String containerName) throws XMLDBException {
        try {
            NxqdContainer container = getNxqdManager().getContainer(containerName);
            String collectionName;
            if (containerName.equals(NXQD_ROOT)) {
                collectionName = NXQD_ROOT;
                return new NxqdCollection(collectionName, this, container, null);
            } else {
                collectionName = containerName.substring(0, container.getName().lastIndexOf(NXQD_INTERNAL_SEP));
                return new NxqdCollection(collectionName, this, container, getParentCollectionFromContainerName(collectionName));
            }
        } catch (NxqdException ne) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Error getting Collection :" + ne.getMessage(), ne);
        }
    }

    protected void deleteCollection(String collectionName, NxqdCollection parent) throws XMLDBException {
        try {
            getNxqdManager().deleteContainer(parent.getContainer().getName() + NXQD_INTERNAL_SEP + collectionName);
        } catch (NxqdException ne) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Error deleting Collection :" + ne.getMessage(), ne);
        }
    }

    protected List listChildCollections(NxqdCollection collection) throws XMLDBException {
        try {
            logger.fine("listChildCollections(" + collection.getName() + ")");
            List allCollections = getNxqdManager().listContainers();
            String name = collection.getContainer().getName();
            List childCollections = new ArrayList();
            String currentCollection, parentName, temp;
            parentName = collection.getContainer().getName();
            for (int i = 0; i < allCollections.size(); i++) {
                currentCollection = allCollections.get(i).toString();
                if (currentCollection.startsWith(name)) {
                    if (!currentCollection.equals(parentName)) {
                        temp = currentCollection.substring(currentCollection.indexOf(parentName) + parentName.length() + NXQD_INTERNAL_SEP.length(), currentCollection.length());
                        if (temp.indexOf(NXQD_INTERNAL_SEP) < 0) {
                            childCollections.add(temp);
                        }
                    }
                }
            }
            return childCollections;
        } catch (NxqdException ne) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, "Error listing child collections :" + ne.getMessage(), ne);
        }
    }

    /**
     * @see org.xmldb.api.base.Database#acceptsURI(java.lang.String)
     */
    public final boolean acceptsURI(final String uri) throws XMLDBException {
        return uri.startsWith(NXQD_NAME + "://");
    }

    /**
     * @see org.xmldb.api.base.Database#getConformanceLevel()
     */
    public final String getConformanceLevel() throws XMLDBException {
        return "1";
    }

    /**
     * @see org.xmldb.api.base.Configurable#getProperty(java.lang.String)
     */
    public final String getProperty(final String name) throws XMLDBException {
        return System.getProperty(name);
    }

    /**
     * @see org.xmldb.api.base.Configurable#setProperty(java.lang.String,
     * java.lang.String)
     */
    public final void setProperty(final String name, final String value) throws XMLDBException {
        System.setProperty(name, value);
    }

    /**
     * The <code>parseHost</code> method will extract the host value
     * from the uri parameter.
     *
     * @param uri a <code>String</code> value
     * @return a <code>String</code> value
     */
    public static String parseHost(final String uri) {
        String tag = "://";
        String parsedHost = uri.substring(uri.indexOf(NXQD_NAME + tag) + NXQD_NAME.length() + tag.length(), uri.length());
        if (parsedHost.indexOf('/') > 0) {
            parsedHost = parsedHost.substring(0, parsedHost.indexOf('/'));
        } else {
            parsedHost = parsedHost.substring(0, parsedHost.length());
        }
        if (parsedHost.indexOf(':') > 0) {
            parsedHost = parsedHost.substring(0, parsedHost.indexOf(':'));
        }
        if (parsedHost.length() < 2) {
            parsedHost = "localhost";
        }
        return parsedHost;
    }

    /**
     * The <code>parseHost</code> method will extract the port value from
     * the uri parameter.
     *
     * @param uri a <code>String</code> value
     * @return a <code>String</code> value
     */
    public static String parsePort(final String uri) throws NxqdException {
        String tag = "://";
        String parsedPort = uri.substring(uri.indexOf(NXQD_NAME + tag) + NXQD_NAME.length() + tag.length(), uri.length());
        if (parsedPort.indexOf('/') > 0) {
            parsedPort = parsedPort.substring(0, parsedPort.indexOf('/'));
        } else {
            parsedPort = parsedPort.substring(0, parsedPort.length());
        }
        if (parsedPort.indexOf(':') > 0) {
            parsedPort = parsedPort.substring(parsedPort.indexOf(':') + 1, parsedPort.length());
            if (parsedPort.indexOf('/') > 0) {
                parsedPort = parsedPort.substring(0, parsedPort.indexOf('/'));
            }
        } else {
            throw new NxqdException("No port was specified in the URI " + uri);
        }
        return parsedPort;
    }

    /**
     * The <code>extractCollectionName</code> method will extract the Collection
     * name from the uri parameter.
     *
     * @param uri a <code>String</code> value
     * @return a <code>String</code> value
     */
    public static String extractCollectionName(final String uri) {
        String collectionName;
        int lastIndex = uri.lastIndexOf('/');
        if (lastIndex > 0) {
            collectionName = uri.substring(lastIndex + 1, uri.length());
        } else {
            collectionName = uri;
        }
        if ((collectionName.equals("/")) | (collectionName.length() == 0)) {
            collectionName = NXQD_ROOT;
        }
        return collectionName;
    }

    /**
     * The <code>extractCollectionPath</code> method will extract the Collection
     * path from the uri parameter.
     *
     * @param uri a <code>String</code> value
     * @return a <code>String</code> value
     */
    public static String extractCollectionPath(final String uri) throws NxqdException {
        String port = parsePort(uri);
        int pathStart = uri.indexOf(port) + port.length() + 1;
        String collectionPath = uri.substring(pathStart, uri.length());
        if (collectionPath.length() == 0) {
            collectionPath = NXQD_ROOT;
        }
        return collectionPath;
    }

    public static String encodePath(final String path) {
        return path.replace("/", NXQD_INTERNAL_SEP);
    }

    public static String decodePath(final String path) {
        return path.replace(NXQD_INTERNAL_SEP, "/");
    }
}
