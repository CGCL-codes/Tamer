package jtk.project4.fleet.field;

import jtk.project4.fleet.domain.Schedules;
import nl.coderight.jazz.form.FormLayout;
import nl.coderight.jazz.form.FormLayout.Align;
import nl.coderight.jazz.form.FormLayout.Resize;
import nl.coderight.jazz.form.control.GroupControl;
import nl.coderight.jazz.form.field.ComboField;
import nl.coderight.jazz.form.field.TextField;
import nl.coderight.jazz.form.field.button.CheckButton;

public class NewEditScheduleField extends GroupControl<Schedules> {

    private TextField scheduleNameField;

    private CheckButton trackByDateField;

    private CheckButton trackByMeterPField;

    private CheckButton trackByMeterSField;

    private ComboField<String> trackByMeterPrField;

    private ComboField trackByMeterScField;

    public NewEditScheduleField(String bindID) {
        setTitle("header.NewEditSchedule");
        setBindID(bindID);
        createFields();
        createLayout();
    }

    private void createFields() {
        scheduleNameField = new TextField("schedName", 20);
        trackByDateField = new CheckButton("Track By Date:");
        trackByMeterPField = new CheckButton("Track By Meter (Primary):");
        trackByMeterSField = new CheckButton("Track By Meter (Secondary):");
        trackByMeterPrField = new ComboField<String>("MeterPri", "MP");
        trackByMeterPrField.setPreferredSize(scheduleNameField.getPreferredSize());
        trackByMeterScField = new ComboField<String>("MeterSec", "MS");
        trackByMeterScField.setPreferredSize(scheduleNameField.getPreferredSize());
    }

    private void createLayout() {
        setLayout(new FormLayout()).addLabel("label.ScheduleName:").addField(scheduleNameField).addRow().addField(trackByDateField).addRow().addField(trackByMeterPField).addField(trackByMeterPrField).addRow().addField(trackByMeterSField).addField(trackByMeterScField);
    }
}
