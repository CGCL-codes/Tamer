package edu.gsbme.wasabi.UI.Forms.FML.FieldAttributes;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;

public class AttributeForm extends FormTemplate {

    public Text data_type;

    public Table attr_value;

    public Button modify;

    private int numberOfDesiredLines = 8;

    @Override
    public void construct_layout(FormToolkit toolkit, ScrolledForm form) {
        form.setText("Attribute");
        GridLayout layout = new GridLayout(4, false);
        form.getBody().setLayout(layout);
        Label label = toolkit.createLabel(form.getBody(), "Type :");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        data_type = toolkit.createText(form.getBody(), "");
        data_type.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 3, 1));
        attr_value = toolkit.createTable(form.getBody(), SWT.NULL);
        GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1);
        gd.heightHint = (numberOfDesiredLines - 1) * attr_value.getItemHeight();
        gd.widthHint = SWT.DEFAULT;
        attr_value.setLayoutData(gd);
        modify = toolkit.createButton(form.getBody(), "Modify", SWT.PUSH);
        modify.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
