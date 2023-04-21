package org.aitools.util.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom.Text;

/**
 * Utilities specific to (X)HTML handling.
 *
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 */
public class XHTML {

    private static final Pattern XHTML_BREAK_LINE_REGEX = Pattern.compile("[\r\n]*(<(.+?:)?br( .*?)?/>|<(.+?:)?p( .+?)?>|</(.+?:)?p>)[\r\n]*");

    private static final Pattern PRE_REGEX = Pattern.compile("<(?:.+?:)?pre(?: .+?)?>(.+?)</(?:.+?:)?pre>", Pattern.DOTALL);

    private static final Pattern LINEFEED_REGEX = Pattern.compile("[\r\n]+");

    private static final String[] EMPTY_STRING_ARRAY = { ("") };

    /**
     * Breaks a message into multiple lines at an (X)HTML &lt;br/&gt;, except if it
     * comes at the beginning of the message, or ending (X)HTML &lt;/p&gt;.
     * 
     * @param input the string to break
     * @return one line per array item
     */
    public static String[] breakLines(String input) {
        if (input == null) {
            return EMPTY_STRING_ARRAY;
        }
        String _input = input.trim();
        if (_input.equals("")) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> rawLines = Arrays.asList(XHTML_BREAK_LINE_REGEX.split(_input));
        List<String> preservedLines = new ArrayList<String>(rawLines.size());
        Matcher matcher;
        String normalizedLine;
        for (String line : rawLines) {
            matcher = PRE_REGEX.matcher(line);
            if (matcher.matches()) {
                for (String preLine : LINEFEED_REGEX.split(matcher.group(1))) {
                    if (preLine.length() > 0) {
                        preservedLines.add(preLine);
                    }
                }
            } else {
                normalizedLine = Text.normalizeString(line);
                if (normalizedLine.length() > 0) {
                    preservedLines.add(normalizedLine);
                }
            }
        }
        return preservedLines.toArray(EMPTY_STRING_ARRAY);
    }
}
