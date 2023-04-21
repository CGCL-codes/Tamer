package bm.core.mvc;

/**
 * A Controller handles user interaction and notifies and changes views.
 *
 * @author <a href="mailto:narciso@elondra.com">Narciso Cerezo</a>
 * @version $Revision:4 $
 */
public interface Controller {

    /**
     * Handle an event.
     *
     * @param event event to handle
     */
    void handle(ControllerEvent event);

    /**
     * When a view is released.
     * @param view view released
     */
    void onRelease(View view);
}
