package gov.nist.siplite.parser;

import gov.nist.siplite.header.*;
import gov.nist.core.*;

/**
 * Parser for Event header.
 *
 * @version JAIN-SIP-1.1
 *
 * <a href="{@docRoot}/uncopyright.html">This code is in the public domain.</a>
 *
 * @version 1.0
 */
public class EventParser extends ParametersParser {

    /** Default constructor. */
    EventParser() {
    }

    /**
     * Creates a new instance of EventParser.
     * @param event the header to parse
     */
    public EventParser(String event) {
        super(event);
    }

    /**
     * Constructor with initial lexer engine.
     * @param lexer initial lexer engine
     */
    protected EventParser(Lexer lexer) {
        super(lexer);
    }

    /**
     * Parses the String message.
     * @return Header (Event object)
     * @throws SIPParseException if the message does not respect the spec.
     */
    public Header parse() throws ParseException {
        if (debug) dbg_enter("EventParser.parse");
        try {
            headerName(TokenTypes.EVENT);
            this.lexer.SPorHT();
            EventHeader event = new EventHeader();
            this.lexer.match(TokenTypes.ID);
            Token token = lexer.getNextToken();
            String value = token.getTokenValue();
            event.setEventType(value);
            super.parse(event);
            this.lexer.SPorHT();
            this.lexer.match('\n');
            return event;
        } catch (ParseException ex) {
            throw createParseException(ex.getMessage());
        } finally {
            if (debug) dbg_leave("EventParser.parse");
        }
    }
}
