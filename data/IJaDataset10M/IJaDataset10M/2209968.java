package com.foursoft.fourever.xmlfileio.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import com.foursoft.component.Config;
import com.foursoft.component.exception.ComponentInternalException;
import com.foursoft.component.util.FileUtilities;
import com.foursoft.fourever.objectmodel.Link;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.fourever.objectmodel.ObjectModelManager;
import com.foursoft.fourever.xmlfileio.Document;
import com.foursoft.fourever.xmlfileio.Fragment;
import com.foursoft.fourever.xmlfileio.XMLFileIOConfig;
import com.foursoft.fourever.xmlfileio.XMLFileIOManager;
import com.foursoft.fourever.xmlfileio.exception.DocumentLockedException;
import com.foursoft.fourever.xmlfileio.exception.SchemaProcessingException;
import com.foursoft.fourever.xmlfileio.exception.XMLProcessingException;
import com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.XSModel;

/**
 * manager for the file io component.
 * 
 * @version $Revision: 1.44 $
 */
public class XMLFileIOManagerImpl implements XMLFileIOManager {

    static Log log = null;

    /** singleton component instance */
    private static final XMLFileIOManagerImpl instance;

    /** List of parser messages */
    private static final List<String> loadMessages = new Vector<String>();

    /** the corresponding object model manager */
    private static ObjectModelManager objectModelManager = null;

    /** configuration object for this component */
    private static XMLFileIOConfig config = null;

    /** set of all known documents */
    private static final Set<Document> documents = new HashSet<Document>();

    /** Parser Property */
    static final String PROP_ID_DOC_CLASS_NAME = "http://apache.org/xml/properties/dom/document-class-name";

    /** Use document with schema information */
    static final String PROP_VALUE_DOC_CLASS_NAME = PSVIDocumentImpl.class.getName();

    /** Parser Property */
    static final String PROP_ID_SCHEMA_VALIDATION = "http://apache.org/xml/features/validation/schema";

    /** Enable schema validation */
    static final Boolean PROP_VALUE_SCHEMA_VALIDATION = Boolean.TRUE;

    static {
        instance = new XMLFileIOManagerImpl();
    }

    /**
	 * Constructor does nothing but is private to force the use of the create
	 * instance method
	 */
    private XMLFileIOManagerImpl() {
    }

    /**
	 * Create the component manager, thus initializing the component.
	 * 
	 * @param omManager
	 *            reference to the object model manager
	 * @param givenLog
	 *            the log to use
	 * @return the singleton instance of the ObjectModelManager
	 */
    public static XMLFileIOManagerImpl createInstance(Log givenLog, ObjectModelManager omManager) {
        XMLFileIOManagerImpl.objectModelManager = omManager;
        XMLFileIOManagerImpl.log = givenLog;
        config = new XMLFileIOConfigImpl();
        return instance;
    }

    /**
	 * Sets a new config object for this manager
	 * 
	 * @param xmlconfig
	 *            the new configuration object
	 */
    public void setConfig(Config xmlconfig) {
        assert (xmlconfig != null);
        assert (xmlconfig instanceof XMLFileIOConfig);
        XMLFileIOManagerImpl.config = (XMLFileIOConfig) xmlconfig;
    }

    /**
	 * Returns the configuration object
	 * 
	 * @return the configuration object
	 */
    public Config getConfig() {
        return XMLFileIOManagerImpl.config;
    }

    /**
	 * Returns the config object
	 * 
	 * @return the config object
	 */
    public static XMLFileIOConfig getTypedConfig() {
        return config;
    }

    /**
	 * Returns the ObjectModelManager instance
	 * 
	 * @return the ObjectModelManager instance
	 */
    public static ObjectModelManager getObjectModelManager() {
        return objectModelManager;
    }

    /**
	 * Destroy the component manager.
	 */
    public static void destroy() {
        log.debug("Destroying the XMLFileIOManager");
        log = null;
        objectModelManager = null;
        config = new XMLFileIOConfigImpl();
    }

    /**
	 * @see com.foursoft.fourever.xmlfileio.XMLFileIOManager#getDocuments()
	 */
    public Iterator<Document> getDocuments() {
        return Collections.unmodifiableSet(new HashSet<Document>(documents)).iterator();
    }

    /**
	 * @see com.foursoft.fourever.xmlfileio.XMLFileIOManager#createDocument(java.io.File)
	 */
    public Document createDocument(File xsdfile) throws SchemaProcessingException {
        return createDocument(xsdfile, getTypedConfig().isLockFiles());
    }

    /**
	 * @see com.foursoft.fourever.xmlfileio.XMLFileIOManager#createDocument(java.io.File)
	 */
    public Document createDocument(File xsdfile, boolean lockFiles) throws SchemaProcessingException {
        log.debug("Creating document from given schema file: " + xsdfile);
        DocumentImpl doc = new DocumentImpl(xsdfile, null);
        addDocument(doc);
        return doc;
    }

    public Document openDocument(File xmlfile) throws IOException, SchemaProcessingException, XMLProcessingException, DocumentLockedException {
        return openDocument(xmlfile, false, config.isLockFiles());
    }

    public Document openDocument(File xmlfile, boolean createNewIds) throws IOException, SchemaProcessingException, XMLProcessingException, DocumentLockedException {
        return openDocument(xmlfile, createNewIds, config.isLockFiles());
    }

    public Document openDocument(File xmlfile, boolean createNewIds, boolean lockFiles) throws IOException, SchemaProcessingException, XMLProcessingException, DocumentLockedException {
        org.w3c.dom.Document xmlDoc;
        Element root;
        ElementPSVI rootpsvi;
        String workingDir = System.getProperty("user.dir");
        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        dFactory.setAttribute(PROP_ID_DOC_CLASS_NAME, PROP_VALUE_DOC_CLASS_NAME);
        dFactory.setAttribute(PROP_ID_SCHEMA_VALIDATION, PROP_VALUE_SCHEMA_VALIDATION);
        dFactory.setNamespaceAware(true);
        dFactory.setXIncludeAware(true);
        dFactory.setValidating(true);
        xmlfile = FileUtilities.cleanRelativePartsFromPath(xmlfile);
        assert (xmlfile.getParent() != null);
        String fileDir = xmlfile.getParent();
        if (lockFiles && xmlfile.canWrite()) {
            File lockfile = getLockfile(xmlfile);
            if (lockfile != null) {
                String message = "Document is locked. Remove lock " + lockfile.toString() + " if you're sure that nobody else is working on this file.";
                log.error(message);
                throw new DocumentLockedException(message, lockfile.toString());
            }
        }
        try {
            DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
            if (getTypedConfig().isReportParserMessages()) {
                loadMessages.clear();
                dBuilder.setErrorHandler(new LogErrorHandler(loadMessages, log));
            } else {
                dBuilder.setErrorHandler(new LogErrorHandler(log));
            }
            System.setProperty("user.dir", fileDir);
            log.debug("Reading from xml file [" + xmlfile + "] or encodes as URI [" + xmlfile.toURI().toASCIIString() + "]");
            xmlDoc = dBuilder.parse(xmlfile.toURI().toASCIIString());
        } catch (SAXException ex) {
            log.debug("Could not parse XML File. Loading aborted.", ex);
            throw new XMLProcessingException("Could not parse XML File. Loading aborted", ex);
        } catch (ParserConfigurationException ex) {
            log.debug("XML Parser misconfigured. Loading aborted.", ex);
            throw new XMLProcessingException("XML Parser misconfigured. Loading aborted", ex);
        } finally {
            System.setProperty("user.dir", workingDir);
        }
        assert (xmlDoc != null);
        root = xmlDoc.getDocumentElement();
        assert (root != null);
        if (root instanceof ElementPSVI) {
            rootpsvi = (ElementPSVI) root;
        } else {
            log.error("Parser built DOM model with unexpected PSVI elements. Loading aborted.");
            throw new XMLProcessingException("Parser built DOM model with unexpected PSVI elements. Loading aborted.");
        }
        XSModel schema = rootpsvi.getSchemaInformation();
        assert (schema != null);
        String schemafileString = root.getAttribute("xsi:noNamespaceSchemaLocation");
        File schemaFile = findSchemaFile(schemafileString, xmlfile, config.isSaveSchemaAbsolute());
        if (!schemaFile.exists()) {
            throw new SchemaProcessingException("Either no schema file found at location " + schemafileString + " or absolute files are not allowed");
        }
        assert ((schemaFile.getAbsolutePath().indexOf("../") == -1) && (schemaFile.getAbsolutePath().indexOf("..\\") == -1));
        Document doc = new DocumentImpl(schemaFile, schema);
        Node docElement = xmlDoc.getDocumentElement();
        Link rootLink = null;
        String rootName = docElement.getNodeName();
        Iterator<Link> rootLinks = doc.getObjectModel().getRootLinks();
        if (!rootLinks.hasNext()) {
            log.error("No root link created - aborting.");
            throw new ComponentInternalException("No root link created - aborting.");
        }
        while (rootLinks.hasNext()) {
            Link nextLink = rootLinks.next();
            String nextLinkName = nextLink.getBinding().getBindingName();
            if ((nextLinkName != null) && nextLinkName.equals(rootName)) {
                rootLink = nextLink;
            }
        }
        doc.readFragment(rootLink, xmlfile, createNewIds, lockFiles);
        addDocument(doc);
        return doc;
    }

    /**
	 * Returns collected parser messages from the previous call to openDocument.
	 * 
	 * @return a list of Strings or null if the manager is not configured to
	 *         collect parser messages
	 */
    public List<String> getParserMessages() {
        return loadMessages;
    }

    /**
	 * Creates a lock file for the given location
	 * 
	 * @param xmlfile
	 *            the file to be locked
	 * @return the created lock file
	 * @throws DocumentLockedException
	 *             if the document is already locked
	 * @throws IOException
	 *             if creation of the lockfile failed
	 */
    public static File createLockfile(File xmlfile) throws IOException, DocumentLockedException {
        assert (xmlfile != null);
        File lockfile = new File(xmlfile.getAbsolutePath() + ".lock");
        log.debug("Trying to obtain lock: " + lockfile);
        if (lockfile.exists()) {
            String message = "Cannot lock document. Remove " + lockfile + " if you are sure nobody else is working on this file.";
            log.error(message);
            throw new DocumentLockedException(message, lockfile.toString());
        }
        lockfile.createNewFile();
        lockfile.deleteOnExit();
        return lockfile;
    }

    /**
	 * Returns the lockfile if existant
	 * 
	 * @param xmlfile
	 *            the locked file
	 * @return the lockfile if existant, null otherwise
	 */
    public File getLockfile(File xmlfile) {
        assert (xmlfile != null);
        File lockfile = new File(xmlfile.getAbsolutePath() + ".lock");
        if (lockfile.exists()) {
            return lockfile;
        } else {
            return null;
        }
    }

    /**
	 * Removes the lock file associated to the given location
	 * 
	 * @param xmlfile
	 *            the xmlfile to be unlocked
	 * @throws IOException
	 *             if an error occurs
	 */
    public static void removeLockfile(File xmlfile) throws IOException {
        assert (xmlfile != null);
        File lockfile = new File(xmlfile.getAbsolutePath() + ".lock");
        if (!lockfile.delete()) {
            log.error("Could not remove lock file " + lockfile + ".");
            throw new IOException("Could not remove lock file " + lockfile + ".");
        }
    }

    /**
	 * @param schemafileString
	 *            the location of the schema in either relative or absolute
	 *            terms.
	 * @param ownerXMLFile
	 * @param allowAbsoluteFiles
	 * @return the schema file
	 */
    private File findSchemaFile(String schemafileString, File ownerXMLFile, boolean allowAbsoluteFiles) {
        assert schemafileString != null;
        File schemaFile = null;
        try {
            URI schemaURI = URI.create(schemafileString);
            if (schemaURI.isAbsolute()) {
                schemaFile = new File(schemaURI);
            } else {
                schemaFile = new File(schemaURI.toString().replace('/', File.separatorChar));
            }
        } catch (IllegalArgumentException ex) {
            log.info("Could not load schema file [" + schemafileString + "] as URI. Load it as File.");
        }
        if (schemaFile == null) {
            schemaFile = new File(schemafileString);
        }
        if (!schemaFile.exists()) {
            schemaFile = new File(ownerXMLFile.getParentFile(), schemafileString.replace('/', File.separatorChar));
            if (schemaFile.exists()) {
                schemaFile = FileUtilities.cleanRelativePartsFromPath(schemaFile);
            }
        }
        return schemaFile;
    }

    /**
	 * @see com.foursoft.fourever.xmlfileio.XMLFileIOManager#getDocument(com.foursoft.fourever.objectmodel.ObjectModel)
	 */
    public Document getDocument(ObjectModel om) {
        Document returnDoc = null;
        for (Document doc : documents) {
            if (doc.getObjectModel() == om) {
                returnDoc = doc;
                break;
            }
        }
        return returnDoc;
    }

    private void addDocument(Document d) {
        documents.add(d);
    }

    /**
	 * Calculates the base path for the document model passsed. The base path is
	 * the shared part of all xml file pathes.
	 * 
	 * @return The base path
	 * @throws CommandException
	 */
    public static File calculateCommonBasePath(Document doc) throws IOException {
        File basePath = null;
        Document docForModel = doc;
        if (docForModel == null || docForModel.getRootFragment() == null || docForModel.getRootFragment().getLocation() == null) {
            log.error("Cannot find common base path, no FileInfo found for given Document.");
            throw new IOException("Cannot find common base path, no FileInfo found for given Document.");
        }
        basePath = docForModel.getRootFragment().getLocation().getParentFile().getAbsoluteFile();
        for (Iterator<Fragment> fragments = docForModel.getFragments(); fragments.hasNext(); ) {
            Fragment frag = fragments.next();
            if (frag.getLocation() == null) {
                log.error("Cannot find common base path, locations not set for a new document in the tree");
                throw new IOException("Cannot find common base path, locations not set for a new document in the tree");
            }
            basePath = new File(FileUtilities.getCommonBasePath(basePath, frag.getLocation().getAbsoluteFile()));
        }
        basePath = basePath.getAbsoluteFile();
        return basePath;
    }

    /**
	 * @see com.foursoft.fourever.xmlfileio.XMLFileIOManager#closeDocument(com.foursoft.fourever.xmlfileio.Document)
	 */
    public boolean closeDocument(Document d) {
        assert (d != null);
        assert (d instanceof DocumentImpl);
        ((DocumentImpl) d).close();
        boolean success = documents.remove(d) && objectModelManager.removeObjectModel(d.getObjectModel());
        return success;
    }
}
