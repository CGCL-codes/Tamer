package com.knwebapp.gwt.googlemap.client.lib;

import com.google.gwt.maps.client.Copyright;
import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.MercatorProjection;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * To create an info window, call the openInfoWindow method, passing it a
 * location and a DOM element to display. The following example code displays an
 * info window anchored to the center of the map with a simple "Hello, world"
 * message.
 */
public class CustomMapTypeDemo extends MapsDemo {

    private static HTML descHTML = null;

    private static final String descString = "<h2>Custom MapType Demo</h2>\n" + "<p>Tests the MapType and TileLayer APIs</p>" + "<p>You should see a <i>MyMap</i> button on the upper right of " + "the map that shows crosses on a grey background when pressed. " + "</p>";

    public static MapsDemoInfo init() {
        return new MapsDemoInfo() {

            @Override
            public MapsDemo createInstance() {
                return new CustomMapTypeDemo();
            }

            @Override
            public HTML getDescriptionHTML() {
                if (descHTML == null) {
                    descHTML = new HTML(descString);
                }
                return descHTML;
            }

            @Override
            public String getName() {
                return "Creating a custom MapType";
            }
        };
    }

    private MapWidget map;

    public CustomMapTypeDemo() {
        VerticalPanel vertPanel = new VerticalPanel();
        vertPanel.setStyleName("hm-panel");
        map = new MapWidget(LatLng.newInstance(33.7814790, -84.3880580), 13);
        map.setSize("500px", "450px");
        CopyrightCollection myCopyright = new CopyrightCollection("");
        myCopyright.addCopyright(new Copyright(1, LatLngBounds.newInstance(LatLng.newInstance(34, -81), LatLng.newInstance(36, -79)), 10, ""));
        TileLayer tileLayer = new TileLayer(myCopyright, 10, 18) {

            @Override
            public double getOpacity() {
                return 1.0;
            }

            @Override
            public String getTileURL(Point tile, int zoomLevel) {
                return "http://www.google.com/apis/maps/documentation/examples/include/tile_crosshairs.png";
            }

            @Override
            public boolean isPng() {
                return true;
            }
        };
        MapType mapType = new MapType(new TileLayer[] { tileLayer }, new MercatorProjection(20), "MyMap");
        map.addMapType(mapType);
        map.addControl(new MapTypeControl());
        vertPanel.add(map);
        initWidget(vertPanel);
    }
}
