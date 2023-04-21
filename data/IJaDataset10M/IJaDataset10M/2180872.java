package com.uside.core.util;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.util.UUID;

/**
 * @version 1.0
 */
public final class StringUtil {

    private StringUtil() {
    }

    /**
	 * 该方法从commons-2.0's ObjectUtils中取出
	 * <p>
	 * Appends the toString that would be produced by <code>Object</code> if a
	 * class did not override toString itself. <code>null</code> will return
	 * <code>null</code>.
	 * </p>
	 * 
	 * <pre>
	 * ObjectUtils.appendIdentityToString(*, null)            = null
	 * ObjectUtils.appendIdentityToString(null, &quot;&quot;)           = &quot;java.lang.String@1e23&quot;
	 * ObjectUtils.appendIdentityToString(null, Boolean.TRUE) = &quot;java.lang.Boolean@7fa&quot;
	 * ObjectUtils.appendIdentityToString(buf, Boolean.TRUE)  = buf.append(&quot;java.lang.Boolean@7fa&quot;)
	 * </pre>
	 * 
	 * @param buffer
	 *          the buffer to append to, may be <code>null</code>
	 * @param object
	 *          the object to create a toString for, may be <code>null</code>
	 * @return the default toString text, or <code>null</code> if
	 *         <code>null</code> passed in
	 * @since 2.0
	 */
    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }
        if (buffer == null) {
            buffer = new StringBuffer();
        }
        return buffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
    }

    public static String createPK() {
        return (new UUID()).toString();
    }

    /**
	 * 判断一个字符串是否为空或者是右空白符号组成
	 * 
	 * @param source
	 *          String
	 * @return boolean
	 */
    public static boolean empty(String source) {
        return (source == null || source.trim().length() == 0 || source.toLowerCase().trim().equals("null")) ? true : false;
    }

    /**
	 * 判断一个字符串是否不为空或者不是右空白符号组成
	 * 
	 * @param source
	 *          String
	 * @return boolean
	 */
    public static boolean notEmpty(String source) {
        return (source != null && source.trim().length() > 0 && !source.toLowerCase().equals("null")) ? true : false;
    }

    public static String[] toArray(String parseString) {
        return toArray(parseString, " \t\n\r\f", false);
    }

    public static String[] toArray(String parseString, String splitString) {
        return toArray(parseString, splitString, false);
    }

    /**
	 * 分隔一个字符串
	 * 
	 * @param parseString
	 *          String 原始字符串
	 * @param splitString
	 *          String 分隔串
	 * @param returnDelims
	 *          boolean 返回值是否包含分隔串
	 * @return String[] 分隔后的字符串
	 */
    public static String[] toArray(String parseString, String splitString, boolean returnDelims) {
        StringTokenizer tokens = new StringTokenizer(parseString, splitString, returnDelims);
        String[] values = new String[tokens.countTokens()];
        int i = 0;
        while (tokens.hasMoreTokens()) {
            values[i++] = tokens.nextToken();
        }
        return values;
    }

    public static char toUpperCase(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            ch -= 32;
        }
        return ch;
    }

    /**
	 * 将给定字符串指定位置上的字母大写
	 * 
	 * @param source
	 *          String
	 * @param pos
	 *          int
	 * @return String
	 */
    public static String upperCharAt(final String source, int pos) {
        if (source == null) {
            return null;
        }
        int len = source.length();
        if (pos < 0 || pos >= len) {
            return "";
        }
        char[] chars = source.toCharArray();
        chars[pos] = toUpperCase(chars[pos]);
        return new String(chars);
    }

    /**
	 * 得到字符串的位长度，中文＝2,英文＝1
	 * 
	 * @param sourceString
	 *          待测的字符串
	 * @return int 字节长度
	 */
    public static int lengthInBit(String sourceString) {
        int index = 0;
        char[] sourceChrs = sourceString.toCharArray();
        int sourceLength = sourceChrs.length;
        for (int i = 0; i < sourceLength; i++) {
            if (sourceChrs[i] <= 202 && sourceChrs[i] >= 8) {
                index++;
            } else {
                index += 2;
            }
        }
        sourceChrs = null;
        return index;
    }

    /**
	 * 截取字符，主要用于显示区域长度固定的字符串的显示（中文＝2,英文＝1）
	 * 
	 * @param sourceString
	 *          待处理的字符串
	 * @param viewLength
	 *          截取的长度
	 * @return 返回截取后的字符串
	 */
    public static String subInBit(final String sourceString, int viewLength) {
        char[] sourceChrs = sourceString.toCharArray();
        int sLen = sourceChrs.length;
        int i = 0;
        for (; i < viewLength; i++) {
            if (i >= sLen) {
                return sourceString;
            }
            if (sourceChrs[i] < 8 || sourceChrs[i] > 202) {
                viewLength--;
            }
        }
        sourceChrs = null;
        return sourceString.substring(0, i);
    }

    /**
	 * 截取字符串,若该字符串被截断，则添加指定字符串（append）在末尾
	 * 
	 * @param sourceString
	 *          待处理的字符串
	 * @param viewLength
	 *          截取的长度,是字节长度,一个中文两个字节
	 * @param append
	 *          需要添加的字符串
	 * @return 返回截取后的字符串
	 */
    public static String subAppend(final String sourceString, int viewLength, final String append) {
        char[] sourceChrs = sourceString.toCharArray();
        int sLen = sourceChrs.length;
        int i = 0;
        for (; i < viewLength; i++) {
            if (i >= sLen) {
                return sourceString;
            }
            if (sourceChrs[i] < 8 || sourceChrs[i] > 202) {
                viewLength--;
            }
        }
        sourceChrs = null;
        return sourceString.substring(0, i) + append;
    }

    public static String wrapToXNL(String content) {
        return "<![CDATA[" + content + "]]>";
    }

    public static String getSetter(String field) {
        return "set" + upperCharAt(field, 0);
    }

    public static String getGetter(String field) {
        return "get" + upperCharAt(field, 0);
    }

    public static boolean isNumber(Object value) {
        if (value == null) {
            return false;
        }
        String sValue = value.toString();
        try {
            new java.math.BigDecimal(sValue);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static double getDouble(Object o) {
        if (o == null) {
            return 0.0;
        }
        try {
            return Double.parseDouble(o.toString().trim());
        } catch (Exception ex) {
            return 0.0;
        }
    }

    public static int getInt(Object o) {
        if (o == null) {
            return 0;
        }
        try {
            return Integer.parseInt(o.toString().trim());
        } catch (Exception ex) {
            return 0;
        }
    }

    public static String arrayToString(Object array) {
        final String nullText = "null";
        final String arrayStart = "[";
        final String arrayEnd = "]";
        final String arraySeparator = ",";
        if (array == null) {
            return nullText;
        }
        if (!array.getClass().isArray()) {
            return nullText;
        }
        int length = Array.getLength(array);
        StringBuffer buffer = new StringBuffer(arrayStart);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            if (i > 0) {
                buffer.append(arraySeparator);
            }
            buffer.append((item == null) ? nullText : item.toString());
        }
        buffer.append(arrayEnd);
        return buffer.toString();
    }

    public static String arrayToSQLStr(Object array) {
        final String nullText = "null";
        final String arrayStart = "(";
        final String arrayEnd = ")";
        final String arraySeparator = ",";
        if (array == null) {
            return nullText;
        }
        if (!array.getClass().isArray()) {
            return nullText;
        }
        int length = Array.getLength(array);
        StringBuffer buffer = new StringBuffer(arrayStart);
        for (int i = 0; i < length; i++) {
            Object item = Array.get(array, i);
            if (i > 0) {
                buffer.append(arraySeparator);
            }
            buffer.append((item == null) ? nullText : "'" + item.toString() + "'");
        }
        buffer.append(arrayEnd);
        return buffer.toString();
    }

    public static String getSQLValue(String SQL, String valueName) {
        String value = "";
        if (SQL != null) {
            String[] sqls = SQL.split("and");
            if (sqls != null && sqls.length > 0) {
                String name = "";
                for (int i = 0; i < sqls.length; i++) {
                    name = sqls[i];
                    if (name.trim().indexOf(valueName) > 0) {
                        String[] temp = name.split("'");
                        String[] temp2 = name.split("%");
                        if (temp != null && temp.length == 3) {
                            value = temp[1].trim();
                            if (temp2 != null && temp2.length == 3) {
                                value = temp2[1].trim();
                            }
                        }
                    }
                }
            }
        }
        return value;
    }

    /**
	 * 根据给定对象（使用Object.toString()方法）返回一个非空且不包含左右空格的字符串
	 * 
	 * @param param
	 *          Object 待处理的对象
	 * @return String 处理后的字符串
	 */
    public static String getNotNullAndTrim(Object param) {
        if (param == null) {
            return "";
        }
        String rtn = param.toString();
        return rtn == null ? "" : rtn.trim();
    }

    /**
	 * 填充字符串
	 * 
	 * @param src
	 * @param length
	 * @param ch
	 * @return
	 */
    public static String fillString(String src, int length, char ch) {
        if (src == null) src = "";
        while (src.length() < length) {
            src = ch + src;
        }
        return src;
    }

    /**
	 * 
	 * <description> 方法描述：转换字符编码
	 * 
	 * </description>
	 * 
	 * @author luwc
	 * @创建日期：Feb 2, 2009 5:23:58 PM
	 * @param src
	 * @return
	 */
    public static String transferToGBK(String src) {
        if (src == null) return null; else if (empty(src)) return "";
        try {
            return new String(src.getBytes("ISO-8859-1"), "GBK");
        } catch (Exception ex) {
            return null;
        }
    }

    /** 
     * 全角空格为12288，半角空格为32 
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248 
     *  
     * 将字符串中的全角字符转为半角 
     * @author wangjc
     * @date 2008-8-20 下午03:20:18
     * @param src 要转换的包含全角的任意字符串 
     * @return  转换之后的字符串 
     */
    public static String getSemiangle(String src) {
        if (empty(src)) return null;
        char[] c = src.trim().toCharArray();
        for (int index = 0; index < c.length; index++) {
            if (c[index] == 12288) {
                c[index] = (char) 32;
            } else if (c[index] > 65280 && c[index] < 65375) {
                c[index] = (char) (c[index] - 65248);
            }
        }
        return String.valueOf(c);
    }

    /**
     * 
     * <description>获得去除特殊字符和标点符号后的字符串</description>
     *  
     * @author wangjc
     * @date 2008-8-20 下午03:20:18
     * @param src
     * @return
     */
    public static String getRegexString(String src) {
        if (empty(src)) return null;
        src = getSemiangle(src.trim());
        Pattern pattern = Pattern.compile("[\\.\\,\\|\\\"\\?\\!:'\\-_\\@\\#\\$\\%\\^\\&\\*\\(\\)\\{\\}\\[\\]]");
        Matcher matcher = pattern.matcher(src);
        String first = matcher.replaceAll("");
        pattern = Pattern.compile(" {2,}");
        matcher = pattern.matcher(first);
        String second = matcher.replaceAll(" ");
        return second;
    }

    /**
	 * 
	 * <description>
	 * 方法描述：组装SQL语句(按字符串处理)
		例如：combSQL(sql,"u",map,"key","=")
	 * </description>
	 * @author wangjc
	 * @创建日期：2009-2-13 上午09:45:03
	 * @param sql 组装好的sql
	 * @param tableFlag 表标识
	 * @param map 条件map
	 * @param key 关键值
	 * @param exp 符号
	 * @return
	 */
    public static void combSQL(StringBuffer sql, String tableFlag, Map map, String key, String exp) {
        combSQL(sql, tableFlag, map, key, key, exp, "0");
    }

    /**
	 * 
	 * <description>
	 * 方法描述：组装SQL语句(按字符串处理)
		例如：combSQL(sql,"u",map,"key","column","=")
	 * </description>
	 * @author wangjc
	 * @创建日期：2009-2-13 上午09:45:03
	 * @param sql 组装好的sql
	 * @param tableFlag 表标识
	 * @param map 条件map
	 * @param key 关键值
	 * @param column 列名
	 * @param exp 符号
	 * @return
	 */
    public static void combSQL(StringBuffer sql, String tableFlag, Map map, String key, String column, String exp) {
        combSQL(sql, tableFlag, map, key, column, exp, "0");
    }

    /**
	 * 
	 * <description>
	 * 方法描述：组装SQL语句
		例如：combSQL(sql,"u",map,"key","column","=","0")
	 * </description>
	 * @author wangjc
	 * @创建日期：2009-2-13 上午09:45:03
	 * @param sql 组装好的sql
	 * @param tableFlag 表标识
	 * @param map 条件map
	 * @param key 关键值
	 * @param column 列名
	 * @param exp 符号
	 * @param flag '0'按字符串处理,'1'按数值处理
	 * @return
	 */
    public static void combSQL(StringBuffer sql, String tableFlag, Map map, String key, String column, String exp, String flag) {
        if (map == null || map.isEmpty() || empty(key) || !map.containsKey(key) || empty(exp)) return;
        String value = (String) map.get(key);
        value = value.trim();
        if (column == null) column = key;
        if (key.equals(column) && (key.toLowerCase().endsWith("min") || key.toLowerCase().endsWith("max"))) column = column.substring(0, column.length() - 3);
        exp = exp.trim();
        if (flag == null) flag = "0";
        if (sql.length() > 0) sql.append(" and ");
        sql.append(tableFlag).append(".").append(column).append(" ").append(exp).append(" ");
        if (exp.equals("=") || exp.equals(">") || exp.equals("<")) {
            if (flag.equals("0")) sql.append("'").append(value).append("'"); else sql.append(value);
        } else if (exp.equals("like")) {
            sql.append("'%").append(value).append("%'");
        }
        sql.append(" ");
    }
}
