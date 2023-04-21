package com.google.gwt.maps.sample.hellomaps.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.sample.hellomaps.client.MapsDemo.MapsDemoInfo;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Main class for implementing the HelloMaps gwt-google-apis demo.
 */
public class HelloMaps implements EntryPoint, ValueChangeHandler<String> {

    protected DemoList list = new DemoList();

    private MapsDemoInfo curInfo;

    private MapsDemo curMapsDemo;

    private HTML description = new HTML();

    private VerticalPanel innerPanel = new VerticalPanel();

    private FlexTable outerPanel = new FlexTable();

    public void onModuleLoad() {
        if (!Maps.isLoaded()) {
            Window.alert("The Maps API is not installed." + "  The <script> tag that loads the Maps API may be missing or your Maps key may be wrong.");
            return;
        }
        if (!Maps.isBrowserCompatible()) {
            Window.alert("The Maps API is not compatible with this browser.");
            return;
        }
        loadMapsDemos();
        innerPanel.setStylePrimaryName("hm-mapinnerpanel");
        HorizontalPanel horizPanel = new HorizontalPanel();
        list.setStylePrimaryName("hm-demolistbox");
        Button nextLink = new Button(">>");
        nextLink.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                show(list.getNext(), true);
            }
        });
        horizPanel.add(new Label("Select Demo: "));
        horizPanel.setSpacing(4);
        horizPanel.add(list);
        horizPanel.add(nextLink);
        innerPanel.add(horizPanel);
        innerPanel.add(description);
        innerPanel.setSpacing(10);
        History.addValueChangeHandler(this);
        outerPanel.setStylePrimaryName("hm-outerpanel");
        outerPanel.insertRow(0);
        outerPanel.insertRow(0);
        outerPanel.insertRow(0);
        outerPanel.insertRow(0);
        outerPanel.insertRow(0);
        outerPanel.addCell(0);
        outerPanel.addCell(1);
        outerPanel.addCell(2);
        outerPanel.addCell(3);
        outerPanel.addCell(4);
        outerPanel.setWidget(0, 0, new HTML("This Maps-enabled application was built using the Google " + "API Library for GWT, " + "<a href=\"http://code.google.com/p/gwt-google-apis/\">" + "http://code.google.com/p/gwt-google-apis/</a>. " + "The drop down list below allows you to select a scenario that " + "demonstrates a particular capability of the Maps support."));
        outerPanel.setWidget(1, 0, innerPanel);
        DecoratorPanel decorator = new DecoratorPanel();
        decorator.add(outerPanel);
        RootPanel.get("hm-map").add(decorator);
        String initToken = History.getToken();
        if (initToken.length() > 0) {
            onValueChange(initToken);
        } else {
            showInfo();
        }
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        onValueChange(token);
    }

    public void show(MapsDemoInfo info, boolean affectHistory) {
        if (info == curInfo) {
            return;
        }
        curInfo = info;
        if (curMapsDemo != null) {
            innerPanel.remove(curMapsDemo);
        }
        curMapsDemo = info.getInstance();
        list.setMapsDemoSelection(info.getName());
        if (affectHistory) {
            History.newItem(info.getName());
        }
        innerPanel.add(curMapsDemo);
        outerPanel.setWidget(2, 0, info.getDescriptionHTML());
        outerPanel.setWidget(3, 0, new HTML("These concepts behind these demos" + " are explained in the " + "<a href=\"http://www.google.com/apis/maps/documentation/index.html\">" + "Google Maps API Concepts</a> document."));
        String strippedClassName = info.getClass().getName();
        int lastIndex = strippedClassName.lastIndexOf('$');
        if (lastIndex > 0) {
            strippedClassName = strippedClassName.substring(0, lastIndex);
        }
        outerPanel.setWidget(4, 0, new HTML("<h5> See source in " + strippedClassName + "</h5><h5>Maps API version: " + Maps.getVersion() + "</h5>"));
        curMapsDemo.onShow();
    }

    /**
   * Adds all MapsDemos to the list. Note that this does not create actual
   * instances of all MapsDemos yet (they are created on-demand). This can make
   * a significant difference in startup time.
   */
    protected void loadMapsDemos() {
        list.addMapsDemo(SimpleDemo.init());
        list.addMapsDemo(AnimateDemo.init());
        list.addMapsDemo(DragCursorDemo.init());
        list.addMapsDemo(ControlsDemo.init());
        list.addMapsDemo(MapUIOptionsDemo.init());
        list.addMapsDemo(EventDemo.init());
        list.addMapsDemo(ClickDemo.init());
        list.addMapsDemo(MapEventDemo.init());
        list.addMapsDemo(InfoWindowDemo.init());
        list.addMapsDemo(OverlayDemo.init());
        list.addMapsDemo(MarkerInfoWindowDemo.init());
        list.addMapsDemo(IconDemo.init());
        list.addMapsDemo(IconClassDemo.init());
        list.addMapsDemo(DragMarkerDemo.init());
        list.addMapsDemo(CustomControlDemo.init());
        list.addMapsDemo(MapTypeDemo.init());
        list.addMapsDemo(CustomMapTypeDemo.init());
        list.addMapsDemo(GroundOverlayDemo.init());
        list.addMapsDemo(CustomOverlayDemo.init());
        list.addMapsDemo(DrawingOverlayDemo.init());
        list.addMapsDemo(GeoRssOverlayDemo.init());
        list.addMapsDemo(KmlOverlayDemo.init());
        list.addMapsDemo(TrafficOverlayDemo.init());
        list.addMapsDemo(SimpleDirectionsDemo.init());
        list.addMapsDemo(RoutedDirectionsDemo.init());
        list.addMapsDemo(GeocoderDemo.init());
        list.addMapsDemo(Geocoder2Demo.init());
        list.addMapsDemo(ReverseGeocoderDemo.init());
        list.addMapsDemo(EarthPluginDemo.init());
        list.addMapsDemo(GoogleBarDemo.init());
        list.addMapsDemo(AdsManagerDemo.init());
        list.addMapsDemo(StreetviewOverlayDemo.init());
        list.addMapsDemo(StreetviewDemo.init());
    }

    private void onValueChange(String token) {
        MapsDemoInfo info = list.find(token);
        if (info == null) {
            showInfo();
            return;
        }
        show(info, false);
    }

    private void showInfo() {
        show(list.find("The Basics"), false);
    }
}
