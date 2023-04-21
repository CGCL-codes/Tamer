package org.mobicents.mgcp.demo;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.NotificationRequestResponse;
import jain.protocol.ip.mgcp.message.Notify;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConflictingParameterException;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.EventName;
import jain.protocol.ip.mgcp.message.parms.NotifiedEntity;
import jain.protocol.ip.mgcp.message.parms.RequestedAction;
import jain.protocol.ip.mgcp.message.parms.RequestedEvent;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;
import jain.protocol.ip.mgcp.pkg.MgcpEvent;
import jain.protocol.ip.mgcp.pkg.PackageName;
import java.text.ParseException;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.FactoryException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;
import org.mobicents.protocols.mgcp.jain.pkg.AUPackage;
import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.mgcp.MgcpEndpointActivity;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

/**
 * 
 * @author amit bhayani
 */
public abstract class TTSSbb implements Sbb {

    public static final String ENDPOINT_NAME = "mobicents/ivr/$";

    public static final String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");

    public static final String WELCOME_ANN = "http://" + JBOSS_BIND_ADDRESS + ":8080/mgcpdemo/audio/RQNT-ULAW.wav";

    public static final String WELCOME = "Hello World. This is Mobicents Media Server Text To Speech Demo. Press any button on your touch dial phone.";

    private static final String DTMF_0 = "This is voice of Kevin You have pressed Zero";

    private static final String DTMF_1 = "This is voice of Kevin Sixteen. You have pressed One";

    private static final String DTMF_2 = "This is voice of Alan. You have pressed Two";

    private static final String DTMF_3 = "This is voice of Mbrola US One. You have pressed Three";

    private static final String DTMF_4 = "This is voice of Mbrola US Two. You have pressed Four";

    private static final String DTMF_5 = "This is voice of Mbrola US Three. You have pressed Five";

    private static final String DTMF_6 = "This is voice of Kevin. You have pressed Six";

    private static final String DTMF_7 = "This is voice of Kevin. You have pressed Seven";

    private static final String DTMF_8 = "This is voice of Kevin. You have pressed Eight";

    private static final String DTMF_9 = "This is voice of Kevin. You have pressed Nine";

    private static final String STAR = "This is voice of Kevin. You have pressed Star Sign";

    private static final String POUND = "This is voice of Kevin. You have pressed Pound Sign";

    private static final String A = "This is voice of Kevin. You have pressed A";

    private static final String B = "This is voice of Kevin. You have pressed B";

    private static final String C = "This is voice of Kevin. You have pressed C";

    private static final String D = "This is voice of Kevin. You have pressed D";

    private static final String VOICE_KEVIN = "kevin";

    private static final String VOICE_KEVIN16 = "kevin16";

    private static final String VOICE_ALAN = "alan";

    private static final String VOICE_MBROLA_US1 = "mbrola_us1";

    private static final String VOICE_MBROLA_US2 = "mbrola_us2";

    private static final String VOICE_MBROLA_US3 = "mbrola_us3";

    private SbbContext sbbContext;

    private SleeSipProvider provider;

    private AddressFactory addressFactory;

    private HeaderFactory headerFactory;

    private MessageFactory messageFactory;

    private SipActivityContextInterfaceFactory acif;

    private JainMgcpProvider mgcpProvider;

    private MgcpActivityContextInterfaceFactory mgcpAcif;

    public static final int MGCP_PEER_PORT = 2427;

    public static final int MGCP_PORT = 2727;

    private Tracer logger;

    /** Creates a new instance of CallSbb */
    public TTSSbb() {
    }

    public void onCallCreated(RequestEvent evt, ActivityContextInterface aci) {
        Request request = evt.getRequest();
        FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
        ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);
        logger.info("Incoming call " + from + " " + to);
        ActivityContextInterface daci = null;
        try {
            Dialog dialog = provider.getNewDialog(evt.getServerTransaction());
            dialog.terminateOnBye(true);
            daci = acif.getActivityContextInterface((DialogActivity) dialog);
            daci.attach(sbbContext.getSbbLocalObject());
        } catch (Exception e) {
            logger.severe("Error during dialog creation", e);
            respond(evt, Response.SERVER_INTERNAL_ERROR);
            return;
        }
        CallIdentifier callID = mgcpProvider.getUniqueCallIdentifier();
        this.setCallIdentifier(callID.toString());
        EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        CreateConnection createConnection = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);
        try {
            String sdp = new String(evt.getRequest().getRawContent());
            createConnection.setRemoteConnectionDescriptor(new ConnectionDescriptor(sdp));
        } catch (ConflictingParameterException e) {
        }
        int txID = mgcpProvider.getUniqueTransactionHandler();
        createConnection.setTransactionHandle(txID);
        MgcpConnectionActivity connectionActivity = null;
        try {
            connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
            ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(connectionActivity);
            epnAci.attach(sbbContext.getSbbLocalObject());
        } catch (FactoryException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (UnrecognizedActivityException ex) {
            ex.printStackTrace();
        }
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { createConnection });
    }

    public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci) {
        logger.info("Receive CRCX response: " + event.getTransactionHandle());
        ServerTransaction txn = getServerTransaction();
        Request request = txn.getRequest();
        ReturnCode status = event.getReturnCode();
        switch(status.getValue()) {
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
                this.setEndpointName(event.getSpecificEndpointIdentifier().getLocalEndpointName());
                logger.info("***&& " + this.getEndpointName());
                ConnectionIdentifier connectionIdentifier = event.getConnectionIdentifier();
                this.setConnectionIdentifier(connectionIdentifier.toString());
                String sdp = event.getLocalConnectionDescriptor().toString();
                ContentTypeHeader contentType = null;
                try {
                    contentType = headerFactory.createContentTypeHeader("application", "sdp");
                } catch (ParseException ex) {
                }
                String localAddress = provider.getListeningPoints()[0].getIPAddress();
                int localPort = provider.getListeningPoints()[0].getPort();
                Address contactAddress = null;
                try {
                    contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
                } catch (ParseException ex) {
                }
                ContactHeader contact = headerFactory.createContactHeader(contactAddress);
                sendAnnRQNT(WELCOME_ANN, true);
                Response response = null;
                try {
                    response = messageFactory.createResponse(Response.OK, request, contentType, sdp.getBytes());
                } catch (ParseException ex) {
                    logger.severe("ParseException while trying to create OK Response", ex);
                }
                response.setHeader(contact);
                try {
                    txn.sendResponse(response);
                } catch (InvalidArgumentException ex) {
                    logger.severe("InvalidArgumentException while trying to send OK Response", ex);
                } catch (SipException ex) {
                    logger.severe("SipException while trying to send OK Response", ex);
                }
                break;
            default:
                try {
                    response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
                    txn.sendResponse(response);
                } catch (Exception ex) {
                    logger.severe("Exception while trying to send SERVER_INTERNAL_ERROR Response", ex);
                }
        }
    }

    private void sendAnnRQNT(String mediaPath, boolean createActivity) {
        EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        NotificationRequest notificationRequest = new NotificationRequest(this, endpointID, mgcpProvider.getUniqueRequestIdentifier());
        EventName[] signalRequests = { new EventName(AUPackage.AU, MgcpEvent.factory("pa").withParm("an=" + mediaPath)) };
        notificationRequest.setSignalRequests(signalRequests);
        RequestedAction[] actions = new RequestedAction[] { RequestedAction.NotifyImmediately };
        RequestedEvent[] requestedEvents = { new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.oc), actions), new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.of), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("0")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("1")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("2")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("3")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("4")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("5")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("6")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("7")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("8")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("9")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("A")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("B")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("C")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("D")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("*")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("#")), actions) };
        notificationRequest.setRequestedEvents(requestedEvents);
        notificationRequest.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        NotifiedEntity notifiedEntity = new NotifiedEntity(JBOSS_BIND_ADDRESS, JBOSS_BIND_ADDRESS, MGCP_PORT);
        notificationRequest.setNotifiedEntity(notifiedEntity);
        if (createActivity) {
            MgcpEndpointActivity endpointActivity = null;
            try {
                endpointActivity = mgcpProvider.getEndpointActivity(endpointID);
                ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(endpointActivity);
                epnAci.attach(sbbContext.getSbbLocalObject());
            } catch (FactoryException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (UnrecognizedActivityException ex) {
                ex.printStackTrace();
            }
        }
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { notificationRequest });
        logger.info(" NotificationRequest sent");
    }

    private void sendTTSRQNT(String ttsText, String voice, boolean createActivity) {
        EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        NotificationRequest notificationRequest = new NotificationRequest(this, endpointID, mgcpProvider.getUniqueRequestIdentifier());
        ttsText = "ts(" + ttsText + ") vc(" + voice + ")";
        EventName[] signalRequests = { new EventName(AUPackage.AU, MgcpEvent.factory("pa").withParm(ttsText)) };
        notificationRequest.setSignalRequests(signalRequests);
        RequestedAction[] actions = new RequestedAction[] { RequestedAction.NotifyImmediately };
        RequestedEvent[] requestedEvents = { new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.oc), actions), new RequestedEvent(new EventName(AUPackage.AU, MgcpEvent.of), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("0")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("1")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("2")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("3")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("4")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("5")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("6")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("7")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("8")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("9")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("A")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("B")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("C")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("D")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("*")), actions), new RequestedEvent(new EventName(PackageName.Dtmf, MgcpEvent.factory("#")), actions) };
        notificationRequest.setRequestedEvents(requestedEvents);
        notificationRequest.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        NotifiedEntity notifiedEntity = new NotifiedEntity(JBOSS_BIND_ADDRESS, JBOSS_BIND_ADDRESS, MGCP_PORT);
        notificationRequest.setNotifiedEntity(notifiedEntity);
        if (createActivity) {
            MgcpEndpointActivity endpointActivity = null;
            try {
                endpointActivity = mgcpProvider.getEndpointActivity(endpointID);
                ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(endpointActivity);
                epnAci.attach(sbbContext.getSbbLocalObject());
            } catch (FactoryException ex) {
                ex.printStackTrace();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (UnrecognizedActivityException ex) {
                ex.printStackTrace();
            }
        }
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { notificationRequest });
        logger.info(" NotificationRequest sent");
    }

    public void onNotificationRequestResponse(NotificationRequestResponse event, ActivityContextInterface aci) {
        logger.info("onNotificationRequestResponse");
        ReturnCode status = event.getReturnCode();
        switch(status.getValue()) {
            case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:
                logger.info("The Announcement should have been started");
                break;
            default:
                ReturnCode rc = event.getReturnCode();
                logger.severe("RQNT failed. Value = " + rc.getValue() + " Comment = " + rc.getComment());
                break;
        }
    }

    public void onNotifyRequest(Notify event, ActivityContextInterface aci) {
        logger.info("onNotifyRequest");
        NotificationRequestResponse response = new NotificationRequestResponse(event.getSource(), ReturnCode.Transaction_Executed_Normally);
        response.setTransactionHandle(event.getTransactionHandle());
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { response });
        EventName[] observedEvents = event.getObservedEvents();
        for (EventName observedEvent : observedEvents) {
            switch(observedEvent.getEventIdentifier().intValue()) {
                case MgcpEvent.REPORT_ON_COMPLETION:
                    logger.info("Announcemnet Completed NTFY received");
                    break;
                case MgcpEvent.REPORT_FAILURE:
                    logger.info("Announcemnet Failed received");
                    break;
                default:
                    if (observedEvent.getPackageName().toString().equals("D")) {
                        int decodedId = decodeDTMF(observedEvent);
                        processDTMF(decodedId);
                    }
            }
        }
    }

    public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {
        EndpointIdentifier endpointID = new EndpointIdentifier(this.getEndpointName(), JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
        DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);
        deleteConnection.setTransactionHandle(mgcpProvider.getUniqueTransactionHandler());
        mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { deleteConnection });
        ServerTransaction tx = evt.getServerTransaction();
        Request request = evt.getRequest();
        try {
            Response response = messageFactory.createResponse(Response.OK, request);
            tx.sendResponse(response);
        } catch (Exception e) {
            logger.severe("Error while sending DLCX ", e);
        }
    }

    private int decodeDTMF(EventName observed) {
        String eventName = observed.getEventIdentifier().getName();
        if (Pattern.matches("\\d", eventName)) {
            int i = Integer.parseInt(eventName);
            return MgcpEvent.DTMF_0 + i;
        } else if (Pattern.matches("[A-D]#*", eventName)) {
            switch(eventName.charAt(0)) {
                case 'A':
                    return MgcpEvent.DTMF_A;
                case 'B':
                    return MgcpEvent.DTMF_B;
                case 'C':
                    return MgcpEvent.DTMF_C;
                case 'D':
                    return MgcpEvent.DTMF_D;
                case '#':
                    return MgcpEvent.DTMF_HASH;
                case '*':
                    return MgcpEvent.DTMF_STAR;
                default:
                    return -1;
            }
        } else {
            return -1;
        }
    }

    private void processDTMF(int id) {
        switch(id) {
            case MgcpEvent.DTMF_0:
                logger.info("You have pressed 0");
                sendTTSRQNT(DTMF_0, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_1:
                logger.info("You have pressed 1");
                sendTTSRQNT(DTMF_1, VOICE_KEVIN16, false);
                break;
            case MgcpEvent.DTMF_2:
                logger.info("You have pressed 2");
                sendTTSRQNT(DTMF_2, VOICE_ALAN, false);
                break;
            case MgcpEvent.DTMF_3:
                logger.info("You have pressed 3");
                sendTTSRQNT(DTMF_3, VOICE_MBROLA_US1, false);
                break;
            case MgcpEvent.DTMF_4:
                logger.info("You have pressed 4");
                sendTTSRQNT(DTMF_4, VOICE_MBROLA_US2, false);
                break;
            case MgcpEvent.DTMF_5:
                logger.info("You have pressed 5");
                sendTTSRQNT(DTMF_5, VOICE_MBROLA_US3, false);
                break;
            case MgcpEvent.DTMF_6:
                logger.info("You have pressed 6");
                sendTTSRQNT(DTMF_6, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_7:
                logger.info("You have pressed 7");
                sendTTSRQNT(DTMF_7, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_8:
                logger.info("You have pressed 8");
                sendTTSRQNT(DTMF_8, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_9:
                logger.info("You have pressed 9");
                sendTTSRQNT(DTMF_9, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_A:
                logger.info("You have pressed A");
                sendTTSRQNT(A, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_B:
                logger.info("You have pressed B");
                sendTTSRQNT(B, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_C:
                logger.info("You have pressed C");
                sendTTSRQNT(C, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_D:
                logger.info("You have pressed D");
                sendTTSRQNT(D, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_STAR:
                logger.info("You have pressed *");
                sendTTSRQNT(STAR, VOICE_KEVIN, false);
                break;
            case MgcpEvent.DTMF_HASH:
                logger.info("You have pressed C");
                sendTTSRQNT(POUND, VOICE_KEVIN, false);
                break;
        }
    }

    private void respond(RequestEvent evt, int cause) {
        Request request = evt.getRequest();
        ServerTransaction tx = evt.getServerTransaction();
        try {
            Response response = messageFactory.createResponse(cause, request);
            tx.sendResponse(response);
        } catch (Exception e) {
            logger.warning("Unexpected error: ", e);
        }
    }

    private ServerTransaction getServerTransaction() {
        ActivityContextInterface[] activities = sbbContext.getActivities();
        for (ActivityContextInterface activity : activities) {
            if (activity.getActivity() instanceof ServerTransaction) {
                return (ServerTransaction) activity.getActivity();
            }
        }
        return null;
    }

    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        this.logger = sbbContext.getTracer(TTSSbb.class.getSimpleName());
        try {
            Context ctx = (Context) new InitialContext().lookup("java:comp/env");
            provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");
            addressFactory = provider.getAddressFactory();
            headerFactory = provider.getHeaderFactory();
            messageFactory = provider.getMessageFactory();
            acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");
            mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
            mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");
        } catch (Exception ne) {
            logger.severe("Could not set SBB context:", ne);
        }
    }

    public abstract String getConnectionIdentifier();

    public abstract void setConnectionIdentifier(String connectionIdentifier);

    public abstract String getCallIdentifier();

    public abstract void setCallIdentifier(String CallIdentifier);

    public abstract String getRemoteSdp();

    public abstract void setRemoteSdp(String remoteSdp);

    public abstract String getEndpointName();

    public abstract void setEndpointName(String endpointName);

    public void unsetSbbContext() {
        this.sbbContext = null;
        this.logger = null;
    }

    public void sbbCreate() throws CreateException {
    }

    public void sbbPostCreate() throws CreateException {
    }

    public void sbbActivate() {
    }

    public void sbbPassivate() {
    }

    public void sbbLoad() {
    }

    public void sbbStore() {
    }

    public void sbbRemove() {
    }

    public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
    }

    public void sbbRolledBack(RolledBackContext rolledBackContext) {
    }
}
