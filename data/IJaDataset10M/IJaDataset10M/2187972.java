package edu.gsbme.wasabi.UI.Forms.FML.BREP;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;

public class FaceAdjacencyForm extends FormTemplate {

    public Text name;

    public Text up;

    public Text down;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Face Adjacency");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        layout.numColumns = 2;
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 1;
        Label label = toolkit.createLabel(form.getBody(), "Ref :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        name = toolkit.createText(form.getBody(), "");
        name.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        label = toolkit.createLabel(form.getBody(), "Up :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        up = toolkit.createText(form.getBody(), "");
        up.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        label = toolkit.createLabel(form.getBody(), "Down :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        down = toolkit.createText(form.getBody(), "");
        down.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
