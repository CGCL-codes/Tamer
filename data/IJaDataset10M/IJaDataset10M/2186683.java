package com.hifiremote.jp1;

/**
 * The Class HexIntegerFormatter.
 */
public class HexIntegerFormatter extends RegexFormatter {

    /**
   * Instantiates a new hex integer formatter.
   * 
   * @param bits the bits
   */
    public HexIntegerFormatter(int bits) {
        super();
        setValueClass(HexInteger.class);
        setBits(bits);
        setCommitsOnValidEdit(true);
    }

    /**
   * Sets the bits.
   * 
   * @param bits the new bits
   */
    public void setBits(int bits) {
        String textPattern = null;
        if (bits < 5) textPattern = patterns[bits - 1]; else if (bits < 9) textPattern = patterns[bits - 5] + '?' + hexDigitPattern; else if (bits < 13) textPattern = patterns[bits - 9] + '?' + hexDigitPattern + "{1,2}"; else textPattern = patterns[bits - 13] + '?' + hexDigitPattern + "{1,3}";
        setPattern(textPattern);
    }

    /** The hex digit pattern. */
    private String hexDigitPattern = "\\p{XDigit}";

    /** The patterns. */
    private String[] patterns = { "[01]", "[0-3]", "[0-7]", hexDigitPattern };
}
