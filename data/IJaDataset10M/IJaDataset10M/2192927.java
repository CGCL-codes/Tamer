package edu.upf.da.p2p.sm.server.manager;

import java.util.Map;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.upf.da.p2p.sm.P2PClient;
import edu.upf.da.p2p.sm.P2PMessage;
import edu.upf.da.p2p.sm.P2PMessageListener;
import edu.upf.da.p2p.sm.client.message.AuthRequest;
import edu.upf.da.p2p.sm.client.message.AuthResponse;
import edu.upf.da.p2p.sm.server.message.UserGranted;

public class AuthManager extends P2PMessageListener {

    private static final Log log = LogFactory.getLog(AuthManager.class);

    private P2PClient p2pc;

    private Vector<P2PMessageListener> listeners;

    public AuthManager(P2PClient client) {
        p2pc = client;
    }

    public void processMessage(P2PMessage pkt) {
        AuthRequest req = (AuthRequest) pkt;
        AuthResponse response = null;
        if (req.isLogIn()) {
            response = new AuthResponse(AuthResponse.LOGIN_OK);
            broadcastUserGranted(req.getUser(), req.getFrom());
            if (log.isTraceEnabled()) {
                log.trace("El usuario '" + req.getUser() + "' se ha logueado correctamente desde '" + req.getFrom() + "'.");
            }
        } else if (req.isLogOut()) {
            response = new AuthResponse(AuthResponse.LOGOUT_OK);
            broadcastUserGranted("", req.getFrom());
            if (log.isTraceEnabled()) {
                log.trace("El usuario '" + req.getFrom() + "' se ha deslogueado!");
            }
        }
        if (response != null) {
            response.setTo(req.getFrom());
            p2pc.sendMessage(response);
        }
    }

    public void broadcastUserGranted(String user, String from) {
        if (listeners == null) {
            Map<String, String> services = p2pc.getServiceManager().getAllServices();
            for (String service : services.keySet()) {
                String addr = services.get(service);
                UserGranted ug = new UserGranted(user, from);
                ug.setTo(addr);
                p2pc.sendMessage(ug);
            }
        } else {
            for (P2PMessageListener pl : listeners) {
                UserGranted ug = new UserGranted(user, from);
                pl.processMessage(ug);
            }
        }
    }

    public void addUserGrantedListener(P2PMessageListener pl) {
        if (listeners == null) listeners = new Vector<P2PMessageListener>();
        listeners.add(pl);
    }

    public void removeUserGrantedListener(P2PMessageListener pl) {
        if (listeners != null) listeners.remove(pl);
    }
}
