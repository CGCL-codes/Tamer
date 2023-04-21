package com.jxva.util;

import java.util.regex.Pattern;

/**
 *
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2009-01-08 09:23:52 by Jxva
 */
public abstract class HtmlUtil {

    private static final Pattern pattern = Pattern.compile("(\\<[^>]*\\>)|(\\&nbsp;)|(\n)");

    /**
	 * HTML字符过滤,进行过滤的五个字符为'<','>','"','&','''
	 * @param str 需要过滤的字符串
	 * @return 过滤后的字符串
	 */
    public static String filter(String str) {
        if (StringUtil.isEmpty(str)) {
            return str;
        }
        StringBuilder result = null;
        for (int i = 0; i < str.length(); i++) {
            String filtered = null;
            switch(str.charAt(i)) {
                case '<':
                    filtered = "&lt;";
                    break;
                case '>':
                    filtered = "&gt;";
                    break;
                case '&':
                    filtered = "&amp;";
                    break;
                case '"':
                    filtered = "&quot;";
                    break;
                case '\'':
                    filtered = "&#39;";
                    break;
            }
            if (result == null) {
                if (filtered != null) {
                    result = new StringBuilder(str.length() + 50);
                    if (i > 0) {
                        result.append(str.substring(0, i));
                    }
                    result.append(filtered);
                }
            } else {
                if (filtered == null) {
                    result.append(str.charAt(i));
                } else {
                    result.append(filtered);
                }
            }
        }
        return (result == null) ? str : result.toString();
    }

    /**
	 *
	 * 完全清除所有HTML代码,空格与换行符
	 * @param str 需要清除的字符串
	 * @return 清除后的字符串
	 */
    public static String clear(String str) {
        if (StringUtil.isEmpty(str)) return str;
        return pattern.matcher(str).replaceAll("");
    }
}
