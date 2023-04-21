package com.ecmdeveloper.plugin.search.wizards;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import com.ecmdeveloper.plugin.search.model.IQueryField;

/**
 * @author ricardo.belfor
 *
 */
public class DateMultiValueWizardPage extends MultiValueWizardPage {

    private DateTime calendar;

    private DateTime time;

    private ComboViewer timeZonesCombo;

    public DateMultiValueWizardPage(IQueryField queryField) {
        super(queryField);
        setDescription("Enter Date Values");
    }

    @Override
    protected void createValuesControls(Composite parent) {
        createCalendar(parent);
        createTime(parent);
        createTimeZones(parent);
    }

    private void createTimeZones(Composite parent) {
        createLabel(parent, "Time Zone:");
        timeZonesCombo = new ComboViewer(parent, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
        timeZonesCombo.setContentProvider(new ArrayContentProvider());
        timeZonesCombo.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                TimeZone timeZone = (TimeZone) element;
                return timeZone.getID();
            }
        });
        timeZonesCombo.setInput(getTimeZones());
        setTimeZone(TimeZone.getDefault());
    }

    private void setTimeZone(TimeZone timeZone) {
        ISelection selection = new StructuredSelection(timeZone);
        timeZonesCombo.setSelection(selection);
    }

    private ArrayList<TimeZone> getTimeZones() {
        ArrayList<TimeZone> timeZoneList = new ArrayList<TimeZone>();
        for (String id : TimeZone.getAvailableIDs()) {
            timeZoneList.add(TimeZone.getTimeZone(id));
        }
        return timeZoneList;
    }

    private void createLabel(Composite container, String text) {
        Label label = new Label(container, SWT.NONE);
        label.setText(text);
    }

    private void createTime(Composite client) {
        createLabel(client, "Time:");
        time = new DateTime(client, SWT.TIME);
    }

    private void createCalendar(Composite client) {
        createLabel(client, "Date:");
        calendar = new DateTime(client, SWT.CALENDAR);
    }

    @Override
    protected void clearEditorValue() {
        setEditorValue(Calendar.getInstance());
    }

    @Override
    protected Object getEditorValue() {
        Calendar calendarValue = Calendar.getInstance();
        calendarValue.set(Calendar.YEAR, calendar.getYear());
        calendarValue.set(Calendar.MONTH, calendar.getMonth());
        calendarValue.set(Calendar.DAY_OF_MONTH, calendar.getDay());
        calendarValue.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendarValue.set(Calendar.MINUTE, time.getMinutes());
        calendarValue.set(Calendar.SECOND, time.getSeconds());
        calendarValue.set(Calendar.MILLISECOND, 0);
        calendarValue.setTimeZone(getTimeZone());
        return calendarValue;
    }

    private TimeZone getTimeZone() {
        IStructuredSelection selection = (IStructuredSelection) timeZonesCombo.getSelection();
        TimeZone timeZone = (TimeZone) selection.iterator().next();
        return timeZone;
    }

    @Override
    protected void setEditorValue(Object value) {
        Calendar calendarValue = (Calendar) value;
        calendar.setDate(calendarValue.get(Calendar.YEAR), calendarValue.get(Calendar.MONTH), calendarValue.get(Calendar.DAY_OF_MONTH));
        int hourOfDay = calendarValue.get(Calendar.HOUR_OF_DAY);
        int minutes = calendarValue.get(Calendar.MINUTE);
        int seconds = calendarValue.get(Calendar.SECOND);
        time.setTime(hourOfDay, minutes, seconds);
        setTimeZone(calendarValue.getTimeZone());
    }
}
