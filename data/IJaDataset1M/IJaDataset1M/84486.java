package org.dago.wecommand;

import org.dago.common.I18N;

/**
 * wecommand localization messages implementation
 */
public final class I18NMessages extends I18N {

    public static String battery;

    public static String leds;

    public static String buttons;

    public static String connected;

    public static String disconnected;

    public static String inquiryError;

    public static String wiimoteFound;

    public static String exceptedMsgNotReceived;

    public static String wiimoteCreation;

    public static String wiimoteCreationFailed;

    public static String failToFindWiimote;

    public static String illegalMac;

    public static String msgErrorCB;

    public static String unknownMessageSent;

    public static String unknownMessageReceived;

    public static String unknownReadMemoryReport;

    public static String memoryReportZERO;

    public static String memoryReportGRAVITY;

    public static String statusReportFlags;

    public static String tooWiimotesConnected;

    public static String generalError;

    static {
        initialize("locales.org.dago.wecommand.messages", I18NMessages.class);
    }
}
