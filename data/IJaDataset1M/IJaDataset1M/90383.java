package com.gm.security.model.base;

import java.util.HashMap;
import java.util.Map;
import com.gm.common.orm.mybatis.BaseEntity;

public class PrivilegeResourceBase extends BaseEntity implements java.io.Serializable {

    protected static final long serialVersionUID = 5454155825314635342L;

    protected java.lang.Long privilegeResource;

    protected java.lang.Long resourceInfoId;

    protected java.lang.Long privilegeId;

    public static Map<String, String> FIELD_MAP = new HashMap<String, String>();

    static {
        FIELD_MAP.put("privilegeResource", "privilege_resource");
        FIELD_MAP.put("resourceInfoId", "resource_info_id");
        FIELD_MAP.put("privilegeId", "privilege_id");
    }

    public static final String PRIVILEGE_RESOURCE = "privilegeResource";

    public static final String RESOURCE_INFO_ID = "resourceInfoId";

    public static final String PRIVILEGE_ID = "privilegeId";

    public void setPrivilegeResource(java.lang.Long value) {
        this.privilegeResource = value;
    }

    public java.lang.Long getPrivilegeResource() {
        return this.privilegeResource;
    }

    public void setResourceInfoId(java.lang.Long value) {
        this.resourceInfoId = value;
    }

    public java.lang.Long getResourceInfoId() {
        return this.resourceInfoId;
    }

    public void setPrivilegeId(java.lang.Long value) {
        this.privilegeId = value;
    }

    public java.lang.Long getPrivilegeId() {
        return this.privilegeId;
    }
}
