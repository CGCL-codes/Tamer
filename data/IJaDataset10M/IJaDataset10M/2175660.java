package jtk.project4.fleet.field;

import java.awt.Dimension;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormLayout.Resize;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.control.RadioControl;
import nl.coderight.jazz.form.field.TextField;
import nl.coderight.jazz.form.field.button.RadioButton;

public class LaborRadioButtonField extends GroupControl {

    private RadioControl<String> radioControl;

    private RadioButton radioButton1;

    private RadioButton radioButton2;

    private TextField costField;

    public LaborRadioButtonField(String bindID) {
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        radioControl = new RadioControl<String>("radio control");
        radioButton1 = new RadioButton("Itemize labor requirments");
        radioButton2 = new RadioButton("Specify labor cost only: ");
        radioControl.addRadioButton(radioButton1, "Itemize labor requirments");
        radioControl.addRadioButton(radioButton2, "Specify labor cost only:");
        costField = new TextField("$0.00", 10);
        costField.setEditable(false);
    }

    private void createLayout() {
        setLayout(new FormLayout()).addField(radioControl).addField(radioControl).addField(costField);
    }
}
