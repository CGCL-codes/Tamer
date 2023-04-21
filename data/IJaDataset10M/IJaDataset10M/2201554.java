package org.vfsutils.ftpserver.usermanager;

import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.vfsutils.ftpserver.filesystem.VfsInfo;

/**
 * User that encapsulates a VFS File object representing its home dir.
 * Note that due to layering and virtualness the name of this home dir
 * does not necessarily have to match the name of HomeDirectory.
 * @author kleij - at - users.sourceforge.net
 *
 */
public class VfsUser extends BaseUser {

    private static final long serialVersionUID = -799346491877716615L;

    private VfsInfo vfsInfo = null;

    private boolean isAdmin = false;

    public VfsInfo getVfsInfo() {
        return vfsInfo;
    }

    public void setVfsInfo(VfsInfo vfsInfo) {
        this.vfsInfo = vfsInfo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
