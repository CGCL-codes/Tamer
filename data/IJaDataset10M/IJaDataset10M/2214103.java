package com.netx.eap.R1.bl;

import com.netx.bl.R1.core.*;

public class FunctionInstance extends TimedInstance<FunctionsMetaData, Functions> {

    public FunctionInstance(String alias) throws ValidationException {
        setPrimaryKey(getMetaData().alias, alias);
    }

    public Functions getEntity() {
        return Functions.getInstance();
    }

    public String getAlias() {
        return (String) getValue(getMetaData().alias);
    }

    public String getPermissionId() {
        return (String) getValue(getMetaData().permissionId);
    }

    public Permission getPermission(Connection c) throws BLException {
        String permissionId = getPermissionId();
        if (permissionId == null) {
            return null;
        } else {
            return Permissions.getInstance().get(c, permissionId);
        }
    }

    public String getTitle() {
        return (String) getValue(getMetaData().title);
    }

    public String getClassName() {
        return (String) getValue(getMetaData().className);
    }

    public String getPackageName() {
        return (String) getValue(getMetaData().packageName);
    }
}
