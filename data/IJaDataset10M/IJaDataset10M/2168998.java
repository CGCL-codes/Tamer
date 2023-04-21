package br.com.arsmachina.accesslogger.services.impl;

import br.com.arsmachina.accesslogger.services.AccessFilterRule;

/**
 * {@link AccessFilterRule} that accepts requests which ends with a given string
 * and ignores the others.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class URLEndingAccessFilterRule implements AccessFilterRule {

    private final String ending;

    /**
	 * Single constructor of this class. 
	 * @param ending a {@link String} containing the request ending. It cannot be null
	 * nor the empty string.
	 */
    public URLEndingAccessFilterRule(String ending) {
        if (ending == null) {
            throw new IllegalArgumentException("Parameter ending cannot be null");
        }
        ending = ending.trim();
        if (ending.length() == 0) {
            throw new IllegalArgumentException("Parameter ending cannot the empty string");
        }
        this.ending = ending;
    }

    /**
	 * @see br.com.arsmachina.accesslogger.services.AccessFilterRule#accept(java.lang.String)
	 */
    public Boolean accept(String path) {
        if (path.endsWith(ending)) {
            return true;
        } else {
            return null;
        }
    }
}
