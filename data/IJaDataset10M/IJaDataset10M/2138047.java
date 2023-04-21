package org.mobicents.servlet.sip.example.diameter.rorf;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Network;
import org.jdiameter.api.NetworkReqListener;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.events.ReAuthAnswer;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.cca.ClientCCASession;
import org.jdiameter.api.cca.ServerCCASession;
import org.jdiameter.api.cca.events.JCreditControlAnswer;
import org.jdiameter.api.cca.events.JCreditControlRequest;
import org.jdiameter.client.api.ISessionFactory;
import org.mobicents.diameter.stack.DiameterStackMultiplexer;
import org.mobicents.servlet.sip.example.diameter.utils.DiameterUtilities;

public class RoClientImpl extends CreditControlSessionFactory implements NetworkReqListener, EventListener<Request, Answer>, RoClient {

    private static Logger logger = Logger.getLogger(RoClient.class);

    private static final int IDLE = 0;

    private static final int SENT_CCR_INITIAL = 2;

    private static final int RECEIVED_CCA_INITIAL = 4;

    private static final int SENT_CCR_UPDATE = 6;

    private static final int RECEIVED_CCA_UPDATE = 8;

    private static final int SENT_CCR_TERMINATION = 10;

    private static final int RECEIVED_CCA_TERMINATION = 12;

    private static final int CC_REQUEST_TYPE_INITIAL = 1;

    private static final int CC_REQUEST_TYPE_UPDATE = 2;

    private static final int CC_REQUEST_TYPE_TERMINATION = 3;

    private static final int CC_REQUEST_TYPE_EVENT = 4;

    private int currentState = IDLE;

    private static String clientHost = "127.0.0.1";

    private static String clientPort = "13868";

    private static String clientURI = "aaa://" + clientHost + ":" + clientPort;

    private static String serverHost = "127.0.0.1";

    private static String serverPort = "3868";

    private static String serverURI = "aaa://" + serverHost + ":" + serverPort;

    private static String realmName = "mobicents.org";

    private ApplicationId roAppId = ApplicationId.createByAuthAppId(10415L, 4L);

    private static final String SERVICE_CONTEXT_DOMAIN = "@mss.mobicents.org";

    private final int CHARGING_UNITS_TIME = 10;

    private DiameterStackMultiplexer muxMBean;

    private RoClientListener listener;

    private HashMap<String, ClientCCASession> roSessions = new HashMap<String, ClientCCASession>();

    private boolean areFinalUnits;

    private int reservedUnits;

    private int ccRequestNumber = 0;

    private int totalCallDurationCounter = 0;

    private int partialCallDurationCounter = 0;

    private Timer callDurationTimer = null;

    private Timer sendUpdatetimer = null;

    private Timer sendTerminateTimer = null;

    public RoClientImpl(RoClientListener listener) {
        super(null, 5000);
        this.listener = listener;
        try {
            ObjectName objectName = new ObjectName("diameter.mobicents:service=DiameterStackMultiplexer");
            Object[] params = new Object[] {};
            String[] signature = new String[] {};
            String operation = "getMultiplexerMBean";
            MBeanServer server = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
            Object object = server.invoke(objectName, operation, params, signature);
            if (object instanceof DiameterStackMultiplexer) {
                muxMBean = (DiameterStackMultiplexer) object;
            }
            Stack stack = muxMBean.getStack();
            super.sessionFactory = stack.getSessionFactory();
            Network network = stack.unwrap(Network.class);
            network.addNetworkReqListener(this, roAppId);
            ((ISessionFactory) sessionFactory).registerAppFacory(ServerCCASession.class, this);
            ((ISessionFactory) sessionFactory).registerAppFacory(ClientCCASession.class, this);
        } catch (Exception e) {
            logger.error("Failed to initialize Ro Client.", e);
        }
    }

    /**
   * Creates a new Ro Client Session
   * 
   * @return
   * @throws InternalException
   */
    private ClientCCASession getRoSession() throws InternalException {
        return ((ISessionFactory) super.sessionFactory).getNewAppSession(null, roAppId, ClientCCASession.class, null);
    }

    public void reserveInitialUnits(String subscriptionId, String serviceContextId) throws Exception {
        ClientCCASession roSession = roSessions.get(serviceContextId);
        if (roSession == null) {
            roSession = getRoSession();
            roSessions.put(serviceContextId, roSession);
        }
        this.ccRequestNumber = 0;
        this.totalCallDurationCounter = 0;
        this.partialCallDurationCounter = 0;
        JCreditControlRequest initialCCR = createCCR(CC_REQUEST_TYPE_INITIAL, subscriptionId, serviceContextId);
        roSession.sendCreditControlRequest(initialCCR);
        switchStateMachine(SENT_CCR_INITIAL);
    }

    public void startCharging(String subscriptionId, String serviceContextId) throws Exception {
        sendUpdatetimer = new Timer();
        sendTerminateTimer = new Timer();
        callDurationTimer = new Timer();
        if (logger.isInfoEnabled()) {
            logger.info("(((o))) SERVICE HAS BEEN ESTABLISHED (((o)))");
        }
        setCountCallTime(serviceContextId, 1000);
        if (areFinalUnits) {
            setTerminateTimer(subscriptionId, serviceContextId, reservedUnits * 1000);
        } else {
            setUpdateTimer(subscriptionId, serviceContextId, reservedUnits * 1000);
        }
    }

    public void updateCharging(String subscriptionId, String serviceContextId, long units) throws Exception {
        ClientCCASession roSession = roSessions.get(serviceContextId);
        JCreditControlRequest updateCCR = createCCR(CC_REQUEST_TYPE_UPDATE, subscriptionId, serviceContextId);
        roSession.sendCreditControlRequest(updateCCR);
        switchStateMachine(SENT_CCR_UPDATE);
    }

    public void stopCharging(String subscriptionId, String serviceContextId) throws Exception {
        sendUpdatetimer.cancel();
        callDurationTimer.cancel();
        try {
            this.listener.creditTerminated();
        } catch (Exception e) {
            logger.error("Failure in Listener handling 'creditTerminated' callback.");
        }
        if (logger.isInfoEnabled()) {
            logger.info("(((o))) SERVICE HAS BEEN TERMINATED! (((o)))");
        }
        ClientCCASession roSession = roSessions.get(serviceContextId);
        JCreditControlRequest terminateCCR = createCCR(CC_REQUEST_TYPE_TERMINATION, subscriptionId, serviceContextId);
        roSession.sendCreditControlRequest(terminateCCR);
        switchStateMachine(SENT_CCR_TERMINATION);
    }

    /**
   * Create a Ro CCR message, with the selected Request Type and Service Context ID
   * 
   * @param ccRequestType
   * @param serviceContextId
   * @return
   * @throws Exception
   */
    private JCreditControlRequest createCCR(int ccRequestType, String subscriptionId, String serviceContextId) throws Exception {
        ClientCCASession roSession = roSessions.get(serviceContextId);
        JCreditControlRequest ccr = createCreditControlRequest(roSession.getSessions().get(0).createRequest(JCreditControlRequest.code, roAppId, realmName, serverHost));
        AvpSet ccrAvps = ccr.getMessage().getAvps();
        ccrAvps.removeAvp(Avp.ORIGIN_HOST);
        ccrAvps.addAvp(Avp.ORIGIN_HOST, clientURI, true);
        ccrAvps.addAvp(Avp.AUTH_APPLICATION_ID, 4);
        AvpSet vsaid = ccrAvps.addGroupedAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID);
        vsaid.addAvp(Avp.VENDOR_ID, 10415);
        vsaid.addAvp(Avp.AUTH_APPLICATION_ID, 4);
        ccrAvps.addAvp(461, serviceContextId + SERVICE_CONTEXT_DOMAIN, false);
        ccrAvps.addAvp(416, ccRequestType);
        ccrAvps.addAvp(415, this.ccRequestNumber++);
        ccrAvps.removeAvp(Avp.DESTINATION_HOST);
        ccrAvps.addAvp(Avp.DESTINATION_HOST, serverURI, false);
        AvpSet subscriptionIdAvp = ccrAvps.addGroupedAvp(443);
        subscriptionIdAvp.addAvp(450, 2);
        subscriptionIdAvp.addAvp(444, subscriptionId, false);
        AvpSet rsuAvp = ccrAvps.addGroupedAvp(437);
        rsuAvp.addAvp(420, CHARGING_UNITS_TIME);
        if (ccRequestNumber >= 1) {
            AvpSet usedServiceUnit = ccrAvps.addGroupedAvp(446);
            usedServiceUnit.addAvp(420, this.partialCallDurationCounter);
        }
        DiameterUtilities.printMessage(ccr.getMessage());
        return ccr;
    }

    public void doCreditControlAnswer(ClientCCASession session, JCreditControlRequest request, JCreditControlAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
        AvpSet ccrAvps = request.getMessage().getAvps();
        AvpSet ccaAvps = answer.getMessage().getAvps();
        long resultCode = 5000;
        try {
            resultCode = answer.getResultCodeAvp().getUnsigned32();
        } catch (Exception e) {
            logger.error("Failed to retrieve Result-Code AVP value.", e);
        }
        switch(currentState) {
            case IDLE:
                logger.error("Unexpected CCA message at IDLE state.");
                break;
            case SENT_CCR_INITIAL:
                try {
                    if (resultCode >= 2000 && resultCode < 3000) {
                        this.reservedUnits = ccaAvps.getAvp(431).getGrouped().getAvp(420).getInteger32();
                        this.areFinalUnits = (answer.getMessage().getAvps().getAvp(430) != null);
                        if (logger.isInfoEnabled()) {
                            logger.info("(( $ )) Requested [" + CHARGING_UNITS_TIME + "] units, granted [" + reservedUnits + "] units. (( $ ))");
                        }
                        try {
                            this.listener.creditGranted(reservedUnits, areFinalUnits);
                        } catch (Exception e) {
                            logger.error("Failure in Listener handling 'creditGranted' callback.");
                        }
                    } else {
                        try {
                            this.listener.creditDenied((int) resultCode);
                        } catch (Exception e) {
                            logger.error("Failure in Listener handling 'creditDenied' callback.");
                        }
                        logger.info("(((o))) UNABLE TO ESTABLISH SERVICE, CREDIT DENIED (" + resultCode + ") (((o)))");
                    }
                } catch (Exception e) {
                    logger.error("(( $ )) Failure handling CCA at SENT_CCR_INITIAL state. (( $ ))", e);
                }
                switchStateMachine(RECEIVED_CCA_INITIAL);
                break;
            case RECEIVED_CCA_INITIAL:
                logger.error("Unexpected CCA message at RECEIVED_CCA_INITIAL state. Duplicate?");
                break;
            case SENT_CCR_UPDATE:
                try {
                    if (resultCode >= 2000 && resultCode < 3000) {
                        long reservedUnits = ccaAvps.getAvp(431).getGrouped().getAvp(420).getInteger32();
                        String serviceContextId = ccrAvps.getAvp(461).getUTF8String().replaceAll(SERVICE_CONTEXT_DOMAIN, "");
                        if (logger.isInfoEnabled()) {
                            logger.info("(( $ )) Requested [" + CHARGING_UNITS_TIME + "] units, granted [" + reservedUnits + "] units. (( $ ))");
                        }
                        partialCallDurationCounter = 0;
                        String subscriptionId = ccrAvps.getAvp(443).getGrouped().getAvp(444).getUTF8String();
                        Avp finalUnitIndication = answer.getMessage().getAvps().getAvp(430);
                        if (finalUnitIndication == null) {
                            setUpdateTimer(subscriptionId, serviceContextId, reservedUnits * 1000);
                        } else {
                            setTerminateTimer(subscriptionId, serviceContextId, reservedUnits * 1000);
                        }
                    } else {
                        callDurationTimer.cancel();
                        logger.error("(( $ )) Failure trying to obtain credit (" + resultCode + "). (( $ ))");
                        logger.error("(((o))) SERVICE HAS BEEN TERMINATED! (((o)))");
                    }
                } catch (Exception e) {
                    logger.error("(( $ )) Failure handling CCA at SENT_CCR_UPDATE state. (( $ ))", e);
                }
                switchStateMachine(RECEIVED_CCA_INITIAL);
                break;
            case RECEIVED_CCA_UPDATE:
                logger.error("Unexpected CCA message at RECEIVED_CCA_UPDATE state.");
                break;
            case SENT_CCR_TERMINATION:
                if (resultCode >= 2000 && resultCode < 3000) {
                    logger.info("(( $ )) Successfully terminated transaction at Online Charging Server (( $ ))");
                } else {
                    logger.error("(( $ )) Failure '" + resultCode + "' terminating transaction at Online Charging Server (( $ ))");
                }
                try {
                    String serviceContextId = ccrAvps.getAvp(461).getUTF8String().replaceAll(SERVICE_CONTEXT_DOMAIN, "");
                    ClientCCASession roSession = this.roSessions.remove(serviceContextId);
                    roSession.release();
                    roSession = null;
                } catch (Exception e) {
                    logger.error("(( $ )) Failure handling CCA at SENT_CCR_TERMINATION state. (( $ ))", e);
                }
                break;
            case RECEIVED_CCA_TERMINATION:
                logger.error("Unexpected CCA message at RECEIVED_CCA_TERMINATION state.");
                break;
            default:
                logger.error("Unexpected CCA message at UNKNOWN state.");
                break;
        }
    }

    public void doOtherEvent(AppSession session, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    }

    public void doReAuthRequest(ClientCCASession session, ReAuthRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    }

    public void doCreditControlRequest(ServerCCASession session, JCreditControlRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    }

    public void doReAuthAnswer(ServerCCASession session, ReAuthRequest request, ReAuthAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    }

    public void sessionSupervisionTimerExpired(ServerCCASession session) {
    }

    public void denyAccessOnTxExpire(ClientCCASession clientCCASessionImpl) {
    }

    public void txTimerExpired(ClientCCASession session) {
    }

    public Answer processRequest(Request request) {
        return null;
    }

    public void receivedSuccessMessage(Request request, Answer answer) {
    }

    private void setUpdateTimer(final String subscriptionId, final String serviceContextId, long ms) {
        Date timeToRun = new Date(System.currentTimeMillis() + ms);
        sendUpdatetimer.schedule(new TimerTask() {

            public void run() {
                try {
                    updateCharging(subscriptionId, serviceContextId, partialCallDurationCounter);
                } catch (Exception e) {
                    logger.error("(( $ )) Failure trying to create/send CCR (UPDATE) message. (( $ ))", e);
                }
            }
        }, timeToRun);
    }

    private void setTerminateTimer(final String subscriptionId, final String serviceContextId, long ms) {
        Date timeToRun = new Date(System.currentTimeMillis() + ms);
        sendTerminateTimer.schedule(new TimerTask() {

            public void run() {
                try {
                    stopCharging(subscriptionId, serviceContextId);
                } catch (Exception e) {
                    logger.error("(( $ )) Failure trying to create/send CCR (UPDATE) message. (( $ ))", e);
                }
            }
        }, timeToRun);
    }

    private void setCountCallTime(final String serviceContextId, long ms) {
        Date timeToRun = new Date(System.currentTimeMillis() + ms);
        callDurationTimer.schedule(new TimerTask() {

            public void run() {
                try {
                    totalCallDurationCounter++;
                    partialCallDurationCounter++;
                    Integer minutes = totalCallDurationCounter / 60;
                    Integer seconds = totalCallDurationCounter % 60;
                    String minutesStr = minutes > 9 ? String.valueOf(minutes) : "0" + String.valueOf(minutes);
                    String secondsStr = seconds > 9 ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
                    if (logger.isInfoEnabled()) {
                        logger.info("   o))) " + minutesStr + ":" + secondsStr + " (((o   ");
                    }
                } catch (Exception e) {
                    logger.error("(((o))) Failure keeping track of service time. (((o)))", e);
                }
            }
        }, timeToRun, 998);
    }

    private void switchStateMachine(int newState) {
        this.currentState = newState;
    }

    public void stateChanged(Object source, Enum oldState, Enum newState) {
        stateChanged(oldState, newState);
    }
}
