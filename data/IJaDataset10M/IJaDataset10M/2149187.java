package jtk.project4.fleet.field;

import jtk.project4.fleet.domain.Equipment;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormLayout.Resize;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.ComboField;
import nl.coderight.jazz.form.field.ImageField;
import nl.coderight.jazz.form.field.NumberField;
import nl.coderight.jazz.form.field.SelectField;
import nl.coderight.jazz.form.field.TextField;
import nl.coderight.jazz.form.field.SpinnerField;

public class EquipmentTiresTiresCofigurationField extends GroupControl {

    private ImageField TireConfiguration;

    public EquipmentTiresTiresCofigurationField(String bindId) {
        setTitle(null);
        setBindID(bindId);
        createFields();
        createLayout();
    }

    private void createFields() {
        TireConfiguration = new ImageField();
    }

    private void createLayout() {
        setLayout(new FormLayout()).addLabel("Tire Configuration : ").addField(TireConfiguration);
    }
}
