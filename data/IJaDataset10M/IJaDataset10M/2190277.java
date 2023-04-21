package ipa.abilities.smix.webservice.pojos;

import java.io.IOException;
import javax.activation.DataHandler;
import javax.jbi.messaging.MessagingException;
import org.jdom.JDOMException;

/**
 * Interface for the simple abilities webservice 
 *
 * @author gmk-sischkaj
 */
public interface AbilitiesWSInterface {

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
    public boolean createSendAbMessage(String msgName, Double msgID, String msgCorrelationID, String processID, String state, String user, String password, String accessMethod, String asynchResponse, String reconciliation, String negotiation, DataHandler[] attachements, DataHandler xmlBinary) throws IOException, JDOMException, MessagingException;
}
