package net.sourceforge.statelessfilter.wrappers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sourceforge.statelessfilter.backend.ISessionBackend;
import net.sourceforge.statelessfilter.backend.ISessionData;
import net.sourceforge.statelessfilter.filter.Configuration;
import net.sourceforge.statelessfilter.session.SessionData;
import net.sourceforge.statelessfilter.session.StatelessSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stateless request wrapper
 * 
 * @author Nicolas Richeton - Capgemini
 * 
 */
public class StatelessRequestWrapper extends HttpServletRequestWrapper {

    private static final String INFO_USES = "{} uses {}";

    private static final String INFO_USES_DEFAULT = "{} uses default {}";

    Configuration backends = null;

    Logger logger = LoggerFactory.getLogger(StatelessRequestWrapper.class);

    HttpServletRequest originalRequest = null;

    StatelessSession session = null;

    private boolean sessionWritten;

    public boolean isSessionWritten() {
        return sessionWritten;
    }

    /**
	 * Create a new request wrapper.
	 * 
	 * @param request
	 * @param backends
	 */
    public StatelessRequestWrapper(HttpServletRequest request, Configuration backends) {
        super(request);
        originalRequest = request;
        this.backends = backends;
    }

    /**
	 * Returns real server session.
	 * 
	 * @return
	 */
    public HttpSession getServerSession() {
        return super.getSession();
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequestWrapper#getSession()
	 */
    @Override
    public HttpSession getSession() {
        if (session == null) {
            try {
                session = createSession();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return session;
    }

    /**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServletRequestWrapper#getSession(boolean)
	 */
    @Override
    public HttpSession getSession(boolean create) {
        if (create) {
            return getSession();
        }
        if (session == null) {
            try {
                session = restoreSession();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return session;
    }

    /**
	 * Stores session in backends
	 * 
	 * @param myrequest
	 * @param myresponse
	 * @throws IOException
	 */
    public void writeSession(HttpServletRequest myrequest, HttpServletResponse myresponse) throws IOException {
        if (session != null) {
            if (backends.useDirty && !session.isDirty()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Session has not changed.");
                }
                return;
            }
            long requestId = System.currentTimeMillis();
            session.setNew(false);
            Map<String, Object> sessionAttributes = session.getContent();
            List<String> modifiedAttributes = session.getDirtyAttributes();
            List<String> modifiedBackends = new ArrayList<String>();
            List<String> remainingModifiedAttributes = new ArrayList<String>(modifiedAttributes);
            Map<String, ISessionData> attributesDispatched = new HashMap<String, ISessionData>();
            Map<String, List<String>> modifiedAttributesDispatched = new HashMap<String, List<String>>();
            for (String name : sessionAttributes.keySet()) {
                if (isAttributeMapped(name)) {
                    getBackendSessionData(attributesDispatched, backends.backendsAttributeMapping.get(name), requestId).getContent().put(name, sessionAttributes.get(name));
                    setModified(modifiedBackends, modifiedAttributes, name);
                    logger.info(INFO_USES, name, backends.backendsAttributeMapping.get(name));
                } else {
                    getBackendSessionData(attributesDispatched, backends.defaultBackend, requestId).getContent().put(name, sessionAttributes.get(name));
                    setModified(modifiedBackends, modifiedAttributes, name);
                    logger.info(INFO_USES_DEFAULT, name, backends.defaultBackend);
                }
                remainingModifiedAttributes.remove(name);
            }
            for (String name : remainingModifiedAttributes) {
                if (isAttributeMapped(name)) {
                    setModified(modifiedBackends, modifiedAttributes, name);
                    logger.info(INFO_USES, name, backends.backendsAttributeMapping.get(name));
                } else {
                    setModified(modifiedBackends, modifiedAttributes, name);
                    logger.info(INFO_USES_DEFAULT, name, backends.defaultBackend);
                }
            }
            if (session.isPropertyDirty()) {
                logger.info("Session properties have changed. Forcing update on all backends.");
                for (String back : backends.backends.keySet()) {
                    ISessionBackend backend = backends.backends.get(back);
                    backend.save(getBackendSessionData(attributesDispatched, back, requestId), modifiedAttributesDispatched.get(back), originalRequest, myresponse);
                }
            } else {
                for (String back : modifiedBackends) {
                    ISessionBackend backend = backends.backends.get(back);
                    backend.save(getBackendSessionData(attributesDispatched, back, requestId), modifiedAttributesDispatched.get(back), originalRequest, myresponse);
                }
            }
        }
        sessionWritten = true;
    }

    /**
	 * Create a session using all backends.
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
    private StatelessSession createSession() throws NoSuchAlgorithmException {
        StatelessSession s = restoreSession();
        if (s == null) {
            s = new StatelessSession(this);
            s.init(true);
        }
        return s;
    }

    /**
	 * Get current data for session backend. Creates a new ISessionData if
	 * necessary.
	 * 
	 * @param dispatched
	 * @param name
	 * @param requestId
	 * @return
	 */
    private ISessionData getBackendSessionData(Map<String, ISessionData> dispatched, String name, long requestId) {
        if (dispatched.containsKey(name)) {
            return dispatched.get(name);
        }
        SessionData data = new SessionData();
        data.setId(session.getId());
        data.setCreationTime(session.getCreationTime());
        data.setValid(session.isValid());
        data.setRequestId(requestId);
        dispatched.put(name, data);
        return data;
    }

    /**
	 * Check if an attribute is mapped to a backend by configuration.
	 * <p>
	 * Returns null if there is no configuration for this attribute. The default
	 * backend should be used in that case.
	 * 
	 * @param attrName
	 * @return
	 */
    private boolean isAttributeMapped(String attrName) {
        if (backends != null && backends.backendsAttributeMapping != null) {
            return backends.backendsAttributeMapping.containsKey(attrName);
        }
        return false;
    }

    private StatelessSession restoreSession() throws NoSuchAlgorithmException {
        StatelessSession s = new StatelessSession(this);
        ISessionData data = null;
        boolean restored = false;
        s.init(false);
        for (ISessionBackend back : backends.backends.values()) {
            data = back.restore(originalRequest);
            if (data != null) {
                restored = true;
                s.merge(data);
            }
        }
        if (!restored) {
            return null;
        }
        return s;
    }

    /**
	 * If attributeName is in modifiedAttributes, then the corresponding backend
	 * is added to modifiedBackends.
	 * 
	 * @param modifiedBackends
	 * @param modifiedAttributes
	 * @param attributeName
	 */
    private void setModified(List<String> modifiedBackends, List<String> modifiedAttributes, String attributeName) {
        String backend = backends.backendsAttributeMapping.get(attributeName);
        if (StringUtils.isEmpty(backend)) {
            backend = backends.defaultBackend;
        }
        if (modifiedAttributes.contains(attributeName) && !modifiedBackends.contains(backend)) {
            modifiedBackends.add(backend);
            if (logger.isDebugEnabled()) {
                logger.info("Flagging backend {} as modified", backend);
            }
        }
    }
}
