package edu.it.contingency.context;

import edu.it.contingency.ContingencyEvent;

/**
 * @author bardram
 *
 */
public class DisplayResource extends AbstractResource {

    public static final int DISPLAY_NONE = 0;

    public static final int DISPLAY_AVAILABLE = 1;

    public DisplayResource() {
        super();
    }

    public void contingency(ContingencyEvent event) {
    }
}
