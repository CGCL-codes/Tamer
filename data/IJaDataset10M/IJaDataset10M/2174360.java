package nuts.ext.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.ext.struts2.components.ViewField;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractListTag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see ViewField
 */
public class ViewFieldTag extends AbstractListTag {

    private static final long serialVersionUID = 5811285953670562288L;

    protected String expression;

    protected String format;

    protected String iconClass;

    /**
     * @see org.apache.struts2.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new ViewField(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        ViewField viewField = ((ViewField) component);
        viewField.setExpression(expression);
        viewField.setFormat(format);
        viewField.setIconClass(iconClass);
    }

    /**
	 * @param expression the expression to set
	 */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
	 * @param format the format to set
	 */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
	 * @param iconClass iconClass icon to set
	 */
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
}
