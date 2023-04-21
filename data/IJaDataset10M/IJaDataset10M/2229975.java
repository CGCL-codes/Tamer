package org.mobicents.servlet.sip.testsuite;

import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipApplicationSessionEvent;
import javax.servlet.sip.SipApplicationSessionListener;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import org.apache.log4j.Logger;

/**
 * 
 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A> 
 *
 */
public class TimersSipServlet extends SipServlet implements SipApplicationSessionListener, TimerListener {

    private static final long serialVersionUID = 1L;

    private static final String RECURRING_TIME = "recurringTime";

    private static final String RECURRING = "recurring";

    private static final String ALREADY_EXTENDED = "alreadyExtended";

    private static final String ALREADY_INVALIDATED = "alreadyInvalidated";

    private static transient Logger logger = Logger.getLogger(TimersSipServlet.class);

    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    private static final String SIP_APP_SESSION_EXPIRED = "sipAppSessionExpired";

    private static final String SIP_APP_SESSION_READY_TO_BE_INVALIDATED = "sipAppSessionReadyToBeInvalidated";

    private static final String TIMER_EXPIRED = "timerExpired";

    private static final String RECURRING_TIMER_EXPIRED = "recurringTimerExpired";

    private SipFactory sipFactory;

    /** Creates a new instance of TimersSipServlet */
    public TimersSipServlet() {
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        logger.info("the timers test sip servlet has been started");
        try {
            Properties jndiProps = new Properties();
            Context initCtx = new InitialContext(jndiProps);
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            sipFactory = (SipFactory) envCtx.lookup("sip/org.mobicents.servlet.sip.testsuite.TimersApplication/SipFactory");
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
        SipServletResponse okResponse = request.createResponse(SipServletResponse.SC_OK);
        okResponse.send();
        request.getApplicationSession().setAttribute("sipFactory", sipFactory);
        request.getApplicationSession().setInvalidateWhenReady(true);
        String fromString = request.getFrom().toString();
        if (fromString.contains("checkReload")) {
            TimerService timerService = (TimerService) getServletContext().getAttribute(TIMER_SERVICE);
            timerService.createTimer(request.getApplicationSession(), 1000, false, null);
        } else if (fromString.contains("expExtInDialog")) {
            request.getApplicationSession().setAttribute("expExtInDialog", "true");
            request.getApplicationSession().setAttribute("sipSessionId", request.getSession().getId());
        } else {
            TimerService timerService = (TimerService) getServletContext().getAttribute(TIMER_SERVICE);
            timerService.createTimer(request.getApplicationSession(), 1000, false, null);
            timerService.createTimer(request.getApplicationSession(), 3000, 3000, true, false, RECURRING);
            request.getApplicationSession().setAttribute(RECURRING_TIME, Integer.valueOf(0));
        }
    }

    @Override
    protected void doSuccessResponse(SipServletResponse resp) throws ServletException, IOException {
        if ("MESSAGE".equals(resp.getMethod())) {
            resp.getSession().invalidate();
        } else if ("INVITE".equals(resp.getMethod())) {
            resp.createAck().send();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    protected void doBye(SipServletRequest request) throws ServletException, IOException {
        logger.info("Got BYE request: " + request);
        SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
        sipServletResponse.send();
    }

    public void sessionExpired(SipApplicationSessionEvent ev) {
        logger.info("sip application session expired " + ev.getApplicationSession());
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        boolean sendMessage = true;
        if (ev.getApplicationSession().getAttribute("expExtInDialog") == null) {
            if (!ev.getApplicationSession().isReadyToInvalidate() && ev.getApplicationSession().getAttribute(ALREADY_EXTENDED) == null) {
                logger.info("extending lifetime of sip app session" + ev.getApplicationSession());
                ev.getApplicationSession().setExpires(1);
                ev.getApplicationSession().setAttribute(ALREADY_EXTENDED, Boolean.TRUE);
            }
        } else {
            if (ev.getApplicationSession().getAttribute("reInviteSent") == null) {
                SipSession sipSession = ev.getApplicationSession().getSipSession((String) ev.getApplicationSession().getAttribute("sipSessionId"));
                ev.getApplicationSession().setAttribute("reInviteSent", "true");
                try {
                    sipSession.createRequest("INVITE").send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendMessage = false;
            }
        }
        if (sendMessage) {
            SipFactory storedFactory = (SipFactory) ev.getApplicationSession().getAttribute("sipFactory");
            try {
                SipServletRequest sipServletRequest = storedFactory.createRequest(ev.getApplicationSession(), "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
                SipURI sipUri = storedFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
                sipServletRequest.setRequestURI(sipUri);
                sipServletRequest.setContentLength(SIP_APP_SESSION_EXPIRED.length());
                sipServletRequest.setContent(SIP_APP_SESSION_EXPIRED, CONTENT_TYPE);
                sipServletRequest.send();
            } catch (ServletParseException e) {
                logger.error("Exception occured while parsing the addresses", e);
            } catch (IOException e) {
                logger.error("Exception occured while sending the request", e);
            }
        }
    }

    public void sessionCreated(SipApplicationSessionEvent ev) {
        logger.info("sip application session created " + ev.getApplicationSession());
    }

    public void sessionDestroyed(SipApplicationSessionEvent ev) {
        logger.info("sip application session destroyed " + ev.getApplicationSession());
    }

    public void timeout(ServletTimer timer) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        SipApplicationSession sipApplicationSession = timer.getApplicationSession();
        Integer recurringTime = (Integer) sipApplicationSession.getAttribute(RECURRING_TIME);
        if (recurringTime != null) {
            logger.info("timer expired " + timer.getId() + " , info " + timer.getInfo() + " , recurring Time " + recurringTime.intValue());
        } else {
            logger.info("timer expired " + timer.getId() + " , info " + timer.getInfo());
        }
        SipFactory storedFactory = (SipFactory) sipApplicationSession.getAttribute("sipFactory");
        if (timer.getInfo() == null) {
            sendMessage(sipApplicationSession, storedFactory, TIMER_EXPIRED);
        } else {
            int temp = recurringTime.intValue() + 1;
            sipApplicationSession.setAttribute(RECURRING_TIME, Integer.valueOf(temp));
            if (temp > 2) {
                sendMessage(sipApplicationSession, storedFactory, RECURRING_TIMER_EXPIRED);
                timer.cancel();
            }
        }
    }

    /**
	 * @param sipApplicationSession
	 * @param storedFactory
	 */
    private void sendMessage(SipApplicationSession sipApplicationSession, SipFactory storedFactory, String content) {
        try {
            SipServletRequest sipServletRequest = storedFactory.createRequest(sipApplicationSession, "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
            SipURI sipUri = storedFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
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

    public void sessionReadyToInvalidate(SipApplicationSessionEvent ev) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.getClass().getSimpleName().equals("WebappClassLoader")) {
            logger.error("ClassLoader " + cl);
            throw new IllegalArgumentException("Bad Context Classloader : " + cl);
        }
        logger.info("sessionReadyToInvalidate called");
        if (ev.getApplicationSession().getAttribute(ALREADY_INVALIDATED) == null) {
            ev.getApplicationSession().setAttribute(ALREADY_INVALIDATED, Boolean.TRUE);
            SipApplicationSession sipApplicationSession = ev.getApplicationSession();
            SipFactory storedFactory = (SipFactory) sipApplicationSession.getAttribute("sipFactory");
            try {
                SipServletRequest sipServletRequest = storedFactory.createRequest(sipApplicationSession, "MESSAGE", "sip:sender@sip-servlets.com", "sip:receiver@sip-servlets.com");
                SipURI sipUri = storedFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5080");
                sipServletRequest.setRequestURI(sipUri);
                sipServletRequest.setContentLength(SIP_APP_SESSION_READY_TO_BE_INVALIDATED.length());
                sipServletRequest.setContent(SIP_APP_SESSION_READY_TO_BE_INVALIDATED, CONTENT_TYPE);
                sipServletRequest.send();
            } catch (ServletParseException e) {
                logger.error("Exception occured while parsing the addresses", e);
            } catch (IOException e) {
                logger.error("Exception occured while sending the request", e);
            }
        }
    }
}
