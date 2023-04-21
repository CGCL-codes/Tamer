package org.mobicents.media.server.ctrl.mgcp;

import jain.protocol.ip.mgcp.JainMgcpResponseEvent;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.NotifiedEntity;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import org.mobicents.media.server.ctrl.mgcp.signal.UnknownEventException;
import org.mobicents.media.server.ctrl.mgcp.signal.UnknownPackageException;
import org.mobicents.media.server.ctrl.mgcp.signal.UnknownSignalException;
import org.mobicents.media.server.spi.Endpoint;

/**
 * 
 * @author kulikov
 */
public class NotificationRequestAction1 implements Callable<JainMgcpResponseEvent> {

    private MgcpController controller;

    private NotificationRequest req;

    private static Logger logger = Logger.getLogger(NotificationRequestAction1.class);

    public NotificationRequestAction1(MgcpController controller, NotificationRequest req) {
        this.controller = controller;
        this.req = req;
    }

    public JainMgcpResponseEvent call() throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info("Request TX= " + req.getTransactionHandle() + ", Endpoint = " + req.getEndpointIdentifier());
        }
        NotificationRequestResponse response = null;
        RequestIdentifier requestID = req.getRequestIdentifier();
        EndpointIdentifier endpointID = req.getEndpointIdentifier();
        if (requestID == null || endpointID == null) {
            return reject(ReturnCode.Protocol_Error);
        }
        NotifiedEntity callAgent = req.getNotifiedEntity();
        if (callAgent == null) {
            callAgent = controller.getNotifiedEntity();
        }
        if (callAgent == null) {
            return reject(ReturnCode.Transient_Error);
        }
        Endpoint endpoint = null;
        try {
            endpoint = controller.getServer().lookup(endpointID.getLocalEndpointName(), true);
        } catch (Exception e) {
            logger.warn("Request to unknown endpoint: " + endpointID.getLocalEndpointName());
            return reject(ReturnCode.Endpoint_Unknown);
        }
        RequestedEvent[] events = req.getRequestedEvents();
        EventName[] signals = req.getSignalRequests();
        boolean emptyRequest = (events == null || events.length == 0) && (signals == null || signals.length == 0);
        EndpointActivity activity = null;
        try {
            activity = controller.activities.getEndpointActivity(endpointID);
            activity.attach(endpoint);
            if (emptyRequest) {
                activity.terminate();
                response = new NotificationRequestResponse(this, ReturnCode.Transaction_Executed_Normally);
                response.setTransactionHandle(req.getTransactionHandle());
                logger.info("Response TX = " + response.getTransactionHandle() + ", Response: " + response.getReturnCode());
                return response;
            }
            activity.setController(controller);
        } catch (Exception e) {
            return reject(ReturnCode.Insufficient_Resources_Now);
        }
        try {
            activity.accept(callAgent, requestID, events, signals);
        } catch (UnknownPackageException e) {
            logger.warn("Unknown package " + e.getMessage());
            return reject(ReturnCode.Unsupported_Or_Unknown_Package);
        } catch (UnknownEventException e) {
            logger.warn("Unknown event " + e.getMessage());
            return reject(ReturnCode.No_Such_Event_Or_Signal);
        } catch (UnknownSignalException e) {
            logger.warn("Unknown signal " + e.getMessage());
            return reject(ReturnCode.No_Such_Event_Or_Signal);
        } catch (UnknownActivityException e) {
            logger.warn("Unknown activity: " + e.getMessage());
            return reject(ReturnCode.Incorrect_Connection_ID);
        }
        response = new NotificationRequestResponse(this, ReturnCode.Transaction_Executed_Normally);
        response.setTransactionHandle(req.getTransactionHandle());
        logger.info("Response TX = " + response.getTransactionHandle() + ", Response: " + response.getReturnCode());
        return response;
    }

    private NotificationRequestResponse reject(ReturnCode code) {
        NotificationRequestResponse response = new NotificationRequestResponse(this, code);
        response.setTransactionHandle(req.getTransactionHandle());
        logger.info("Response TX = " + response.getTransactionHandle() + ", Response: " + response.getReturnCode());
        return response;
    }
}
