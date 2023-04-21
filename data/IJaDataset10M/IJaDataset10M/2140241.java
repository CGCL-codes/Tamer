package gov.nist.javax.sip.parser;

import gov.nist.javax.sip.header.*;
import java.text.ParseException;

/** 
 * Parser for authorization headers.
 *
 * @version 1.2 $Revision: 1.7 $ $Date: 2006/07/13 09:02:22 $
 *
 * @author Olivier Deruelle 
 * @author M. Ranganathan 
 * 
 *
 */
public class AuthorizationParser extends ChallengeParser {

    /**
	 * Constructor
	 * @param authorization Authorization message to parse
	 */
    public AuthorizationParser(String authorization) {
        super(authorization);
    }

    /**
	 * Cosntructor
	 * @param lexer Lexer to set
	 */
    protected AuthorizationParser(Lexer lexer) {
        super(lexer);
    }

    /**
	 * parse the String message 
	 * @return SIPHeader (Authorization object)
	 * @throws SIPParseException if the message does not respect the spec.
	 */
    public SIPHeader parse() throws ParseException {
        dbg_enter("parse");
        try {
            headerName(TokenTypes.AUTHORIZATION);
            Authorization auth = new Authorization();
            super.parse(auth);
            return auth;
        } finally {
            dbg_leave("parse");
        }
    }
}
