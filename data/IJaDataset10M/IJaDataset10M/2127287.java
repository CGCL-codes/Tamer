package org.gbif.portal.web.tag;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FormatDropDownListTag extends TagSupport {

    private static final long serialVersionUID = -8115727364284355169L;

    /** Logger */
    private static Log logger = LogFactory.getLog(FormatTextTag.class);

    /** The scientific name from the record **/
    protected String content;

    /** Max length of string value **/
    protected int maxLength;

    /** End string for long string values **/
    protected String end;

    /**
	 * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
	 */
    public int doStartTag() throws JspException {
        String output = "";
        output = cutLongValues();
        try {
            pageContext.getOut().print(output.toString());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    public String cutLongValues() {
        if (content.length() > maxLength && maxLength - end.length() > -1) {
            content = content.substring(0, maxLength - end.length()) + end;
        }
        return content;
    }

    /**
	 * @return the content
	 */
    public String getContent() {
        return content;
    }

    /**
	 * @param content
	 *            the content to set
	 */
    public void setContent(String content) {
        this.content = content;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
