package com.objectcode.time4u.server.web.jsf.tag;

public interface UserRoleAware {

    static final String ENABLED_ON_USER_ROLE_ATTR = "enabledOnUserRole";

    static final String VISIBLE_ON_USER_ROLE_ATTR = "visibleOnUserRole";

    String getEnabledOnUserRole();

    void setEnabledOnUserRole(String userRole);

    String getVisibleOnUserRole();

    void setVisibleOnUserRole(String userRole);
}
