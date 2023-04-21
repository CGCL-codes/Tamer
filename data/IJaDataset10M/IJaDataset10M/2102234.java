package org.plazmaforge.bsolution.payroll.client.swing.forms;

import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import org.plazmaforge.bsolution.payroll.client.swing.GUIPayrollEnvironment;
import org.plazmaforge.bsolution.payroll.common.beans.EmployeeVacation;
import org.plazmaforge.bsolution.payroll.common.beans.EmployeeVacationAverange;
import org.plazmaforge.framework.client.swing.controls.XCurrencyField;
import org.plazmaforge.framework.client.swing.controls.XIntegerField;
import org.plazmaforge.framework.client.swing.forms.EXTChildEditForm;
import org.plazmaforge.framework.client.swing.gui.GridBagPanel;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * @author Oleh Hapon
 * Date: 04.11.2004
 * Time: 8:25:41
 * $Id: EmployeeLeaveAverangeEdit.java,v 1.3 2010/12/05 07:56:47 ohapon Exp $
 */
public class EmployeeLeaveAverangeEdit extends EXTChildEditForm {

    private JLabel monthLabel;

    private JLabel workDayLabel;

    private JLabel leaveDayLabel;

    private JLabel addLeaveDayLabel;

    private JLabel amountLabel;

    private XIntegerField monthField;

    private XIntegerField workDayField;

    private XIntegerField leaveDayField;

    private XIntegerField addLeaveDayField;

    private XCurrencyField amountField;

    public EmployeeLeaveAverangeEdit() throws ApplicationException {
        super(GUIPayrollEnvironment.getResources());
        initialize();
    }

    private void initialize() {
        this.setEntityClass(EmployeeVacationAverange.class);
    }

    protected void initComponents() throws ApplicationException {
        setTitle(getString("title"));
        monthLabel = new JLabel(getString("panel.label-month.text"));
        workDayLabel = new JLabel(getString("panel.label-work-day.text"));
        leaveDayLabel = new JLabel(getString("panel.label-leave-day.text"));
        addLeaveDayLabel = new JLabel(getString("panel.label-add-leave-day.text"));
        amountLabel = new JLabel(getString("panel.label-amount.text"));
        monthField = new XIntegerField(20);
        workDayField = new XIntegerField(20);
        leaveDayField = new XIntegerField(20);
        addLeaveDayField = new XIntegerField(20);
        amountField = new XCurrencyField(20);
        GridBagPanel editPanel = new GridBagPanel();
        GridBagConstraints gbc = editPanel.getGridBagConstraints();
        editPanel.add(monthLabel);
        editPanel.addByY(workDayLabel);
        editPanel.addByY(leaveDayLabel);
        editPanel.addByY(addLeaveDayLabel);
        editPanel.addByY(amountLabel);
        gbc.gridy = 0;
        editPanel.addByX(monthField);
        editPanel.addByY(workDayField);
        editPanel.addByY(leaveDayField);
        editPanel.addByY(addLeaveDayField);
        editPanel.addByY(amountField);
        add(editPanel);
    }

    private EmployeeVacationAverange getEmployeeLeaveAverange() {
        return (EmployeeVacationAverange) this.getEntity();
    }

    protected void updateView() throws ApplicationException {
        monthField.setValue(getEmployeeLeaveAverange().getMonth());
        workDayField.setValue(getEmployeeLeaveAverange().getWorkDay());
        leaveDayField.setValue(getEmployeeLeaveAverange().getVacationDay());
        addLeaveDayField.setValue(getEmployeeLeaveAverange().getAddVacationDay());
        amountField.setValue(getEmployeeLeaveAverange().getAmount());
    }

    protected void populateData() throws ApplicationException {
        getEmployeeLeaveAverange().setMonth(monthField.intValue());
        getEmployeeLeaveAverange().setWorkDay(workDayField.intValue());
        getEmployeeLeaveAverange().setVacationDay(leaveDayField.intValue());
        getEmployeeLeaveAverange().setAddVacationDay(addLeaveDayField.intValue());
        getEmployeeLeaveAverange().setAmount(amountField.floatValue());
    }

    private EmployeeVacation employeeLeave;

    public EmployeeVacation getEmployeeLeave() {
        return employeeLeave;
    }

    public void setEmployeeLeave(EmployeeVacation datable) {
        this.employeeLeave = datable;
    }

    protected void addChild() throws ApplicationException {
        getEmployeeLeave().addAverangeItem(getEmployeeLeaveAverange());
    }
}
