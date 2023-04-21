package org.rgse.wikiinajar.helpers.wiki.render.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jens DOT fauth AT gmx DOT net
 * 
 */
public class DefinitionIndentFilter implements Filter {

    public String filter(String text) {
        String result = text;
        String regex = "(^([;:]+|[:]*\\.)([^\r\n]+)[\r\n]*)+";
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        while (m.find()) {
            String plainList = m.group();
            String formattedList = generateDefinitionIdent(plainList);
            int listStart = result.indexOf(plainList);
            String first = result.substring(0, listStart);
            String end = result.substring(listStart + plainList.length());
            result = first + formattedList + end;
        }
        return result;
    }

    private String generateDefinitionIdent(String text) {
        String[] lines = text.split("\n");
        StringBuffer buffer = new StringBuffer("");
        int iIndex = 0;
        int iCurrentDepth = 0;
        if (lines[0].charAt(0) == ';') {
            buffer.append("<dl>\n<dt><b>");
            buffer.append(lines[0].substring(1).trim());
            buffer.append("</b></dt><dd>\n");
            ++iIndex;
            ++iCurrentDepth;
        }
        for (; iIndex < lines.length; iIndex++) {
            String line = lines[iIndex].trim();
            int iDepth = getDepth(line);
            if (iDepth > iCurrentDepth) {
                for (int iCountDepth = iCurrentDepth; iCountDepth < iDepth; iCountDepth++) {
                    buffer.append("<dl>\n<dd>");
                }
                iCurrentDepth = iDepth;
            } else {
                if (iDepth == iCurrentDepth) {
                    buffer.append("</dd>\n<dd>");
                } else {
                    for (int iCountDepth = iCurrentDepth; iCountDepth > iDepth; iCountDepth--) {
                        buffer.append("</dd>\n</dl>\n");
                    }
                    iCurrentDepth = iDepth;
                }
            }
            buffer.append(line.substring(iDepth).trim());
        }
        if (iCurrentDepth > 0) {
            for (int iCountDepth = iCurrentDepth; iCountDepth > 0; iCountDepth--) {
                buffer.append("</dd>\n</dl>\n");
            }
        }
        return buffer.toString();
    }

    private int getDepth(String line) {
        for (int iCountDepth = 0; iCountDepth < line.length(); iCountDepth++) {
            if (line.charAt(iCountDepth) != ':') {
                return iCountDepth;
            }
        }
        return line.length();
    }
}
