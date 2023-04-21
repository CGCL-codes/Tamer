package org.plazmaforge.bsolution.organization.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.framework.client.swt.controls.XTextField;

/** 
 * @author Oleh Hapon
 * $Id: BranchEditForm.java,v 1.2 2010/04/28 06:31:06 ohapon Exp $
 */
public class BranchEditForm extends AbstractOrganizableEditForm {

    private XTextField nameField;

    private Label nameLabel;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public BranchEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("BranchEditForm.title"));
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginBottom = 10;
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        nameLabel = new Label(this, SWT.NONE);
        nameLabel.setText(Messages.getString("BranchEditForm.nameLabel.text"));
        nameField = new XTextField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        nameField.setLayoutData(gridData);
        nameField.setTextLimit(50);
        this.setSize(new Point(470, 108));
    }

    protected void bindControls() {
        bindControl(nameField, "name", nameLabel, REQUIRED);
    }
}
