package au.org.emii.portal;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * Handy utilities to compose windows with - 
 * Currently, if you compose against this class, you get:
 *  + autowire variables
 *  + autoforward variables
 *  + nudge windows dragged offscreen back into the browser
 * @author geoff
 *
 */
public class UtilityComposer extends GenericAutowireAutoforwardComposer {

    private static final long serialVersionUID = 1L;

    /**
	 * Stop user dragging windows outsize viewport - if they 
	 * try to do this, nudge it back again!
	 * @param window
	 */
    public void onNudgeToView() {
        logger.debug("nudgeing window");
        String top = getTop();
        if ((!Validate.empty(top)) && (top.charAt(0) == '-')) {
            logger.debug("moving window from " + top + " to 0px");
            setTop("0px");
        }
    }

    /**
	 * autowire a default action for id=close
	 */
    public void onClick$close() {
        detach();
    }

    /**
	 * autowire a default action for id=hide
	 */
    public void onClick$hide() {
        setVisible(false);
    }

    public void doAfterCompose(Component component) throws Exception {
    }

    public void afterCompose() {
        super.afterCompose();
        logger.debug("registered UtilityComposer event listeners");
        addEventListener("onMove", new EventListener() {

            public void onEvent(Event event) throws Exception {
                onNudgeToView();
            }
        });
    }
}
