package jsr203.nio.file.attribute;

import java.io.IOException;
import java.nio.ByteBuffer;
import jsr203.nio.file.FileRef;

/**
 * A file attribute view that provides a view of a file's <em>named
 * attributes</em>. This {@code FileAttributeView} provides a view of a file's
 * named attributes as a set of name/value pairs. It is primarily intended for
 * file system implementations that support a mechanism to associate metadata
 * with a file that is not meaningful to the file system. It may also be
 * emulated on file systems that do not support this feature directly but
 * instead support <em>named subfiles</em>, a concept whereby a file may have
 * several named streams. The details of such emulation is highly
 * implementation specific and therefore not specified.
 *
 * <p> This {@code FileAttributeView} represents an attribute name as a {@code
 * String}. An implementation may require to encode and decode from the platform
 * or file system representation when accessing the attribute. The {@link #read
 * read} and {@link #write write} methods are defined to read into or write from
 * a {@link ByteBuffer}. This {@code FileAttributeView} is not intended to be
 * used where the size of an attribute value is larger than  {@link
 * Integer#MAX_VALUE}.

 * <p> Named attributes may be used in some implementations to store security
 * related attributes so consequently, in the case of the default provider at
 * least, all methods that access named attributes require the
 * {@code RuntimePermission("accessNamedAttributes")} permission when a
 * security manager is installed.
 *
 * <p> The {@link jsr203.nio.file.FileStore#supportsFileAttributeView
 * supportsFileAttributeView} method may be used to test if a specific {@link
 * jsr203.nio.file.FileStore FileStore} supports the storage of named attributes.
 *
 * @since 1.7
 */
public interface NamedAttributeView extends FileAttributeView {

    @Override
    NamedAttributeView bind(FileRef file);

    @Override
    NamedAttributeView bind(FileRef file, boolean followLinks);

    /**
     * Returns an object to iterate over the names of the file's named
     * attributes. The ordering that the names are returned is not specified.
     * The iterator is weakly consistent. It is thread safe. It may or may not
     * reflect updates to the list of named attributes that occur after the
     * {@code Iterable} is created. The iterator's {@link
     * java.util.Iterator#remove remove} method removes the named attribute for
     * the last element returned by the iterator, as if by invoking the delete
     * {@link #delete delete} method.
     *
     * @return  An object to iterate over the names of the file's named
     *          attributes
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support accessing named attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, and it denies {@link
     *          RuntimePermission}<tt>("accessNamedAttributes")</tt>
     *          or its {@link SecurityManager#checkRead(String) checkRead} method
     *          denies read access to the file.
     */
    Iterable<String> list() throws IOException;

    /**
     * Returns the size of the value of the given attribute.
     *
     * @param   name
     *          The attribute name
     *
     * @return  The size of the attribute value, in bytes.
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support accessing named attributes of a symbolic link
     * @throws  ArithmeticException
     *          If the size of the attribute is larger than {@link Integer#MAX_VALUE}
     * @throws  IllegalStateException
     *          If the attribute view is not bound
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, and it denies {@link
     *          RuntimePermission}<tt>("accessNamedAttributes")</tt>
     *          or its {@link SecurityManager#checkRead(String) checkRead} method
     *          denies read access to the file.
     */
    int size(String name) throws IOException;

    /**
     * Read the value of an attribute into a buffer.
     *
     * <p> This method reads the value of the attribute into the given buffer
     * as a sequence of bytes, failing if the number of bytes remaining in
     * the buffer is insufficient to read the complete attribute value. The
     * number of bytes transferred into the buffer is {@code n}, where {@code n}
     * is the size of the attribute value. The first byte in the sequence is at
     * index {@code p} and the last byte is at index {@code p + n - 1}, where
     * {@code p} is the buffer's position. Upon return the buffer's position
     * will be equal to {@code p + n}; its limit will not have changed.
     *
     * @param   name
     *          The attribute name
     * @param   dst
     *          The destination buffer
     *
     * @return  The number of bytes read, possibly zero
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support accessing named attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, and it denies {@link
     *          RuntimePermission}<tt>("accessNamedAttributes")</tt>
     *          or its {@link SecurityManager#checkRead(String) checkRead} method
     *          denies read access to the file.
     *
     * @see #size
     */
    int read(String name, ByteBuffer dst) throws IOException;

    /**
     * Writes the value of an extended attribute from a buffer.
     *
     * <p> This method writes the value of the attribute from a given buffer as
     * a sequence of bytes. The size of the value to transfer is {@code r},
     * where {@code r} is the number of bytes remaining in the buffer, that is
     * {@code src.remaining()}. The sequence of bytes is transferred from the
     * buffer starting at index {@code p}, where {@code p} is the buffer's
     * position. Upon return, the buffer's position will be equal to {@code
     * p + n}, where {@code n} is the number of bytes transferred; its limit
     * will not have changed.
     *
     * <p> If an attribute of the given name already exists then its value is
     * replaced. If the attribute does not exist then it is created. If it
     * implementation specific if a test to check for the existence of the
     * attribute and the creation of attribute are atomic with repect to other
     * file system activities.
     *
     * <p> Where there is insufficient spec to store the attribute, or the
     * attribute name or value exceed an implementation specific maximum size
     * then an {@code IOException} is thrown.
     *
     * @param   name
     *          The attribute name
     * @param   src
     *          The buffer containing the attribute value
     *
     * @return  The number of bytes written, possibly zero
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support accessing named attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, and it denies {@link
     *          RuntimePermission}<tt>("accessNamedAttributes")</tt>
     *          or its {@link SecurityManager#checkWrite(String) checkWrite}
     *          method denies write access to the file.
     */
    int write(String name, ByteBuffer src) throws IOException;

    /**
     * Deletes an attribute.
     *
     * @param   name
     *          The attribute name
     *
     * @return  This file attribute view
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support accessing named attributes of a symbolic link
     * @throws  IllegalStateException
     *          If the attribute view is not bound
     * @throws  IOException
     *          If an I/O error occurs or the attribute does not exist
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, and it denies {@link
     *          RuntimePermission}<tt>("accessNamedAttributes")</tt>
     *          or its {@link SecurityManager#checkWrite(String) checkWrite}
     *          method denies write access to the file.
     */
    NamedAttributeView delete(String name) throws IOException;
}
