package wicket.contrib.gmap.event;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * See "infowindowopen" in the event section of <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMap2">GMap2</a>.
 */
public abstract class InfoWindowOpenListener extends GEventListenerBehavior {

    @Override
    protected String getEvent() {
        return "infowindowopen";
    }

    @Override
    protected void onEvent(AjaxRequestTarget target) {
        onInfoWindowOpen(target);
    }

    protected abstract void onInfoWindowOpen(AjaxRequestTarget target);
}
