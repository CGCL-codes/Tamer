package org.opencms.workplace.tools;

import org.opencms.i18n.A_CmsMessageBundle;
import org.opencms.i18n.I_CmsMessageBundle;

/**
 * Convenience class to access the localized messages of this OpenCms package.<p> 
 * 
 * @author Michael Moossen 
 * 
 * @version $Revision: 1.16 $ 
 * 
 * @since 6.0.0 
 */
public final class Messages extends A_CmsMessageBundle {

    /** Message constant for key in the resource bundle. */
    public static final String ERR_ADMIN_INSUFFICIENT_RIGHTS_0 = "ERR_ADMIN_INSUFFICIENT_RIGHTS_0";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_NOT_CONFIGURED_ROOT_1 = "ERR_NOT_CONFIGURED_ROOT_1";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_VIEW_LOADING_0 = "GUI_ADMIN_VIEW_LOADING_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_VIEW_ROOT_HELP_0 = "GUI_ADMIN_VIEW_ROOT_HELP_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_VIEW_ROOT_NAME_0 = "GUI_ADMIN_VIEW_ROOT_NAME_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_ADMIN_VIEW_UPLEVEL_0 = "GUI_ADMIN_VIEW_UPLEVEL_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_CATEGORIES_RELATION_0 = "GUI_CATEGORIES_RELATION_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_COMPARE_NAVBAR_0 = "GUI_COMPARE_NAVBAR_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_COMPARE_TOOL_NAME_0 = "GUI_COMPARE_TOOL_NAME_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_DIFF_NAVBAR_0 = "GUI_DIFF_NAVBAR_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_DIFF_TOOL_NAME_0 = "GUI_DIFF_TOOL_NAME_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_EMPTY_MESSAGE_0 = "GUI_EMPTY_MESSAGE_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_EXPLORER_VIEW_ROOT_HELP_0 = "GUI_EXPLORER_VIEW_ROOT_HELP_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_EXPLORER_VIEW_ROOT_NAME_0 = "GUI_EXPLORER_VIEW_ROOT_NAME_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_HISTORY_0 = "GUI_HISTORY_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_LINK_RELATION_SOURCE_0 = "GUI_LINK_RELATION_SOURCE_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_LINK_RELATION_TARGET_0 = "GUI_LINK_RELATION_TARGET_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_OU_TYPE_NORMAL_0 = "GUI_OU_TYPE_NORMAL_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_OU_TYPE_WEBUSER_0 = "GUI_OU_TYPE_WEBUSER_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_SHOW_SIBLINGS_0 = "GUI_SHOW_SIBLINGS_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_TOOLS_DEFAULT_GROUP_0 = "GUI_TOOLS_DEFAULT_GROUP_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_TOOLS_DEFAULT_HELP_0 = "GUI_TOOLS_DEFAULT_HELP_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_TOOLS_DEFAULT_NAME_0 = "GUI_TOOLS_DEFAULT_NAME_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_TOOLS_DISABLED_HELP_0 = "GUI_TOOLS_DISABLED_HELP_0";

    /** Message constant for key in the resource bundle. */
    public static final String GUI_TOOLS_DISABLED_ONLINE_HELP_0 = "GUI_TOOLS_DISABLED_ONLINE_HELP_0";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_CREATED_0 = "INIT_TOOLMANAGER_CREATED_0";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_DUPLICATED_ERROR_3 = "INIT_TOOLMANAGER_DUPLICATED_ERROR_3";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_INCONSISTENT_PATH_2 = "INIT_TOOLMANAGER_INCONSISTENT_PATH_2";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_INSTALL_ERROR_2 = "INIT_TOOLMANAGER_INSTALL_ERROR_2";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_NEWTOOL_FOUND_2 = "INIT_TOOLMANAGER_NEWTOOL_FOUND_2";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_ROOT_SKIPPED_2 = "INIT_TOOLMANAGER_ROOT_SKIPPED_2";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_SETUP_1 = "INIT_TOOLMANAGER_SETUP_1";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_SETUP_ERROR_1 = "INIT_TOOLMANAGER_SETUP_ERROR_1";

    /** Message constant for key in the resource bundle. */
    public static final String INIT_TOOLMANAGER_TOOL_SETUP_ERROR_1 = "INIT_TOOLMANAGER_TOOL_SETUP_ERROR_1";

    /** Message constant for key in the resource bundle. */
    public static final String LOG_MISSING_ADMIN_TOOL_1 = "LOG_MISSING_ADMIN_TOOL_1";

    /** Message constant for key in the resource bundle. */
    public static final String LOG_MISSING_TOOL_HANDLER_2 = "LOG_MISSING_TOOL_HANDLER_2";

    /** Name of the used resource bundle. */
    private static final String BUNDLE_NAME = "org.opencms.workplace.tools.messages";

    /** Static instance member. */
    private static final I_CmsMessageBundle INSTANCE = new Messages();

    /**
     * Hides the public constructor for this utility class.<p>
     */
    private Messages() {
    }

    /**
     * Returns an instance of this localized message accessor.<p>
     * 
     * @return an instance of this localized message accessor
     */
    public static I_CmsMessageBundle get() {
        return INSTANCE;
    }

    /**
     * Returns the bundle name for this OpenCms package.<p>
     * 
     * @return the bundle name for this OpenCms package
     */
    public String getBundleName() {
        return BUNDLE_NAME;
    }
}
