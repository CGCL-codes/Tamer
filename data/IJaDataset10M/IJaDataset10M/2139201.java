package net.sunji.spring.plus.utils;

/**
 * @author seyoung
 * 
 */
public class HtmlUtils extends org.springframework.web.util.HtmlUtils {

    public static String filterScript(String message) {
        if (message == null) return (null);
        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch(content[i]) {
                case ')':
                    result.append("\\)");
                    break;
                case '(':
                    result.append("\\(");
                    break;
                case '\'':
                    result.append("\\'");
                    break;
                case '"':
                    result.append("\\\"");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return (result.toString());
    }

    public static String filterHTML(String message) {
        if (message == null) return "";
        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 50);
        for (int i = 0; i < content.length; i++) {
            switch(content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default:
                    result.append(content[i]);
            }
        }
        return (result.toString());
    }

    public static String br2nl(String html) {
        if (html == null) return "";
        html = html.replaceAll("<(br|BR)/?>", "\r\n");
        return html;
    }

    public static String nl2br(String text) {
        if (text == null) return "";
        text = text.replaceAll("\r(\n)?", "<br/>");
        return text;
    }

    public static String nbsp2space(String html) {
        if (html == null) return "";
        html = html.replaceAll("&nbsp;", " ");
        return html;
    }

    public static String space2nbsp(String text) {
        if (text == null) return "";
        text = text.replaceAll(" ", "&nbsp;");
        return text;
    }

    public static String escape(String text) {
        String html = htmlEscape(text);
        html = space2nbsp(html);
        html = nl2br(html);
        return html;
    }

    public static String unescape(String input) {
        String text = br2nl(input);
        text = nbsp2space(text);
        text = htmlUnescape(text);
        return text;
    }
}
