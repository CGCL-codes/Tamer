package org.apache.myfaces.trinidadinternal.share.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * MultipartFormItem - Represent an item from a form file-post.
 * An item can be either a parameter or a file.  If it is a parameter,
 * use getName() and getValue() to get the name and value of the
 * parameter.  If it is a file, then use getFilename and writeFile
 * to find the filename and then write the contents to an OutputStream.
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/share/util/MultipartFormItem.java#0 $) $Date: 10-nov-2005.18:59:24 $
 */
public interface MultipartFormItem {

    /**
   * Returns the value of the parameter, or null if this is a file and not
   * a parameter.
   */
    public String getValue();

    /**
   * Returns the input-field name of this item in the form.
   */
    public String getName();

    /**
   * Returns the filename of this item, or null if this is a parameter
   * and not a file
   */
    public String getFilename();

    /**
   * Returns the MIME content type of the file.
   */
    public String getContentType();

    /**
   * Writes the file to the given output stream.  Clients
   * can call this method or getInputStream(), but not both.
   * <p>
   * @see #getInputStream
   * @param stream  the output stream to write to.
   * @return     The total number of bytes written
   * @exception java.io.EOFException if the length of this item
   *   exceeds the maximum length set on the MultipartFormHandler
   * @see org.apache.myfaces.trinidadinternal.share.util.MultipartFormHandler#setMaximumAllowedBytes
   */
    public long writeFile(OutputStream stream) throws IOException;

    /**
   * Returns an InputStream that can be used to read the file.  Clients
   * may call this method or writeFile(), but not both.
   * @see #writeFile
   */
    public InputStream getInputStream() throws IOException;
}
