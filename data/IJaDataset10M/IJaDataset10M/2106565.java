package com.nullfish.app.jfd2.comparator;

import com.nullfish.lib.vfs.Permission;
import com.nullfish.lib.vfs.VFile;
import com.nullfish.lib.vfs.exception.VFSException;
import com.nullfish.lib.vfs.permission.ClassFileAccess;
import com.nullfish.lib.vfs.permission.PermissionType;

/**
 * @author shunji
 *
 */
public class PermissionComparator implements FileComparator {

    public int compare(VFile file1, VFile file2) {
        try {
            Permission permission1 = file1.getPermission();
            boolean writable1 = permission1 != null ? permission1.hasPermission(PermissionType.WRITABLE, ClassFileAccess.ALL) : false;
            Permission permission2 = file2.getPermission();
            boolean writable2 = permission2 != null ? permission2.hasPermission(PermissionType.WRITABLE, ClassFileAccess.ALL) : false;
            if (writable1 == writable2) {
                return 0;
            }
            if (!writable1) {
                return -1;
            } else {
                return 1;
            }
        } catch (VFSException e) {
            return 0;
        }
    }
}
