package net.cygeek.tech.client.ui.form;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.DateField;
import net.cygeek.tech.client.adapters.TimeEventAdapter;
import net.cygeek.tech.client.data.TimeEvent;
import net.cygeek.tech.client.ohrm;
import net.cygeek.tech.client.cache.SystemCache;
import net.cygeek.tech.client.ui.grid.TimeEventGrid;
import net.cygeek.tech.client.util.*;
import java.util.Date;

/**
 *
 */
public class TimeEventForm extends AbstractForm {

    ComboBox cmbTimesheetId;

    ComboBox cmbEndTime;

    TextField txtTimeEventId;

    TextField txtDuration;

    ComboBox cmbEmployeeId;

    ComboBox cmbStartTime;

    TextField txtDescription;

    ComboBox cmbProjectId;

    ComboBox cmbActivityId;

    DateField txtReportedDate;

    DateField eventDate;

    Button save = new Button(TS.gi().get("L0001"));

    Button cancel = new Button(TS.gi().get("L0003"));

    public long periodStart = 0;

    public long periodEnd = 0;

    public TimeEventForm() {
        this.setFrame(true);
        this.setWidth(365);
        this.setLabelWidth(75);
        eventDate = new DateField(TS.gi().get("L0024"), "dateEvent", 230);
        cmbTimesheetId = new ComboBox();
        cmbTimesheetId.setForceSelection(true);
        cmbTimesheetId.setMinChars(1);
        cmbTimesheetId.setFieldLabel(TS.gi().get("L0238"));
        cmbTimesheetId.setDisplayField("status");
        cmbTimesheetId.setValueField("timesheetId");
        cmbTimesheetId.setMode(ComboBox.LOCAL);
        cmbTimesheetId.setTriggerAction(ComboBox.ALL);
        cmbTimesheetId.setEmptyText("Select");
        cmbTimesheetId.setLoadingText("Searching...");
        cmbTimesheetId.setTypeAhead(true);
        cmbTimesheetId.setSelectOnFocus(true);
        cmbTimesheetId.setWidth(230);
        cmbTimesheetId.setHideTrigger(false);
        cmbEndTime = new ComboBox(TS.gi().get("L0185"), "cmbEndTime", 230);
        cmbEndTime.setForceSelection(true);
        cmbEndTime.setMinChars(1);
        cmbEndTime.setFieldLabel(TS.gi().get("L0185"));
        cmbEndTime.setDisplayField("value");
        cmbEndTime.setValueField("id");
        cmbEndTime.setMode(ComboBox.LOCAL);
        cmbEndTime.setTriggerAction(ComboBox.ALL);
        cmbEndTime.setEmptyText(TS.gi().get("L0022"));
        cmbEndTime.setLoadingText(TS.gi().get("L0017"));
        cmbEndTime.setTypeAhead(true);
        cmbEndTime.setSelectOnFocus(true);
        cmbEndTime.setWidth(230);
        cmbEndTime.setHideTrigger(false);
        cmbEndTime.setStore(DataStore.getHours());
        txtTimeEventId = new TextField(TS.gi().get("L0019"), "txtTimeEventId", 230);
        txtDuration = new TextField(TS.gi().get("L0244"), "txtDuration", 230);
        cmbEmployeeId = new ComboBox();
        cmbEmployeeId.setForceSelection(true);
        cmbEmployeeId.setMinChars(1);
        cmbEmployeeId.setFieldLabel("EmployeeId");
        cmbEmployeeId.setDisplayField("empNickName");
        cmbEmployeeId.setValueField("empNumber");
        cmbEmployeeId.setMode(ComboBox.LOCAL);
        cmbEmployeeId.setTriggerAction(ComboBox.ALL);
        cmbEmployeeId.setEmptyText("Select");
        cmbEmployeeId.setLoadingText("Searching...");
        cmbEmployeeId.setTypeAhead(true);
        cmbEmployeeId.setSelectOnFocus(true);
        cmbEmployeeId.setWidth(230);
        cmbEmployeeId.setHideTrigger(false);
        cmbStartTime = new ComboBox(TS.gi().get("L0184"), "cmbStartTime", 230);
        cmbStartTime.setForceSelection(true);
        cmbStartTime.setMinChars(1);
        cmbStartTime.setFieldLabel(TS.gi().get("L0184"));
        cmbStartTime.setDisplayField("value");
        cmbStartTime.setValueField("id");
        cmbStartTime.setMode(ComboBox.LOCAL);
        cmbStartTime.setTriggerAction(ComboBox.ALL);
        cmbStartTime.setEmptyText(TS.gi().get("L0022"));
        cmbStartTime.setLoadingText(TS.gi().get("L0017"));
        cmbStartTime.setTypeAhead(true);
        cmbStartTime.setSelectOnFocus(true);
        cmbStartTime.setWidth(230);
        cmbStartTime.setHideTrigger(false);
        cmbStartTime.setStore(DataStore.getHours());
        txtDescription = new TextField(TS.gi().get("L0021"), "txtDescription", 230);
        cmbProjectId = new ComboBox();
        cmbProjectId.setForceSelection(true);
        cmbProjectId.setMinChars(1);
        cmbProjectId.setFieldLabel(TS.gi().get("L0232"));
        cmbProjectId.setDisplayField("name");
        cmbProjectId.setValueField("projectId");
        cmbProjectId.setMode(ComboBox.LOCAL);
        cmbProjectId.setTriggerAction(ComboBox.ALL);
        cmbProjectId.setEmptyText("Select");
        cmbProjectId.setLoadingText("Searching...");
        cmbProjectId.setTypeAhead(true);
        cmbProjectId.setSelectOnFocus(true);
        cmbProjectId.setWidth(230);
        cmbProjectId.setHideTrigger(false);
        cmbActivityId = new ComboBox();
        cmbActivityId.setForceSelection(true);
        cmbActivityId.setMinChars(1);
        cmbActivityId.setFieldLabel(TS.gi().get("L0236"));
        cmbActivityId.setDisplayField("name");
        cmbActivityId.setValueField("activityId");
        cmbActivityId.setMode(ComboBox.LOCAL);
        cmbActivityId.setTriggerAction(ComboBox.ALL);
        cmbActivityId.setEmptyText("Select");
        cmbActivityId.setLoadingText("Searching...");
        cmbActivityId.setTypeAhead(true);
        cmbActivityId.setSelectOnFocus(true);
        cmbActivityId.setWidth(230);
        cmbActivityId.setHideTrigger(false);
        txtReportedDate = new DateField("Reported Date", "txtReportedDate", 230);
        this.add(cmbProjectId);
        this.add(cmbActivityId);
        this.add(eventDate);
        this.add(cmbStartTime);
        this.add(cmbEndTime);
        this.add(txtDescription);
        save.addListener(new SaveButtonAdapter(this));
        this.addButton(save);
        cancel.addListener(new CancelButtonAdapter(this));
        this.addButton(cancel);
    }

    public void reinitialize(int mode) {
        this.isNew = (mode == AbstractWindow.ADD);
        cmbTimesheetId.setReadOnly(!isNew);
        txtTimeEventId.setReadOnly(!isNew);
        cmbEmployeeId.setReadOnly(!isNew);
        cmbProjectId.setReadOnly(!isNew);
        cmbProjectId.setStore(ohrm.GR_Project.store);
        cmbActivityId.setStore(ohrm.GR_ProjectActivity.store);
        if (isNew) {
            cmbTimesheetId.setValue(null);
            cmbEndTime.setValue(null);
            txtDuration.setValue(null);
            cmbEmployeeId.setValue(null);
            cmbStartTime.setValue(null);
            txtDescription.setValue(null);
            cmbProjectId.setValue(null);
            cmbActivityId.setValue(null);
            txtReportedDate.setValue(new Date());
            txtTimeEventId.setValue(String.valueOf(getNextEventNumber()));
        } else if (mode == AbstractWindow.VIEW) {
            cmbTimesheetId.setReadOnly(true);
            cmbEndTime.setReadOnly(true);
            txtTimeEventId.setReadOnly(true);
            txtDuration.setReadOnly(true);
            cmbEmployeeId.setReadOnly(true);
            cmbStartTime.setReadOnly(true);
            txtDescription.setReadOnly(true);
            cmbProjectId.setReadOnly(true);
            cmbActivityId.setReadOnly(true);
            txtReportedDate.setReadOnly(true);
            save.setVisible(false);
        }
        if (mode != AbstractWindow.VIEW) {
            save.setVisible(true);
            cmbTimesheetId.setReadOnly(false);
            cmbEndTime.setReadOnly(false);
            txtTimeEventId.setReadOnly(false);
            txtDuration.setReadOnly(false);
            cmbEmployeeId.setReadOnly(false);
            cmbStartTime.setReadOnly(false);
            txtDescription.setReadOnly(false);
            cmbProjectId.setReadOnly(false);
            cmbActivityId.setReadOnly(false);
            txtReportedDate.setReadOnly(false);
        }
        if (mode != AbstractWindow.ADD) {
            cmbTimesheetId.setReadOnly(true);
            txtTimeEventId.setReadOnly(true);
            cmbEmployeeId.setReadOnly(true);
            cmbProjectId.setReadOnly(true);
        } else {
            cmbTimesheetId.setReadOnly(false);
            txtTimeEventId.setReadOnly(false);
            cmbEmployeeId.setReadOnly(false);
            cmbProjectId.setReadOnly(false);
        }
    }

    public void save() {
        if (cmbProjectId.getText() == null || cmbProjectId.getText() == "") {
            MessageStore.showIsEmptyError("L0232");
            return;
        }
        if (cmbActivityId.getText() == null || cmbActivityId.getText() == "") {
            MessageStore.showIsEmptyError("L0236");
            return;
        }
        if (cmbStartTime.getText() == null || cmbStartTime.getText() == "") {
            MessageStore.showIsEmptyError("L0184");
            return;
        }
        if (cmbEndTime.getText() == null || cmbEndTime.getText() == "") {
            MessageStore.showIsEmptyError("L0185");
            return;
        }
        if (eventDate.getValue() == null) {
            MessageStore.showIsEmptyError("L0185");
            return;
        }
        long stime = ODateFormat.getTimeInMills(eventDate.getValue(), cmbStartTime.getValue());
        long etime = ODateFormat.getTimeInMills(eventDate.getValue(), cmbEndTime.getValue());
        if (stime >= etime) {
            MessageStore.showPlain("msg0014", MessageBox.ERROR);
            return;
        }
        if (!CalendarUtils.isDayBetween(((TimeEventGrid) grid).getStartDate(), ((TimeEventGrid) grid).getEndDate(), eventDate.getValue())) {
            MessageStore.showPlain("msg0018");
            return;
        }
        TimeEvent loc = new TimeEvent();
        loc.setTimesheetId(((TimeEventGrid) grid).getTimeSheetId());
        loc.setEndTime(String.valueOf(etime));
        loc.setTimeEventId(Integer.parseInt(txtTimeEventId.getText()));
        loc.setDuration(CalendarUtils.getTimeDiffinMinutes(etime, stime));
        loc.setEmployeeId(getPriId());
        loc.setStartTime(String.valueOf(stime));
        loc.setDescription(txtDescription.getText());
        loc.setProjectId(Integer.parseInt(cmbProjectId.getValue()));
        loc.setActivityId(Integer.parseInt(cmbActivityId.getValue()));
        loc.setReportedDate(String.valueOf(SystemCache.currentDate.getTime()));
        TimeEventAdapter.getInstance().addTimeEvent(loc, getGrid(), isNew);
        getGrid().window.hide();
    }

    public void cancel() {
        getGrid().window.hide();
    }

    public void setData(Record record) {
        int k = ((TimeEventGrid) ohrm.GR_TimeEvent).getTimeSheetId();
        TimeEvent loc = (TimeEvent) getItemByID(String.valueOf(k) + "#" + record.getAsString("timeEventId") + "#" + String.valueOf(getPriId()) + "#" + record.getAsString("projectid"));
        cmbTimesheetId.setValue(String.valueOf(loc.getTimesheetId()));
        Debug.gi().print("End Time :" + ODateFormat.getTimeFromString(loc.getEndTime()));
        cmbEndTime.setValue(ODateFormat.getTimeFromString(loc.getEndTime()));
        txtTimeEventId.setValue(String.valueOf(loc.getTimeEventId()));
        txtDuration.setValue(String.valueOf(loc.getDuration()));
        cmbEmployeeId.setValue(String.valueOf(loc.getEmployeeId()));
        Debug.gi().print("Start Time :" + ODateFormat.getTimeFromString(loc.getStartTime()));
        cmbStartTime.setValue(ODateFormat.getTimeFromString(loc.getStartTime()));
        txtDescription.setValue(String.valueOf(loc.getDescription()));
        cmbProjectId.setValue(String.valueOf(loc.getProjectId()));
        cmbActivityId.setValue(String.valueOf(loc.getActivityId()));
        eventDate.setValue(ODateFormat.getDateFromString(loc.getStartTime()));
        txtReportedDate.setValue(ODateFormat.getDateFromString(loc.getReportedDate()));
    }

    private int getNextEventNumber() {
        int k = 0;
        for (int i = 0; i < grid.data.size(); i++) {
            int j = ((TimeEvent) grid.data.get(i)).getTimeEventId();
            if (j >= k) {
                k = j + 1;
            }
        }
        return k;
    }

    class SaveButtonAdapter extends ButtonListenerAdapter {

        AbstractForm parent;

        SaveButtonAdapter(AbstractForm parent) {
            this.parent = parent;
        }

        public void onClick(Button button, EventObject e) {
            parent.save();
        }
    }

    class CancelButtonAdapter extends ButtonListenerAdapter {

        AbstractForm parent;

        CancelButtonAdapter(AbstractForm parent) {
            this.parent = parent;
        }

        public void onClick(Button button, EventObject e) {
            parent.cancel();
        }
    }
}
