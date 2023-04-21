package uk.ac.bris.portlet.bookmarks.portlet.el;

import javax.servlet.jsp.JspException;
import uk.ac.bris.portlet.bookmarks.portlet.ParamSupport;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;

/**
 * <p>A handler for &lt;param&gt; that accepts attributes as Strings
 * and evaluates them as expressions at runtime.</p>
 *
 * @author Shawn Bayern
 */
public class ParamTag extends ParamSupport {

    private String name_;

    private String value_;

    public ParamTag() {
        super();
        init();
    }

    public int doStartTag() throws JspException {
        evaluateExpressions();
        return super.doStartTag();
    }

    public void release() {
        super.release();
        init();
    }

    public void setName(String name_) {
        this.name_ = name_;
    }

    public void setValue(String value_) {
        this.value_ = value_;
    }

    private void init() {
        name_ = value_ = null;
    }

    private void evaluateExpressions() throws JspException {
        name = (String) ExpressionUtil.evalNotNull("param", "name", name_, String.class, this, pageContext);
        value = ExpressionUtil.evalNotNull("param", "value", value_, Object.class, this, pageContext);
    }
}
