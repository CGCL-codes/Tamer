package org.mobicents.servlet.sip.arquillian.showcase;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipApplicationSession;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletContextEvent;
import javax.servlet.sip.SipServletListener;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.sip.message.Request;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:gvagenas@gmail.com">George Vagenas</a>
 * 
 */
@javax.servlet.sip.annotation.SipServlet(loadOnStartup = 1, applicationName = "LifecycleSimpleApplication")
@javax.servlet.sip.annotation.SipListener
public class LifecycleSipServlet extends SipServlet implements SipServletListener {

    private final Logger logger = Logger.getLogger(LifecycleSipServlet.class);

    @Resource
    SipFactory sipFactory;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        logger.info("the sip servlet has been started");
        super.init(servletConfig);
    }

    @Override
    protected void doSuccessResponse(SipServletResponse resp) throws ServletException, IOException {
        logger.info("Got success response: " + resp.toString());
        super.doSuccessResponse(resp);
    }

    @Override
    protected void doBye(SipServletRequest req) throws ServletException, IOException {
        logger.info("Got BYE request");
        SipServletResponse resp = req.createResponse(200);
        resp.send();
    }

    @Override
    public void servletInitialized(SipServletContextEvent ce) {
        if (sipFactory == null) sipFactory = (SipFactory) ce.getServletContext().getAttribute(SIP_FACTORY);
        SipApplicationSession sipApplicationSession = sipFactory.createApplicationSession();
        javax.servlet.sip.Address fromAddr = null;
        javax.servlet.sip.Address toAddr = null;
        SipURI requestURI = sipFactory.createSipURI("LittleGuy", "127.0.0.1:5080");
        String myValueParam = ce.getServletContext().getInitParameter("myParam");
        String myValueParam2 = ce.getServletContext().getInitParameter("myParam2");
        String concurrencyControMode = ce.getServletContext().getAttribute("org.mobicents.servlet.sip.annotation.ConcurrencyControlMode").toString();
        try {
            fromAddr = sipFactory.createAddress("sip:BigGuy@here.com");
            toAddr = sipFactory.createAddress("sip:LittleGuy@there.com");
        } catch (ServletParseException e) {
            e.printStackTrace();
        }
        SipServletRequest request = sipFactory.createRequest(sipApplicationSession, Request.INVITE, fromAddr, toAddr);
        request.setRequestURI(requestURI);
        if (myValueParam != null) request.addHeader("Additional-Header", myValueParam);
        if (myValueParam2 != null) request.addHeader("Additional-Header2", myValueParam2);
        if (concurrencyControMode != null) request.addHeader("ConcurrencyControlMode", concurrencyControMode);
        try {
            request.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
