package org.jabber.xdb.jabberbeans;

import org.jabber.jabberbeans.*;
import org.jabber.jabberbeans.Extension.*;

public class XDBIQAuth extends XMLData implements QueryExtension {

    /** a <code>String</code> which represents the user name on the server. */
    private String username;

    /** a <code>String</code> which represents the session resource. */
    private String resource;

    /** a <code>String</code> which holds the password for this user. */
    private String password;

    /** 
	 * a <code>String</code> which holds the SHA1 Digest for this user.
	 * The digest authentication is a way to log in without sending the
	 * password over the wire in plaintext. Both entities are still required
	 * to know the plaintext password. The server sends a SessionID as part
	 * of the initial <stream:stream> tag. This values is prepended to the
	 * user password, then run through a SHA hash and converted to Hex format.
	 * Hashes are non-reversable, so the original password is not discernable,
	 * but the server can perform the same steps and compare output to log
	 * a user in.
	 *
	 * @see org.jabber.jabberbeans.util.SHA1Helper
	 */
    private String SHA1Digest;

    /**
	 * <code>getUsername</code> returns the value of the username attribute
	 *
	 * @return a <code>String</code> value
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * <code>getResource</code> returns the value of the resource attribute
	 *
	 * @return a <code>String</code> value
	 */
    public String getResource() {
        return resource;
    }

    /**
	 * <code>getPassword</code> returns the value used for the password
	 *
	 * @return a <code>String</code> value
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * <code>isSHA1Digest</code> differentiates between the passkey being
	 * a password or SHA1 key
	 *
	 * @return a <code>boolean</code> value
	 */
    public String getSHA1Digest() {
        return SHA1Digest;
    }

    /**
	 * <code>appendItem</code> appends the XML representation of the
	 * current packet data to the specified <code>StringBuffer</code>.
	 *
	 * @param retval The <code>StringBuffer</code> to append to
	 */
    public void appendItem(StringBuffer retval) {
        if (usePlaintextAuth == true) {
            retval.append("<password xmlns=\"jabber:iq:auth\">");
            retval.append(password);
            retval.append("</password>");
        } else {
            retval.append("<query xmlns=\"jabber:iq:auth\">");
            appendChild(retval, "username", username);
            appendChild(retval, "password", password);
            appendChild(retval, "digest", SHA1Digest);
            appendChild(retval, "resource", resource);
            retval.append("</query>");
        }
    }

    /** a <code>boolean</code> which determines whether plaintext authentication is used */
    private boolean usePlaintextAuth;

    /** Creates a new <code>IQAuthExtension</code> instance. This will fail if
	 * the builder does not have sufficient or correct information for an IQ
	 * Auth snippet.
	 *
	 * @param builder an <code>IQAuthExtensionBuilder</code> which holds the
	 * values we are using.
	 *
	 * @exception InstantiationException thrown if the builder does not hold
	 * sufficient data to create a valid Extension snippet.
	 */
    public XDBIQAuth(XDBIQAuthBuilder builder) throws InstantiationException {
        username = builder.getUsername();
        resource = builder.getResource();
        password = builder.getPassword();
        usePlaintextAuth = builder.getPlaintextAuth();
        SHA1Digest = builder.getSHA1Digest();
    }
}
