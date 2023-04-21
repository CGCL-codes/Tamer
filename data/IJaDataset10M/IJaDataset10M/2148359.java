package de.dgrid.bisgrid.secure.proxy.workflow.util;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.codehaus.xfire.XFireException;
import org.codehaus.xfire.exchange.AbstractMessage;
import org.codehaus.xfire.soap.SoapConstants;
import org.codehaus.xfire.transport.DefaultTransportManager;
import org.codehaus.xfire.transport.Transport;
import org.codehaus.xfire.util.dom.DOMInHandler;
import org.codehaus.xfire.util.dom.DOMOutHandler;

public class MessageProcessingUtils {

    public static Logger log = Logger.getLogger(MessageProcessingUtils.class);

    public static void copyMessageContent(AbstractMessage from, AbstractMessage to) {
        to.setAttachments(from.getAttachments());
        to.setHeader(from.getHeader());
        to.setSoapVersion(from.getSoapVersion());
        to.setBody(from.getBody());
        to.setHeader(from.getHeader());
        to.setProperty(DOMOutHandler.DOM_MESSAGE, from.getProperty(DOMInHandler.DOM_MESSAGE));
        to.setProperty(SoapConstants.SOAP_ACTION, from.getProperty(SoapConstants.SOAP_ACTION));
        to.setEncoding((from.getEncoding() != null) ? from.getEncoding() : "utf-8");
    }

    @SuppressWarnings("unchecked")
    public static Transport getTransport(String uri) throws XFireException {
        DefaultTransportManager tm = new DefaultTransportManager();
        tm.initialize();
        Collection<Transport> col = tm.getTransportsForUri(uri);
        if (col.isEmpty()) throw new XFireException("cannot find transport for uri: " + uri);
        Transport transport = null;
        for (Transport t : col) {
            log.debug("found transport for URI: " + uri + " " + t.toString());
            if (transport == null) transport = t;
        }
        return transport;
    }
}
