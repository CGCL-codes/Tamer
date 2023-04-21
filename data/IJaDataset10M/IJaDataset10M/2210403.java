package jdbframework.tags;

import jdbframework.tags.property.TagQueryProperty;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class QueryTag extends BodyTagSupport {

    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws JspException {
        String sql = getBodyContent().getString().trim();
        TagQueryProperty query = new TagQueryProperty(sql);
        StatementTag parent = (StatementTag) getParent();
        parent.addQuery(query);
        return EVAL_PAGE;
    }
}
