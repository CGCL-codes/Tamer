package org.jmonks.dms.versioncontrol.api;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Suresh Pragada
 */
public interface DirectoryEntry extends RepositoryEntry {

    /**
     * Gets all repository entries available under this entry.
     *
     * @return  Returns the list of RepositoryEntry objects. If not exists, it returns the list of size 0.
     */
    public List getAllEntries();

    /**
     *  Let users create a directory entry in the system.
     *
     * @param   directoryName   Name with which the directory entry needs to be created.
     *
     * @return  Returns newly created Directory Entry.
     */
    public DirectoryEntry createDirectoryEntry(String directoryName);

    /**
     *  Let users create a file entry in the system.
     *
     * @param   fileName    Entry name with which the file entry needs to be created.
     * @param   authorName      Authro name to be stored with meta info of the first version of the file.
     * @param   description     Description to be stored with meta info of the first version of the file.
     * @param   versionDate     Version Date to be stored with meta info of the first version of the file.     
     * @param   inputStream     InputStream of the information to be checked-in as the first version of the file.
     *
     * @return  Returns newly created File Entry.
     */
    public FileEntry createFileEntry(String fileName, String authorName, String description, Date versionDate, InputStream inputStream);

    /**
     * To find out the is the entry already exist in repository.
     * Though operating system doesnt allow us to create file and folder with the same name, since we
     * are creating file entries along with the version name, there wont be any conflicts.
     *
     * @param   entryName   Entry name to create.
     * @param   entryType   Type of the entry to find out.
     * @return  Returns true if the entry exists, false otherwise.
     */
    public boolean isExistingEntry(String entryName, String entryType);
}
