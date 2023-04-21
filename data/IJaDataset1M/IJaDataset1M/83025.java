package org.plazmaforge.bsolution.personality.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.plazmaforge.framework.client.PWT;
import org.plazmaforge.framework.client.swt.controls.XComboEdit;
import org.plazmaforge.framework.client.swt.controls.XDateCombo;
import org.plazmaforge.framework.client.swt.controls.XTextField;

/** 
 * @author Oleh Hapon
 * $Id: PersonEducationEditForm.java,v 1.2 2010/04/28 06:31:02 ohapon Exp $
 */
public class PersonEducationEditForm extends AbstractPersonableEdit {

    private Label educationTypeLabel;

    private Label educationFormLabel;

    private Label educationInstituteLabel;

    private Label endDateLabel;

    private Label facultyLabel;

    private Label academicDegreeLabel;

    private XComboEdit educationTypeField;

    private XComboEdit educationFormField;

    private XComboEdit educationInstituteField;

    private XDateCombo endDateField;

    private XTextField facultyField;

    private XComboEdit academicDegreeField;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public PersonEducationEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        setTitle(Messages.getString("PersonEducationEditForm.title"));
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginBottom = 10;
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        educationTypeLabel = new Label(this, SWT.NONE);
        educationTypeLabel.setText(Messages.getString("PersonEducationEditForm.educationTypeLabel.text"));
        educationTypeField = new XComboEdit(this, SWT.BORDER, PWT.VIEW_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        educationTypeField.setLayoutData(gridData);
        educationFormLabel = new Label(this, SWT.NONE);
        educationFormLabel.setText(Messages.getString("PersonEducationEditForm.educationFormLabel.text"));
        educationFormField = new XComboEdit(this, SWT.BORDER, PWT.VIEW_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        educationFormField.setLayoutData(gridData);
        educationInstituteLabel = new Label(this, SWT.NONE);
        educationInstituteLabel.setText(Messages.getString("PersonEducationEditForm.educationInstituteLabel.text"));
        educationInstituteField = new XComboEdit(this, SWT.BORDER, PWT.VIEW_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        educationInstituteField.setLayoutData(gridData);
        endDateLabel = new Label(this, SWT.NONE);
        endDateLabel.setText(Messages.getString("PersonEducationEditForm.endDateLabel.text"));
        endDateField = new XDateCombo(this, SWT.BORDER);
        gridData = new GridData(100, SWT.DEFAULT);
        endDateField.setLayoutData(gridData);
        facultyLabel = new Label(this, SWT.NONE);
        facultyLabel.setText(Messages.getString("PersonEducationEditForm.facultyLabel.text"));
        facultyField = new XTextField(this, SWT.BORDER);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        facultyField.setLayoutData(gridData);
        facultyField.setTextLimit(30);
        academicDegreeLabel = new Label(this, SWT.NONE);
        academicDegreeLabel.setText(Messages.getString("PersonEducationEditForm.academicDegreeLabel.text"));
        academicDegreeField = new XComboEdit(this, SWT.BORDER, PWT.VIEW_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gridData.widthHint = 350;
        academicDegreeField.setLayoutData(gridData);
        this.setSize(new Point(470, 108));
    }

    protected void bindControls() {
        bindControl(educationTypeField, "educationType", educationTypeLabel, REQUIRED);
        bindControl(educationFormField, "educationForm", educationFormLabel, REQUIRED);
        bindControl(educationInstituteField, "educationInstitute", educationInstituteLabel, REQUIRED);
        bindControl(endDateField, "endDate");
        bindControl(facultyField, "faculty");
        bindControl(academicDegreeField, "academicDegree");
    }
}
