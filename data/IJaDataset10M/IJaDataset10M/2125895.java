package de.jformular.tag;

import javax.servlet.jsp.JspException;

/**
 * Class declaration
 * @author Frank Dolibois, fdolibois@itzone.de, http://www.itzone.de
 * @version $Id: FormularTextTag.java,v 1.8 2002/10/14 14:02:09 fdolibois Exp $
 */
public class FormularTextTag extends FormularTagBase {

    private boolean textonly = false;

    private boolean disabled = false;

    /**
     * Constructor declaration
     */
    public FormularTextTag() {
        super();
    }

    /**
     */
    public void setTextonly(boolean b) {
        textonly = b;
    }

    /**
     */
    public boolean isTextonly() {
        return textonly;
    }

    /**
        
    */
    public int doEndTag() throws JspException {
        StringBuffer sb = new StringBuffer();
        if (isDisabled()) {
            if (hasError()) {
                sb.append(getErrorStartTag());
            }
            sb.append("<input type=\"text\" name=\"");
            sb.append(getProperty() + "_disabled");
            sb.append("\"");
            sb.append(" value=\"");
            sb.append(getValue());
            sb.append("\" disabled");
            if (getSize() > 0) {
                sb.append(" size=\"" + getSize() + "\"");
            }
            if (hasError()) {
                sb.append(" " + getErrorClassTag() + " ");
            }
            sb.append(">");
            if (hasError()) {
                sb.append(getErrorEndTag());
            }
            sb.append("<input type=\"hidden\" name=\"");
            sb.append(getProperty());
            sb.append("\"");
            sb.append(" value=\"");
            sb.append(getValue());
            sb.append("\"");
            sb.append(">");
        } else if (isTextonly()) {
            sb.append(getValue());
            sb.append("<input type=\"hidden\" name=\"");
            sb.append(getProperty());
            sb.append("\"");
            sb.append(" value=\"");
            sb.append(getValue());
            sb.append("\"");
            sb.append(">");
        } else {
            if (hasError()) {
                sb.append(getErrorStartTag());
            }
            sb.append("<input type=\"text\" name=\"");
            sb.append(getProperty());
            sb.append("\"");
            sb.append(" value=\"");
            sb.append(getValue());
            sb.append("\"");
            if (getMaxlength() > 0) {
                sb.append(" maxlength=\"" + getMaxlength() + "\"");
            }
            if (getSize() > 0) {
                sb.append(" size=\"" + getSize() + "\"");
            }
            if (getCommand() != null) {
                sb.append(" " + getCommand() + " ");
            }
            if (hasError()) {
                sb.append(" " + getErrorClassTag() + " ");
            }
            sb.append(">");
            if (hasError()) {
                sb.append(getErrorEndTag());
            }
        }
        try {
            if (!isInvisible()) {
                pageContext.getOut().print(sb.toString());
            }
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
