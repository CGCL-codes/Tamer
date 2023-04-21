package org.plazmaforge.bsolution.project.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.framework.client.swt.controls.XCheckBox;
import org.plazmaforge.framework.client.swt.controls.XTextArea;
import org.plazmaforge.framework.client.swt.controls.XTextField;
import org.plazmaforge.framework.client.swt.forms.AbstractEditForm;

/** 
 * @author Oleh Hapon
 * $Id: ProjectStatusEditForm.java,v 1.1 2008/12/01 12:27:17 ohapon Exp $
 */
public class ProjectStatusEditForm extends AbstractEditForm {

    private Label codeLabel;

    private Label nameLabel;

    private Label descriptionLabel;

    private Label startStatusLabel;

    private Label finishStatusLabel;

    private XTextField codeField;

    private XTextField nameField;

    private XTextArea descriptionField;

    private XCheckBox startStatusField;

    private XCheckBox finishStatusField;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public ProjectStatusEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("ProjectStatusEditForm.title"));
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        codeLabel = new Label(this, SWT.NONE);
        codeLabel.setText(Messages.getString("ProjectStatusEditForm.codeLabel.text"));
        codeField = new XTextField(this, SWT.BORDER);
        codeField.setLayoutData(new GridData(100, SWT.DEFAULT));
        codeField.setTextLimit(20);
        nameLabel = new Label(this, SWT.NONE);
        nameLabel.setText(Messages.getString("ProjectStatusEditForm.nameLabel.text"));
        nameField = new XTextField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 250;
        nameField.setLayoutData(gridData);
        nameField.setTextLimit(50);
        descriptionLabel = new Label(this, SWT.NONE);
        descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
        descriptionLabel.setText(Messages.getString("ProjectStatusEditForm.descriptionLabel.text"));
        descriptionField = new XTextArea(this, SWT.BORDER | SWT.MULTI);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, true, false);
        gridData.heightHint = 100;
        gridData.widthHint = 250;
        descriptionField.setLayoutData(gridData);
        descriptionField.setTextLimit(100);
        startStatusLabel = new Label(this, SWT.NONE);
        startStatusLabel.setText(Messages.getString("ProjectStatusEditForm.startStatusLabel.text"));
        startStatusField = new XCheckBox(this, SWT.NONE);
        finishStatusLabel = new Label(this, SWT.NONE);
        finishStatusLabel.setText(Messages.getString("ProjectStatusEditForm.finishStatusLabel.text"));
        finishStatusField = new XCheckBox(this, SWT.NONE);
        this.setSize(new Point(470, 108));
    }

    protected void bindControls() {
        bindControl(codeField, "code");
        bindControl(nameField, "name", nameLabel, REQUIRED);
        bindControl(descriptionField, "description");
        bindControl(startStatusField, "start", Boolean.TYPE);
        bindControl(finishStatusField, "finish", Boolean.TYPE);
    }
}
