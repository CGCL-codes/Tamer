package org.drftpd.vfs.perms;

import org.drftpd.GlobalContext;
import org.drftpd.permissions.PathPermission;

/**
 * @author fr0w
 * @version $Id: VFSPermHandler.java 1891 2008-05-16 01:31:39Z fr0w $
 */
public abstract class VFSPermHandler {

    protected void addPermission(String directive, PathPermission pathPerm) {
        GlobalContext.getConfig().getVFSPermissions().addPermissionToMap(directive, pathPerm);
    }
}
