package org.hl7.CTSMAPI;

/**
 * The format of the match text wasn't parsable
 */
public final class BadlyFormedMatchText extends java.lang.Exception {

    public org.hl7.types.ST matchText = null;

    public BadlyFormedMatchText() {
    }

    public BadlyFormedMatchText(org.hl7.types.ST _matchText) {
        matchText = _matchText;
    }

    public BadlyFormedMatchText(String $reason, org.hl7.types.ST _matchText) {
        super($reason);
        matchText = _matchText;
    }
}
