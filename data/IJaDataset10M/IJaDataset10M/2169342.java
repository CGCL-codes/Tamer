package de.jformular.tag;

import de.jformular.event.FormularEvent;
import javax.servlet.jsp.JspException;

/**
 * Class declaration
 * @author Frank Dolibois, fdolibois@itzone.de, http://www.itzone.de
 * @version $Id: FormularSendDataButtonTag.java,v 1.5 2002/10/14 14:01:58 fdolibois Exp $
 */
public class FormularSendDataButtonTag extends FormularButtonTagBase {

    /**
     */
    public int doEndTag() throws JspException {
        try {
            if ((getValue() == null) || getValue().equals("")) {
                setValue("Send");
            }
            this.setProperty(FormularEvent.COMMAND_SENDDATA);
            pageContext.getOut().print(createButton());
        } catch (Exception e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }
}
