package org.sqlanyware.sqlwclient.webservices.utils;

import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.hibernate.SessionFactory;
import org.sqlanyware.sqlwclient.api.data.session.Session;
import org.sqlanyware.sqlwclient.api.data.session.SessionToken;
import org.sqlanyware.sqlwclient.api.services.session.ISessionHolder;
import org.sqlanyware.sqlwclient.api.services.session.ISessionManager;
import org.sqlanyware.sqlwclient.api.services.session.InvalidTokenException;

public class PreInvocationInterceptor extends BaseInterceptor {

    /**
	 * Constructor.
	 */
    public PreInvocationInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    /**
	 * {@inheritDoc}
	 */
    public void handleMessage(final SoapMessage soapMessage) throws Fault {
        final SessionFactory sessionFactory = getSessionFactory();
        final ISessionHolder sessionHolder = getSessionHolder();
        final ISessionManager sessionManager = getSessionManager();
        try {
            final org.hibernate.Session databaseSession = sessionFactory.getCurrentSession();
            databaseSession.beginTransaction();
            sessionHolder.setCurrentSession(null);
            final String sessionTokenAsString = getTokenFromMessage(soapMessage);
            if (null != sessionTokenAsString && !"".equals(sessionTokenAsString)) {
                final SessionToken sessionToken = new SessionToken(sessionTokenAsString);
                final Session session = sessionManager.getSession(sessionToken);
                sessionHolder.setCurrentSession(session);
            }
        } catch (final InvalidTokenException invalidTokenException) {
            throw new SoapFault("Invalid token", PreInvocationInterceptor.ERROR_QNAME);
        }
    }
}
