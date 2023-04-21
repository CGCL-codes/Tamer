package jtk.project4.fleet.field;

import java.awt.FlowLayout;
import java.util.logging.Filter;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.ComboField;
import nl.coderight.jazz.form.field.SearchField;
import nl.coderight.jazz.form.field.SliderField;
import nl.coderight.jazz.form.field.TableField;
import nl.coderight.jazz.form.field.TextField;
import nl.coderight.jazz.form.field.TableField.AutoResizeMode;
import nl.coderight.jazz.form.field.button.CheckButton;
import nl.coderight.jazz.form.field.button.PushButton;
import nl.coderight.jazz.form.field.button.RadioButton;
import nl.coderight.jazz.form.field.input.InputField;
import nl.coderight.jazz.form.field.scrollable.ScrollableField;
import nl.coderight.jazz.form.field.selectable.SelectableField;

public class TireInventoryField extends GroupControl {

    private ComboField<String> viewComboField;

    private TextField tireTextField;

    private TableField tireTableField;

    private PushButton addtirePushButton;

    private PushButton edittirePushButton;

    private PushButton deletetirePushButton;

    private PushButton printtirePushButton;

    private PushButton filtertirePushButton;

    private PushButton searchtirePushButton;

    private CheckButton check;

    public TireInventoryField(String bindID) {
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        tireTableField = new TableField("", "");
        tireTableField.setAutoResizeMode(AutoResizeMode.OFF);
        tireTableField.addColumn("label.Serial#", "");
        tireTableField.addColumn("label.Brand", "");
        tireTableField.addColumn("label.Model", "");
        tireTableField.addColumn("label.Size", "");
        tireTableField.addColumn("label.Total mi/KM/hr", "");
        tireTableField.addColumn("label.Cost mi/KM/hr", "");
        tireTableField.addColumn("label.Total Cost", "");
        tireTableField.addColumn("label.Location(Equipment)", "");
        tireTableField.addColumn("label.Tread", "");
        tireTableField.addColumn("label.Tread Depth", "");
        tireTableField.addColumn("label.Load", "");
        tireTableField.addColumn("label.Traction", "");
        tireTableField.addColumn("label.Vendor", "");
        tireTableField.addColumn("label.Invoice #", "");
        tireTableField.addColumn("label.Price", "");
        tireTableField.addColumn("label.Purchase Date", "");
        tireTableField.addColumn("label.Notes", "");
        tireTableField.addColumn("label.Under Warranty", "");
        tireTableField.addColumn("label.Warranty Days", "");
        tireTableField.addColumn("label.Warranty Meter", "");
        tireTableField.addColumn("label.Type", "");
        tireTableField.addColumn("label.Status", "");
        tireTableField.addColumn("label.Location", "");
        addtirePushButton = new PushButton("Add");
        edittirePushButton = new PushButton("Edit");
        deletetirePushButton = new PushButton("Delete");
        printtirePushButton = new PushButton("Print");
        filtertirePushButton = new PushButton("Filter");
        searchtirePushButton = new PushButton("Search");
        check = new CheckButton("Show row filter");
        viewComboField = new ComboField<String>("inventory", "");
        tireTextField = new TextField("", 20);
        tireTextField.setEditable(false);
    }

    private GroupControl createButtonsField1() {
        GroupControl b = new GroupControl();
        b.setLayout(new FlowLayout(FlowLayout.LEFT));
        b.addField(tireTableField);
        return b;
    }

    private GroupControl createButtonsFields2() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttons.addField(tireTextField);
        buttons.addField(addtirePushButton);
        buttons.addField(edittirePushButton);
        buttons.addField(deletetirePushButton);
        buttons.addField(printtirePushButton);
        buttons.addField(filtertirePushButton);
        buttons.addField(searchtirePushButton);
        buttons.addField(check);
        buttons.addField(viewComboField);
        return buttons;
    }

    private GroupControl createButtonsField3() {
        GroupControl b = new GroupControl();
        b.setLayout(new FlowLayout(FlowLayout.LEFT));
        b.addField(tireTableField);
        return b;
    }

    private void createLayout() {
        setLayout(new FormLayout()).addField(tireTableField).addRow().addField(createButtonsFields2()).addRow();
    }
}
