package org.mobicents.servlet.sip.testsuite;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.SipURI;
import org.apache.log4j.Logger;

public class ReplacesReceiverSipServlet extends SipServlet {

    private static final long serialVersionUID = 1L;

    private static final String REPLACES = "Replaces";

    private static transient Logger logger = Logger.getLogger(ReplacesReceiverSipServlet.class);

    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";

    @Resource
    private SipFactory sipFactory;

    @Resource
    private SipSessionsUtil sipSessionsUtil;

    /** Creates a new instance of ReplacesSenderSipServlet */
    public ReplacesReceiverSipServlet() {
    }

    @Override
    protected void doInvite(SipServletRequest request) throws ServletException, IOException {
        logger.info("Got request:\n" + request.toString());
        SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
        if ("receiver".equalsIgnoreCase(((SipURI) request.getFrom().getURI()).getUser()) && request.getHeader(REPLACES) == null) {
            sipServletResponse = request.createResponse(SipServletResponse.SC_DECLINE);
        } else if ("receiver".equalsIgnoreCase(((SipURI) request.getFrom().getURI()).getUser()) && request.getHeader(REPLACES) != null) {
            SipSession sipSession = sipSessionsUtil.getCorrespondingSipSession(request.getSession(), REPLACES);
            if (sipSession == null) {
                sipServletResponse = request.createResponse(SipServletResponse.SC_DECLINE);
            }
            request.getSession().setAttribute("ReplacesInviteReceived", Boolean.TRUE);
        }
        sipServletResponse.send();
    }

    @Override
    protected void doAck(SipServletRequest request) throws ServletException, IOException {
        if (request.getSession().getAttribute("ReplacesInviteReceived") == null) {
            String callId = request.getHeader("Call-ID");
            String fromTag = request.getFrom().getParameter("tag");
            String toTag = request.getTo().getParameter("tag");
            String messageContent = "Replaces : " + callId + "; from-tag=" + fromTag + "; to-tag=" + toTag;
            SipApplicationSession sipApplicationSession = sipFactory.createApplicationSession();
            SipURI fromURI = sipFactory.createSipURI("receiver", "sip-servlets.com");
            SipURI requestURI = sipFactory.createSipURI("receiver", "" + System.getProperty("org.mobicents.testsuite.testhostaddr") + ":5090");
            SipServletRequest sipServletRequest = sipFactory.createRequest(sipApplicationSession, "MESSAGE", fromURI, request.getFrom().getURI());
            sipServletRequest.setContentLength(messageContent.length());
            sipServletRequest.setContent(messageContent, CONTENT_TYPE);
            sipServletRequest.setRequestURI(requestURI);
            sipServletRequest.send();
        } else {
            SipSession sipSession = request.getSession();
            sipSession.createRequest("BYE").send();
            SipSession replacedSession = sipSessionsUtil.getCorrespondingSipSession(sipSession, REPLACES);
            replacedSession.createRequest("BYE").send();
        }
    }

    @Override
    protected void doSuccessResponse(SipServletResponse resp) throws ServletException, IOException {
        logger.info("Got Success Response : " + resp);
        if (!"BYE".equalsIgnoreCase(resp.getMethod())) {
            resp.createAck().send();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    protected void doBye(SipServletRequest request) throws ServletException, IOException {
        if (request.getSession().getAttribute("replacesInvite") != null) {
            logger.info("Got BYE request: " + request);
            SipServletResponse sipServletResponse = request.createResponse(SipServletResponse.SC_OK);
            sipServletResponse.send();
        }
    }
}
