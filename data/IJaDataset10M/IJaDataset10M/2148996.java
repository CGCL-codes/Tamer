package gov.nist.javax.sip.parser;

import gov.nist.javax.sip.header.*;
import gov.nist.core.*;
import java.text.ParseException;

/**
 * Parser for ProxyRequire header.
 * @version 1.2 $Revision: 1.6 $ $Date: 2006/07/13 09:02:15 $
 *
 * @author Olivier Deruelle   <br/>
 * @author M. Ranganathan   <br/>
 *
 * 
 */
public class ProxyRequireParser extends HeaderParser {

    /**
	 * Creates a new instance of ProxyRequireParser 
	 * @param require the header to parse 
	 */
    public ProxyRequireParser(String require) {
        super(require);
    }

    /**
	 * Constructor
	 * @param lexer the lexer to use to parse the header
	 */
    protected ProxyRequireParser(Lexer lexer) {
        super(lexer);
    }

    /**
	 * parse the String message
	 * @return SIPHeader (ProxyRequireList object)
	 * @throws SIPParseException if the message does not respect the spec.
	 */
    public SIPHeader parse() throws ParseException {
        ProxyRequireList list = new ProxyRequireList();
        if (debug) dbg_enter("ProxyRequireParser.parse");
        try {
            headerName(TokenTypes.PROXY_REQUIRE);
            while (lexer.lookAhead(0) != '\n') {
                ProxyRequire r = new ProxyRequire();
                r.setHeaderName(SIPHeaderNames.PROXY_REQUIRE);
                this.lexer.match(TokenTypes.ID);
                Token token = lexer.getNextToken();
                r.setOptionTag(token.getTokenValue());
                this.lexer.SPorHT();
                list.add(r);
                while (lexer.lookAhead(0) == ',') {
                    this.lexer.match(',');
                    this.lexer.SPorHT();
                    r = new ProxyRequire();
                    this.lexer.match(TokenTypes.ID);
                    token = lexer.getNextToken();
                    r.setOptionTag(token.getTokenValue());
                    this.lexer.SPorHT();
                    list.add(r);
                }
            }
        } finally {
            if (debug) dbg_leave("ProxyRequireParser.parse");
        }
        return list;
    }
}
