package org.hip.vif.core;

/**
 * Central provider of application's constants.
 *
 * @author Luthiger
 * Created: 25.11.2007
 */
public class ApplicationConstants {

    public static final String[][] VIF_TRANSLATIONS = { { "en", "English" }, { "de", "Deutsch" } };

    public static final String DFLT_AUTHENTICATOR = "org.hip.vif.member.standard";

    public static final String DFLT_SEARCHER = "org.hip.vif.member.standard";

    public static final String PARTLET_PARTLET_ID = "partlet";

    public static final String PARTLET_MENU_TASK_ID = "menuTask";

    public static final String PARTLET_TASK_BACK_ID = "taskBack";

    public static final String PARTLET_TASKSET_ID = "taskSet";

    public static final String PARTLET_TASK_ID = "task";

    public static final String PARTLET_ADDITIONAL_ID = "additionalTasks";

    public static final String PLUGGABLE_TASK_ID = "pluggableTask";

    public static final String PLUGGABLE_PRIMARY_ID = "primaryTask";

    public static final String PLUGGABLE_SEPARATOR_ID_APPEND = "appendTo";

    public static final String PLUGGABLE_SEPARATOR_ID_INSERT_BEFORE = "insertBefore";

    public static final String PLUGGABLE_SEPARATOR_ID_INSERT_AFTER = "insertAfter";

    public static final String PLUGGABLE_SEPARATOR_NAME_ID = "markerName";

    public static final String PLUGGABLE_MENU_ID = "menuID";

    public static final String PARAMETER_APP_TYPE = "appType";

    public static final String PARAMETER_MENU_PART = "menuPart";

    public static final String PARAMETER_MENU_SET = "menuSet";

    public static final String PARAMETER_LANGUAGE = "language";

    public static final String PARAMETER_MENU_VALUE_INIT = "init";

    public static final String APP_TYPE_ADMIN = "admin";

    public static final String APP_TYPE_FORUM = "forum";

    public static final String MENU_ID_LOGOUT = "logout";

    public static final String XSL_PARAM_REQUEST_URL = "requestUrl";

    public static final String XSL_PARAM_WEBAPP_URL = "webappUrl";

    public static final String TMPLATE_LI = "<li class=\"part\">%s</li>";

    public static String PROCESSING_INSTRUCTION = "version=\"1.0\" encoding=\"UTF-8\"";

    public static String HEADER = "<?xml " + PROCESSING_INSTRUCTION + "?>";

    public static String ROOT_BEGIN = "<Root>";

    public static String ROOT_END = "</Root>";

    public static final String ROLE_ID_SU = "1";

    public static final String ROLE_ID_ADMIN = "2";

    public static final String ROLE_ID_GROUP_ADMIN = "3";

    public static final String ROLE_ID_PARTICIPANT = "4";

    public static final String ROLE_ID_MEMBER = "5";

    public static final String ROLE_ID_GUEST = "6";

    public static final String ROLE_ID_EXCL_PARTICIPANT = "7";

    public static final String DB_TYPE_DERBY = "jdbc:derby:";

    public static final String WORKSPACE_DIR = "workspace";

    public static final String KEY_REQUEST_PARAMETER = "request";

    public static final String KEY_GROUP_ID = "groupID";

    public static final String KEY_RATING_ID = "ratingID";

    public static final String PARAMETER_KEY_GENERIC = "generic_key";

    public static final String DFLT_SKIN = "org.hip.vif.default";

    public static final String DFLT_PATTERN = "yyyy/MM/dd";

    public static final String PREFERENCES_SERVICE_NAME = "preferences";

    public static final String USER_SETTINGS_LANGUAGE = "language";

    public static final int DFLT_UPLOADE_QUOTA = 50;

    public static final int DFLT_REQUEST_LATENCY = 3;
}
