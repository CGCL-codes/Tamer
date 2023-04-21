package org.modss.facilitator.ui.result;

import java.util.*;

/**
 * An event indicating that selection of alternatives changed.
 * Currently does not contain information about which alternatives, because we
 * don't use that information anyway.
 *
 * @author John Farrell
 * @version $Revision: 1.1 $
 */
public class AlternativeSelectionEvent extends EventObject {

    public AlternativeSelectionEvent(Object source) {
        super(source);
    }
}
