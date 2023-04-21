package gov.nist.javax.sip.parser.chars.ims;

import gov.nist.javax.sip.parser.Lexer;
import gov.nist.javax.sip.parser.TokenTypes;
import java.text.ParseException;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.ims.PAssertedIdentity;
import gov.nist.javax.sip.header.ims.PAssertedIdentityList;
import gov.nist.javax.sip.header.ims.SIPHeaderNamesIms;
import gov.nist.javax.sip.parser.AddressParametersParser;

/**
 * @author ALEXANDRE MIGUEL SILVA SANTOS
 */
public class PAssertedIdentityParser extends AddressParametersParser implements TokenTypes {

    /**
     * Constructor
     * @param assertedIdentity -  message to parse to set
     */
    public PAssertedIdentityParser(String assertedIdentity) {
        super(assertedIdentity);
    }

    protected PAssertedIdentityParser(Lexer lexer) {
        super(lexer);
    }

    public SIPHeader parse() throws ParseException {
        if (debug) dbg_enter("AssertedIdentityParser.parse");
        PAssertedIdentityList assertedIdList = new PAssertedIdentityList();
        try {
            headerName(TokenTypes.P_ASSERTED_IDENTITY);
            PAssertedIdentity pai = new PAssertedIdentity();
            pai.setHeaderName(SIPHeaderNamesIms.P_ASSERTED_IDENTITY);
            super.parse(pai);
            assertedIdList.add(pai);
            this.lexer.SPorHT();
            while (lexer.lookAhead(0) == ',') {
                this.lexer.match(',');
                this.lexer.SPorHT();
                pai = new PAssertedIdentity();
                super.parse(pai);
                assertedIdList.add(pai);
                this.lexer.SPorHT();
            }
            this.lexer.SPorHT();
            this.lexer.match('\n');
            return assertedIdList;
        } finally {
            if (debug) dbg_leave("AssertedIdentityParser.parse");
        }
    }
}
