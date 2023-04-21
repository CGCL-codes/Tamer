package com.laoer.bbscs.comm;

import java.util.*;
import com.laoer.bbscs.bean.ForumHistory;
import com.laoer.bbscs.bean.ForumMain;
import com.laoer.bbscs.web.ui.OptionsInt;
import com.laoer.bbscs.web.ui.OptionsString;

/**
 * @author laoer
 *
 */
public class Constant {

    public static final String VSERION = "8.0.2";

    public static String SERVLET_MAPPING = "*.bbscs";

    public static final String CHARSET = "UTF-8";

    public static String ROOTPATH = "";

    public static String FTL_PATH = "WEB-INF/templates/";

    public static boolean USE_URL_REWRITE = false;

    public static final boolean USE_PERMISSION_CACHE = true;

    public static final String USER_SESSION_KEY = "user_session";

    public static boolean USE_CLUSTER = false;

    public static int POST_STORAGE_MODE = 0;

    public static final int ORDER_ASC = 0;

    public static final int ORDER_DESC = 1;

    public static final String USER_PROFILE = "UserProFile.txt";

    public static final String USER_FRIEND_FILE = "UserFriendFile.txt";

    public static final String SESSION_FILE = "session.s";

    public static String BEANPERFIX = "com.laoer.bbscs.bean.";

    public static String IMG_SMALL_FILEPREFIX = "_Small";

    public static String RE = "Re:";

    public static final int ROLE_TYPE_DEFAULT = 0;

    public static final int ROLE_TYPE_BOARD = 1;

    public static final int ROLE_TYPE_USERADD = 2;

    public static final int USER_GROUP_GUEST = 1;

    public static final int USER_GROUP_REGUSER = 2;

    public static final int USER_GROUP_UNVUSER = 3;

    public static final int USER_GROUP_SUPERBM = 4;

    public static final int USER_GROUP_ADMIN = 5;

    public static final int USER_GROUP_FORBID = 6;

    public static final String GUEST_USERID = "---";

    public static final String GUEST_USERNAME = "GUEST@";

    public static final int FIND_BOARDS_BY_ORDER = 0;

    public static final int FIND_BOARDS_BY_MAINPOSTNUM = 1;

    public static final int FIND_BOARDS_BY_POSTNUM = 2;

    public static final int INDEX_STATUS_NO_INDEX = 0;

    public static final int INDEX_STATUS_INDEXED = 1;

    public static final int INDEX_STATUS_NEED_UPDTAE = 2;

    public static final int INDEX_STATUS_NEED_DEL = 3;

    public static final int INDEX_STATUS_DELED = 4;

    public static final int INDEX_STATUS_NO_INDEX_TO_DEL = 5;

    public static final int INDEX_STATUS_UPDATE_TO_DEL = 6;

    public static final int INDEX_STATUS_AUDIT = 5;

    public static final String FORUM_NEW_CACHE_NAME = "ForumNew";

    public static final String COMMEND_CACHE_NAME = "CommendSceipt";

    public static long[] BOARD_PERMISSION_GROUP_1 = { 401, 402, 403, 404, 405, 406, 408, 409, 410, 440 };

    public static long[] BOARD_PERMISSION_GROUP_2 = { 401, 402, 403, 404, 405, 406, 408, 409, 410, 414, 415, 416, 420, 421, 422, 423, 424, 425, 426, 427, 430, 431, 432, 433, 434, 435, 440, 441, 450, 460 };

    public static long[] BOARD_PERMISSION_GROUP_3 = { 401, 402, 403, 405, 406, 408, 409, 410, 414, 426, 427, 435, 440, 450 };

    public static long[] BOARD_PERMISSION_GROUP_4 = { 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 420, 421, 422, 423, 424, 425, 426, 427, 430, 431, 432, 433, 434, 435, 440, 441, 450, 451, 460, 462, 463, 464, 465, 466, 601, 602, 603, 604, 608, 609, 610 };

    public static long[] BOARD_PERMISSION_GROUP_5 = { 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 420, 421, 422, 423, 424, 425, 426, 427, 430, 431, 432, 433, 434, 435, 440, 441, 450, 451, 460, 462, 463, 464, 465, 466, 470, 471, 475, 601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611 };

    public static List<Long> BOARD_PERMISSION_GROUP_LIST_1 = new ArrayList<Long>();

    public static List<Long> BOARD_PERMISSION_GROUP_LIST_2 = new ArrayList<Long>();

    public static List<Long> BOARD_PERMISSION_GROUP_LIST_3 = new ArrayList<Long>();

    public static List<Long> BOARD_PERMISSION_GROUP_LIST_4 = new ArrayList<Long>();

    public static List<Long> BOARD_PERMISSION_GROUP_LIST_5 = new ArrayList<Long>();

    public static List<Long> BOARD_PERMISSION_GROUP_LIST_6 = new ArrayList<Long>();

    public static List<Integer> NORMAL_USER_GROUPS = new ArrayList<Integer>();

    public static List<Integer> GUEST_USER_GROUPS = new ArrayList<Integer>();

    public static List<OptionsInt> TIMEZONE = new ArrayList<OptionsInt>();

    public static List<OptionsString> USERTIMEZONE = new ArrayList<OptionsString>();

    public static List<OptionsInt> HOURS = new ArrayList<OptionsInt>();

    public static List<OptionsInt> YEAR = new ArrayList<OptionsInt>();

    public static List<OptionsString> YEARS = new ArrayList<OptionsString>();

    public static List<OptionsInt> MONTH = new ArrayList<OptionsInt>();

    public static List<OptionsInt> DAY = new ArrayList<OptionsInt>();

    public static List<OptionsString> NYEAR = new ArrayList<OptionsString>();

    public static HashMap<String, String> ICON_MAP = new HashMap<String, String>();

    public static Map<String, Class> FORUM_CLASS_MAP = new HashMap<String, Class>();

    public static String[] TITLECOLOR = { "", "#990000", "#006600", "#666600", "#000066", "#660066", "#008080", "#808080", "#CCCCCC", "#FF0000", "#00FF00", "#FFFF00", "#0000FF", "#FF00FF", "#00FFFF" };

    public static String[][] TIMEZONEVALUES = { { "(GMT -12:00) Eniwetok, Kwajalein", "GMT-12:00" }, { "(GMT -11:00) Midway Island, Samoa", "GMT-11:00" }, { "(GMT -10:00) Hawaii", "GMT-10:00" }, { "(GMT -09:00) Alaska", "GMT-09:00" }, { "(GMT -08:00) Pacific Time", "GMT-08:00" }, { "(GMT -07:00) Mountain Time", "GMT-07:00" }, { "(GMT -06:00) Central Time", "GMT-06:00" }, { "(GMT -05:00) Eastern Time", "GMT-05:00" }, { "(GMT -04:00) Atlantic Time", "GMT-04:00" }, { "(GMT -03:30) Newfoundland", "GMT-03:30" }, { "(GMT -03:00) Brassila, Buenos Aires", "GMT-03:00" }, { "(GMT -02:00) Mid-Atlantic", "GMT-02:00" }, { "(GMT -01:00) Azores, Cape Verde Islands", "GMT-01:00" }, { "(GMT) Casablanca, Dublin, London", "GMT" }, { "(GMT +01:00) Amsterdam, Berlin,Paris, Rome", "GMT+01:00" }, { "(GMT +02:00) Cairo, Helsinki", "GMT+02:00" }, { "(GMT +03:00) Baghdad, Moscow, Nairobi", "GMT+03:00" }, { "(GMT +03:30) Tehran", "GMT+03:30" }, { "(GMT +04:00) Abu Dhabi, Baku", "GMT+04:00" }, { "(GMT +04:30) Kabul", "GMT+04:30" }, { "(GMT +05:00) Ekaterinburg, Islamabad", "GMT+05:00" }, { "(GMT +05:30) Bombay, Calcutta", "GMT+05:30" }, { "(GMT +05:45) Katmandu", "GMT+05:45" }, { "(GMT +06:00) Almaty, Colombo", "GMT+06:00" }, { "(GMT +06:30) Rangoon", "GMT+06:30" }, { "(GMT +07:00) Bangkok, Hanoi, Jakarta", "GMT+07:00" }, { "(GMT +08:00) Beijing, Hong Kong, Perth", "GMT+08:00" }, { "(GMT +09:00) Osaka, Sapporo, Tokyo", "GMT+09:00" }, { "(GMT +09:30) Adelaide, Darwin", "GMT+09:30" }, { "(GMT +10:00) Canberra, Guam", "GMT+10:00" }, { "(GMT +11:00) Magadan, New Caledonia", "GMT+11:00" }, { "(GMT +12:00) Auckland, Wellington", "GMT+12:00" } };

    static {
        NORMAL_USER_GROUPS.add(new Integer(USER_GROUP_REGUSER));
        NORMAL_USER_GROUPS.add(new Integer(USER_GROUP_SUPERBM));
        NORMAL_USER_GROUPS.add(new Integer(USER_GROUP_ADMIN));
        GUEST_USER_GROUPS.add(new Integer(USER_GROUP_GUEST));
        for (int i = 0; i < BOARD_PERMISSION_GROUP_1.length; i++) {
            BOARD_PERMISSION_GROUP_LIST_1.add(new Long(BOARD_PERMISSION_GROUP_1[i]));
        }
        for (int i = 0; i < BOARD_PERMISSION_GROUP_2.length; i++) {
            BOARD_PERMISSION_GROUP_LIST_2.add(new Long(BOARD_PERMISSION_GROUP_2[i]));
        }
        for (int i = 0; i < BOARD_PERMISSION_GROUP_3.length; i++) {
            BOARD_PERMISSION_GROUP_LIST_3.add(new Long(BOARD_PERMISSION_GROUP_3[i]));
        }
        for (int i = 0; i < BOARD_PERMISSION_GROUP_4.length; i++) {
            BOARD_PERMISSION_GROUP_LIST_4.add(new Long(BOARD_PERMISSION_GROUP_4[i]));
        }
        for (int i = 0; i < BOARD_PERMISSION_GROUP_5.length; i++) {
            BOARD_PERMISSION_GROUP_LIST_5.add(new Long(BOARD_PERMISSION_GROUP_5[i]));
        }
        for (int i = 0; i < TIMEZONEVALUES.length; i++) {
            String[] values = TIMEZONEVALUES[i];
            TIMEZONE.add(new OptionsInt(i, values[0]));
            USERTIMEZONE.add(new OptionsString(values[1], values[0]));
        }
        for (int i = 0; i < 24; i++) {
            HOURS.add(new OptionsInt(i, String.valueOf(i)));
        }
        for (int i = 1930; i <= 2000; i++) {
            YEAR.add(new OptionsInt(i, String.valueOf(i)));
            YEARS.add(new OptionsString(String.valueOf(i), String.valueOf(i)));
        }
        for (int i = 2007; i <= 2010; i++) {
            NYEAR.add(new OptionsString(String.valueOf(i), String.valueOf(i)));
        }
        for (int i = 1; i <= 12; i++) {
            MONTH.add(new OptionsInt(i, String.valueOf(i)));
        }
        for (int i = 1; i <= 31; i++) {
            DAY.add(new OptionsInt(i, String.valueOf(i)));
        }
        ICON_MAP.put("ai", "ai.gif");
        ICON_MAP.put("avi", "avi.gif");
        ICON_MAP.put("bmp", "bmp.gif");
        ICON_MAP.put("cs", "cs.gif");
        ICON_MAP.put("dll", "dll.gif");
        ICON_MAP.put("doc", "doc.gif");
        ICON_MAP.put("exe", "exe.gif");
        ICON_MAP.put("fla", "fla.gif");
        ICON_MAP.put("gif", "gif.gif");
        ICON_MAP.put("htm", "htm.gif");
        ICON_MAP.put("html", "html.gif");
        ICON_MAP.put("jpg", "jpg.gif");
        ICON_MAP.put("js", "js.gif");
        ICON_MAP.put("mdb", "mdb.gif");
        ICON_MAP.put("mp3", "mp3.gif");
        ICON_MAP.put("pdf", "pdf.gif");
        ICON_MAP.put("ppt", "ppt.gif");
        ICON_MAP.put("rar", "rar.gif");
        ICON_MAP.put("rdp", "rdp.gif");
        ICON_MAP.put("swf", "swf.gif");
        ICON_MAP.put("swt", "swt.gif");
        ICON_MAP.put("txt", "txt.gif");
        ICON_MAP.put("vsd", "vsd.gif");
        ICON_MAP.put("xls", "xls.gif");
        ICON_MAP.put("xml", "xml.gif");
        ICON_MAP.put("zip", "zip.gif");
        FORUM_CLASS_MAP.put("Main", ForumMain.class);
        FORUM_CLASS_MAP.put("History", ForumHistory.class);
    }

    public static final long SPERMISSION_CAN_SEE_HIDDEN_BOARD = 901;

    public static final long SPERMISSION_CAN_USE_TITLECOLOR = 602;

    public static final long SPERMISSION_CAN_SEE_HIDDENDETAIL = 603;

    public static final long SPERMISSION_NOT_POSTCHECKTIME = 604;

    public static final long SPERMISSION_CAN_EDITPOST = 605;

    public static final long SPERMISSION_CAN_DELPOST = 606;

    public static final long SPERMISSION_CAN_DELATTACH = 607;

    public static final long SPERMISSION_INBOARD_NOT_NEEDPASSWD = 608;

    public static final long SPERMISSION_INBOARD_NOT_NEEDAUTH = 609;

    public static final long SPERMISSION_CAN_SEE_NOT_AUDITING_ATTACH = 610;

    public static final long SPERMISSION_POST_NOT_AUDITING = 611;
}
