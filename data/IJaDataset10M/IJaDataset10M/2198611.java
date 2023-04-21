package net.orangemile.security.acl;

/**
 * @author Orange Mile, Inc
 */
public interface AclContext {

    public void put(Object object, Acl acl);

    public Acl get(Object object);
}
