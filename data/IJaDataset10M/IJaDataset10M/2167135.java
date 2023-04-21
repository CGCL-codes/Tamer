package epm;

import java.util.ArrayList;
import java.util.List;
import com.systop.common.modules.security.user.model.Role;
import com.systop.core.Constants;

/**
 * EPM公共常量类
 * @author sam
 *
 */
public final class EpmConstants {

    /**
   * 登录用户当前所处项目的ID的session id
   */
    public static final String SESSION_ACTIVE_PROJECT = "__session_active_project__";

    /**
   * 系统管理员角色
   */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
   * 项目管理员
   */
    public static final String ROLE_PM = "ROLE_PM";

    /**
   * 项目普通成员
   */
    public static final String ROLE_NORMAL = "ROLE_NORMAL";

    /**
   * 系统管理员账户名
   */
    public static final String ADMIN = "admin";

    private static Role sysRole(String roleName, String descn) {
        Role role = new Role();
        role.setName(roleName);
        role.setDescn(descn);
        role.setIsSys(Constants.STATUS_AVAILABLE);
        return role;
    }

    /**
   * 系统角色列表
   */
    public static final List<Role> SYS_ROLES = new ArrayList<Role>();

    static {
        SYS_ROLES.add(sysRole(ROLE_ADMIN, "系统管理员角色"));
        SYS_ROLES.add(sysRole(ROLE_PM, "项目管理员"));
        SYS_ROLES.add(sysRole(ROLE_NORMAL, "项目组普通成员"));
    }

    /**
   * 防止实例化
   */
    private EpmConstants() {
    }
}
