package org.mayo.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.ServletRequest;
import org.mayo.screen.Screen;

/**
 * Displays the <title> tag.
 * @author Chris Corbyn <chris@w3style.co.uk>
 */
public class TitleTag extends TagSupport {

    /**
   * Process the closing tag.
   * @throws JspException
   */
    public int doEndTag() throws JspException {
        try {
            ServletRequest request = pageContext.getRequest();
            Screen screen = (Screen) request.getAttribute("screen");
            String title = screen.getTitle();
            String markup = "<title>" + title + "</title>";
            pageContext.getOut().print(markup);
        } catch (Exception e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }
}
