package com.cvo.scrumtoolkit.client.pannels;

import java.util.ArrayList;
import com.cvo.scrumtoolkit.client.controller.ControllerImpl;
import com.cvo.scrumtoolkit.client.controller.HistoryManager;
import com.cvo.scrumtoolkit.client.controller.View;
import com.cvo.scrumtoolkit.client.entities.LoadedProject;
import com.cvo.scrumtoolkit.client.entities.Project;
import com.cvo.scrumtoolkit.client.model.ProjectStore;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HomePanel extends Composite {

    private ControllerImpl controller;

    private ProjectStore projectStore;

    private static ProjectStorePanelUiBinder uiBinder = GWT.create(ProjectStorePanelUiBinder.class);

    private final PersistentServiceAsync persistentService = GWT.create(PersistentService.class);

    private String[] scrummaster = { "Selecteer", "Wim Aelvoet", "Yves Bosmans", "Tsjerk Raeymaekers", "Ricardo Cappelle", "John De Mars", "Jos De herdt" };

    MenuPanel mp = new MenuPanel();

    FlowPanel Flow = new FlowPanel();

    interface ProjectStorePanelUiBinder extends UiBinder<Widget, HomePanel> {
    }

    @UiField
    TextBox txtprojectname;

    @UiField
    ListBox lstscrummaster;

    @UiField
    Button btncreate;

    @UiHandler("btncreate")
    void onClick(ClickEvent e) {
        persistentService.persistProject(txtprojectname.getText(), lstscrummaster.getValue(lstscrummaster.getSelectedIndex()).toString(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                txtprojectname.setText("");
                System.out.println("Project is goed aangemaakt");
            }

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Project is niet aangemaakt");
            }
        });
        haalProjecten();
        RootPanel.get("menuContainer").clear();
        RootPanel.get("menuContainer").add(mp);
    }

    @UiField
    VerticalPanel verticalPanel;

    public HomePanel() {
        initWidget(uiBinder.createAndBindUi(this));
        verticalPanel.add(Flow);
        Flow.setStyleName("flow");
        for (int i = 0; i < scrummaster.length; i++) {
            lstscrummaster.addItem(scrummaster[i]);
        }
        haalProjecten();
    }

    public void haalProjecten() {
        Flow.clear();
        persistentService.searchProjects(new AsyncCallback<ArrayList<Project>>() {

            @Override
            public void onFailure(Throwable caught) {
                System.out.println("Projecten konden niet geladen worden");
            }

            @Override
            public void onSuccess(ArrayList<Project> result) {
                for (final Project project : result) {
                    final Button projectButton = new Button(project.getProjectname());
                    Flow.add(projectButton);
                    projectButton.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            try {
                                String string = "projectbacklog/" + project.getProjectname() + "/0";
                                System.out.println(string);
                                HistoryManager.gotoUrl(string);
                                LoadedProject.getLoadedProject().setProjectId(project.getId().toString());
                            } catch (Exception ex) {
                            }
                        }
                    });
                }
            }
        });
    }
}
