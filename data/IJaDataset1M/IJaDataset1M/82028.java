package gu.client.view;

import java.util.ArrayList;
import gu.client.dbService;
import gu.client.dbServiceAsync;
import gu.client.dao.ObjectFactory;
import gu.client.dao.ObjectFactoryListener;
import gu.client.ui.GoogleMap;
import gu.client.ui.LoadingPanel;
import gu.client.ui.RoundedPanel;
import gu.client.view.treeitems.BaseTreeItem;
import gu.client.view.treeitems.ShippersTreeItem;
import gu.client.view.treeitems.ConsigneesTreeItem;
import gu.client.view.treeitems.StoriesTreeItem;
import gu.client.view.treeitems.UsersTreeItem;
import gu.client.view.treeitems.WordersTreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapDoubleClickHandler;
import com.google.gwt.maps.client.event.MapDoubleClickHandler.MapDoubleClickEvent;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel;

public class CopyOfUfosView extends Composite implements ObjectFactoryListener {

    private static final int HeaderRowIndex = 0;

    int rowIndex = 1;

    private static String[][] tt_srv = null;

    private static String[][] tt_clt = null;

    private final dbServiceAsync srv = GWT.create(dbService.class);

    final ArrayList<Marker> ar = new ArrayList<Marker>();

    final ArrayList<Integer> aritab = new ArrayList<Integer>();

    MapWidget map = new MapWidget(LatLng.newInstance(44, -77), 3);

    VerticalPanel dp1 = new VerticalPanel();

    VerticalPanel dp2 = new VerticalPanel();

    TabPanel tabPanel = new TabPanel();

    private VerticalPanel mainPanel = new VerticalPanel();

    private HorizontalPanel hp = new HorizontalPanel();

    private VerticalPanel vp1 = new VerticalPanel();

    private VerticalPanel gap1 = new VerticalPanel();

    private VerticalPanel gap2 = new VerticalPanel();

    HTML info = new HTML();

    private ObjectFactory objectFactory;

    private Label loadingLabel = new Label("loading...");

    private LoadingPanel loading = new LoadingPanel(new Label("loading..."));

    final Geocoder geo = new Geocoder();

    final LatLng gde = LatLng.newInstance(44, -77);

    final Button but_map = new Button("Map");

    final Button but_LoadBoard = new Button("Load Board");

    final Button but_database = new Button("Database");

    final Button but_submit = new Button("Search");

    final Button but_reload = new Button("Reload");

    final FlexTable flexTable = new FlexTable();

    final ListBox km = new ListBox(false);

    final ListBox drop_box_equip = new ListBox(false);

    final FlexTable layout = new FlexTable();

    final TextBox tbox1 = new TextBox();

    final TextBox tbox2 = new TextBox();

    @SuppressWarnings("deprecation")
    public CopyOfUfosView() {
        initWidget(mainPanel);
        setStyleName("databaseEditorView");
        map = new MapWidget(gde, 3);
        map.setSize("600px", "400px");
        map.setScrollWheelZoomEnabled(true);
        map.addControl(new LargeMapControl());
        map.addMapClickHandler(h);
        dp1.clear();
        dp1.add(map);
        tabPanel = new TabPanel();
        tabPanel.add(dp2, "Board");
        tabPanel.add(dp1, "Map");
        tabPanel.selectTab(0);
        hp.add(tabPanel);
        mainPanel.add(hp);
        RootPanel.get().add(loading);
        RootPanel.get().add(new HTML("<br>&nbsp;&nbsp;test@quicklydone.com | <b><a href=\".\">Logout</a></b><br><br>"));
    }

    final MapClickHandler h = new MapClickHandler() {

        public void onClick(MapClickEvent event) {
            info.removeFromParent();
            if (event.getLatLng() != null) info = new HTML(String.valueOf(event.getLatLng()));
            if (event.getOverlayLatLng() != null) info = new HTML(String.valueOf(event.getOverlayLatLng()));
            dp1.add(info);
        }
    };

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
        objectFactory.setListener(this);
    }

    public void onError(String error) {
        PopupPanel popup = new PopupPanel(true);
        popup.setStyleName("error");
        popup.setWidget(new HTML(error));
        popup.show();
        popup.center();
    }

    public void onLoadingFinish() {
        loading.loadingEnd();
    }

    public void onLoadingStart() {
        loading.loadingBegin();
    }

    @Override
    public void onRefresh() {
    }

    public void onSigned(String logout) {
        onLoadingStart();
        srv.getData("all", new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                onError(caught.toString());
            }

            public void onSuccess(String[][] r2) {
                ArrayList<String[]> ar2 = new ArrayList<String[]>();
                for (int j = 0; j < r2.length; j++) {
                    String[] ccn = new String[] { r2[j][4], r2[j][5], r2[j][6], r2[j][7], "", "", "", "", r2[j][8], r2[j][9], r2[j][10], r2[j][11], r2[j][12], r2[j][13], r2[j][14], r2[j][15], r2[j][16], r2[j][17], r2[j][18], r2[j][19], r2[j][20], r2[j][21] };
                    if (!r2[j][5].equals("0")) ar2.add(ccn);
                    String[] ccn2 = new String[] { r2[j][0], r2[j][1], r2[j][2], r2[j][3], "", "", "", "", r2[j][8], r2[j][9], r2[j][10], r2[j][11], r2[j][12], r2[j][13], r2[j][14], r2[j][15], r2[j][16], r2[j][17], r2[j][18], r2[j][19], r2[j][20], r2[j][21] };
                    if (!r2[j][5].equals("0")) ar2.add(ccn2);
                }
                String[][] r = new String[2 * r2.length][22];
                for (int n = 0; n < ar2.size(); n++) {
                    r[n] = ar2.get(n);
                }
                set_Map_Markers(r);
                onLoadingFinish();
            }
        });
    }

    public void set_Map_Markers(String[][] r) {
        try {
            for (int i = 0; i < r.length; i++) {
                final String color = r[i][0];
                double dlat = -1;
                try {
                    dlat = Double.parseDouble(r[i][1]);
                } catch (Exception e) {
                }
                double dlng = -1;
                try {
                    dlng = Double.parseDouble(r[i][2]);
                } catch (Exception e) {
                }
                final String s1 = "WO#: " + r[i][8];
                final Icon icon = Icon.newInstance(Icon.DEFAULT_ICON);
                if (color.equals("b")) icon.setImageURL("blue.png"); else if (color.equals("g")) icon.setImageURL("markerGreen.png"); else icon.setImageURL("marker.png");
                MarkerOptions ops = MarkerOptions.newInstance(icon);
                ops.setIcon(icon);
                final Marker mm = new Marker(LatLng.newInstance(dlat, dlng), ops);
                mm.addMarkerClickHandler(new MarkerClickHandler() {

                    public void onClick(MarkerClickEvent event) {
                        map.getInfoWindow().open(mm, new InfoWindowContent(s1));
                    }
                });
                ar.add(mm);
                aritab.add(i);
                map.addOverlay(mm);
            }
        } catch (Exception eee) {
            onError(eee.toString());
        }
    }

    void prepLayout() {
        tbox1.setText("Toronto");
        km.addItem("all");
        km.addItem("10 km");
        km.addItem("20 km");
        km.addItem("50 km");
        km.addItem("100 km");
        km.addItem("200 km");
        layout.setWidget(0, 0, new HTML(" "));
        layout.setCellSpacing(7);
        layout.setWidget(0, 0, new Label("Origin "));
        layout.setWidget(0, 1, tbox1);
        layout.setWidget(1, 0, new Label("Radius"));
        layout.setWidget(1, 1, km);
        layout.setWidget(2, 1, but_submit);
    }

    private void prep_FlexTable() {
        flexTable.insertRow(HeaderRowIndex);
        flexTable.getRowFormatter().addStyleName(HeaderRowIndex, "FlexTable-Header");
        addColumn("WO#");
        addColumn("From:");
        addColumn("Origin");
        addColumn("To:");
        addColumn("Destination");
        addColumn("Equipmqnt");
        addColumn("Pieces");
        addColumn("Type");
        addColumn("Description");
        addColumn("Weight:");
        addColumn("lbs");
        addColumn("kgs");
        addColumn("Pickup");
        addColumn("Delivery");
    }

    public void set_data_for_flexTable() {
        try {
            tt_clt = new String[tt_srv.length][tt_srv[0].length - 8];
            for (int row = 0; row < tt_srv.length; row++) {
                for (int col = 0; col < tt_srv[row].length - 8; col++) {
                    tt_clt[row][col] = tt_srv[row][col + 8];
                }
                if (aritab.contains(row)) addRow(tt_clt[row++]);
            }
            applyDataRowStyles();
            flexTable.setCellSpacing(0);
            flexTable.addStyleName("FlexTable");
        } catch (Exception aa) {
            onError("getData *** " + aa.toString());
        }
    }

    private void addColumn(Object columnHeading) {
        Widget widget = createCellWidget(columnHeading);
        int cell = flexTable.getCellCount(HeaderRowIndex);
        widget.setWidth("100%");
        widget.addStyleName("FlexTable-ColumnLabel");
        flexTable.setWidget(HeaderRowIndex, cell, widget);
        flexTable.getCellFormatter().addStyleName(HeaderRowIndex, cell, "FlexTable-ColumnLabelCell");
    }

    private Widget createCellWidget(Object cellObject) {
        Widget widget = null;
        if (cellObject instanceof Widget) widget = (Widget) cellObject; else widget = new Label(cellObject.toString());
        return widget;
    }

    private void addRow(Object[] cellObjects) {
        for (int cell = 0; cell < cellObjects.length; cell++) {
            Widget widget = createCellWidget(cellObjects[cell]);
            flexTable.setWidget(rowIndex, cell, widget);
            flexTable.getCellFormatter().addStyleName(rowIndex, cell, "FlexTable-Cell");
        }
        rowIndex++;
    }

    private void applyDataRowStyles() {
        HTMLTable.RowFormatter rf = flexTable.getRowFormatter();
        for (int row = 1; row < flexTable.getRowCount(); ++row) {
            if ((row % 2) != 0) {
                rf.addStyleName(row, "FlexTable-OddRow");
            } else {
                rf.addStyleName(row, "FlexTable-EvenRow");
            }
        }
    }

    void get_Data() {
        srv.getData("all", new AsyncCallback<String[][]>() {

            public void onFailure(Throwable caught) {
                onError(caught.toString());
            }

            public void onSuccess(String[][] r2) {
                ArrayList<String[]> ar2 = new ArrayList<String[]>();
                for (int j = 0; j < r2.length; j++) {
                    String[] ccn = new String[] { r2[j][4], r2[j][5], r2[j][6], r2[j][7], "", "", "", "", r2[j][8], r2[j][9], r2[j][10], r2[j][11], r2[j][12], r2[j][13], r2[j][14], r2[j][15], r2[j][16], r2[j][17], r2[j][18], r2[j][19], r2[j][20], r2[j][21] };
                    if (!r2[j][5].equals("0")) ar2.add(ccn);
                    String[] ccn2 = new String[] { r2[j][0], r2[j][1], r2[j][2], r2[j][3], "", "", "", "", r2[j][8], r2[j][9], r2[j][10], r2[j][11], r2[j][12], r2[j][13], r2[j][14], r2[j][15], r2[j][16], r2[j][17], r2[j][18], r2[j][19], r2[j][20], r2[j][21] };
                    if (!r2[j][5].equals("0")) ar2.add(ccn2);
                }
                String[][] r = new String[2 * r2.length][22];
                for (int n = 0; n < ar2.size(); n++) {
                    r[n] = ar2.get(n);
                }
                tt_srv = r;
                set_Map_Markers(r);
            }
        });
        but_submit.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                get_Data();
            }
        });
    }
}
