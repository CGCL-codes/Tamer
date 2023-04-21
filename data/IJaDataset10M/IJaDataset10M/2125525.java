package org.exteca.web.search.taglib;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;
import org.exteca.web.search.model.Constants;
import org.exteca.web.search.model.HelpDocument;

/** JSP tag which gets the body of the help document.
 *  A scripting variable "helpBody" is also set up which can
 *  be used by the JSP page.
 *
 */
public class HelpBodyTag extends TagSupport {

    public HelpBodyTag() {
        super();
    }

    public int doStartTag() {
        try {
            HttpSession session = pageContext.getSession();
            if (session != null) {
                HelpDocument docSession = (HelpDocument) session.getAttribute(Constants.SESSION_ATTRIBUTE_HELPDOCUMENT);
                if (docSession != null) {
                    String helpBody = docSession.getBody();
                    pageContext.setAttribute(Constants.PAGE_ATTRIBUTE_HELPBODY, helpBody);
                }
            }
            return EVAL_BODY_INCLUDE;
        } catch (Exception e) {
            System.err.println("TagLib HighlightDocumentTag: " + e.toString());
            e.printStackTrace();
            return SKIP_PAGE;
        }
    }
}
