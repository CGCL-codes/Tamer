package eu.jbart.bunit;

import java.io.File;

/**
 * The backup file represents a file in a backup. It is very abstract
 * because it be on a local filesystem, a file on a FTP server or
 * on other remote place.
 * 
 * @author Bart Frackiewicz <mail@jbart.eu>
 *
 */
public interface BackupFile {

    /**
     * 
     * @return the length in bytes
     */
    public long length();

    /**
     * 
     * @return the unix timestamp of last modified time
     */
    public long lastModified();

    /**
     * This method should return a temporary copy.
     * 
     * @return a local copy of the backup file to work with it
     */
    public File getWorkingFile();
}
