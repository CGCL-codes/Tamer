package tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class CheckTokensTag extends TagSupport {

    private String property;

    public int doEndTag() throws JspException {
        ServletRequest req = pageContext.getRequest();
        String sessionToken = (String) req.getParameter("token");
        String requestToken = (String) pageContext.getSession().getAttribute("token");
        if (requestToken == null || requestToken == null || !sessionToken.equals(requestToken)) throw new JspException("Sorry, but this sensitive page" + " can't be resubmitted.");
        return EVAL_PAGE;
    }
}
