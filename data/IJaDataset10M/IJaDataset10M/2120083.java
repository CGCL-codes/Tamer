package org.columba.ristretto.imap.parser;

import org.columba.ristretto.message.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for MessageAttributes.
 * 
 * @author tstich
 *
 */
public class MessageAttributeParser {

    private static final Pattern msg_att_key = Pattern.compile(" ?(ENVELOPE|INTERNALDATE|RFC822.(HEADER|TEXT|SIZE)|UID|FLAGS|BODYSTRUCTURE|BODY(\\[[^\\]]*\\])?) ");

    /**
	 * Parse the attributes.
	 * 
	 * @param input
	 * @return the Attributes.
	 */
    public static Attributes parse(String input) {
        String strippedParentheses = input.substring(1, input.length() - 1);
        Matcher matcher = msg_att_key.matcher(strippedParentheses);
        Attributes result = new Attributes();
        String lastKey = null;
        int lastEnd = 0;
        while (matcher.find()) {
            if (lastKey != null) {
                result.put(lastKey, strippedParentheses.substring(lastEnd, matcher.start()));
            }
            lastKey = matcher.group(1);
            if (lastKey.startsWith("BODY") && !lastKey.equals("BODYSTRUCTURE")) {
                lastKey = "BODY";
            }
            lastEnd = matcher.end();
        }
        if (lastKey != null) {
            result.put(lastKey, strippedParentheses.substring(lastEnd, strippedParentheses.length()));
        }
        return result;
    }
}
