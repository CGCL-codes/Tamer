package vqwiki.tags;

import vqwiki.WikiBase;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author garethc
 * Date: Jan 7, 2003
 */
public class WikiBaseTag extends TagSupport {

    private String var;

    /**
     *
     */
    public int doEndTag() throws JspException {
        try {
            this.pageContext.setAttribute(var, WikiBase.getInstance());
        } catch (Exception e) {
            throw new JspException("Getting WikiBase", e);
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
