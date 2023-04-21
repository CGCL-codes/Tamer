package jtk.project4.fleet.screen.tireHistory;

import java.awt.FlowLayout;
import jtk.project4.fleet.field.TireHistory1Field;
import jtk.project4.fleet.field.TireHistory2Field;
import nl.coderight.jazz.form.FormField;
import nl.coderight.jazz.form.FormHeader;
import nl.coderight.jazz.form.FormView;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.button.PushButton;

public class TireHistoryView extends FormView {

    private FormHeader formHeader;

    private PushButton helpButton;

    private PushButton cancelButton;

    @Override
    protected void init() {
        CreateFields();
        CreateLayout();
    }

    private void CreateFields() {
        helpButton = new PushButton("Help");
        cancelButton = new PushButton("Cancel");
    }

    private void CreateLayout() {
        setHeader(CreateHeader());
        addField(CreateForm());
    }

    private FormHeader CreateHeader() {
        formHeader = new FormHeader();
        formHeader.setTitle("TireHistory.header.form");
        return formHeader;
    }

    private FormField CreateForm() {
        GroupControl form = new GroupControl();
        form.addField(new TireHistory1Field(""), NORTH);
        form.addField(new TireHistory2Field(""), CENTER);
        form.addField(CreateButtons(), SOUTH);
        return form;
    }

    private GroupControl CreateButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(helpButton);
        buttons.addField(cancelButton);
        return buttons;
    }
}
