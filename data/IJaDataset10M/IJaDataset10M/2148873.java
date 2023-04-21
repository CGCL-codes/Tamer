package org.itsnat.impl.core.listener;

import org.itsnat.core.SyncMode;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserWebKit;
import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;
import org.itsnat.impl.core.doc.ItsNatAJAXDocumentImpl;
import org.itsnat.impl.core.util.HasUniqueId;
import org.itsnat.impl.core.util.UniqueId;
import org.itsnat.impl.core.util.UniqueIdGenerator;

/**
 *
 * @author jmarranz
 */
public abstract class EventListenerWrapperImpl implements HasUniqueId {

    protected UniqueId idObj;

    /** Creates a new instance of EventListenerWrapperImpl */
    public EventListenerWrapperImpl(UniqueIdGenerator generator) {
        this.idObj = new UniqueId(generator, "el");
    }

    protected abstract int getSyncModeDeclared();

    public int getSyncMode(ClientAJAXDocumentImpl clientDoc) {
        int sync = getSyncModeDeclared();
        return clientDoc.fixSyncMode(sync);
    }

    public abstract long getAJAXTimeout();

    public abstract ItsNatAJAXDocumentImpl getItsNatAJAXDocument();

    public String getId() {
        return idObj.getId();
    }

    public UniqueId getUniqueId() {
        return idObj;
    }
}
