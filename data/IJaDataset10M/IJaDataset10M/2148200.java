package findgoshow.client;

import java.util.ArrayList;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.event.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.maps.client.InfoWindowContent;

/**
 *
 * @author dotevo
 */
public class Place {

    public int id;

    public String name;

    Marker marker;

    public RoutesLGoogle routes;

    public MapWidget map;

    public ArrayList<Transport> transports = new ArrayList<Transport>();

    public ArrayList<Place> childrens = new ArrayList<Place>();

    public Place(int id, String name, LatLng loc, final MapWidget map, final RoutesLGoogle routes) {
        this.routes = routes;
        this.id = id;
        this.name = name;
        this.map = map;
        transports.add(new TransportCar(this));
        MarkerOptions options = MarkerOptions.newInstance();
        options.setDraggable(true);
        marker = new Marker(loc, options);
        map.addOverlay(marker);
        marker.addMarkerDragEndHandler(new MarkerDragEndHandler() {

            public void onDragEnd(MarkerDragEndEvent event) {
                draw();
                int index = routes.miejsca.indexOf(getMe());
                if (index + 1 < routes.miejsca.size()) routes.miejsca.get(index + 1).draw();
            }
        });
        marker.addMarkerClickHandler(new MarkerClickHandler() {

            public void onClick(MarkerClickEvent event) {
                Panel panel = new FlowPanel();
                Button button = new Button("Usuń Waypoint");
                button.addClickListener(new ClickListener() {

                    public void onClick(Widget sender) {
                        map.removeOverlay(marker);
                        routes.delPlace(getMe());
                    }
                });
                panel.add(button);
                map.getInfoWindow().open(marker.getLatLng(), new InfoWindowContent(panel));
            }
        });
    }

    public void draw() {
        int index = routes.miejsca.indexOf(this);
        if (index > 0) {
            Place before = routes.miejsca.get(index - 1);
            transports.get(0).draw(before.marker.getLatLng());
        }
    }

    void clear() {
        for (int i = 0; i < transports.size(); i++) {
            transports.get(i).clear();
        }
    }

    Place getMe() {
        return this;
    }
}
