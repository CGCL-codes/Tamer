package org.mockftpserver.fake.filesystem;

/**
 * File system entry representing a directory
 *
 * @author Chris Mair
 * @version $Revision: 204 $ - $Date: 2008-12-12 19:38:37 -0500 (Fri, 12 Dec 2008) $
 */
public class DirectoryEntry extends AbstractFileSystemEntry {

    /**
     * Construct a new instance without setting its path
     */
    public DirectoryEntry() {
    }

    /**
     * Construct a new instance with the specified value for its path
     *
     * @param path - the value for path
     */
    public DirectoryEntry(String path) {
        super(path);
    }

    /**
     * Return true to indicate that this entry represents a directory
     *
     * @return true
     */
    public boolean isDirectory() {
        return true;
    }

    /**
     * Return the size of this directory. This method returns zero.
     *
     * @return the file size in bytes
     */
    public long getSize() {
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Directory['" + getPath() + "' lastModified=" + getLastModified() + "  owner=" + getOwner() + "  group=" + getGroup() + "  permissions=" + getPermissions() + "]";
    }

    /**
     * Return a new FileSystemEntry that is a clone of this object, except having the specified path
     *
     * @param path - the new path value for the cloned file system entry
     * @return a new FileSystemEntry that has all the same values as this object except for its path
     */
    public FileSystemEntry cloneWithNewPath(String path) {
        DirectoryEntry clone = new DirectoryEntry(path);
        clone.setLastModified(getLastModified());
        clone.setOwner(getOwner());
        clone.setGroup(getGroup());
        clone.setPermissions(getPermissions());
        return clone;
    }
}
