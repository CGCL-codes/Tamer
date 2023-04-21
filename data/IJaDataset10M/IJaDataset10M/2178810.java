package com.jpeterson.littles3.bo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class FileS3Object extends S3Object {

    /**
	 * If incompatible serialization changes are made, mostly deleting methods,
	 * this must be changed.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Create a new <code>S3Object</code> that uses a local file to store the
	 * object data.
	 * 
	 * @param bucket
	 *            The object bucket.
	 * @param key
	 *            The key bucket.
	 * @param storageUrl
	 *            The URL used to store the object data. This must be a URL with
	 *            the <code>file</code> protocol.
	 * @throws IllegalArgumentException
	 *             Thrown if the <code>storageUrl</code> is not a
	 *             <code>file</code> protocol.
	 */
    public FileS3Object(String bucket, String key, URL storageUrl) {
        super();
        setBucket(bucket);
        setKey(key);
        setStorageUrl(storageUrl);
    }

    /**
	 * Overrides the default implementation to ensure that a "file" URL is used.
	 * 
	 * @param storageUrl
	 *            The URL identifying the location of the object data.
	 * @throws IllegalArgumentException
	 *             Thrown if the <code>storageUrl</code> is not a
	 *             <code>file</code> protocol.
	 * @Override
	 */
    public void setStorageUrl(URL storageUrl) {
        if (!(storageUrl.getProtocol().equalsIgnoreCase("file"))) {
            throw new IllegalArgumentException("FileS3Object only supports file storage URLs");
        }
        super.setStorageUrl(storageUrl);
    }

    /**
	 * Overrides the default implementation to provide the length provided by
	 * the file system.
	 * 
	 * @return The length of the object data in bytes.
	 * @Override
	 */
    public long getContentLength() {
        File storageFile;
        try {
            storageFile = new File(getStorageUrl().toURI());
            return storageFile.length();
        } catch (URISyntaxException e) {
            logger.error("Can not convert storage URL to a URI", e);
            return 0;
        }
    }

    /**
	 * Override the default implementation to provide the last modified date
	 * provided by the file system.
	 * 
	 * @return The last modified data provided by the file system.
	 * @Override
	 */
    public long getLastModified() {
        File storageFile;
        try {
            storageFile = new File(getStorageUrl().toURI());
            return storageFile.lastModified();
        } catch (URISyntaxException e) {
            logger.error("Can not convert storage URL to a URI", e);
            return 0;
        }
    }

    @Override
    public boolean deleteData() {
        File storageLocation;
        File storageFile;
        boolean deletedFile;
        try {
            storageFile = new File(getStorageUrl().toURI());
        } catch (URISyntaxException e) {
            logger.error("Can not convert storage URL to a URI", e);
            return false;
        }
        storageLocation = storageFile.getParentFile();
        deletedFile = storageFile.delete();
        if (!deletedFile) {
            logger.error("Can not delete local file: " + storageFile.getAbsolutePath());
        }
        storageLocation.delete();
        return deletedFile;
    }

    @Override
    public InputStream getInputStream() {
        File storageFile;
        InputStream in;
        try {
            storageFile = new File(getStorageUrl().toURI());
        } catch (URISyntaxException e) {
            logger.error("Can not convert storage URL to a URI", e);
            return null;
        }
        try {
            in = new FileInputStream(storageFile);
        } catch (FileNotFoundException e) {
            logger.warn("Unable to open inputStream to file: " + storageFile.getAbsolutePath(), e);
            return null;
        }
        return in;
    }

    @Override
    public OutputStream getOutputStream() {
        File storageLocation;
        File storageFile;
        OutputStream out;
        try {
            storageFile = new File(getStorageUrl().toURI());
        } catch (URISyntaxException e) {
            logger.error("Can not convert storage URL to a URI", e);
            return null;
        }
        storageLocation = storageFile.getParentFile();
        storageLocation.mkdirs();
        try {
            storageFile.createNewFile();
            out = new FileOutputStream(storageFile);
        } catch (IOException e) {
            logger.warn("Error creating file for storing the object data: " + storageFile.getAbsolutePath(), e);
            return null;
        }
        return out;
    }
}
