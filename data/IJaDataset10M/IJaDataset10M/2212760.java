package org.proteusframework.platformservice.persistence.api;

import org.proteusframework.core.api.model.IGlyph;
import org.proteusframework.core.api.model.IMetadata;
import org.proteusframework.core.api.model.INamespace;
import org.proteusframework.platformservice.persistence.vfs.FilePath;
import java.io.File;

public interface IVFSFile extends INamespace, IMetadata, IGlyph {

    /**
     * Get the java.io.File object that backs this object. You may read and write from the file, but do not delete
     * or move it
     *
     * @return The File object
     */
    File getFile();

    /**
     * Get the file size, in bytes.
     *
     * @return file size, measured in bytes.
     */
    long length();

    /**
     * Tests whether the application can execute the file denoted by
     * this abstract pathname.
     */
    boolean canExecute();

    /**
     * Tests whether the application can read the file denoted by this abstract pathname.
     */
    boolean canRead();

    /**
     * Tests whether the application can modify the file denoted by
     * this abstract pathname.
     */
    boolean canWrite();

    /**
     * Compares two abstract pathnames lexicographically.
     */
    int compareTo(File pathname);

    /**
     * Compares two VFS files. The files are considered equal if their location in the Virtual File System is the same.
     *
     * @param file File to comapare with.
     * @return True if the files have lexographically equal same file paths. The comparison is case sensitive.
     */
    int compareTo(IVFSFile file);

    /**
     * Tests this abstract pathname for equality with the given object.
     */
    boolean equals(Object obj);

    /**
     * Tests whether the file or directory denoted by this abstract pathname exists.
     */
    boolean exists();

    /**
     * Returns the name of the file or directory denoted by this abstract pathname.
     */
    String getName();

    /**
     * Returns the abstract pathname of this abstract pathname's
     * parent, or null if this pathname does not name a parent
     * directory.
     *
     * @return A path relative to the VFS root of the file's parent.
     */
    FilePath getParentFilePath();

    /**
     * Converts this abstract pathname into a pathname string.
     */
    String getPath();

    /**
     * Converts the abstract pathname into a fully qualified (relative) FilePath
     *
     * @return FilePath containing the fully qualified VFS path (relative to the VFS root)
     */
    FilePath getFilePath();

    /**
     * Get the absolute path to the file on disk. This path is not relative to the Virtual File System root, and
     * is not portable. <b>Plugins should generally not use this method. Do not assume that the file is ready for use.
     * The file on disk may represent a remote file that needs to be downloaded before use.</b> Use {@link #getFile()}
     * to safely obtain a reference to the underlying file.
     *
     * @return Absolute path to the file on disk.
     * @see #getFile()
     */
    String getAbsolutePath();

    /**
     * Tests whether the file denoted by this abstract pathname is a
     * directory.
     */
    boolean isDirectory();

    /**
     * Tests whether the file denoted by this abstract pathname is a
     * normal file.
     */
    boolean isFile();

    /**
     * Tests whether the file named by this abstract pathname is a
     * hidden file.
     */
    boolean isHidden();

    /**
     * Returns the time that the file denoted by this abstract
     * pathname was last modified.
     */
    long lastModified();

    /**
     * A convenience method to set the owner's execute permission for
     * this abstract pathname.
     *
     * @param executableFlag
     * @return <code>true</code> if and only if the operation succeeded.
     */
    boolean setExecutable(boolean executableFlag);

    /**
     * Sets the owner's or everybody's execute permission for this
     * abstract pathname.
     */
    boolean setExecutable(boolean executableFlag, boolean ownerOnlyFlag);

    /**
     * Sets the last-modified time of the file or directory named by
     * this abstract pathname.
     */
    boolean setLastModified(long time);

    /**
     * Sets the owner's or everybody's read permission for this
     * abstract pathname.
     */
    boolean setReadable(boolean readableFlag);

    /**
     * Sets the owner's or everybody's read permission for this
     * abstract pathname.
     */
    boolean setReadable(boolean readableFlag, boolean ownerOnlyFlag);

    /**
     * Marks the file or directory named by this abstract pathname so
     * that only read operations are allowed.
     */
    boolean setReadOnly();

    /**
     * A convenience method to set the owner's write permission for
     * this abstract pathname.
     */
    boolean setWritable(boolean writableFlag);

    /**
     * Sets the owner's or everybody's write permission for this
     * abstract pathname.
     */
    boolean setWritable(boolean writableFlag, boolean ownerOnlyFlag);

    /**
     * Flag that indicates if the underlying implementation supports the association of metadata with the VFS file.
     *
     * @return true, if the underlying implementation supports metadata association with the VFS file
     */
    boolean supportsMetadata();

    /**
     * Flag that indicates if the underlying implementation supports the association of an arbitrary icon with the
     * VFS file.
     *
     * @return true, if the underlying implementation supports the arbitrary association of an icon with the VFS file
     */
    boolean supportsIcon();
}
