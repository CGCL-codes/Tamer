package jsr203.nio.file.attribute;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import jsr203.nio.file.FileRef;

/**
 * A file attribute view that provides a view of the legacy "DOS" file attributes.
 * These attributes are supported by file systems such as the File Allocation
 * Table (FAT) format commonly used in <em>consumer devices</em>.
 *
 * <p> A {@code DosFileAttributeView} is a {@link BasicFileAttributeView} that
 * additionally supports access to the set of DOS attribute flags that are used
 * to indicate if the file is read-only, hidden, a system file, or archived.
 *
 * @since 1.7
 */
public interface DosFileAttributeView extends BasicFileAttributeView {

    @Override
    DosFileAttributeView bind(FileRef file);

    @Override
    DosFileAttributeView bind(FileRef file, boolean followLinks);

    /**
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support reading the attributes of a symbolic link
     * @throws  IllegalStateException                   {@inheritDoc}
     * @throws  IOException                             {@inheritDoc}
     * @throws  SecurityException                       {@inheritDoc}
     */
    @Override
    DosFileAttributes readAttributes() throws IOException;

    /**
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support updating the attributes of a symbolic link
     * @throws  IllegalStateException                   {@inheritDoc}
     * @throws  IOException                             {@inheritDoc}
     * @throws  SecurityException                       {@inheritDoc}
     */
    @Override
    DosFileAttributeView setTimes(Long lastModifiedTime, Long lastAccessTime, Long createTime, TimeUnit unit) throws IOException;

    /**
     * Updates the value of the read-only attribute.
     *
     * <p> It is implementation specific if the attribute can be updated as an
     * atomic operation with respect to other file system operations. An
     * implementation may, for example, require to read the existing value of
     * the DOS attribute in order to update this attribute.
     *
     * @param   value
     *          The new value of the attribute
     *
     * @return  this attribute view
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support updating the attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound, or if bound to an object
     *          that is not open for writing
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default, and a security manager is installed,
     *          its  {@link SecurityManager#checkWrite(String) checkWrite} method
     *          is invoked to check write access to the file
     */
    DosFileAttributeView setReadOnly(boolean value) throws IOException;

    /**
     * Updates the value of the hidden attribute.
     *
     * <p> It is implementation specific if the attribute can be updated as an
     * atomic operation with respect to other file system operations. An
     * implementation may, for example, require to read the existing value of
     * the DOS attribute in order to update this attribute.
     *
     * @param   value
     *          The new value of the attribute
     *
     * @return  this attribute view
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support updating the attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound, or if bound to an object
     *          that is not open for writing
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default, and a security manager is installed,
     *          its  {@link SecurityManager#checkWrite(String) checkWrite} method
     *          is invoked to check write access to the file
     */
    DosFileAttributeView setHidden(boolean value) throws IOException;

    /**
     * Updates the value of the system attribute.
     *
     * <p> It is implementation specific if the attribute can be updated as an
     * atomic operation with respect to other file system operations. An
     * implementation may, for example, require to read the existing value of
     * the DOS attribute in order to update this attribute.
     *
     * @param   value
     *          The new value of the attribute
     *
     * @return  this attribute view
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support updating the attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound, or if bound to an object
     *          that is not open for writing
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default, and a security manager is installed,
     *          its  {@link SecurityManager#checkWrite(String) checkWrite} method
     *          is invoked to check write access to the file
     */
    DosFileAttributeView setSystem(boolean value) throws IOException;

    /**
     * Updates the value of the archive attribute.
     *
     * <p> It is implementation specific if the attribute can be updated as an
     * atomic operation with respect to other file system operations. An
     * implementation may, for example, require to read the existing value of
     * the DOS attribute in order to update this attribute.
     *
     * @param   value
     *          The new value of the attribute
     *
     * @return  this attribute view
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support updating the attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound, or if bound to an object
     *          that is not open for writing
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default, and a security manager is installed,
     *          its  {@link SecurityManager#checkWrite(String) checkWrite} method
     *          is invoked to check write access to the file
     */
    DosFileAttributeView setArchive(boolean value) throws IOException;
}
