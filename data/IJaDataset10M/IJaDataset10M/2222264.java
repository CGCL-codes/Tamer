package jreceiver.j2me.common.rec.source;

/**
 * An interface to be implemented by all sources which exist
 * as filesystem objects.
 * <p>
 * Corresponds to Mfiles table in the JReceiver database.
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:08 $
 */
public interface Mfile extends Source {

    public static final String HKEY_FOLDER_ID = "FOLDER_ID";

    public static final String HKEY_FILE_SIZE = "FILE_SIZE";

    public static final String HKEY_LAST_MODIFIED = "LAST_MODIFIED";

    /**
     * Obtain the folder id
     */
    public int getFolderId();

    /**
     * Assign the folder_id
     */
    public void setFolderId(int folder_id);

    /**
     * Obtain the file size
     */
    public long getFileSize();

    /**
     * Assign the file size
     */
    public void setFileSize(long file_size);

    /**
     * Obtain the unix epoc timestamp of the last time the file was modified
     * <p>
     * Units are in milliseconds.
     */
    public long getLastModified();

    /**
     * Assign the unix epoc timestamp of the last time the file was modified
     * <p>
     * Units are in milliseconds.
     */
    public void setLastModified(long last_modified);

    /**
     * Assign the timestamp of the last time the file was modified
     * <p>
     * Provide a date in ISO-8601 format (yyyy-MM-dd'T'HH:mm:ss)
     * <p>
     * Note that we have to use a different name than 'setLastModified'
     * as Struts/BeanUtils gets confused and thinks that getLastModified
     * should return a string.
     */
    public void setLastModifiedISO(String iso_date);
}
