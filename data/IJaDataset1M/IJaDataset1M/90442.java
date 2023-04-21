package cwsexamples.wsframework;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.addressing.EndpointReference;
import cartago.ArtifactId;
import cartago.CartagoException;
import cartago.Op;
import cartago.OpFeedbackParam;
import cartago.OpRequestTimeoutException;
import cartagows.CartagowsParam;
import cartagows.WSMsgInfo;
import cartagows.util.WSAgent;
import cartagows.util.XMLib;
import cartagows.wsframework.core.DataContainer;
import cartagows.wsframework.core.IOperation;
import cartagows.wsframework.core.IRequest;
import cartagows.wsframework.core.Request;
import cartagows.wsframework.core.ServiceConfiguration;
import cartagows.wsframework.wsatomictransaction.ATRegisterPartecipantContent;
import cartagows.wsframework.wsatomictransaction.WSAtomicTransactionOperation;
import cartagows.wsframework.wsatomictransaction.WSAtomicTransactionParam;
import cartagows.wsframework.wsatomictransaction.WSAtomicTransactionWSDLOperation;
import cartagows.wsframework.wscoordination.WSCoordinationErrorParam;
import cartagows.wsframework.wscoordination.WSCoordinationMessageElem;
import cartagows.wsframework.wscoordination.WSCoordinationOperation;
import cartagows.wsframework.wscoordination.WSCoordinationParam;

public class TransportService extends WSAgent {

    private HashMap<Date, BookingDateInfo> bookingRegistry;

    private HashMap<String, BookingTransactionInfo> transactionsOutcome;

    private int capacityLimit;

    private ArtifactId wsProxy;

    private Calendar cal;

    private ArtifactId wsPanel;

    private ArtifactId agentWallet;

    private ArtifactId wscoordReqMediator;

    public TransportService(String agentName) throws CartagoException {
        super(agentName);
        init();
    }

    public TransportService(String agentName, String workspaceName, String workspaceHost) throws CartagoException {
        super(agentName, workspaceName, workspaceHost);
        init();
    }

    private void init() throws CartagoException {
        bookingRegistry = new HashMap<Date, BookingDateInfo>();
        transactionsOutcome = new HashMap<String, BookingTransactionInfo>();
        capacityLimit = 10;
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(2008, 0, 1);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(0));
        cal.set(2008, 0, 2);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(4));
        cal.set(2008, 0, 3);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(9));
        cal.set(2008, 0, 4);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(1));
        cal.set(2008, 0, 5);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(3));
        cal.set(2008, 0, 6);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(0));
        cal.set(2008, 0, 7);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(5));
        cal.set(2008, 0, 8);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(5));
        cal.set(2008, 0, 9);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(8));
        cal.set(2008, 0, 10);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(8));
        cal.set(2008, 0, 11);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(2));
        cal.set(2008, 0, 12);
        bookingRegistry.put(cal.getTime(), new BookingDateInfo(4));
        wsPanel = makeArtifact(ExampleParam.TM_SERVICE_WSPANEL_DEF_NAME, CartagowsParam.WSPANEL_CLASS_PATH, new Object[] { ExampleParam.BOOKING_WSDL_URI, ExampleParam.TM_SERVICE_NAME });
        agentWallet = makeArtifact(ExampleParam.TM_WALLET_NAME, CartagowsParam.AGENTWALLET_CLASS_PATH);
        wscoordReqMediator = lookupArtifact(WSCoordinationParam.REQUEST_MEDIATOR_DEF_NAME);
    }

    public void run() {
        log("[TRANSPORT_MANAGER:]Starting");
        while (true) {
            OMElement coordContext, header, body;
            WSMsgInfo inputMsg = null;
            String senderEPR, coordEPR;
            OpFeedbackParam<WSMsgInfo> res = new OpFeedbackParam<WSMsgInfo>();
            try {
                log("[TRANSPORT_MANAGER:]Waiting requests");
                doAction(wsPanel, new Op("getWSMsg", res));
                inputMsg = res.get();
                String opName = inputMsg.getOperationName();
                header = XMLib.getInstance().buildElementFromString(inputMsg.getHeader());
                senderEPR = inputMsg.getWSAReplyToAddress();
                body = XMLib.getInstance().buildElementFromString(inputMsg.getBody());
                coordContext = XMLib.getInstance().getElement(WSCoordinationMessageElem.COORDINATION_CONTEXT, header);
                String activityId = XMLib.getInstance().getElementValue(WSCoordinationMessageElem.IDENTIFIER, coordContext);
                if (activityId != null) {
                    if (opName.equals(ExampleWSDLOperation.BOOKING)) {
                        log("[TRANSPORT_MANAGER:]Bookin operation found");
                        log("[TRANSPORT_MANAGER:]Registering as a participant for the AT:" + activityId);
                        WSCoordinationOperation operation = new WSCoordinationOperation(WSCoordinationOperation.REGISTER_PARTICIPANT);
                        ATRegisterPartecipantContent content = new ATRegisterPartecipantContent(WSAtomicTransactionParam.VOLATILE_2PC, coordContext);
                        DataContainer reqData = new DataContainer(DataContainer.OPERATION_SPECIFIC, content);
                        Request request = new Request(WSCoordinationParam.WSDL_URI, WSCoordinationParam.REGISTRATION_SERVICE_NAME, WSCoordinationParam.REGISTRATION_PORT_TYPE, ExampleParam.TM_EPR, operation, reqData);
                        OpFeedbackParam<String> requestFedback = new OpFeedbackParam<String>();
                        doAction(wscoordReqMediator, new Op("addNewRequest", request, requestFedback));
                        String requestID = requestFedback.get();
                        OpFeedbackParam<IRequest> requestResFedback = new OpFeedbackParam<IRequest>();
                        doAction(wscoordReqMediator, new Op("getRequestResult", requestID, requestResFedback));
                        request = (Request) requestResFedback.get();
                        if (request.isRequestSucceeded()) {
                            doAction(agentWallet, new Op("addInfoToWallet", senderEPR, new Integer(IOperation.WS_ATOMIC_TRANSACTION), new DataContainer(DataContainer.OMELEMENT, coordContext)));
                            coordEPR = (String) request.getRequestResult().getData();
                            doAction(agentWallet, new Op("addInfoToWallet", coordEPR, new Integer(IOperation.WS_ATOMIC_TRANSACTION), new DataContainer(DataContainer.OMELEMENT, coordContext)));
                            wsProxy = createWSInterface(ExampleParam.TM_SERVICE_WSPROXY_DEF_NAME, WSAtomicTransactionParam.WSDL_URI, WSAtomicTransactionParam.COORDINATOR_PORT_TYPE, new EndpointReference(coordEPR));
                            doAction(wsProxy, new Op("configure", new ServiceConfiguration(coordContext, null)));
                            doAction(wsProxy, new Op("setLocalEPR", ExampleParam.TM_EPR));
                            log("[TRANSPORT_MANAGER:]Registration done successfully");
                            String startDate = XMLib.getInstance().getElementValue(ExampleMessageElem.BOOKING_REQUEST, ExampleMessageElem.BOOKING_START_DATE, body);
                            String endDate = XMLib.getInstance().getElementValue(ExampleMessageElem.BOOKING_REQUEST, ExampleMessageElem.BOOKING_END_DATE, body);
                            String[] splitted = startDate.split("-");
                            String[] splitted2 = endDate.split("-");
                            cal.set(Integer.valueOf(splitted[0]).intValue(), Integer.valueOf(splitted[1]).intValue(), Integer.valueOf(splitted[2]).intValue());
                            Date d1 = cal.getTime();
                            Date workingDate = cal.getTime();
                            cal.set(Integer.valueOf(splitted2[0]).intValue(), Integer.valueOf(splitted2[1]).intValue(), Integer.valueOf(splitted2[2]).intValue());
                            Date d2 = cal.getTime();
                            transactionsOutcome.put(activityId, new BookingTransactionInfo(d1, d2));
                            cal.setTime(workingDate);
                            workingDate = cal.getTime();
                            if (d2.after(d1)) {
                                boolean isTransportFull = false;
                                while (workingDate.compareTo(d2) <= 0) {
                                    if (bookingRegistry.get(workingDate).getBookingNumber() >= capacityLimit || bookingRegistry.get(workingDate).isLocked()) {
                                        isTransportFull = true;
                                        break;
                                    }
                                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
                                    workingDate = cal.getTime();
                                }
                                String replyMsg = "";
                                if (isTransportFull) {
                                    replyMsg = createBookingResponseNotAvailable().toString();
                                    transactionsOutcome.get(activityId).setVote(false);
                                    log("[TRANSPORT_MANAGER:]Cannot reserve the transport for the specified date, " + "the TransportManager will vote ABORT as transaction outcome");
                                } else {
                                    workingDate = d1;
                                    cal.setTime(workingDate);
                                    while (workingDate.compareTo(d2) <= 0) {
                                        bookingRegistry.get(cal.getTime()).setLocked(true);
                                        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
                                        workingDate = cal.getTime();
                                    }
                                    replyMsg = createBookingResponseAvailable(200, activityId).toString();
                                    transactionsOutcome.get(activityId).setVote(true);
                                    log("[TRANSPORT_MANAGER:]Is possible reserve the transport for the specified date, " + "the TransportManager will vote COMMIT as transaction outcome");
                                }
                                try {
                                    OpFeedbackParam<String> replyId = new OpFeedbackParam<String>();
                                    doAction(wsPanel, new Op("sendWSReply", inputMsg, replyMsg, new ServiceConfiguration(coordContext, null), replyId));
                                    log("[TRANSPORT_MANAGER:]Reply sent successfully - id: " + replyId.get());
                                } catch (CartagoException ex) {
                                    log("[TRANSPORT_MANAGER:]Reply failed.");
                                }
                            } else log("[TRANSPORT_MANAGER:]Wrong dates");
                        } else log("[TRANSPORT_MANAGER:]" + ((String) request.getRequestResult().getData()));
                    } else if (opName.equals(WSAtomicTransactionWSDLOperation.PREPARE)) {
                        log("[TRANSPORT_MANAGER:]Prepare operation found");
                        boolean validationRes = validateContext(senderEPR, inputMsg);
                        if (validationRes == true) {
                            if (transactionsOutcome.get(activityId).getVote() == true) {
                                doOneWay(wsProxy, WSAtomicTransactionWSDLOperation.PREPARED, createPreparedMessage().toString(), CartagowsParam.LONG_TIMEOUT);
                            } else {
                                doOneWay(wsProxy, WSAtomicTransactionWSDLOperation.ABORTED, createAbortedMessage().toString(), CartagowsParam.LONG_TIMEOUT);
                            }
                        } else log("[TRANSPORT_MANAGER:]" + WSCoordinationErrorParam.ERR_BAD_CONTEXT);
                    } else if (opName.equals(WSAtomicTransactionWSDLOperation.COMMIT)) {
                        log("[TRANSPORT_MANAGER:]Commit operation found");
                        boolean validationRes = validateContext(senderEPR, inputMsg);
                        if (validationRes == true) {
                            cal.setTime(transactionsOutcome.get(activityId).getStartDate());
                            Date endDate = transactionsOutcome.get(activityId).getEndDate();
                            Date workingDate = cal.getTime();
                            int oldValue;
                            while (workingDate.compareTo(endDate) <= 0) {
                                oldValue = bookingRegistry.get(cal.getTime()).getBookingNumber();
                                bookingRegistry.get(cal.getTime()).setBookingNumber(oldValue + 1);
                                bookingRegistry.get(cal.getTime()).setLocked(false);
                                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
                                workingDate = cal.getTime();
                            }
                            doOneWay(wsProxy, WSAtomicTransactionWSDLOperation.COMMITED, createCommittedMessage().toString(), CartagowsParam.LONG_TIMEOUT);
                            transactionsOutcome.remove(activityId);
                            log("[TRANSPORT_MANAGER:]Transaction" + activityId + "successfully committed");
                        } else log("[TRANSPORT_MANAGER:]" + WSCoordinationErrorParam.ERR_BAD_CONTEXT);
                    } else if (opName.equals(WSAtomicTransactionWSDLOperation.ROLLBACK)) {
                        log("[TRANSPORT_MANAGER:]Rollback operation found");
                        boolean validationRes = validateContext(senderEPR, inputMsg);
                        if (validationRes == true) {
                            cal.setTime(transactionsOutcome.get(activityId).getStartDate());
                            Date endDate = transactionsOutcome.get(activityId).getEndDate();
                            Date workingDate = cal.getTime();
                            while (workingDate.compareTo(endDate) <= 0) {
                                bookingRegistry.get(cal.getTime()).setLocked(false);
                                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
                                workingDate = cal.getTime();
                            }
                            doOneWay(wsProxy, WSAtomicTransactionWSDLOperation.ABORTED, createAbortedMessage().toString(), CartagowsParam.LONG_TIMEOUT);
                            transactionsOutcome.remove(activityId);
                            log("[TRANSPORT_MANAGER:]Transaction" + activityId + "successfully aborted");
                        } else log("[TRANSPORT_MANAGER:]" + WSCoordinationErrorParam.ERR_BAD_CONTEXT);
                    }
                } else {
                    log("[TRANSPORT_MANAGER:]activity Id nullo");
                }
            } catch (OpRequestTimeoutException ex) {
                log("[TRANSPORT_MANAGER:]No msg");
            } catch (Exception e) {
                log("[TRANSPORT_MANAGER:]" + e.getMessage() + " " + e.getCause());
                e.printStackTrace();
            }
        }
    }

    private OMElement createCommittedMessage() {
        OMElement msg = XMLib.getInstance().buildElement(WSCoordinationMessageElem.COMMITTED);
        return msg;
    }

    private OMElement createAbortedMessage() {
        OMElement msg = XMLib.getInstance().buildElement(WSCoordinationMessageElem.ABORTED);
        return msg;
    }

    private OMElement createPreparedMessage() {
        OMElement msg = XMLib.getInstance().buildElement(WSCoordinationMessageElem.PREPARED);
        return msg;
    }

    private OMElement createBookingResponseAvailable(double price, String bookingID) {
        OMElement msg = XMLib.getInstance().buildElement(ExampleMessageElem.BOOKING_RESPONSE);
        OMElement available = XMLib.getInstance().buildElement(ExampleMessageElem.AVAILABLE);
        OMElement priceEl = XMLib.getInstance().buildElement(ExampleMessageElem.PRICE, "" + price);
        OMElement bookingIDEl = XMLib.getInstance().buildElement(ExampleMessageElem.BOOKIG_ID, bookingID);
        XMLib.getInstance().addChildNode(available, priceEl);
        XMLib.getInstance().addChildNode(available, bookingIDEl);
        XMLib.getInstance().addChildNode(msg, available);
        return msg;
    }

    private OMElement createBookingResponseNotAvailable() {
        OMElement msg = XMLib.getInstance().buildElement(ExampleMessageElem.BOOKING_RESPONSE);
        OMElement notAvailable = XMLib.getInstance().buildElement(ExampleMessageElem.NOT_AVAILABLE);
        XMLib.getInstance().addChildNode(msg, notAvailable);
        return msg;
    }

    private boolean validateContext(String senderEPR, WSMsgInfo msg) throws Exception {
        OpFeedbackParam<Boolean> checkContext = new OpFeedbackParam<Boolean>();
        doAction(agentWallet, new Op("validateMessage", senderEPR, msg, new WSAtomicTransactionOperation(WSAtomicTransactionOperation.VALIDATE_AT_CONTEXT), checkContext));
        return checkContext.get();
    }
}
