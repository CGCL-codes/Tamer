package org.mikha.utils.web.jsp;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Implementation of &lt;iferror&gt; tag.<br>
 * Executes body only if there is a error in a form parameter with specified
 * name. Error text is (optionally) available in 'page' scope variable.
 * @author Dmitry Mikhaylov
 */
public class IferrorTag extends SimpleTagSupport {

    private String name;

    private String var;

    public void doTag() throws JspException, IOException {
        if (!JspSupport.hasError((PageContext) getJspContext(), name)) {
            return;
        }
        JspFragment f = getJspBody();
        if (f != null) {
            if (var != null) {
                String err = JspSupport.getError((PageContext) getJspContext(), name);
                if (err != null) {
                    getJspContext().setAttribute(var, err);
                }
            }
            f.invoke(null);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
