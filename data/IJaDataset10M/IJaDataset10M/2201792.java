package org.blue.star.include;

public class common_h {

    public static String PROGRAM_VERSION = "2.7";

    public static String PROGRAM_MODIFICATION_DATE = "31-01-2007";

    /***************************** COMMANDS *********************************/
    public static final int CMD_NONE = 0;

    public static final int CMD_ADD_HOST_COMMENT = 1;

    public static final int CMD_DEL_HOST_COMMENT = 2;

    public static final int CMD_ADD_SVC_COMMENT = 3;

    public static final int CMD_DEL_SVC_COMMENT = 4;

    public static final int CMD_ENABLE_SVC_CHECK = 5;

    public static final int CMD_DISABLE_SVC_CHECK = 6;

    public static final int CMD_SCHEDULE_SVC_CHECK = 7;

    public static final int CMD_DELAY_SVC_NOTIFICATION = 9;

    public static final int CMD_DELAY_HOST_NOTIFICATION = 10;

    public static final int CMD_DISABLE_NOTIFICATIONS = 11;

    public static final int CMD_ENABLE_NOTIFICATIONS = 12;

    public static final int CMD_RESTART_PROCESS = 13;

    public static final int CMD_SHUTDOWN_PROCESS = 14;

    public static final int CMD_ENABLE_HOST_SVC_CHECKS = 15;

    public static final int CMD_DISABLE_HOST_SVC_CHECKS = 16;

    public static final int CMD_SCHEDULE_HOST_SVC_CHECKS = 17;

    public static final int CMD_DELAY_HOST_SVC_NOTIFICATIONS = 19;

    public static final int CMD_DEL_ALL_HOST_COMMENTS = 20;

    public static final int CMD_DEL_ALL_SVC_COMMENTS = 21;

    public static final int CMD_ENABLE_SVC_NOTIFICATIONS = 22;

    public static final int CMD_DISABLE_SVC_NOTIFICATIONS = 23;

    public static final int CMD_ENABLE_HOST_NOTIFICATIONS = 24;

    public static final int CMD_DISABLE_HOST_NOTIFICATIONS = 25;

    public static final int CMD_ENABLE_ALL_NOTIFICATIONS_BEYOND_HOST = 26;

    public static final int CMD_DISABLE_ALL_NOTIFICATIONS_BEYOND_HOST = 27;

    public static final int CMD_ENABLE_HOST_SVC_NOTIFICATIONS = 28;

    public static final int CMD_DISABLE_HOST_SVC_NOTIFICATIONS = 29;

    public static final int CMD_PROCESS_SERVICE_CHECK_RESULT = 30;

    public static final int CMD_SAVE_STATE_INFORMATION = 31;

    public static final int CMD_READ_STATE_INFORMATION = 32;

    public static final int CMD_ACKNOWLEDGE_HOST_PROBLEM = 33;

    public static final int CMD_ACKNOWLEDGE_SVC_PROBLEM = 34;

    public static final int CMD_START_EXECUTING_SVC_CHECKS = 35;

    public static final int CMD_STOP_EXECUTING_SVC_CHECKS = 36;

    public static final int CMD_START_ACCEPTING_PASSIVE_SVC_CHECKS = 37;

    public static final int CMD_STOP_ACCEPTING_PASSIVE_SVC_CHECKS = 38;

    public static final int CMD_ENABLE_PASSIVE_SVC_CHECKS = 39;

    public static final int CMD_DISABLE_PASSIVE_SVC_CHECKS = 40;

    public static final int CMD_ENABLE_EVENT_HANDLERS = 41;

    public static final int CMD_DISABLE_EVENT_HANDLERS = 42;

    public static final int CMD_ENABLE_HOST_EVENT_HANDLER = 43;

    public static final int CMD_DISABLE_HOST_EVENT_HANDLER = 44;

    public static final int CMD_ENABLE_SVC_EVENT_HANDLER = 45;

    public static final int CMD_DISABLE_SVC_EVENT_HANDLER = 46;

    public static final int CMD_ENABLE_HOST_CHECK = 47;

    public static final int CMD_DISABLE_HOST_CHECK = 48;

    public static final int CMD_START_OBSESSING_OVER_SVC_CHECKS = 49;

    public static final int CMD_STOP_OBSESSING_OVER_SVC_CHECKS = 50;

    public static final int CMD_REMOVE_HOST_ACKNOWLEDGEMENT = 51;

    public static final int CMD_REMOVE_SVC_ACKNOWLEDGEMENT = 52;

    public static final int CMD_SCHEDULE_FORCED_HOST_SVC_CHECKS = 53;

    public static final int CMD_SCHEDULE_FORCED_SVC_CHECK = 54;

    public static final int CMD_SCHEDULE_HOST_DOWNTIME = 55;

    public static final int CMD_SCHEDULE_SVC_DOWNTIME = 56;

    public static final int CMD_ENABLE_HOST_FLAP_DETECTION = 57;

    public static final int CMD_DISABLE_HOST_FLAP_DETECTION = 58;

    public static final int CMD_ENABLE_SVC_FLAP_DETECTION = 59;

    public static final int CMD_DISABLE_SVC_FLAP_DETECTION = 60;

    public static final int CMD_ENABLE_FLAP_DETECTION = 61;

    public static final int CMD_DISABLE_FLAP_DETECTION = 62;

    public static final int CMD_ENABLE_HOSTGROUP_SVC_NOTIFICATIONS = 63;

    public static final int CMD_DISABLE_HOSTGROUP_SVC_NOTIFICATIONS = 64;

    public static final int CMD_ENABLE_HOSTGROUP_HOST_NOTIFICATIONS = 65;

    public static final int CMD_DISABLE_HOSTGROUP_HOST_NOTIFICATIONS = 66;

    public static final int CMD_ENABLE_HOSTGROUP_SVC_CHECKS = 67;

    public static final int CMD_DISABLE_HOSTGROUP_SVC_CHECKS = 68;

    public static final int CMD_CANCEL_HOST_DOWNTIME = 69;

    public static final int CMD_CANCEL_SVC_DOWNTIME = 70;

    public static final int CMD_CANCEL_ACTIVE_HOST_DOWNTIME = 71;

    public static final int CMD_CANCEL_PENDING_HOST_DOWNTIME = 72;

    public static final int CMD_CANCEL_ACTIVE_SVC_DOWNTIME = 73;

    public static final int CMD_CANCEL_PENDING_SVC_DOWNTIME = 74;

    public static final int CMD_CANCEL_ACTIVE_HOST_SVC_DOWNTIME = 75;

    public static final int CMD_CANCEL_PENDING_HOST_SVC_DOWNTIME = 76;

    public static final int CMD_FLUSH_PENDING_COMMANDS = 77;

    public static final int CMD_DEL_HOST_DOWNTIME = 78;

    public static final int CMD_DEL_SVC_DOWNTIME = 79;

    public static final int CMD_ENABLE_FAILURE_PREDICTION = 80;

    public static final int CMD_DISABLE_FAILURE_PREDICTION = 81;

    public static final int CMD_ENABLE_PERFORMANCE_DATA = 82;

    public static final int CMD_DISABLE_PERFORMANCE_DATA = 83;

    public static final int CMD_SCHEDULE_HOSTGROUP_HOST_DOWNTIME = 84;

    public static final int CMD_SCHEDULE_HOSTGROUP_SVC_DOWNTIME = 85;

    public static final int CMD_SCHEDULE_HOST_SVC_DOWNTIME = 86;

    public static final int CMD_PROCESS_HOST_CHECK_RESULT = 87;

    public static final int CMD_START_EXECUTING_HOST_CHECKS = 88;

    public static final int CMD_STOP_EXECUTING_HOST_CHECKS = 89;

    public static final int CMD_START_ACCEPTING_PASSIVE_HOST_CHECKS = 90;

    public static final int CMD_STOP_ACCEPTING_PASSIVE_HOST_CHECKS = 91;

    public static final int CMD_ENABLE_PASSIVE_HOST_CHECKS = 92;

    public static final int CMD_DISABLE_PASSIVE_HOST_CHECKS = 93;

    public static final int CMD_START_OBSESSING_OVER_HOST_CHECKS = 94;

    public static final int CMD_STOP_OBSESSING_OVER_HOST_CHECKS = 95;

    public static final int CMD_SCHEDULE_HOST_CHECK = 96;

    public static final int CMD_SCHEDULE_FORCED_HOST_CHECK = 98;

    public static final int CMD_START_OBSESSING_OVER_SVC = 99;

    public static final int CMD_STOP_OBSESSING_OVER_SVC = 100;

    public static final int CMD_START_OBSESSING_OVER_HOST = 101;

    public static final int CMD_STOP_OBSESSING_OVER_HOST = 102;

    public static final int CMD_ENABLE_HOSTGROUP_HOST_CHECKS = 103;

    public static final int CMD_DISABLE_HOSTGROUP_HOST_CHECKS = 104;

    public static final int CMD_ENABLE_HOSTGROUP_PASSIVE_SVC_CHECKS = 105;

    public static final int CMD_DISABLE_HOSTGROUP_PASSIVE_SVC_CHECKS = 106;

    public static final int CMD_ENABLE_HOSTGROUP_PASSIVE_HOST_CHECKS = 107;

    public static final int CMD_DISABLE_HOSTGROUP_PASSIVE_HOST_CHECKS = 108;

    public static final int CMD_ENABLE_SERVICEGROUP_SVC_NOTIFICATIONS = 109;

    public static final int CMD_DISABLE_SERVICEGROUP_SVC_NOTIFICATIONS = 110;

    public static final int CMD_ENABLE_SERVICEGROUP_HOST_NOTIFICATIONS = 111;

    public static final int CMD_DISABLE_SERVICEGROUP_HOST_NOTIFICATIONS = 112;

    public static final int CMD_ENABLE_SERVICEGROUP_SVC_CHECKS = 113;

    public static final int CMD_DISABLE_SERVICEGROUP_SVC_CHECKS = 114;

    public static final int CMD_ENABLE_SERVICEGROUP_HOST_CHECKS = 115;

    public static final int CMD_DISABLE_SERVICEGROUP_HOST_CHECKS = 116;

    public static final int CMD_ENABLE_SERVICEGROUP_PASSIVE_SVC_CHECKS = 117;

    public static final int CMD_DISABLE_SERVICEGROUP_PASSIVE_SVC_CHECKS = 118;

    public static final int CMD_ENABLE_SERVICEGROUP_PASSIVE_HOST_CHECKS = 119;

    public static final int CMD_DISABLE_SERVICEGROUP_PASSIVE_HOST_CHECKS = 120;

    public static final int CMD_SCHEDULE_SERVICEGROUP_HOST_DOWNTIME = 121;

    public static final int CMD_SCHEDULE_SERVICEGROUP_SVC_DOWNTIME = 122;

    public static final int CMD_CHANGE_GLOBAL_HOST_EVENT_HANDLER = 123;

    public static final int CMD_CHANGE_GLOBAL_SVC_EVENT_HANDLER = 124;

    public static final int CMD_CHANGE_HOST_EVENT_HANDLER = 125;

    public static final int CMD_CHANGE_SVC_EVENT_HANDLER = 126;

    public static final int CMD_CHANGE_HOST_CHECK_COMMAND = 127;

    public static final int CMD_CHANGE_SVC_CHECK_COMMAND = 128;

    public static final int CMD_CHANGE_NORMAL_HOST_CHECK_INTERVAL = 129;

    public static final int CMD_CHANGE_NORMAL_SVC_CHECK_INTERVAL = 130;

    public static final int CMD_CHANGE_RETRY_SVC_CHECK_INTERVAL = 131;

    public static final int CMD_CHANGE_MAX_HOST_CHECK_ATTEMPTS = 132;

    public static final int CMD_CHANGE_MAX_SVC_CHECK_ATTEMPTS = 133;

    public static final int CMD_SCHEDULE_AND_PROPAGATE_TRIGGERED_HOST_DOWNTIME = 134;

    public static final int CMD_ENABLE_HOST_AND_CHILD_NOTIFICATIONS = 135;

    public static final int CMD_DISABLE_HOST_AND_CHILD_NOTIFICATIONS = 136;

    public static final int CMD_SCHEDULE_AND_PROPAGATE_HOST_DOWNTIME = 137;

    public static final int CMD_ENABLE_SERVICE_FRESHNESS_CHECKS = 138;

    public static final int CMD_DISABLE_SERVICE_FRESHNESS_CHECKS = 139;

    public static final int CMD_ENABLE_HOST_FRESHNESS_CHECKS = 140;

    public static final int CMD_DISABLE_HOST_FRESHNESS_CHECKS = 141;

    public static final int CMD_SET_HOST_NOTIFICATION_NUMBER = 142;

    public static final int CMD_SET_SVC_NOTIFICATION_NUMBER = 143;

    /** Commands added for the dynamic inventory system **/
    public static final int CMD_JAVA_COMMAND = 1776;

    /************************ SERVICE CHECK TYPES ****************************/
    public static final int SERVICE_CHECK_ACTIVE = 0;

    public static final int SERVICE_CHECK_PASSIVE = 1;

    /************************** HOST CHECK TYPES *****************************/
    public static final int HOST_CHECK_ACTIVE = 0;

    public static final int HOST_CHECK_PASSIVE = 1;

    /************************ SERVICE STATE TYPES ****************************/
    public static final int SOFT_STATE = 0;

    public static final int HARD_STATE = 1;

    /************************* SCHEDULED DOWNTIME TYPES **********************/
    public static final int SERVICE_DOWNTIME = 1;

    public static final int HOST_DOWNTIME = 2;

    public static final int ANY_DOWNTIME = 3;

    /************************** ACKNOWLEDGEMENT TYPES ************************/
    public static final int HOST_ACKNOWLEDGEMENT = 0;

    public static final int SERVICE_ACKNOWLEDGEMENT = 1;

    public static final int ACKNOWLEDGEMENT_NONE = 0;

    public static final int ACKNOWLEDGEMENT_NORMAL = 1;

    public static final int ACKNOWLEDGEMENT_STICKY = 2;

    /**************************** DEPENDENCY TYPES ***************************/
    public static final int NOTIFICATION_DEPENDENCY = 1;

    public static final int EXECUTION_DEPENDENCY = 2;

    /**************************** PROGRAM MODES ******************************/
    public static final int STANDBY_MODE = 0;

    public static final int ACTIVE_MODE = 1;

    /************************** LOG ROTATION MODES ***************************/
    public static final int LOG_ROTATION_NONE = 0;

    public static final int LOG_ROTATION_HOURLY = 1;

    public static final int LOG_ROTATION_DAILY = 2;

    public static final int LOG_ROTATION_WEEKLY = 3;

    public static final int LOG_ROTATION_MONTHLY = 4;

    /***************************** LOG VERSIONS ******************************/
    public static String LOG_VERSION_1 = "1.0";

    public static String LOG_VERSION_2 = "2.0";

    /************************* GENERAL DEFINITIONS  **************************/
    public static final int OK = 0;

    public static final int ERROR = -2;

    public static final int TRUE = 1;

    public static final int FALSE = 0;

    /****************** HOST CONFIG FILE READING OPTIONS ********************/
    public static final int READ_HOSTS = 1;

    public static final int READ_HOSTGROUPS = 2;

    public static final int READ_CONTACTS = 4;

    public static final int READ_CONTACTGROUPS = 8;

    public static final int READ_SERVICES = 16;

    public static final int READ_COMMANDS = 32;

    public static final int READ_TIMEPERIODS = 64;

    public static final int READ_SERVICEESCALATIONS = 128;

    public static final int READ_HOSTGROUPESCALATIONS = 256;

    public static final int READ_SERVICEDEPENDENCIES = 512;

    public static final int READ_HOSTDEPENDENCIES = 1024;

    public static final int READ_HOSTESCALATIONS = 2048;

    public static final int READ_HOSTEXTINFO = 4096;

    public static final int READ_SERVICEEXTINFO = 8192;

    public static final int READ_SERVICEGROUPS = 16384;

    public static final int READ_ALL_OBJECT_DATA = READ_HOSTS | READ_HOSTGROUPS | READ_CONTACTS | READ_CONTACTGROUPS | READ_SERVICES | READ_COMMANDS | READ_TIMEPERIODS | READ_SERVICEESCALATIONS | READ_SERVICEDEPENDENCIES | READ_HOSTDEPENDENCIES | READ_HOSTESCALATIONS | READ_HOSTEXTINFO | READ_SERVICEEXTINFO | READ_SERVICEGROUPS;

    /************************** DATE/TIME TYPES *****************************/
    public static final int LONG_DATE_TIME = 0;

    public static final int SHORT_DATE_TIME = 1;

    public static final int SHORT_DATE = 2;

    public static final int SHORT_TIME = 3;

    public static final int HTTP_DATE_TIME = 4;

    /**************************** DATE FORMATS ******************************/
    public static final int DATE_FORMAT_US = 0;

    public static final int DATE_FORMAT_EURO = 1;

    public static final int DATE_FORMAT_ISO8601 = 2;

    public static final int DATE_FORMAT_STRICT_ISO8601 = 3;

    /************************** MISC DEFINITIONS ****************************/
    public static final int MAX_FILENAME_LENGTH = 256;

    public static final int MAX_INPUT_BUFFER = 1024;

    public static final int MAX_COMMAND_BUFFER = 8192;

    public static final int MAX_DATETIME_LENGTH = 48;

    /************************* MODIFIED ATTRIBUTES **************************/
    public static final int MODATTR_NONE = 0;

    public static final int MODATTR_NOTIFICATIONS_ENABLED = 1;

    public static final int MODATTR_ACTIVE_CHECKS_ENABLED = 2;

    public static final int MODATTR_PASSIVE_CHECKS_ENABLED = 4;

    public static final int MODATTR_EVENT_HANDLER_ENABLED = 8;

    public static final int MODATTR_FLAP_DETECTION_ENABLED = 16;

    public static final int MODATTR_FAILURE_PREDICTION_ENABLED = 32;

    public static final int MODATTR_PERFORMANCE_DATA_ENABLED = 64;

    public static final int MODATTR_OBSESSIVE_HANDLER_ENABLED = 128;

    public static final int MODATTR_EVENT_HANDLER_COMMAND = 256;

    public static final int MODATTR_CHECK_COMMAND = 512;

    public static final int MODATTR_NORMAL_CHECK_INTERVAL = 1024;

    public static final int MODATTR_RETRY_CHECK_INTERVAL = 2048;

    public static final int MODATTR_MAX_CHECK_ATTEMPTS = 4096;

    public static final int MODATTR_FRESHNESS_CHECKS_ENABLED = 8192;
}
