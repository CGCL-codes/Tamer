package org.itsnat.impl.comp;

import org.w3c.dom.events.Event;

/**
 *
 * @author jmarranz
 */
public interface ItsNatHTMLFormCompChangeBased {

    public void handleEventOnChange(Event evt);

    public void postHandleEventOnChange(Event evt);
}
