package org.nodal.security;

/**
 * <p>The basic interface to queries on security and privacy of a Node.</p>
 * 
 * <p>The static values represent the right to access the various
 * restricted interfaces of a Node.</p>
 * 
 * <p>The READ permission represents access to the Content of a Node.
 * Without this, a User may only access a Node's NID and Type.</p>
 * 
 * <p>The EDIT permission represents access to an Editor for a Node.
 * Without this permission, a User may not modify the contents of a
 * Node.  When the owner of a Node removes his/her own EDIT
 * permission, then the Node is read-only.  An EDIT permission cannot
 * be granted to anyone without READ permission.</p>
 * 
 * <p>The HISTORY permission allows a User access to the NodeHistory
 * for a Node.  This allows one to examine the audit trail or
 * provenance of a Node.</p?
 * 
 * <p>The DELEGATE_ permissions allows a User to delegate a subset of
 * his own permissions to another User or revoke permissions
 * previously delegated.  If this permission is revoked, then all such
 * delegated permissions are automatically revoked.</p>
 * 
 * <p>On creation, the owner of a Node is granted all of these
 * permissions.</p>
 *
 * @author Lee Iverson <leei@ece.ubc.ca> 
 * @version May 23, 2002
 */
public interface Capability {

    int READ = 1;

    int EDIT = 2;

    int HISTORY = 4;

    int DELEGATE_READ = READ << 8;

    int DELEGATE_EDIT = EDIT << 8;

    int DELEGATE_HISTORY = HISTORY << 8;

    boolean allowed(int ops);

    boolean allowRead();

    boolean allowEdit();

    boolean allowHistory();

    boolean allowDelegate(int op);
}
