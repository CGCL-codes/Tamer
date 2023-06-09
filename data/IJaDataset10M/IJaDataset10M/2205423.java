package org.ietf.jgss;

/**
 * <p>This interface encapsulates the GSS-API credentials for an entity.
 * A credential contains all the necessary cryptographic information to
 * enable the creation of a context on behalf of the entity that it
 * represents.  It may contain multiple, distinct, mechanism specific
 * credential elements, each containing information for a specific
 * security mechanism, but all referring to the same entity.</p>
 *
 * <p>A credential may be used to perform context initiation, acceptance,
 * or both.</p>
 *
 * <p>GSS-API implementations must impose a local access-control policy on
 * callers to prevent unauthorized callers from acquiring credentials to
 * which they are not entitled.  GSS-API credential creation is not
 * intended to provide a "login to the network" function, as such a
 * function would involve the creation of new credentials rather than
 * merely acquiring a handle to existing credentials.  Such functions,
 * if required, should be defined in implementation-specific extensions
 * to the API.</p>
 *
 * <p>If credential acquisition is time-consuming for a mechanism, the
 * mechanism may choose to delay the actual acquisition until the
 * credential is required (e.g.  by {@link GSSContext}). Such mechanism-
 * specific implementation decisions should be invisible to the calling
 * application; thus the query methods immediately following the
 * creation of a credential object must return valid credential data,
 * and may therefore incur the overhead of a deferred credential
 * acquisition.</p>
 *
 * <p>Applications will create a credential object passing the desired
 * parameters.  The application can then use the query methods to obtain
 * specific information about the instantiated credential object
 * (equivalent to the gss_inquire routines).  When the credential is no
 * longer needed, the application should call the dispose (equivalent to
 * gss_release_cred) method to release any resources held by the
 * credential object and to destroy any cryptographically sensitive
 * information.</p>
 *
 * <p>Classes implementing this interface also implement the {@link Cloneable}
 * interface. This indicates the the class will support the {@link
 * Cloneable#clone()} method that will allow the creation of duplicate
 * credentials. This is useful when called just before the {@link
 * #add(org.ietf.jgss.GSSName,int,int,org.ietf.jgss.Oid,int)} call to retain
 * a copy of the original credential.</p>
 *
 * <h3>Example Code</h3>
 *
 * <pre>
GSSManager mgr = GSSManager.getInstance();

// start by creating a name object for the entity
GSSName name = mgr.createName("userName", GSSName.NT_USER_NAME);

// now acquire credentials for the entity
GSSCredential cred = mgr.createCredential(name,
                                          GSSCredential.ACCEPT_ONLY);

// display credential information - name, remaining lifetime,
// and the mechanisms it has been acquired over
print(cred.getName().toString());
print(cred.getRemainingLifetime());

Oid [] mechs = cred.getMechs();
if (mechs != null)
  {
    for (int i = 0; i < mechs.length; i++)
      print(mechs[i].toString());
  }

// release system resources held by the credential
cred.dispose();
 * </pre>
 */
public interface GSSCredential extends Cloneable {

    /**
   * Credential usage flag requesting that it be able to be used for both
   * context initiation and acceptance.
   */
    int INITIATE_AND_ACCEPT = 0;

    /**
   * Credential usage flag requesting that it be able to be used for
   * context initiation only.
   */
    int INITIATE_ONLY = 1;

    /**
   * Credential usage flag requesting that it be able to be used for
   * context acceptance only.
   */
    int ACCEPT_ONLY = 2;

    /**
   * A lifetime constant representing the default credential lifetime.
   */
    int DEFAULT_LIFETIME = 0;

    /**
   * A lifetime constant representing indefinite credential lifetime.
   */
    int INDEFINITE_LIFETIME = Integer.MAX_VALUE;

    /**
   * Releases any sensitive information that the GSSCredential object may
   * be containing.  Applications should call this method as soon as the
   * credential is no longer needed to minimize the time any sensitive
   * information is maintained.
   *
   * @throws GSSException If this operation fails.
   */
    void dispose() throws GSSException;

    /**
   * Retrieves the name of the entity that the credential asserts.
   *
   * @return The name.
   * @throws GSSException If this operation fails.
   */
    GSSName getName() throws GSSException;

    /**
   * Retrieves a mechanism name of the entity that the credential asserts.
   * Equivalent to calling {@link GSSName#canonicalize(org.ietf.jgss.Oid)}
   * on the name returned by {@link #getName()}.
   *
   * @param mechOID The mechanism for which information should be returned.
   * @return The name.
   * @throws GSSException If this operation fails.
   */
    GSSName getName(Oid mechOID) throws GSSException;

    /**
   * Returns the remaining lifetime in seconds for a credential.  The
   * remaining lifetime is the minimum lifetime for any of the underlying
   * credential mechanisms.  A return value of {@link
   * GSSCredential#INDEFINITE_LIFETIME} indicates that the credential does
   * not expire.  A return value of 0 indicates that the credential is
   * already expired.
   *
   * @return The remaining lifetime.
   * @throws GSSException If this operation fails.
   */
    int getRemainingLifetime() throws GSSException;

    /**
   * Returns the remaining lifetime is seconds for the credential to
   * remain capable of initiating security contexts under the specified
   * mechanism.  A return value of {@link GSSCredential#INDEFINITE_LIFETIME}
   * indicates that the credential does not expire for context initiation.
   * A return value of 0 indicates that the credential is already expired.
   *
   * @param mech The mechanism for which information should be returned.
   * @return The remaining lifetime.
   * @throws GSSException If this operation fails.
   */
    int getRemainingInitLifetime(Oid mech) throws GSSException;

    /**
   * Returns the remaining lifetime is seconds for the credential to
   * remain capable of accepting security contexts under the specified
   * mechanism.  A return value of {@link GSSCredential#INDEFINITE_LIFETIME}
   * indicates that the credential does not expire for context acceptance.
   * A return value of 0 indicates that the credential is already expired.
   *
   * @param mech The mechanism for which information should be returned.
   * @return The remaining lifetime.
   * @throws GSSException If this operation fails.
   */
    int getRemainingAcceptLifetime(Oid mech) throws GSSException;

    /**
   * Returns the credential usage flag.  The return value will be one of
   * {@link GSSCredential#INITIATE_ONLY}, {@link GSSCredential#ACCEPT_ONLY},
   * or {@link GSSCredential#INITIATE_AND_ACCEPT}.
   *
   * @return The credential usage flag.
   * @throws GSSException If this operation fails.
   */
    int getUsage() throws GSSException;

    /**
   * Returns the credential usage flag for the specified credential
   * mechanism.  The return value will be one of
   * {@link GSSCredential#INITIATE_ONLY}, {@link GSSCredential#ACCEPT_ONLY},
   * or {@link GSSCredential#INITIATE_AND_ACCEPT}.
   *
   * @param mechOID The mechanism for which information should be returned.
   * @return The credential usage flag.
   * @throws GSSException If this operation fails.
   */
    int getUsage(Oid mechOID) throws GSSException;

    /**
   * Returns an array of mechanisms supported by this credential.
   *
   * @return The supported mechanism.
   * @throws GSSException If this operation fails.
   */
    Oid[] getMechs() throws GSSException;

    /**
   * <p>Adds a mechanism specific credential-element to an existing
   * credential.  This method allows the construction of credentials one
   * mechanism at a time.</p>
   *
   * <p>This routine is envisioned to be used mainly by context acceptors
   * during the creation of acceptance credentials which are to be used
   * with a variety of clients using different security mechanisms.</p>
   *
   * <p>This routine adds the new credential element "in-place".  To add the
   * element in a new credential, first call {@link Cloneable#clone()} to
   * obtain a copy of this credential, then call its <code>add()</code>
   * method.</p>
   *
   * @param aName          Name of the principal for whom this credential
   *                       is to be acquired. Use <code>null</code> to
   *                       specify the default principal.
   * @param initLifetime   The number of seconds that credentials should
   *                       remain valid for initiating of security contexts.
   *                       Use {@link #INDEFINITE_LIFETIME} to request that
   *                       the credentials have the maximum permitted lifetime.
   *                       Use {@link GSSCredential#DEFAULT_LIFETIME} to
   *                       request the default credential lifetime.
   * @param acceptLifetime The number of seconds that credentials should
   *                       remain valid for accepting of security contexts.
   *                       Use {@link GSSCredential#INDEFINITE_LIFETIME} to
   *                       request that the credentials have the maximum
   *                       permitted lifetime. Use {@link
   *                       GSSCredential#DEFAULT_LIFETIME} to request
   *                       the default credential lifetime.
   * @param mech           The mechanisms over which the credential is to be
   *                       acquired.
   * @param usage          The intended usage for this credential object. The
   *                       value of this parameter must be one of:
   *                       {@link GSSCredential#ACCEPT_AND_INITIATE},
   *                       {@link GSSCredential#ACCEPT_ONLY},
   *                       {@link GSSCredential#INITIATE_ONLY}.
   * @throws GSSException If this operation fails.
   */
    void add(GSSName aName, int initLifetime, int acceptLifetime, Oid mech, int usage) throws GSSException;

    /**
   * Tests if this GSSCredential refers to the same entity as the supplied
   * object.  The two credentials must be acquired over the same
   * mechanisms and must refer to the same principal. Returns <code>true</code>
   * if the two GSSCredentials refer to the same entity; <code>false</code>
   * otherwise. (Note that the Java language specification requires that two
   * objects that are equal according to the {@link
   * Object#equals(java.lang.Object)} method must return the same integer
   * result when the {@link Object#hashCode()} method is called on them.)
   *
   * @param another Another GSSCredential object for comparison.
   * @return True if this object equals the other.
   */
    boolean equals(Object another);

    /**
   * Return the hash code of this credential. When overriding {@link #equals},
   * it is necessary to override hashCode() as well.
   *
   * @return the hash code that must be the same for two credentials if
   * {@link #equals} returns true.
   */
    int hashCode();
}
