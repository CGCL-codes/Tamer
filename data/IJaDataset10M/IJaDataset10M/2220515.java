package jtk.project4.fleet.field;

import java.awt.Dimension;
import jtk.project4.fleet.domain.Employee;
import jtk.project4.fleet.domain.Objects;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.SelectField;

public class CostStatisticSelectField extends GroupControl<Employee> {

    private SelectField<Objects> equipselectField;

    public CostStatisticSelectField(String bindID) {
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        equipselectField = new SelectField<Objects>("objectTitle", "objectTitle");
        equipselectField.setPreferredSize(new Dimension(250, 20));
    }

    private void createLayout() {
        setLayout(new FormLayout()).addLabel("Equipment Selection").addField(equipselectField);
    }
}
