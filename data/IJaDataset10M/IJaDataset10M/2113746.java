package edu.gsbme.wasabi.UI.Forms.Declaration;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericSingleTextField;

public class EquationRefForm extends FormTemplate {

    public GenericSingleTextField text;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Equation");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        text = new GenericSingleTextField();
        text.createLayout(toolkit, form, gd, "Equation Detail", "", "Equation Ref");
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
