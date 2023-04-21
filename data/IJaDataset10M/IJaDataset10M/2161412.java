package jtk.project4.fleet.field;

import jtk.project4.fleet.domain.Employee;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.TextField;

public class MicellaneousField extends GroupControl<Employee> {

    private TextField custVal1Field;

    private TextField custVal2Field;

    private TextField custVal3Field;

    private TextField custVal4Field;

    public MicellaneousField(String bindID) {
        setTitle("header.micellaneous");
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        custVal1Field = new TextField("custLbl3", 20);
        custVal2Field = new TextField("custVal3", 20);
        custVal3Field = new TextField("custLbl4", 20);
        custVal4Field = new TextField("custVal4", 20);
    }

    private void createLayout() {
        setLayout(new FormLayout()).addLabel("(user defined)").addField(custVal1Field).addLabel("(user defined)").addField(custVal2Field).addRow().addLabel("(user defined)").addField(custVal3Field).addLabel("(user defined)").addField(custVal4Field);
    }
}
