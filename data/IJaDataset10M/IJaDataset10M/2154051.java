package edu.gsbme.wasabi.UI.Forms.FML;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericSingleTextField;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericTableAndButtons;

public class FMLOverviewForm extends FormTemplate {

    public GenericSingleTextField idField;

    public GenericTableAndButtons importTable;

    public GenericTableAndButtons frameTable;

    public GenericTableAndButtons decTable;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Model Overview");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        layout.numColumns = 2;
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 1;
        idField = new GenericSingleTextField();
        idField.createLayout(toolkit, form, gd, "Model name", "", "Name");
        importTable = new GenericTableAndButtons();
        importTable.createLayout(toolkit, form, gd, "Imports", "");
        frameTable = new GenericTableAndButtons();
        frameTable.createLayout(toolkit, form, gd, "Frame List", "");
        decTable = new GenericTableAndButtons();
        decTable.createLayout(toolkit, form, gd, "Declarations", "");
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
