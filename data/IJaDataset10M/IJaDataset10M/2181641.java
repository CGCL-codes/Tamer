package org.plazmaforge.bsolution.employee.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.framework.client.swt.controls.XTextField;
import org.plazmaforge.framework.client.swt.forms.AbstractEditForm;

/** 
 * @author Oleh Hapon
 * $Id: PositionEditForm.java,v 1.2 2010/04/28 06:31:04 ohapon Exp $
 */
public class JobPositionEditForm extends AbstractEditForm {

    private XTextField nameField;

    private Label nameLabel;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public JobPositionEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("JobPositionEditForm.title"));
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginBottom = 10;
        gridLayout.marginTop = 10;
        gridLayout.horizontalSpacing = 10;
        gridLayout.verticalSpacing = 10;
        gridLayout.marginHeight = 10;
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        nameLabel = new Label(this, SWT.NONE);
        nameLabel.setText(Messages.getString("JobPositionEditForm.nameLabel.text"));
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
