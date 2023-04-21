package com.vangent.hieos.services.xds.registry.transactions.hl7v2;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author Bernie Thuman
 */
public class HL7ADTPatientAddMessageHandler extends HL7ADTPatientIdentityFeedMessageHandler {

    private static final Logger log = Logger.getLogger(HL7ADTPatientAddMessageHandler.class);

    /**
     * 
     * @param props
     */
    public HL7ADTPatientAddMessageHandler(HL7ServerProperties props) {
        super(props);
    }

    /**
     * Process the inbound HL7v2 message and return an ACK.
     *
     * @param inMessage The inbound HL7v2 message.
     * @param socket The socket that accepted the inbound connection.
     * @return The outbound message (ACK).
     * @throws ApplicationException
     * @throws HL7Exception
     */
    public Message handleMessage(Message inMessage, Socket socket) throws ApplicationException, HL7Exception {
        log.info("++++ PATIENT ADD ++++");
        try {
            Terser terser = new Terser(inMessage);
            String messageType = terser.get("/.MSH-9-2");
            String patientId = terser.get("/.PID-3-1");
            String assigningAuthorityUniversalId = terser.get("/PID-3-4-2");
            String messageControlId = terser.get("/.MSH-10");
            String sendingApplication = terser.get("/.MSH-3");
            String sendingFacility = terser.get("/.MSH-4");
            log.info("Message Type = " + messageType);
            log.info("Message Control Id = " + messageControlId);
            log.info("Sending Application = " + sendingApplication);
            log.info("Sending Facility = " + sendingFacility);
            log.info("Patient Id = " + patientId);
            log.info("Assigning Authority = " + assigningAuthorityUniversalId);
            OMElement registryRequest = this.createRegistryAddRequest(this.getRemoteIPAddress(socket), inMessage, messageControlId, this.formatPatientIdentitySource(sendingFacility, sendingApplication), this.formatPatientId(patientId, assigningAuthorityUniversalId));
            OMElement registryResponse = this.sendRequestToRegistry(registryRequest);
            return this.generateACK(inMessage, registryResponse);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            throw new HL7Exception(e);
        }
    }
}
