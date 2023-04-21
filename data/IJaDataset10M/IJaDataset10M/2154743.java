package org.capelin.mvc.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.jsp.tagext.TagSupport;
import org.capelin.core.utils.StaticStrings;

/**
 * <a href="http://code.google.com/p/capline-opac/">Capelin-opac</a>
 * License: GNU AGPL v3 | http://www.gnu.org/licenses/agpl.html
 * 
 * 
 * Custom tags to display record.
 * 
 * @author Jing Xiao <jing.xiao.ca at gmail dot com>
 * 
 */
public class TagUtils extends TagSupport {

    private static final long serialVersionUID = 1L;

    public static String scl(String s) {
        if (null == s) return StaticStrings.EMPLTY;
        s = encode(s).replace(StaticStrings.DB_SPLIT_STRING, StaticStrings.SEMICOLON_SPACE);
        return s;
    }

    public static String unique(String s) {
        if (null == s) return StaticStrings.EMPLTY;
        String[] a = encode(s).split(StaticStrings.DB_SPLIT_STRING);
        return a[0];
    }

    public static String br(String s) {
        if (null == s) return StaticStrings.EMPLTY;
        s = encode(s).replace(StaticStrings.DB_SPLIT_STRING, StaticStrings.END_LINE);
        return s;
    }

    public static List<String> itemsToList(String s) {
        if (null == s) return new ArrayList<String>(0);
        return Arrays.asList(s.split(StaticStrings.DB_SPLIT_STRING));
    }

    public static String url(String s) {
        if (null == s) return null;
        String[] array = s.split(StaticStrings.DB_SPLIT_STRING);
        StringBuffer sb = new StringBuffer();
        for (String line : array) {
            if (line.startsWith("http://")) {
                sb.append("<a target=\"_blank\" href=\"").append(line).append("\">").append(line).append("</a>");
            } else {
                sb.append(line);
            }
            sb.append(StaticStrings.END_LINE);
        }
        return sb.toString();
    }

    public static String clink(String s) {
        StringBuffer sb = new StringBuffer();
        if (null != s) {
            s = s.trim();
            s = s.replaceAll(StaticStrings.QUOTE, StaticStrings.SPACE);
            s = s.replaceAll("&", StaticStrings.SPACE);
            s = s.replaceAll("%", StaticStrings.SPACE);
            try {
                sb.append(WebViewUtils.U_DOUBLE_QUOTE).append(URLEncoder.encode(s, "UTF8")).append(WebViewUtils.U_DOUBLE_QUOTE);
            } catch (UnsupportedEncodingException e) {
                sb.append(WebViewUtils.U_DOUBLE_QUOTE).append(s).append(WebViewUtils.U_DOUBLE_QUOTE);
            }
        }
        return sb.toString().replace(WebViewUtils.PLUS, StaticStrings.SPACE).toLowerCase();
    }

    public static String encode(String s) {
        if (null == s) return null;
        s = s.replaceAll("&", " ");
        s = s.replaceAll(degreeS, "&#176;");
        s = s.replaceAll("\"", "&quot;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        return s;
    }

    private static String degreeS = Character.toString((char) 192);
}
