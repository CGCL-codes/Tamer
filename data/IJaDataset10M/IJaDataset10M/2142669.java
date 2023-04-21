package org.melati.servlet;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Holds information parsed from a multipart/form-data file upload.
 *
 * Based on RFC 1867 which describes the format 
 * for uploading files in multipart/form-data.
 * Tested on IE 5.0, HotJava 3.0, Netscape Navigator 4.x
 *
 * @see <a href="http://www.ietf.org/rfc/rfc1867.txt">rfc1867</a>
 * @author Vasily Pozhidaev <vasilyp@paneris.org>
 */
public class MultipartFormField {

    private String contentDisposition = "";

    private String fieldName = "";

    private String filePath = "";

    private String contentType = "";

    private FormDataAdaptor adaptor = null;

    /** Number of bytes in a kilobyte. */
    public static final long KILOBYTE = 1024;

    /** Number of bytes in a megabyte. */
    public static final long MEGABYTE = 1024 * 1024;

    /** Number of bytes in a gigabyte. */
    public static final long GIGABYTE = 1024 * 1024 * 1024;

    /** The url to this file */
    String url = null;

    /** Constructor. */
    public MultipartFormField() {
    }

    /** 
   * Constructor.
   *
   * @param contentDisposition whether attachment or inline 
   * @param fieldName          the name of this field
   * @param filePath           the path to the file in the file system
   * @param contentType        the MIME content type
   * @param adaptor            the Melati data adaptor
   */
    public MultipartFormField(String contentDisposition, String fieldName, String filePath, String contentType, FormDataAdaptor adaptor) {
        this.contentDisposition = contentDisposition;
        this.fieldName = fieldName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.adaptor = adaptor;
    }

    /** 
   * Set the <code>ContentType</code>. 
   *
   * @param contentType  the Content Type to set it to
   */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /** 
   * Get the <code>ContentType</code>. 
   *
   * @return the <code>ContentType</code>
   */
    public String getContentType() {
        return contentType;
    }

    /** Set the <code>ContentDisposition</code>. 
   *
   * @param contentDisposition  the Content Disposition to set it to
   */
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    /** 
   * Get the <code>ContentDisposition</code>. 
   *
   * @return the <code>ContentDisposition</code>
   */
    public String getContentDisposition() {
        return contentDisposition;
    }

    /** 
   * Set the <code>FieldName</code>. 
   *
   * @param fieldName  the name to set {@link #fieldName} to 
   */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /** 
   * Get the <code>FieldName</code>. 
   *
   * @return the <code>FieldName</code>
   */
    public String getFieldName() {
        return fieldName;
    }

    /** 
   * Set the <code>UploadedFilePath</code>.
   *
   * @param filePath  the name to set {@link #filePath} to 
   */
    public void setUploadedFilePath(String filePath) {
        this.filePath = filePath;
    }

    /** 
   * Get the <code>UploadedFilePath</code>. 
   * @return the <code>UploadedFilePath</code>
   */
    public String getUploadedFilePath() {
        return filePath;
    }

    /** 
   * Get the <code>UploadedFileName</code>.
   *
   * @return the <code>UploadedFileName</code> 
   */
    public String getUploadedFileName() {
        try {
            return filePath.substring(((filePath.lastIndexOf("\\") != -1) ? filePath.lastIndexOf("\\") : filePath.lastIndexOf("/")) + 1);
        } catch (Exception e) {
            return "";
        }
    }

    /**
   * Work with an uploaded file/stored value.
   * 
   * We can store uploaded files or values in different ways depending
   * on which adaptor we use.
   *
   * @param adaptor  a {@link FormDataAdaptor} to set {@link #adaptor} to
   */
    public void setFormDataAdaptor(FormDataAdaptor adaptor) {
        this.adaptor = adaptor;
    }

    /**
   * @return the saved data as a byte array
   */
    public byte[] getData() {
        return (adaptor != null) ? adaptor.getData() : new byte[0];
    }

    /**
   * @return the saved data as a string
   */
    public String getDataString() {
        return new String(getData());
    }

    /**
   * Get the data using the specified encoding.
   *
   * @param enc an encoding which may be null or empty
   * @return the saved data as a string using the encoding supplied
   */
    public String getDataString(String enc) {
        if (enc == null || enc.equals("")) return getDataString();
        try {
            return new String(getData(), enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return getDataString();
        }
    }

    /**
   * Return the length of the data.
   * @return the length of the data
   */
    public long getDataSize() {
        return (adaptor != null) ? adaptor.getSize() : 0;
    }

    /**
   * Return the data saved as a file (or null if it is not saved).
   * @return The data saved as a file (or null if it is not saved)
   */
    public File getDataFile() {
        return (adaptor != null) ? adaptor.getFile() : null;
    }

    /**
   * Return a URL to the saved data (or null if no such URL exists).
   * @return a URL to the saved data (or null if no such URL exists)
   */
    public String getDataURL() {
        return (adaptor != null) ? adaptor.getURL() : null;
    }

    /**
   * The size of the file as a formatted string.
   * 
   * @return the size <code>String</code>.
   */
    public String getPrettyDataSize() {
        long size = 0;
        try {
            size = getDataSize();
        } catch (Exception e) {
            return "Unknown";
        }
        String sizeString = null;
        if ((size / KILOBYTE) >= 1) {
            if ((size / MEGABYTE) >= 1) {
                if ((size / GIGABYTE) >= 1) sizeString = (size / GIGABYTE) + " Gb"; else sizeString = (size / MEGABYTE) + " Mb";
            } else {
                sizeString = (size / KILOBYTE) + " Kb";
            }
        } else {
            sizeString = size + " bytes";
        }
        return sizeString;
    }
}
