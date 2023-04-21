package gov.nist.siplite.parser;

import gov.nist.siplite.address.*;
import gov.nist.siplite.header.*;
import gov.nist.core.*;

/**
 * Parser for the SIP request line.
 *
 * @version JAIN-SIP-1.1
 *
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 */
class RequestLineParser extends Parser {

    /**
     * Constructor with initial request line string.
     * @param requestLine initial request line
     */
    public RequestLineParser(String requestLine) {
        this.lexer = new Lexer("method_keywordLexer", requestLine);
    }

    /**
     * Constructor with initial lexer engine.
     * @param lexer initial lexer engine
     */
    public RequestLineParser(Lexer lexer) {
        this.lexer = lexer;
        this.lexer.selectLexer("method_keywordLexer");
    }

    /**
     * Invokes parser for the request line.
     * @return the parsed request line
     * @exception ParseException if a parsing error occurs
     */
    public RequestLine parse() throws ParseException {
        if (debug) dbg_enter("parse");
        try {
            RequestLine retval = new RequestLine();
            String m = method();
            lexer.SPorHT();
            retval.setMethod(m);
            this.lexer.selectLexer("sip_urlLexer");
            URLParser urlParser = new URLParser(this.getLexer());
            URI url = urlParser.uriReference();
            lexer.SPorHT();
            retval.setUri(url);
            this.lexer.selectLexer("request_lineLexer");
            String v = sipVersion();
            retval.setSipVersion(v);
            lexer.SPorHT();
            lexer.match('\n');
            return retval;
        } finally {
            if (debug) dbg_leave("parse");
        }
    }
}
