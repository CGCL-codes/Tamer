package org.jamwiki.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.log4j.Logger;
import org.jamwiki.WikiBase;
import org.jamwiki.WikiMembers;
import org.jamwiki.utils.Utilities;

/**
 *
 */
public class MemberTag extends TagSupport {

    private static final Logger logger = Logger.getLogger(MemberTag.class);

    private String var;

    /**
	 *
	 */
    public int doEndTag() throws JspException {
        try {
            String user = Utilities.getUserFromRequest((HttpServletRequest) this.pageContext.getRequest());
            String virtualWiki = (String) pageContext.findAttribute("virtualWiki");
            WikiMembers members = WikiBase.getInstance().getWikiMembersInstance(virtualWiki);
            pageContext.setAttribute(var, members.findMemberByName(user));
        } catch (Exception e) {
            logger.warn(e);
        }
        return SKIP_BODY;
    }

    /**
	 *
	 */
    public String getVar() {
        return var;
    }

    /**
	 *
	 */
    public void setVar(String var) {
        this.var = var;
    }
}
