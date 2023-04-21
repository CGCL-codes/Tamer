package com.osgisamples.congress.frontend.client;

import java.util.ArrayList;
import java.util.Iterator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ShowRegistrantsComponent extends UpdateableComposite {

    private Panel composite = new VerticalPanel();

    private Label titleBar = new Label("Registrants");

    private Grid registrantsGrid;

    public ShowRegistrantsComponent() {
        initWidget(composite);
        composite.setStyleName("widget");
        titleBar.setStyleName("title");
        composite.add(titleBar);
        getRegistrantsFromServer();
        startUpdateTimer(1);
    }

    public void update() {
        getRegistrantsFromServer();
    }

    private void getRegistrantsFromServer() {
        CongressServiceAsync serviceproxy = (CongressServiceAsync) GWT.create(CongressService.class);
        ServiceDefTarget target = (ServiceDefTarget) serviceproxy;
        target.setServiceEntryPoint(GWT.getModuleBaseURL() + "registrants");
        AsyncCallback callback = new AsyncCallback() {

            public void onFailure(Throwable caught) {
                GWT.log("Error", caught);
            }

            public void onSuccess(Object result) {
                ArrayList registrants = (ArrayList) result;
                showRegistrantsComponent(registrants);
            }
        };
        serviceproxy.listRegistrants(callback);
    }

    private void showRegistrantsComponent(ArrayList registrants) {
        if (registrantsGrid != null) {
            composite.remove(registrantsGrid);
        }
        if (registrants != null && !registrants.isEmpty()) {
            registrantsGrid = new Grid(registrants.size() + 1, 2);
            registrantsGrid.addStyleName("grid");
            Label columnName = new Label("Name");
            columnName.setStyleName("header-column");
            Label columnRegNumber = new Label("Registrantion number");
            columnRegNumber.setStyleName("header-column");
            addRowToGrid(columnName, columnRegNumber, 0);
            int rowNum = 0;
            for (Iterator rows = registrants.iterator(); rows.hasNext(); ) {
                rowNum++;
                Registrant registrant = (Registrant) rows.next();
                Label labelName = new Label(registrant.getName());
                Label labelRegNumber = new Label(registrant.getRegistrationNumber());
                addRowToGrid(labelName, labelRegNumber, rowNum);
            }
        }
        composite.add(registrantsGrid);
    }

    private void addRowToGrid(Widget widgetName, Widget widgetNumber, int row) {
        registrantsGrid.setWidget(row, 0, widgetName);
        registrantsGrid.setWidget(row, 1, widgetNumber);
    }

    private void addRowToGrid(String name, String number, int row) {
        registrantsGrid.setText(row, 0, name);
        registrantsGrid.setText(row, 1, number);
    }
}
