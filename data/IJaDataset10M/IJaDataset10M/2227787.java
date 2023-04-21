package org.itsnat.impl.core.event;

import org.itsnat.core.event.ItsNatEvent;
import org.itsnat.impl.core.doc.ItsNatAJAXDocumentImpl;
import org.itsnat.impl.core.ItsNatServletRequestImpl;
import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;

/**
 *
 * @author jmarranz
 */
public interface ItsNatEventInternal extends ItsNatEvent {

    public ItsNatServletRequestImpl getItsNatServletRequestImpl();

    public ItsNatAJAXDocumentImpl getItsNatAJAXDocument();

    public ClientAJAXDocumentImpl getClientAJAXDocument();
}
