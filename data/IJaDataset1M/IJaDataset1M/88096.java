package com.g2inc.scap.library.content.style;

import java.util.regex.Pattern;

public class ContentStylePattern {

    /**
	 * The pattern to match against.
	 */
    private Pattern pattern = null;

    /**
	 * An explanation of what the pattern intends to recognize
	 * in case you want to tell the user the pattern didn't match
	 * but want to indicate what the text they supplied should look
	 * like without showing them a complicated regex.
	 * 
	 */
    private String friendlyExplanation = null;

    public ContentStylePattern(Pattern p, String humanReadableText) {
        setPattern(p);
        setFriendlyExplanation(humanReadableText);
    }

    /**
	 * Return the Pattern object.
	 * 
	 * @return Pattern
	 */
    public Pattern getPattern() {
        return pattern;
    }

    /**
	 * Set the Pattern obect.
	 * 
	 * @param pattern
	 */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
	 * An explanation of what the pattern intends to recognize
	 * in case you want to tell the user the pattern didn't match
	 * but want to indicate what the text they supplied should look
	 * like without showing them a complicated regex.
	 * 
	 * @return String.
	 */
    public String getFriendlyExplanation() {
        return friendlyExplanation;
    }

    /**
	 * Set the friendly explanation.
	 * 
	 * @param humanReadableExplanation
	 */
    public void setFriendlyExplanation(String humanReadableExplanation) {
        this.friendlyExplanation = humanReadableExplanation;
    }

    /**
	 * Overriding the default toString() method to something more meaningful.
	 */
    @Override
    public String toString() {
        return (pattern != null ? pattern.pattern() : null) + " -- " + friendlyExplanation;
    }
}
