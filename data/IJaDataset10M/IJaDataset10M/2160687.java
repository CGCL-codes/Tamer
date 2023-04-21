package nuts.ext.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.ext.struts2.components.ActionError;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * ActionError Tag.
 *
 */
public class ActionErrorTag extends AbstractUITag {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -8990569817299754182L;

    /**
	 * @see org.apache.struts2.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new ActionError(stack, req, res);
    }
}
