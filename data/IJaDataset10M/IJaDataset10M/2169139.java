package com.unitt.commons.authorization.jpa.call;

import java.io.Serializable;
import com.unitt.commons.authorization.PermissionKey;
import com.unitt.commons.authorization.jpa.Permission;
import com.unitt.commons.authorization.jpa.PermissionDao;
import com.unitt.commons.authorization.jpa.PermissionPk;
import com.unitt.commons.authorization.util.PermissionHelper;

public class HasPermission implements Runnable, Serializable {

    private static final long serialVersionUID = 2109183662766060130L;

    protected RunnableCallback<HasPermission, Boolean> callback;

    protected PermissionKey key;

    protected long requestedPermission;

    protected PermissionDao dao;

    public HasPermission(RunnableCallback<HasPermission, Boolean> aCallback, PermissionKey aKey, long aRequestedPermission, PermissionDao aDao) {
        super();
        callback = aCallback;
        key = aKey;
        requestedPermission = aRequestedPermission;
        dao = aDao;
    }

    public void run() {
        try {
            Boolean result = false;
            Permission permission = dao.find(new PermissionPk(key));
            if (permission != null) {
                result = PermissionHelper.allows(requestedPermission, permission.getPermissionMask());
            }
            callback.onSuccess(result);
        } catch (Exception e) {
            callback.onError(this, e);
        }
    }

    @Override
    public String toString() {
        return "HasPermission [key=" + key + ", requestedPermission=" + requestedPermission + "]";
    }
}
