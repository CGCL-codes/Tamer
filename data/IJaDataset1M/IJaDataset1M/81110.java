package cn.vlabs.umtssotag;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * When user login Application Server, this class object
 * will send login action to UMT and send the sessionid of the user's browser
 * to UMT
 * @author yhw
 *Jul 11, 2009
 */
public class UmtUrlTag extends BodyTagSupport {

    public int doAfterBody() throws JspTagException {
        BodyContent body = getBodyContent();
        String baseURL = body.getString();
        body.clearBody();
        try {
            HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            String encodedURL = response.encodeRedirectURL(baseURL);
            response.sendRedirect(encodedURL + "&jsessionid=" + pageContext.getSession().getId());
        } catch (IOException e) {
            throw new JspTagException("I/O exception" + e.getMessage());
        }
        return SKIP_BODY;
    }
}
