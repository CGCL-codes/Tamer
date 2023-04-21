package org.timoconsult.costcontrol.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CostControl implements EntryPoint, ClickHandler {

    private HorizontalPanel mainPanel;

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        CostControlConstants constants = GWT.create(CostControlConstants.class);
        mainPanel = new HorizontalPanel();
        mainPanel.setSpacing(50);
        StackPanel panel = new StackPanel();
        panel.setWidth("200px");
        Grid projectGrid = new Grid(3, 1);
        Button addBtn = new Button(constants.addProject());
        addBtn.ensureDebugId("addBtn");
        addBtn.setWidth("150px");
        addBtn.addClickHandler(this);
        projectGrid.setWidget(0, 0, addBtn);
        Button editBtn = new Button(constants.editProject());
        editBtn.setTitle("editBtn");
        editBtn.setWidth("150px");
        editBtn.addClickHandler(this);
        projectGrid.setWidget(1, 0, editBtn);
        Button listBtn = new Button(constants.listProjects());
        listBtn.setTitle("listBtn");
        listBtn.setWidth("150px");
        listBtn.addClickHandler(this);
        projectGrid.setWidget(2, 0, listBtn);
        panel.add(projectGrid, constants.project());
        mainPanel.add(panel);
        RootPanel.get().add(mainPanel);
    }

    @Override
    public void onClick(ClickEvent event) {
        CostControlConstants constants = GWT.create(CostControlConstants.class);
        Button btn = (Button) event.getSource();
        String text = btn.getText();
        if (btn.getText().equals(constants.addProject())) {
            if (mainPanel.getWidgetCount() > 1) mainPanel.remove(1);
            ProjectPanel projectPanel = new ProjectPanel();
            mainPanel.add(projectPanel);
        } else if (btn.getText().equals(constants.editProject())) {
            if (mainPanel.getWidgetCount() > 1) mainPanel.remove(1);
        } else if (btn.getText().equals(constants.listProjects())) {
            if (mainPanel.getWidgetCount() > 1) mainPanel.remove(1);
            ProjectListPanel projectListPanel = new ProjectListPanel();
            mainPanel.add(projectListPanel);
        }
    }
}
