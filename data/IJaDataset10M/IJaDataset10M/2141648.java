package org.subethamail.web.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.entity.i.Permission;

/**
 * A class and static method that will wrap a Set<Permission> in a
 * Map<String, Boolean>.  This way permissions can be checked
 * in the JSP as "${perms.PERMISSION_NAME}".
 * 
 * This implements only the methods that are likely to be called
 * from JSP expressions.  The static wrap() method is suitable
 * for calling as a JSP function.
 *  
 * @author Jeff Schnitzer
 */
public class PermissionWrapper implements Map<String, Boolean> {

    /** */
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(PermissionWrapper.class);

    /** */
    Set<Permission> perms;

    /** */
    public PermissionWrapper(Set<Permission> perms) {
        this.perms = perms;
    }

    /**
	 * Creates a new wrapper with the useful interface.
	 */
    public static Map<String, Boolean> wrapPerms(Set<Permission> perms) {
        return new PermissionWrapper(perms);
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        Permission p = Permission.valueOf(key.toString());
        return this.perms.contains(p);
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Set<Entry<String, Boolean>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public Boolean get(Object key) {
        Permission p = Permission.valueOf(key.toString());
        if (this.perms.contains(p)) return Boolean.TRUE; else return Boolean.FALSE;
    }

    public boolean isEmpty() {
        return this.perms.isEmpty();
    }

    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    public Boolean put(String key, Boolean value) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends String, ? extends Boolean> t) {
        throw new UnsupportedOperationException();
    }

    public Boolean remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return this.perms.size();
    }

    public Collection<Boolean> values() {
        throw new UnsupportedOperationException();
    }
}
