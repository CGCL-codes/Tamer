package org.columba.ristretto.imap.parser;

import org.columba.ristretto.imap.CopyInfo;
import org.columba.ristretto.imap.IMAPResponse;
import org.columba.ristretto.parser.ParserException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for the CopyInfo.
 * 
 * @author tstich
 *
 */
public class CopyInfoParser {

    private static final Pattern copyInfoPattern = Pattern.compile("^COPYUID ([0-9]*) ([^ ]*) ([^ ]*)");

    /**
	 * Parse the CopyInfo of an IMAP response.
	 * 
	 * @param response
	 * @return the CopyInfo
	 * @throws ParserException
	 */
    public static CopyInfo parse(IMAPResponse response) throws ParserException {
        String in = response.getResponseTextCode().getStringValue();
        Matcher matcher = copyInfoPattern.matcher(in);
        if (matcher.find()) {
            try {
                return new CopyInfo(Long.parseLong(matcher.group(1)), SequenceSetParser.parse(matcher.group(2)), SequenceSetParser.parse(matcher.group(3)));
            } catch (Exception e) {
                throw new ParserException(e);
            }
        } else {
            throw new ParserException("No COPYUID response : " + in);
        }
    }
}
