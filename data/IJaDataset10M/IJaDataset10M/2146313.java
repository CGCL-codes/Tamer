package com.sesca.sip.presence;

import local.net.KeepAliveSip;
import local.ua.RegisterAgentListener;
import org.zoolu.net.SocketAddress;
import org.zoolu.sip.address.*;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.header.*;
import org.zoolu.sip.message.*;
import org.zoolu.sip.transaction.TransactionClient;
import org.zoolu.sip.transaction.TransactionClientListener;
import org.zoolu.sip.authentication.DigestAuthentication;
import org.zoolu.sip.dialog.SubscriberDialog;
import org.zoolu.sip.dialog.SubscriberDialogListener;
import org.zoolu.tools.Log;
import org.zoolu.tools.LogLevel;
import com.sesca.misc.Logger;
import com.sesca.sip.presence.pidf.Presentity;
import com.sesca.sip.presence.pidf.SimpleParser;
import com.sesca.sip.presence.pidf.Tuple;
import com.sesca.voip.ua.AppletUANG;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class PresenceAgent implements Runnable, SubscriberDialogListener, TransactionClientListener, PublishSchedulerListener {

    PresenceAgentListener listener;

    SipProvider sip_provider;

    NameAddress target;

    String username;

    String realm;

    String authName;

    String passwd;

    String next_nonce;

    String qop;

    NameAddress contact;

    int expire_time;

    int renew_time;

    boolean loop;

    boolean running = false;

    Log log;

    int attempts;

    SubscriberDialog subscriberDialog = null;

    String currentPresenceStatus = "";

    String currentPresenceNote = "";

    boolean published = false;

    HashMap<String, Presentity> presentities = null;

    Hashtable dialogs = null;

    int publishExpireTime = 0;

    long publishedTime = 0;

    long initialTime = 0;

    static int hysteresis = 30;

    PublishScheduler ps;

    public PresenceAgent(SipProvider sip_provider, String contact_url, PresenceAgentListener listener) {
        init(sip_provider, contact_url, listener);
    }

    public PresenceAgent(SipProvider sip_provider, String contact_url, String username, String realm, String passwd, PresenceAgentListener listener) {
        init(sip_provider, contact_url, listener);
        this.username = username;
        this.realm = realm;
        this.passwd = passwd;
        this.authName = username;
    }

    public PresenceAgent(SipProvider sip_provider, String contact_url, String username, String authName, String realm, String passwd, PresenceAgentListener listener) {
        init(sip_provider, contact_url, listener);
        this.username = username;
        this.realm = realm;
        this.passwd = passwd;
        this.authName = authName;
    }

    private void init(SipProvider sip_provider, String contact_url, PresenceAgentListener listener) {
        this.listener = listener;
        this.sip_provider = sip_provider;
        this.log = sip_provider.getLog();
        this.contact = new NameAddress(contact_url);
        this.expire_time = SipStack.default_expires;
        this.renew_time = 0;
        this.running = false;
        this.username = null;
        this.realm = null;
        this.passwd = null;
        this.next_nonce = null;
        this.qop = null;
        this.attempts = 0;
        presentities = new HashMap();
        dialogs = new Hashtable();
        initialTime = System.currentTimeMillis();
    }

    public void subscribe(int expireTime, String to) {
        if (to == null || to.length() == 0) return;
        boolean newDialog = true;
        if (expireTime < 0) expireTime = SipStack.default_expires;
        if (dialogs.containsKey(to)) {
            subscriberDialog = (SubscriberDialog) dialogs.get(to);
            if (subscriberDialog == null) subscriberDialog = new SubscriberDialog(sip_provider, "presence", null, this); else newDialog = false;
        } else {
            if (expireTime == 0) return; else subscriberDialog = new SubscriberDialog(sip_provider, "presence", null, this);
        }
        if (newDialog) {
            String from = username + "@" + realm;
            subscriberDialog.subscribe(to, to, from, contact.toString(), expireTime, username, passwd, realm);
            dialogs.put(to, subscriberDialog);
            Presentity prs = new Presentity(to, Presentity.statusPending);
            if (presentities.containsKey(to)) presentities.remove(to);
            presentities.put(to, prs);
            Logger.debug("to presentities: " + to);
        } else {
            subscriberDialog.reSubscribe(expireTime);
            dialogs.put(to, subscriberDialog);
            Presentity prs;
            if (presentities.containsKey(to)) prs = presentities.get(to); else prs = new Presentity(to, Presentity.statusPending);
            prs.setStatus(Presentity.statusPending);
            presentities.put(to, prs);
            Logger.debug("to presentities: " + to);
        }
        if (expireTime == 0) {
            Presentity prs = null;
            if (presentities.containsKey(to)) prs = presentities.get(to);
            if (prs != null) {
                prs.setStatus(Presentity.statusCancelled);
                presentities.put(to, prs);
            }
        }
        onPresenceUpdate();
    }

    public void publish(String status, String note, int expireTime) {
        publish(status, note, expireTime, false);
    }

    public void publish(String status, String note, int expireTime, boolean forced) {
        if (expireTime < hysteresis && expireTime > 0) expireTime += hysteresis;
        boolean statusChanged = false;
        status = status.toLowerCase().trim();
        note = note.trim();
        if (!status.equals("open") && !status.equals("closed")) {
            Logger.error("Illegal presence status in pidf");
            return;
        }
        if (!status.equals(currentPresenceStatus) || !note.equals(currentPresenceNote)) statusChanged = true;
        if (!statusChanged && !forced) {
            Logger.debug("Presence has not changed");
            return;
        }
        currentPresenceStatus = status;
        currentPresenceNote = note;
        if (expireTime < 0) expireTime = SipStack.default_expires;
        String from = username + "@" + realm;
        MessageFactory msgf = new MessageFactory();
        Message req = msgf.createPublishRequest(sip_provider, new NameAddress(from), "presence");
        req.setExpiresHeader(new ExpiresHeader(expireTime));
        String tupleId = sip_provider.UAIdentity.replace("/", "").replace(":", "").toLowerCase();
        String entity = "sip:" + username + "@" + realm;
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<presence xmlns=\"urn:ietf:params:xml:ns:pidf\"" + "entity=\"" + entity + "\">" + "<tuple id=\"" + tupleId + "\">" + "<status>" + "<basic>" + status + "</basic>" + "</status>";
        if (note != "" && note.length() > 0) xml += "<note>" + note + "</note>";
        xml += "</tuple>" + "</presence>";
        req.setBody("application/pidf+xml", xml);
        TransactionClient t = new TransactionClient(sip_provider, req, this);
        t.request();
        publishExpireTime = expireTime;
        publishedTime = System.currentTimeMillis();
        published = false;
        run();
    }

    /** Run method */
    public void run() {
        ps = new PublishScheduler(this);
        ps.init(publishedTime, publishExpireTime, hysteresis);
        ps.start();
    }

    @Override
    public void onDlgNotify(SubscriberDialog dialog, NameAddress target, NameAddress notifier, NameAddress contact, String state, String content_type, String body, Message msg) {
        String n = null;
        if (notifier != null) n = (notifier.getAddress().toString());
        boolean fail = true;
        if (content_type != null && content_type.toLowerCase().equals("application/pidf+xml")) fail = false;
        if (!presentities.containsKey(n)) fail = true;
        if (!fail && body != null && state.trim().toLowerCase().equals("active")) {
            Logger.debug("pa.onDlgNotify sucksess");
            SimpleParser p = new SimpleParser();
            p.parse(body);
            Vector<Tuple> tuples = p.getTuples();
            Presentity prs = new Presentity(n, Presentity.statusActive);
            for (int i = 0; i < tuples.size(); i++) {
                Tuple t = tuples.elementAt(i);
                prs.addTuple(t.getId(), t);
            }
            presentities.put(n, prs);
            onPresenceUpdate();
        } else {
            Logger.debug("pa.onDlgNotify fails");
            if (body == null && presentities.containsKey(n)) {
                Logger.debug("body is empty");
                if (state.trim().toLowerCase().equals("active")) {
                    Logger.debug("new active subscription");
                    Presentity prs = new Presentity(n, Presentity.statusActive);
                    presentities.put(n, prs);
                } else {
                    Logger.debug("subscription not active");
                }
            } else Logger.debug("presentity not in list");
            onPresenceUpdate();
            return;
        }
    }

    @Override
    public void onDlgSubscribeTimeout(SubscriberDialog dialog) {
        System.out.println("PresenceAgent.onDlgSubscribeTimeout");
    }

    @Override
    public void onDlgSubscriptionFailure(SubscriberDialog dialog, int code, String reason, Message msg) {
        String key = (msg.getToHeader().getNameAddress().toString()).replace("<", "").replace(">", "");
        if (dialogs.containsValue(dialog)) {
            if (dialogs.containsKey(key) && dialogs.get(key) == dialog) {
                dialogs.remove(key);
            }
        }
        if (presentities.containsKey(key)) presentities.remove(key);
        onPresenceUpdate();
    }

    @Override
    public void onDlgSubscriptionSuccess(SubscriberDialog dialog, int code, String reason, Message msg) {
        String s = msg.getToHeader().getNameAddress().toString().replace("<", "").replace(">", "");
        onPresenceUpdate();
    }

    @Override
    public void onDlgSubscriptionTerminated(SubscriberDialog dialog) {
        String to = null;
        Iterator it = dialogs.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            if (dialogs.get(key).equals(dialog)) {
                to = key;
                break;
            }
        }
        if (to != null) {
            Presentity prs = presentities.get(to);
            if (prs != null) {
                if (prs.getStatus().equals(Presentity.statusCancelled)) {
                    presentities.remove(to);
                    dialogs.remove(to);
                    dialog = null;
                } else {
                    prs.setStatus(Presentity.statusExpired);
                    presentities.put(to, prs);
                    dialogs.remove(to);
                    dialog = null;
                    subscribe(3600, to);
                }
            }
        }
        onPresenceUpdate();
    }

    private boolean parse(String xml) {
        int start = xml.indexOf("<basic>");
        int end = xml.indexOf("</basic>");
        String status = xml.substring(start + 7, end);
        return true;
    }

    @Override
    public void onTransFailureResponse(TransactionClient tc, Message resp) {
        String method = tc.getTransactionMethod();
        StatusLine status_line = resp.getStatusLine();
        int code = status_line.getCode();
        if ((code == 401 && resp.hasWwwAuthenticateHeader() && resp.getWwwAuthenticateHeader().getRealmParam().equalsIgnoreCase(realm)) || (code == 407 && resp.hasProxyAuthenticateHeader() && resp.getProxyAuthenticateHeader().getRealmParam().equalsIgnoreCase(realm))) {
            Message req = tc.getRequestMessage();
            req.setCSeqHeader(req.getCSeqHeader().incSequenceNumber());
            WwwAuthenticateHeader wah;
            if (code == 401) wah = resp.getWwwAuthenticateHeader(); else wah = resp.getProxyAuthenticateHeader();
            String qop_options = wah.getQopOptionsParam();
            qop = (qop_options != null) ? "auth" : null;
            RequestLine rl = req.getRequestLine();
            DigestAuthentication digest = new DigestAuthentication(rl.getMethod(), rl.getAddress().toString(), wah, null, null, authName, passwd);
            AuthorizationHeader ah;
            if (code == 401) ah = digest.getAuthorizationHeader(); else ah = digest.getProxyAuthorizationHeader();
            req.setAuthorizationHeader(ah);
            tc = new TransactionClient(sip_provider, req, this);
            tc.request();
        }
    }

    @Override
    public void onTransProvisionalResponse(TransactionClient tc, Message resp) {
    }

    @Override
    public void onTransSuccessResponse(TransactionClient tc, Message resp) {
        published = true;
    }

    @Override
    public void onTransTimeout(TransactionClient tc) {
    }

    private void printPresentities() {
        if (false) {
            Iterator it = presentities.keySet().iterator();
            while (it.hasNext()) {
                String row;
                Object key = it.next();
                Presentity val = presentities.get(key);
                String con = val.getContact();
                String sta = val.getStatus();
                row = ("(" + key + ") " + con + ", " + sta);
                HashMap tuples = (HashMap) val.getTuples();
                if (tuples != null && !tuples.isEmpty()) {
                    Iterator tit = tuples.keySet().iterator();
                    while (tit.hasNext()) {
                        Object tkey = tit.next();
                        Tuple tval = (Tuple) tuples.get(tkey);
                        row += ("-> " + tval.getId() + ", " + tval.getStatus_basic());
                    }
                }
            }
        }
    }

    void onPresenceUpdate() {
        listener.onPresenceChange(presentities);
    }

    @Override
    public void rePublish() {
        if (published) publish(currentPresenceStatus, currentPresenceNote, publishExpireTime, true);
    }
}
