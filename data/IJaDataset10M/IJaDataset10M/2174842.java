package de.dgrid.wisent.gridftp.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import de.dgrid.wisent.gridftp.gui.actions.ChangeModeAction;

public class ChangeModeDialog extends Dialog {

    private ChangeModeAction action;

    private String permissions;

    private Button userRead;

    private Button userWrite;

    private Button userExecute;

    private Button groupRead;

    private Button groupWrite;

    private Button groupExecute;

    private Button otherRead;

    private Button otherWrite;

    private Button otherExecute;

    private boolean[] oldPermissions;

    public ChangeModeDialog(Shell parentShell, String permissions) {
        super(parentShell);
        this.permissions = permissions;
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout compositeLayout = new GridLayout();
        compositeLayout.numColumns = 1;
        composite.setLayout(compositeLayout);
        FormToolkit toolkit = new FormToolkit(parent.getDisplay());
        Group userGroup = new Group(composite, SWT.NONE);
        GridData userGroupData = new GridData(GridData.FILL_HORIZONTAL);
        userGroup.setLayoutData(userGroupData);
        userGroup.setText("User");
        GridLayout userGroupLayout = new GridLayout();
        userGroupLayout.numColumns = 3;
        userGroupLayout.makeColumnsEqualWidth = true;
        userGroup.setLayout(userGroupLayout);
        this.userRead = toolkit.createButton(userGroup, "Read", SWT.CHECK);
        this.userRead.setBackground(this.userRead.getParent().getBackground());
        this.userWrite = toolkit.createButton(userGroup, "Write", SWT.CHECK);
        this.userWrite.setBackground(this.userWrite.getParent().getBackground());
        this.userExecute = toolkit.createButton(userGroup, "Execute", SWT.CHECK);
        this.userExecute.setBackground(this.userExecute.getParent().getBackground());
        Group groupGroup = new Group(composite, SWT.NONE);
        GridData groupGroupData = new GridData(GridData.FILL_HORIZONTAL);
        groupGroup.setLayoutData(groupGroupData);
        groupGroup.setText("Group");
        GridLayout groupGroupLayout = new GridLayout();
        groupGroupLayout.numColumns = 3;
        groupGroupLayout.makeColumnsEqualWidth = true;
        groupGroup.setLayout(groupGroupLayout);
        this.groupRead = toolkit.createButton(groupGroup, "Read", SWT.CHECK);
        this.groupRead.setBackground(this.groupRead.getParent().getBackground());
        this.groupWrite = toolkit.createButton(groupGroup, "Write", SWT.CHECK);
        this.groupWrite.setBackground(this.groupWrite.getParent().getBackground());
        this.groupExecute = toolkit.createButton(groupGroup, "Execute", SWT.CHECK);
        this.groupExecute.setBackground(this.groupExecute.getParent().getBackground());
        Group otherGroup = new Group(composite, SWT.NONE);
        GridData otherGroupData = new GridData(GridData.FILL_HORIZONTAL);
        otherGroup.setLayoutData(otherGroupData);
        otherGroup.setText("Other");
        GridLayout otherGroupLayout = new GridLayout();
        otherGroupLayout.numColumns = 3;
        otherGroupLayout.makeColumnsEqualWidth = true;
        otherGroup.setLayout(otherGroupLayout);
        this.otherRead = toolkit.createButton(otherGroup, "Read", SWT.CHECK);
        this.otherRead.setBackground(this.otherRead.getParent().getBackground());
        this.otherWrite = toolkit.createButton(otherGroup, "Write", SWT.CHECK);
        this.otherWrite.setBackground(this.otherWrite.getParent().getBackground());
        this.otherExecute = toolkit.createButton(otherGroup, "Execute", SWT.CHECK);
        this.otherExecute.setBackground(this.otherExecute.getParent().getBackground());
        if (this.permissions != null && this.permissions.length() == 10) {
            if (this.permissions.substring(1, 2).equals("r")) {
                this.userRead.setSelection(true);
            }
            if (this.permissions.subSequence(2, 3).equals("w")) {
                this.userWrite.setSelection(true);
            }
            if (this.permissions.substring(3, 4).equals("x")) {
                this.userExecute.setSelection(true);
            }
            if (this.permissions.substring(4, 5).equals("r")) {
                this.groupRead.setSelection(true);
            }
            if (this.permissions.substring(5, 6).equals("w")) {
                this.groupWrite.setSelection(true);
            }
            if (this.permissions.substring(6, 7).equals("x")) {
                this.groupExecute.setSelection(true);
            }
            if (this.permissions.substring(7, 8).equals("r")) {
                this.otherRead.setSelection(true);
            }
            if (this.permissions.substring(8, 9).equals("w")) {
                this.otherWrite.setSelection(true);
            }
            if (this.permissions.substring(9, 10).equals("x")) {
                this.otherExecute.setSelection(true);
            }
        }
        this.oldPermissions = new boolean[9];
        this.oldPermissions[0] = this.userRead.getSelection();
        this.oldPermissions[1] = this.userWrite.getSelection();
        this.oldPermissions[2] = this.userExecute.getSelection();
        this.oldPermissions[3] = this.groupRead.getSelection();
        this.oldPermissions[4] = this.groupWrite.getSelection();
        this.oldPermissions[5] = this.groupExecute.getSelection();
        this.oldPermissions[6] = this.otherRead.getSelection();
        this.oldPermissions[7] = this.otherWrite.getSelection();
        this.oldPermissions[8] = this.otherExecute.getSelection();
        return composite;
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("GridFTP Client");
    }

    protected void okPressed() {
        boolean[] permissions = new boolean[9];
        if (this.userRead.getSelection() == true) {
            permissions[0] = true;
        } else {
            permissions[0] = false;
        }
        if (this.userWrite.getSelection() == true) {
            permissions[1] = true;
        } else {
            permissions[1] = false;
        }
        if (this.userExecute.getSelection() == true) {
            permissions[2] = true;
        } else {
            permissions[2] = false;
        }
        if (this.groupRead.getSelection() == true) {
            permissions[3] = true;
        } else {
            permissions[3] = false;
        }
        if (this.groupWrite.getSelection() == true) {
            permissions[4] = true;
        } else {
            permissions[4] = false;
        }
        if (this.groupExecute.getSelection() == true) {
            permissions[5] = true;
        } else {
            permissions[5] = false;
        }
        if (this.otherRead.getSelection() == true) {
            permissions[6] = true;
        } else {
            permissions[6] = false;
        }
        if (this.otherWrite.getSelection() == true) {
            permissions[7] = true;
        } else {
            permissions[7] = false;
        }
        if (this.otherExecute.getSelection() == true) {
            permissions[8] = true;
        } else {
            permissions[8] = false;
        }
        boolean modified = false;
        for (int i = 0; i < this.oldPermissions.length; i++) {
            if (this.oldPermissions[i] != permissions[i]) {
                modified = true;
                break;
            }
        }
        if (modified == false) {
            super.cancelPressed();
        } else {
            this.action.setPermissions(permissions);
            super.okPressed();
        }
    }

    /**
	 * Sets the action which controls this dialog
	 * @param action the action
	 */
    public void setAction(ChangeModeAction action) {
        this.action = action;
    }
}
