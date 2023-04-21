package net.sf.opentranquera.xkins.forms;

import javax.servlet.jsp.JspException;

/**
 * Bot�n del Formulario
 *@author Guillermo Meyer
 **/
public class ResetTag extends ButtonTag {

    protected String formAction = null;

    protected String formName = null;

    /**
     * DOCUMENT ME!
     *
     * @throws JspException DOCUMENT ME!
     */
    protected void prepareData() throws JspException {
        super.prepareData();
        formName = this.getFormName();
        formAction = this.getActionFormName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComponentRender() {
        StringBuffer sb = new StringBuffer();
        StringBuffer command = new StringBuffer();
        this.startButton(sb);
        this.onclickScript(command);
        command.append("document.");
        command.append(this.formName);
        command.append(".reset(); ");
        this.endButton(sb, command);
        return sb.toString();
    }

    public void release() {
        super.release();
        formAction = null;
        formName = null;
    }
}
