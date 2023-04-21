package edu.gsbme.wasabi.UI.Forms.FML.BREP;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;
import edu.gsbme.wasabi.UI.Forms.Generics.GenericTableAndButtons;

public class AdjacencyOverviewForm extends FormTemplate {

    public GenericTableAndButtons overview;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Adjacency Overview");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        layout.numColumns = 2;
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 1;
        overview = new GenericTableAndButtons();
        overview.createLayout(toolkit, form, gd, "Adjacency Overview", "");
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
