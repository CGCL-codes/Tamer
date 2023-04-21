package com.anasoft.os.daofusion.sample.hellodao.client;

import com.anasoft.os.daofusion.sample.hellodao.client.smartgwt.CustomerGrid;
import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point to our sample application.
 */
public class HelloDAO implements EntryPoint {

    public static final String RPC_SERVICE_RELATIVE_PATH = "../../rpc";

    private static final String ICON_PATH = "../js-gwt/hellodao/icons/";

    public void onModuleLoad() {
        Canvas screen = new Canvas();
        screen.setMargin(10);
        screen.setWidth100();
        screen.setHeight100();
        VLayout layout = new VLayout(10);
        layout.setWidth100();
        layout.setHeight100();
        final CustomerGrid customerGrid = new CustomerGrid();
        customerGrid.setHeight100();
        layout.addMember(customerGrid);
        HLayout customerButtons = new HLayout(10);
        customerButtons.setTop(10);
        layout.addMember(customerButtons);
        IButton refresh = new IButton("Refresh");
        refresh.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                customerGrid.fetchData();
            }
        });
        refresh.setIcon(ICON_PATH + "refresh.png");
        refresh.setWidth100();
        customerButtons.addMember(refresh);
        IButton cleanRefresh = new IButton("Clean refresh");
        cleanRefresh.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                customerGrid.invalidateCache();
                customerGrid.fetchData();
            }
        });
        cleanRefresh.setIcon(ICON_PATH + "refresh.png");
        cleanRefresh.setWidth100();
        customerButtons.addMember(cleanRefresh);
        IButton newEntry = new IButton("New entry");
        newEntry.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                customerGrid.startEditingNew();
            }
        });
        newEntry.setIcon(ICON_PATH + "new.png");
        newEntry.setWidth100();
        customerButtons.addMember(newEntry);
        IButton saveChanges = new IButton("Save changes");
        saveChanges.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                customerGrid.saveAllEdits();
            }
        });
        saveChanges.setIcon(ICON_PATH + "save.png");
        saveChanges.setWidth100();
        customerButtons.addMember(saveChanges);
        IButton deleteSelected = new IButton("Delete selected");
        deleteSelected.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                customerGrid.removeSelectedData();
            }
        });
        deleteSelected.setIcon(ICON_PATH + "delete.png");
        deleteSelected.setWidth100();
        customerButtons.addMember(deleteSelected);
        screen.addChild(layout);
        screen.draw();
    }
}
