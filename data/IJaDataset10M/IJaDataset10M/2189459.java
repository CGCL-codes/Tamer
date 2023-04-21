package org.kwantu.m2.xpath;

import org.kwantu.m2.KwantuContingencyException;

/**
 * Used for invalid XPath specifications in the User Interface.
 */
public class KwantuXPathInvalidException extends KwantuContingencyException {

    public KwantuXPathInvalidException(final String msg) {
        super(msg);
    }
}
