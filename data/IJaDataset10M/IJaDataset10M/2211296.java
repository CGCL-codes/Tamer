package com.googlecode.habano.systeminfo.beans;

/**
 * 
 * A bean to represent the status of a file system.
 * 
 * @author Agustin Barto <abarto@gmail.com>
 *
 */
public class FileSystemInfo {

    /** The path. */
    private String path;

    /** The size. */
    private long size;

    /** The free space. */
    private long freeSpace;

    /**
	 * Property <code>path</code>. The path to the root of the filesystem. It
	 * might be a mount point ("<code>/home</code>") in Linux/UNIX or a drive
	 * name in Windows ("<code>C:\</code>").
	 *
	 * @return the path
	 */
    public String getPath() {
        return path;
    }

    /**
	 * Setter for the <code>path</code> property.
	 *
	 * @param path the new path
	 */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
	 * Property <code>size</code>. The total size of the file system in
	 * bytes.
	 *
	 * @return the size
	 */
    public long getSize() {
        return size;
    }

    /**
	 * Setter for the <code>size</code> property.
	 *
	 * @param size the new size
	 */
    public void setSize(final long size) {
        this.size = size;
    }

    /**
	 * Property <code>freeSpace</code>. The space available on the file system
	 * in bytes.
	 *
	 * @return the free space
	 */
    public long getFreeSpace() {
        return freeSpace;
    }

    /**
	 * Setter for the <code>freeSpace</code> property.
	 *
	 * @param freeSpace the new free space
	 */
    public void setFreeSpace(final long freeSpace) {
        this.freeSpace = freeSpace;
    }

    @Override
    public String toString() {
        return "FileSystemInfo [freeSpace=" + freeSpace + ", path=" + path + ", size=" + size + "]";
    }
}
