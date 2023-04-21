package com.gwtaf.core.shared.permission;

import java.io.Serializable;
import com.gwtaf.core.shared.util.StringUtil;

public class Privilege implements IPrivilege, IPermissionDelegate, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    protected Privilege() {
    }

    public Privilege(String id) {
        if (StringUtil.isNull(id)) throw new IllegalArgumentException("Illegal id");
        this.id = id;
    }

    public Privilege(PrivilegeNamespace namespace, String name) {
        PrivilegeUtil.checkName(name);
        if (namespace == null) throw new IllegalArgumentException("Group must not be null");
        this.id = PrivilegeUtil.mergePath(namespace.getPath(), name);
    }

    public String getId() {
        return id;
    }

    public Permission getPermission(Object... data) {
        return GlobalPrivilegeProvider.get().getPermission(this.id, data);
    }

    public void addListener(IPermissionListener listener) {
        GlobalPrivilegeProvider.addListener(listener);
    }

    public void removeListener(IPermissionListener listener) {
        GlobalPrivilegeProvider.removeListener(listener);
    }
}
