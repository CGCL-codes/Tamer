package org.jreform.criteria;

/**
 * Checks that the length of the value is within the given range. 
 * 
 * @author armandino (at) gmail.com
 */
public final class Length extends AbstractCriterion<String> {

    private int min;

    private int max;

    Length(int min, int max) {
        this.min = min;
        this.max = max;
    }

    protected boolean verify(String value) {
        return value.length() <= max && value.length() >= min;
    }

    protected String generateErrorMessage() {
        return "The value must be between " + min + " and " + max + " characters long";
    }
}
