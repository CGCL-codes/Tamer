package org.hibnet.lune.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.hibnet.lune.ui.IndexInput;
import org.hibnet.lune.ui.LuneUIPlugin;
import org.hibnet.lune.ui.composite.validation.ValidatedFormListener;
import org.hibnet.lune.ui.plugin.ConfigurableBeanFactoryInlineComposite;

public class NewIndexDialog extends Dialog {

    private ConfigurableBeanFactoryInlineComposite<IndexInput> factoryComposite;

    private IndexInput index;

    String error;

    public NewIndexDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createContents(Composite parent) {
        Control content = super.createContents(parent);
        factoryComposite.setList(LuneUIPlugin.getIndexInputFactories());
        factoryComposite.setBean(null);
        factoryComposite.setFocus();
        return content;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        factoryComposite = new ConfigurableBeanFactoryInlineComposite<IndexInput>(composite, SWT.NONE, "index");
        factoryComposite.addValidatedFormListener(new ValidatedFormListener() {

            public void validated(String err) {
                error = err;
                getButton(IDialogConstants.OK_ID).setEnabled(error == null);
            }
        });
        return composite;
    }

    @Override
    protected void okPressed() {
        index = factoryComposite.getBean();
        super.okPressed();
    }

    public IndexInput getIndex() {
        return index;
    }
}
