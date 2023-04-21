package jtk.project4.fleet.screen.newEditSchedule;

import java.awt.FlowLayout;
import java.util.List;
import jtk.project4.fleet.action.CancelAction;
import jtk.project4.fleet.action.CancelNewEditScheduleAction;
import jtk.project4.fleet.action.SaveNewScheduleAction;
import jtk.project4.fleet.action.SubmitAction;
import jtk.project4.fleet.event.NewEditScheduleEvent;
import jtk.project4.fleet.field.ButtomNewEditScheduleField;
import jtk.project4.fleet.field.MainNewEditScheduleField;
import jtk.project4.fleet.field.NewEditScheduleField;
import jtk.project4.fleet.field.addMaintenanceTaskField;
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

public class newEditScheduleView extends FormView {

    private FormHeader formHeader;

    private PushButton saveButton;

    private PushButton cancelButton;

    private PushButton helpButton;

    private Action cancelAction;

    private Action submitAction;

    @Override
    protected void init() {
        createAction();
        createFields();
        createLayout();
    }

    private FormHeader createHeader() {
        formHeader = new FormHeader();
        formHeader.setTitle("NewEditSchedule.header.form");
        return formHeader;
    }

    private void createLayout() {
        setHeader(createHeader());
        addField(createForm());
    }

    private GroupControl createForm() {
        GroupControl form = new GroupControl();
        form.addField(new MainNewEditScheduleField("Schedules"), NORTH);
        form.addField(createButtons(), SOUTH);
        return form;
    }

    public void handleEvent(NewEditScheduleEvent evt) {
        switch(evt.getType()) {
            case CANCEL:
                close();
                break;
            case SAVE:
                List<ValidationError> errors = validate();
                if (!errors.isEmpty()) {
                    errors.get(0).getField().focus();
                    break;
                } else {
                    submit();
                    close();
                }
            default:
                propagateEvent(evt);
                break;
        }
    }

    private void createAction() {
        submitAction = new SaveNewScheduleAction();
        cancelAction = new CancelNewEditScheduleAction();
        setOnChangeAction(submitAction);
    }

    private void createFields() {
        saveButton = new PushButton(submitAction);
        cancelButton = new PushButton(cancelAction);
        helpButton = new PushButton("helpAction");
    }

    private GroupControl createButtons() {
        GroupControl buttons = new GroupControl();
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttons.addField(saveButton);
        buttons.addField(cancelButton);
        return buttons;
    }
}
