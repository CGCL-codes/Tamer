package com.objectcode.time4u.server.web.jsf.renderkit;

import javax.faces.component.UIComponent;

public class FormInfo {

    private final UIComponent form;

    private final String formName;

    /**
   * 
   * @param form <code>UIComponent</code> that represents the used form
   * @param formName the name of the form.
	 */
    public FormInfo(final UIComponent form, final String formName) {
        this.form = form;
        this.formName = formName;
    }

    public UIComponent getForm() {
        return form;
    }

    public String getFormName() {
        return formName;
    }
}
