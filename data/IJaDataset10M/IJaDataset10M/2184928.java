package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;

/**
 * Represents a single 'word' in a parsed text, that is: a token does not
 * contain any spaces and is the main building block of other sentence parts.
 * 
 * @author rdenaux
 * 
 */
public interface IParsedToken extends IParsedPart {

    /**
     * Returns the {@link String} corresponding to this {@link IParsedToken}.
     * This should be the same as {@link IParsedPart#getOriginalString()}
     * 
     * @return
     */
    String getString();
}
