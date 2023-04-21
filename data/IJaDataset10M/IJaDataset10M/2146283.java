package model.configuration;

import javax.swing.UIManager;

public class IPMonitorProperties {

    public static String MAINVIEW_LOCATION_X = "MainViewLocationX";

    public static String MAINVIEW_LOCATION_Y = "MainViewLocationY";

    public static String MAINVIEW_SIZE_X = "MainViewSizeX";

    public static String MAINVIEW_SIZE_Y = "MainViewSizeY";

    public static String OPTIONS_MONITOR_INTERVAL = "OptionsMonitorInterval";

    public static int OPTIONS_MONITOR_INTERVAL_VALUE = 900;

    public static String OPTIONS_MONITOR_AUTOSTART = "OptionsMonitorAutoStart";

    public static boolean OPTIONS_MONITOR_AUTOSTART_VALUE = false;

    public static String OPTIONS_MONITOR_URL = "OptionsMonitorURL";

    public static String OPTIONS_MONITOR_URL_VALUE = "http://checkip.dyndns.org";

    public static String OPTIONS_MONITOR_REGULAR_EXPRESSION = "OptionsMonitorRegularExpression";

    public static String OPTIONS_MONITOR_REGULAR_EXPRESSION_VALUE = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

    public static String OPTIONS_NOTIFICATION_AUDIO = "OptionsNotificationAudio";

    public static boolean OPTIONS_NOTIFICATION_AUDIO_VALUE = false;

    public static String OPTIONS_NOTIFICATION_MAIL = "OptionsNotificationMail";

    public static boolean OPTIONS_NOTIFICATION_MAIL_VALUE = false;

    public static String OPTIONS_NOTIFICATION_VISUAL = "OptionsNotificationVisual";

    public static boolean OPTIONS_NOTIFICATION_VISUAL_VALUE = false;

    public static String OPTIONS_NOTIFICATION_COMMAND = "OptionsNotificationCommand";

    public static boolean OPTIONS_NOTIFICATION_COMMAND_VALUE = false;

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_AUDIO_PATH = "OptionsNotificationConfigurationAudioPath";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_AUDIO_PATH_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_SERVER = "OptionsNotificationConfigurationMailServer";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_SERVER_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_PORT = "OptionsNotificationConfigurationMailPort";

    public static int OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_PORT_VALUE = 25;

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_USER = "OptionsNotificationConfigurationMailUser";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_USER_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_PASSWORD = "OptionsNotificationConfigurationMailPassword";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_PASSWORD_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_AUTHENTICATION_REQUIRED = "OptionsNotificationConfigurationMailAuthenticationRequired";

    public static boolean OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_AUTHENTICATION_REQUIRED_VALUE = false;

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_USE_SSL = "OptionsNotificationConfigurationMailUseSSL";

    public static boolean OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_USE_SSL_VALUE = false;

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_FROM_NAME = "OptionsNotificationConfigurationMailFromName";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_FROM_NAME_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_FROM_ADDRESS = "OptionsNotificationConfigurationMailFromAddress";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_FROM_ADDRESS_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_TO_ADDRESSES = "OptionsNotificationConfigurationMailToAddresses";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_TO_ADDRESSES_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_SUBJECT = "OptionsNotificationConfigurationMailSubject";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_SUBJECT_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_TEXT = "OptionsNotificationConfigurationMailText";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_TEXT_VALUE = "";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_USE_HTML = "OptionsNotificationConfigurationMailUseHTML";

    public static boolean OPTIONS_NOTIFICATION_CONFIGURATION_MAIL_USE_HTML_VALUE = true;

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_VISUAL_TITLE = "OptionsNotificationConfigurationVisualTitle";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_VISUAL_TITLE_VALUE = "IP change detected";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_VISUAL_TEXT = "OptionsNotificationConfigurationVisualText";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_VISUAL_TEXT_VALUE = "From IP: %OLDIP%\nTo IP: %NEWIP%";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_VISUAL_ICON = "OptionsNotificationConfigurationVisualIcon";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_VISUAL_ICON_VALUE = "INFO";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_COMMAND_PATH = "OptionsNotificationConfigurationCommand";

    public static String OPTIONS_NOTIFICATION_CONFIGURATION_COMMAND_PATH_VALUE = "";

    public static String OPTIONS_INTERFACE_LOOK_AND_FEEL_CLASS_NAME = "OptionsInterfaceLookAndFeelClassName";

    public static String OPTIONS_INTERFACE_LOOK_AND_FEEL_CLASS_NAME_VALUE = UIManager.getCrossPlatformLookAndFeelClassName();
}
