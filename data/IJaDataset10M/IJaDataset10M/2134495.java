package org.freem.love.client.formfields;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import org.freem.love.client.i18n.Months;
import java.util.Date;

public class DateWidget extends Composite {

    private ListBox day;

    private ListBox month;

    private ListBox year;

    public DateWidget() {
        FlowPanel panel = new FlowPanel();
        day = new ListBox(false);
        day.addItem("число", "0");
        for (int i = 0; i < 31; i++) {
            String value = Integer.toString(i + 1);
            day.addItem(value, value);
        }
        String[] months = ((Months) GWT.create(Months.class)).months();
        month = new ListBox(false);
        month.addItem("месяц", "0");
        for (int i = 0; i < 12; i++) {
            month.addItem(months[i], Integer.toString(i + 1));
        }
        year = new ListBox(false);
        year.addItem("год", "0");
        for (int i = 0; i < 63; i++) {
            String value = Integer.toString((new Date()).getYear() - 18 + 1900 - i);
            year.addItem(value, value);
        }
        panel.add(day);
        panel.add(month);
        panel.add(year);
        initWidget(panel);
    }

    public ListBox getDay() {
        return day;
    }

    public void setDay(ListBox day) {
        this.day = day;
    }

    public ListBox getMonth() {
        return month;
    }

    public void setMonth(ListBox month) {
        this.month = month;
    }

    public ListBox getYear() {
        return year;
    }

    public void setYear(ListBox year) {
        this.year = year;
    }

    public void setDate(Date date) {
        day.setSelectedIndex(date.getDate());
        year.setSelectedIndex((new Date()).getYear() - 18 - date.getYear() + 1);
        month.setSelectedIndex(date.getMonth());
    }

    public Date getDate() {
        return new Date(Integer.parseInt(year.getValue(year.getSelectedIndex())) - 1900, Integer.parseInt(month.getValue(month.getSelectedIndex())) - 1, Integer.parseInt(day.getValue(day.getSelectedIndex())) - 1);
    }
}
