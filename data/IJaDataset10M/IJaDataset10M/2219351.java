package fi.hip.gb.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import fi.hip.gb.utils.FileUtils;

/**
 * Container for transfering files. Includes metadata about the file and transfers
 * the actual result file using Java Activation Framework.
 * <p>
 * When SOAP engine deserialises the object with setters, the file is
 * written into the owner's working directory. 
 * 
 * @author Juho Karppinen
 */
public class JobAttachment implements Cloneable {

    /** URL where the file exists, this is not transfered through network, only filename is */
    private URL fileURL;

    /** array of job IDs of the owner */
    private Long[] ownerID;

    /** DataHandler file */
    private DataHandler file;

    /** classname of the object */
    private String type;

    /** size of the file in bytes, -1 if not available */
    private long size = -1;

    /** size of the file in disk in bytes */
    private long payloadSize;

    private static Log log = LogFactory.getLog(JobAttachment.class);

    /**
     * Empty constructor needed for the serialization
     */
    public JobAttachment() {
    }

    /**
     * Creates an new attachment file.
     * 
     * @param fileURL URL for the file
     * @param type class of the object
     * @param ownerID IDs of the owner
     * @throws IOException if file exists but cannot be read
     */
    public JobAttachment(URL fileURL, Class type, Long[] ownerID) throws IOException {
        this.fileURL = fileURL;
        this.type = type.getName();
        this.ownerID = ownerID;
        this.refreshPayload();
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            JobAttachment clone = new JobAttachment(this.fileURL, getClass(), this.ownerID);
            clone.setType(this.type);
            return clone;
        } catch (IOException e) {
            log.error("Cloning failed for attachment", e);
            return null;
        }
    }

    /**
     * Reads the content of the file stored on the disk
     * 
     * @return content of the file, or null if source file not found
     */
    public String readContent() {
        byte[] bytes = readBytes();
        return (bytes != null) ? new String(bytes) : null;
    }

    /**
     * Reads bytes of the file stored on the disk
     * 
     * @return bytes of the file, or null if source file not found
     */
    public byte[] readBytes() {
        try {
            return FileUtils.readBytes(FileUtils.convertFromJarURL(fileURL));
        } catch (IOException ioe) {
            log.error("File cannot be read from " + fileURL.toString() + " : " + ioe.getMessage());
            return null;
        }
    }

    /**
     * Writes content of the file to the disk.
     * 
     * @param content content of the file
     * @throws NullPointerException if the target file was null
     * @throws IOException if operation failed
     */
    public void writeContent(String content) throws NullPointerException, IOException {
        if (this.fileURL != null) {
            FileUtils.writeFile(FileUtils.convertFromJarURL(this.fileURL), content);
            refreshPayload();
        } else {
            throw new NullPointerException("Target file was null, file not written into disk");
        }
    }

    /**
     * Gets the name of the file.
     * 
     * @return  name of the file
     */
    public String fileName() {
        return FileUtils.getFilename(this.fileURL);
    }

    /**
     * Gets URL for the file. Can be JAR url (jar:url!/).
     * @return URL for the file in string format
     */
    public String getFileURL() {
        return this.fileURL.toString();
    }

    /**
     * Gets URL for the file. Can be JAR url (jar:url!/).
     * @return URL for the file
     */
    public URL fileURL() {
        return this.fileURL;
    }

    /**
     * Sets URL for the file in string format.
     * 
     * @param fileURL URL of the file
     */
    public void setFileURL(String fileURL) {
        try {
            this.fileURL = new URL(fileURL);
        } catch (MalformedURLException e) {
            log.error("Could not parse file URL from " + fileURL);
        }
    }

    /**
     * Sets java Activation handler for the file. Change the <code>DataHandler</code>
     * type into String when running jbossws-tools. 
     * 
     * @param file file as a DataHandler object
     */
    public void setFile(DataHandler file) {
        this.file = file;
        if (this.file != null && "application/octet-stream".equals(this.file.getContentType())) {
            String target = Config.getWorkingDir(this.ownerID[0]);
            FileUtils.createDir(target);
            target += "/" + fileName();
            log.debug("saving SOAP attachment to " + target);
            try {
                FileUtils.copyStream(this.file.getInputStream(), new FileOutputStream(target));
                target = "file:" + target;
                if (this.fileURL.getProtocol().equals("jar")) {
                    target = "jar:" + target + "!/";
                }
                this.fileURL = new URL(target);
            } catch (IOException e) {
                log.error("Failed to save the file into disk", e);
            }
        }
    }

    /**
     * Gets Java Activation handler for the file. Change the <code>DataHandler</code>
     * type into String when running jbossws-tools.
     * 
     * @return file as a DataHandler object
     */
    public DataHandler getFile() {
        return this.file;
    }

    /**
     * Checks if the payload data exists and can be read.
     * @return true if file is readable
     */
    public boolean exists() {
        return (this.size >= 0);
    }

    /**
     * Removes all the payload data, actual file contents, from
     * this object but not from the disk. This is done by removing the <code>DataHandler</code>
     * file.
     */
    public void clearPayload() {
        this.file = null;
        this.payloadSize = 0;
    }

    /**
     * Refresh the payload data from the current URL. If the file doesn't exists, nothing is done.
     * @throws IOException if file could not be loaded
     */
    public void refreshPayload() throws IOException {
        URL location = FileUtils.convertFromJarURL(this.fileURL);
        File file = new File(location.getFile());
        if (file.isFile() && file.exists() && file.canRead()) {
            String fname = file.getAbsoluteFile().getCanonicalPath();
            this.file = new DataHandler(new FileDataSource(fname));
            this.payloadSize = this.size = file.length();
        } else {
            this.size = -1;
        }
    }

    /**
     * Gets the classname of the serialized object.
     * @return classname of the object
     */
    public String getType() {
        return this.type;
    }

    /**
     * Sets the classname of the serialized object.
     * @param type classname of the object 
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the owner jobID for the attachment. ID is used
     * to discover the directory for files.
     * 
     * @param id array of IDs for the attachment
     */
    public void setOwnerID(Long[] id) {
        this.ownerID = id;
    }

    /**
     * Gets the jobID of the attachment. ID is used
     * to discover the directory for files.
     * 
     * @return array of IDs for the attachment
     */
    public Long[] getOwnerID() {
        return this.ownerID;
    }

    /**
     * Gets the actual size of the file.
     * Use {@link JobAttachment#getSize()} to get the
     * original size of this file. If file is
     * transfered without payload, it only shows
     * in the output of this method.
     * 
     * @return real file size in bytes
     */
    public long payloadSize() {
        return this.payloadSize;
    }

    /**
     * Gets the size of the file. This isn't the
     * real size on disk which can be obtained from
     * {@link JobAttachment#payloadSize()} method.
     * If file is transfered without payload, it doesn't
     * change the output of this method.
     * 
     * @return file size in bytes
     */
    public long getSize() {
        return this.size;
    }

    /**
     * Sets the size of the file. Only used for serialization.
     * 
     * @param size size in bytes
     */
    public void setSize(long size) {
        this.size = size;
    }
}
