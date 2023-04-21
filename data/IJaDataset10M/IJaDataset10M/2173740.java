package org.plazmaforge.bsolution.employee.client.swt.forms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.plazmaforge.bsolution.document.client.swt.forms.AbstractDocumentEditForm;
import org.plazmaforge.bsolution.employee.common.beans.Employee;
import org.plazmaforge.framework.client.PWT;
import org.plazmaforge.framework.client.swt.controls.XComboEdit;
import org.plazmaforge.framework.client.swt.controls.XCurrencyField;
import org.plazmaforge.framework.client.swt.controls.XDateCombo;
import org.plazmaforge.framework.client.swt.controls.XTextField;

/** 
 * @author Oleh Hapon
 * $Id: EmployeeDischargeEditForm.java,v 1.3 2010/04/28 06:31:04 ohapon Exp $
 */
public class EmployeeDischargeEditForm extends AbstractDocumentEditForm {

    private Label documentNoLabel;

    private Label documentDateLabel;

    private Label employeeLabel;

    private Label dischargeDateLabel;

    private Label dischargeTypeLabel;

    private Label compensationDayLabel;

    private Label outputDayLabel;

    private Label pensionAmountLabel;

    private Label responsibleLabel;

    private XTextField documentNoField;

    private XDateCombo documentDateField;

    private XComboEdit employeeField;

    private XDateCombo dischargeDateField;

    private XComboEdit dischargeTypeField;

    private XCurrencyField compensationDayField;

    private XCurrencyField outputDayField;

    private XCurrencyField pensionAmountField;

    private XComboEdit responsibleField;

    /**
     * Create the form
     * @param parent
     * @param style
     */
    public EmployeeDischargeEditForm(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    private void initialize() {
        TabFolder tabFolder;
        TabItem generalTabItem;
        Composite composite;
        GridData gridData;
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginHeight = 0;
        setTitle(Messages.getString("EmployeeDischargeEditForm.title"));
        setLayout(gridLayout);
        tabFolder = new TabFolder(this, SWT.NONE);
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        generalTabItem = new TabItem(tabFolder, SWT.NONE);
        generalTabItem.setText(Messages.getString("EmployeeDischargeEditForm.generalTabItem.text"));
        composite = new Composite(tabFolder, SWT.NONE);
        gridLayout = new GridLayout();
        gridLayout.marginBottom = 10;
        gridLayout.marginTop = 10;
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);
        generalTabItem.setControl(composite);
        documentNoLabel = new Label(composite, SWT.NONE);
        documentNoLabel.setText(Messages.getString("EmployeeDischargeEditForm.documentNoLabel.text"));
        documentNoField = new XTextField(composite, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        documentNoField.setLayoutData(gridData);
        documentNoField.setTextLimit(20);
        documentDateLabel = new Label(composite, SWT.NONE);
        documentDateLabel.setText(Messages.getString("EmployeeDischargeEditForm.documentDateLabel.text"));
        documentDateField = new XDateCombo(composite, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        documentDateField.setLayoutData(gridData);
        employeeLabel = new Label(composite, SWT.NONE);
        employeeLabel.setText(Messages.getString("EmployeeDischargeEditForm.employeeLabel.text"));
        employeeField = new XComboEdit(composite, SWT.BORDER, PWT.VIEW_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 250;
        employeeField.setLayoutData(gridData);
        dischargeDateLabel = new Label(composite, SWT.NONE);
        dischargeDateLabel.setText(Messages.getString("EmployeeDischargeEditForm.dischargeDateLabel.text"));
        dischargeDateField = new XDateCombo(composite, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        dischargeDateField.setLayoutData(gridData);
        dischargeTypeLabel = new Label(composite, SWT.NONE);
        dischargeTypeLabel.setText(Messages.getString("EmployeeDischargeEditForm.dischargeTypeLabel.text"));
        dischargeTypeField = new XComboEdit(composite, SWT.BORDER, PWT.POPUP_BUTTON);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 250;
        dischargeTypeField.setLayoutData(gridData);
        compensationDayLabel = new Label(composite, SWT.NONE);
        compensationDayLabel.setText(Messages.getString("EmployeeDischargeEditForm.compensationDayLabel.text"));
        compensationDayField = new XCurrencyField(composite, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        compensationDayField.setLayoutData(gridData);
        outputDayLabel = new Label(composite, SWT.NONE);
        outputDayLabel.setText(Messages.getString("EmployeeDischargeEditForm.outputDayLabel.text"));
        outputDayField = new XCurrencyField(composite, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        outputDayField.setLayoutData(gridData);
        pensionAmountLabel = new Label(composite, SWT.NONE);
        pensionAmountLabel.setText(Messages.getString("EmployeeDischargeEditForm.pensionAmountLabel.text"));
        pensionAmountField = new XCurrencyField(composite, SWT.BORDER);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        gridData.widthHint = 100;
        pensionAmountField.setLayoutData(gridData);
        responsibleLabel = new Label(composite, SWT.NONE);
        responsibleLabel.setText(Messages.getString("EmployeeDischargeEditForm.responsibleLabel.text"));
        responsibleField = new XComboEdit(composite, SWT.BORDER, PWT.VIEW_BUTTON | PWT.DELETE_BUTTON);
        gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        responsibleField.setLayoutData(gridData);
        this.setSize(new Point(600, 350));
        employeeField.setAssociationValueClass(Employee.class);
        initResponsibleField(responsibleField);
    }

    protected void bindControls() {
        bindControl(documentNoField, "documentNo", documentNoLabel, REQUIRED);
        bindControl(documentDateField, "documentDate", documentDateLabel, REQUIRED);
        bindControl(employeeField, "employee", employeeLabel, REQUIRED);
        bindControl(dischargeDateField, "dischargeDate", dischargeDateLabel, REQUIRED);
        bindControl(dischargeTypeField, "dischargeType", dischargeTypeLabel, REQUIRED);
        bindControl(compensationDayField, "compensationDay");
        bindControl(outputDayField, "outputDay");
        bindControl(pensionAmountField, "pensionAmount");
        bindControl(responsibleField, "responsible");
    }
}
