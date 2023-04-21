package org.atricore.idbus.capabilities.josso.main.binding;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.capabilities.sso.support.core.util.XmlUtils;
import org.atricore.idbus.kernel.main.federation.metadata.EndpointDescriptor;
import org.atricore.idbus.kernel.main.mediation.Channel;
import org.atricore.idbus.kernel.main.mediation.MediationMessage;
import org.atricore.idbus.kernel.main.mediation.MediationMessageImpl;
import org.atricore.idbus.kernel.main.mediation.MediationState;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.AbstractMediationHttpBinding;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.CamelMediationMessage;
import org.w3._1999.xhtml.Html;
import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class JossoHttpRedirectBinding extends AbstractMediationHttpBinding {

    private static final Log logger = LogFactory.getLog(JossoHttpRedirectBinding.class);

    protected JossoHttpRedirectBinding(Channel channel) {
        super(JossoBinding.JOSSO_REDIRECT.getValue(), channel);
    }

    public MediationMessage createMessage(CamelMediationMessage message) {
        Exchange exchange = message.getExchange().getExchange();
        logger.debug("Create Message Body from exchange " + exchange.getClass().getName());
        Message httpMsg = exchange.getIn();
        if (httpMsg.getHeader("http.requestMethod") == null) {
            if (logger.isDebugEnabled()) {
                Map<String, Object> h = httpMsg.getHeaders();
                for (String key : h.keySet()) {
                    logger.debug("CAMEL Header:" + key + ":" + h.get(key));
                }
            }
            throw new IllegalArgumentException("Unknown message, no valid HTTP Method header found!");
        }
        MediationState state = createMediationState(exchange);
        String relayState = state.getTransientVariable("RelayState");
        return new MediationMessageImpl(message.getMessageId(), null, null, relayState, null, state);
    }

    public void copyMessageToExchange(CamelMediationMessage josso11Out, Exchange exchange) {
        MediationMessage out = josso11Out.getMessage();
        EndpointDescriptor ed = out.getDestination();
        assert ed != null : "Mediation Response MUST Provide a destination";
        if (out.getContent() != null) throw new IllegalStateException("Content not supported for JOSSO HTTP Redirect bidning");
        if (logger.isDebugEnabled()) logger.debug("Creating HTML Redirect to " + ed.getLocation());
        String jossoQryString = "";
        if (out.getRelayState() != null) {
            jossoQryString += "?relayState=" + out.getRelayState();
        }
        Message httpOut = exchange.getOut();
        Message httpIn = exchange.getIn();
        String jossoRedirLocation = this.buildHttpTargetLocation(httpIn, ed) + jossoQryString;
        if (logger.isDebugEnabled()) logger.debug("Redirecting to " + jossoRedirLocation);
        try {
            copyBackState(out.getState(), exchange);
            if (!isEnableAjax()) {
                httpOut.getHeaders().put("Cache-Control", "no-cache, no-store");
                httpOut.getHeaders().put("Pragma", "no-cache");
                httpOut.getHeaders().put("http.responseCode", 302);
                httpOut.getHeaders().put("Content-Type", "text/html");
                httpOut.getHeaders().put("Location", jossoRedirLocation);
            } else {
                Html redir = this.createHtmlRedirectMessage(jossoRedirLocation);
                String marshalledHttpResponseBody = XmlUtils.marshal(redir, "http://www.w3.org/1999/xhtml", "html", new String[] { "org.w3._1999.xhtml" });
                marshalledHttpResponseBody = marshalledHttpResponseBody.replace("&amp;josso_", "&josso_");
                httpOut.getHeaders().put("Cache-Control", "no-cache, no-store");
                httpOut.getHeaders().put("Pragma", "no-cache");
                httpOut.getHeaders().put("http.responseCode", 200);
                httpOut.getHeaders().put("Content-Type", "text/html");
                ByteArrayInputStream baos = new ByteArrayInputStream(marshalledHttpResponseBody.getBytes());
                httpOut.setBody(baos);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
