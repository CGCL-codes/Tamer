package util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: serhiy
 * Created on Feb 18, 2007, 7:55:14 AM
 */
public final class HtmlParser {

    private static final Pattern eol_re = Pattern.compile("\r?\n");

    private static final Pattern comment_re = Pattern.compile("<!--(.*?-->|.*)", Pattern.DOTALL);

    private static final Pattern lineBreak_re = Pattern.compile("<(div|p|h[1-6]|address|blockquote|center|dt|dd|li|br)\\b.*?>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private static final Pattern tag_re = Pattern.compile("<.*?>", Pattern.DOTALL);

    private static final Pattern endspaces_re = Pattern.compile("\\s+$");

    private static final Pattern entity_re = Pattern.compile("&(.*?);");

    private static final Map<String, String> entities = new HashMap<String, String>();

    static {
        entities.put("amp", "&");
        entities.put("lt", "<");
        entities.put("gt", ">");
        entities.put("quot", "\"");
        entities.put("nbsp", " ");
        entities.put("ensp", " ");
        entities.put("emsp", " ");
        entities.put("ndash", "-");
        entities.put("mdash", "--");
        entities.put("lsquo", "`");
        entities.put("rsquo", "'");
        entities.put("sbquo", ",");
        entities.put("ldsquo", "``");
        entities.put("rdsquo", "''");
        entities.put("bdquo", ",,");
        entities.put("lsaquo", "<");
        entities.put("rsaquo", ">");
        entities.put("hellip", "...");
        entities.put("prime", "'");
        entities.put("Prime", "\"");
        entities.put("trade", "(tm)");
        entities.put("minus", "-");
        entities.put("lowast", "*");
        entities.put("laquo", "<<");
        entities.put("raquo", ">>");
        entities.put("shy", "");
        entities.put("reg", "(R)");
        entities.put("copy", "(c)");
    }

    private static String readAll(Reader in) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1 << 16];
        int count;
        while ((count = in.read(buffer, 0, buffer.length)) >= 0) builder.append(buffer, 0, count);
        return builder.toString();
    }

    public static String htmlToText(String str) {
        str = eol_re.matcher(str).replaceAll(" ");
        str = comment_re.matcher(str).replaceAll("");
        str = lineBreak_re.matcher(str).replaceAll("\n");
        str = tag_re.matcher(str).replaceAll("");
        str = endspaces_re.matcher(str).replaceAll("");
        Matcher matcher = entity_re.matcher(str);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(1);
            String replacement = entities.get(name.toLowerCase());
            if (replacement == null) {
                replacement = "?";
                if (name.length() > 1 && name.charAt(0) == '#') {
                    try {
                        if (name.charAt(1) == 'x' || name.charAt(1) == 'X') replacement = String.valueOf((char) Integer.parseInt(name.substring(2), 16)); else replacement = String.valueOf((char) Integer.parseInt(name.substring(1), 10));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = args.length == 0 ? System.in : new FileInputStream(args[0]);
        System.out.println(htmlToText(readAll(new InputStreamReader(inputStream, "UTF-8"))));
    }
}
