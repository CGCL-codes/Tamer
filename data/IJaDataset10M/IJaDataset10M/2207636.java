package net.sf.elbe.ui.dialogs;

import net.sf.elbe.core.model.ISearch;
import net.sf.elbe.ui.widgets.BaseWidgetUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class ScopeDialog extends Dialog {

    private String dialogTitle;

    private boolean multi;

    private int scope = -1;

    private Button objectScopeButton;

    private Button onelevelScopeButton;

    private Button subtreeScopeButton;

    public ScopeDialog(Shell parentShell, String dialogTitle, boolean multi) {
        super(parentShell);
        super.setShellStyle(super.getShellStyle() | SWT.RESIZE);
        this.dialogTitle = dialogTitle;
        this.multi = multi;
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(dialogTitle);
    }

    public boolean close() {
        return super.close();
    }

    protected void okPressed() {
        this.scope = this.objectScopeButton.getSelection() ? ISearch.SCOPE_OBJECT : this.onelevelScopeButton.getSelection() ? ISearch.SCOPE_ONELEVEL : ISearch.SCOPE_SUBTREE;
        super.okPressed();
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);
        Group group = BaseWidgetUtils.createGroup(composite, "Please select the copy depth", 1);
        this.objectScopeButton = new Button(group, SWT.RADIO);
        this.objectScopeButton.setSelection(true);
        this.objectScopeButton.setText(this.multi ? "&Object (Only the copied entries)" : "&Object (Only the copied entry)");
        this.onelevelScopeButton = new Button(group, SWT.RADIO);
        this.onelevelScopeButton.setText(this.multi ? "O&ne Level (The copied entries and their direct children)" : "O&ne Level (The copied entry and its direct children)");
        this.subtreeScopeButton = new Button(group, SWT.RADIO);
        this.subtreeScopeButton.setText(this.multi ? "&Subtree (The whole subtrees)" : "&Subtree (The whole subtree)");
        applyDialogFont(composite);
        return composite;
    }

    public int getScope() {
        return this.scope;
    }
}
