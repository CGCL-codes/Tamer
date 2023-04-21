package com.prolix.editor.resourcemanager.zip;

import java.io.File;
import org.eclipse.core.runtime.Platform;
import org.jdom.Document;
import org.jdom.Element;
import com.prolix.editor.resourcemanager.exceptions.GLMRessourceManagerException;

/**
 * Manages the work of the Zip and FileManager
 * 
 * @author Susanne Neumann, Stefan Zander, Philipp Prenner
 */
public class ContentManager {

    /**
	 * Default Names
	 */
    public static final String IMS_MANIFEST_FILENAME = "imsmanifest.xml";

    public static final String PROLIX_DIAGRAM_DATA_FILENAME = "ProlixDiagramData.xml";

    public static final String INTERNAL_RESOURCES_DIRECTORY_NAME = "internal_resources";

    /**
	 * 
	 * @return the default size for the Read/Write Methods
	 */
    protected static byte[] getDefaultBuffer() {
        return new byte[1024];
    }

    private FileManager fileManager;

    private ZipManager zipManager;

    private RessourceManager manager;

    protected ContentManager(String zipFile, boolean open, RessourceManager manager) throws GLMRessourceManagerException {
        this.manager = manager;
        zipManager = new ZipManager(zipFile, manager);
        if (open) zipManager.load();
        fileManager = new FileManager(zipManager.getTempDirectory());
    }

    /**
	 * gets the root element of the prolix diagram data
	 * 
	 * @return xml root element
	 */
    protected Element getProlixGLMData() throws GLMRessourceManagerException {
        Document doc = fileManager.loadXML(PROLIX_DIAGRAM_DATA_FILENAME);
        return doc.getRootElement();
    }

    public File getFile(String id) throws GLMRessourceManagerException {
        return fileManager.getFile(id);
    }

    public File getInternalFile(String id) throws GLMRessourceManagerException {
        return fileManager.getInternalFile(id);
    }

    public Element getXML(String id) throws GLMRessourceManagerException {
        return fileManager.loadXML(id).getRootElement();
    }

    public Element getInternalXML(String id) throws GLMRessourceManagerException {
        return fileManager.loadInternalXML(id).getRootElement();
    }

    public String getFileContent(String id) throws GLMRessourceManagerException {
        return fileManager.getFileContent(id);
    }

    public String getInternalFileContent(String id) throws GLMRessourceManagerException {
        return fileManager.getInternalFileContent(id);
    }

    public void saveContentFile(String id, String content) throws GLMRessourceManagerException {
        fileManager.saveContentFile(id, content);
    }

    public void saveInternalContentFile(String id, String content) throws GLMRessourceManagerException {
        fileManager.saveInternalContentFile(id, content);
    }

    public void saveXML(String id, Element element) throws GLMRessourceManagerException {
        Document doc = new Document(element);
        fileManager.saveXML(id, doc);
    }

    public void saveXML(String id, Document doc) throws GLMRessourceManagerException {
        fileManager.saveXML(id, doc);
    }

    public void saveInternalXML(String id, Element element) throws GLMRessourceManagerException {
        Document doc = new Document(element);
        fileManager.saveInternalXML(id, doc);
    }

    public void saveInternalXML(String id, Document document) throws GLMRessourceManagerException {
        fileManager.saveInternalXML(id, document);
    }

    public void deleteFile(String id) throws GLMRessourceManagerException {
        fileManager.deleteFile(id);
    }

    public void deleteInternalFile(String id) throws GLMRessourceManagerException {
        fileManager.deleteInternalFile(id);
    }

    public void save(String newZip, boolean export) throws GLMRessourceManagerException {
        if (newZip == null) {
            zipManager.save(export);
            return;
        }
        ZipManager tempZip = new ZipManager(newZip, zipManager.getTempDirectory(), manager);
        tempZip.save(export);
        if (export) try {
            fileManager.deleteInternalDirectory();
        } catch (Exception e) {
        }
    }

    public void dispose() throws GLMRessourceManagerException {
        fileManager.dispose();
        fileManager = null;
        zipManager = null;
    }

    protected String getTempDirectory() {
        return zipManager.getTempDirectory();
    }

    protected String getZipFileName() {
        return zipManager.getZipFileName();
    }

    protected void changeZipManager(String newZipFile) throws GLMRessourceManagerException {
        zipManager = new ZipManager(newZipFile, getTempDirectory(), manager);
    }

    protected void copyInternalFile(String inputPath, String filename) throws GLMRessourceManagerException {
        fileManager.copyInternalFile(inputPath, filename);
    }

    protected void copyFile(String inputPath, String filename) throws GLMRessourceManagerException {
        fileManager.copyFile(inputPath, filename);
    }

    protected static String getTempFolder() {
        return Platform.getLocation().toString();
    }

    protected static String getPostFix(File file) {
        if (file == null) return "";
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        if (pos < 0) return "";
        return name.substring(pos, name.length());
    }
}
