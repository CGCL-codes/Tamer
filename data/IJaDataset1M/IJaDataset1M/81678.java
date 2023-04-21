package org.hip.vif;

/**
 * Central provider of application's constants.
 *
 * @author Luthiger
 * Created: 25.11.2007
 */
public class ApplicationConstants {

    public static final String[][] VIF_TRANSLATIONS = { { "en", "English" }, { "de", "Deutsch" } };

    public static final String EXTENSION_POINT_ID_SKIN = "org.hip.vifapp.skins";

    public static final String EXTENSION_POINT_ID_AUTHENTICATOR = "org.hip.vifapp.authenticators";

    public static final String EXTENSION_POINT_ID_MEMBER_SEARCHERS = "org.hip.vifapp.memberSearchers";

    public static final String EXTENSION_POINT_ID_PARTLETS = "org.hip.vifapp.partlets";

    public static final String EXTENSION_POINT_ID_MEMBER_PARTLETS = "org.hip.vifapp.memberPartlets";

    public static final String EXTENSION_POINT_ID_GROUP_PARTLETS = "org.hip.vifapp.groupPartlets";

    public static final String[] EXTENSION_POINTS_PARTLETS = { EXTENSION_POINT_ID_PARTLETS, EXTENSION_POINT_ID_MEMBER_PARTLETS, EXTENSION_POINT_ID_GROUP_PARTLETS };

    public static final String EXTENSION_ELEMENT_ID_PARTLET = "partlet";

    public static final String EXTENSION_ELEMENT_ID_LOOKUP = "lookup";

    public static final String EXTENSION_ELEMENT_ID_GROUP = "group";

    public static final String DFLT_SKIN = "org.hip.vif.skin.default";

    public static final String DFLT_AUTHENTICATOR = "org.hip.vif.member.standard";

    public static final String DFLT_SEARCHER = "org.hip.vif.member.standard";

    public static final String SERVLET_ALIAS_ADMIN = "/admin";

    public static final String SERVLET_ALIAS_FORUM = "/forum";

    public static final String SERVLET_ALIAS_MENU = "/menu";

    public static final String SKIN_RESOURCES_ALIAS = "++resources++";

    public static final String LOCAL_RESOURCES_ALIAS = "+resources+";

    public static final String LOCAL_RESOURCES_DIR = "/resources";

    public static final String FRAME_NAME_HEAD = "head";

    public static final String FRAME_NAME_MENU = "menu";

    public static final String FRAME_NAME_BODY = "body";

    public static final String PARTLET_PARTLET_ID = "partlet";

    public static final String PARTLET_MENU_TASK_ID = "menuTask";

    public static final String PARTLET_TASK_BACK_ID = "taskBack";

    public static final String PARTLET_TASKSET_ID = "taskSet";

    public static final String PARTLET_TASK_ID = "task";

    public static final String PARTLET_ADDITIONAL_ID = "additionalTasks";

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
}
