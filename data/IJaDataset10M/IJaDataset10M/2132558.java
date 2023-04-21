package com.ivata.mask.web.format;

import org.apache.log4j.Logger;

/**
 * Convert a string to upper or lower case.
 *
 * @since ivata masks 0.6 (2005-03-15)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class CaseFormat implements HTMLFormat {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(CaseFormat.class);

    /**
     * <copyDoc>Refer to {@link #isLower}.</copyDoc>
     */
    private boolean lower = true;

    /**
     * Converts the given text to either upper or lower (default) case,
     * depending on the setting of {@link #isLower lower}.
     *
     * @param textParam text to be converted.
     * @return text with all characters converted to either upper or lower case.
     */
    public String format(final String textParam) {
        if (lower) {
            return textParam.toLowerCase();
        } else {
            return textParam.toUpperCase();
        }
    }

    /**
     * If <code>true</code>, then <code>format</code> will convert to lower
     * case.
     * @return Returns <code>true</code>, if <code>format</code> should convert
     * to lower case, otherwise <code>false</code>.
     */
    public boolean isLower() {
        return lower;
    }

    /**
     * <copyDoc>Refer to {@link #isLower}.</copyDoc>
     * @param lowerParam <copyDoc>Refer to {@link #isLower}.</copyDoc>
     */
    public void setLower(final boolean lowerParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("Setting lower. Before '" + lower + "', after '" + lowerParam + "'");
        }
        lower = lowerParam;
    }
}
