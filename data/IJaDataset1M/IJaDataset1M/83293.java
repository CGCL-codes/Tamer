package com.ivata.mask.web.format;

import org.apache.log4j.Logger;
import java.util.StringTokenizer;

/**
 * <p>
 * Convert line breaks into HTML break tags.
 * </p>
 *
 * @since ivata masks 0.4 (2002-06-19)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.5 $
 */
public class LineBreakFormat implements HTMLFormat {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(LineBreakFormat.class);

    /**
     * <p>
     * Stores the string which is prepended to each new line.
     * </p>
     */
    private String prepend = "";

    /**
     * <p>
     * If set to <code>true</code>, then all line break characters in the
     * <code>htmText</code> (see {@link #format format}) are converted into
     * HTML line-breaks (&lt;br/&gt;).
     * </p>
     */
    private boolean convertLineBreaks = false;

    /**
     * <p>
     * Convert all line breaks in the text provided to &lt;br/&gt; tags, and
     * prepend a string to new each line. One example where this is required is
     * in'quoted' return emails, where each line is traditionally preceded by
     * the 'greater than' symbol &gt;
     * </p>
     *
     * @param hTMLTextParam
     *            HTML text to convert line breaks in.
     * @return formatted text, with all of the line breaks converted to HTML
     *         tags
     */
    public final String format(final String hTMLTextParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("format(String hTMLTextParam = " + hTMLTextParam + ") - start");
        }
        String hTMLText = hTMLTextParam;
        int index = 0;
        if (convertLineBreaks) {
            while ((index = hTMLText.indexOf('\n')) != -1) {
                if (index > 1) {
                    hTMLText = hTMLText.substring(0, index) + "<br/>" + prepend + hTMLText.substring(index + 1);
                } else {
                    hTMLText = "<br/>" + prepend + hTMLText.substring(index + 1);
                }
            }
        } else if (!prepend.equals("")) {
            String sNew = "";
            StringTokenizer st = new StringTokenizer(hTMLText, "\n");
            while (st.hasMoreTokens()) {
                if (!sNew.equals("")) {
                    sNew += "\n";
                }
                sNew += prepend + st.nextToken();
            }
            hTMLText = sNew;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("format(String) - end - return value = " + hTMLText);
        }
        return hTMLText;
    }

    /**
     * <p>
     * Get the string which is prepended to each new line.
     * </p>
     *
     * @return the current value of the string to prepend to each line.
     */
    public final String getPrepend() {
        if (logger.isDebugEnabled()) {
            logger.debug("getPrepend() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getPrepend() - end - return value = " + prepend);
        }
        return prepend;
    }

    /**
     * <p>
     * Set the string which is prepended to each new line.
     * </p>
     *
     * @param prependParam
     *            the new value of the string to prepend to each line.
     */
    public final void setPrepend(final String prependParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setPrepend(String prependParam = " + prependParam + ") - start");
        }
        this.prepend = prependParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setPrepend(String) - end");
        }
    }

    /**
     * <p>
     * Get whether or not we should convert line breaks. If set to
     * <code>true</code>, then all line break characters in the
     * <code>htmText</code> (see {@link #format format}) are converted into
     * HTML line-breaks (&lt;br/&gt;).
     * </p>
     *
     * @return <code>true</code> if line breaks are converted, otherwise
     *         <code>false</code>.
     */
    public final boolean getConvertLineBreaks() {
        if (logger.isDebugEnabled()) {
            logger.debug("getConvertLineBreaks() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getConvertLineBreaks() - end - return value = " + convertLineBreaks);
        }
        return convertLineBreaks;
    }

    /**
     * <p>
     * Set whether or not we should convert line breaks. If set to
     * <code>true</code>, then all line break characters in the
     * <code>hTMLText</code> (see {@link #format format}) are converted into
     * HTML line-breaks (&lt;br/&gt;).
     * </p>
     *
     * @param convertLineBreaksParam
     *            set to <code>true</code> if line breaks should be converted,
     *            otherwise <code>false</code>.
     */
    public final void setConvertLineBreaks(final boolean convertLineBreaksParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setConvertLineBreaks(boolean convertLineBreaksParam = " + convertLineBreaksParam + ") - start");
        }
        this.convertLineBreaks = convertLineBreaksParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setConvertLineBreaks(boolean) - end");
        }
    }
}
