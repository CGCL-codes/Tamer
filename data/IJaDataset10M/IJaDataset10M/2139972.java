package wicket.contrib.gmap.event;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import wicket.contrib.gmap.api.GLatLng;
import wicket.contrib.gmap.api.GOverlay;

/**
 * See "click" in the event section of <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMap2">GMap2</a>.
 */
public abstract class ClickListener extends GEventListenerBehavior {

    @Override
    protected String getEvent() {
        return "click";
    }

    @Override
    protected void onEvent(AjaxRequestTarget target) {
        Request request = RequestCycle.get().getRequest();
        GOverlay overlay = null;
        GLatLng latLng = null;
        String markerParameter = request.getParameter("argument0");
        if (markerParameter != null) {
            for (GOverlay ovl : getGMap2().getOverlays()) {
                if (ovl.getId().equals(markerParameter)) {
                    overlay = ovl;
                    break;
                }
            }
        }
        String latLngParameter = request.getParameter("argument1");
        if (latLngParameter != null) {
            latLng = GLatLng.parse(latLngParameter);
        }
        onClick(target, latLng, overlay);
    }

    /**
	 * Override this method to provide handling of a click on the map. See the
	 * event section of <a
	 * href="http://www.google.com/apis/maps/documentation/reference.html#GMap2">GMap2</a>.
	 * 
	 * @param latLng
	 *            The clicked GLatLng. Might be null if a Marker was clicked.
	 * @param overlay
	 *            The clicked overlay. Might be null.
	 * @param target
	 *            The target that initiated the click.
	 */
    protected abstract void onClick(AjaxRequestTarget target, GLatLng latLng, GOverlay overlay);
}
