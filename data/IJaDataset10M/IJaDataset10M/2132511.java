package org.restlet.client.data;

import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.engine.util.SystemUtils;
import org.restlet.client.util.Series;

/**
 * Authentication response sent by client to an origin server. This is typically
 * following a {@link ChallengeRequest} sent by the origin server to the client.<br>
 * <br>
 * Sometimes, it might be faster to preemptively issue a challenge response if
 * the client knows for sure that the target resource will require
 * authentication.<br>
 * <br>
 * Note that when used with HTTP connectors, this class maps to the
 * "Authorization" header.
 * 
 * @author Jerome Louvel
 */
public final class ChallengeResponse extends ChallengeMessage {

    /**
     * Indicates if the identifier or principal has been authenticated. The
     * application is responsible for updating this property, relying on a
     * {@link org.restlet.client.security.Guard} or manually.
     */
    private volatile boolean authenticated;

    /** The client nonce value. */
    private volatile String clientNonce;

    /**
     * The {@link Request#getResourceRef()} value duplicated here in case a
     * proxy changed it.
     */
    private volatile Reference digestRef;

    /** The user identifier, such as a login name or an access key. */
    private volatile String identifier;

    /** The chosen quality of protection. */
    private volatile String quality;

    /** The user secret, such as a password or a secret key. */
    private volatile char[] secret;

    /** The server nonce count. */
    private volatile int serverNounceCount;

    /**
     * Constructor with no credentials.
     * 
     * @param scheme
     *            The challenge scheme.
     */
    public ChallengeResponse(ChallengeScheme scheme) {
        super(scheme);
        this.identifier = null;
        this.secret = null;
        this.authenticated = false;
        this.clientNonce = null;
        this.digestRef = null;
        this.quality = null;
        this.serverNounceCount = 1;
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param credentials
     *            The raw credentials for custom challenge schemes.
     * @deprecated Use {@link #setRawValue(String)} instead.
     */
    @Deprecated
    public ChallengeResponse(ChallengeScheme scheme, String credentials) {
        this(scheme);
        setRawValue(credentials);
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public ChallengeResponse(final ChallengeScheme scheme, final String identifier, char[] secret) {
        super(scheme);
        this.identifier = identifier;
        this.secret = secret;
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param parameters
     *            The additional scheme parameters.
     */
    public ChallengeResponse(final ChallengeScheme scheme, final String identifier, Series<Parameter> parameters) {
        super(scheme, parameters);
        this.identifier = identifier;
        this.secret = null;
    }

    /**
     * Constructor.
     * 
     * @param scheme
     *            The challenge scheme.
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public ChallengeResponse(final ChallengeScheme scheme, final String identifier, String secret) {
        super(scheme);
        this.identifier = identifier;
        this.secret = (secret != null) ? secret.toCharArray() : null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        boolean result = (obj == this);
        if (!result) {
            if (obj instanceof ChallengeResponse) {
                final ChallengeResponse that = (ChallengeResponse) obj;
                if (getCredentials() != null) {
                    result = getCredentials().equals(that.getCredentials());
                } else {
                    result = (that.getCredentials() == null);
                }
                if (result) {
                    if (getIdentifier() != null) {
                        result = getIdentifier().equals(that.getIdentifier());
                    } else {
                        result = (that.getIdentifier() == null);
                    }
                    if (result) {
                        if (getScheme() != null) {
                            result = getScheme().equals(that.getScheme());
                        } else {
                            result = (that.getScheme() == null);
                        }
                        if (result) {
                            if ((getSecret() == null) || (that.getSecret() == null)) {
                                result = (getSecret() == that.getSecret());
                            } else {
                                if (getSecret().length == that.getSecret().length) {
                                    boolean equals = true;
                                    for (int i = 0; (i < getSecret().length) && equals; i++) {
                                        equals = (getSecret()[i] == that.getSecret()[i]);
                                    }
                                    result = equals;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the client nonce.
     * 
     * @return The client nonce.
     */
    public String getClientNonce() {
        return this.clientNonce;
    }

    /**
     * Returns the raw credentials.
     * 
     * @return The raw credentials.
     * @deprecated Use {@link #getRawValue()} instead.
     */
    @Deprecated
    public String getCredentials() {
        return getRawValue();
    }

    /**
     * Returns the {@link Request#getResourceRef()} value duplicated here in
     * case a proxy changed it.
     * 
     * @return The digest URI reference.
     */
    public Reference getDigestRef() {
        return digestRef;
    }

    /**
     * Returns the user identifier, such as a login name or an access key.
     * 
     * @return The user identifier, such as a login name or an access key.
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the chosen quality of protection.
     * 
     * @return The chosen quality of protection.
     */
    public String getQuality() {
        return quality;
    }

    /**
     * Returns the user secret, such as a password or a secret key.
     * 
     * It is not recommended to use {@link String#String(char[])} for security
     * reasons.
     * 
     * @return The user secret, such as a password or a secret key.
     */
    public char[] getSecret() {
        return this.secret;
    }

    /**
     * Returns the server nonce count.
     * 
     * @return The server nonce count.
     */
    public int getServerNounceCount() {
        return serverNounceCount;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return SystemUtils.hashCode(getScheme(), getIdentifier(), getCredentials());
    }

    /**
     * Indicates if the identifier or principal has been authenticated. The
     * application is responsible for updating this property, relying on a
     * {@link org.restlet.client.security.Guard} or manually.
     * 
     * @return True if the identifier or principal has been authenticated.
     * @deprecated Use {@link ClientInfo#isAuthenticated()} instead.
     */
    @Deprecated
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    /**
     * Indicates if the identifier or principal has been authenticated. The
     * application is responsible for updating this property, relying on a
     * {@link org.restlet.client.security.Guard} or manually.
     * 
     * @param authenticated
     *            True if the identifier or principal has been authenticated.
     * @deprecated Use {@link ClientInfo#setAuthenticated(boolean)} instead.
     */
    @Deprecated
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * Sets the client nonce.
     * 
     * @param clientNonce
     *            The client nonce.
     */
    public void setClientNonce(String clientNonce) {
        this.clientNonce = clientNonce;
    }

    /**
     * Sets the raw credentials.
     * 
     * @param credentials
     *            The credentials.
     * @deprecated Use {@link #getRawValue()} instead.
     */
    @Deprecated
    public void setCredentials(String credentials) {
        setRawValue(credentials);
    }

    /**
     * Sets the digest URI reference.
     * 
     * @param digestRef
     *            The digest URI reference.
     */
    public void setDigestRef(Reference digestRef) {
        this.digestRef = digestRef;
    }

    /**
     * Sets the user identifier, such as a login name or an access key.
     * 
     * @param identifier
     *            The user identifier, such as a login name or an access key.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Sets the chosen quality of protection.
     * 
     * @param quality
     *            The chosen quality of protection.
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     * Sets the user secret, such as a password or a secret key.
     * 
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public void setSecret(char[] secret) {
        this.secret = secret;
    }

    /**
     * Sets the user secret, such as a password or a secret key.
     * 
     * @param secret
     *            The user secret, such as a password or a secret key.
     */
    public void setSecret(String secret) {
        this.secret = (secret == null) ? null : secret.toCharArray();
    }

    /**
     * Sets the server nonce count.
     * 
     * @param serverNounceCount
     *            The server nonce count.
     */
    public void setServerNounceCount(int serverNounceCount) {
        this.serverNounceCount = serverNounceCount;
    }
}
