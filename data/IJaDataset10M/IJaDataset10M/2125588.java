package com.idocbox.common.lang;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String util
 * @author Chunhui Li   
 * email:wiseneuron@gmail.com
 *
 */
public class StringUtil {

    /**
	 * check whether given string is not blank
	 * 
	 * @param str
	 *            string to check.
	 * @return true:not blank, false:it is blank.
	 */
    public static boolean isNotBlank(String str) {
        return (str != null && !"".equals(str)) ? true : false;
    }

    /**
	 * check whether given string blank.
	 * 
	 * @param str
	 *            string to check.
	 * @return true:it is blank, false:it is not blank.
	 */
    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    /**
	 * check whether given string is null or "".
	 * 
	 * @param str
	 *            string to check.
	 * @return true:str is not null and "", false:str is null or "".
	 */
    public static boolean isNotEmpty(String str) {
        return (str != null && !"".equals(str.trim())) ? true : false;
    }

    /**
	 * check whether given string is not null and not "".
	 * 
	 * @param str
	 *            string to check.
	 * @return trus: str is null or "", false: str is not null and not "".
	 */
    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    /**
	 * companare string 
	 * 
	 * @param src
	 * @param des
	 * @return
	 */
    public static boolean equals(String src, String des) {
        if (src == null) return (des == null ? true : false);
        if (des == null) return (src == null ? true : false);
        return src.equals(des);
    }

    /**
	 * join elements in given string array with ",".
	 * 
	 * @param str
	 *            string array.
	 * @return joint elements.
	 */
    public static String StringArrayToString(String[] str) {
        StringBuilder sb = new StringBuilder();
        if (str != null && str.length > 0) {
            for (String s : str) {
                if (s != null) {
                    sb.append(s + ",");
                }
            }
            if (sb.length() == 0) return "";
            return sb.substring(0, sb.length() - 1).toString();
        }
        return str[0];
    }

    /**
	 * join string in array as a string with given seperator.
	 * @param strArray  string array to join.
	 * @param seperator seperator to use.
	 * @return
	 */
    public static String joinWith(String[] strArray, String seperator) {
        String str = new String();
        if (null != strArray) {
            int len = strArray.length;
            for (int i = 0; i < len; i++) {
                String ele = strArray[i];
                str += seperator + ele;
            }
            if (str.startsWith(seperator)) {
                str = str.substring(seperator.length());
            }
        }
        return str;
    }

    /**
	 * compare two string.
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
    public static boolean eq(final String str1, final String str2) {
        boolean re = false;
        if (null != str1) {
            re = str1.equals(str2);
        } else if (null != str2) {
            re = false;
        } else {
            re = true;
        }
        return re;
    }

    /**
	 * trim a string.
	 * @param str source string.
	 * @return trimmed string.
	 */
    public static String trim(String str) {
        return (new String(str)).trim();
    }

    /**
	 * Is given email format correct?
	 * @param email
	 * @return true:ok, false: error.
	 */
    public static boolean isEmail(final String email) {
        boolean isMatched = false;
        String check = "^([a-z0-9A-Z]+[-|//.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?//.)+[a-zA-Z]{2,}$";
        isMatched = isMatched(email, check);
        return isMatched;
    }

    /**
	 * Is given mobile format correct?
	 * @param mobile
	 * @return true: ok, false: error.
	 */
    public static boolean isMobile(final String mobile) {
        boolean isMatched = false;
        String cmcc = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[89]{1}))[0-9]{8}$";
        String cucc = "^[1]{1}(([3]{1}[0-3]{1})|([5]{1}[3]{1}))[0-9]{8}$";
        String cnc = "^[1]{1}[8]{1}[89]{1}[0-9]{8}$";
        isMatched = isMatched(mobile, cmcc) || isMatched(mobile, cucc) || isMatched(mobile, cnc);
        return isMatched;
    }

    /**
	 * Is given string's format correct?
	 * @param str string.
	 * @param regExp regular expression.
	 * @return true:ok, false: error.
	 */
    public static boolean isMatched(final String str, final String regExp) {
        boolean isMatched = false;
        Pattern regex = Pattern.compile(regExp);
        Matcher matcher = regex.matcher(str);
        isMatched = matcher.matches();
        return isMatched;
    }

    /** 
     * Remove all html tags in inputed string.
     *  
     * @param input 
     * @return 
     */
    public static String filterHtmlTag(String input) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        return str;
    }

    /** 
     * remove html tag in inputed string.
     *  
     * @param input 
     * @param length 
     * @return 
     */
    public static String splitAndFilterHtmlTag(String input, int length) {
        if (input == null || input.trim().equals("")) {
            return "";
        }
        String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
        str = str.replaceAll("[(/>)<]", "");
        int len = str.length();
        if (len <= length) {
            return str;
        } else {
            str = str.substring(0, length);
            str += "......";
        }
        return str;
    }

    /**
     * if src.length < len, then fill given char at the front of the string src,
     * after it is filled, src.length = len. if src.length >= len, then nothing to 
     * do, and return src.
     * @param src
     * @param len
     * @param fillWith
     * @return
     */
    public static String fillBefore(String src, int len, char fillWith) {
        String filled = null;
        int srclen = src.length();
        if (null != src && srclen >= len) {
            filled = src;
        } else {
            int dif = len - srclen;
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < dif; i++) {
                buf.append(fillWith);
            }
            buf.append(src);
            filled = buf.toString();
        }
        return filled;
    }
}
