package edu.gsbme.wasabi.UI.Forms.Math;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericTwoTextField;

public class SimpleEquationAttachForm extends FormTemplate {

    public GenericTwoTextField textfield;

    @Override
    public void construct_layout(FormToolkit toolkit, final ScrolledForm form) {
        form.setText("Equation");
        form.getBody().setLayout(new GridLayout(1, false));
        textfield = new GenericTwoTextField();
        textfield.createLayout(toolkit, form, new GridData(SWT.FILL, SWT.FILL, true, true), "Equations", "", "ID", "EQ");
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
