package org.josso.gateway.ws._1_1.wsdl.soapbinding;

import org.josso.gateway.ws._1_1.protocol.*;
import org.josso.gateway.ws._1_1.wsdl.SSOSessionManager;
import org.josso.gateway.SSOContext;
import org.josso.gateway.session.exceptions.NoSuchSessionException;
import org.josso.gateway.session.SSOSession;
import org.josso.SecurityDomain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.rmi.RemoteException;

public class SSOSessionManagerBindingImpl extends BaseSSOService implements SSOSessionManager {

    private static Log logger = LogFactory.getLog(SSOSessionManagerBindingImpl.class);

    public AccessSessionResponseType accessSession(AccessSessionRequestType body) throws java.rmi.RemoteException {
        try {
            String ssoSessionId = body.getSsoSessionId();
            prepareCtx(org.josso.gateway.session.service.SSOSessionManager.TOKEN_TYPE, ssoSessionId);
            if (logger.isDebugEnabled()) logger.debug("About to access session");
            if (SSOContext.getCurrent() == null) {
                throw new NoSuchSessionErrorType(body.getSsoSessionId() != null && !"".equals(body.getSsoSessionId()) ? body.getSsoSessionId() : "[NULL]");
            }
            SecurityDomain sd = SSOContext.getCurrent().getSecurityDomain();
            sd.getSessionManager().accessSession(ssoSessionId);
            if (logger.isDebugEnabled()) logger.debug("After access session");
            AccessSessionResponseType response = new AccessSessionResponseType();
            response.setSsoSessionId(ssoSessionId);
            return response;
        } catch (NoSuchSessionException e) {
            if (logger.isDebugEnabled()) logger.debug(e.getMessage());
            throw new NoSuchSessionErrorType(body.getSsoSessionId() != null && !"".equals(body.getSsoSessionId()) ? body.getSsoSessionId() : "[NULL]");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SSOSessionErrorType("SSOSessionManager error : " + e.getMessage());
        }
    }

    public SessionResponseType getSession(SessionRequestType body) throws RemoteException, NoSuchSessionErrorType, SSOSessionErrorType {
        try {
            String ssoSessionId = body.getSessionId();
            prepareCtx(org.josso.gateway.session.service.SSOSessionManager.TOKEN_TYPE, ssoSessionId);
            SSOSession s = SSOContext.getCurrent().getSecurityDomain().getSessionManager().getSession(ssoSessionId);
            SessionResponseType response = new SessionResponseType();
            response.setSSOSession(adaptSession(s));
            return response;
        } catch (NoSuchSessionException e) {
            throw new NoSuchSessionErrorType(body.getSessionId() != null && !"".equals(body.getSessionId()) ? body.getSessionId() : "[NULL]");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SSOSessionErrorType("SSOSessionManager error : " + e.getMessage());
        }
    }

    private SSOSessionType adaptSession(SSOSession s) {
        SSOSessionType st = new SSOSessionType();
        st.setId(s.getId());
        st.setAccessCount(s.getAccessCount());
        st.setCreationTime(s.getCreationTime());
        st.setLastAccessTime(s.getLastAccessTime());
        st.setMaxInactiveInterval(s.getMaxInactiveInterval());
        st.setUsername(s.getUsername());
        return st;
    }
}
