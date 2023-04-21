package org.acegisecurity.intercept;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.acegisecurity.AccessDecisionManager;
import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.AcegiMessageSource;
import org.acegisecurity.AfterInvocationManager;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationCredentialsNotFoundException;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.RunAsManager;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.event.authorization.AuthenticationCredentialsNotFoundEvent;
import org.acegisecurity.event.authorization.AuthorizationFailureEvent;
import org.acegisecurity.event.authorization.AuthorizedEvent;
import org.acegisecurity.event.authorization.PublicInvocationEvent;
import org.acegisecurity.runas.NullRunAsManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;

/**
 * Abstract class that implements security interception for secure objects.
 * 
 * <p>
 * The <code>AbstractSecurityInterceptor</code> will ensure the proper startup
 * configuration of the security interceptor. It will also implement the
 * proper handling of secure object invocations, being:
 * 
 * <ol>
 * <li>
 * Obtain the {@link Authentication} object from the {@link
 * SecurityContextHolder}.
 * </li>
 * <li>
 * Determine if the request relates to a secured or public invocation by
 * looking up the secure object request against the {@link
 * ObjectDefinitionSource}.
 * </li>
 * <li>
 * For an invocation that is secured (there is a
 * <code>ConfigAttributeDefinition</code> for the secure object invocation):
 * 
 * <ol type="a">
 * <li>
 * If either the {@link org.acegisecurity.Authentication#isAuthenticated()}
 * returns <code>false</code>, or the {@link #alwaysReauthenticate} is
 * <code>true</code>,  authenticate the request against the configured {@link
 * AuthenticationManager}. When authenticated, replace the
 * <code>Authentication</code> object on the
 * <code>SecurityContextHolder</code> with the returned value.
 * </li>
 * <li>
 * Authorize the request against the configured {@link AccessDecisionManager}.
 * </li>
 * <li>
 * Perform any run-as replacement via the configured {@link RunAsManager}.
 * </li>
 * <li>
 * Pass control back to the concrete subclass, which will actually proceed with
 * executing the object. A {@link InterceptorStatusToken} is returned so that
 * after the subclass has finished proceeding with  execution of the object,
 * its finally clause can ensure the <code>AbstractSecurityInterceptor</code>
 * is re-called and tidies up correctly.
 * </li>
 * <li>
 * The concrete subclass will re-call the
 * <code>AbstractSecurityInterceptor</code> via the {@link
 * #afterInvocation(InterceptorStatusToken, Object)} method.
 * </li>
 * <li>
 * If the <code>RunAsManager</code> replaced the <code>Authentication</code>
 * object, return the <code>SecurityContextHolder</code> to the object that
 * existed after the call to <code>AuthenticationManager</code>.
 * </li>
 * <li>
 * If an <code>AfterInvocationManager</code> is defined, invoke the invocation
 * manager and allow it to replace the object due to be returned to the
 * caller.
 * </li>
 * </ol>
 * 
 * </li>
 * <li>
 * For an invocation that is public (there is no
 * <code>ConfigAttributeDefinition</code> for the secure object invocation):
 * 
 * <ol type="a">
 * <li>
 * As described above, the concrete subclass will be returned an
 * <code>InterceptorStatusToken</code> which is subsequently re-presented to
 * the <code>AbstractSecurityInterceptor</code> after the secure object has
 * been executed. The <code>AbstractSecurityInterceptor</code> will take no
 * further action when its {@link #afterInvocation(InterceptorStatusToken,
 * Object)} is called.
 * </li>
 * </ol>
 * 
 * </li>
 * <li>
 * Control again returns to the concrete subclass, along with the
 * <code>Object</code> that should be returned to the caller.  The subclass
 * will then return that  result or exception to the original caller.
 * </li>
 * </ol>
 * </p>
 *
 * @author Ben Alex
 * @version $Id: AbstractSecurityInterceptor.java,v 1.29 2006/02/16 01:11:31 luke_t Exp $
 */
public abstract class AbstractSecurityInterceptor implements InitializingBean, ApplicationEventPublisherAware, MessageSourceAware {

    protected static final Log logger = LogFactory.getLog(AbstractSecurityInterceptor.class);

    private AccessDecisionManager accessDecisionManager;

    private AfterInvocationManager afterInvocationManager;

    private ApplicationEventPublisher eventPublisher;

    private AuthenticationManager authenticationManager;

    protected MessageSourceAccessor messages = AcegiMessageSource.getAccessor();

    private RunAsManager runAsManager = new NullRunAsManager();

    private boolean alwaysReauthenticate = false;

    private boolean rejectPublicInvocations = false;

    private boolean validateConfigAttributes = true;

    /**
     * Completes the work of the <code>AbstractSecurityInterceptor</code> after
     * the secure object invocation has been complete
     *
     * @param token as returned by the {@link #beforeInvocation(Object)}}
     *        method
     * @param returnedObject any object returned from the secure object
     *        invocation (may be<code>null</code>)
     *
     * @return the object the secure object invocation should ultimately return
     *         to its caller (may be <code>null</code>)
     */
    protected Object afterInvocation(InterceptorStatusToken token, Object returnedObject) {
        if (token == null) {
            return returnedObject;
        }
        if (token.isContextHolderRefreshRequired()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Reverting to original Authentication: " + token.getAuthentication().toString());
            }
            SecurityContextHolder.getContext().setAuthentication(token.getAuthentication());
        }
        if (afterInvocationManager != null) {
            returnedObject = afterInvocationManager.decide(token.getAuthentication(), token.getSecureObject(), token.getAttr(), returnedObject);
        }
        return returnedObject;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getSecureObjectClass(), "Subclass must provide a non-null response to getSecureObjectClass()");
        Assert.notNull(this.messages, "A message source must be set");
        Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");
        Assert.notNull(this.accessDecisionManager, "An AccessDecisionManager is required");
        Assert.notNull(this.runAsManager, "A RunAsManager is required");
        Assert.notNull(this.obtainObjectDefinitionSource(), "An ObjectDefinitionSource is required");
        Assert.isTrue(this.obtainObjectDefinitionSource().supports(getSecureObjectClass()), "ObjectDefinitionSource does not support secure object class: " + getSecureObjectClass());
        Assert.isTrue(this.runAsManager.supports(getSecureObjectClass()), "RunAsManager does not support secure object class: " + getSecureObjectClass());
        Assert.isTrue(this.accessDecisionManager.supports(getSecureObjectClass()), "AccessDecisionManager does not support secure object class: " + getSecureObjectClass());
        if (this.afterInvocationManager != null) {
            Assert.isTrue(this.afterInvocationManager.supports(getSecureObjectClass()), "AfterInvocationManager does not support secure object class: " + getSecureObjectClass());
        }
        if (this.validateConfigAttributes) {
            Iterator iter = this.obtainObjectDefinitionSource().getConfigAttributeDefinitions();
            if (iter == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Could not validate configuration attributes as the MethodDefinitionSource did not return a ConfigAttributeDefinition Iterator");
                }
            } else {
                Set set = new HashSet();
                while (iter.hasNext()) {
                    ConfigAttributeDefinition def = (ConfigAttributeDefinition) iter.next();
                    Iterator attributes = def.getConfigAttributes();
                    while (attributes.hasNext()) {
                        ConfigAttribute attr = (ConfigAttribute) attributes.next();
                        if (!this.runAsManager.supports(attr) && !this.accessDecisionManager.supports(attr) && ((this.afterInvocationManager == null) || !this.afterInvocationManager.supports(attr))) {
                            set.add(attr);
                        }
                    }
                }
                if (set.size() == 0) {
                    if (logger.isInfoEnabled()) {
                        logger.info("Validated configuration attributes");
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported configuration attributes: " + set.toString());
                }
            }
        }
    }

    protected InterceptorStatusToken beforeInvocation(Object object) {
        Assert.notNull(object, "Object was null");
        if (!getSecureObjectClass().isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException("Security invocation attempted for object " + object.getClass().getName() + " but AbstractSecurityInterceptor only configured to support secure objects of type: " + getSecureObjectClass());
        }
        ConfigAttributeDefinition attr = this.obtainObjectDefinitionSource().getAttributes(object);
        if ((attr == null) && rejectPublicInvocations) {
            throw new IllegalArgumentException("No public invocations are allowed via this AbstractSecurityInterceptor. This indicates a configuration error because the AbstractSecurityInterceptor.rejectPublicInvocations property is set to 'true'");
        }
        if (attr != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Secure object: " + object.toString() + "; ConfigAttributes: " + attr.toString());
            }
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                credentialsNotFound(messages.getMessage("AbstractSecurityInterceptor.authenticationNotFound", "An Authentication object was not found in the SecurityContext"), object, attr);
            }
            Authentication authenticated;
            if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || alwaysReauthenticate) {
                try {
                    authenticated = this.authenticationManager.authenticate(SecurityContextHolder.getContext().getAuthentication());
                } catch (AuthenticationException authenticationException) {
                    throw authenticationException;
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Successfully Authenticated: " + authenticated.toString());
                }
                SecurityContextHolder.getContext().setAuthentication(authenticated);
            } else {
                authenticated = SecurityContextHolder.getContext().getAuthentication();
                if (logger.isDebugEnabled()) {
                    logger.debug("Previously Authenticated: " + authenticated.toString());
                }
            }
            try {
                this.accessDecisionManager.decide(authenticated, object, attr);
            } catch (AccessDeniedException accessDeniedException) {
                AuthorizationFailureEvent event = new AuthorizationFailureEvent(object, attr, authenticated, accessDeniedException);
                this.eventPublisher.publishEvent(event);
                throw accessDeniedException;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Authorization successful");
            }
            AuthorizedEvent event = new AuthorizedEvent(object, attr, authenticated);
            this.eventPublisher.publishEvent(event);
            Authentication runAs = this.runAsManager.buildRunAs(authenticated, object, attr);
            if (runAs == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("RunAsManager did not change Authentication object");
                }
                return new InterceptorStatusToken(authenticated, false, attr, object);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Switching to RunAs Authentication: " + runAs.toString());
                }
                SecurityContextHolder.getContext().setAuthentication(runAs);
                return new InterceptorStatusToken(authenticated, true, attr, object);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Public object - authentication not attempted");
            }
            this.eventPublisher.publishEvent(new PublicInvocationEvent(object));
            return null;
        }
    }

    /**
     * Helper method which generates an exception containing the passed
     * reason, and publishes an event to the application context.
     *
     * <p>
     * Always throws an exception.
     * </p>
     *
     * @param reason to be provided in the exception detail
     * @param secureObject that was being called
     * @param configAttribs that were defined for the secureObject
     */
    private void credentialsNotFound(String reason, Object secureObject, ConfigAttributeDefinition configAttribs) {
        AuthenticationCredentialsNotFoundException exception = new AuthenticationCredentialsNotFoundException(reason);
        AuthenticationCredentialsNotFoundEvent event = new AuthenticationCredentialsNotFoundEvent(secureObject, configAttribs, exception);
        this.eventPublisher.publishEvent(event);
        throw exception;
    }

    public AccessDecisionManager getAccessDecisionManager() {
        return accessDecisionManager;
    }

    public AfterInvocationManager getAfterInvocationManager() {
        return afterInvocationManager;
    }

    public AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    public RunAsManager getRunAsManager() {
        return runAsManager;
    }

    /**
     * Indicates the type of secure objects the subclass will be presenting
     * to the abstract parent for processing. This is used to ensure
     * collaborators wired to the <code>AbstractSecurityInterceptor</code>
     * all support the indicated secure object class.
     *
     * @return the type of secure object the subclass provides services for
     */
    public abstract Class getSecureObjectClass();

    public boolean isAlwaysReauthenticate() {
        return alwaysReauthenticate;
    }

    public boolean isRejectPublicInvocations() {
        return rejectPublicInvocations;
    }

    public boolean isValidateConfigAttributes() {
        return validateConfigAttributes;
    }

    public abstract ObjectDefinitionSource obtainObjectDefinitionSource();

    public void setAccessDecisionManager(AccessDecisionManager accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    public void setAfterInvocationManager(AfterInvocationManager afterInvocationManager) {
        this.afterInvocationManager = afterInvocationManager;
    }

    /**
     * Indicates whether the <code>AbstractSecurityInterceptor</code>
     * should ignore the {@link Authentication#isAuthenticated()}
     * property. Defaults to <code>false</code>, meaning by default the
     * <code>Authentication.isAuthenticated()</code> property is trusted
     * and re-authentication will not occur if the principal has already
     * been authenticated.
     *
     * @param alwaysReauthenticate <code>true</code> to force
     *        <code>AbstractSecurityInterceptor</code> to disregard the
     *        value of <code>Authentication.isAuthenticated()</code> and
     *        always re-authenticate the request (defaults to
     *        <code>false</code>).
     */
    public void setAlwaysReauthenticate(boolean alwaysReauthenticate) {
        this.alwaysReauthenticate = alwaysReauthenticate;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void setAuthenticationManager(AuthenticationManager newManager) {
        this.authenticationManager = newManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    /**
     * By rejecting public invocations (and setting this property to
     * <code>true</code>), essentially you are ensuring that every secure
     * object invocation advised by
     * <code>AbstractSecurityInterceptor</code> has a configuration
     * attribute defined. This is useful to ensure a "fail safe" mode
     * where undeclared secure objects will be rejected and configuration
     * omissions detected early. An <code>IllegalArgumentException</code>
     * will be thrown by the <code>AbstractSecurityInterceptor</code> if
     * you set this property to <code>true</code> and an attempt is made
     * to invoke a secure object that has no configuration attributes.
     *
     * @param rejectPublicInvocations set to <code>true</code> to reject
     *        invocations of secure objects that have no configuration
     *        attributes (by default it is <code>true</code> which treats
     *        undeclared secure objects as "public" or unauthorized)
     */
    public void setRejectPublicInvocations(boolean rejectPublicInvocations) {
        this.rejectPublicInvocations = rejectPublicInvocations;
    }

    public void setRunAsManager(RunAsManager runAsManager) {
        this.runAsManager = runAsManager;
    }

    public void setValidateConfigAttributes(boolean validateConfigAttributes) {
        this.validateConfigAttributes = validateConfigAttributes;
    }
}
