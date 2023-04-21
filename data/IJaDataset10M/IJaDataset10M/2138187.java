package jtk.project4.fleet.field;

import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormLayout.Resize;
import nl.coderight.jazz.form.control.CalendarControl;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.ComboField;
import nl.coderight.jazz.form.field.NumberField;
import nl.coderight.jazz.form.field.SelectField;
import nl.coderight.jazz.form.field.TextField;

public class PoliceAccidentField extends GroupControl {

    private TextField departementField;

    private TextField officerField;

    private TextField reportField;

    private NumberField phoneField;

    public PoliceAccidentField(String bindID) {
        setTitle("Responding Police Unit");
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        departementField = new TextField("departement", 15);
        departementField.setRequired(true);
        officerField = new TextField("officer", 15);
        officerField.setRequired(true);
        reportField = new TextField("report", 15);
        reportField.setRequired(true);
        phoneField = new NumberField("phone", 15);
    }

    private void createLayout() {
        setLayout(new FormLayout()).addLabel("label.departement").addField(departementField, Resize.HORIZONTAL).addRow().addLabel("label.officer").addField(officerField, Resize.HORIZONTAL).addRow().addLabel("label.report#").addField(reportField).addRow().addLabel("label.phone#").addField(phoneField);
    }
}
