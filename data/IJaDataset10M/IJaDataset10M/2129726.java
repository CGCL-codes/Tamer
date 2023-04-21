package org.itracker.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.struts.taglib.TagUtils;
import org.itracker.core.resources.ITrackerResources;
import org.itracker.web.util.LoginUtilities;

/**
 * Truncate a string if it's longer than truncateLength
 * 
 * @author ranks@rosa.com
 *
 */
public class FormatDescriptionTag extends BodyTagSupport {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String text = null;

    private String truncateKey = "itracker.web.generic.truncated";

    private String truncateText = null;

    private int truncateLength = 40;

    public String getTruncateKey() {
        return truncateKey;
    }

    public void setTruncateKey(String value) {
        truncateKey = value;
    }

    public int getTruncateLength() {
        return truncateLength;
    }

    public void setTruncateLength(int value) {
        truncateLength = value;
    }

    public int doStartTag() throws JspException {
        text = null;
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0) {
                text = value;
            }
        }
        return SKIP_BODY;
    }

    public String getTruncateText() {
        if (null == truncateText) {
            if (pageContext.getRequest() instanceof HttpServletRequest) {
                truncateText = ITrackerResources.getString(truncateKey, LoginUtilities.getCurrentLocale((HttpServletRequest) this.pageContext.getRequest()));
            } else {
                truncateText = ITrackerResources.getString(truncateKey);
            }
            if (null != truncateText) {
                truncateText = truncateText.trim();
            } else {
                truncateText = "";
            }
        }
        return truncateText;
    }

    public int doEndTag() throws JspException {
        StringBuffer results = new StringBuffer();
        if (text != null && text.trim().length() > truncateLength - getTruncateText().length()) {
            results.append(text.trim().substring(0, truncateLength - getTruncateText().length()).trim());
            results.append(getTruncateText());
        } else if (null == text) {
            results.append("");
        } else {
            results.append(text.trim());
        }
        TagUtils.getInstance().write(pageContext, results.toString());
        clearState();
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
        clearState();
    }

    private void clearState() {
        truncateKey = "itracker.web.generic.truncated";
        truncateLength = 40;
        text = null;
        truncateText = null;
    }
}
