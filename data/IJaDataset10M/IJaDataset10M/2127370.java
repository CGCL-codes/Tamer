package wicket.contrib.gmap.api;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import wicket.contrib.gmap.js.Constructor;

/**
 * Represents an Google Maps API's <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMarker">GMarker</a>.
 */
public class GMarker extends GOverlay {

    private static final long serialVersionUID = 1L;

    private GLatLng latLng;

    private final GMarkerOptions options;

    /**
	 * @param gLatLng
	 *            the point on the map where this marker will be anchored
	 */
    public GMarker(GLatLng gLatLng) {
        this(gLatLng, null);
    }

    public GMarker(GLatLng gLatLng, GMarkerOptions options) {
        super();
        this.latLng = gLatLng;
        this.options = options;
    }

    public GLatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(GLatLng gLatLng) {
        this.latLng = gLatLng;
    }

    public GMarkerOptions getMarkerOptions() {
        return this.options;
    }

    @Override
    protected String getJSconstructor() {
        Constructor constructor = new Constructor("GMarker").add(latLng.getJSconstructor());
        if (options != null) {
            constructor.add(options.getJSconstructor());
        }
        return constructor.toJS();
    }

    @Override
    protected void updateOnAjaxCall(AjaxRequestTarget target, GEvent overlayEvent) {
        Request request = RequestCycle.get().getRequest();
        this.latLng = GLatLng.parse(request.getParameter("overlay.latLng"));
    }
}
