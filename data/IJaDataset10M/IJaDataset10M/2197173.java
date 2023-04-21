package com.ivata.mask.web.format;

import org.apache.log4j.Logger;

/**
 * <p>
 * This format word-wraps each line of text to a user-specified column.
 * </p>
 *
 * <p>
 * <b>Note: </b> by default, no wrapping will take place. You must call {@link
 * #setWordWrapColumn setWordWrapColumn}.
 * </p>
 *
 * @since ivata masks 0.4 (2002-06-19)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.4 $
 */
public class WordWrapFormat implements HTMLFormat {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(WordWrapFormat.class);

    /**
     * <p>
     * Private member used to store the value set in setWordWrapColumn.
     * </p>
     *
     * @see #setWordWrapColumn(int wordWrapColumn)
     */
    private int wordWrapColumn = 0;

    /**
     * <p>
     * Format the string given in <code>hTMLText</code>, wrapped to the
     * column provided by calling <code>setWordWrapColumn</code>.
     * </p>
     *
     * <p>
     * <b>Note: </b> by default, no wrapping will take place.
     * </p>
     *
     * @param hTMLTextParam
     *            the text to truncate.
     * @return text wrapped at the column specified.
     */
    public final String format(final String hTMLTextParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("format(String hTMLTextParam = " + hTMLTextParam + ") - start");
        }
        String hTMLText = hTMLTextParam;
        if (wordWrapColumn <= 0) {
            if (logger.isDebugEnabled()) {
                logger.debug("format(String) - end - return value = " + hTMLText);
            }
            return hTMLText;
        }
        String outString = "";
        int actualWordWrapColumn = this.wordWrapColumn;
        int wordWrapColumnCut = 0;
        while (!hTMLText.equals("")) {
            if (hTMLText.length() <= actualWordWrapColumn) {
                outString += hTMLText;
                hTMLText = "";
            } else {
                for (wordWrapColumnCut = 0; (wordWrapColumnCut < actualWordWrapColumn) && (hTMLText.charAt(wordWrapColumnCut) != '\n'); ) {
                    ++wordWrapColumnCut;
                }
                if (wordWrapColumnCut == actualWordWrapColumn) {
                    for (wordWrapColumnCut = actualWordWrapColumn; (wordWrapColumnCut >= 0) && !(hTMLText.charAt(wordWrapColumnCut) == ' '); ) {
                        --wordWrapColumnCut;
                    }
                    if (outString.equals("")) {
                        --actualWordWrapColumn;
                    }
                    if (wordWrapColumnCut > 0) {
                        outString += hTMLText.substring(0, wordWrapColumnCut) + "\n";
                        hTMLText = hTMLText.substring(wordWrapColumnCut + 1);
                    } else {
                        outString += hTMLText.substring(0, actualWordWrapColumn) + "\n";
                        hTMLText = hTMLText.substring(actualWordWrapColumn + 1);
                    }
                } else {
                    outString += hTMLText.substring(0, wordWrapColumnCut) + "\n";
                    hTMLText = hTMLText.substring(wordWrapColumnCut + 1);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("format(String) - end - return value = " + outString);
        }
        return outString;
    }

    /**
     * <p>
     * Private member used to store the value set in setWordWrapColumn.
     * </p>
     *
     * @see #setWordWrapColumn(int wordWrapColumn)
     *
     *
     * @return the current value of wordWrapColumn.
     */
    public final int getWordWrapColumn() {
        if (logger.isDebugEnabled()) {
            logger.debug("getWordWrapColumn() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getWordWrapColumn() - end - return value = " + wordWrapColumn);
        }
        return wordWrapColumn;
    }

    /**
     * <p>
     * Private member used to store the value set in setWordWrapColumn.
     * </p>
     *
     * @see #setWordWrapColumn(int wordWrapColumn)
     * @param wordWrapColumnParam
     *            the new value of wordWrapColumn.
     */
    public final void setWordWrapColumn(final int wordWrapColumnParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setWordWrapColumn(int wordWrapColumnParam = " + wordWrapColumnParam + ") - start");
        }
        this.wordWrapColumn = wordWrapColumnParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setWordWrapColumn(int) - end");
        }
    }
}
