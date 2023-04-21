package org.atricore.idbus.kernel.main.authn.exceptions;

import org.atricore.idbus.kernel.main.authn.SSOPolicyEnforcementStatement;
import java.util.HashSet;
import java.util.Set;

/**
 * This exception is thrown when an authentication fails
 * An authentication failure can happen, for example, when the credentials supplied by the user are invalid.
 *
 * @author <a href="mailto:sgonzalez@josso.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: AuthenticationFailureException.java 1040 2009-03-05 00:56:52Z gbrigand $
 */
public class AuthenticationFailureException extends SSOAuthenticationException {

    /**
     * This stores the error type detected during authentication, for example AUTH_FAILED.
     */
    private String errorType;

    private Set<SSOPolicyEnforcementStatement> policyEnforcements = new HashSet<SSOPolicyEnforcementStatement>();

    /**
     * It uses AUTH_FAILDE as error type.
     *
     * @param message El mensaje asociado al error.
     */
    public AuthenticationFailureException(String message) {
        this(message, "AUTH_FAILED");
    }

    /**
     * It uses AUTH_FAILDE as error type.
     *
     * @param message El mensaje asociado al error.
     */
    public AuthenticationFailureException(String message, Set<SSOPolicyEnforcementStatement> policyEnforcements) {
        this(message, "AUTH_FAILED");
        this.policyEnforcements.addAll(policyEnforcements);
    }

    /**
     * Allows error type specification, usefull when extending the Authenticator to provide business specific rules.
     *
     * @param message   The error message
     * @param errorType The error type
     */
    public AuthenticationFailureException(String message, String errorType) {
        super(message);
        this.errorType = errorType;
    }

    public String getErrorType() {
        return this.errorType;
    }

    public Set<SSOPolicyEnforcementStatement> getSSOPolicies() {
        return policyEnforcements;
    }
}
