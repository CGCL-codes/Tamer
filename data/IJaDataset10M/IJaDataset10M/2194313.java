package com.ivata.mask.web.tag.webgui.frame;

import org.apache.log4j.Logger;
import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import com.ivata.mask.web.tag.webgui.ControlTag;

/**
 * <p>
 * Create a space between buttons at the foot of a page.
 * </p>
 *
 * <p>
 * <b>Note: </b> this tag currently has no attributes.
 * </p>
 *
 * @since ivata masks 0.4 (2003-01-28)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.5 $
 */
public final class ButtonSpacerTag extends ControlTag {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ButtonSpacerTag.class);

    /**
     * Serialization version (for <code>Serializable</code> interface).
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * This method contains all the functionality to process the body of the
     * tag.
     * </p>
     *
     * @param out
     *            used to write the body output to
     * @param bodyContent
     *            returned by <code>getBodyContent</code>
     * @throws IOException
     *             thrown by <code>JspWriter.println</code>
     * @return this method always returns <code>SKIP_BODY</code>.
     */
    public int writeTagBodyContent(final JspWriter out, final BodyContent bodyContent) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("writeTagBodyContent(JspWriter out = " + out + ", BodyContent bodyContent = " + bodyContent + ") - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("writeTagBodyContent - end - return value = " + SKIP_BODY);
        }
        return SKIP_BODY;
    }
}
