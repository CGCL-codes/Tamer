package com.javaeedev.util;

import java.util.HashMap;
import java.util.Map;

public final class StringUtil {

    private static final char CHAR_SPACE = ' ';

    private static final char CHAR_CHINESE_SPACE = '　';

    private static final String TAG_SCRIPT_START = "<script";

    private static final String TAG_SCRIPT_END = "</script>";

    private static final String TAG_OBJECT_START = "<object";

    private static final String TAG_OBJECT_END = "</object>";

    private static final String TAG_IFRAME_START = "<iframe";

    private static final String TAG_IFRAME_END = "</iframe>";

    public static boolean isEmpty(String s) {
        if (s == null) return true;
        return s.trim().length() == 0;
    }

    /**
     * Compact String by removing space, tab, etc.
     */
    public static String compact(String s) {
        char[] cs = new char[s.length()];
        int len = 0;
        for (int n = 0; n < cs.length; n++) {
            char c = s.charAt(n);
            if (c == CHAR_SPACE || c == '\t' || c == '\r' || c == '\n' || c == CHAR_CHINESE_SPACE) continue;
            cs[len] = c;
            len++;
        }
        return new String(cs, 0, len);
    }

    public static String removeScriptTags(String html) {
        if (html == null) return "";
        boolean found;
        int count = 0;
        int start, end;
        StringBuilder sb = new StringBuilder(html);
        StringBuilder sb_lowercase = new StringBuilder(html.toLowerCase());
        do {
            found = false;
            start = sb_lowercase.lastIndexOf(TAG_SCRIPT_START);
            end = sb_lowercase.lastIndexOf(TAG_SCRIPT_END);
            if (start < end && start >= 0) {
                found = true;
                count++;
                sb_lowercase.delete(start, end + 9);
                sb.delete(start, end + 9);
            } else {
                start = sb_lowercase.lastIndexOf(TAG_OBJECT_START);
                end = sb_lowercase.lastIndexOf(TAG_OBJECT_END);
                if (start < end && start >= 0) {
                    found = true;
                    count++;
                    sb_lowercase.delete(start, end + 9);
                    sb.delete(start, end + 9);
                }
            }
            start = sb_lowercase.lastIndexOf(TAG_IFRAME_START);
            end = sb_lowercase.lastIndexOf(TAG_IFRAME_END);
            if (start < end && start >= 0) {
                found = true;
                count++;
                sb_lowercase.delete(start, end + 9);
                sb.delete(start, end + 9);
            }
        } while (found);
        if (count == 0) return html;
        return sb.toString();
    }

    /**
     * Encode text to html.
     */
    public static String encodeHtml(String html) {
        StringBuilder sb = new StringBuilder(html.length());
        for (int i = 0; i < html.length(); i++) {
            char c = html.charAt(i);
            switch(c) {
                case ' ':
                    sb.append("&nbsp;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '\n':
                    sb.append("<br/>");
                    break;
                case '\r':
                    break;
                case '\"':
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String html2text(String html) {
        StringBuilder sb = new StringBuilder(html.length());
        char[] data = html.toCharArray();
        int start = 0;
        boolean previousIsPre = false;
        Token token = null;
        for (; ; ) {
            token = parse(data, start, previousIsPre);
            if (token == null) break;
            previousIsPre = token.isPreTag();
            sb = sb.append(token.getText());
            start += token.getLength();
        }
        return sb.toString();
    }

    private static Token parse(char[] data, int start, boolean previousIsPre) {
        if (start >= data.length) return null;
        char c = data[start];
        if (c == '<') {
            int end_index = indexOf(data, start + 1, '>');
            if (end_index == (-1)) {
                return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
            }
            String s = new String(data, start, end_index - start + 1);
            if (s.startsWith("<!--")) {
                int end_comment_index = indexOf(data, start + 1, "-->");
                if (end_comment_index == (-1)) {
                    return new Token(Token.TOKEN_COMMENT, data, start, data.length, previousIsPre);
                } else return new Token(Token.TOKEN_COMMENT, data, start, end_comment_index + 3, previousIsPre);
            }
            String s_lowerCase = s.toLowerCase();
            if (s_lowerCase.startsWith("<script")) {
                int end_script_index = indexOf(data, start + 1, "</script>");
                if (end_script_index == (-1)) return new Token(Token.TOKEN_SCRIPT, data, start, data.length, previousIsPre); else return new Token(Token.TOKEN_SCRIPT, data, start, end_script_index + 9, previousIsPre);
            } else {
                return new Token(Token.TOKEN_TAG, data, start, start + s.length(), previousIsPre);
            }
        }
        int next_tag_index = indexOf(data, start + 1, '<');
        if (next_tag_index == (-1)) return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
        return new Token(Token.TOKEN_TEXT, data, start, next_tag_index, previousIsPre);
    }

    private static int indexOf(char[] data, int start, String s) {
        char[] ss = s.toCharArray();
        for (int i = start; i < (data.length - ss.length); i++) {
            boolean match = true;
            for (int j = 0; j < ss.length; j++) {
                if (data[i + j] != ss[j]) {
                    match = false;
                    break;
                }
            }
            if (match) return i;
        }
        return (-1);
    }

    private static int indexOf(char[] data, int start, char c) {
        for (int i = start; i < data.length; i++) {
            if (data[i] == c) return i;
        }
        return (-1);
    }
}

class Token {

    public static final int TOKEN_TEXT = 0;

    public static final int TOKEN_COMMENT = 1;

    public static final int TOKEN_TAG = 2;

    public static final int TOKEN_SCRIPT = 3;

    private static final char[] TAG_BR = "<br".toCharArray();

    private static final char[] TAG_P = "<p".toCharArray();

    private static final char[] TAG_LI = "<li".toCharArray();

    private static final char[] TAG_PRE = "<pre".toCharArray();

    private static final char[] TAG_HR = "<hr".toCharArray();

    private static final char[] END_TAG_TD = "</td>".toCharArray();

    private static final char[] END_TAG_TR = "</tr>".toCharArray();

    private static final char[] END_TAG_LI = "</li>".toCharArray();

    private static final Map<String, String> SPECIAL_CHARS = new HashMap<String, String>();

    private int type;

    private String html;

    private String text = null;

    private int length = 0;

    private boolean isPre = false;

    static {
        SPECIAL_CHARS.put("&quot;", "\"");
        SPECIAL_CHARS.put("&lt;", "<");
        SPECIAL_CHARS.put("&gt;", ">");
        SPECIAL_CHARS.put("&amp;", "&");
        SPECIAL_CHARS.put("&reg;", "(r)");
        SPECIAL_CHARS.put("&copy;", "(c)");
        SPECIAL_CHARS.put("&nbsp;", " ");
        SPECIAL_CHARS.put("&pound;", "?");
    }

    public Token(int type, char[] data, int start, int end, boolean previousIsPre) {
        this.type = type;
        this.length = end - start;
        this.html = new String(data, start, length);
        parseText(previousIsPre);
    }

    public int getLength() {
        return length;
    }

    public boolean isPreTag() {
        return isPre;
    }

    private void parseText(boolean previousIsPre) {
        if (type == TOKEN_TAG) {
            char[] cs = html.toCharArray();
            if (compareTag(TAG_BR, cs) || compareTag(TAG_P, cs)) text = "\n"; else if (compareTag(TAG_LI, cs)) text = "\n* "; else if (compareTag(TAG_PRE, cs)) isPre = true; else if (compareTag(TAG_HR, cs)) text = "\n--------\n"; else if (compareString(END_TAG_TD, cs)) text = "\t"; else if (compareString(END_TAG_TR, cs) || compareString(END_TAG_LI, cs)) text = "\n";
        } else if (type == TOKEN_TEXT) {
            text = toText(html, previousIsPre);
        }
    }

    public String getText() {
        return text == null ? "" : text;
    }

    private String toText(String html, final boolean isPre) {
        char[] cs = html.toCharArray();
        StringBuilder buffer = new StringBuilder(cs.length);
        int start = 0;
        boolean continueSpace = false;
        char current, next;
        for (; ; ) {
            if (start >= cs.length) break;
            current = cs[start];
            if (start + 1 < cs.length) next = cs[start + 1]; else next = '\0';
            if (current == ' ') {
                if (isPre || !continueSpace) buffer = buffer.append(' ');
                continueSpace = true;
                start++;
                continue;
            }
            if (current == '\r' && next == '\n') {
                if (isPre) buffer = buffer.append('\n');
                start += 2;
                continue;
            }
            if (current == '\n' || current == '\r') {
                if (isPre) buffer = buffer.append('\n');
                start++;
                continue;
            }
            continueSpace = false;
            if (current == '&') {
                int length = readUtil(cs, start, ';', 10);
                if (length == (-1)) {
                    buffer = buffer.append('&');
                    start++;
                    continue;
                } else {
                    String spec = new String(cs, start, length);
                    String specChar = SPECIAL_CHARS.get(spec);
                    if (specChar != null) {
                        buffer = buffer.append(specChar);
                        start += length;
                        continue;
                    } else {
                        if (next == '#' && cs[start + length - 1] == ';') {
                            String num = new String(cs, start + 2, length - 3);
                            try {
                                int code = Integer.parseInt(num);
                                if (code > 0 && code < 65536) {
                                    buffer = buffer.append((char) code);
                                    start += length;
                                    continue;
                                }
                            } catch (Exception e) {
                            }
                            buffer = buffer.append("&#");
                            start += 2;
                            continue;
                        } else {
                            buffer = buffer.append('&');
                            start++;
                            continue;
                        }
                    }
                }
            } else {
                buffer = buffer.append(current);
                start++;
                continue;
            }
        }
        return buffer.toString();
    }

    private int readUtil(final char[] cs, final int start, final char util, final int maxLength) {
        int end = start + maxLength;
        if (end > cs.length) end = cs.length;
        for (int i = start; i < start + maxLength; i++) {
            if (i < cs.length && cs[i] == util) {
                return i - start + 1;
            }
        }
        return (-1);
    }

    private boolean compareTag(final char[] ori_tag, char[] tag) {
        if (ori_tag.length >= tag.length) return false;
        for (int i = 0; i < ori_tag.length; i++) {
            if (Character.toLowerCase(tag[i]) != ori_tag[i]) return false;
        }
        if (tag.length > ori_tag.length) {
            char c = Character.toLowerCase(tag[ori_tag.length]);
            if (c < 'a' || c > 'z') return true;
            return false;
        }
        return true;
    }

    private boolean compareString(final char[] ori, char[] comp) {
        if (ori.length > comp.length) return false;
        for (int i = 0; i < ori.length; i++) {
            if (Character.toLowerCase(comp[i]) != ori[i]) return false;
        }
        return true;
    }

    public String toString() {
        return html;
    }
}
