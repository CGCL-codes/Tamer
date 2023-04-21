package com.incendiaryblue.cmslite;

import com.incendiaryblue.appframework.AppConfig;
import com.incendiaryblue.appframework.StringProperty;
import com.incendiaryblue.user.Group;
import com.incendiaryblue.user.User;
import com.incendiaryblue.user.UserGroupRel;

/**
 * A class containing only static methods for checking security on the file
 * upload tool. Security can be optionally implemented using the incendiaryblue Java
 * user management system as follows:<p>
 *
 * There are three group names defined in the CMSLiteConstants class -
 * FU_UPLOAD_GROUP, FU_CREATE_GROUP and FU_DELETE_GROUP, to which incendiaryblue users
 * can be added to give them the right to upload files, create folders and
 * delete files respectively. If integrated security is enabled, the upload
 * manager will check that the current user is a member of the necessary group
 * using the methods in this class before allowing the action.<p>
 *
 * Integrated security is switched on in a incendiaryblue Java application by
 * specifying a StringProperty value in the XML configuration file with the
 * name 'Upload Security' and value 'true'. If this StringProperty is
 * not present or its value is not 'true', integration is disabled.
 */
public class FileUploadSecurity {

    private static boolean doSecurity = false;

    static {
        try {
            StringProperty sp = (StringProperty) AppConfig.getComponent(StringProperty.class, "Upload Security");
            if (sp != null && sp.getValue().toLowerCase().equals("true")) {
                doSecurity = true;
            }
        } catch (IllegalArgumentException e) {
        }
    }

    /**
	 * Returns a boolean indicating whether the current user in the executing
	 * thread has permission to upload files using the upload manager. This will
	 * be true if a) integrated security is disabled or b) the current user is
	 * a member of the CMSLiteConstants.FU_UPLOAD_GROUP group.
	 */
    public static boolean canUpload() {
        return (!doSecurity || UserGroupRel.userIsInGroup(User.getCurrentUser(), Group.getGroup(CMSLiteConstants.FU_UPLOAD_GROUP)));
    }

    /**
	 * Returns a boolean indicating whether the current user in the executing
	 * thread has permission to delete files using the upload manager. This will
	 * be true if a) integrated security is disabled or b) the current user is
	 * a member of the CMSLiteConstants.FU_DELETE_GROUP group.
	 */
    public static boolean canDelete() {
        return (!doSecurity || UserGroupRel.userIsInGroup(User.getCurrentUser(), Group.getGroup(CMSLiteConstants.FU_DELETE_GROUP)));
    }

    /**
	 * Returns a boolean indicating whether the current user in the executing
	 * thread has permission to create folders using the upload manager. This
	 * will be true if a) integrated security is disabled or b) the current user
	 * is a member of the CMSLiteConstants.FU_CREATE_GROUP group.
	 */
    public static boolean canCreateFolder() {
        return (!doSecurity || UserGroupRel.userIsInGroup(User.getCurrentUser(), Group.getGroup(CMSLiteConstants.FU_CREATE_GROUP)));
    }

    /**
	 * Returns a boolean to indicate whether integrated security is enabled.
	 */
    public static boolean securityEnabled() {
        return doSecurity;
    }
}
