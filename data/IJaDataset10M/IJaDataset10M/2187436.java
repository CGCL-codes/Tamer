package com.google.gwt.maps.sample.hellomaps.client;

import com.google.gwt.maps.client.MapPane;
import com.google.gwt.maps.client.MapPaneType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * This demo shows how to create a custom overlay in the form of a Rectangle
 * and add it to the map.
 */
public class CustomOverlayDemo extends MapsDemo {

    private static class Rectangle extends AbsolutePanel {

        public Rectangle() {
            super(DOM.createDiv());
            DOM.setStyleAttribute(getElement(), "borderStyle", "solid");
        }

        public void setBorderColor(String color) {
            DOM.setStyleAttribute(getElement(), "borderColor", color);
        }

        public void setBorderWidth(String width) {
            DOM.setStyleAttribute(getElement(), "borderWidth", width);
        }
    }

    private static class RectangleOverlay extends Overlay {

        private final LatLngBounds bounds;

        private final Rectangle rectangle;

        private final int weight;

        private MapWidget map;

        private MapPane pane;

        public RectangleOverlay(LatLngBounds bounds, int weight) {
            this.bounds = bounds;
            this.weight = weight;
            rectangle = new Rectangle();
            rectangle.setBorderWidth(weight + "px");
            rectangle.setBorderColor("#888888");
        }

        @Override
        protected Overlay copy() {
            return new RectangleOverlay(bounds, weight);
        }

        @Override
        protected void initialize(MapWidget map) {
            this.map = map;
            pane = map.getPane(MapPaneType.MAP_PANE);
            pane.add(rectangle);
        }

        @Override
        protected void redraw(boolean force) {
            if (!force) {
                return;
            }
            Point sw = map.convertLatLngToDivPixel(bounds.getSouthWest());
            Point ne = map.convertLatLngToDivPixel(bounds.getNorthEast());
            pane.setWidgetPosition(rectangle, Math.min(sw.getX(), ne.getX()), Math.min(sw.getY(), ne.getY()));
            int width = Math.abs(ne.getX() - sw.getX()) - weight;
            int height = Math.abs(ne.getY() - sw.getY()) - weight;
            rectangle.setSize(width + "px", height + "px");
        }

        @Override
        protected void remove() {
            rectangle.removeFromParent();
        }
    }

    private static HTML descHTML = null;

    private static final String descString = "<p>Creates a 500 x 300 pixel map viewport centered on Palo Alto, CA USA.</p>" + "<p>Draws a rectangle overlay at the center of the maps window.</p>\n" + "<p>Equivalent to the Maps JavaScript API Example: " + "<a href=\"http://code.google.com/apis/maps/documentation/examples/overlay-custom.html\">" + "http://code.google.com/apis/maps/documentation/examples/overlay-custom.html</a></p>\n";

    public static MapsDemoInfo init() {
        return new MapsDemoInfo() {

            @Override
            public MapsDemo createInstance() {
                return new CustomOverlayDemo();
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
                return "Custom Overlays";
            }
        };
    }

    private MapWidget map;

    private boolean firstTime = true;

    public CustomOverlayDemo() {
        map = new MapWidget(LatLng.newInstance(37.4419, -122.1419), 13);
        map.setSize("500px", "300px");
        map.setUIToDefault();
        initWidget(map);
    }

    @Override
    public void onShow() {
        if (firstTime) {
            firstTime = false;
            LatLngBounds bounds = map.getBounds();
            LatLng southWest = bounds.getSouthWest();
            LatLng northEast = bounds.getNorthEast();
            double lngDelta = (northEast.getLongitude() - southWest.getLongitude()) / 4;
            double latDelta = (northEast.getLatitude() - southWest.getLatitude()) / 4;
            LatLngBounds rectBounds = LatLngBounds.newInstance(LatLng.newInstance(southWest.getLatitude() + latDelta, southWest.getLongitude() + lngDelta), LatLng.newInstance(northEast.getLatitude() - latDelta, northEast.getLongitude() - lngDelta));
            map.addOverlay(new RectangleOverlay(rectBounds, 2));
        }
    }
}
