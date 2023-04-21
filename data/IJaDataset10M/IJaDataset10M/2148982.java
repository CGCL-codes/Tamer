package com.loc.pojo;

@SuppressWarnings("serial")
public class Resources extends BaseBean implements java.io.Serializable {

    private int resource_id;

    private String resource_type;

    private String resource_name;

    private int permission_id;

    private String memo;

    public Resources() {
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resourceId) {
        resource_id = resourceId;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resourceType) {
        resource_type = resourceType;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resourceName) {
        resource_name = resourceName;
    }

    public int getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(int permissionId) {
        permission_id = permissionId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
