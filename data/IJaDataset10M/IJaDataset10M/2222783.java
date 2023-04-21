package org.ttt.salt;

import org.w3c.dom.Element;

/**
 *
 * @author Lance Finn Helsten
 * @version $Id: InvalidLanguageException.java 1 2008-05-23 03:51:58Z lanhel $
 * @license Licensed under the Apache License, Version 2.0.
 */
public class InvalidLanguageException extends XCSValidationException {

    /** SCM information. */
    public static final String RCSID = "$Id: InvalidLanguageException.java 1 2008-05-23 03:51:58Z lanhel $";

    /**
     * @param elem The XML element this exception occured on.
     */
    public InvalidLanguageException(Element elem) {
        super(elem);
    }

    /** {@inheritDoc} */
    public String getMessage() {
        String lang = null;
        if (element().hasAttribute("xml:lang")) lang = element().getAttribute("xml:lang"); else if (element().hasAttribute("lang")) lang = element().getAttribute("lang");
        return "Invalid language for tag: " + lang + " at " + buildContext();
    }

    /** {@inheritDoc} */
    public String getLocalizedMessage() {
        return getMessage();
    }
}
