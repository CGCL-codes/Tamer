package org.dragonfly.jsdocx;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dragonfly.jsdocx.model.JSClass;
import org.dragonfly.jsdocx.model.JSElement;
import org.dragonfly.jsdocx.model.JSPackage;

public class JsdocHelper {

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0 ? true : false;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty() ? true : false;
    }

    public static boolean isExistValue(List<String> list, String value) {
        if (list == null || list.isEmpty()) return false;
        for (String v : list) {
            if (v.equals(value)) return true;
        }
        return false;
    }

    public static String join4JSON(List<String> list, String join) {
        if (list == null || list.isEmpty()) return null;
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                sb.append("'" + list.get(i) + "'");
            } else {
                sb.append(join + "'" + list.get(i) + "'");
            }
        }
        return sb.toString();
    }

    public static String getPackageName(String className) {
        if (isEmpty(className)) return "";
        int pos = className.lastIndexOf(".");
        if (pos <= 0) return "";
        return className.substring(0, pos);
    }

    public static String getClassShortName(String className) {
        if (isEmpty(className) || className.endsWith(".")) return "";
        int pos = className.lastIndexOf(".");
        if (pos < 0) return className;
        return className.substring(pos + 1);
    }

    public static String getShortFileName(String fileName) {
        if (isEmpty(fileName)) return null;
        fileName = fileName.replaceAll("/", "\\\\");
        return fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1, fileName.length());
    }

    public static String[] getObjectTypes(String str) {
        return getObjectTypes(str, "Object");
    }

    public static String[] getObjectTypes(String str, String defaultType) {
        if (isEmpty(str)) return new String[] { defaultType };
        if (str.indexOf("{") >= 0) str = getBetween(str, "\\{", "\\}");
        if (JsdocHelper.isEmpty(str)) return new String[] { defaultType };
        return str.split("\\|");
    }

    public static boolean isOptionalParameter(String parameter) {
        return !isEmpty(parameter) && parameter.endsWith(":optional");
    }

    public static String getOptionalParameter(String parameter) {
        return parameter.replaceFirst(":optional", "");
    }

    public static String getBetween(String str, String s1, String s2) {
        if (isEmpty(str)) return null;
        Pattern pattern = Pattern.compile(s1 + "[\\s\\S]*" + s2);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            MatchResult rst = matcher.toMatchResult();
            String find = rst.group();
            return find.substring(isEmpty(s1) ? 0 : s1.replaceAll("\\\\", "").length(), isEmpty(s2) ? find.length() : find.length() - s2.replaceAll("\\\\", "").length());
        }
        return null;
    }

    public static String toIgnoreText(String str, int len) {
        if (isEmpty(str)) return "";
        if (str.length() <= len) return str;
        return str.substring(0, len) + "...";
    }

    public static boolean isCommentLine(String line) {
        return isSingleCommentLine(line) || isMultiCommentLine(line);
    }

    public static boolean isSingleCommentLine(String line) {
        if (isEmpty(line)) return false;
        String li = line.trim();
        return li.startsWith("//");
    }

    public static boolean isMultiCommentLine(String line) {
        if (isEmpty(line)) return false;
        String li = line.trim();
        return li.indexOf("/*") >= 0 || li.startsWith("*") || li.endsWith("*/");
    }

    public static boolean isMultiCommentBeginLine(String line) {
        return line.indexOf("/*") >= 0;
    }

    public static boolean isMultiCommentEndLine(String line) {
        return line.endsWith("*/");
    }

    public static String getMultiCommentText(String line) {
        if (line.startsWith("*/")) return null;
        int p1 = 0;
        int p2 = line.length();
        if (line.indexOf("/**") >= 0) {
            p1 = line.indexOf("/**") + 3;
        } else if (line.indexOf("/*") >= 0) {
            p1 = line.indexOf("/*") + 2;
        } else if (line.indexOf("*") >= 0) {
            p1 = line.indexOf("*") + 1;
        }
        if (line.lastIndexOf("*/") >= 0) p2 = line.lastIndexOf("*/");
        line = line.substring(p1, p2);
        return isTagLine(line) ? line.trim() : (line.startsWith(" ") ? line.substring(1) : line);
    }

    public static boolean isTagLine(String line) {
        return line.trim().startsWith("@");
    }

    public static String formatToHTML(String str) {
        str = str.replaceAll("\n", "<br/>");
        return str;
    }

    public static String escapeHTML(String str) {
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\t", "    ");
        return str;
    }

    public static String toDeprName(String name, boolean isDepr) {
        return isDepr ? "<strike>" + name + "</strike>" : name;
    }

    public static String formatDateTime(Date date) {
        if (date == null) return "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String formatDate(Date date) {
        if (date == null) return "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Date parseDate(String date) {
        if (date == null) return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
        }
        return d;
    }

    public static String formatVersion(String version) {
        if (isEmpty(version)) return version;
        return version.replaceAll("\\D+", ".");
    }

    public static int compareVersion(String v1, String v2) {
        if (isEmpty(v1)) {
            if (isEmpty(v2)) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if (isEmpty(v2)) {
                return 1;
            } else {
                v1 = normalisedVersion(formatVersion(v1));
                v2 = normalisedVersion(formatVersion(v2));
                int c = v1.compareTo(v2);
                return c == 0 ? 0 : (c > 0 ? 1 : -1);
            }
        }
    }

    private static String normalisedVersion(String version) {
        String[] split = Pattern.compile(".", Pattern.LITERAL).split(version);
        StringBuilder sb = new StringBuilder();
        for (String s : split) {
            sb.append(String.format("%8s", s));
        }
        return sb.toString();
    }

    private static String replaceCode(String code) {
        int p1 = code.indexOf("<code>") + 6;
        int p2 = code.indexOf("</code>");
        StringBuffer sb = new StringBuffer("");
        String[] lines = code.substring(p1, p2).split("\n");
        for (String line : lines) {
            sb.append(line + "\n");
        }
        return "<pre class=\"source\">" + JsdocHelper.escapeHTML(sb.toString()) + "</pre>";
    }

    public static String formatDescHTML(String str) {
        if (JsdocHelper.isEmpty(str)) return "";
        Pattern pattern = Pattern.compile("<code>[\\s\\S]*</code>");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, replaceCode(matcher.group()).replaceAll("\\$", "\\\\\\$"));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String formatDescHTML(JSElement jse) {
        String str = jse.getDesc();
        if (JsdocHelper.isEmpty(str)) return "";
        if (jse instanceof JSClass) {
            JSClass jClass = (JSClass) jse;
            if (JSClass.TYPE_STRUCT.equals(jClass.getType())) str = "<code>" + str + "</code>";
        }
        return formatDescHTML(str);
    }

    public static void sortPackage(List<JSPackage> pkgs) {
        java.util.Collections.sort(pkgs, new Comparator<JSPackage>() {

            public int compare(JSPackage pkg1, JSPackage pkg2) {
                String name1 = pkg1.getName();
                String name2 = pkg2.getName();
                int p = name1.charAt(0) - name2.charAt(0);
                if (p != 0) return p;
                return name1.length() - name2.length();
            }
        });
    }

    public static String[] splitWithSpace(String text, int num) {
        if (text == null || text.length() <= 0) return null;
        text = text.replaceFirst("\\s+\\{\\s+", " {");
        text = text.replaceFirst("\\s+\\}\\s+", "} ");
        text = text.replaceAll("\\s*\\|\\s*", "|");
        String[] splitArr = text.split("\\s+", num);
        if (splitArr.length < num) {
            String[] newArr = new String[num];
            for (int i = 0; i < newArr.length; i++) {
                if (i < splitArr.length) {
                    newArr[i] = splitArr[i];
                } else {
                    newArr[i] = "";
                }
            }
            return newArr;
        } else {
            return splitArr;
        }
    }

    public static void main(String[] args) throws JsdocException {
        String s = JsdocHelper.getBetween("array<Event", "<", ">");
        System.out.println(s);
        System.out.println(compareVersion("1.9_2012", "1.10alpha2011"));
        System.out.println(compareVersion("1.10_2012", "1.10alpha2011"));
        System.out.println(getObjectTypes("{string|function}"));
        System.out.println(getMultiCommentText("sss/* @constant {Int} BACK_SPACE */ "));
        String[] a = splitWithSpace("{String | HTMLElement |Array} el Accepts a", 3);
        for (String string : a) {
            System.out.println(string);
        }
    }
}
