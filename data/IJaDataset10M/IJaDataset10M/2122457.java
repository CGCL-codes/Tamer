package guitarscales.options.models;

import java.util.EventObject;

/**
 * This event is passed if something has changed to the OptionsModel. The OptionsModel itself
 * must be retrieved using <code>OptionsModel.getInstance()</code> and cannot be asked to this Event.
 * @author Wim Deblauwe
 */
public class OptionsEvent extends EventObject {

    /**
	 * Constructor
	 * @param source the source that sends the event
	 */
    public OptionsEvent(Object source) {
        super(source);
    }
}
