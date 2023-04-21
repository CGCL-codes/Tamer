package hambo.community;

import hambo.app.core.HamboAbstractApplication;
import hambo.app.core.HamboApplicationManager;
import hambo.config.ConfigManager;
import hambo.config.Config;
import hambo.community.chatengine.CoffeeMaker;
import com.lutris.appserver.server.Enhydra;

/**
 * Cafe Application object. Currently there is no configuration to
 * initialize for Cafe. This class is empty.
 */
public class CommunityApplication extends HamboAbstractApplication {

    public static String ROOT_DIRECTORY = null;

    public static String OWN_PICTURE_DIRECTORY = null;

    public static boolean USE_GUESTBOOK = false;

    public static boolean USE_SMS_NOTIFICATION = false;

    public static boolean USE_SMS_FORWARDING = false;

    public static boolean USE_ROOMIES_OF_THE_DAY = false;

    public static boolean USE_MESSAGES_ARCHIVE = false;

    public static boolean USE_LUD_INFO = false;

    public static boolean USE_CHAT = false;

    public static boolean USE_MYWAP_HOMEPAGE = false;

    public static boolean USE_LAST_LOGGED_IN_USERS = false;

    public static boolean USE_LIGHT_SEARCH = false;

    public static int LIFETIME_SEC = 600;

    public static int INTERVAL_SEC = 30;

    public static int MAXQUEUE_SIZE = 20;

    public static String JMSROUTER = "localhost:4001";

    public static String JMSTOPIC = "testtopic";

    public static boolean TOPDOWN = true;

    public static String SEND_SMS = "N";

    public static String RECEIVE_SMS = "Y";

    public static String NOTIFY = "N";

    public static int MAX_SMS = 0;

    public static int NUMBER_OF_NOTIFICATIONS = 0;

    public static String FORWARD_MESSAGES_TO_ICQ = "N";

    public static String REQUEST_SMS_FORWARD = "N";

    public static String IS_SETTINGS_SET_BY_USER = "N";

    public static int INBOX_RESTRICTION = 1000;

    public static int ARCHIVE_RESTRICTION = 1000;

    /**
     * Do the initialization stuff. Like reading config and
     * adding event listeners.
     * @param config configuration object for the application.
     */
    public boolean doInit(Config config) {
        Config sc = ConfigManager.getConfig("server");
        ROOT_DIRECTORY = sc.getProperty("user_doc_url");
        OWN_PICTURE_DIRECTORY = sc.getProperty("user_doc_path");
        JMSROUTER = config.getProperty("jmsrouter");
        JMSTOPIC = config.getProperty("jmstopic");
        LIFETIME_SEC = Integer.parseInt(config.getProperty("lifetimesec"));
        INTERVAL_SEC = Integer.parseInt(config.getProperty("intervalsec"));
        MAXQUEUE_SIZE = Integer.parseInt(config.getProperty("maxqueuesize"));
        TOPDOWN = (Boolean.valueOf(config.getProperty("topdown"))).booleanValue();
        USE_GUESTBOOK = (Boolean.valueOf(config.getProperty("use_guestbook"))).booleanValue();
        USE_MESSAGES_ARCHIVE = (Boolean.valueOf(config.getProperty("use_messages_archive"))).booleanValue();
        USE_SMS_NOTIFICATION = (Boolean.valueOf(config.getProperty("use_sms_notification"))).booleanValue();
        USE_SMS_FORWARDING = (Boolean.valueOf(config.getProperty("use_sms_forwarding"))).booleanValue();
        USE_ROOMIES_OF_THE_DAY = (new Boolean(config.getProperty("use_roomies_of_the_day"))).booleanValue();
        USE_LUD_INFO = (Boolean.valueOf(config.getProperty("use_lud_info"))).booleanValue();
        USE_CHAT = (Boolean.valueOf(config.getProperty("use_chat"))).booleanValue();
        USE_MYWAP_HOMEPAGE = (Boolean.valueOf(config.getProperty("use_my_waphomepage"))).booleanValue();
        USE_LAST_LOGGED_IN_USERS = (Boolean.valueOf(config.getProperty("use_last_logged_in"))).booleanValue();
        USE_LIGHT_SEARCH = (Boolean.valueOf(config.getProperty("use_light_search"))).booleanValue();
        INBOX_RESTRICTION = Integer.parseInt(config.getProperty("inbox_size"));
        ARCHIVE_RESTRICTION = Integer.parseInt(config.getProperty("archive_size"));
        SEND_SMS = config.getProperty("send_sms");
        RECEIVE_SMS = config.getProperty("receive_sms");
        NOTIFY = config.getProperty("notify");
        MAX_SMS = Integer.parseInt(config.getProperty("max_sms"));
        NUMBER_OF_NOTIFICATIONS = Integer.parseInt(config.getProperty("number_of_notifications"));
        FORWARD_MESSAGES_TO_ICQ = config.getProperty("forward_messages_to_icq");
        REQUEST_SMS_FORWARD = config.getProperty("request_sms_forward");
        IS_SETTINGS_SET_BY_USER = config.getProperty("is_settings_set_by_user");
        HamboApplicationManager man = HamboApplicationManager.getApplicationManager();
        man.addUserLogoutListener(new CommunityLogoutListener());
        CoffeeMaker.init();
        FriendsUpdater fu = new FriendsUpdater();
        Thread thrFu = new Thread(fu);
        Enhydra.register(thrFu, Enhydra.getApplication());
        thrFu.start();
        return true;
    }
}
