package com.sun.mail.imap.protocol;

import com.sun.mail.iap.*;

/**
 * This class 
 *
 * @version 1.8, 07/05/04
 * @author  John Mani
 */
public class RFC822SIZE implements Item {

    static final char[] name = { 'R', 'F', 'C', '8', '2', '2', '.', 'S', 'I', 'Z', 'E' };

    public int msgno;

    public int size;

    /**
     * Constructor
     * @param port	portnumber to connect to
     */
    public RFC822SIZE(FetchResponse r) throws ParsingException {
        msgno = r.getNumber();
        r.skipSpaces();
        size = r.readNumber();
    }
}
