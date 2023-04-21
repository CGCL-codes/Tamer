package tags.logic;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class StringsEqualTag extends TagSupport {

    private String compare, to;

    public void setCompare(String compare) {
        this.compare = compare;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int doStartTag() throws JspException {
        return compare.equals(to) ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}
