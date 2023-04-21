package com.client;

import java.util.ArrayList;
import java.util.List;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GridExample extends LayoutContainer {

    private ColumnModel cm;

    private ListLoader<ListLoadResult<ModelData>> loader;

    private Grid<BeanModel> testGrid;

    private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FlowLayout(10));
        getAriaSupport().setPresentation(true);
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig();
        column.setId("name");
        column.setHeader("Name");
        column.setWidth(200);
        column.setRowHeader(true);
        configs.add(column);
        column = new ColumnConfig("lastUpdated", "lastUpdated", 100);
        column.setAlignment(HorizontalAlignment.RIGHT);
        column.setDateTimeFormat(DateTimeFormat.getFormat("dd/MM/yyyy HH:mm"));
        configs.add(column);
        cm = new ColumnModel(configs);
        ContentPanel cp = new ContentPanel();
        cp.setBodyBorder(true);
        cp.setHeading("Basic Grid");
        cp.setButtonAlign(HorizontalAlignment.CENTER);
        cp.setLayout(new FitLayout());
        cp.getHeader().setIconAltText("Grid Icon");
        cp.setSize(600, 300);
        testGrid = new Grid<BeanModel>(getDataStore(), cm);
        testGrid.setLoadMask(true);
        testGrid.getView().setEmptyText("no data");
        testGrid.getView().setForceFit(true);
        testGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        testGrid.setAutoHeight(true);
        testGrid.setAutoWidth(true);
        cp.add(testGrid);
        add(cp);
        Button button = new Button("refresh", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                loader.load();
            }
        });
        add(button);
    }

    private ListStore<BeanModel> getDataStore() {
        RpcProxy<List<TestDomain>> proxy = new RpcProxy<List<TestDomain>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<List<TestDomain>> callback) {
                greetingService.greetServer(callback);
            }
        };
        BeanModelReader reader = new BeanModelReader();
        loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);
        final ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
        loader.addLoadListener(new LoadListener() {

            @Override
            public void loaderBeforeLoad(LoadEvent le) {
                Window.alert("loader before load");
            }

            @Override
            public void loaderLoad(LoadEvent le) {
                Window.alert("loaderload");
                testGrid.getView().setEmptyText("No data!");
                testGrid.unmask();
            }

            @Override
            public void loaderLoadException(LoadEvent le) {
                Window.alert("loader exception");
                Window.alert("Exception occured:" + le.exception);
            }
        });
        loader.load();
        return store;
    }
}
