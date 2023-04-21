package com.shenming.sms.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Enumeration;

/**
 * Usage of extends Tool.java
 *   public class TestTool extends Tool{
 *   	static{
 *   		TestTool.setRbFromPath("com.test.util.resources.application");
 *   	}
 *   	
 *   	public static void initialize(){
 *   		logDebug("[TestTool]Rretrieve bundle from: "+TestTool.bundlePath);
 *   	}
 *   }
 * @author simon
 *
 */
public class Tool {

    /**
	 * Date format instance for Tool class use
	 */
    private static SimpleDateFormat df = new SimpleDateFormat();

    /**
	 * The resource bundle for Tool class use
	 */
    protected static String bundlePath = "resources.application";

    /**
	 * Resource bundle instance of the bundlePath 
	 */
    private static ResourceBundle rb = ResourceBundle.getBundle(bundlePath);

    /**
	 * To initialize the Tool with base resource bundle
	 * Default resource bundle is locate in "com/alchip/utils/general/tool/base.properties"
	 */
    public static void initialize() {
        setRbFromPath(bundlePath);
    }

    /**
	 * To initialize the Tool with base resource bundle
	 * Default resource bundle is locate in "com/alchip/utils/general/tool/base.properties"
	 */
    public static void initialize(String bundlePath) {
        setRbFromPath(bundlePath);
    }

    /**
	 * The getter method of a bundle instance
	 * @return the current bundle instance
	 */
    public static ResourceBundle getRb() {
        return rb;
    }

    /**
	 * To set the bundle resource bundle
	 * @param rb
	 */
    public static void setRb(ResourceBundle rb) {
        Tool.rb = rb;
    }

    /**
	 * To set the bundle resource by bundle path.
	 * @param bundlePath
	 */
    public static void setRbFromPath(String bundlePath) {
        Tool.bundlePath = bundlePath;
        Tool.rb = ResourceBundle.getBundle(bundlePath);
    }

    /**
	 * To get the bundle path
	 * @return
	 */
    public static String getBundlePath() {
        return bundlePath;
    }

    /**
	 * Test of a String is empty or not
	 * If String is null or it's length is less or equal with 0, then return false, vice versa. 
	 * @param s
	 * @return
	 */
    public static boolean isNotEmpty(String s) {
        if ((s != null) && s.length() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Object s) {
        if ((s != null) && s.toString().length() > 0) {
            return true;
        }
        return false;
    }

    /**
	 * To normalize phonenumber
	 * @param completePhoneNumber
	 * @return
	 */
    public static String normalizePhoneNumber(String completePhoneNumber) {
        if (Tool.isNotEmpty(completePhoneNumber)) {
            if (completePhoneNumber.startsWith("ADSL_")) {
                completePhoneNumber = completePhoneNumber.substring(5);
            }
            completePhoneNumber = completePhoneNumber.replaceAll("-", "");
            if (completePhoneNumber.startsWith("0")) {
                completePhoneNumber = completePhoneNumber.substring(1);
            }
            return completePhoneNumber;
        } else {
            return "";
        }
    }

    /**
	 * Using pattern like yyyy/MM/dd to retrieve the date string
	 * @see #convertDate(Date, String)
	 * @param pattern
	 * @return
	 */
    public static String getToday(String pattern) {
        df.applyPattern(pattern);
        return df.format(Calendar.getInstance().getTime());
    }

    /**
	 * Get default date String
	 * Using the default pattern yyyy/MM/dd to retrieve Today date String
	 * @see #convertDate(Date, String)
	 * @return
	 */
    public static String getToday() {
        df.applyPattern("yyyy/MM/dd");
        return df.format(Calendar.getInstance().getTime());
    }

    /**
	 * To convert java.util.Date object using pattern
	 * <pre>
	 * Date and Time Pattern Sample 
	 * (Reference to {@link http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html})  
	 * "yyyy.MM.dd G 'at' HH:mm:ss z"  => 2001.07.04 AD at 12:08:56 PDT  
	 * "EEE, MMM d, ''yy"              => Wed, Jul 4, '01  
	 * "h:mm a"                        => 12:08 PM  
	 * "hh 'o''clock' a, zzzz"         => 12 o'clock PM, Pacific Daylight Time  
	 * "K:mm a, z"                     => 0:08 PM, PDT  
	 * "yyyyy.MMMMM.dd GGG hh:mm aaa"  => 02001.July.04 AD 12:08 PM  
	 * "EEE, d MMM yyyy HH:mm:ss Z"    => Wed, 4 Jul 2001 12:08:56 -0700  
	 * "yyMMddHHmmssZ"                 => 010704120856-0700  
	 * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"    => 2001-07-04T12:08:56.235-0700  
	 * </pre>
	 * @param date
	 * @param pattern
	 * @return
	 */
    public static String convertDate(Date date, String pattern) {
        if (date != null) {
            df.applyPattern(pattern);
            return df.format(date);
        } else {
            return "";
        }
    }

    /**
	 * Using pattern to convert java.sql.Timestamp object
	 * @see #convertDate(Date, String)
	 * @param ts
	 * @return
	 */
    public static String paseDay(java.sql.Timestamp ts) {
        if (ts != null) {
            df.applyPattern("yyyy/MM/dd HH:mm:ss");
            return df.format(new Date(ts.getTime()));
        } else {
            return "";
        }
    }

    /**
	 * Get the given days ago Dats String
	 * @param i how many days ago.
	 * @return 
	 */
    public static String getDateDaysAgo(int days) {
        long dateTimeM = Calendar.getInstance().getTimeInMillis() / 60000 - (long) (days * 24 * 60);
        df.applyPattern("yyyy/MM/dd");
        return df.format(new Date(dateTimeM * 60000));
    }

    /**
	 * Get date String of the given days after
	 * @param i how many days after.
	 * @return
	 */
    public static String getDateDaysAfter(int days) {
        long dateTimeM = Calendar.getInstance().getTimeInMillis() / 60000 + (long) (days * 24 * 60);
        df.applyPattern("yyyy/MM/dd");
        return df.format(new Date(dateTimeM * 60000));
    }

    /**
	 * Convert a log IP to String
	 * <pre>
	 * Example: 
	 * Long ip = Long.parseLong("3553880577");
	 * System.out.println(Tool.convertIpLongToString(ip));
	 * Result: 
	 * 	211.211.234.1
	 * </pre>
	 * @see #convertIpStringToLong(String)
	 * @param longIp IP Long to Stirng
	 * @return
	 */
    public static String convertIpLongToString(long longIp) {
        String ipAddress = ((longIp >> 24) & 0xff) + "." + ((longIp >> 16) & 0xff) + "." + ((longIp >> 8) & 0xff) + "." + (longIp & 0xff);
        return ipAddress;
    }

    /**
	 * To convert IP String to Long
	 * <pre>
	 * Example:
	 * System.out.println(TestTool.convertIpStringToLong("211.211.234.1"));
	 * Result:
	 * 3553880577
	 * </pre>
	 * @see #convertIpLongToString(long)
	 * @param ipAddress
	 * @return
	 */
    public static long convertIpStringToLong(String ipAddress) {
        long longIp = 0;
        StringTokenizer st = new StringTokenizer(ipAddress, ".", false);
        while (st.hasMoreTokens()) {
            longIp = (longIp << 8) + Long.parseLong(st.nextToken());
        }
        return longIp;
    }

    /**
	 * To trim the given String object
	 * @param s
	 * @return
	 */
    public static String trim(String s) {
        if (s != null) {
            return s.trim();
        }
        return s;
    }

    /**
	 * Get a Integer property from property file
	 * <pre>
	 * If the property file not exist or the name not define.
	 * It will return "��Integer Properties����:" + name.
	 * </pre>
	 * @param name
	 * @return
	 */
    public static int getIntProperty(String name) {
        try {
            return Integer.parseInt(rb.getString(name));
        } catch (Exception e) {
            System.out.println("��Integer Properties����:" + name);
            return 0;
        }
    }

    /**
	 * Get a String property from property file
	 * <pre>
	 * If the property file not exist or the name not define.
	 * It will return "��String Properties����:" + name.
	 * </pre>
	 * 
	 * @param name
	 * @return
	 */
    public static String getStringProperty(String name) {
        try {
            return rb.getString(name);
        } catch (Exception e) {
            System.out.println("��String Properties����:" + name);
            return null;
        }
    }

    /**
	 * Get a String properties Map using given prefix from property file.
	 * <pre>
	 * If the property file not exist or the name not define.
	 * It will return null.
	 * </pre>
	 * @param forname
	 * @return
	 */
    public static HashMap<String, String> getMapProperty(String forname) {
        HashMap<String, String> hm = new HashMap<String, String>();
        try {
            Enumeration em = rb.getKeys();
            while (em.hasMoreElements()) {
                String str = (String) em.nextElement();
                if (str.length() > forname.length() && str.substring(0, forname.length()).equals(forname)) {
                    hm.put(str.substring(forname.length() + 1, str.length()), rb.getString(str));
                }
            }
            return hm;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
	 * Gets java.sql.Timestamp object in "yyyy/MM/dd HH:mm:ss" format
	 * @param dateTimeString datetime in String format
	 * @return java.util.Date object
	 * @throws ParseException
	 */
    public static java.sql.Timestamp getTimestampFromString(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.compareTo("") == 0) {
            return null;
        }
        java.util.Calendar cal = java.util.Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            return new java.sql.Timestamp(df.parse(dateTimeString).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * defaultString is default String, when string is null, it will return the defaultString
	 * @param s
	 * @param def
	 * @return
	 */
    public static String getString(String string, String defaultString) {
        String temp = defaultString;
        if (isNotEmpty(string)) {
            temp = string;
        }
        return temp;
    }

    /**
	 * defaultString is default String, when obj is null, it will return the defaultString
	 * @param obj
	 * @param def
	 * @return
	 */
    public static String getString(Object obj, String defaultString) {
        String temp = defaultString;
        if (obj != null) {
            temp = String.valueOf(obj);
        }
        return temp;
    }

    /**
	 * Get the Integer value of a given String, if string is null, it will return "0".
	 * @param string
	 * @return
	 */
    public static int getInt(Object string) {
        if (string == null) return 0; else {
            try {
                return Integer.parseInt(getString(string, "0"));
            } catch (NumberFormatException ne) {
                return 0;
            }
        }
    }

    /**
	 * Check current runtime is debug or not.\
	 * <pre>
	 * If property file define a DEBUG=TRUE (or true).
	 * It will return true.
	 * If the property
	 * </pre>
	 * @return
	 */
    public static boolean isDebug() {
        try {
            return Tool.getStringProperty("DEBUG").equalsIgnoreCase("TRUE");
        } catch (Exception e) {
            return false;
        }
    }

    /**
	 * Print the given message String when debug mode.
	 * <pre>
	 * If the property not defind the DEBUG or DEBUG value not "true" or "TRUE", 
	 * it will print the givne message to console. 
	 * </pre>
	 * @see #isDebug()
	 * @param msg
	 */
    public static void logDebug(String msg) {
        if (Tool.isDebug()) System.out.println("[" + Tool.getToday("yyyy/MM/dd HH:mm:ss") + "]" + msg);
    }

    public static void logDebug(String[] msg) {
        Tool.logDebug(Tool.arrayToString(msg));
    }

    /**
	 * Level 3: The most detail log
	 * Level 2: Less then Level 3
	 * Level 1: Less then Level 2
	 */
    public static void logDebug(String msg, int level) {
        int debug_level = Tool.getIntProperty("DEBUG_LEVEL");
        if (level <= debug_level) {
            Tool.logDebug(msg);
        }
    }

    public static void logDebug(String[] msg, int level) {
        Tool.logDebug(Tool.arrayToString(msg), level);
    }

    public static String arrayToString(String[] msg) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i > msg.length - 1; i++) {
            sb.append(msg[i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
	 * Print the given message String when debug mode.
	 * <pre>
	 * If the property not defind the DEBUG or DEBUG value not "true" or "TRUE", 
	 * it will print the givne message to console. 
	 * </pre>
	 * @see #isDebug()
	 * @param msg
	 */
    public static void logError(String msg) {
        if (Tool.isDebug()) System.err.println("[" + Tool.getToday("yyyy/MM/dd HH:mm:ss") + "][ERROR]" + msg);
    }

    /**
	 * Level 3: The most detail log
	 * Level 2: Less then Level 3
	 * Level 1: Less then Level 2
	 */
    public static void logError(String msg, int level) {
        int debug_level = Tool.getIntProperty("DEBUG_LEVEL");
        if (level <= debug_level) {
            Tool.logError(msg);
        }
    }

    /**
	 * To get the "PROPERTY.NAME" that defind in the property file
	 * <pre>
	 * If there is any error, it will return a null value. 
	 * </pre>
	 * @return
	 */
    public static String getPropertyName() {
        try {
            return Tool.getStringProperty("PROPERTY.NAME");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeString(String string, String fromEncoding, String toEncoding) {
        if (string == null) return null;
        try {
            return new String(string.getBytes(fromEncoding), toEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map encodeMap(Map map, String fromEncoding, String toEncoding) {
        if (map == null && map.size() == 0) return null;
        try {
            Map newMap = new HashMap();
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                Object obj = map.get(key);
                if (obj instanceof String) {
                    newMap.put(key, new String(((String) obj).getBytes(fromEncoding), toEncoding));
                }
            }
            map.putAll(newMap);
            return map;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeBig5ToISO88591(String string) {
        return encodeString(string, "BIG5", "iso-8859-1");
    }

    public static Map encodeMapBig5ToISO88591(Map map) {
        return encodeMap(map, "BIG5", "iso-8859-1");
    }

    public static String encodeISO88591ToBig5(String string) {
        return encodeString(string, "iso-8859-1", "BIG5");
    }

    public static Map encodeMapISO88591ToBig5(Map map) {
        return encodeMap(map, "iso-8859-1", "BIG5");
    }
}
