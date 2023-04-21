package org.ombu.stubs;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import org.ombu.model.CompletionParticipant;
import org.ombu.model.CoordinationContext;
import org.ombu.stubs.wsclients.EPRParser;
import org.ombu.stubs.wsclients.InitiatorService;
import org.ombu.stubs.wsclients.InitiatorServiceService;
import org.ombu.util.Log;

public class CompletionInitiatorStub {

    public static final String COMPLETION_PROTOCOL_IDENTIFIER = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Completion";

    public static final QName SERVICE_NAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "CompletionInitiatorService");

    public static final QName PORT_NAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "CompletionInitiatorPort");

    private static CompletionInitiatorStub instance;

    public static CompletionInitiatorStub getInstance() {
        if (instance == null) {
            instance = new CompletionInitiatorStub();
        }
        return instance;
    }

    private CompletionInitiatorStub() {
    }

    public void aborted(CompletionParticipant participant) {
        logSendingMessage("aborted", participant);
        W3CEndpointReference epr = participant.getParticipantProtocolService();
        EPRParser parser = new EPRParser(epr);
        InitiatorService servicePort = getService(parser.getAddress());
        Long participantId = Long.valueOf(parser.getReferenceParameters().get("Participant"));
        AddressingHeader header = new AddressingHeader();
        header.setTo(parser.getAddress());
        try {
            header.setAction(new URL("http://docs.oasis-open.org/ws-tx/wsat/2006/06/Aborted"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("ABORTED: address = " + parser.getAddress());
        servicePort.aborted(header, participantId, "");
    }

    public void commited(CompletionParticipant participant) {
        logSendingMessage("committed", participant);
        EPRParser parser = new EPRParser(participant.getParticipantProtocolService());
        InitiatorService servicePort = getService(parser.getAddress());
        Long participantId = Long.valueOf(parser.getReferenceParameters().get("Participant"));
        AddressingHeader header = new AddressingHeader();
        header.setTo(parser.getAddress());
        try {
            header.setAction(new URL("http://docs.oasis-open.org/ws-tx/wsat/2006/06/Committed"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        servicePort.commited(header, participantId, "");
    }

    private void logSendingMessage(String message, CompletionParticipant participant) {
        String logMsg = "Context %s: sending '%s' to completion participant %d.";
        long participantId = participant.getId();
        CoordinationContext ctx = participant.getCoordinationContext();
        String ctxIdentifier = ctx.getIdentifier();
        Log.info(String.format(logMsg, ctxIdentifier, message, participantId));
    }

    @SuppressWarnings("unused")
    private InitiatorService getService(W3CEndpointReference reference) {
        InitiatorServiceService serviceFactory = null;
        URL address = new EPRParser(reference).getAddress();
        serviceFactory = new InitiatorServiceService(address, SERVICE_NAME);
        InitiatorService reg = serviceFactory.getPort(reference, InitiatorService.class, new AddressingFeature(true));
        return reg;
    }

    private InitiatorService getService(URL address) {
        InitiatorServiceService serviceFactory = null;
        serviceFactory = new InitiatorServiceService(address, SERVICE_NAME);
        InitiatorService reg = serviceFactory.getPort(PORT_NAME, InitiatorService.class);
        return reg;
    }
}
