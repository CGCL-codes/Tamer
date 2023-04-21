package edu.gsbme.wasabi.UI.Dialog.Metadata;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Element;
import edu.gsbme.wasabi.UI.UItype;
import edu.gsbme.wasabi.UI.Dialog.MFormDialog;
import edu.gsbme.wasabi.UI.Forms.Metadata.ModificationForm;

public class ModificationDialog extends MFormDialog {

    public ModificationDialog(Shell shell, Element element, UItype type) {
        super(shell, element, type);
        x = 500;
        y = 350;
    }

    protected void createFormContent(IManagedForm mform) {
        FormToolkit toolkit = mform.getToolkit();
        ScrolledForm scForm = mform.getForm();
        skeleton = new ModificationForm();
        skeleton.construct_layout(toolkit, scForm);
    }

    @Override
    public boolean commit_change() {
        return false;
    }

    @Override
    public void initial_load_UI() {
    }

    @Override
    public void refresh_UI() {
    }
}
