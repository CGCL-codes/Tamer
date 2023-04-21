package org.posterita.businesslogic;

import java.util.Properties;
import org.compiere.model.MRoleMenu;
import org.compiere.model.X_U_WebMenu;
import org.posterita.exceptions.OperationException;
import org.posterita.model.U_RoleMenu;

public class RoleMenuManager {

    public static MRoleMenu createRoleMenu(Properties ctx, int roleId, int menuId, String trxName) throws OperationException {
        MRoleMenu roleMenu;
        X_U_WebMenu menu = new X_U_WebMenu(ctx, menuId, null);
        int parentMenuId = menu.getParentMenu_ID();
        if (parentMenuId != 0) {
            int[] parentRoleMenuIds = MRoleMenu.getAllIDs(MRoleMenu.Table_Name, " ad_role_id = " + roleId + " and u_webmenu_id = " + parentMenuId, trxName);
            if (parentRoleMenuIds.length == 0) roleMenu = createRoleMenu(ctx, roleId, parentMenuId, trxName);
        }
        roleMenu = new MRoleMenu(ctx, 0, trxName);
        roleMenu.setAD_Role_ID(roleId);
        roleMenu.setU_WebMenu_ID(menuId);
        U_RoleMenu udiRoleMenu = new U_RoleMenu(roleMenu);
        udiRoleMenu.save();
        return roleMenu;
    }

    public static void createRoleMenus(Properties ctx, int roleId, int menuIds[], String trxName) throws OperationException {
        for (int i = 0; i < menuIds.length; i++) {
            RoleMenuManager.createRoleMenu(ctx, roleId, menuIds[i], trxName);
        }
    }

    public static boolean isRoleMenuPresent(Properties ctx, int roleId, int menuId, String trxName) {
        String whereClause = "AD_Role_ID=" + roleId + " and U_Menu_ID=" + menuId;
        int roleMenuIds[] = MRoleMenu.getAllIDs(MRoleMenu.Table_Name, whereClause, trxName);
        if (roleMenuIds == null || roleMenuIds.length == 0) return false;
        return true;
    }
}
