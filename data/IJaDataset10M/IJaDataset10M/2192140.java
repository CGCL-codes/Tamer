package com.funambol.syncclient.google.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Provide methods to dictionary language
 *
 * @version $Id: Language.java,v 1.0 2007/06/20 16:19:47 Paulo F. Exp $
 */
public class Language {

    public static final String LANGUAGE_FILE = "language/language";

    public static final Locale LANGUAGE_LOCALE = Locale.US;

    public static final String LABEL_TITLE_MAINWINDOW = "LABEL_TITLE_MAINWINDOW";

    public static final String MENU_FILE = "MENU_FILE";

    public static final String MENU_FILE_SYNC = "MENU_FILE_SYNC";

    public static final String MENU_FILE_EXIT = "MENU_FILE_EXIT";

    public static final String MENU_EDIT = "MENU_EDIT";

    public static final String MENU_EDIT_COMMUNICATION_SET = "MENU_EDIT_COMMUNICATION_SET";

    public static final String MENU_EDIT_SYNC_SET = "MENU_EDIT_SYNC_SET";

    public static final String MENU_HELP = "MENU_HELP";

    public static final String MENU_HELP_ABOUT = "MENU_HELP_ABOUT";

    public static final String MENU_HELP_LOG = "MENU_HELP_LOG";

    public static final String LABEL_TITLE_ABOUT = "LABEL_TITLE_ABOUT";

    public static final String LABEL_ABOUT_GOOGLE_VERSION = "LABEL_ABOUT_GOOGLE_VERSION";

    public static final String LABEL_ABOUT_COPYRIGHT = "LABEL_ABOUT_COPYRIGHT";

    public static final String LABEL_ABOUT_RIGHTS = "LABEL_ABOUT_RIGHTS";

    public static final String LABEL_ABOUT_LINK = "LABEL_ABOUT_LINK";

    public static final String LABEL_TITLE_SYNCSETWINDOW = "LABEL_TITLE_SYNCSETWINDOW";

    public static final String LABEL_SYNCSET_CONTACTS = "LABEL_SYNCSET_CONTACTS";

    public static final String LABEL_SYNCSET_CALENDARS = "LABEL_SYNCSET_CALENDARS";

    public static final String LABEL_SYNCSET_OTHER = "LABEL_SYNCSET_OTHER";

    public static final String LABEL_SYNCSET_LOGLEVEL = "LABEL_SYNCSET_LOGLEVEL";

    public static final String LABEL_SYNCSET_LOGNONE = "LABEL_SYNCSET_LOGNONE";

    public static final String LABEL_SYNCSET_LOGINFO = "LABEL_SYNCSET_LOGINFO";

    public static final String LABEL_SYNCSET_LOGDEBUG = "LABEL_SYNCSET_LOGDEBUG";

    public static final String LABEL_TITLE_COMMUNICATION = "LABEL_TITLE_COMMUNICATION";

    public static final String LABEL_COMM_SERVERURL = "LABEL_COMM_SERVERURL";

    public static final String LABEL_COMM_USERNAME = "LABEL_COMM_USERNAME";

    public static final String LABEL_COMM_PASSWORD = "LABEL_COMM_PASSWORD";

    public static final String LABEL_GOOGLE_USERNAME = "LABEL_GOOGLE_USERNAME";

    public static final String LABEL_GOOGLE_PASSWORD = "LABEL_GOOGLE_PASSWORD";

    public static final String LABEL_COMM_DEVICEID = "LABEL_COMM_DEVICEID";

    public static final String LABEL_GOOGLE_DAYSAFTER = "LABEL_GOOGLE_DAYSAFTER";

    public static final String LABEL_GOOGLE_DAYSBEFORE = "LABEL_GOOGLE_DAYSBEFORE";

    public static final String LABEL_SYNC_INTERVAL = "LABEL_SYNC_INTERVAL";

    public static final String GOOGLE_INCORRECT_LOGIN_SETTINS = "GOOGLE_INCORRECT_LOGIN_SETTINS";

    public static final String SYNCML_INCORRECT_LOGIN_SETTINS = "SYNCML_INCORRECT_LOGIN_SETTINS";

    public static final String LABEL_GOOGLE_SETTINGS = "LABEL_GOOGLE_SETTINGS";

    public static final String LABEL_GOOGLE_CREDENTIALS = "LABEL_GOOGLE_CREDENTIALS";

    public static final String LABEL_TITLE_REMOTESET = "LABEL_TITLE_REMOTESET";

    public static final String LABEL_REMOTE_CONTACT = "LABEL_REMOTE_CONTACT";

    public static final String LABEL_REMOTE_CALENDAR = "LABEL_REMOTE_CALENDAR";

    public static final String LABEL_TITLE_LOGWINDOW = "LABEL_TITLE_LOGWINDOW";

    public static final String BT_OK = "BT_OK";

    public static final String BT_CANCEL = "BT_CANCEL";

    public static final String BT_SYNC = "BT_SYNC";

    public static final String BT_CLOSE = "BT_CLOSE";

    public static final String BT_REMOTE_SET = "BT_REMOTE_SET";

    public static final String LOGGING_STOPPED = "LOGGING_STOPPED";

    public static final String LOG_SYNC_BEGIN = "LOG_SYNC_BEGIN";

    public static final String LOG_SYNCSOURCE_BEGIN = "LOG_SYNCSOURCE_BEGIN";

    public static final String LOG_SYNCSOURCE_SOURCEURI = "LOG_SYNCSOURCE_SOURCEURI";

    public static final String LOG_SYNCSOURCE_SYNCMODE = "LOG_SYNCSOURCE_SYNCMODE";

    public static final String LOG_SYNC_END = "LOG_SYNC_END";

    public static final String LOG_SYNCSOURCE_END = "LOG_SYNCSOURCE_END";

    public static final String LOG_SYNC_ERROR = "LOG_SYNC_ERROR";

    public static final String LOG_SYNCERROR_MSG = "LOG_SYNCERROR_MSG";

    public static final String LOG_SYNCCAUSE_MSG = "LOG_SYNCCAUSE_MSG";

    public static final String LOG_SYNC_SENDINIT = "LOG_SYNC_SENDINIT";

    public static final String LOG_SYNC_SENDMOD = "LOG_SYNC_SENDMOD";

    public static final String LOG_SYNC_SENDFINAL = "LOG_SYNC_SENDFINAL";

    public static final String LOG_SYNCTRANSPORT_SENDDATABEGIN = "LOG_SYNCTRANSPORT_SENDDATABEGIN";

    public static final String LOG_SYNCTRANSPORT_SENDDATAEND = "LOG_SYNCTRANSPORT_SENDDATAEND";

    public static final String LOG_SYNCTRANSPORT_RECEIVEDATABEGIN = "LOG_SYNCTRANSPORT_RECEIVEDATABEGIN";

    public static final String LOG_SYNCTRANSPORT_RECEIVEDATA = "LOG_SYNCTRANSPORT_RECEIVEDATA";

    public static final String LOG_SYNCTRANSPORT_RECEIVEDATAEND = "LOG_SYNCTRANSPORT_RECEIVEDATAEND";

    public static final String LOG_SYNCITEM_ADDSERVER = "LOG_SYNCITEM_ADDSERVER";

    public static final String LOG_SYNCITEM_KEY = "LOG_SYNCITEM_KEY";

    public static final String LOG_SYNCITEM_DELETESERVER = "LOG_SYNCITEM_DELETESERVER";

    public static final String LOG_SYNCITEM_UPDATESERVER = "LOG_SYNCITEM_UPDATESERVER";

    public static final String LOG_SYNCITEM_ADDCLIENT = "LOG_SYNCITEM_ADDCLIENT";

    public static final String LOG_SYNCITEM_DELETECLIENT = "LOG_SYNCITEM_DELETECLIENT";

    public static final String LOG_SYNCITEM_UPDATECLIENT = "LOG_SYNCITEM_UPDATECLIENT";

    public static final String LOG_SYNCSTATUS_SEND = "LOG_SYNCSTATUS_SEND";

    public static final String LOG_SYNCSTATUS_CMD = "LOG_SYNCSTATUS_CMD";

    public static final String LOG_SYNCSTATUS_STATUS = "LOG_SYNCSTATUS_STATUS";

    public static final String LOG_SYNCSTATUS_RECEIVED = "LOG_SYNCSTATUS_RECEIVED";

    public static final String STATUS_SYNC_BEGIN = "STATUS_SYNC_BEGIN";

    public static final String STATUS_SYNC_END = "STATUS_SYNC_END";

    public static final String STATUS_NOT_SYNC_REQUIRED = "STATUS_NOT_SYNC_REQUIRED";

    public static final String ERROR_CONNECT = "ERROR_CONNECT";

    public static final String ERROR_AUTH = "ERROR_AUTH";

    public static final String ERROR_SYNC = "ERROR_SYNC";

    public static final String ERROR_NOT_FOUNT_DM_VALUES = "ERROR_NOT_FOUNT_DM_VALUES";

    public static final String ERROR_DEVICE_MANAGER = "ERROR_DEVICE_MANAGER";

    public static final String ERROR_WRITE_SYNCSET = "ERROR_WRITE_SYNCSET";

    public static final String ERROR_WRITE_REMOTESET = "ERROR_WRITE_REMOTESET";

    public static final String ERROR_WRITE_COMMUNICATIONSET = "ERROR_WRITE_COMMUNICATIONSET";

    public static final String ERROR_SERVER_GENERIC = "ERROR_SERVER_GENERIC";

    private static ResourceBundle rb = null;

    /**
     * Initialize ResourceBundle.
     */
    public static void init() {
        rb = ResourceBundle.getBundle(LANGUAGE_FILE, LANGUAGE_LOCALE);
    }

    /**
     * Return the message to show in the panels.
     *
     * @param key the key of the message
     * @return String the message
     */
    public static String getMessage(String key) {
        return rb.getString(key);
    }
}
