package org.mobicents.servlet.sip.catalina.rules.request;

import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;

/**
 * @author Thomas Leseney 
 */
public class User implements Extractor {

    public User(String token) {
        if (!token.equals("uri")) {
            throw new IllegalArgumentException("Invalid expression: user after " + token);
        }
    }

    public Object extract(Object input) {
        URI uri = (URI) input;
        if (uri.isSipURI()) {
            return ((SipURI) uri).getUser();
        } else {
            return null;
        }
    }
}
