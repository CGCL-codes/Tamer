package gov.nist.javax.sip.parser;

import gov.nist.javax.sip.header.*;
import gov.nist.core.*;
import java.text.ParseException;

/**
 * Parser for ContentLanguage header.
 *
 * @version 1.2 $Revision: 1.7 $ $Date: 2006/07/13 09:01:56 $
 *
 * @author Olivier Deruelle   <br/>
 * @author M. Ranganathan   <br/>
 *
 * 
 *
 * @version 1.0
 */
public class ContentEncodingParser extends HeaderParser {

    /**
	 * Creates a new instance of ContentEncodingParser
	 * @param contentEncoding the header to parse 
	 */
    public ContentEncodingParser(String contentEncoding) {
        super(contentEncoding);
    }

    /**
	 * Constructor
	 * @param lexer the lexer to use to parse the header
	 */
    protected ContentEncodingParser(Lexer lexer) {
        super(lexer);
    }

    /**
	 * parse the ContentEncodingHeader String header
	 * @return SIPHeader (ContentEncodingList object)
	 * @throws SIPParseException if the message does not respect the spec.
	 */
    public SIPHeader parse() throws ParseException {
        if (debug) dbg_enter("ContentEncodingParser.parse");
        ContentEncodingList list = new ContentEncodingList();
        try {
            headerName(TokenTypes.CONTENT_ENCODING);
            while (lexer.lookAhead(0) != '\n') {
                ContentEncoding cl = new ContentEncoding();
                cl.setHeaderName(SIPHeaderNames.CONTENT_ENCODING);
                this.lexer.SPorHT();
                this.lexer.match(TokenTypes.ID);
                Token token = lexer.getNextToken();
                cl.setEncoding(token.getTokenValue());
                this.lexer.SPorHT();
                list.add(cl);
                while (lexer.lookAhead(0) == ',') {
                    cl = new ContentEncoding();
                    this.lexer.match(',');
                    this.lexer.SPorHT();
                    this.lexer.match(TokenTypes.ID);
                    this.lexer.SPorHT();
                    token = lexer.getNextToken();
                    cl.setEncoding(token.getTokenValue());
                    this.lexer.SPorHT();
                    list.add(cl);
                }
            }
            return list;
        } catch (ParseException ex) {
            throw createParseException(ex.getMessage());
        } finally {
            if (debug) dbg_leave("ContentEncodingParser.parse");
        }
    }
}
