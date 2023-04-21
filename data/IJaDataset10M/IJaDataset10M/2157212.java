package jsr203.nio.file.attribute;

import java.io.IOException;
import java.util.List;
import jsr203.nio.file.FileRef;
import jsr203.nio.file.FileStore;
import jsr203.nio.file.Path;
import jsr203.nio.file.ProviderMismatchException;

/**
 * A file attribute view that supports reading or updating a file's Access
 * Control Lists (ACL) or file owner attributes.
 *
 * <p> ACLs are used to specify access rights to file system objects. An ACL is
 * an ordered list of {@link AclEntry access-control-entries}, each specifying a
 * {@link UserPrincipal} and the level of access for that user principal. This
 * file attribute view defines the {@link #getAcl() getAcl}, and {@link
 * #setAcl(List) setAcl} methods to read and write ACLs based on the ACL
 * model specified in <a href="http://www.ietf.org/rfc/rfc3530.txt"><i>RFC&nbsp;3530:
 * Network File System (NFS) version 4 Protocol</i></a>. This file attribute view
 * is intended for file system implementations that support the NFSv4 ACL model
 * or have a <em>well-defined</em> mapping between the NFSv4 ACL model and the ACL
 * model used by the file system. The details of such mapping are implementation
 * dependent and are therefore unspecified.
 *
 * <p> This class also extends {@code FileOwnerAttributeView} so as to define
 * methods to get and set the file owner.
 *
 * <p> When a file system provides access to a set of {@link FileStore
 * file-systems} that are not homogeneous then only some of the file systems may
 * support ACLs. The {@link FileStore#supportsFileAttributeView
 * supportsFileAttributeView} method can be used to test if a file system
 * supports ACLs.
 *
 * <p> This class implements the {@link UserPrincipalLookupService} interface
 * to allow for the lookup of user or group names. There is no guarantee that the
 * {@link UserPrincipal} objects returned from a lookup can be used by other
 * attribute view types.
 *
 * <a name="interop"><h4>Interoperability</h4></a>
 *
 * RFC&nbsp;3530 allows for special user identities to be used on platforms that
 * support the POSIX defined access permissions. The special user identities
 * are "{@code OWNER@}", "{@code GROUP@}", and "{@code EVERYONE@}". When both
 * the {@code AclFileAttributeView} and the {@link PosixFileAttributeView}
 * are supported then these special user identities may be included in ACL {@link
 * AclEntry entries} that are read or written. The {@link #lookupPrincipalByName
 * lookupPrincipalByName} method can be used to obtain a {@link UserPrincipal}
 * to represent these special identities.
 *
 * <p> <b>Usage Example:</b>
 * Suppose we wish to add an entry to an existing ACL to grant "joe" access:
 * <pre>
 *     AclFileAttributeView view = file.newFileAttributeView(AclFileAttributeView.class, true);
 *
 *     // lookup "joe"
 *     UserPrincipal joe = view.lookupPrincipalByName("joe");
 *
 *     // create ACE to give "joe" read access
 *     AclEntry entry = AclEntry.newBuilder()
 *         .setType(AclEntryType.ALLOW)
 *         .setPrincipal(joe)
 *         .setPermissions(AclEntryPermission.READ_DATA, AclEntryPermission.READ_ATTRIBUTES)
 *         .build();
 *
 *     // read ACL, insert ACE, re-write ACL
 *     List&lt;AclEntry&gt acl = view.getAcl();
 *     acl.add(0, entry);   // insert before any DENY entries
 *     view.setAcl(acl);
 * </pre>
 *
 * @since 1.7
 */
public interface AclFileAttributeView extends FileOwnerAttributeView {

    @Override
    AclFileAttributeView bind(FileRef obj);

    @Override
    AclFileAttributeView bind(FileRef obj, boolean followLinks);

    /**
     * @throws  UnsupportedOperationException           {@inheritDoc}
     * @throws  IllegalStateException                   {@inheritDoc}
     * @throws  IOException                             {@inheritDoc}
     * @throws  SecurityException                       {@inheritDoc}
     */
    @Override
    AclFileAttributeView setOwner(UserPrincipal owner) throws IOException;

    /**
     * Reads the access control list.
     *
     * <p> When the file system uses an ACL model that differs from the NFSv4
     * defined ACL model, then this method returns an ACL that is the translation
     * of the ACL to the NFSv4 ACL model.
     *
     * <p> The returned list is modifiable so as to facilitate changes to the
     * existing ACL. The {@link #setAcl setAcl} method is used to update
     * the file's ACL attribute.
     *
     * @return  An ordered list of {@link AclEntry entries} representing the
     *          ACL
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support reading a symbolic link's ACL
     * @throws  IllegalStateException
     *          If the attribute view is not bound, or if bound to an object
     *          that is not open for reading
     * @throws  IOException
     *          If an I/O error occurs
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, and it denies {@link RuntimePermission}<tt>("accessUserInformation")</tt>
     *          or its {@link SecurityManager#checkRead(String) checkRead} method
     *          denies read access to the file.
     */
    List<AclEntry> getAcl() throws IOException;

    /**
     * Updates (replace) the access control list.
     *
     * <p> Where the file system supports Access Control Lists, and it uses an
     * ACL model that differs from the NFSv4 defined ACL model, then this method
     * must translate the ACL to the model supported by the file system. This
     * method should reject (by throwing {@link IOException IOException}) any
     * attempt to write an ACL that would appear to make the file more secure
     * than would be the case if the ACL were updated. Where an implementation
     * does not support a mapping of {@link AclEntryType#AUDIT} or {@link
     * AclEntryType#ALARM} entries, then this method ignores these entries when
     * writing the ACL.
     *
     * <p> If an ACL entry contains a {@link AclEntry#principal user-principal}
     * that is not associated with the same provider as this attribute view then
     * {@link ProviderMismatchException} is thrown. Additional validation, if
     * any, is implementation dependent.
     *
     * <p> If the file system supports other security related file attributes
     * (such as a file {@link PosixFileAttributes#permissions
     * access-permissions} for example), the updating the access control list
     * may also cause these security related attributes to be updated.
     *
     * @param   acl
     *          The new access control list
     *
     * @return  This attribute view
     *
     * @throws  UnsupportedOperationException
     *          If not following links, the file is a link, and the implementation
     *          does not support updating a symbolic link's ACL
     * @throws  IllegalStateException
     *          If the attribute view is not bound, or if bound to an object
     *          that is not open for writing
     * @throws  IOException
     *          If an I/O error occurs or the ACL is invalid
     * @throws  SecurityException
     *          In the case of the default provider, a security manager is
     *          installed, it denies {@link RuntimePermission}<tt>("accessUserInformation")</tt>
     *          or its {@link SecurityManager#checkWrite(String) checkWrite}
     *          method denies write access to the file.
     */
    AclFileAttributeView setAcl(List<AclEntry> acl) throws IOException;

    /**
     * Constructs an {@link Attribute} object to represent the value of an
     * ACL attribute.
     *
     * <p> The resulting object can be used with methods such as {@link
     * Path#createFile createFile} to set the initial ACL when creating a file.
     *
     * <p> Where the file system supports Access Control Lists and the ACL model
     * differs from the NFSv4 defined ACL model, then this method must translate
     * the ACL to the model supported by the file system. Methods that create a
     * file should reject (by throwing {@link IOException IOException}) any
     * attempt to create a file that would appear to make the file more secure
     * than than would be the case if the ACL were updated.
     *
     * @return  A new attribute object; its initial value is an empty list
     *
     * @throws  UnsupportedOperationException
     *          If the implementation does not support creating a file or directory
     *          with an initial ACL that is set atomically when the file or
     *          directory is created
     */
    Attribute<List<AclEntry>> newAclAttribute();
}
