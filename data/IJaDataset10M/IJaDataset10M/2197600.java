package jtk.project4.fleet.screen.addNewLocation;

import java.awt.FlowLayout;
import java.util.List;
import jtk.project4.fleet.event.EquipmentEvent;
import jtk.project4.fleet.field.AddNewLocationField;
import jtk.project4.fleet.field.ButtonEastField;
import jtk.project4.fleet.field.EditCompanyNameField;
import jtk.project4.fleet.field.TreeWestField;
import nl.coderight.jazz.action.Action;
import nl.coderight.jazz.dialog.MessageType;
import nl.coderight.jazz.dialog.OptionDialog;
import nl.coderight.jazz.dialog.OptionType;
import nl.coderight.jazz.dialog.ReturnOptionType;
import nl.coderight.jazz.form.FormHeader;
import nl.coderight.jazz.form.FormView;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.button.PushButton;
import nl.coderight.jazz.form.validation.ValidationError;

public class AddNewLocationView extends FormView {

    private FormHeader formHeader;

    private PushButton submitButton;

    private PushButton cancelButton;

    @Override
    protected void init() {
        createFields();
        createLayout();
    }

    private void createFields() {
        submitButton = new PushButton("okAction");
        cancelButton = new PushButton("cancelAction");
    }

    private void createLayout() {
        setHeader(createHeader());
        addField(createForm());
    }

    private FormHeader createHeader() {
        formHeader = new FormHeader();
        formHeader.setTitle("addNewLocation.header.form");
        return formHeader;
    }

    private GroupControl createForm() {
        GroupControl form = new GroupControl();
        form.addField(new AddNewLocationField("AddNewLocation"), NORTH);
        form.addField(createButtons(), SOUTH);
        return form;
    }

    private GroupControl createButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttons.addField(submitButton);
        buttons.addField(cancelButton);
        return buttons;
    }
}
