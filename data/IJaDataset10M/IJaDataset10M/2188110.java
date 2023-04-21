package com.bonkey.filesystem.cloudfiles;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TimeZone;
import nu.xom.Attribute;
import nu.xom.Element;
import org.eclipse.core.runtime.IProgressMonitor;
import com.bonkey.config.BonkeyConstants;
import com.bonkey.filesystem.browsable.BrowsableFile;
import com.bonkey.filesystem.browsable.BrowsableFolder;
import com.bonkey.filesystem.browsable.BrowsableItem;
import com.bonkey.filesystem.remote.RemoteWritableFile;
import com.bonkey.filesystem.remote.RemoteWritableFileSystem;
import com.bonkey.filesystem.remote.RemoteWritableFolder;
import com.rackspacecloud.client.cloudfiles.FilesClient;
import com.rackspacecloud.client.cloudfiles.FilesConstants;
import com.rackspacecloud.client.cloudfiles.FilesObject;

public class CloudfilesFileSystem extends RemoteWritableFileSystem {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5676883678674345324L;

    private static final SimpleDateFormat dateFormatter;

    static {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'H:m:s.S");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private static final String A_CONTAINER = "container";

    /**
	 * Name of the cloudfiles container being used.
	 */
    protected String container;

    /**
	 * Client used to connect to CloudFiles service.
	 */
    protected transient FilesClient client;

    public CloudfilesFileSystem(Element e) {
        super(e);
        container = e.getAttributeValue(A_CONTAINER);
    }

    /**
	 * Create a CloudFiles filesystem with an encryption key
	 * @param name name of the filesystem
	 * @param uri URI of root folder
	 * @param key encryption key
	 * @param saveKey whether to save the key between executions of the program
	 */
    public CloudfilesFileSystem(String name, String uri, String key, boolean saveKey) {
        super(name, uri, key, saveKey);
    }

    public CloudfilesFileSystem(String name, String uri, String encryptionKey, boolean saveEncryptionKey, String container) {
        this(name, uri, encryptionKey, saveEncryptionKey);
        this.container = container;
    }

    protected String getContainer() {
        return container;
    }

    protected RemoteWritableFile createInternalFile(String name, String fileSystemName, String uri, Date lastModified, long size) {
        return new CloudfilesFile(name, fileSystemName, uri, lastModified, size);
    }

    protected RemoteWritableFolder createInternalFolder(String subfolderName, String fileSystemName, String uri) {
        return new CloudfilesFolder(subfolderName, fileSystemName, uri);
    }

    protected void createRemoteFolder(String relativePath) throws IOException {
    }

    protected void deleteRemoteFile(BrowsableItem item) throws IOException {
        if (item instanceof BrowsableFile) {
            getFilesClient().deleteObject(getContainer(), item.getRelativeURI());
        }
    }

    protected void disconnectRemoteHost() throws IOException {
    }

    protected int getDefaultPort() {
        return -1;
    }

    protected Collection<BrowsableItem> listRemoteFolder(String relativeURI) throws IOException {
        HashSet<BrowsableItem> contents = new HashSet<BrowsableItem>();
        for (FilesObject file : getFilesClient().listObjects(getContainer(), relativeURI)) {
            contents.add(new CloudfilesFile(file.getName().substring(relativeURI.length()), getName(), file.getName(), parseLastModifiedString(file.getLastModified()), file.getSize()));
        }
        return contents;
    }

    public InputStream getInputStream(String relativeURI) throws IOException {
        InputStream input = getFilesClient().getObjectAsStream(getContainer(), relativeURI);
        input = decryptStreamIfNeeded(input);
        return input;
    }

    public void putFileContents(InputStream input, long size, String relativeURI, IProgressMonitor monitor, float incrementSize) throws IOException {
        String extension = "";
        int extensionStart = relativeURI.lastIndexOf('.');
        if (extensionStart != -1) {
            extension = relativeURI.substring(extensionStart);
        }
        String mimeType = FilesConstants.getMimetype(extension);
        input = encryptStreamIfNeeded(input);
        getFilesClient().storeStreamedObject(getContainer(), input, mimeType, relativeURI, new HashMap<String, String>());
        input.close();
        BrowsableItem file = getFile(relativeURI);
        if (file instanceof CloudfilesFile) {
            ((CloudfilesFile) file).setSize(size);
            ((CloudfilesFile) file).setLastModified(new Date());
        } else {
            CloudfilesFolder rootFolder = (CloudfilesFolder) getRoot();
            file = rootFolder.addItem(relativeURI, new Date(), size);
        }
    }

    public String getImageName() {
        if (isEncrypted()) {
            return BonkeyConstants.ICON_TARGET_ENCRYPTED;
        }
        return BonkeyConstants.ICON_TARGET;
    }

    protected BrowsableFolder getRoot() throws IOException {
        if (root == null) {
            CloudfilesFolder newRoot = new CloudfilesFolder(getName(), getName(), getURI());
            for (FilesObject file : getFilesClient().listObjects(getContainer())) {
                newRoot.addItem(file.getName(), parseLastModifiedString(file.getLastModified()), file.getSize());
            }
            root = newRoot;
        }
        return root;
    }

    private static Date parseLastModifiedString(String lastModified) throws IOException {
        try {
            Date date = dateFormatter.parse(lastModified.substring(0, lastModified.length() - 3));
            return date;
        } catch (ParseException e) {
            throw new IOException("Error parsing last modified: " + e.getMessage());
        }
    }

    protected FilesClient getFilesClient() throws IOException {
        if (client == null) {
            client = new FilesClient(getAuthentication().getUsername(), getAuthentication().getPassword());
            if (!client.login()) {
                throw new IOException("Login failed for username " + getAuthentication().getUsername());
            }
        } else if (!client.isLoggedin()) {
            if (!client.login()) {
                throw new IOException("Login failed for username " + getAuthentication().getUsername());
            }
        }
        return client;
    }

    protected void setRemoteHostDetails() {
    }

    public Element toXML() {
        Element e = super.toXML();
        e.addAttribute(new Attribute(A_CONTAINER, getContainer()));
        return e;
    }

    public Element toXMLSecure() {
        Element e = super.toXMLSecure();
        e.addAttribute(new Attribute(A_CONTAINER, getContainer()));
        return e;
    }

    public static boolean createContainer(String username, String apiKey, String containerName) throws IOException {
        FilesClient tempClient = new FilesClient(username, apiKey);
        if (!tempClient.login()) {
            return false;
        }
        if (!tempClient.containerExists(containerName)) {
            tempClient.createContainer(containerName);
            return true;
        } else {
            return false;
        }
    }
}
