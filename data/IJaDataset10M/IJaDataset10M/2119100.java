package org.torweg.pulse.component.core.accesscontrol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jdom.Element;
import org.torweg.pulse.bundle.Result;

/**
 * The <code>Result</code> of the <code>Authentication</code>.
 * 
 * @author Christian Schatt
 * @version $Revision: 1.1 $
 */
public class AuthenticationResult implements Result {

    /**
	 * The serialVersionUID of the <code>AuthenticationResult</code>.
	 */
    private static final long serialVersionUID = -4190087654409506672L;

    /**
	 * The login-status of this <code>AuthenticationResult</code>.
	 */
    private boolean loginStatus = false;

    /**
	 * The name-mappings for the httpParameters of this
	 * <code>AuthenticationResult</code>.
	 */
    private Map<String, String> parameterMappings = new HashMap<String, String>();

    /**
	 * The login-modes of this <code>AuthenticationResult</code>.
	 */
    private Set<String> loginModes = new HashSet<String>();

    /**
	 * The error-code of this <code>AuthenticationResult</code>.
	 */
    private Integer errorCode = null;

    /**
	 * The error-description of this <code>AuthenticationResult</code>.
	 */
    private String errorDescription = null;

    /**
	 * desired redirect URI.
	 */
    private String redirectURI = null;

    /**
	 * Sets the login-status of this <code>AuthenticationResult</code>.
	 * 
	 * @param newLoginStatus
	 *            the new login-status of this <code>AuthenticationResult</code>
	 */
    protected final void setLoginStatus(final boolean newLoginStatus) {
        this.loginStatus = newLoginStatus;
    }

    /**
	 * Sets the name-mappings for the httpParameters of this
	 * <code>AuthenticationResult</code>.
	 * 
	 * @param newParameterMappings
	 *            the new name-mappings for the httpParameters of the
	 *            <code>AuthenticationResult</code>
	 */
    protected final void setParameterMappings(final Map<String, String> newParameterMappings) {
        if (newParameterMappings == null) {
            throw new NullPointerException("AuthenticationResult.parameterMappings" + " must not be set to null.");
        }
        this.parameterMappings = newParameterMappings;
    }

    /**
	 * Sets the login-modes of this <code>AuthenticationResult</code>.
	 * 
	 * @param newLoginModes
	 *            the new login-modes of this <code>AuthenticationResult</code>
	 */
    protected final void setLoginModes(final Set<String> newLoginModes) {
        if (newLoginModes == null) {
            throw new NullPointerException("AuthenticationResult.loginModes must not be set to null.");
        }
        this.loginModes = newLoginModes;
    }

    /**
	 * Sets the error-code and the error-description of this
	 * <code>AuthenticationResult</code>. If the parameter newErrorCode is
	 * <code>null</code>, the error-description will be set to <code>null</code>
	 * , too, regardless of the parameter newErrorDescription.
	 * 
	 * @param newErrorCode
	 *            the new error-code of this <code>AuthenticationResult</code>
	 * @param newErrorDescription
	 *            the new error-description of this
	 *            <code>AuthenticationResult</code>
	 */
    protected final void setError(final Integer newErrorCode, final String newErrorDescription) {
        if (newErrorCode != null) {
            this.errorCode = newErrorCode;
            this.errorDescription = newErrorDescription;
        }
    }

    /**
	 * Sets the desired redirect URI.
	 * @param redirect
	 *            the desired redirect URI
	 */
    protected void setRedirectURI(final String redirect) {
        this.redirectURI = redirect;
    }

    /**
	 * Serializes the state of this <code>AuthenticationResult</code> as a JDOM
	 * <code>Element</code>.
	 * 
	 * @return the state of this <code>AuthenticationResult</code> as a JDOM
	 *         <code>Element</code>.
	 * 
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 */
    public final Element deserializeToJDOM() {
        Element result = new Element("result").setAttribute("class", this.getClass().getCanonicalName());
        if (this.redirectURI != null) {
            result.addContent(new Element("redirect").setText(this.redirectURI));
        }
        result.addContent(new Element("login-status").setAttribute("value", Boolean.toString(this.loginStatus)));
        Element mappings = new Element("parameter-mappings");
        for (Map.Entry<String, String> entry : this.parameterMappings.entrySet()) {
            Element mapping = new Element(entry.getKey());
            mapping.setAttribute("name", entry.getValue());
            mappings.addContent(mapping);
        }
        result.addContent(mappings);
        Element modes = new Element("login-modes");
        for (String mode : this.loginModes) {
            modes.addContent(new Element("login-mode").setAttribute("name", mode));
        }
        result.addContent(modes);
        if (this.errorCode != null) {
            result.addContent(new Element("error-code").setAttribute("value", this.errorCode.toString()).setAttribute("description", this.errorDescription));
        }
        return result;
    }
}
