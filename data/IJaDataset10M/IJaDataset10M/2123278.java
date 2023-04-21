package edu.gsbme.wasabi.UI.Forms.Declaration;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericTableAndButtons;

public class EquationListForm extends FormTemplate {

    public GenericTableAndButtons overviewTableObj;

    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Equation List");
        GridLayout layout = new GridLayout();
        layout.numColumns = 5;
        form.getBody().setLayout(layout);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 5;
        overviewTableObj = new GenericTableAndButtons();
        overviewTableObj.createLayout(toolkit, form, gd, "Equation List", "");
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
