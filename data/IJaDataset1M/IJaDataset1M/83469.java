package onepoint.project.util;

import java.awt.Image;
import onepoint.express.server.XExpressService;
import onepoint.project.OpInitializer;
import onepoint.project.forms.OpErrorFormProvider;

/**
 * Class used for holding constants used in more than one place and specific to the application.
 *
 * @author horia.chiorean
 */
public interface OpProjectConstants {

    public static final String RUN_LEVEL = "runLevel";

    public static final String START_FORM = "startForm";

    public static final String START_FORM_PARAMETERS = "startFormParameters";

    public static final String AUTO_LOGIN_START_FORM = "autoLoginStartForm";

    public static final String PARAMETERS = XExpressService.PARAMETERS;

    public static final String GET_RUN_LEVEL_ACTION = "GetRunLevel";

    public static final String CONFIGURATION_WIZARD_FORM = "/modules/configuration_wizard/forms/configuration_wizard.oxf.xml";

    public static final String LOCK_FILE_EXISTS_FORM = "/modules/setup/forms/lock_file_confirmation.oxf.xml";

    public static final String OP_HOME_NOT_SET_FORM = "/modules/setup/forms/op_home_not_set_confirmation.oxf.xml";

    public static final String CHILDREN = "children";

    public static final String DUMMY_ROW_ID = "DummyChildId";

    public static final String REFRESH_PARAM = "refresh";

    public static final String PROJECT_PACKAGE = "onepoint/project";

    public static final String SIGNON_ACTION = "UserService.signOn";

    public static final String SIGNOFF_ACTION = "UserService.signOff";

    public static final String GET_INIT_PARAMS_ACTION = "SetupService.getInitParams";

    public static final String REMEMBER_PARAM = "remember";

    public static final String LOGIN_PARAM = "login";

    public static final String PASSWORD_PARAM = "password";

    public static final String CLIENT_TIMEZONE = "clientTimeZone";

    public static final String CLIENT_JVM_OPTIONS = "java_arguments";

    public static final String ERROR_MESSAGE = OpErrorFormProvider.TEXT_MESSAGE;

    public static final String ERROR_MESSAGE_ID = OpErrorFormProvider.ERROR_TEXT_ID;

    public static final String WIDTH = "width";

    public static final String HEIGHT = "height";

    /**
    * General application flavour codes.
    */
    public static final String BASIC_EDITION_CODE = "OPPBD";

    public static final String PROFESSIONAL_EDITION_CODE = "OPPPD";

    public static final String OPEN_EDITION_CODE = "OPPCS";

    public static final String TEAM_EDITION_CODE = "OPPES";

    public static final String NETWORK_EDITION_CODE = "OPPGS";

    public static final String ON_DEMAND_EDITION_CODE = "OPPEC";

    public static final String MASTER_EDITION_CODE = "OPPMD";

    /**
    * The code version number.
    */
    public static final String CODE_VERSION_MAJOR_NUMBER = "10";

    public static final String CODE_VERSION_MINOR_NUMBER = "0";

    public static final String CODE_VERSION_NUMBER = CODE_VERSION_MAJOR_NUMBER + "." + CODE_VERSION_MINOR_NUMBER;

    public static final String CALENDAR = "calendar";

    /**
    * HSQL BD folder name.
    */
    public static final String DB_DIR_NAME = "repository";

    /**
    * HSQL BD file name.
    */
    public static final String DB_FILE_NAME = "onepoint";

    /**
    * Backup/restore folder name.
    */
    public static final String BACKUP_DIR_NAME = "backup";

    /**
    * Constants needed for attachment management
    */
    public static final String LINKED_ATTACHMENT_DESCRIPTOR = "u";

    public static final String DOCUMENT_ATTACHMENT_DESCRIPTOR = "d";

    public static final String NO_CONTENT_ID = "0";

    /**
    * Run Levels.
    */
    public static final byte CONFIGURATION_WIZARD_REQUIRED_RUN_LEVEL = 0;

    public static final byte COULD_NOT_LOAD_REGISTERED_MODULES_RUN_LEVEL = 1;

    public static final byte DATABASE_NOT_CONFIGURED_RUN_LEVEL = 2;

    public static final byte DATABASE_NOT_SET_UP_RUN_LEVEL = 3;

    public static final byte COULD_NOT_UPDATE_SCHEMA_RUN_LEVEL = 4;

    public static final byte COULD_NOT_START_REGISTERED_MODULES_RUN_LEVEL = 5;

    public static final byte LOCK_FILE_EXISTS_RUN_LEVEL = 6;

    public static final byte SERVER_NOT_ACCESSIBLE_RUN_LEVEL = 7;

    public static final byte COULD_NOT_SET_ONEPOINT_HOME_RUN_LEVEL = 8;

    public static final byte SUCCESS_RUN_LEVEL = 127;

    public static final String RUN_LEVEL_ERROR_FORM = "/forms/runLevel.oxf.xml";

    public static final String INIT_PARAMS = "initParams";

    String PROJECT_ID = "project_id";

    /**
    * User level types
    */
    public static final byte OBSERVER_CUSTOMER_USER_LEVEL = -1;

    public static final byte OBSERVER_USER_LEVEL = 0;

    public static final byte CONTRIBUTOR_USER_LEVEL = 1;

    public static final byte MANAGER_USER_LEVEL = 2;

    public static final byte SYSTEM_USER_LEVEL = 3;

    public static final String CATEGORY_NAME = "category";

    public static final byte[] USER_LEVELS = { OBSERVER_CUSTOMER_USER_LEVEL, OBSERVER_USER_LEVEL, CONTRIBUTOR_USER_LEVEL, MANAGER_USER_LEVEL };

    public static String OP_OBJECT_TABLE_NAME = "op_object";

    public static String OP_PERMISSION_TABLE_NAME = "op_permission";

    public static String CONTENT_LOCATOR_REG_EX = "OpContent.\\d{1,}?.xid";

    public static final Image GREEN_TRAFFIC_LIGHT = ImageUtil.getImageWithTransparency(OpInitializer.class.getResource("icons/leds-green-130px.png"));

    public static final Image ORANGE_TRAFFIC_LIGHT = ImageUtil.getImageWithTransparency(OpInitializer.class.getResource("icons/leds-orange-130px.png"));

    public static final Image RED_TRAFFIC_LIGHT = ImageUtil.getImageWithTransparency(OpInitializer.class.getResource("icons/leds-red-130px.png"));

    public static final String INDENT = "    ";

    public static final String ROOT_RESOURCE_POOL_NAME = "${RootResourcePoolName}";

    public static final String ROOT_RESOURCE_POOL_DESCRIPTION = "${RootResourcePoolDescription}";

    public static final String ROOT_RESOURCE_POOL_ID_QUERY = "select pool.id from OpResourcePool as pool where pool.Name = '" + ROOT_RESOURCE_POOL_NAME + "'";

    public static final int BULK_COMMIT_INTERVAL = 1000;
}
