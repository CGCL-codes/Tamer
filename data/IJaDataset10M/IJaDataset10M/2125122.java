package org.jxpfw.jsp.tag.format;

import java.io.IOException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.josef.web.html.HtmlUtil;

/**
 * Custom JSP tag that copies the body to the output stream as is, provided
 * the body contains either xml or html.
 * <br>Use this tag to display html and xml without processing the html or xml
 * code. Unfortunately this tag can't be used to show jsp expressions within
 * tags since these tags are evaluated before the body is made available.<br>
 * Usage: &lt;jxpfw:verbatim&gt;html or xml content&lt;/jxpfw:verbatim&gt;
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.9 $
 */
public class VerbatimTag extends BodyTagSupport {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 7048370196375935557L;

    /**
     * Takes the body of the tag and changes the body to quoted html/xml before
     * writing the new body.
     * @return SKIP_BODY.
     * @throws JspTagException When an error occurs during writing of the quoted
     *  body text to the page.
     */
    @Override
    public int doAfterBody() throws JspTagException {
        final BodyContent actualBodyContent = getBodyContent();
        final String bodyText = actualBodyContent.getString();
        actualBodyContent.clearBody();
        try {
            final JspWriter out = actualBodyContent.getEnclosingWriter();
            out.print(HtmlUtil.quoteHtml(bodyText));
        } catch (final IOException exception) {
            throw new JspTagException(exception.getMessage(), exception);
        }
        return SKIP_BODY;
    }
}
