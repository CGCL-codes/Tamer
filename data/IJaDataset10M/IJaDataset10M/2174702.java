package jdbframework.tags;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import jdbframework.action.ParametersManagement;
import jdbframework.common.GeneralSettings;

public class HiddenTag extends BodyTagSupport {

    private String property = null;

    private String value = null;

    public void setProperty(String property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String getValue(HttpServletRequest request) {
        String Value = new ParametersManagement(request).getParameter(this.property);
        if (Value == null) {
            Value = (this.value == null ? "" : this.value);
        }
        request.setAttribute(this.property, Value);
        return Value;
    }

    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        try {
            out.println("<input type=\"hidden\" id=\"" + this.property + "\" name=\"" + this.property + "\" value=\"" + GeneralSettings.replaceHtmlSpecialChars(getValue(request)) + "\">");
        } catch (java.io.IOException io) {
        }
        return EVAL_PAGE;
    }
}
