package org.cantaloop.tools.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {

    public static String getClassnameWithoutPackage(Class c) {
        String fqname = c.getName();
        String classname = fqname.substring(fqname.lastIndexOf('.') + 1);
        return classname;
    }

    public static String padLeft(int i, char c, String string) {
        int fillSize = i - string.length();
        return fill(c, fillSize) + string;
    }

    public static String fill(char c, int size) {
        if (size < 0) throw new IllegalArgumentException();
        StringBuffer sb = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String lastXCharacters(String string, int i) {
        int cutpos = string.length() - i;
        if (cutpos < 0) return string;
        return string.substring(cutpos);
    }

    public static String lastXCharacters(String string, int i, boolean prefixDots) {
        int cutpos = string.length() - i;
        if (cutpos < 0) return string;
        String prefix = "";
        if (prefixDots) {
            prefix = "...";
            cutpos += 3;
        }
        return prefix + string.substring(cutpos);
    }

    public static List breakLines(String longText, int width) {
        StringTokenizer strTok = new StringTokenizer(longText, " \n\t", false);
        List lines = new ArrayList();
        StringBuffer currentLine = new StringBuffer(strTok.nextToken());
        while (strTok.hasMoreTokens()) {
            String word = strTok.nextToken();
            if (currentLine.length() + word.length() >= width) {
                lines.add(currentLine.toString());
                currentLine = new StringBuffer(word);
            } else {
                currentLine.append(" ");
                currentLine.append(word);
            }
        }
        lines.add(currentLine.toString());
        return lines;
    }

    public static void printLines(int spaceCount, List lines, StringBuffer sb) {
        String spaces = fill(' ', spaceCount);
        for (Iterator it = lines.iterator(); it.hasNext(); ) {
            String line = (String) it.next();
            sb.append(spaces);
            sb.append(line);
            if (it.hasNext()) sb.append("\n");
        }
    }
}
