package net.sf.springlayout.web.layout.taglib;

import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import net.sf.springlayout.web.util.WebConstants;
import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;

/**
 * Iterates over messages exposing each as a variable
 * @author Rob Monie
 *
 */
public class ForEachMessageTag extends HtmlEscapingAwareTag {

    private static final long serialVersionUID = 8041505133654275578L;

    private List messages;

    private int index;

    private String var;

    /**
     * Set the name of the variable to be exposed for each iteration of the tag
    * @param var
    */
    public void setVar(String var) {
        this.var = var;
    }

    protected final int doStartTagInternal() throws ServletException, JspException {
        this.messages = (List) pageContext.findAttribute(WebConstants.MESSAGES);
        if (messages != null && index < messages.size()) {
            pageContext.setAttribute(var, messages.get(index));
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            return TagSupport.SKIP_BODY;
        }
    }

    public int doEndTag() {
        this.pageContext.removeAttribute(var);
        return EVAL_PAGE;
    }

    public int doAfterBody() throws JspException {
        this.index++;
        if (this.index < messages.size()) {
            pageContext.setAttribute(var, messages.get(index));
            return TagSupport.EVAL_BODY_AGAIN;
        } else {
            return TagSupport.SKIP_BODY;
        }
    }

    public void doFinally() {
        this.messages = null;
        this.index = 0;
    }
}
