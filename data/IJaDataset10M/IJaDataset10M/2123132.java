package com.germinus.portlet.file_directory;

import com.germinus.util.Partition;
import com.germinus.xpression.cms.directory.DirectoryFolder;
import com.germinus.xpression.groupware.GroupwareRole;
import com.germinus.xpression.groupware.util.LiferayHelper;
import com.germinus.xpression.groupware.util.LiferayHelperFactory;
import java.util.List;
import java.util.Map;
import static com.germinus.xpression.groupware.DirectoryItemAuthorizationActions.ADD_SUBITEM;
import static com.germinus.xpression.groupware.DirectoryItemAuthorizationActions.MANAGE_FOLDER;
import static com.germinus.xpression.groupware.DirectoryItemAuthorizationActions.VIEW;

/**
 *
 * User: Eduardo
 * Date: 29/08/11
 * Time: 11:51
 *
 */
public class FolderPermissionsData extends PermissionWizardForm {

    private static LiferayHelper liferayHelper = LiferayHelperFactory.getLiferayHelper();

    private static long GUEST_ROLE_ID = liferayHelper.getRoleId(null, GUEST_ROLE_NAME);

    public FolderPermissionsData(DirectoryFolder currentFolder) {
        this.currentFolder = currentFolder;
        Map<GroupwareRole, List<String>> authorizedActionsForEachRole = getAuthorizator().authorizedActionsForEachRole(currentFolder);
        Partition<String, GroupwareRole> rolesWhichHasAction = new Partition<String, GroupwareRole>();
        for (GroupwareRole groupwareRole : authorizedActionsForEachRole.keySet()) {
            for (String action : authorizedActionsForEachRole.get(groupwareRole)) {
                rolesWhichHasAction.add(action, groupwareRole);
            }
        }
        List<GroupwareRole> viewRoles = rolesWhichHasAction.get(VIEW.name());
        List<GroupwareRole> addRoles = rolesWhichHasAction.get(ADD_SUBITEM.name());
        List<GroupwareRole> manageRoles = rolesWhichHasAction.get(MANAGE_FOLDER.name());
        if (viewRoles != null && viewRoles.size() > 0) this.viewPermissionRole = viewRoles.get(0).getId();
        if (addRoles != null && addRoles.size() > 0) this.addSubItemPermissionRole = addRoles.get(0).getId();
        if (manageRoles != null && manageRoles.size() > 0) this.manageFolderPermissionRole = manageRoles.get(0).getId();
    }

    public boolean areFolderPermissionModified() {
        if (addSubItemPermissionRole != ADMINITRATOR_ROLE_ID) return true;
        if (manageFolderPermissionRole != ADMINITRATOR_ROLE_ID) return true;
        if (viewPermissionRole != GUEST_ROLE_ID) return true;
        return false;
    }
}
