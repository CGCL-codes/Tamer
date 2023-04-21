package org.itsnat.impl.core.mut;

import org.itsnat.impl.core.doc.*;
import org.itsnat.impl.core.doc.ItsNatXMLDocumentImpl;
import org.w3c.dom.events.MutationEvent;

/**
 *
 * @author jmarranz
 */
public class MutationEventListenerXMLImpl extends MutationEventListenerImpl {

    public MutationEventListenerXMLImpl(ItsNatXMLDocumentImpl itsNatDoc) {
        super(itsNatDoc);
    }

    protected void checkOperation(MutationEvent mutEvent) {
    }

    protected void renderAndSendJSCode(final MutationEvent mutEvent) {
    }
}
