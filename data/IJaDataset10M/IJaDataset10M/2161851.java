package com.byterefinery.rmbench.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.byterefinery.rmbench.model.schema.CheckConstraint;
import com.byterefinery.rmbench.operations.ModifyCheckConstraintOperation;

public class CheckConstraintEditorDialog extends Dialog {

    private CheckConstraint constraint;

    private Text nameText;

    private Text expressionText;

    public CheckConstraintEditorDialog(Shell parentShell, CheckConstraint constraint) {
        super(parentShell);
        this.constraint = constraint;
        parentShell.setText(Messages.CheckConstraintEditorDialog_Title);
    }

    public CheckConstraintEditorDialog(IShellProvider parentShell, CheckConstraint constraint) {
        super(parentShell);
        this.constraint = constraint;
        parentShell.getShell().setText(Messages.CheckConstraintEditorDialog_Title);
    }

    protected Control createDialogArea(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        GridData gridData;
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 2;
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.CheckConstraintEditorDialog_Name + ":");
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        container.setLayoutData(gridData);
        nameText = new Text(container, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        nameText.setLayoutData(gridData);
        nameText.setText(constraint.getName());
        label = new Label(container, SWT.NONE);
        label.setText(Messages.CheckConstraintEditorDialog_Expression + ":");
        expressionText = new Text(container, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.minimumHeight = convertHeightInCharsToPixels(5);
        gridData.minimumWidth = convertWidthInCharsToPixels(30);
        expressionText.setLayoutData(gridData);
        expressionText.setText(constraint.getExpression());
        return container;
    }

    protected void okPressed() {
        if ((constraint.getName().equals(nameText.getText())) && (constraint.getExpression().equals(expressionText.getText()))) {
            super.okPressed();
        }
        ModifyCheckConstraintOperation op = new ModifyCheckConstraintOperation(constraint, nameText.getText(), expressionText.getText());
        op.execute(this);
        super.okPressed();
    }
}
