package com.openbravo.bean;

import com.openbravo.bean.shard.UserOrderTypePermission;
import com.openbravo.bean.shard.UserPermission;
import com.openbravo.bean.infobean.RoleInfo;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class Role extends RoleInfo {

    private List<UserPermission> permissions;

    private Map<String, UserOrderTypePermission> orderTypePermissions;

    private Map<String, UserOrderTypePermission> stateProcessPermissions;

    public Map<String, UserOrderTypePermission> getOrderTypePermissions() {
        return orderTypePermissions;
    }

    public void setOrderTypePermissions(Map<String, UserOrderTypePermission> orderTypePermissions) {
        this.orderTypePermissions = orderTypePermissions;
    }

    public List<UserPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public void setStateProcessPermissions(Map<String, UserOrderTypePermission> formatOrderTypePermissions) {
        stateProcessPermissions = formatOrderTypePermissions;
    }

    public Map<String, UserOrderTypePermission> getStateProcessPermissions() {
        return stateProcessPermissions;
    }
}
