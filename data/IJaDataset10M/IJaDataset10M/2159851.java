package de.intarsys.pdf.crypt;

public abstract class AbstractAccessPermissions implements IAccessPermissions {

    protected final PermissionFlags flags;

    public AbstractAccessPermissions(PermissionFlags flags) {
        super();
        this.flags = flags;
    }
}
