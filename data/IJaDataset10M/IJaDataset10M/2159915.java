package com.sourceforge.oraclewicket.markup.html.form.panel;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class CheckboxPanel extends Panel {

    public static final long serialVersionUID = 1L;

    public CheckboxPanel(final String pId, final IModel<Boolean> pInputModel) {
        super(pId);
        add(new CheckBox("checkboxInput", pInputModel));
    }
}
