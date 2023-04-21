package com.liveims.webims.sip;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.sip.SipApplicationSessionEvent;
import javax.servlet.sip.SipApplicationSessionListener;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletRequest;
import org.apache.log4j.Logger;
import com.alcatel.as.session.distributed.SessionException;
import com.liveims.webims.session.IMSManager;
import com.liveims.webims.util.WimsMessage;

public class SipListener extends SipServlet implements SipApplicationSessionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5302072397475985799L;

    private static final Logger log = Logger.getLogger(SipListener.class);

    static IMSManager manager = IMSManager.getInstance();

    @Override
    protected void doMessage(SipServletRequest req) throws ServletException, IOException {
        req.createResponse(200).send();
        String sid = req.getTo().getURI().toString();
        log.debug(sid);
        if (IMSManager.hasSession(sid)) {
            log.debug("session existe, tell the manager about new message");
            try {
                manager.updateSessionAttribute(sid, IMSManager.IM_ATTRIBUTE, new WimsMessage(req));
            } catch (SessionException e) {
                e.printStackTrace();
                log.error("fail to set IM attribute", e);
            }
        }
    }

    @Override
    protected void doNotify(SipServletRequest req) throws ServletException, IOException {
        String sid = req.getFrom().getURI().toString();
        try {
            manager.updateSessionAttribute(sid, IMSManager.Presence_ATTRIBUTE, new WimsMessage(req));
        } catch (SessionException e) {
            e.printStackTrace();
            log.error("fail to set Presence attribute", e);
        }
    }

    @Override
    protected void doRequest(SipServletRequest req) throws ServletException, IOException {
        log.info("Receive SipRequest, " + req.getMethod() + req.getFrom());
        super.doRequest(req);
    }

    public void sessionCreated(SipApplicationSessionEvent arg0) {
    }

    public void sessionDestroyed(SipApplicationSessionEvent arg0) {
    }

    public void sessionExpired(SipApplicationSessionEvent event) {
        event.getApplicationSession().setExpires(3600);
    }
}
