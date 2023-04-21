package wicket.contrib.gmap.event;

import org.apache.wicket.ajax.AjaxRequestTarget;
import wicket.contrib.gmap.GMap2;

/**
 * See "movestart" in the event section of <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMap2">GMap2</a>.
 */
public abstract class MoveStartListener extends GEventListenerBehavior {

    @Override
    protected String getEvent() {
        return "movestart";
    }

    @Override
    protected void onEvent(AjaxRequestTarget target) {
        onMoveStart(target);
    }

    /**
	 * Override this method to provide handling of a move.<br>
	 * You can get the new center coordinates of the map by calling
	 * {@link GMap2#getCenter()}.
	 * 
	 * @param target
	 *            the target that initiated the move
	 */
    protected abstract void onMoveStart(AjaxRequestTarget target);
}
