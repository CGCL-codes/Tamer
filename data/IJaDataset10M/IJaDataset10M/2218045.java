package com.liveims.webims;

import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import com.alcatel.as.service.concurrent.PlatformExecutor;
import com.alcatel.as.service.concurrent.PlatformExecutors;
import com.alcatel.sip.client.SipClientFactory;
import com.alcatel.sip.client.SipMessage;
import com.alcatel.sip.client.publish.PublishActivationEvent;
import com.alcatel.sip.client.publish.PublishClient;
import com.alcatel.sip.client.publish.PublishClientListener;
import com.alcatel.sip.client.publish.PublishTerminationEvent;
import com.alcatel.sip.client.subscribe.NotifyEvent;
import com.alcatel.sip.client.subscribe.SubscribeActivationEvent;
import com.alcatel.sip.client.subscribe.SubscribeClient;
import com.alcatel.sip.client.subscribe.SubscribeClientListener;
import com.alcatel.sip.client.subscribe.SubscribeTerminationEvent;
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
import com.nextenso.proxylet.http.HttpResponseProlog;

public class PresenceProxylet implements BufferedHttpRequestProxylet, SubscribeClientListener, PublishClientListener {

    private ProxyletContext _context;

    private static final Logger log = Logger.getLogger(PresenceProxylet.class);

    private static PlatformExecutors executors = PlatformExecutors.getInstance();

    RestPath restPath;

    public int doRequest(HttpRequest request) throws ProxyletException {
        HttpRequestProlog reqProlog = request.getProlog();
        String _impu = restPath.getIMPU();
        final String user_sipuri = "sip:" + _impu;
        if (reqProlog.getMethod().equalsIgnoreCase("GET")) {
            try {
                PlatformExecutor currentexecu = executors.getExecutor(PlatformExecutors.CURRENT_EXECUTOR);
                log.debug("***Current thread ID = " + currentexecu.getId());
                SubscribeClient subclient = SipClientFactory.getInstance().newSubscribeClient("presence", user_sipuri, user_sipuri, this);
                subclient.setFrom(user_sipuri);
                subclient.setRoute(Settings.getInstance().valueOf(Settings.Attribute.S_CSCF_PROPERTY));
                subclient.attach(request);
                subclient.activate(0);
                currentexecu = executors.getExecutor(PlatformExecutors.CURRENT_EXECUTOR);
                log.debug("***Current thread ID after sending SIP MES= " + currentexecu.getId());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                log.debug("got exception when sending subscribe " + e);
            }
        } else if (reqProlog.getMethod().equalsIgnoreCase("PUT")) {
        }
        return SUSPEND;
    }

    public int accept(HttpRequestProlog prolog, HttpHeaders headers) {
        String path = prolog.getURL().getPath();
        restPath = new RestPath(path);
        if (restPath.isForPresence()) return ACCEPT; else return IGNORE;
    }

    public void destroy() {
    }

    public String getProxyletInfo() {
        return null;
    }

    public void init(ProxyletConfig cnf) throws ProxyletException {
        _context = cnf.getProxyletContext();
    }

    public void clientActivated(SubscribeClient client, SubscribeActivationEvent event) {
        log.info("***Received SIP subscribe response: " + event.getSipMessage());
        log.debug("***subscribe client activated");
    }

    public void clientNotified(SubscribeClient client, NotifyEvent event) {
        log.info("****Received SIP Notify response");
        HttpRequest req = (HttpRequest) client.attachment();
        HttpResponse resp = req.getResponse();
        HttpResponseProlog respProlog = resp.getProlog();
        SipMessage notif = event.getSipMessage();
        log.debug("***" + notif.getContentType());
        log.debug("***" + new String(notif.getContent()));
        respProlog.setStatus(200);
        respProlog.setReason("OK");
        resp.getHeaders().setContentType(notif.getContentType());
        resp.getBody().setContent(new String(notif.getContent()));
        client.attach(req);
    }

    public void clientTerminated(SubscribeClient client, SubscribeTerminationEvent event) {
        HttpRequest req = (HttpRequest) client.attachment();
        _context.resume(req, RESPOND_FIRST_PROXYLET);
        log.debug("******proxylet resumes");
        PlatformExecutor currentexecu = executors.getExecutor(PlatformExecutors.CURRENT_EXECUTOR);
        log.debug("***Current thread ID after sending SIP MES= " + currentexecu.getId());
    }

    public void clientActivated(PublishClient client, PublishActivationEvent event) {
    }

    public void clientTerminated(PublishClient client, PublishTerminationEvent event) {
    }
}
