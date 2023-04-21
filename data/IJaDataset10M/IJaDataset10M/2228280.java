package naru.aweb.auth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import naru.async.pool.PoolBase;
import naru.async.store.DataUtil;
import naru.aweb.config.Config;
import naru.aweb.config.User;

public class AuthSession extends PoolBase {

    private static Config config = Config.getConfig();

    public static AuthSession UNAUTH_SESSION = new AuthSession(new User(), "");

    private static Logger logger = Logger.getLogger(AuthSession.class);

    private static String serverId = DataUtil.digestHex(config.getAuthUrl().getBytes()) + System.currentTimeMillis() + ".";

    private static long appIdSeq = 0;

    private User user;

    private String token;

    private String appId;

    private Map<String, Object> attribute = new HashMap<String, Object>();

    private boolean isLogout = false;

    private SessionId sessionId;

    private Set<LogoutEvent> logoutEvents = new HashSet<LogoutEvent>();

    private Set<AuthSession> secandarySessions = new HashSet<AuthSession>();

    private static synchronized long getAppIdSeq() {
        return appIdSeq++;
    }

    public void recycle() {
        Iterator<Object> itr = attribute.values().iterator();
        while (itr.hasNext()) {
            Object v = itr.next();
            if (v instanceof PoolBase) {
                ((PoolBase) v).unref();
            }
        }
        attribute.clear();
        logoutEvents.clear();
        secandarySessions.clear();
        isLogout = false;
        super.recycle();
    }

    AuthSession(User user, String token) {
        init(user, token);
    }

    public AuthSession() {
    }

    void init(User user, String token) {
        this.user = user;
        this.token = token;
        this.appId = DataUtil.digestHex((serverId + getAppIdSeq()).getBytes());
    }

    public AuthSession createSecondarySession() {
        AuthSession secodarySession = Authenticator.internalCreateAuthSession(user);
        secandarySessions.add(secodarySession);
        return secodarySession;
    }

    public synchronized AuthSession getSecondarySession(String authUrl) {
        if (isLogout) {
            return null;
        }
        if (sessionId.getType() != SessionId.Type.PRIMARY) {
            logger.error("logout type error." + sessionId.getType(), new Exception());
            return null;
        }
        for (AuthSession secondarySession : secandarySessions) {
            if (secondarySession.getSessionId().isCookieMatch(authUrl)) {
                return secondarySession;
            }
        }
        return null;
    }

    public User getUser() {
        return user;
    }

    public synchronized void logout() {
        if (isLogout) {
            return;
        }
        if (sessionId.getType() != SessionId.Type.PRIMARY) {
            logger.error("logout type error." + sessionId.getType(), new Exception());
            return;
        }
        user.logout();
        isLogout = true;
        for (AuthSession secondarySession : secandarySessions) {
            synchronized (secondarySession) {
                if (secondarySession.isLogout()) {
                    continue;
                }
                for (LogoutEvent evnet : secondarySession.logoutEvents) {
                    evnet.onLogout();
                }
                secondarySession.logoutEvents.clear();
                SessionId secondaryId = secondarySession.getSessionId();
                secondaryId.remove();
                secondarySession.unref();
            }
        }
        secandarySessions.clear();
        for (LogoutEvent evnet : logoutEvents) {
            evnet.onLogout();
        }
        logoutEvents.clear();
        sessionId.remove();
        unref();
    }

    public synchronized boolean addLogoutEvent(LogoutEvent event) {
        if (isLogout) {
            return false;
        }
        logoutEvents.add(event);
        return true;
    }

    public synchronized void removeLogoutEvent(LogoutEvent event) {
        logoutEvents.remove(event);
    }

    public boolean isLogout() {
        return isLogout;
    }

    public Object getAttribute(String name) {
        return attribute.get(name);
    }

    public void setAttribute(String name, Object value) {
        if (value != null && value instanceof PoolBase) {
            ((PoolBase) value).ref();
        }
        Object obj = attribute.get(name);
        if (obj != null && obj instanceof PoolBase) {
            ((PoolBase) obj).unref();
        }
        attribute.put(name, value);
    }

    public Iterator<String> getAttributeNames() {
        return attribute.keySet().iterator();
    }

    public String getToken() {
        return token;
    }

    public String getAppId() {
        return appId;
    }

    public SessionId getSessionId() {
        return sessionId;
    }

    public void setSessionId(SessionId sessionId) {
        this.sessionId = sessionId;
    }
}
