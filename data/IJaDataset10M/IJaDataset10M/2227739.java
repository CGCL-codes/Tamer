package org.xebra.client.events;

import java.awt.Component;
import java.util.EventObject;

/**
 * An event that is spawned when a tool has been activated, either by the toolbar
 * or through popup menus.  Unlike the {@link org.xebra.client.events.ToolSelectionEvent}
 * class, this class refers to those tools which are not controlled by toggle switches
 * (eg: Invert, Rotate, and Flip);
 * 
 * @author Rafael Chargel
 * @version $Revision: 1.2 $
 */
public class ToolActivatedEvent extends EventObject {

    private static final long serialVersionUID = -9006891526681296654L;

    public enum Tool {

        INVERT_IMAGE, ROTATE_CW, ROTATE_CCW, FLIP_VERTICAL, FLIP_HORIZONTAL, FIT_TO_PANEL, ACTUAL_SIZE, EXIT_APP, STUDY_INFO, SERIES_INFO, OVERLAY_TOGGLE, PANEL_SELECT, SCROLL_SELECT
    }

    private Object value;

    private Tool toolActivated;

    /**
	 * Constructor for the </code>ToolActivatedEvent</code> class.
	 * 
	 * @param src
	 *        The component that initiated the event.
	 *        
	 * @param toolActivated
	 *        The tool that was activated.
	 */
    public ToolActivatedEvent(Component src, Tool toolActivated) {
        super(src);
        this.toolActivated = toolActivated;
    }

    /**
	 * Gets the tool that this event encapsulates.
	 * 
	 * @return Returns the tool that this event encapsulates.
	 */
    public Tool getToolActivated() {
        return this.toolActivated;
    }

    /**
	 * Sets a value for this event. The interpretation of this value differs
	 * from one tool to another. Not used by all tools.
	 * 
	 * @param value 
	 *        The event value.
	 */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
	 * Gets the value for this event. The interpretation of this value differs
	 * from one tool to another. Not used by all tools.
	 * 
	 * @return Returns the value for this event, may be null.
	 */
    public Object getValue() {
        return this.value;
    }
}
