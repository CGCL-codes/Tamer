package jyt.model.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class HTML2Text {

    private static boolean body_found = false;

    private static boolean in_body = false;

    private static boolean pre = false;

    public static String convert(String source) throws Exception {
        StringBuffer result = new StringBuffer();
        StringBuffer result2 = new StringBuffer();
        StringReader input = new StringReader(source);
        try {
            String text = null;
            int c = input.read();
            while (c != -1) {
                text = "";
                if (c == '<') {
                } else if (c == '&') {
                    String specialchar = getSpecial(input);
                    if (specialchar.equals("lt;") || specialchar.equals("#60")) {
                        text = "<";
                    } else if (specialchar.equals("gt;") || specialchar.equals("#62")) {
                        text = ">";
                    } else if (specialchar.equals("amp;") || specialchar.equals("#38")) {
                        text = "&";
                    } else if (specialchar.equals("nbsp;")) {
                        text = " ";
                    } else if (specialchar.equals("quot;") || specialchar.equals("#34")) {
                        text = "\"";
                    } else if (specialchar.equals("copy;") || specialchar.equals("#169")) {
                        text = "[Copyright]";
                    } else if (specialchar.equals("reg;") || specialchar.equals("#174")) {
                        text = "[Registered]";
                    } else if (specialchar.equals("trade;") || specialchar.equals("#153")) {
                        text = "[Trademark]";
                    } else {
                        text = "&" + specialchar;
                    }
                } else if (!pre && Character.isWhitespace((char) c)) {
                    StringBuffer s = in_body ? result : result2;
                    if (s.length() > 0 && Character.isWhitespace(s.charAt(s.length() - 1))) {
                        text = "";
                    } else {
                        text = " ";
                    }
                } else {
                    text = "" + (char) c;
                }
                StringBuffer s = in_body ? result : result2;
                s.append(text);
                c = input.read();
            }
        } catch (Exception e) {
            input.close();
            throw e;
        }
        StringBuffer s = body_found ? result : result2;
        return s.toString().trim();
    }

    private static String getSpecial(Reader r) throws IOException {
        StringBuffer result = new StringBuffer();
        r.mark(1);
        int c = r.read();
        while (Character.isLetter((char) c)) {
            result.append((char) c);
            r.mark(1);
            c = r.read();
        }
        if (c == ';') {
            result.append(';');
        } else {
            r.reset();
        }
        return result.toString();
    }
}
