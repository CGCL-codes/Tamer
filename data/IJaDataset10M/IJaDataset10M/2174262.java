package com.ivata.mask.web.tag.util;

import org.apache.log4j.Logger;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * <p>Loop increasing a counter nCounter until it reaches to (inclusive).</p>
 *
 * @since ivata masks 0.5 (2001-12-12)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.6 $
 * TODO: This class is a temporary workaround because we had problems with the
 * JSTL class.
 */
public class ForEachTag extends BodyTagSupport {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ForEachTag.class);

    /**
     * Serialization version (for <code>Serializable</code> interface).
     */
    private static final long serialVersionUID = 1L;

    /**
     * <copyDoc>Refer to {@link #getBegin}.</copyDoc>
     */
    private int begin = 0;

    /**
     * <copyDoc>Refer to {@link #getEnd}.</copyDoc>
     */
    private int end;

    /**
     * <copyDoc>Refer to {@link #getStep}.</copyDoc>
     */
    private int step = 1;

    /**
     * <copyDoc>Refer to {@link #getVar}.</copyDoc>
     */
    private int var = 0;

    /**
     * Name of an attribute to set the var value to.
     */
    private String varAttribute = "forEachTagVarVariable";

    /**
     * <copyDoc>Refer to {@link IterationTag#doAfterBody}.</copyDoc>
     *
     * @return <copyDoc>Refer to {@link IterationTag#doAfterBody}.</copyDoc>
     * @throws JspException
     * <copyDoc>Refer to {@link IterationTag#doAfterBody}.</copyDoc>
     */
    public int doAfterBody() throws JspException {
        if (logger.isDebugEnabled()) {
            logger.debug("doAfterBody() - start");
        }
        try {
            JspWriter out = getPreviousOut();
            BodyContent bodyContent = getBodyContent();
            bodyContent.writeOut(out);
            bodyContent.clearBody();
            bodyContent.clearBuffer();
        } catch (Exception ex) {
            logger.error("doAfterBody()", ex);
            throw new JspException("error in ForEachTag: " + ex.getClass() + ": " + ex);
        }
        var += step;
        pageContext.setAttribute(varAttribute, new Integer(var));
        if (var <= end) {
            if (logger.isDebugEnabled()) {
                logger.debug("doAfterBody() - end - return value = " + EVAL_BODY_BUFFERED);
            }
            return EVAL_BODY_BUFFERED;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("doAfterBody() - end - return value = " + SKIP_BODY);
            }
            return SKIP_BODY;
        }
    }

    /**
     * <copyDoc>Refer to {@link Tag#doStartTag}.</copyDoc>
     * @return <copyDoc>Refer to {@link Tag#doStartTag}.</copyDoc>
     * @throws JspException <copyDoc>Refer to {@link Tag#doStartTag}.</copyDoc>
     */
    public int doStartTag() throws JspException {
        if (logger.isDebugEnabled()) {
            logger.debug("doStartTag() - start");
        }
        pageContext.setAttribute(varAttribute, new Integer(var));
        if (var < begin) {
            var = begin;
        }
        if (var <= end) {
            if (logger.isDebugEnabled()) {
                logger.debug("doStartTag() - end - return value = " + EVAL_BODY_BUFFERED);
            }
            return EVAL_BODY_BUFFERED;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("doStartTag() - end - return value = " + SKIP_BODY);
            }
            return SKIP_BODY;
        }
    }

    /**
     * Get the value of the counter.
     *
     * @return current counter value.
     */
    public final String getVar() {
        if (logger.isDebugEnabled()) {
            logger.debug("getVar() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getVar() - end - return value = " + varAttribute);
        }
        return varAttribute;
    }

    /**
     * Sets the first value of the loop counter.
     *
     * @param value the value the counter should start at.
     */
    public final void setBegin(final int value) {
        if (logger.isDebugEnabled()) {
            logger.debug("setBegin(int value = " + value + ") - start");
        }
        begin = value;
        if (logger.isDebugEnabled()) {
            logger.debug("setBegin(int) - end");
        }
    }

    /**
     * Sets the final value of the loop counter, inclusive.
     *
     * @param value final value of the loop counter, inclusive.
     */
    public final void setEnd(final int value) {
        if (logger.isDebugEnabled()) {
            logger.debug("setEnd(int value = " + value + ") - start");
        }
        end = value;
        if (logger.isDebugEnabled()) {
            logger.debug("setEnd(int) - end");
        }
    }

    /**
     * Sets the increment by which the counter is increased after each loop.
     *
     * @param value increment by which the counter is increased after each loop.
     */
    public final void setStep(final int value) {
        if (logger.isDebugEnabled()) {
            logger.debug("setStep(int value = " + value + ") - start");
        }
        step = value;
        if (logger.isDebugEnabled()) {
            logger.debug("setStep(int) - end");
        }
    }

    /**
     * Set the current value of the counter.
     *
     * @param value new value of counter.
     */
    public final void setVar(final String value) {
        if (logger.isDebugEnabled()) {
            logger.debug("setVar(String value = " + value + ") - start");
        }
        varAttribute = value;
        if (logger.isDebugEnabled()) {
            logger.debug("setVar(String) - end");
        }
    }
}
