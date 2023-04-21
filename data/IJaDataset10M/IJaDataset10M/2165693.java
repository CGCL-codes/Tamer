package ipa.abilities.smix.webservice.pojos;

import ipa.abilities.smix.common.xml.ESBMessageJDOM;
import java.io.IOException;
import javax.jbi.messaging.InOnly;
import javax.jbi.messaging.NormalizedMessage;
import javax.jbi.component.ComponentContext;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import javax.activation.DataHandler;
import javax.jbi.messaging.MessagingException;
import org.jdom.JDOMException;

/**
 * A simple (in-out) webservice that tries to build a xml document with the 
 * given parameters. Validates it against the ESBMessage XML Schema, and, if
 * valid, sends it to the specified JBI endpoint. (targetService is used here...)
 *
 * @author gmk-sischkaj
 */
public class AbilitiesWS implements AbilitiesWSInterface {

    private ESBMessageJDOM abMessageJDOM = null;

    private ComponentContext ctx;

    private String xmlSchemaURL;

    private String serviceNamespace;

    private String serviceName;

    /**
     * Creates an ESBMessage with the given parameters, validates it against the
     * schema and than sends it. 
     *
     * @param msgName The message name [min/max-occurs: 0/1; restr.-base: xsd:string; min/max-length: 4/256]
     * @param msgID The message ID [min/max-occurs: 0/1; restr.-base: xsd:decimal]
     * @param msgCorrelationID The message correlation ID [min/max-occurs: 0/1; restr.-base: xsd:string; min/max-length: 0/256]
     * @param processID The process ID [min/max-occurs: 0/1; restr.-base: xsd:string; min/max-length: 0/256]
     * @param state The current state of this message [restr.-base: xsd:string]
     * @param user The user (sender) [min-occurs: 0; restr.-base: xsd:string; max-length: 128]
     * @param password The user's password [min-occurs: 0; restr.-base: xsd:string]
     * @param accessMethod The access methods supported [restr.-base: xsd:string; enum: {clear, MD5, XMLSignature}]
     * @param asynchResponse Asynchronous Response [type="xsd:anyURI"]
     * @param reconciliation Reconciliation flag [min-occurs: 0; type="xsd:string"]
     * @param negotiation Negotiation flag [min-occurs: 0; type="xsd:string"]
     * @param attachements The attachments to be sent [restr.-base="xsd:base64Binary"]
     * @param xmlBinary The actual UBL document to embedd as the body of the ESBMessage
     * @throws java.io.IOException 
     * @throws org.jdom.JDOMException 
     * @throws javax.jbi.messaging.MessagingException 
     * @return (boolean) True if the message was valid and has been sent.
     */
    public boolean createSendAbMessage(String msgName, Double msgID, String msgCorrelationID, String processID, String state, String user, String password, String accessMethod, String asynchResponse, String reconciliation, String negotiation, DataHandler[] attachements, DataHandler xmlBinary) throws IOException, JDOMException, MessagingException {
        abMessageJDOM = new ESBMessageJDOM(msgName, msgID, msgCorrelationID, processID, state, user, password, accessMethod, asynchResponse, reconciliation, negotiation, attachements, xmlBinary);
        if (abMessageJDOM.isValid(xmlSchemaURL)) {
            return (transportViaDevChannel(abMessageJDOM));
        }
        return false;
    }

    private boolean transportViaDevChannel(Object objToSend) throws MessagingException {
        InOnly exchange = ctx.getDeliveryChannel().createExchangeFactory().createInOnlyExchange();
        NormalizedMessage message = exchange.createMessage();
        if (objToSend instanceof ESBMessageJDOM) {
            message.setContent(new StreamSource(new StringReader(((ESBMessageJDOM) objToSend).toString())));
            if (((ESBMessageJDOM) objToSend).getAttachements() != null) {
                for (int i = 0; i < ((ESBMessageJDOM) objToSend).getAttachements().length; i++) {
                    message.addAttachment("attachement_" + i, ((ESBMessageJDOM) objToSend).getAttachements()[i]);
                }
            }
        }
        exchange.setService(new QName(serviceNamespace, serviceName));
        exchange.setInMessage(message);
        return (ctx.getDeliveryChannel().sendSync(exchange));
    }

    /**
     * The JBI context.
     * @param context The JBI context. Is set automatically by servicemix!
     */
    public void setContext(ComponentContext context) {
        System.out.println("abilitiesWS.setContext()");
        this.ctx = context;
    }

    /**
     * The path to the ESBMessage XSD File
     * @param xmlSchemaURL The path to the ESBMessage XSD File
     */
    public void setXmlSchemaURL(String xmlSchemaURL) {
        System.out.println("abilitiesWS.setXmlSchemaURL()");
        this.xmlSchemaURL = xmlSchemaURL;
    }

    /**
     * The (target) service namespace (e.g. "urn:ipa:abilities")
     * @param serviceNamespace The (target) service namespace (e.g. "urn:ipa:abilities")
     */
    public void setServiceNamespace(String serviceNamespace) {
        System.out.println("abilitiesWS.setServiceNamespace()");
        this.serviceNamespace = serviceNamespace;
    }

    /**
     * The (target) service name (e.g. "myTrace")
     * @param serviceName The (target) service name (e.g. "myTrace")
     */
    public void setServiceName(String serviceName) {
        System.out.println("abilitiesWS.setServiceName()");
        this.serviceName = serviceName;
    }
}
