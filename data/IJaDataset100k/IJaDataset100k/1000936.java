package org.pluginbuilder.ui.editors;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.pluginbuilder.core.releases.NamedConfiguration;

public class VersioningAttributesDialog extends Dialog {

    protected MultiFieldGroup panel;

    private final NamedConfiguration namedConfiguration;

    private final boolean isNew;

    public VersioningAttributesDialog(final Shell parentShell, final NamedConfiguration namedConfiguration, boolean isNew) {
        super(parentShell);
        this.isNew = isNew;
        this.namedConfiguration = namedConfiguration;
        setShellStyle(getShellStyle() | SWT.RESIZE);
        panel = new MultiFieldGroup(namedConfiguration.getProperties());
    }

    private String getTitle() {
        String result;
        if (isNew) {
            result = "New release configuration " + namedConfiguration.getName();
        } else {
            result = "Edit release configuration " + namedConfiguration.getName();
        }
        return result;
    }

    private String getMessage() {
        String result;
        if (isNew) {
            result = "The fields contain default values in angle brackets. Please edit and remove the brackets. The pipe character indicates a choice.";
        } else {
            result = "Edit the properties of the release configuration";
        }
        return result;
    }

    protected void configureShell(final Shell shell) {
        super.configureShell(shell);
        shell.setText(getTitle());
    }

    protected Control createDialogArea(final Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout(2, false);
        container.setLayout(gridLayout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        Label label = new Label(container, SWT.WRAP);
        label.setText(getMessage());
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2);
        gridData.widthHint = 400;
        label.setLayoutData(gridData);
        panel.createContents(container);
        panel.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Dialog.applyDialogFont(container);
        return container;
    }

    protected void okPressed() {
        panel.storeValues();
        namedConfiguration.setDirty(true);
        super.okPressed();
    }

    public int open() {
        applyDialogFont(panel.getControl());
        return super.open();
    }
}
