package org.jwos.plugin.mail.parser;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jwos.plugin.mail.domain.Email;
import org.jwos.plugin.mail.domain.EmailPart;
import org.jwos.plugin.mail.util.Utility;

public class HTMLMessageParser {

    private static Locale loc = new Locale("en", "US");

    /**
     * Default Constructor
     */
    public HTMLMessageParser() {
        super();
    }

    public static String prepareInlineHTMLContent(Email msg, String str) {
        if ((str == null) || (str.equals(""))) return "";
        int i = -1;
        String tmp = null;
        String contentId = null;
        int j = -1;
        int partId = -1;
        while ((i = str.indexOf("\"cid:")) != -1) {
            tmp = str.substring(i + 5);
            j = tmp.indexOf("\"");
            contentId = tmp.substring(0, j);
            partId = getPartIdByContentId(msg, contentId);
            str = str.substring(0, i) + "\"dumpPart.service?partid=" + partId + "\"" + tmp.substring(j + 1);
        }
        return str;
    }

    public static String organizeLinks(String str) {
        String EXPR = null;
        StringBuffer buffer = null;
        Matcher matcher = null;
        int count = 0;
        String tag = null;
        try {
            EXPR = "target([ =\"\']*[\t\n\r:#0-9a-z\\./@~?&=;%_-]+[ \"']*)";
            buffer = new StringBuffer();
            matcher = Pattern.compile(EXPR, Pattern.CASE_INSENSITIVE).matcher(str);
            count = 0;
            while (matcher.find()) {
                count++;
                tag = matcher.group();
                tag = findTagValue(tag);
                matcher.appendReplacement(buffer, "");
            }
            matcher.appendTail(buffer);
            str = buffer.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        try {
            EXPR = " href([ =\"\']*[\t\n\r:#0-9a-z\\./@~?&=;%_-]+[ \"']*)";
            buffer = new StringBuffer();
            matcher = Pattern.compile(EXPR, Pattern.CASE_INSENSITIVE).matcher(str);
            count = 0;
            while (matcher.find()) {
                count++;
                tag = matcher.group();
                tag = findTagValue(tag);
                if (tag != null && tag.toLowerCase(loc).startsWith("mailto:")) {
                    tag = tag.substring(7);
                }
                if (tag.indexOf("@") > 0) {
                    tag = "javascript:parent.fastEmail('" + tag + "')";
                    matcher.appendReplacement(buffer, " href=\"" + tag + "\"");
                } else {
                    matcher.appendReplacement(buffer, " href=\"" + tag + "\" target=\"_blank\" ");
                }
            }
            matcher.appendTail(buffer);
            str = buffer.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String findTagValue(String tag) {
        int pos = tag.indexOf("=");
        tag = Utility.extendedTrim(tag.substring(pos + 1), "\"");
        tag = Utility.extendedTrim(tag, "'");
        return tag;
    }

    private static int getPartIdByContentId(Email msg, String cid) {
        ArrayList parts = msg.getParts();
        for (int i = 0; i < parts.size(); i++) {
            EmailPart part = (EmailPart) parts.get(i);
            String contentId = part.getContentId();
            String fileName = part.getFileName();
            if ((contentId != null && contentId.equals(cid)) || (fileName != null && fileName.equalsIgnoreCase(cid))) {
                return i;
            }
        }
        for (int i = 0; i < parts.size(); i++) {
            EmailPart part = (EmailPart) parts.get(i);
            String contentId = part.getContentId();
            contentId = Utility.extendedTrim(contentId, "<");
            contentId = Utility.extendedTrim(contentId, ">");
            if (contentId != null && contentId.equals(cid)) {
                return i;
            }
        }
        return -1;
    }
}
