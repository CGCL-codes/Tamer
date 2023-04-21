package de.uni_leipzig.lots.jsp.tag.form;

import de.uni_leipzig.lots.webfrontend.formbeans.AutoValidateForm;
import de.uni_leipzig.lots.webfrontend.formbeans.DeleteForm;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import java.io.IOException;

/**
 * @author Alexander Kiel
 * @version $Id: SubmitLineTag.java,v 1.8 2007/10/23 06:30:21 mai99bxd Exp $
 */
public class SubmitLineTag extends AbstractFormTag {

    @Override
    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            out.write("<div class=\"submit\">");
            if (printRequiredMessage()) {
                out.write("<p class=\"required-message\">");
                out.write(LocaleSupport.getLocalizedMessage(pageContext, "requiredmessage", BUNDLE_NAME));
                out.write("</p>");
            }
        } catch (IOException e) {
            throw new JspException("Error: IOException while writing to client" + e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            out.write("</div>\n");
        } catch (IOException e) {
            throw new JspException("Error: IOException while writing to client" + e.getMessage());
        }
        return EVAL_PAGE;
    }

    private boolean printRequiredMessage() throws JspException {
        AutoValidateForm form = TagSupport.getAutoValidateForm(pageContext);
        return form.isRequired() && !(form instanceof DeleteForm);
    }
}
