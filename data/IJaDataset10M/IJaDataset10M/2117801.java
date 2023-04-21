package com.netx.eap.R1.bl;

import com.netx.bl.R1.core.*;

public class UserRolesMetaData extends TimedMetaData {

    public final Field userId = new FieldForeignKey(this, "userId", "user_id", null, true, true, EAP.getUsers().getMetaData(), FieldForeignKey.ON_DELETE_CONSTRAINT.CASCADE);

    public final Field roleId = new FieldForeignKey(this, "roleId", "role_id", null, true, true, EAP.getRoles().getMetaData(), FieldForeignKey.ON_DELETE_CONSTRAINT.CASCADE);

    public final Field primaryRole = new FieldBoolean(this, "primaryRole", "primary_role", new Boolean(false), true, false);

    UserRolesMetaData() {
        super("UserRoles", "eap_user_roles");
        addPrimaryKeyField(userId);
        addPrimaryKeyField(roleId);
        addField(primaryRole);
        addDefaultFields();
    }

    public final DATA_TYPE getDataType() {
        return DATA_TYPE.TRANSACTIONAL;
    }

    public Class<UserRole> getInstanceClass() {
        return UserRole.class;
    }

    public Field getAutonumberKeyField() {
        return null;
    }
}
