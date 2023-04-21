package org.txt2xml.core;

/**
 * @author ns
 *
 * FixedFieldProcessor: assumes the text to be of fixed-length csv-format
 * that means text like "1234567890" has 3 Fields of length 3 : 
 * "123", "456", "789" and a reminder of "0"
 */
public class FixedFieldProcessor extends Processor {

    private int start = 0;

    private boolean haveFound = false;

    protected String Field;

    protected String length = "0";

    private int intlen = 0;

    /**
	 * @see org.txt2xml.core.Processor#findMatch()
	 */
    protected boolean findMatch() {
        if (haveFound) return false;
        intlen = Integer.parseInt(length);
        if (start + intlen > chars.length()) return false;
        return true;
    }

    /**
	 * @see org.txt2xml.core.Processor#getMatchedText()
	 */
    protected CharSequence getMatchedText() {
        haveFound = true;
        start = start + intlen;
        return chars.subSequence(start - intlen, start);
    }

    /**
	 * @see org.txt2xml.core.Processor#getRemainderText()
	 */
    protected CharSequence getRemainderText() {
        return chars.subSequence(start, chars.length());
    }

    /**
 * Returns the fieldName.
 * @return String
 */
    public String getField() {
        return Field;
    }

    /**
 * Sets the fieldName.
 * @param fieldName The fieldName to set
 */
    public void setField(String fieldName) {
        Field = fieldName;
    }

    /**
	 * Returns the length.
	 * @return String
	 */
    public String getLength() {
        return length;
    }

    /**
	 * Sets the length.
	 * @param length The length to set
	 */
    public void setLength(String length) {
        this.length = length;
    }

    protected void resetMatching() {
        super.resetMatching();
        start = 0;
        haveFound = false;
    }
}
