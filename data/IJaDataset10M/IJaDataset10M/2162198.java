package org.starobjects.wicket.viewer.common.scalars.jdkdates;

import org.apache.wicket.markup.html.form.TextField;
import org.starobjects.wicket.viewer.common.scalars.ScalarModel;
import org.starobjects.wicket.viewer.common.scalars.ScalarPanelTextFieldAbstract;

public class JavaSqlTimePanel extends ScalarPanelTextFieldAbstract {

    private static final long serialVersionUID = 1L;

    public JavaSqlTimePanel(String id, final ScalarModel scalarModel) {
        super(id, scalarModel);
        TextField<String> textField = addTextField();
        addSemantics(textField);
    }

    private void addSemantics(TextField<String> textField) {
    }
}
