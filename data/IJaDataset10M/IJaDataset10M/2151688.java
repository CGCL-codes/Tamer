package com.liveims.webims;

import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import com.alcatel.sip.client.SipClientFactory;
import com.alcatel.sip.client.message.MessageClient;
import com.alcatel.sip.client.message.MessageClientListener;
import com.alcatel.sip.client.message.MessageDocument;
import com.alcatel.sip.client.message.MessageEvent;
import com.liveims.webims.session.IMSManager;
import com.liveims.webims.util.RestPath;
import com.liveims.webims.util.Settings;
import com.nextenso.proxylet.ProxyletConfig;
import com.nextenso.proxylet.ProxyletContext;
import com.nextenso.proxylet.ProxyletException;
import com.nextenso.proxylet.http.BufferedHttpRequestProxylet;
import com.nextenso.proxylet.http.HttpHeaders;
import com.nextenso.proxylet.http.HttpRequest;
import com.nextenso.proxylet.http.HttpRequestProlog;
import com.nextenso.proxylet.http.HttpResponse;

public class IMProxylet implements BufferedHttpRequestProxylet, MessageClientListener {

    private ProxyletContext _context;

    private String sid;

    private static final Logger log = Logger.getLogger(IMProxylet.class);

    RestPath restPath;

    public int doRequest(HttpRequest request) throws ProxyletException {
        String _impu = restPath.getIMPU();
        final String from = "sip:" + _impu;
        final String to = request.getProlog().getParameter("to");
        final String content = request.getProlog().getParameter("content");
        try {
            MessageDocument msg = new Msg(content);
            MessageClient mc = SipClientFactory.getInstance().newMessageClient(to, this);
            mc.setFrom(from);
            mc.setRoute("sip:192.168.231.7:5060");
            mc.attach(request);
            mc.sendMessage(msg);
            log.debug("message sent to " + to);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.debug("got exception when sending subscribe " + e);
        }
        return SUSPEND;
    }

    public int accept(HttpRequestProlog prolog, HttpHeaders headers) {
        String path = prolog.getURL().getPath();
        restPath = new RestPath(path);
        log.debug("receiving request to " + path);
        if (headers.getCookie("sessionid") != null) {
            log.debug("session ID " + headers.getCookie("sessionid").getValue());
            this.sid = headers.getCookie("sessionid").getValue();
            if (IMSManager.hasSession(sid)) return ACCEPT;
        }
        log.info("Don't handle the  request");
        return IGNORE;
    }

    public void destroy() {
    }

    public String getProxyletInfo() {
        return null;
    }

    public void init(ProxyletConfig cnf) throws ProxyletException {
        _context = cnf.getProxyletContext();
    }

    public void messageCompleted(MessageClient client, MessageEvent event) {
        log.info("Message completed: client=" + client + ": event=[success=" + event.isSuccess() + ", info=" + event.toString() + ", message=" + event.getSipMessage() + ", document=" + event.getMessageDocument());
        HttpRequest req = (HttpRequest) client.attachment();
        HttpResponse resp = req.getResponse();
        if (event.isSuccess()) {
            resp.getProlog().setStatus(200);
            resp.getProlog().setReason("ok");
        } else {
            resp.getProlog().setStatus(500);
            resp.getProlog().setReason("server error");
        }
        _context.resume(req, RESPOND_LAST_PROXYLET);
        log.info("Http resumes");
    }

    public static class Msg implements MessageDocument {

        private String _s;

        Msg(String s) {
            _s = s;
        }

        public String getContentType() {
            return "text/plain";
        }

        public byte[] toByteArray() {
            return _s.getBytes();
        }

        public String toString() {
            return _s;
        }
    }
}
