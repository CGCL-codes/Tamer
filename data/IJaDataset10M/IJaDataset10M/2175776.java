package com.timoconsult.OpenProjectControl.client;

import java.util.ArrayList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;

public class AddNoteDlg extends DialogBox {

    public AddNoteDlg(int t, String p, int i) {
        type = t;
        projectId = p;
        id = i;
        name = new TextBox();
        note = new TextArea();
        Ok = new Button("Ok");
        Cancel = new Button("Cancel");
        date = new TextBox();
        date.setVisible(false);
        setText("Add a note");
        draw();
    }

    public AddNoteDlg(int noteid) {
        name = new TextBox();
        note = new TextArea();
        name.setEnabled(false);
        note.setEnabled(false);
        Ok = new Button("Ok");
        Ok.setVisible(false);
        Cancel = new Button("Cancel");
        date = new TextBox();
        date.setEnabled(false);
        ProjectServiceAsync projectService = (ProjectServiceAsync) GWT.create(ProjectService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) projectService;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "/project";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        AsyncCallback callback = new AsyncCallback() {

            public void onSuccess(Object result) {
                Note n = (Note) result;
                name.setText(n.name());
                note.setText(n.note());
                date.setText(DateTimeFormat.getShortDateFormat().format(n.date()));
            }

            public void onFailure(Throwable caught) {
                Window.alert("Could not receive note from " + "the server. Please contact your system administrator");
            }
        };
        projectService.getNote(noteid, callback);
        setText("Note");
        draw();
    }

    private void draw() {
        VerticalPanel panel = new VerticalPanel();
        setWidget(panel);
        panel.add(name);
        panel.add(date);
        note.setHeight("200px");
        panel.add(note);
        HorizontalPanel btnPanel = new HorizontalPanel();
        Ok.addClickListener(new ClickListener() {

            public void onClick(Widget w) {
                ProjectServiceAsync projectService = (ProjectServiceAsync) GWT.create(ProjectService.class);
                ServiceDefTarget endpoint = (ServiceDefTarget) projectService;
                String moduleRelativeURL = GWT.getModuleBaseURL() + "/project";
                endpoint.setServiceEntryPoint(moduleRelativeURL);
                AsyncCallback callback = new AsyncCallback() {

                    public void onSuccess(Object result) {
                        hide();
                        draw();
                        ProjectList.get().draw();
                        ProjectData.get().draw();
                    }

                    public void onFailure(Throwable caught) {
                        Window.alert("Could not add the budget item to " + "the server. Please contact your system administrator");
                    }
                };
                Note noteObj = new Note();
                noteObj.setName(name.getText());
                noteObj.setNote(note.getText());
                projectService.addNote(noteObj, type, projectId, id, callback);
            }
        });
        btnPanel.add(Ok);
        Cancel.addClickListener(new ClickListener() {

            public void onClick(Widget w) {
                hide();
            }
        });
        btnPanel.add(Cancel);
        panel.add(btnPanel);
    }

    private int type;

    private String projectId = null;

    private int id = -1;

    TextBox name;

    TextBox date;

    TextArea note;

    Button Ok;

    Button Cancel;
}
