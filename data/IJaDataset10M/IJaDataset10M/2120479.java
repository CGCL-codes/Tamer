package edu.gsbme.wasabi.UI.Forms.Declaration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericSingleTextField;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericTableAndButtons;

public class UnitsForm extends FormTemplate {

    public GenericSingleTextField nameField;

    public GenericTableAndButtons unitsTable;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Units");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        layout.numColumns = 1;
        nameField = new GenericSingleTextField();
        nameField.createLayout(toolkit, form, new GridData(SWT.FILL, SWT.TOP, true, false), "Units Attribute", "", "Name");
        unitsTable = new GenericTableAndButtons();
        unitsTable.createLayout(toolkit, form, new GridData(SWT.FILL, SWT.FILL, true, true), "Units Element", "");
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
