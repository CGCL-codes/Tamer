package org.jmonks.dms.versioncontrol.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author Suresh Pragada
 * 
 * This represents gives the interface to access particular version of a file entry in repository. 
 * It would allow to access the meta information of that version and contents of that version.
 * 
 */
public interface FileVersion {

    /**
     *  Returns the number of version this object holds.
     *  @return Retuns the version number of this object.
     */
    public int getVersionNumber();

    /**
     *  Returns the created date of this version.
     *  @return Retuns the created date as java.util.Date.
     */
    public Date getVersionDate();

    /**
     *  Returns the author of version of this object.
     *  @return Retuns the author of this version.
     */
    public String getVersionAuthor();

    /**
     *  Returns the description of this version.
     *  @return Retuns the description of this version.
     */
    public String getVersionDescription();

    /**
     * This method will allow to read the contents of that particular version of that file entry.
     * @return  InputStream of contents of that version.
     */
    public InputStream getInputStream();

    /**
     * Returns the length of the file of this version. This will be used in downloads to specifiy
     * the length of the file being downloaded.
     * 
     * @return Returns the number of bytes to be downloaded.
     */
    public int getLength();
}
