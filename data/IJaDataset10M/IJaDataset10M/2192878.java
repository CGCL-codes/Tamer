package com.jaspersoft.jasperserver.api.metadata.user.domain;

/**
 * @author swood
 *
 */
public interface ObjectPermission {

    public ObjectIdentity getObjectIdentity();

    public void setObjectIdentity(ObjectIdentity objectIdentity);

    public Object getPermissionRecipient();

    public void setPermissionRecipient(Object permissionRecipient);

    public int getPermissionMask();

    public void setPermissionMask(int permissionMask);
}
