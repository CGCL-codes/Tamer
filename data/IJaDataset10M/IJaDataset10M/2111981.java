package wicketrocks.gmaps;

import org.apache.wicket.markup.html.panel.Panel;
import wicket.google.maps.MapContainer;

/**
 * @author manuelbarzi
 * @version 20111201180314 
 */
public class SimpleMapPanel extends Panel {

    public SimpleMapPanel(String id) {
        super(id);
        MapContainer simpleMap = new MapContainer("simpleMap", "AIzaSyBzmS3xm4X1HwCHrr-c24fZ2Ksozy5Fraw", Boolean.TRUE);
        simpleMap.initDefaults();
        add(simpleMap);
    }
}
