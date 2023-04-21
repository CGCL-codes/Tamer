package jtk.project4.fleet.field;

import java.awt.FlowLayout;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.ImageField;
import nl.coderight.jazz.form.field.button.PushButton;
import nl.coderight.jazz.form.field.scrollable.ScrollableField;

public class MeterReplacementsButtonField extends GroupControl<String> {

    private PushButton buttonfield1;

    private PushButton buttonfield2;

    private PushButton buttonfield3;

    private PushButton buttonfield4;

    private PushButton buttonfield5;

    public MeterReplacementsButtonField(String bindID) {
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        buttonfield1 = new PushButton("Add");
        buttonfield2 = new PushButton("Edit");
        buttonfield3 = new PushButton("Delete");
        buttonfield4 = new PushButton("Print");
        buttonfield5 = new PushButton("Search");
    }

    private void createLayout() {
        setLayout(new FormLayout()).addField(buttonfield1).addField(buttonfield2).addField(buttonfield3).addField(buttonfield4).addField(buttonfield5);
        ;
    }

    private GroupControl createAddButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttons.addField(buttonfield1);
        return buttons;
    }

    private GroupControl createEditButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(buttonfield2);
        return buttons;
    }

    private GroupControl createDeleteButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(buttonfield3);
        return buttons;
    }

    private GroupControl createPrintButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(buttonfield4);
        return buttons;
    }

    private GroupControl createSearchButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(buttonfield5);
        return buttons;
    }
}
