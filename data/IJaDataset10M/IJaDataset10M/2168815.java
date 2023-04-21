package org.josso.gateway.session.service;

import org.josso.gateway.session.exceptions.NoSuchSessionException;
import org.josso.gateway.session.exceptions.SSOSessionException;
import org.josso.gateway.session.SSOSession;

/**
 * This is the service interface exposed to JOSSO Agents and external JOSSO Session Service consumers.
 *
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Rev: 568 $ $Date: 2008-07-31 15:39:20 -0300 (Thu, 31 Jul 2008) $
 */
public interface SSOSessionManagerService {

    /**
     * This method accesss the session associated to the received id.
     * This resets the session last access time and updates the access count.
     *
     * @param sessionId the session id previously returned by initiateSession.
     * @throws org.josso.gateway.session.exceptions.NoSuchSessionException
     *          if the session id is not valid or the session is not valid.
     */
    void accessSession(String sessionId) throws NoSuchSessionException, SSOSessionException;

    /**
     * This method returns a SSOSession instance based on its id.
     *
     * @param sessionId
     * @return
     * @throws NoSuchSessionException it the SSO Session does not exists (or is invalid)
     * @throws SSOSessionException
     */
    SSOSession getSession(String sessionId) throws NoSuchSessionException, SSOSessionException;
}
