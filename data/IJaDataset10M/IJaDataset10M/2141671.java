package org.atricore.idbus.capabilities.sso.main.binding;

import oasis.names.tc.saml._2_0.protocol.RequestAbstractType;
import oasis.names.tc.saml._2_0.wsdl.SAMLRequestPortType;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.MessageContentsList;
import org.atricore.idbus.capabilities.sso.support.SAMLR2MessagingConstants;
import org.atricore.idbus.capabilities.sso.support.binding.SSOBinding;
import org.atricore.idbus.kernel.main.federation.metadata.EndpointDescriptor;
import org.atricore.idbus.kernel.main.mediation.*;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.AbstractMediationSoapBinding;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.CamelMediationExchange;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.CamelMediationMessage;
import org.atricore.idbus.kernel.main.mediation.state.LocalState;
import org.atricore.idbus.kernel.main.mediation.state.ProviderStateContext;
import javax.xml.ws.Service;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This endpoint can only consume, producing to this endpoint is unsupported.
 *
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class SamlR2SoapBinding extends AbstractMediationSoapBinding {

    private static final Log logger = LogFactory.getLog(SamlR2SoapBinding.class);

    public SamlR2SoapBinding(Channel channel) {
        super(SSOBinding.SAMLR2_SOAP.getValue(), channel);
    }

    public MediationMessage createMessage(CamelMediationMessage message) {
        CamelMediationExchange samlR2exchange = message.getExchange();
        Exchange exchange = samlR2exchange.getExchange();
        logger.debug("Create Message Body from exchange " + exchange.getClass().getName());
        Message in = exchange.getIn();
        if (in.getBody() instanceof MessageContentsList) {
            MessageContentsList mclIn = (MessageContentsList) in.getBody();
            logger.debug("Using CXF Message Content : " + mclIn.get(0));
            MediationMessage body;
            LocalState lState = null;
            MediationState state = null;
            if (mclIn.get(0) instanceof RequestAbstractType) {
                RequestAbstractType samlReq = (RequestAbstractType) mclIn.get(0);
                try {
                    Method getSessionIndex = samlReq.getClass().getMethod("getSessionIndex");
                    List<String> sessionIndexes = (List<String>) getSessionIndex.invoke(samlReq);
                    if (sessionIndexes != null) {
                        if (sessionIndexes.size() > 0) {
                            String sessionIndex = sessionIndexes.get(0);
                            ProviderStateContext ctx = createProviderStateContext();
                            int retryCount = getRetryCount();
                            if (retryCount > 0) {
                                lState = ctx.retrieve("idpSsoSessionId", sessionIndex, retryCount, getRetryDelay());
                            } else {
                                lState = ctx.retrieve("idpSsoSessionId", sessionIndex);
                            }
                            if (logger.isDebugEnabled()) logger.debug("Local state was" + (lState == null ? " NOT" : "") + " retrieved for ssoSessionId " + sessionIndex);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    if (logger.isTraceEnabled()) logger.trace("SAML Request does not have session index : " + e.getMessage());
                } catch (InvocationTargetException e) {
                    logger.error("Cannot recover local state : " + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    logger.error("Cannot recover local state : " + e.getMessage(), e);
                }
            }
            if (lState == null) {
                state = createMediationState(exchange);
            } else {
                state = new MediationStateImpl(lState);
            }
            body = new MediationMessageImpl(in.getMessageId(), mclIn.get(0), null, null, null, state);
            return body;
        } else {
            throw new IllegalArgumentException("Unknown message type " + in.getBody());
        }
    }

    @Override
    public Object sendMessage(MediationMessage message) throws IdentityMediationException {
        if (logger.isTraceEnabled()) logger.trace("Sending new SAML 2.0 message using SOAP Binding");
        EndpointDescriptor endpoint = message.getDestination();
        String soapEndpoint = endpoint.getLocation();
        Service service = Service.create(SAMLR2MessagingConstants.SERVICE_NAME);
        service.addPort(SAMLR2MessagingConstants.PORT_NAME, javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING, endpoint.getLocation());
        Object content = message.getContent();
        if (!(content instanceof RequestAbstractType)) {
            throw new IdentityMediationException("Unsupported content " + content);
        }
        String soapMethodName = content.getClass().getSimpleName();
        soapMethodName = "saml" + soapMethodName.substring(0, soapMethodName.length() - 4);
        if (logger.isTraceEnabled()) logger.trace("Using soap method [" + soapMethodName + "]");
        SAMLRequestPortType port = service.getPort(SAMLR2MessagingConstants.PORT_NAME, SAMLRequestPortType.class);
        if (logger.isTraceEnabled()) logger.trace("Sending SSO SOAP Request: " + content);
        try {
            Method soapMethod = port.getClass().getMethod(soapMethodName, content.getClass());
            Object o = soapMethod.invoke(port, content);
            if (logger.isTraceEnabled()) logger.trace("Received SSO SOAP Response: " + o);
            return o;
        } catch (NoSuchMethodException e) {
            throw new IdentityMediationException("SOAP Method not impelmented " + soapMethodName + ": " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IdentityMediationException("SOAP error: " + e.getMessage(), e);
        }
    }
}
