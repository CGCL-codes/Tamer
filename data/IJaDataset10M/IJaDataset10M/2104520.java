package com.ivata.mask.web.format;

import org.apache.log4j.Logger;

/**
 * <p>
 * Format a string by appending a leading character. You can specify either an
 * absolute length of the final string, or a number of characters to append.
 * </p>
 *
 * @since ivata masks 0.4 (2002-10-24)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.5 $
 */
public class LeadingCharacterFormat implements HTMLFormat {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(LeadingCharacterFormat.class);

    /**
     * <p>
     * If <code>false</code>, the <code>count</code> from {@link #setCount
     * setCount} represents a total number of characters to prepend. Otherwise (
     * <code>true</code>- default setting) it represents a maximum string
     * length for the whole string returned in {@link #format format}.
     * </p>
     */
    private boolean countIsMaximum = true;

    /**
     * <p>
     * This character will be prepended to the string.
     * </p>
     */
    private char character = ' ';

    /**
     * <p>
     * The number of times the character will be prepended.
     * </p>
     */
    private int count = 1;

    /**
     * <p>
     * Takes the text provided and prepends the character defined in
     * {@link #setCharacter setCharacter}the number of times defined in
     * {@link #setCount setCount}. If you specified this is a maximum (
     * {@link #setMaximum setMaximum(true)}), then only the rightmost count of
     * characters are returned.
     * </p>
     *
     * @param hTMLText <copyDoc>Refer to {@link HTMLFormat#format}.</copyDoc>
     * @return <copyDoc>Refer to {@link HTMLFormat#format}.</copyDoc>
     */
    public final String format(final String hTMLText) {
        if (logger.isDebugEnabled()) {
            logger.debug("format(String hTMLText = " + hTMLText + ") - start");
        }
        int length = hTMLText.length();
        if (countIsMaximum && (length > count)) {
            String returnString = hTMLText.substring(length - count - 1);
            if (logger.isDebugEnabled()) {
                logger.debug("format(String) - end - return value = " + returnString);
            }
            return returnString;
        }
        StringBuffer buffer = new StringBuffer(hTMLText).reverse();
        for (int i = 0; (i < count) && (!countIsMaximum || (buffer.length() < count)); ++i) {
            buffer.append(character);
        }
        String returnString = buffer.reverse().toString();
        if (logger.isDebugEnabled()) {
            logger.debug("format(String) - end - return value = " + returnString);
        }
        return returnString;
    }

    /**
     * <p>
     * Get whether the number specified in {@link #setCount setCount}should be
     * a maximum string length, or the number of characters to prepend.
     *
     * @return <code>false</code> if the <code>count</code> from {@link
     *         #setCount setCount} represents a total number of characters to
     *         prepend, or <code>true</code>- default setting - represents a
     *         maximum string length for the whole string returned in
     *         {@link #format}.
     *         </p>
     */
    public final boolean getCountIsMaximum() {
        if (logger.isDebugEnabled()) {
            logger.debug("getCountIsMaximum() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getCountIsMaximum() - end - return value = " + countIsMaximum);
        }
        return countIsMaximum;
    }

    /**
     * <p>
     * Set whether the number specified in {@link #setCount setCount}should be
     * a maximum string length, or the number of characters to prepend.
     *
     * @param countIsMaximumParam
     *            set to <code>false</code> if the <code>count</code> from
     *            {@link #setCount setCount}represents a total number of
     *            characters to prepend, or <code>true</code>- default
     *            setting - represents a maximum string length for the whole
     *            string returned in {@link #format format}.
     *            </p>
     */
    public final void setCountIsMaximum(final boolean countIsMaximumParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setCountIsMaximum(boolean countIsMaximumParam = " + countIsMaximumParam + ") - start");
        }
        this.countIsMaximum = countIsMaximumParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setCountIsMaximum(boolean) - end");
        }
    }

    /**
     * <p>
     * This character will be prepended to the string.
     * </p>
     *
     * @return the current value of the character which will prepended.
     */
    public final char getCharacter() {
        if (logger.isDebugEnabled()) {
            logger.debug("getCharacter() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getCharacter() - end - return value = " + character);
        }
        return character;
    }

    /**
     * <p>
     * This character will be prepended to the string.
     * </p>
     *
     * @param characterParam
     *            the new value of the character which will be prepended.
     */
    public final void setCharacter(final char characterParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setCharacter(char characterParam = " + characterParam + ") - start");
        }
        this.character = characterParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setCharacter(char) - end");
        }
    }

    /**
     * <p>
     * The number of times the character will be prepended.
     * </p>
     *
     * @return the current value of count.
     */
    public final int getCount() {
        if (logger.isDebugEnabled()) {
            logger.debug("getCount() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getCount() - end - return value = " + count);
        }
        return count;
    }

    /**
     * <p>
     * The number of times the character will be prepended.
     * </p>
     *
     * @param countParam
     *            the new value of count.
     */
    public final void setCount(final int countParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setCount(int countParam = " + countParam + ") - start");
        }
        this.count = countParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setCount(int) - end");
        }
    }
}
