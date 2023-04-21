package com.gm.security.model.query;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.gm.common.orm.mybatis.BaseQuery;

public class PrivilegeResourceQuery extends BaseQuery implements Serializable {

    private static final long serialVersionUID = 3148176768559230877L;

    public static Map<String, String> FIELD_MAP = new HashMap<String, String>();

    static {
        FIELD_MAP.put("privilegeResource", "privilege_resource");
        FIELD_MAP.put("resourceInfoId", "resource_info_id");
        FIELD_MAP.put("privilegeId", "privilege_id");
    }

    public static final String PRIVILEGE_RESOURCE = "privilege_resource";

    public static final String RESOURCE_INFO_ID = "resource_info_id";

    public static final String PRIVILEGE_ID = "privilege_id";

    /** privilegeResource */
    private java.lang.Long privilegeResource;

    /** resourceInfoId */
    private java.lang.Long resourceInfoId;

    /** privilegeId */
    private java.lang.Long privilegeId;

    public java.lang.Long getPrivilegeResource() {
        return this.privilegeResource;
    }

    public void setPrivilegeResource(java.lang.Long value) {
        this.privilegeResource = value;
    }

    public java.lang.Long getResourceInfoId() {
        return this.resourceInfoId;
    }

    public void setResourceInfoId(java.lang.Long value) {
        this.resourceInfoId = value;
    }

    public java.lang.Long getPrivilegeId() {
        return this.privilegeId;
    }

    public void setPrivilegeId(java.lang.Long value) {
        this.privilegeId = value;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
