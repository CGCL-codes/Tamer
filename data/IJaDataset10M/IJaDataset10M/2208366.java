package net.sf.mxlosgi.core.impl;

import net.sf.mxlosgi.core.XmppConnection;
import net.sf.mxlosgi.core.filter.StanzaFilter;
import net.sf.mxlosgi.core.listener.StanzaReceListener;
import net.sf.mxlosgi.xmpp.XmlStanza;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 * @author noah
 *
 */
public class StanzaReceListenerServiceTracker extends ServiceTracker {

    public StanzaReceListenerServiceTracker(BundleContext context) {
        super(context, StanzaReceListener.class.getName(), null);
    }

    public void fireStanzaReceListener(XmppConnection connection, XmlStanza stanza) {
        ServiceReference[] references = getServiceReferences();
        if (references == null) {
            return;
        }
        for (ServiceReference reference : references) {
            StanzaFilter filter = (StanzaFilter) reference.getProperty("filter");
            if (filter == null || filter.accept(connection, stanza)) {
                StanzaReceListener listener = (StanzaReceListener) getService(reference);
                listener.processReceStanza(connection, stanza);
            }
        }
    }
}
