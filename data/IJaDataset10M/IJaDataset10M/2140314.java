package org.apache.harmony.security;

import java.security.Identity;
import java.security.IdentityScope;
import java.security.KeyManagementException;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.harmony.security.internal.nls.Messages;

/**
 * @see java.security.IdentityScope
 */
public class SystemScope extends IdentityScope {

    /**
     * @serial
     */
    private static final long serialVersionUID = -4810285697932522607L;

    private Hashtable names = new Hashtable();

    private Hashtable keys = new Hashtable();

    /**
     * @see java.security.IdentityScope#IdentityScope()
     */
    public SystemScope() {
        super();
    }

    /**
     * @see java.security.IdentityScope#IdentityScope(String)
     */
    public SystemScope(String name) {
        super(name);
    }

    /**
     * @see java.security.IdentityScope#IdentityScope(String, IdentityScope)
     */
    public SystemScope(String name, IdentityScope scope) throws KeyManagementException {
        super(name, scope);
    }

    /**
     * @see java.security.IdentityScope#size()
     */
    public int size() {
        return names.size();
    }

    /**
     * @see java.security.IdentityScope#getIdentity(java.lang.String)
     */
    public synchronized Identity getIdentity(String name) {
        if (name == null) {
            throw new NullPointerException();
        }
        return (Identity) names.get(name);
    }

    /**
     * @see java.security.IdentityScope#getIdentity(java.security.PublicKey)
     */
    public synchronized Identity getIdentity(PublicKey key) {
        if (key == null) {
            return null;
        }
        return (Identity) keys.get(key);
    }

    /**
     * @see java.security.IdentityScope#addIdentity(java.security.Identity)
     */
    public synchronized void addIdentity(Identity identity) throws KeyManagementException {
        if (identity == null) {
            throw new NullPointerException(Messages.getString("security.92"));
        }
        String name = identity.getName();
        if (names.containsKey(name)) {
            throw new KeyManagementException(Messages.getString("security.93", name));
        }
        PublicKey key = identity.getPublicKey();
        if (key != null && keys.containsKey(key)) {
            throw new KeyManagementException(Messages.getString("security.94", key));
        }
        names.put(name, identity);
        if (key != null) {
            keys.put(key, identity);
        }
    }

    /**
     * @see java.security.IdentityScope#removeIdentity(java.security.Identity)
     */
    public synchronized void removeIdentity(Identity identity) throws KeyManagementException {
        if (identity == null) {
            throw new NullPointerException(Messages.getString("security.92"));
        }
        String name = identity.getName();
        if (name == null) {
            throw new NullPointerException(Messages.getString("security.95"));
        }
        boolean contains = names.containsKey(name);
        names.remove(name);
        PublicKey key = identity.getPublicKey();
        if (key != null) {
            contains = contains || keys.containsKey(key);
            keys.remove(key);
        }
        if (!contains) {
            throw new KeyManagementException(Messages.getString("security.96"));
        }
    }

    /**
     * @see java.security.IdentityScope#identities()
     */
    public Enumeration identities() {
        return names.elements();
    }
}
