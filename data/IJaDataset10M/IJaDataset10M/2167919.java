package riafswing.helper;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.apache.log4j.Logger;
import riaf.controller.RiafMgr;
import riafswing.RComponent;

public class RFocusListener implements FocusListener {

    /**
	 * a static logger-object.
	 */
    private static final Logger logger = Logger.getLogger(RFocusListener.class);

    /**
	 * The component.
	 */
    private final RComponent component;

    /**
	 * Creates a new {@link FocusLostContentUpdater} for the given component.
	 * 
	 * @param owner
	 *            the component which this updater updates whenever the focus of
	 *            the component to which it is attached is lost. Usually that is
	 *            the implementation of the given component
	 */
    public RFocusListener(RComponent owner) {
        component = owner;
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (RiafMgr.global().isDebug()) {
            logger.info("Focus Lost: " + component.toString());
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (RiafMgr.global().isDebug()) {
            logger.info("Focus Gained: " + component.toString());
        }
    }
}
