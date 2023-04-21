package javax.mail;

/**
 * An interface implemented by Stores that support quotas.
 * The {@link #getQuota getQuota} and {@link #setQuota setQuota} methods
 * support the quota model defined by the IMAP QUOTA extension.
 * Refer to <A HREF="http://www.ietf.org/rfc/rfc2087.txt">RFC 2087</A>
 * for more information. <p>
 *
 * @since JavaMail 1.4
 */
public interface QuotaAwareStore {

    /**
     * Get the quotas for the named quota root.
     * Quotas are controlled on the basis of a quota root, not
     * (necessarily) a folder.  The relationship between folders
     * and quota roots depends on the server.  Some servers
     * might implement a single quota root for all folders owned by
     * a user.  Other servers might implement a separate quota root
     * for each folder.  A single folder can even have multiple
     * quota roots, perhaps controlling quotas for different
     * resources.
     *
     * @param	root	the name of the quota root
     * @return		array of Quota objects
     * @exception MessagingException	if the server doesn't support the
     *					QUOTA extension
     */
    Quota[] getQuota(String root) throws MessagingException;

    /**
     * Set the quotas for the quota root specified in the quota argument.
     * Typically this will be one of the quota roots obtained from the
     * <code>getQuota</code> method, but it need not be.
     *
     * @param	quota	the quota to set
     * @exception MessagingException	if the server doesn't support the
     *					QUOTA extension
     */
    void setQuota(Quota quota) throws MessagingException;
}
