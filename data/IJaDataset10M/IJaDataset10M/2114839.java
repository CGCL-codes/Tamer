package com.liferay.taglib.ui;

import com.liferay.taglib.util.IncludeTag;
import javax.servlet.ServletRequest;

/**
 * <a href="TagsSummaryTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TagsSummaryTag extends IncludeTag {

    public int doStartTag() {
        ServletRequest req = pageContext.getRequest();
        req.setAttribute("liferay-ui:tags_summary:className", _className);
        req.setAttribute("liferay-ui:tags_summary:classPK", String.valueOf(_classPK));
        req.setAttribute("liferay-ui:tags_summary:message", _message);
        return EVAL_BODY_BUFFERED;
    }

    public void setClassName(String className) {
        _className = className;
    }

    public void setClassPK(long classPK) {
        _classPK = classPK;
    }

    public void setMessage(String message) {
        _message = message;
    }

    protected String getDefaultPage() {
        return _PAGE;
    }

    private static final String _PAGE = "/html/taglib/ui/tags_summary/page.jsp";

    private String _className;

    private long _classPK;

    private String _message;
}
