package com.timoconsult.OpenProjectControl.client;

import java.util.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;

public class ActivityDlg extends DialogBox {

    public ActivityDlg(Project p) {
        project = p;
        setText("Activity");
        draw();
    }

    public void draw() {
        VerticalPanel panel = new VerticalPanel();
        ScrollPanel scroll = new ScrollPanel();
        scroll.setStyleName("ScrollSize");
        activityGrid = new Grid(5, 5);
        Activity.setGridCaption(activityGrid);
        Activity.populateBudgetItemGrid(activityGrid, project.id());
        scroll.add(activityGrid);
        panel.add(scroll);
        Grid addActivity = new Grid(4, 8);
        addActivity.setStyleName("grid");
        Button deleteActivity = new Button("Delete Selected Activity Items");
        deleteActivity.addClickListener(new ClickListener() {

            public void onClick(Widget w) {
                Activity.deleteActivities(activityGrid, ActivityDlg.this);
            }
        });
        addActivity.setWidget(0, 0, deleteActivity);
        addActivity.setText(1, 0, "Name");
        addActivity.setText(1, 1, "Start: Month");
        addActivity.setText(1, 2, "Start: Day");
        addActivity.setText(1, 3, "Start: Year");
        addActivity.setText(1, 4, "End: Month");
        addActivity.setText(1, 5, "End: Day");
        addActivity.setText(1, 6, "End: Year");
        addActivity.setText(1, 7, "BudgetItem");
        addActivity.setWidget(2, 0, name);
        startMonth.addChangeListener(new ValidationListener(ValidationListener.INT));
        addActivity.setWidget(2, 1, startMonth);
        startDay.addChangeListener(new ValidationListener(ValidationListener.INT));
        addActivity.setWidget(2, 2, startDay);
        startYear.addChangeListener(new ValidationListener(ValidationListener.INT));
        addActivity.setWidget(2, 3, startYear);
        endMonth.addChangeListener(new ValidationListener(ValidationListener.INT));
        addActivity.setWidget(2, 4, endMonth);
        endDay.addChangeListener(new ValidationListener(ValidationListener.INT));
        addActivity.setWidget(2, 5, endDay);
        endYear.addChangeListener(new ValidationListener(ValidationListener.INT));
        addActivity.setWidget(2, 6, endYear);
        BudgetItem.populateBudgetItemList(budgets, project.id());
        addActivity.setWidget(2, 7, budgets);
        Button addActivityItem = new Button("Add Activity Item");
        addActivityItem.addClickListener(new AddActivityItemListener());
        addActivity.setWidget(3, 4, addActivityItem);
        panel.add(addActivity);
        Button close = new Button("Close Activity Panel");
        close.addClickListener(new ClickListener() {

            public void onClick(Widget w) {
                hide();
            }
        });
        panel.add(close);
        setWidget(panel);
    }

    private class AddActivityItemListener implements ClickListener {

        public void onClick(Widget w) {
            Activity activity = new Activity();
            activity.setName(name.getText());
            Date start = new Date();
            int startM = Integer.parseInt(startMonth.getText()) - 1;
            int startD = Integer.parseInt(startDay.getText());
            int startY = Integer.parseInt(startYear.getText()) - 1900;
            start.setMonth(startM);
            start.setDate(startD);
            start.setYear(startY);
            activity.setStart(start);
            Date end = new Date();
            end.setMonth(Integer.parseInt(endMonth.getText()) - 1);
            end.setDate(Integer.parseInt(endDay.getText()));
            end.setYear(Integer.parseInt(endYear.getText()) - 1900);
            activity.setEnd(end);
            activity.setBudgetItemID(Integer.parseInt(budgets.getValue(budgets.getSelectedIndex())));
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
            projectService.addActivity(activity, callback);
        }
    }

    Grid activityGrid;

    TextBox name = new TextBox();

    TextBox startMonth = new TextBox();

    TextBox startDay = new TextBox();

    TextBox startYear = new TextBox();

    TextBox endMonth = new TextBox();

    TextBox endDay = new TextBox();

    TextBox endYear = new TextBox();

    ListBox budgets = new ListBox();

    Project project;
}
