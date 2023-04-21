package ori.provider;

/**
 * Provides permissions related data.
 * 
 * @author Attila Korompai
 */
public interface IPermissionDataProvider {

    /**
     * @return IPermissionList a list of permission associated to the users
     */
    IPermissionList getUserPermissionList();

    /**
     * @return IPermissionList a list of permission associated to the roles
     */
    IPermissionList getRolePermissionList();

    /**
     * @return IUserRoleList Contains all user's roles.
     */
    IUserRoleList getUserRoleList();

    void refresh();
}
