package de.fuh.xpairtise.server.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.DataConversionException;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import de.fuh.xpairtise.common.XPLog;
import de.fuh.xpairtise.common.replication.elements.ReplicatedVCResource;

/**
 * This class implements consistency of the metadata of the member resources of
 * version controlled projects.
 */
public class VCProjectMetaData {

    private final File baseDir;

    private final File xmlFile;

    private final String projectName;

    private static final String DTD_NAME = "VCProjectMetaData.dtd";

    private static final String SUFFIX = "_MetaData.xml";

    private static final String SEPARATOR = System.getProperty("file.separator");

    private final String logPrefix;

    private Document document;

    /**
   * Creates a new <code>VCProjectMetaData</code> instance.
   * 
   * @param baseDir
   *          the base directory the data of the current session is stored in
   * @param sessionDir
   *          the directory to store the metadata for the projects of the
   *          current session in
   * @param projectName
   *          the name of the project this instance is associated with
   */
    public VCProjectMetaData(File baseDir, File sessionDir, String projectName) {
        this.baseDir = baseDir;
        this.projectName = projectName;
        logPrefix = "VCProjectMetaData (" + projectName + ") - ";
        xmlFile = new File(sessionDir.getAbsolutePath() + SEPARATOR + projectName.replaceAll(" ", "_") + SUFFIX);
        loadXMLFile();
    }

    /**
   * Adds a new element associated with the given resource.
   * 
   * @param resource
   *          the <code>ReplicatedVCResource</code> element describing the
   *          added resource.
   * @param sync
   *          indicates whether the cached data should immediately be written to
   *          disk after performing the change
   */
    public void addResource(ReplicatedVCResource resource, boolean sync) {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(logPrefix + "Adding resource: " + resource.getPath());
        }
        synchronized (document) {
            Element root = document.getRootElement();
            Element element = new Element("resource");
            root.addContent(element);
            String type = resource.getType() == ReplicatedVCResource.TYPE_FILE ? "file" : "folder";
            element.setAttribute("type", type);
            element.setAttribute("path", resource.getPath());
            if (resource.isRemoteOnly()) {
                element.setAttribute("remoteOnly", "yes");
            }
            if (resource.getRevision() != null) {
                element.setAttribute("revision", resource.getRevision());
            }
            if (resource.getChecksum() != null) {
                element.setAttribute("checksum", resource.getChecksum());
            }
            if (sync) {
                saveXMLtoFile();
            }
        }
    }

    /**
   * Removes the element associated with the given resource.
   * 
   * @param resource
   *          the <code>ReplicatedVCResource</code> describing the removed
   *          resource
   * @param sync
   *          indicates whether the cached data should immediately be written to
   *          disk after performing the change
   */
    public void removeResource(ReplicatedVCResource resource, boolean sync) {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(logPrefix + "Removing resource: " + resource.getPath());
        }
        synchronized (document) {
            String path;
            for (Object o : document.getRootElement().getChildren()) {
                if (o instanceof Element) {
                    Element e = (Element) o;
                    path = e.getAttributeValue("path");
                    if (path != null && path.equals(resource.getPath())) {
                        document.getRootElement().removeContent(e);
                        break;
                    }
                }
            }
            if (sync) {
                saveXMLtoFile();
            }
        }
    }

    /**
   * Updates the element associated with the given resource.
   * 
   * @param resource
   *          the <code>ReplicatedVCResource</code> describing the updated
   *          resource
   */
    public void updatedResource(ReplicatedVCResource resource) {
        synchronized (document) {
            removeResource(resource, false);
            addResource(resource, true);
        }
    }

    /**
   * Synchronizes the cached data with the XML file on disk.
   */
    public void sync() {
        if (XPLog.isDebugEnabled()) {
            XPLog.printDebug(logPrefix + "Writing cached document to disk.");
        }
        saveXMLtoFile();
    }

    /**
   * Returns a list of new <code>ReplicatedVCResource</code> elements
   * describing the registered resources.
   * 
   * @return a list of <code>ReplicatedVCResource</code> elements describing
   *         the registered resources
   */
    public List<ReplicatedVCResource> readResources() {
        List<ReplicatedVCResource> list = new ArrayList<ReplicatedVCResource>();
        Element root = document.getRootElement();
        ReplicatedVCResource resource = null;
        for (Object o : root.getChildren()) {
            if (o instanceof Element) {
                resource = readResource((Element) o);
                if (resource != null) {
                    list.add(resource);
                }
            }
        }
        return list;
    }

    /**
   * Removes the XML file used by this instance from the file system.
   */
    public void close() {
        xmlFile.delete();
    }

    private void createDTDFile() {
        try {
            File file = new File(baseDir.getAbsolutePath() + SEPARATOR + DTD_NAME);
            PrintWriter output = new PrintWriter(file);
            output.println("<!ELEMENT MetaData (resource*)>");
            output.println("<!ELEMENT resource (#PCDATA)>");
            output.println("<!ATTLIST resource");
            output.println("\ttype ( file|folder ) #REQUIRED");
            output.println("\tpath CDATA #REQUIRED");
            output.println("\trevision CDATA #IMPLIED");
            output.println("\tchecksum CDATA #IMPLIED");
            output.println("\tremoteOnly ( yes|no ) #REQUIRED>");
            output.close();
        } catch (IOException e) {
            XPLog.logException(Level.WARN, 0, "Failed to create metadata DTD file", e);
        }
    }

    private void loadXMLFile() {
        try {
            SAXBuilder sxb = new SAXBuilder();
            document = sxb.build(xmlFile);
        } catch (JDOMException e) {
            XPLog.logException(Level.WARN, 0, "Failed to parse XML file.", e);
        } catch (IOException e) {
            createEmptyDocument();
            saveXMLtoFile();
            createDTDFile();
        }
    }

    private void createEmptyDocument() {
        DocType dt = new DocType("root", "../" + DTD_NAME);
        Element root = new Element("MetaData");
        Comment comment = new Comment("Metadata of project \"" + projectName + "\"");
        root.addContent(comment);
        document = new Document(root, dt);
    }

    private void saveXMLtoFile() {
        try {
            FileOutputStream out = new FileOutputStream(xmlFile);
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(document, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            XPLog.logException(Level.WARN, 0, "Failed to write XML file.", e);
        }
    }

    private ReplicatedVCResource readResource(Element element) {
        if (!element.getName().equals("resource")) {
            return null;
        }
        int type = element.getAttributeValue("type").equals("file") ? ReplicatedVCResource.TYPE_FILE : ReplicatedVCResource.TYPE_FOLDER;
        String path = element.getAttributeValue("path");
        String revision = element.getAttributeValue("revision");
        String checksum = element.getAttributeValue("checksum");
        boolean remoteOnly = false;
        Attribute remoteAttribute = element.getAttribute("remoteOnly");
        if (remoteAttribute != null) {
            try {
                remoteOnly = remoteAttribute.getBooleanValue();
            } catch (DataConversionException d) {
                if (XPLog.isDebugEnabled()) {
                    XPLog.printDebug("Failed to parse boolean value: " + d.getMessage());
                    return null;
                }
            }
        }
        return new ReplicatedVCResource(path, type, revision, checksum, remoteOnly);
    }
}
