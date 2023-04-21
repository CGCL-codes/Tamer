package org.objectstyle.cayenne.modeler.event;

import java.util.EventListener;

/**
 * Used to display DbAttribute.
 */
public interface DbAttributeDisplayListener extends EventListener {

    /** Displays specified db attribute. */
    public void currentDbAttributeChanged(AttributeDisplayEvent e);
}
