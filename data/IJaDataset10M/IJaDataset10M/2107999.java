package org.opennms.web.springframework.security;

import java.util.HashMap;
import java.util.Map;

/**
 * An uninstantiatable class that provides a servlet container-independent
 * interface to the authentication system and a list of useful constants.
 * 
 * @author <A HREF="mailto:larry@opennms.org">Lawrence Karnowski </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 */
public final class Authentication extends Object {

    public static final String USER_ROLE = "ROLE_USER";

    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    public static final String READONLY_ROLE = "ROLE_READONLY";

    public static final String DASHBOARD_ROLE = "ROLE_DASHBOARD";

    public static final String RTC_ROLE = "ROLE_RTC";

    public static final String ROLE_PROVISION = "ROLE_PROVISION";

    private static Map<String, String> s_oldToNewMap = new HashMap<String, String>();

    static {
        s_oldToNewMap.put("OpenNMS RTC Daemon", RTC_ROLE);
        s_oldToNewMap.put("OpenNMS Administrator", ADMIN_ROLE);
        s_oldToNewMap.put("OpenNMS Read-Only User", READONLY_ROLE);
        s_oldToNewMap.put("OpenNMS Dashboard User", DASHBOARD_ROLE);
        s_oldToNewMap.put("OpenNMS Provision User", ROLE_PROVISION);
    }

    /** Private, empty constructor so this class cannot be instantiated. */
    private Authentication() {
    }

    public static String getSpringSecuirtyRoleFromOldRoleName(String oldRole) {
        return s_oldToNewMap.get(oldRole);
    }
}
