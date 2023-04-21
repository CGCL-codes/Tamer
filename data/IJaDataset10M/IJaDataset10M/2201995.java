package org.mobicents.servlet.sip.testsuite;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipApplicationSessionActivationListener;
import javax.servlet.sip.SipApplicationSessionAttributeListener;
import javax.servlet.sip.SipApplicationSessionBindingEvent;
import javax.servlet.sip.SipApplicationSessionBindingListener;
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
import javax.servlet.sip.SipSessionActivationListener;
import javax.servlet.sip.SipSessionAttributeListener;
import javax.servlet.sip.SipSessionBindingEvent;
import javax.servlet.sip.SipSessionBindingListener;
import javax.servlet.sip.SipSessionEvent;
import javax.servlet.sip.SipSessionListener;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import org.apache.log4j.Logger;

public class ListenersSipServlet extends SipServlet implements SipErrorListener, SipServletListener, SipSessionListener, SipSessionActivationListener, SipSessionBindingListener, SipApplicationSessionListener, SipApplicationSessionActivationListener, SipApplicationSessionBindingListener, SipSessionAttributeListener, SipApplicationSessionAttributeListener, TimerListener {

    private static final long serialVersionUID = 1L;

    private static transient Logger logger = Logger.getLogger(ListenersSipServlet.class);

    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    private static final String OK = "OK";

    private static final String KO = "KO";

    private static final String ATTRIBUTE = "attribute";

    private static final String VALUE = "value";

    private static final String NEW_VALUE = "new_value";

    private static final String NO_ACK_RECEIVED = "noAckReceived";

    private static final String NO_PRACK_RECEIVED = "noPrackReceived";

    private static final String SIP_SERVLET_INITIALIZED = "sipServletInitialized";

    private static final String SIP_SESSION_CREATED = "sipSessionCreated";

    private static final String SIP_SESSION_DESTROYED = "sipSessionDestroyed";

    private static final String SIP_APP_SESSION_VALUE_UNBOUND = "sipAppSessionValueUnbound";

    private static final String SIP_APP_SESSION_VALUE_BOUND = "sipAppSessionValueBound";

    private static final String SIP_APP_SESSION_ATTRIBUTE_REPLACED = "sipAppSessionAttributeReplaced";

    private static final String SIP_APP_SESSION_ATTRIBUTE_REMOVED = "sipAppSessionAttributeRemoved";

    private static final String SIP_APP_SESSION_ATTRIBUTE_ADDED = "sipAppSessionAttributeAdded";

    private static final String SIP_APP_SESSION_PASSIVATED = "sipAppSessionPassivated";

    private static final String SIP_APP_SESSION_ACTIVATED = "sipAppSessionActivated";

    private static final String SIP_APP_SESSION_EXPIRED = "sipAppSessionExpired";

    private static final String SIP_APP_SESSION_DESTROYED = "sipAppSessionDestroyed";

    private static final String SIP_APP_SESSION_CREATED = "sipAppSessionCreated";

    private static final String SIP_SESSION_VALUE_UNBOUND = "sipSessionValueUnbound";

    private static final String SIP_SESSION_VALUE_BOUND = "sipSessionValueBound";

    private static final String SIP_SESSION_ATTRIBUTE_REPLACED = "sipSessionAttributeReplaced";

    private static final String SIP_SESSION_ATTRIBUTE_REMOVED = "sipSessionAttributeRemoved";

    private static final String SIP_SESSION_ATTRIBUTE_ADDED = "sipSessionAttributeAdded";

    private static final String SIP_SESSION_PASSIVATED = "sipSessionPassivated";

    private static final String SIP_SESSION_ACTIVATED = "sipSessionActivated";

    @Resource
    private SipFactory sipFactory;

    /** Creates a new instance of ListenersSipServlet */
    public ListenersSipServlet() {
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        logger.info("the listeners test sip servlet has been started");
        try {
            Properties jndiProps = new Properties();
            Context initCtx = new InitialContext(jndiProps);
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            sipFactory = (SipFactory) envCtx.lookup("sip/org.mobicents.servlet.sip.testsuite.ListenersApplication/SipFactory");
            logger.info("Sip Factory ref from JNDI : " + sipFactory);
        } catch (NamingException e) {
            throw new ServletException("Uh oh -- JNDI problem !", e);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    protected void doInvite(SipServletRequest request) throws ServletException, IOException {
        logger.info("Got request: " + request.getMethod());
        SipServletResponse ringingResponse = request.createResponse(SipServletResponse.SC_RINGING);
        ringingResponse.send();
        SipServletResponse okResponse = request.createResponse(SipServletResponse.SC_OK);
        okResponse.send();
    }

    /**
	 * {@inheritDoc}
	 */
    protected void doBye(SipServletRequest request) throws ServletException, IOException {
        logger.info("Got BYE request: " + request);
        SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
        sipServletResponse.send();
        request.getSession().setAttribute("sipFactory", sipFactory);
        TimerService timerService = (TimerService) getServletContext().getAttribute(TIMER_SERVICE);
        timerService.createTimer(request.getApplicationSession(), 5000, false, null);
    }

    @Override
    protected void doMessage(SipServletRequest request) throws ServletException, IOException {
        logger.info("Got request: " + request.getMethod());
        SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
        sipServletResponse.send();
        processMessage(request);
    }

    /**
	 * 
	 * @param request
	 */
    private void processMessage(SipServletRequest request) {
        try {
            String message = (String) request.getContent();
            if (message != null && message.length() > 0) {
                SipServletRequest responseMessage = request.getSession().createRequest("MESSAGE");
                responseMessage.setContentLength(2);
                responseMessage.setContent(KO, CONTENT_TYPE);
                if (hasListenerBeenCalled(request, message)) {
                    responseMessage.setContent(OK, CONTENT_TYPE);
                }
                responseMessage.send();
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("the encoding is not supported", e);
        } catch (IOException e) {
            logger.error("an IO exception occured", e);
        }
    }

    /**
	 * Check if a particular listener has been called
	 * @param request the request the message has been received on 
	 * @param message the message containing the listener to check
	 * @return true if the listener has been called, flase otherwise
	 */
    private boolean hasListenerBeenCalled(SipServletRequest request, String message) {
        if (SIP_SESSION_CREATED.equals(message) && request.getSession().getAttribute(message) != null) {
            return true;
        } else if (SIP_SESSION_DESTROYED.equals(message)) {
            request.getSession().invalidate();
            if (request.getSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        }
        if (SIP_APP_SESSION_CREATED.equals(message) && request.getApplicationSession().getAttribute(message) != null) {
            return true;
        } else if (SIP_APP_SESSION_DESTROYED.equals(message)) {
            request.getApplicationSession().invalidate();
            if (request.getApplicationSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_SESSION_ATTRIBUTE_ADDED.equals(message) || SIP_SESSION_VALUE_BOUND.equals(message)) {
            request.getSession().setAttribute(ATTRIBUTE, VALUE);
            if (request.getSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_SESSION_ATTRIBUTE_REMOVED.equals(message) || SIP_SESSION_VALUE_UNBOUND.equals(message)) {
            request.getSession().setAttribute(ATTRIBUTE, VALUE);
            request.getSession().removeAttribute(ATTRIBUTE);
            if (request.getSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_SESSION_ATTRIBUTE_REPLACED.equals(message)) {
            request.getSession().setAttribute(ATTRIBUTE, VALUE);
            request.getSession().setAttribute(ATTRIBUTE, NEW_VALUE);
            if (request.getSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_APP_SESSION_ATTRIBUTE_ADDED.equals(message) || SIP_APP_SESSION_VALUE_BOUND.equals(message)) {
            request.getApplicationSession().setAttribute(ATTRIBUTE, VALUE);
            if (request.getApplicationSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_APP_SESSION_ATTRIBUTE_REMOVED.equals(message) || SIP_APP_SESSION_VALUE_UNBOUND.equals(message)) {
            request.getApplicationSession().setAttribute(ATTRIBUTE, VALUE);
            request.getApplicationSession().removeAttribute(ATTRIBUTE);
            if (request.getApplicationSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_APP_SESSION_ATTRIBUTE_REPLACED.equals(message)) {
            request.getApplicationSession().setAttribute(ATTRIBUTE, VALUE);
            request.getApplicationSession().setAttribute(ATTRIBUTE, NEW_VALUE);
            if (request.getApplicationSession().getAttribute(message) != null) {
                return true;
            } else {
                return false;
            }
        } else if (SIP_SERVLET_INITIALIZED.equals(message) && getServletContext().getAttribute(message) != null) {
            return true;
        }
        return false;
    }

    @Override
    protected void doRequest(SipServletRequest req) throws ServletException, IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        super.doRequest(req);
    }

    @Override
    protected void doResponse(SipServletResponse resp) throws ServletException, IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        super.doResponse(resp);
    }

    public void noAckReceived(SipErrorEvent ee) {
        logger.error("noAckReceived.");
        ee.getRequest().getSession().setAttribute(NO_ACK_RECEIVED, OK);
    }

    public void noPrackReceived(SipErrorEvent ee) {
        logger.info("noPrackReceived.");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        ee.getRequest().getSession().setAttribute(NO_PRACK_RECEIVED, OK);
    }

    public void servletInitialized(SipServletContextEvent ce) {
        logger.info("servlet initialized ");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        ce.getServletContext().setAttribute(SIP_SERVLET_INITIALIZED, OK);
    }

    public void sessionCreated(SipSessionEvent se) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        logger.info("sip session created " + se.getSession());
        se.getSession().setAttribute(SIP_SESSION_CREATED, OK);
    }

    public void sessionDestroyed(SipSessionEvent se) {
        logger.info("sip session destroyed " + se.getSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        SipApplicationSession sipApplicationSession = sipFactory.createApplicationSession();
        try {
            SipServletRequest sipServletRequest = sipFactory.createRequest(sipApplicationSession, "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
            SipURI sipUri = sipFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
            sipServletRequest.setRequestURI(sipUri);
            sipServletRequest.setContentLength(SIP_SESSION_DESTROYED.length());
            sipServletRequest.setContent(SIP_SESSION_DESTROYED, CONTENT_TYPE);
            sipServletRequest.send();
        } catch (ServletParseException e) {
            logger.error("Exception occured while parsing the addresses", e);
        } catch (IOException e) {
            logger.error("Exception occured while sending the request", e);
        }
    }

    public void sessionDidActivate(SipSessionEvent se) {
        logger.info("sip session activated " + se.getSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (se.getSession().isValid()) {
            se.getSession().setAttribute(SIP_SESSION_ACTIVATED, OK);
        }
    }

    public void sessionWillPassivate(SipSessionEvent se) {
        logger.info("sip session passivated " + se.getSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (se.getSession().isValid()) {
            se.getSession().setAttribute(SIP_SESSION_PASSIVATED, OK);
        }
    }

    public void attributeAdded(SipSessionBindingEvent ev) {
        logger.info("sip session attribute added " + ev.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_SESSION_ATTRIBUTE_ADDED.equals(ev.getName()) && !SIP_SESSION_VALUE_BOUND.equals(ev.getName()) && !SIP_SESSION_ATTRIBUTE_REPLACED.equals(ev.getName())) {
            if (ev.getSession().isValid()) {
                ev.getSession().setAttribute(SIP_SESSION_ATTRIBUTE_ADDED, OK);
            }
        }
    }

    public void attributeRemoved(SipSessionBindingEvent ev) {
        logger.info("sip session attribute removed " + ev.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_SESSION_ATTRIBUTE_REMOVED.equals(ev.getName()) && !SIP_SESSION_VALUE_UNBOUND.equals(ev.getName()) && !SIP_SESSION_ATTRIBUTE_REPLACED.equals(ev.getName())) {
            if (ev.getSession().isValid()) {
                ev.getSession().setAttribute(SIP_SESSION_ATTRIBUTE_REMOVED, OK);
            }
        }
    }

    public void attributeReplaced(SipSessionBindingEvent ev) {
        logger.info("sip session attribute removed " + ev.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_SESSION_ATTRIBUTE_REPLACED.equals(ev.getName())) {
            if (ev.getSession().isValid()) {
                ev.getSession().setAttribute(SIP_SESSION_ATTRIBUTE_REPLACED, OK);
            }
        }
    }

    public void valueBound(SipSessionBindingEvent event) {
        logger.info("sip session attribute bound " + event.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_SESSION_VALUE_BOUND.equals(event.getName()) && !SIP_SESSION_ATTRIBUTE_ADDED.equals(event.getName()) && !SIP_SESSION_ATTRIBUTE_REPLACED.equals(event.getName())) {
            if (event.getSession().isValid()) {
                event.getSession().setAttribute(SIP_SESSION_VALUE_BOUND, OK);
            }
        }
    }

    public void valueUnbound(SipSessionBindingEvent event) {
        logger.info("sip session attribute unbound " + event.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_SESSION_VALUE_UNBOUND.equals(event.getName()) && !SIP_SESSION_ATTRIBUTE_REMOVED.equals(event.getName()) && !SIP_SESSION_ATTRIBUTE_REPLACED.equals(event.getName())) {
            event.getSession().setAttribute(SIP_SESSION_VALUE_UNBOUND, OK);
        }
    }

    public void sessionCreated(SipApplicationSessionEvent ev) {
        logger.info("sip application session created " + ev.getApplicationSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (ev.getApplicationSession().isValid()) {
            ev.getApplicationSession().setAttribute(SIP_APP_SESSION_CREATED, OK);
        }
    }

    public void sessionDestroyed(SipApplicationSessionEvent ev) {
        logger.info("sip application session destroyed " + ev.getApplicationSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (sipFactory != null) {
            SipApplicationSession sipApplicationSession = sipFactory.createApplicationSession();
            try {
                SipServletRequest sipServletRequest = sipFactory.createRequest(sipApplicationSession, "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
                SipURI sipUri = sipFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
                sipServletRequest.setRequestURI(sipUri);
                sipServletRequest.setContentLength(SIP_APP_SESSION_DESTROYED.length());
                sipServletRequest.setContent(SIP_APP_SESSION_DESTROYED, CONTENT_TYPE);
                sipServletRequest.send();
            } catch (ServletParseException e) {
                logger.error("Exception occured while parsing the addresses", e);
            } catch (IOException e) {
                logger.error("Exception occured while sending the request", e);
            }
        }
    }

    public void sessionExpired(SipApplicationSessionEvent ev) {
        logger.info("sip application session expired " + ev.getApplicationSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (ev.getApplicationSession().isValid()) {
            ev.getApplicationSession().setAttribute(SIP_APP_SESSION_EXPIRED, OK);
        }
    }

    public void sessionDidActivate(SipApplicationSessionEvent se) {
        logger.info("sip application session activated " + se.getApplicationSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (se.getApplicationSession().isValid()) {
            se.getApplicationSession().setAttribute(SIP_APP_SESSION_ACTIVATED, OK);
        }
    }

    public void sessionWillPassivate(SipApplicationSessionEvent se) {
        logger.info("sip application session passivated " + se.getApplicationSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (se.getApplicationSession().isValid()) {
            se.getApplicationSession().setAttribute(SIP_APP_SESSION_PASSIVATED, OK);
        }
    }

    public void attributeAdded(SipApplicationSessionBindingEvent ev) {
        logger.info("sip application session attribute added " + ev.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_APP_SESSION_ATTRIBUTE_ADDED.equals(ev.getName()) && !SIP_APP_SESSION_VALUE_BOUND.equals(ev.getName()) && !SIP_APP_SESSION_ATTRIBUTE_REPLACED.equals(ev.getName())) {
            if (ev.getApplicationSession().isValid()) {
                ev.getApplicationSession().setAttribute(SIP_APP_SESSION_ATTRIBUTE_ADDED, OK);
            }
        }
    }

    public void attributeRemoved(SipApplicationSessionBindingEvent ev) {
        logger.info("sip application session attribute removed " + ev.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_APP_SESSION_ATTRIBUTE_REMOVED.equals(ev.getName()) && !SIP_APP_SESSION_VALUE_UNBOUND.equals(ev.getName()) && !SIP_APP_SESSION_ATTRIBUTE_REPLACED.equals(ev.getName())) {
            if (ev.getApplicationSession().isValid()) {
                ev.getApplicationSession().setAttribute(SIP_APP_SESSION_ATTRIBUTE_REMOVED, OK);
            }
        }
    }

    public void attributeReplaced(SipApplicationSessionBindingEvent ev) {
        logger.info("sip application session attribute replaced " + ev.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_APP_SESSION_ATTRIBUTE_REMOVED.equals(ev.getName()) && !SIP_APP_SESSION_VALUE_UNBOUND.equals(ev.getName()) && !SIP_APP_SESSION_ATTRIBUTE_REPLACED.equals(ev.getName())) {
            if (ev.getApplicationSession().isValid()) {
                ev.getApplicationSession().setAttribute(SIP_APP_SESSION_ATTRIBUTE_REPLACED, OK);
            }
        }
    }

    public void valueBound(SipApplicationSessionBindingEvent event) {
        logger.info("sip application session value bound " + event.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_APP_SESSION_VALUE_BOUND.equals(event.getName()) && !SIP_APP_SESSION_ATTRIBUTE_ADDED.equals(event.getName()) && !SIP_APP_SESSION_ATTRIBUTE_REPLACED.equals(event.getName())) {
            if (event.getApplicationSession().isValid()) {
                event.getApplicationSession().setAttribute(SIP_APP_SESSION_VALUE_BOUND, OK);
            }
        }
    }

    public void valueUnbound(SipApplicationSessionBindingEvent event) {
        logger.info("sip application session value unbound " + event.getName());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        if (!SIP_APP_SESSION_VALUE_UNBOUND.equals(event.getName()) && !SIP_APP_SESSION_ATTRIBUTE_REMOVED.equals(event.getName()) && !SIP_APP_SESSION_ATTRIBUTE_REPLACED.equals(event.getName())) {
            if (event.getApplicationSession().isValid()) {
                event.getApplicationSession().setAttribute(SIP_APP_SESSION_VALUE_UNBOUND, OK);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void timeout(ServletTimer timer) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        SipApplicationSession sipApplicationSession = timer.getApplicationSession();
        Iterator<SipSession> sipSessions = (Iterator<SipSession>) sipApplicationSession.getSessions("SIP");
        int nbSipSessions = 0;
        while (sipSessions.hasNext()) {
            sipSessions.next();
            nbSipSessions++;
        }
        logger.info("Number of sip sessions contained in the sip application " + "session to invalidate " + nbSipSessions);
        sipSessions = (Iterator<SipSession>) sipApplicationSession.getSessions("SIP");
        while (sipSessions.hasNext()) {
            SipSession sipSession = (SipSession) sipSessions.next();
            if (sipSession.isValid()) {
                sipSession.invalidate();
            }
        }
        if (sipApplicationSession.isValid()) {
            sipApplicationSession.invalidate();
        }
    }

    public void sessionReadyToInvalidate(SipSessionEvent se) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
    }

    public void sessionReadyToInvalidate(SipApplicationSessionEvent ev) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
    }
}
