package edu.gsbme.wasabi.UI.Forms.FML.Cells;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;

public class ControlPointForm extends FormTemplate {

    public Text x;

    public Text y;

    public Text z;

    public Text weight;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Control Point");
        GridLayout layout = new GridLayout(2, false);
        form.getBody().setLayout(layout);
        Label label = toolkit.createLabel(form.getBody(), "x :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        x = toolkit.createText(form.getBody(), "");
        x.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        label = toolkit.createLabel(form.getBody(), "y :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        y = toolkit.createText(form.getBody(), "");
        y.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        label = toolkit.createLabel(form.getBody(), "z :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        z = toolkit.createText(form.getBody(), "");
        z.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        label = toolkit.createLabel(form.getBody(), "weight :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        weight = toolkit.createText(form.getBody(), "");
        weight.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
