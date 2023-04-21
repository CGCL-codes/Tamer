package de.herberlin.server.common.event;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Specialized class holding cached file data for display issues.
 * @author Hans Joachim Herbertz
 * @created 14.02.2003
 */
public class FileData implements Serializable {

    private File file = null;

    private String encoding = null;

    private String contentType = null;

    private long fileSize = -1;

    private byte[] byteData = null;

    private String contentDescription = null;

    private Logger logger = Logger.getLogger(getClass().getName());

    /**
	 * Constructor for FileData.
	 */
    public FileData() {
        super();
    }

    public String toString() {
        if (getEncoding() == null) {
            return contentType;
        } else {
            return contentType + ", " + getEncoding();
        }
    }

    /**
	 * Returns the file.
	 * @return File
	 */
    public File getFile() {
        return file;
    }

    /**
     * Returns the content description, 
     * e.g. the file name.
     */
    public String getContentDescription() {
        if (contentDescription != null) {
            return contentDescription;
        } else if (file != null) {
            return file.getAbsolutePath();
        } else {
            return "";
        }
    }

    /**
     * Gets a Stream on the File data or null if 
     * no data to display.
     */
    public InputStream getInputStream() {
        if (file != null && file.isFile()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.info(e.toString());
                return null;
            }
        } else if (byteData != null) {
            return new ByteArrayInputStream(byteData);
        } else {
            return null;
        }
    }

    private long getMaxFileSize() {
        long size = -1;
        if (file != null && file.isFile()) {
            size = (long) file.length();
        }
        size = Math.max(size, getContentLength());
        return size;
    }

    /**
     * Gets the fileData content as byte[] or
     * null if no data present
     */
    public byte[] getBytes() {
        if (byteData != null) {
            return byteData;
        } else if (file != null && file.isFile()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) getMaxFileSize());
            try {
                FileInputStream in = new FileInputStream(file);
                byte[] buffer = new byte[512];
                int count = -1;
                while ((count = in.read(buffer)) != -1) {
                    byteBuffer.put(buffer, 0, count);
                }
                in.close();
            } catch (IOException e) {
                logger.info(e.toString());
                return null;
            }
            byteData = byteBuffer.array();
            return byteData;
        } else {
            return null;
        }
    }

    /**
     * Returns the contentType or null
     * if not set.
     */
    public String getContentType() {
        return contentType;
    }

    /**
	 * Returns the fileSize.
	 * @return int
	 */
    public long getContentLength() {
        return fileSize;
    }

    /**
	 * Sets the contentType.
	 * @param contentType The contentType to set
	 */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
	 * Sets the file.
	 * @param file The file to set
	 */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Set the fileData to data. Use this 
     * method if request hanling is not file based, e.g
     * creates a directory list or throws an exception.
     * 
     * The content length is not set with this method
     */
    public void setData(byte[] data) {
        byteData = data;
    }

    /**
	 * Sets the fileSize.
	 * @param fileSize The fileSize to set
	 */
    public void setContentLength(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @param string
     */
    public void setContentDescription(String string) {
        contentDescription = string;
    }

    /**
     * @return
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param string
     */
    public void setEncoding(String string) {
        encoding = string;
    }
}
