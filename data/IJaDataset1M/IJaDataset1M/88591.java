package org.mobicents.servlet.sip.arquillian.showcase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.Address;
import javax.servlet.sip.AuthInfo;
import javax.servlet.sip.Parameterable;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipApplicationSessionEvent;
import javax.servlet.sip.SipApplicationSessionListener;
import javax.servlet.sip.SipErrorEvent;
import javax.servlet.sip.SipErrorListener;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletContextEvent;
import javax.servlet.sip.SipServletListener;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSession.State;
import javax.servlet.sip.SipSessionEvent;
import javax.servlet.sip.SipSessionListener;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.URI;
import javax.sip.ListeningPoint;
import javax.sip.header.AuthenticationInfoHeader;
import org.apache.log4j.Logger;
import org.mobicents.javax.servlet.sip.SipSessionExt;
import org.mobicents.servlet.sip.SipConnector;
import org.mobicents.servlet.sip.listener.SipConnectorListener;

@javax.servlet.sip.annotation.SipServlet(loadOnStartup = 1, applicationName = "org.mobicents.servlet.sip.arquillian.testsuite.SimpleApplication")
@javax.servlet.sip.annotation.SipListener
public class SimpleSipServlet extends SipServlet implements SipErrorListener, TimerListener, SipConnectorListener, SipSessionListener, SipApplicationSessionListener, SipServletListener {

    private static transient Logger logger = Logger.getLogger(SimpleSipServlet.class);

    private static final String TEST_EXCEPTION_ON_EXPIRE = "exceptionOnExpire";

    private static final String TEST_BYE_ON_EXPIRE = "byeOnExpire";

    private static final String CLONE_URI = "cloneURI";

    private static final long serialVersionUID = 1L;

    private static final String TEST_PRACK = "prack";

    private static final String TEST_ERROR_RESPONSE = "testErrorResponse";

    private static final String TEST_2X_ACK = "test2xACK";

    private static final String TEST_REGISTER_C_SEQ = "testRegisterCSeq";

    private static final String TEST_REGISTER_NO_CONTACT = "testRegisterNoContact";

    private static final String TEST_REGISTER_SAVED_SESSION = "testRegisterSavedSession";

    private static final String TEST_SUBSCRIBER_URI = "testSubscriberUri";

    private static final String TEST_FLAG_PARAM = "testFlagParameter";

    private static final String TEST_EXTERNAL_ROUTING = "testExternalRouting";

    private static final String TEST_EXTERNAL_ROUTING_NO_INFO = "testExternalRoutingNoInfo";

    private static final String TEST_NON_EXISTING_HEADER = "TestNonExistingHeader";

    private static final String TEST_NON_EXISTING_HEADER_LIST = "TestNonExistingHeaderList";

    private static final String TEST_ALLOW_HEADER = "TestAllowHeader";

    private static final String TEST_TO_TAG = "TestToTag";

    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    private static final String CANCEL_RECEIVED = "cancelReceived";

    private static final String SUBSCRIBER_URI = "sip:testSubscriberUri@sip-servlets.com";

    private static final String TEST_REINVITE_USERNAME = "reinvite";

    private static final String TEST_IS_SEND_REINVITE_USERNAME = "isendreinvite";

    private static final String TEST_IS_SEND_REINVITE_PRACK = "prackisendreinvite";

    private static final String TEST_IS_SIP_SERVLET_SEND_BYE = "SSsendBye";

    private static final String TEST_CANCEL_USERNAME = "cancel";

    private static final String TEST_BYE_ON_DESTROY = "testByeOnDestroy";

    private static final String TEST_NO_ACK_RECEIVED = "noAckReceived";

    private static final String TEST_SYSTEM_HEADER_MODIFICATION = "systemHeaderModification";

    private static final String TEST_SERIALIZATION = "serialization";

    @Resource
    SipFactory sipFactory;

    @Resource
    TimerService timerService;

    SipSession registerSipSession;

    SipSession inviteSipSession;

    int timeout = 15000;

    @Override
    protected void doBranchResponse(SipServletResponse resp) throws ServletException, IOException {
        resp.getApplicationSession().setAttribute("doBranchResponse", "true");
        super.doBranchResponse(resp);
    }

    /** Creates a new instance of SimpleProxyServlet */
    public SimpleSipServlet() {
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        logger.info("the simple sip servlet has been started");
        new File("expirationFailure.tmp").delete();
        super.init(servletConfig);
    }

    /**
	 * {@inheritDoc}
	 */
    protected void doInvite(SipServletRequest request) throws ServletException, IOException {
        String vvv = request.getHeaders("Via").toString();
        URI requestURI = request.getRequestURI();
        logger.info("request URI : " + requestURI);
        String fromString = request.getFrom().toString();
        if (fromString.contains(TEST_NO_ACK_RECEIVED)) {
            request.createResponse(SipServletResponse.SC_OK).send();
            return;
        }
        logger.info("from : " + fromString);
        logger.info("Got request: " + request.getMethod());
        if (fromString.contains(TEST_SYSTEM_HEADER_MODIFICATION)) {
            SipServletResponse res = request.createResponse(SipServletResponse.SC_OK);
            Address contact = res.getAddressHeader("Contact");
            SipURI uri = (SipURI) contact.getURI();
            try {
                uri.setHost("foo.com");
                logger.error("can modify host of the Contact URI, this shouldn't be allowed");
                request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR).send();
                return;
            } catch (IllegalArgumentException e) {
            }
            try {
                SipURI from = (SipURI) res.getAddressHeader("From").getURI();
                from.setHost("bar.com");
                logger.error("can modify host of the From URI, this shouldn't be allowed");
                request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR).send();
                return;
            } catch (IllegalArgumentException e) {
            }
            res.send();
            return;
        }
        if (fromString.contains(CLONE_URI)) {
            URI from = request.getFrom().getURI();
            SipURI clonedFrom = (SipURI) from.clone();
            for (final Iterator<String> parameterNames = from.getParameterNames(); parameterNames.hasNext(); ) clonedFrom.removeParameter(parameterNames.next());
        }
        if (fromString.contains(TEST_SYSTEM_HEADER_MODIFICATION)) {
            SipServletResponse res = request.createResponse(SipServletResponse.SC_OK);
            Address contact = res.getAddressHeader("Contact");
            SipURI uri = (SipURI) contact.getURI();
            try {
                uri.setHost("foo.com");
                logger.error("can modify host of the Contact URI, this shouldn't be allowed");
                request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR).send();
                return;
            } catch (IllegalArgumentException e) {
            }
            try {
                SipURI from = (SipURI) res.getAddressHeader("From").getURI();
                from.setHost("bar.com");
                logger.error("can modify host of the From URI, this shouldn't be allowed");
                request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR).send();
                return;
            } catch (IllegalArgumentException e) {
            }
            res.send();
            return;
        }
        request.setAttribute("test", "test");
        String requestAttribute = (String) request.getAttribute("test");
        request.removeAttribute("test");
        if (!requestAttribute.equalsIgnoreCase("test")) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
            sipServletResponse.send();
            return;
        }
        if (!request.getApplicationSession().getInvalidateWhenReady()) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
            sipServletResponse.send();
            return;
        }
        if (request.getParameterableHeader("additionalParameterableHeader") != null) {
            request.getParameterableHeader("additionalParameterableHeader").setParameter("dsfds", "value");
            boolean error = false;
            try {
                request.getParameterableHeader("nonParameterableHeader").setParameter("dsfds", "value");
            } catch (ServletParseException e) {
                error = true;
            }
            if (error) {
                request.createResponse(200).send();
            }
            return;
        }
        request.createResponse(SipServletResponse.SC_TRYING).send();
        if (fromString.contains(TEST_BYE_ON_DESTROY)) {
            inviteSipSession = request.getSession();
        }
        if (fromString.contains(TEST_BYE_ON_EXPIRE)) {
            inviteSipSession = request.getSession();
            inviteSipSession.setAttribute(TEST_BYE_ON_EXPIRE, true);
        }
        if (fromString.contains(TEST_EXCEPTION_ON_EXPIRE)) {
            inviteSipSession = request.getSession();
            inviteSipSession.setAttribute(TEST_EXCEPTION_ON_EXPIRE, true);
        }
        if (fromString.contains(TEST_ERROR_RESPONSE)) {
            request.getApplicationSession().setAttribute(TEST_ERROR_RESPONSE, "true");
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_BUSY_HERE);
            sipServletResponse.send();
            return;
        }
        if (fromString.contains(TEST_EXTERNAL_ROUTING_NO_INFO)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            timerService.createTimer(request.getApplicationSession(), 1000, false, (Serializable) sipServletResponse);
            return;
        }
        if (fromString.contains(TEST_EXTERNAL_ROUTING)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            sipServletResponse.send();
            sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            timerService.createTimer(request.getApplicationSession(), 1000, false, (Serializable) sipServletResponse);
            return;
        }
        if (fromString.contains(TEST_ALLOW_HEADER)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_METHOD_NOT_ALLOWED);
            sipServletResponse.setHeader("Allow", "INVITE, ACK, CANCEL, OPTIONS, BYE");
            sipServletResponse.setHeader("Allow", "INVITE, ACK, CANCEL, OPTIONS, BYE");
            sipServletResponse.addHeader("Allow", "SUBSCRIBE, NOTIFY");
            sipServletResponse.addHeader("Allow", "REFER");
            sipServletResponse.send();
            return;
        }
        logger.info("Subscriber URI received : " + request.getSubscriberURI());
        if (fromString.contains(TEST_SUBSCRIBER_URI) && !SUBSCRIBER_URI.equalsIgnoreCase(request.getSubscriberURI().toString())) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
            sipServletResponse.send();
            return;
        }
        if (fromString.contains(TEST_TO_TAG)) {
            Address fromAddress = request.getFrom();
            Address toAddress = sipFactory.createAddress(request.getRequestURI(), request.getTo().getDisplayName());
            SipServletRequest newRequest = sipFactory.createRequest(request.getApplicationSession(), "INVITE", fromAddress, toAddress);
            if (newRequest.getTo().getParameter("tag") != null) {
                logger.error("the ToTag should be empty, sending 500 response");
                SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                sipServletResponse.send();
                return;
            }
        }
        if (fromString.contains("RemotePartyId")) {
            Address remotePID = request.getAddressHeader("Remote-Party-ID");
            Address remotePID2 = request.getAddressHeader("Remote-Party-ID2");
            Address remotePID3 = request.getAddressHeader("Remote-Party-ID3");
            Address remotePID4 = request.getAddressHeader("Remote-Party-ID4");
            Address remotePID5 = request.getAddressHeader("Remote-Party-ID5");
            boolean sendErrorResponse = false;
            logger.info("Remote-Party-ID value is " + remotePID);
            logger.info("Remote-Party-ID2 value is " + remotePID2);
            logger.info("Remote-Party-ID3 value is " + remotePID3);
            logger.info("Remote-Party-ID4 value is " + remotePID4);
            logger.info("Remote-Party-ID5 value is " + remotePID5);
            if (!remotePID.toString().trim().equals("\"KATE SMITH\" <sip:4162375543@47.135.223.88;user=phone>; screen=yes; party=calling; privacy=off")) {
                sendErrorResponse = true;
                logger.info("Remote-Party-ID Sending Error Response ");
            }
            if (!remotePID2.toString().trim().equals("sip:4162375543@47.135.223.88;user=phone; screen=yes; party=calling; privacy=off")) {
                sendErrorResponse = true;
                logger.info("Remote-Party-ID2 Sending Error Response ");
            }
            if (!remotePID3.toString().trim().equals("<sip:4162375543@47.135.223.88;user=phone>; screen=yes; party=calling; privacy=off")) {
                sendErrorResponse = true;
                logger.info("Remote-Party-ID3 Sending Error Response ");
            }
            if (!remotePID4.toString().trim().equals("\"KATE SMITH\" <sip:4162375543@47.135.223.88>; screen=yes; party=calling; privacy=off")) {
                sendErrorResponse = true;
                logger.info("Remote-Party-ID4 Sending Error Response ");
            }
            if (!remotePID5.toString().trim().equals("<sip:4162375543@47.135.223.88>; screen=yes; party=calling; privacy=off")) {
                sendErrorResponse = true;
                logger.info("Remote-Party-ID5 Sending Error Response ");
            }
            if (sendErrorResponse) {
                SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                sipServletResponse.send();
                return;
            }
        }
        request.getAddressHeader(TEST_NON_EXISTING_HEADER);
        request.getHeader(TEST_NON_EXISTING_HEADER);
        request.getHeaders(TEST_NON_EXISTING_HEADER);
        request.setHeader(TEST_NON_EXISTING_HEADER, "true");
        request.removeHeader(TEST_NON_EXISTING_HEADER);
        request.addHeader(TEST_NON_EXISTING_HEADER, "true");
        request.addHeader(TEST_NON_EXISTING_HEADER_LIST, "true,false,maybe");
        request.getParameterableHeader("Reply-To");
        request.getParameterableHeaders("Reply-To");
        if (fromString.contains(TEST_REGISTER_C_SEQ)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            sipServletResponse.send();
            sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            sipServletResponse.send();
            sendRegister(null, false);
            return;
        }
        if (fromString.contains(TEST_REGISTER_NO_CONTACT)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            sipServletResponse.send();
            sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            sipServletResponse.send();
            sendRegister(null, true);
            return;
        }
        if (fromString.contains(TEST_REGISTER_SAVED_SESSION)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            sipServletResponse.send();
            sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            sipServletResponse.send();
            sendRegister();
            return;
        }
        if (fromString.contains(TEST_PRACK)) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            if (fromString.contains("require-present")) {
                sipServletResponse.addHeader("Require", "100rel");
            }
            sipServletResponse.sendReliably();
            sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            sipServletResponse.getSession().setAttribute("okResponse", sipServletResponse);
            return;
        }
        if (!TEST_CANCEL_USERNAME.equalsIgnoreCase(((SipURI) request.getFrom().getURI()).getUser())) {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            if (fromString.contains(TEST_SERIALIZATION)) {
                FileOutputStream fos = new FileOutputStream("val.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(request);
                oos.close();
                fos.close();
                FileInputStream fis = new FileInputStream("val.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                try {
                    SipServletRequest deserializedRequest = (SipServletRequest) ois.readObject();
                    if (!deserializedRequest.toString().equals(request.toString())) {
                        logger.error("deserializedRequest " + deserializedRequest + " different from received request " + request);
                        sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                        sipServletResponse.send();
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    ois.close();
                    fis.close();
                }
                fos = new FileOutputStream("val.ser");
                oos = new ObjectOutputStream(fos);
                oos.writeObject(sipServletResponse);
                oos.close();
                fos.close();
                fis = new FileInputStream("val.ser");
                ois = new ObjectInputStream(fis);
                try {
                    SipServletResponse deserializedResponse = (SipServletResponse) ois.readObject();
                    if (!deserializedResponse.toString().equals(sipServletResponse.toString())) {
                        logger.error("deserializedResponse " + deserializedResponse + " different from response " + sipServletResponse);
                        sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                        sipServletResponse.send();
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    ois.close();
                    fis.close();
                }
            }
            if (sipServletResponse.getParameterableHeader("Contact") == null) {
                logger.error("the Contact Header on a non 3xx or 485 response should be set");
                sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                sipServletResponse.send();
            }
            sipServletResponse.send();
            sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            if (fromString.contains(TEST_FLAG_PARAM)) {
                try {
                    sipServletResponse.setHeader("Contact", "sip:" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5070");
                    logger.error("an IllegalArgumentException should be thrown when trying to set the Contact Header on a 2xx response");
                    sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                    sipServletResponse.send();
                    return;
                } catch (IllegalArgumentException e) {
                    logger.info("Contact Header is not set-able for the 2XX response to an INVITE");
                }
                Parameterable contact = sipServletResponse.getParameterableHeader("Contact");
                contact.setParameter("flagparam", "");
                String contactStringified = contact.toString().trim();
                logger.info("Contact Header with flag param " + contactStringified);
                if (contactStringified.endsWith("flagparam=")) {
                    logger.error("the flagParam should not contains the equals followed by empty, it is a flag so no equals sign should be present, sending 500 response");
                    sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                    sipServletResponse.send();
                    return;
                }
                contact.setParameter("flagparam", null);
                contactStringified = contact.toString().trim();
                logger.info("Contact Header with flag param " + contactStringified);
                if (contactStringified.endsWith("flagparam")) {
                    logger.error("the flagParam should have been removed when setting its value to null, sending 500 response");
                    sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                    sipServletResponse.send();
                    return;
                }
                contact = sipFactory.createParameterable("sip:user@" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080;flagparam");
                contactStringified = contact.toString().trim();
                logger.info("Contact Header with flag param " + contactStringified);
                if (contactStringified.endsWith("flagparam=")) {
                    logger.error("the flagParam should not contains the equals followed by empty, it is a flag so no equals sign should be present, sending 500 response");
                    sipServletResponse = request.createResponse(SipServletResponse.SC_SERVER_INTERNAL_ERROR);
                    sipServletResponse.send();
                    return;
                }
            }
            sipServletResponse.getAddressHeader(TEST_NON_EXISTING_HEADER);
            sipServletResponse.getHeader(TEST_NON_EXISTING_HEADER);
            sipServletResponse.getHeaders(TEST_NON_EXISTING_HEADER);
            sipServletResponse.setHeader(TEST_NON_EXISTING_HEADER, "true");
            sipServletResponse.removeHeader(TEST_NON_EXISTING_HEADER);
            sipServletResponse.addHeader(TEST_NON_EXISTING_HEADER, "true");
            sipServletResponse.addHeader(TEST_NON_EXISTING_HEADER_LIST, "true,false,maybe");
            sipServletResponse.getParameterableHeader("Reply-To");
            sipServletResponse.send();
        } else {
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_RINGING);
            sipServletResponse.send();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("unexpected exception while waiting ", e);
            }
        }
    }

    @Override
    protected void doPrack(SipServletRequest req) throws ServletException, IOException {
        req.createResponse(SipServletResponse.SC_OK).send();
        SipServletResponse okResponseToInvite = (SipServletResponse) req.getSession().getAttribute("okResponse");
        okResponseToInvite.send();
    }

    @Override
    protected void doAck(SipServletRequest req) throws ServletException, IOException {
        if (req.getFrom().getURI() instanceof SipURI) {
            if (TEST_REINVITE_USERNAME.equalsIgnoreCase(((SipURI) req.getFrom().getURI()).getUser())) {
                SipServletRequest reInvite = req.getSession(false).createRequest("INVITE");
                if (reInvite.getHeader("Contact").contains("0.0.0.0")) {
                    logger.error("Reinvite doesn't add correct address. We must not see 0.0.0.0 here");
                    return;
                }
                if (req.getSession(false) == reInvite.getSession(false)) {
                    reInvite.send();
                } else {
                    logger.error("the newly created subsequent request doesn't have " + "the same session instance as the one it has been created from");
                }
            } else if (TEST_IS_SEND_REINVITE_USERNAME.equalsIgnoreCase(((SipURI) req.getFrom().getURI()).getUser()) || TEST_IS_SEND_REINVITE_PRACK.equalsIgnoreCase(((SipURI) req.getFrom().getURI()).getUser())) {
                Integer nbOfAcks = (Integer) req.getSession().getAttribute("nbAcks");
                if (nbOfAcks == null) {
                    nbOfAcks = Integer.valueOf(1);
                } else {
                    nbOfAcks = Integer.valueOf(nbOfAcks.intValue() + 1);
                }
                req.getSession().setAttribute("nbAcks", nbOfAcks);
            } else if (TEST_IS_SIP_SERVLET_SEND_BYE.equalsIgnoreCase(((SipURI) req.getFrom().getURI()).getUser())) {
                timerService.createTimer(req.getApplicationSession(), timeout, false, (Serializable) req.getSession());
            } else if (TEST_EXCEPTION_ON_EXPIRE.equalsIgnoreCase(((SipURI) req.getFrom().getURI()).getUser())) {
                try {
                    Thread.sleep(1000);
                    SipServletRequest r = req.getSession().createRequest("INVITE");
                    r.send();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        String fromString = req.getFrom().toString();
        if (fromString.contains(TEST_ERROR_RESPONSE) || fromString.contains(TEST_2X_ACK)) {
            sendMessage(req.getApplicationSession(), sipFactory, "ackReceived", null);
            return;
        }
        if (getServletContext().getInitParameter("changeKeepAliveTimeout") != null) {
            timerService.createTimer(sipFactory.createApplicationSession(), Long.valueOf(getServletContext().getInitParameter("timeout")), false, "" + req.getInitialRemotePort());
        }
    }

    @Override
    protected void doSuccessResponse(SipServletResponse resp) throws ServletException, IOException {
        final String fromString = resp.getFrom().toString();
        if (resp.getMethod().equalsIgnoreCase("REGISTER") && !fromString.contains(TEST_REGISTER_NO_CONTACT)) {
            int cseq = Integer.parseInt(resp.getRequest().getHeader("CSeq").substring(0, 1));
            if (cseq < 4) {
                SipApplicationSession appSession = sipFactory.createApplicationSession();
                timerService.createTimer(appSession, 15000L, false, (Serializable) resp);
            }
            return;
        }
        if (!"BYE".equalsIgnoreCase(resp.getMethod()) && !"MESSAGE".equalsIgnoreCase(resp.getMethod())) {
            resp.createAck().send();
            resp.getSession(false).createRequest("BYE").send();
        }
    }

    @Override
    protected void doErrorResponse(SipServletResponse response) throws ServletException, IOException {
        SipFactory sipFactory = (SipFactory) getServletContext().getAttribute(SIP_FACTORY);
        if (response.getStatus() == SipServletResponse.SC_UNAUTHORIZED || response.getStatus() == SipServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED) {
            if (!"true".equals(getServletContext().getAttribute("FirstResponseRecieved"))) {
                getServletContext().setAttribute("FirstResponseRecieved", "true");
                AuthInfo authInfo = sipFactory.createAuthInfo();
                authInfo.addAuthInfo(response.getStatus(), "sip-servlets-realm", "user", "pass");
                SipServletRequest challengeRequest = null;
                String fromString = response.getFrom().toString();
                if (fromString.contains(TEST_REGISTER_SAVED_SESSION)) {
                    challengeRequest = registerSipSession.createRequest(response.getMethod());
                } else {
                    challengeRequest = response.getSession().createRequest(response.getMethod());
                }
                challengeRequest.addAuthHeader(response, authInfo);
                challengeRequest.send();
            }
        }
        logger.info("Got response: " + response);
    }

    /**
	 * {@inheritDoc}
	 */
    protected void doBye(SipServletRequest request) throws ServletException, IOException {
        int statusCode = SipServletResponse.SC_OK;
        logger.info("Got BYE request: " + request);
        if (TEST_IS_SEND_REINVITE_USERNAME.equalsIgnoreCase(((SipURI) request.getFrom().getURI()).getUser())) {
            Integer nbOfAcks = (Integer) request.getSession().getAttribute("nbAcks");
            if (nbOfAcks == null || nbOfAcks.intValue() != 2) {
                logger.error("Number of ACK seen " + nbOfAcks + " sending Error Response");
                statusCode = SipServletResponse.SC_DECLINE;
            }
        }
        SipServletResponse sipServletResponse = request.createResponse(statusCode);
        if (!"true".equals(request.getApplicationSession().getAttribute("doBranchResponse"))) sipServletResponse.send();
    }

    @Override
    protected void doCancel(SipServletRequest request) throws ServletException, IOException {
        logger.info("Got CANCEL request: " + request);
        SipFactory sipFactory = (SipFactory) getServletContext().getAttribute(SIP_FACTORY);
        try {
            SipServletRequest sipServletRequest = sipFactory.createRequest(sipFactory.createApplicationSession(), "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
            SipURI sipUri = sipFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
            sipServletRequest.setRequestURI(sipUri);
            sipServletRequest.setContentLength(CANCEL_RECEIVED.length());
            sipServletRequest.setContent(CANCEL_RECEIVED, CONTENT_TYPE);
            sipServletRequest.send();
        } catch (ServletParseException e) {
            logger.error("Exception occured while parsing the addresses", e);
        } catch (IOException e) {
            logger.error("Exception occured while sending the request", e);
        }
    }

    @Override
    protected void doRegister(SipServletRequest req) throws ServletException, IOException {
        Address contact = req.getAddressHeader("Contact");
        contact.setExpires(3600);
        logger.info("REGISTER Contact Address.toString = " + contact.toString());
        int response = SipServletResponse.SC_OK;
        if (!("<sip:sender@" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080;transport=udp;lr>;expires=3600").equals(contact.toString())) {
            response = SipServletResponse.SC_SERVER_INTERNAL_ERROR;
        }
        SipServletResponse resp = req.createResponse(response);
        if (req.getFrom().toString().contains("authenticationInfoHeader")) {
            resp = req.createResponse(SipServletResponse.SC_UNAUTHORIZED);
            resp.addHeader(AuthenticationInfoHeader.NAME, "NTLM rspauth=\"01000000000000005CD422F0C750C7C6\",srand=\"0B9D33A2\",snum=\"1\",opaque=\"BCDC0C9D\",qop=\"auth\",targetname=\"server.contoso.com\",realm=\"SIP Communications Service\"");
        }
        resp.send();
    }

    @Override
    protected void doInfo(SipServletRequest req) throws ServletException, IOException {
        String content = (String) req.getContent();
        int response = SipServletResponse.SC_OK;
        if (content != null) {
            req.getSession().setAttribute("mutable", content);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!content.equals(req.getSession().getAttribute("mutable"))) response = SipServletResponse.SC_SERVER_INTERNAL_ERROR;
        } else {
            SipURI outboundInterface = (SipURI) sipFactory.createURI("sip:" + req.getInitialRemoteAddr() + ":" + req.getLocalPort() + ";transport=" + req.getTransport());
            ((SipSessionExt) req.getSession()).setOutboundInterface(outboundInterface);
        }
        SipServletResponse resp = req.createResponse(response);
        resp.send();
    }

    /**
	 * {@inheritDoc}
	 */
    public void noAckReceived(SipErrorEvent ee) {
        logger.error("noAckReceived.");
        sendMessage(ee.getRequest().getApplicationSession(), sipFactory, "noAckReceived", null);
    }

    /**
	 * {@inheritDoc}
	 */
    public void noPrackReceived(SipErrorEvent ee) {
        logger.error("noPrackReceived.");
    }

    public void timeout(ServletTimer timer) {
        Serializable info = timer.getInfo();
        if (info instanceof SipServletResponse) {
            SipServletResponse sipServletResponse = (SipServletResponse) timer.getInfo();
            if (sipServletResponse.getMethod().equals("REGISTER")) {
                if (sipServletResponse.getSession().getState().equals(State.INITIAL)) {
                    try {
                        sendRegister(sipServletResponse.getSession(), false);
                    } catch (ServletParseException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    sipServletResponse.send();
                } catch (IOException e) {
                    logger.error("Unexpected exception while sending the OK", e);
                }
            }
        } else if (info instanceof SipSession) {
            try {
                ((SipSession) info).createRequest("BYE").send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (info instanceof String) {
            String port = (String) info;
            SipConnector[] sipConnectors = (SipConnector[]) getServletContext().getAttribute("org.mobicents.servlet.sip.SIP_CONNECTORS");
            for (SipConnector sipConnector : sipConnectors) {
                if (sipConnector.getIpAddress().equals(System.getProperty("org.mobicents.testsuite.testhostaddr")) && sipConnector.getPort() == 5070 && sipConnector.getTransport().equalsIgnoreCase("TCP")) {
                    try {
                        boolean changed = sipConnector.setKeepAliveTimeout(System.getProperty("org.mobicents.testsuite.testhostaddr"), Integer.valueOf(port), Long.valueOf(getServletContext().getInitParameter("changeKeepAliveTimeout")));
                        logger.info("SipConnector timeoutvalue changed " + getServletContext().getInitParameter("changeKeepAliveTimeout") + " changed " + changed + "for " + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":" + Integer.valueOf(port));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendRegister() throws ServletParseException, IOException {
        SipServletRequest register = null;
        if (registerSipSession != null) {
            logger.info("saved session instance : " + registerSipSession);
            logger.info("session attribute is : " + registerSipSession.getAttribute("attribute"));
            register = registerSipSession.createRequest("REGISTER");
        } else {
            SipApplicationSession app = sipFactory.createApplicationSession();
            register = sipFactory.createRequest(app, "REGISTER", "sip:testRegisterSavedSession@simple-servlet.com", "sip:you@" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5058");
            Parameterable contact = sipFactory.createParameterable("sip:john@" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":6090;expires=900");
            register.addParameterableHeader("Contact", contact, true);
            registerSipSession = register.getSession();
            logger.info("saved session instance : " + registerSipSession);
            registerSipSession.setAttribute("attribute", "value");
        }
        register.setHeader("Expires", "3600");
        register.setHeader("test", "test");
        register.send();
    }

    private void sendRegister(SipSession sipSession, boolean removeContact) throws ServletParseException, IOException {
        SipServletRequest register = null;
        if (sipSession != null) {
            register = sipSession.createRequest("REGISTER");
        } else {
            SipApplicationSession app = sipFactory.createApplicationSession();
            register = sipFactory.createRequest(app, "REGISTER", "sip:testRegisterCSeq@simple-servlet.com", "sip:you@" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5058");
            Parameterable contact = sipFactory.createParameterable("sip:john@" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":6090;expires=900");
            register.addParameterableHeader("Contact", contact, true);
        }
        register.setHeader("Expires", "3600");
        register.setHeader("test", "test");
        if (removeContact) {
            register.removeHeader("Contact");
        }
        register.send();
    }

    public void sipConnectorAdded(SipConnector connector) {
        logger.info(connector + " added");
        if (connector.getTransport().equalsIgnoreCase(ListeningPoint.TCP) && connector.getPort() == 5072) {
            sendMessage(sipFactory.createApplicationSession(), sipFactory, "sipConnectorAdded", ListeningPoint.UDP);
            sendMessage(sipFactory.createApplicationSession(), sipFactory, "sipConnectorAdded", connector.getTransport());
        } else if (connector.getPort() != 5072) {
            sendMessage(sipFactory.createApplicationSession(), sipFactory, "sipConnectorAdded", connector.getTransport());
        }
    }

    public void sipConnectorRemoved(SipConnector connector) {
        logger.info(connector + " removed");
        sendMessage(sipFactory.createApplicationSession(), sipFactory, "sipConnectorRemoved", connector.getTransport());
    }

    /**
	 * @param sipApplicationSession
	 * @param storedFactory
	 */
    public static void sendMessage(SipApplicationSession sipApplicationSession, SipFactory storedFactory, String content, String transport) {
        try {
            SipServletRequest sipServletRequest = storedFactory.createRequest(sipApplicationSession, "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
            sipServletRequest.addHeader("Ext", "Test 1, 2 ,3");
            SipURI sipUri = storedFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
            if (transport != null) {
                if (transport.equalsIgnoreCase(ListeningPoint.TCP)) {
                    sipUri = storedFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5081");
                }
                sipUri.setTransportParam(transport);
            }
            sipServletRequest.setRequestURI(sipUri);
            sipServletRequest.setContentLength(content.length());
            sipServletRequest.setContent(content, CONTENT_TYPE);
            sipServletRequest.send();
        } catch (ServletParseException e) {
            logger.error("Exception occured while parsing the addresses", e);
        } catch (IOException e) {
            logger.error("Exception occured while sending the request", e);
        }
    }

    @Override
    public void destroy() {
        if (inviteSipSession != null && inviteSipSession.isValid()) {
            try {
                inviteSipSession.createRequest("BYE").send();
            } catch (IOException e) {
                logger.error("Exception occured while sending the request", e);
            }
        }
        super.destroy();
    }

    public void sessionCreated(SipSessionEvent se) {
    }

    public void sessionDestroyed(SipSessionEvent se) {
    }

    public void sessionReadyToInvalidate(SipSessionEvent se) {
        if (se.getSession().getApplicationSession().getAttribute(TEST_ERROR_RESPONSE) != null) {
            sendMessage(sipFactory.createApplicationSession(), sipFactory, "sipSessionReadyToInvalidate", null);
        }
    }

    public void sessionCreated(SipApplicationSessionEvent ev) {
    }

    public void sessionDestroyed(SipApplicationSessionEvent ev) {
    }

    public void sessionExpired(SipApplicationSessionEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("Distributable Simple Servlet: sip app session " + event.getApplicationSession().getId() + " expired");
        }
        Iterator<SipSession> sipSessionsIt = (Iterator<SipSession>) event.getApplicationSession().getSessions("SIP");
        if (sipSessionsIt.hasNext()) {
            SipSession sipSession = sipSessionsIt.next();
            if (sipSession != null && sipSession.getAttribute(TEST_BYE_ON_EXPIRE) != null) {
                if (sipSession != null && sipSession.isValid() && !State.TERMINATED.equals(sipSession.getState())) {
                    try {
                        sipSession.createRequest("BYE").send();
                    } catch (IOException e) {
                        logger.error("An unexpected exception occured while sending the BYE", e);
                    }
                }
            }
            if (sipSession != null && sipSession.getAttribute(TEST_EXCEPTION_ON_EXPIRE) != null) {
                Integer expirations = (Integer) sipSession.getAttribute("expirations");
                if (expirations == null) expirations = 1;
                sipSession.setAttribute("expirations", expirations + 1);
                if (expirations > 1) {
                    logger.fatal("TOO MANY EXPIRATIONS");
                    try {
                        new File("expirationFailure.tmp").createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                throw new IllegalStateException();
            }
        }
    }

    public void sessionReadyToInvalidate(SipApplicationSessionEvent ev) {
        if (ev.getApplicationSession().getAttribute(TEST_ERROR_RESPONSE) != null) {
            sendMessage(sipFactory.createApplicationSession(), sipFactory, "sipAppSessionReadyToInvalidate", null);
        }
    }

    public void servletInitialized(SipServletContextEvent ce) {
        String byeDelayString = getServletContext().getInitParameter("byeDelay");
        if (byeDelayString != null) {
            timeout = Integer.parseInt(byeDelayString);
        }
    }

    @Override
    public void onKeepAliveTimeout(SipConnector connector, String peerAddress, int peerPort) {
        logger.error("SipConnector " + connector + " remotePeer " + peerAddress + ":" + peerPort);
        sendMessage(sipFactory.createApplicationSession(), sipFactory, "shootme onKeepAliveTimeout", "tcp");
    }
}
