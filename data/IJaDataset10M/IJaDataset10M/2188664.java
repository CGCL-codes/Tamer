package jtk.project4.fleet.screen.advancedOptions;

import java.awt.FlowLayout;
import java.util.List;
import jtk.project4.fleet.event.AdvancedOptionsEvent;
import jtk.project4.fleet.field.AdvancedOptionsField;
import jtk.project4.fleet.field.DriverLicenseInformationField;
import jtk.project4.fleet.field.GeneralInformationField;
import jtk.project4.fleet.field.MicellaneousField;
import jtk.project4.fleet.field.PersonnelInformationField;
import jtk.project4.fleet.field.TaskTerminationField;
import jtk.project4.fleet.field.SeasonalTrackingField;
import nl.coderight.jazz.form.FormHeader;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormView;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.button.PushButton;

public class AdvancedOptionsView extends FormView {

    private FormHeader formHeader;

    private PushButton submitButton;

    private PushButton cancelButton;

    @Override
    protected void init() {
        createFields();
        createLayout();
    }

    private void createFields() {
        submitButton = new PushButton("OK");
    }

    private void createLayout() {
        setHeader(createHeader());
        addField(createForm());
    }

    private FormHeader createHeader() {
        formHeader = new FormHeader();
        formHeader.setTitle("advancedOptions.header.form");
        return formHeader;
    }

    private GroupControl createForm() {
        GroupControl form = new GroupControl();
        form.addField(createGeneralTab(), CENTER);
        form.addField(createButtons(), SOUTH);
        return form;
    }

    private GroupControl createButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(submitButton);
        return buttons;
    }

    private GroupControl createGeneralTab() {
        GroupControl form = new GroupControl();
        form.setLayout(new FormLayout()).addField(new AdvancedOptionsField("advancedOptions")).addRow().addField(new TaskTerminationField("advancedOptions")).addRow().addField(new SeasonalTrackingField("advancedOptions"));
        return form;
    }
}
