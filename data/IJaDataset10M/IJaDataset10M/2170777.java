package org.tripcom.security.main;

import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tripcom.integration.entry.Error;
import org.tripcom.integration.entry.SecurityCookieInfo;
import org.tripcom.integration.entry.SetCookieEntry;
import org.tripcom.integration.entry.SpaceSelection;
import org.tripcom.integration.entry.TripComEntry;
import org.tripcom.security.Constants;
import org.tripcom.security.Request;
import org.tripcom.security.RequestAuthorizedEvent;
import org.tripcom.security.Role;
import org.tripcom.security.SecurityContext;
import org.tripcom.security.ac.ACResult;
import org.tripcom.security.exceptions.RequestDiscardedException;

/**
 * Base handler implementation.
 * <p>
 * This abstract class serves as a base for implementing request handlers. Its
 * {@link #handle(Engine, TripComEntry)} method implements the basic request
 * handling cycle, consisting of request decoding, authentication, authorization
 * and processing (see the API documentation of the method for further details).
 * Derived classes must override method
 * {@link #process(SecurityContext, SpaceSelection)} and override method
 * {@link #decode()} to extract security information from the request (at least
 * the operation type, the target space and the security info structure).
 * </p>
 * 
 * @param <T> The type of entry managed by this handler.
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public abstract class AbstractHandler<T extends TripComEntry> implements RequestHandler<T> {

    /** The log object. */
    private static final Log log = LogFactory.getLog(AbstractHandler.class);

    /** A reference to the invoking engine. */
    private Engine engine;

    /**
     * Default constructor. Instantiate a new handler.
     */
    protected AbstractHandler() {
        this.engine = null;
    }

    /**
     * {@inheritDoc} This method stores the obtained parameters and make them
     * available through {@link #getEngine()}.
     */
    public void init(final Engine engine) {
        if (engine == null) {
            throw new NullPointerException();
        }
        this.engine = engine;
    }

    /**
     * Returns a reference to the engine that invoked this handler.
     * 
     * @return the engine.
     */
    protected final Engine getEngine() {
        return engine;
    }

    /**
     * {@inheritDoc}
     */
    public void handle(T entry) {
        final SecurityContext context;
        final ACResult authorizationResult;
        final Request<T> request = decode(entry);
        if (getEngine().isSecurityEnabled()) {
            context = authenticate(request);
            authorizationResult = authorize(request, context);
        } else {
            context = SecurityContext.ANONYMOUS;
            authorizationResult = new ACResult();
            if (request.getTargetSpace() != null) {
                authorizationResult.getPermittedSubtrees().add(request.getTargetSpace());
            }
        }
        process(request, context, authorizationResult);
        notify(request, context, authorizationResult);
    }

    /**
     * Decodes the request. This method must be implemented by derived classes
     * and it is responsible to extract security information from the request.
     * This method is also responsible to validate the decoded request, checking
     * types and values.
     * 
     * @param entry the request entry to decode.
     * @return the decoded request, containing at least the operation type and,
     *         possibly, the target space and the security info associated to
     *         the request.
     * @throws RequestDiscardedException in case the request cannot be
     *             processed.
     */
    protected abstract Request<T> decode(T entry) throws RequestDiscardedException;

    /**
     * Authenticate the client. This method extract and validate the client
     * identity from the provided security information. The method returns the
     * security context associated to the client, which contains the parsed
     * identity and attribute assertions. For authenticated clients, identity
     * assertions are checked to determine whether they are trusted (the TAM
     * module is used to filter them by applying the policy of the target
     * space); if the authentication fails or the identity assertions cannot be
     * trusted the method throws a {@link RequestDiscardedException}. For
     * anonymous requests the method simply returns an anonymous security
     * context.
     * 
     * @param request the request containing the authentication information to
     *            process (not null).
     * @return the security context for the client, on success.
     * @throws RequestDiscardedException on authentication failure or in case
     *             the request cannot be processed.
     */
    protected SecurityContext authenticate(Request<T> request) throws RequestDiscardedException {
        assert (request != null);
        SecurityContext context = getEngine().getAuthn().authenticate(request.getSecurityInfo());
        if (context != SecurityContext.ANONYMOUS) {
            if (request.getTargetSpace() != null) {
                Collection<Role> roles = getEngine().getTam().filterAssertionsAndMapRoles(context, request.getTargetSpace());
                if (!roles.contains(Constants.AUTHENTICATED)) {
                    throw new RequestDiscardedException(Error.SMAuthenticationRejected, "Asserted identity not trusted");
                }
            }
            if (!(request.getSecurityInfo() instanceof SecurityCookieInfo)) {
                if (log.isDebugEnabled()) {
                    log.debug("Setting cookie for new context: " + context.getContextId());
                }
                SetCookieEntry scEntry = new SetCookieEntry();
                scEntry.operationID = request.getEntry().operationID;
                scEntry.cookie = context.getContextId();
                getEngine().sendEntry(scEntry);
            }
        }
        return context;
    }

    /**
     * Authorize the request. This method performs access control on the
     * (authenticated) request. On success, it returns the selection of spaces
     * for which the operation should be granted. On failure a
     * {@link RequestDiscardedException} is thrown.
     * 
     * @param request the request to authorize (not null).
     * @param context the security context associated to the client (not null).
     * @return the spaces for which the request should be authorized.
     * @throws RequestDiscardedException if the request is denied or it cannot
     *             be processed.
     */
    protected ACResult authorize(Request<T> request, SecurityContext context) {
        assert (request != null) && (context != null);
        return getEngine().getAc().authorize(context, request.getOperation(), request.getTargetSpace());
    }

    /**
     * Process the specified authorized request. This method can either:
     * <ul>
     * <li>Forward one or more entries to internal components in charge to
     * handle the request. The particular components contacted depend on the
     * kind of request.</li>
     * <li>Directly execute the request according to its semantic, in case it
     * affects the Security Manager. To this end, particular methods of the
     * Security Manager modules (e.g. the policy manager) may be invoked in
     * order to execute the operation.</li>
     * </ul>
     * This method must be overridden by derived classes.
     * 
     * @param request the authorized request to process (not null).
     * @param context the security context associated to the client (not null).
     * @param authorizationResult the spaces for which the request is authorized
     *            (not null).
     * @throws RequestDiscardedException on authentication failure or in case
     *             the request cannot be processed.
     */
    protected abstract void process(Request<T> request, SecurityContext context, ACResult authorizationResult) throws RequestDiscardedException;

    /**
     * Notify interested listeners of the authorization of a request. This
     * method uses the engine event publisher {@link Engine#getEventPublisher()}
     * to publish a {@link RequestAuthorizedEvent} that is broadcasted to all
     * interested listeners.
     * 
     * @param request the authorized request (not null).
     * @param context the security context associated to the request (not null).
     * @param authorizationResult the spaces for which the request is authorized
     *            (not null).
     */
    protected void notify(Request<T> request, SecurityContext context, ACResult authorizationResult) {
        assert (request != null) && (context != null) && (authorizationResult != null);
        RequestAuthorizedEvent event = new RequestAuthorizedEvent(this, request, context, authorizationResult);
        engine.getEventPublisher().publishEvent(event);
    }

    /**
     * Utility method that throws a {@link RequestDiscardedException} with a
     * {@link Error#SMMalformedRequest} error if the condition is not satisfied.
     * 
     * @param condition the condition to check.
     * @param message the message to include in the exception if the condition
     *            is not satisfied.
     */
    protected static final void checkNotMalformed(boolean condition, String message) {
        if (!condition) {
            throw new RequestDiscardedException(Error.SMMalformedRequest, message);
        }
    }

    /**
     * Utility method that throws a {@link RequestDiscardedException} with a
     * {@link Error#SMUnsupportedOperation} error if the condition is not
     * satisfied.
     * 
     * @param condition the condition to check.
     * @param message the message to include in the exception if the condition
     *            is not satisfied.
     */
    protected static final void checkSupported(boolean condition, String message) {
        if (!condition) {
            throw new RequestDiscardedException(Error.SMUnsupportedOperation, message);
        }
    }
}
