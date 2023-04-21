package com.timoconsult.OpenProjectControl.client;

import java.util.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;

public class HoursDlg extends DialogBox {

    public HoursDlg() {
        setText("Add Hours");
        draw();
    }

    public void draw() {
        VerticalPanel panel = new VerticalPanel();
        Grid addHour = new Grid(gridSize + 2, 8);
        addHour.setStyleName("grid");
        addHour.setText(0, 0, "Employee");
        addHour.setWidget(0, 1, Employee);
        addHour.setText(0, 2, "Week Ending Month");
        WeekEndingMonth.addChangeListener(new ValidationListener(ValidationListener.INT));
        addHour.setWidget(0, 3, WeekEndingMonth);
        addHour.setText(0, 4, "Day");
        WeekEndingDay.addChangeListener(new ValidationListener(ValidationListener.INT));
        addHour.setWidget(0, 5, WeekEndingDay);
        addHour.setText(0, 6, "Year");
        WeekEndingYear.addChangeListener(new ValidationListener(ValidationListener.INT));
        addHour.setWidget(0, 7, WeekEndingYear);
        addHour.setText(1, 0, "Job #");
        addHour.setText(1, 1, "CSI");
        addHour.setText(1, 2, "Hours");
        addHour.setText(1, 3, "Rate");
        for (int i = 0; i < gridSize; i++) {
            ListBox job = new ListBox();
            Project.populateProjectsList(job);
            job.addChangeListener(new projectListChangeListener(i));
            projectLists.add(job);
            ListBox budget = new ListBox();
            budgetLists.add(budget);
            BudgetItem.populateBudgetItemList(budget, null);
            TextBox hours = new TextBox();
            hours.addChangeListener(new ValidationListener(ValidationListener.DOUBLE));
            hourList.add(hours);
            TextBox rate = new TextBox();
            rate.addChangeListener(new ValidationListener(ValidationListener.DOUBLE));
            rateList.add(rate);
            addHour.setWidget(i + 2, 0, job);
            addHour.setWidget(i + 2, 1, budget);
            addHour.setWidget(i + 2, 2, hours);
            addHour.setWidget(i + 2, 3, rate);
        }
        panel.add(addHour);
        HorizontalPanel btnPanel = new HorizontalPanel();
        Button addHours = new Button("Add Hours");
        addHours.addClickListener(new AddHourListener());
        btnPanel.add(addHours);
        Button close = new Button("Cancel");
        close.addClickListener(new ClickListener() {

            public void onClick(Widget w) {
                hide();
            }
        });
        btnPanel.add(close);
        panel.add(btnPanel);
        setWidget(panel);
    }

    private class AddHourListener implements ClickListener {

        public void onClick(Widget w) {
            try {
                if (Employee.getText().equals("") || WeekEndingDay.getText().equals("") || WeekEndingMonth.getText().equals("") || WeekEndingYear.getText().equals("")) {
                    Window.alert("Please enter employee and ending week!");
                    return;
                }
                for (int i = 0; i < gridSize; i++) {
                    if (((TextBox) hourList.get(i)).getText().equals("") || ((TextBox) rateList.get(i)).getText().equals("")) {
                        break;
                    }
                    HourItem hour = new HourItem();
                    hour.setName(Employee.getText());
                    hour.setAmount(Double.parseDouble(((TextBox) hourList.get(i)).getText()));
                    hour.setRate(Double.parseDouble(((TextBox) rateList.get(i)).getText()));
                    ListBox budget = (ListBox) budgetLists.get(i);
                    int selIndex = budget.getSelectedIndex();
                    hour.setBudgetId(Integer.parseInt(budget.getValue(selIndex)));
                    Date week = new Date();
                    week.setDate(Integer.parseInt(WeekEndingDay.getText()));
                    week.setMonth(Integer.parseInt(WeekEndingMonth.getText()) - 1);
                    week.setYear(Integer.parseInt(WeekEndingYear.getText()) - 1900);
                    hour.setWeek(week);
                    ProjectServiceAsync projectService = (ProjectServiceAsync) GWT.create(ProjectService.class);
                    ServiceDefTarget endpoint = (ServiceDefTarget) projectService;
                    String moduleRelativeURL = GWT.getModuleBaseURL() + "/project";
                    endpoint.setServiceEntryPoint(moduleRelativeURL);
                    AsyncCallback callback = new AsyncCallback() {

                        public void onSuccess(Object result) {
                            draw();
                            ProjectList.get().draw();
                            ProjectData.get().draw();
                        }

                        public void onFailure(Throwable caught) {
                            Window.alert("Could not add the budget item to " + "the server. Please contact your system administrator");
                        }
                    };
                    projectService.addHour(hour, callback);
                }
                hide();
            } catch (Exception ex) {
                Window.alert(ex.getMessage());
            }
        }
    }

    private class projectListChangeListener implements ChangeListener {

        public projectListChangeListener(int i) {
            index = i;
        }

        public void onChange(Widget w) {
            ListBox projectList = (ListBox) w;
            int selItem = projectList.getSelectedIndex();
            BudgetItem.populateBudgetItemList((ListBox) budgetLists.get(index), projectList.getValue(selItem));
        }

        int index;
    }

    Grid hourGrid;

    TextBox Employee = new TextBox();

    TextBox WeekEndingMonth = new TextBox();

    TextBox WeekEndingDay = new TextBox();

    TextBox WeekEndingYear = new TextBox();

    ArrayList projectLists = new ArrayList();

    ArrayList budgetLists = new ArrayList();

    ArrayList hourList = new ArrayList();

    ArrayList rateList = new ArrayList();

    String projectID;

    private static final int gridSize = 15;
}
