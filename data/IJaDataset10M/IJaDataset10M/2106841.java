package org.j2eebuilder.view;

import javax.servlet.jsp.JspException;
import java.beans.Expression;
import org.j2eebuilder.ComponentDefinition;
import org.j2eebuilder.AttributeDefinition;

/**
 * @author Sandeep Dixit
 * @version 1.0 $date 2004/03/12
 * This tag will
 * formatted HTML text.
 */
public class LabelTag extends ComponentAttributeTag {

    public String getHtmlElementID() throws JspException {
        return this.getComponentName() + "_" + this.getAttributeName() + "_Label";
    }

    public StringBuffer toHtml() throws JspException {
        StringBuffer html = new StringBuffer();
        html.append("<label");
        html.append(" ");
        html.append("id=\"");
        html.append(this.getHtmlElementID());
        html.append("\"");
        html.append(">");
        html.append(this.getAttributeDefinition().getDescription() == null ? this.getAttributeDefinition().getName() : this.getAttributeDefinition().getDescription());
        html.append("</label>");
        return html;
    }
}
