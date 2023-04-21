package jtk.project4.fleet.field;

import jtk.project4.fleet.domain.Parts;
import nl.coderight.jazz.form.FormField;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormLayout.Resize;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.button.CheckButton;
import nl.coderight.jazz.form.field.ComboField;
import nl.coderight.jazz.form.field.SelectField;
import nl.coderight.jazz.form.field.TextField;

public class PartIdentificationField extends GroupControl<Parts> {

    private TextField partField;

    private TextField nameField;

    private TextField descriptionField;

    private TextField unitCostField;

    private ComboField<Parts> unitOfMeasure;

    private TextField warrantyFieldField;

    private TextField upcField;

    private TextField udf1;

    private TextField udf2;

    private SelectField manufacturerField;

    private SelectField markUpTypeField;

    private SelectField categoryField;

    private CheckButton trackCheck;

    public PartIdentificationField(String bindID) {
        setTitle("header.PartIdentification");
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        partField = new TextField("partId", 40);
        partField.setEditable(false);
        nameField = new TextField("name", 40);
        descriptionField = new TextField("desc", 40);
        unitCostField = new TextField("unitCost", 15);
        unitOfMeasure = new ComboField<Parts>("PartIdentification", "unitOfMeasure");
        unitOfMeasure.setPreferredSize(unitCostField.getPreferredSize());
        unitOfMeasure.setEditable(false);
        markUpTypeField = new SelectField<Parts>("PartIdentification", "markupType");
        markUpTypeField.setPreferredSize(unitCostField.getPreferredSize());
        warrantyFieldField = new TextField("warr", 40);
        udf1 = new TextField("udf1", 15);
        udf2 = new TextField("udf2", 15);
        upcField = new TextField("upcId", 20);
        manufacturerField = new SelectField<Parts>("PartIdentification", "");
        manufacturerField.setPreferredSize(partField.getPreferredSize());
        categoryField = new SelectField<Parts>("PartIdentification", "catId");
        categoryField.setPreferredSize(partField.getPreferredSize());
        trackCheck = new CheckButton("Track Inventory For This Part");
    }

    private void createLayout() {
        setLayout(new FormLayout()).addLabel("Part #").addField(partField).addRow().addLabel("Name").addField(nameField).addRow().addLabel("Description").addField(descriptionField).addRow().addLabel("Manufacturer").addField(manufacturerField).addRow().addLabel("Category").addField(categoryField).addRow().addLabel("Unit Cost").addField(unitCostField).addLabel("Unit Of Measure").addField(unitOfMeasure, Resize.HORIZONTAL).addRow().addLabel("Mark-Up Type").addField(markUpTypeField).addRow().addLabel("label.warranty").addField(warrantyFieldField).addRow().addLabel("label.upc").addField(upcField).addRow().addLabel("UDF #1").addField(udf1).addRow().addLabel("UDF #2").addField(udf2).addRow().addField(trackCheck);
    }
}
