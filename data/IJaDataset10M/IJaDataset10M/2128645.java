package net.sourceforge.myvd.protocol.ldap.mina.ldap.message;

/**
 * SearchResponseReference implementation
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Revision: 437007 $
 */
public class SearchResponseReferenceImpl extends AbstractResponse implements SearchResponseReference {

    static final long serialVersionUID = 7423807019951309810L;

    /** Referral holding the reference urls */
    private Referral referral;

    /**
     * Creates a Lockable SearchResponseReference as a reply to an SearchRequest
     * to indicate the end of a search operation.
     * 
     * @param id
     *            the session unique message id
     */
    public SearchResponseReferenceImpl(final int id) {
        super(id, TYPE);
    }

    /**
     * Gets the sequence of LdapUrls as a Referral instance.
     * 
     * @return the sequence of LdapUrls
     */
    public Referral getReferral() {
        return this.referral;
    }

    /**
     * Sets the sequence of LdapUrls as a Referral instance.
     * 
     * @param referral
     *            the sequence of LdapUrls
     */
    public void setReferral(Referral referral) {
        this.referral = referral;
    }

    /**
     * Checks to see if an object is equal to this SearchResponseReference stub.
     * 
     * @param obj
     *            the object to compare to this response stub
     * @return true if the objects are equivalent false otherwise
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        SearchResponseReference resp = (SearchResponseReference) obj;
        if (this.referral != null && resp.getReferral() == null) {
            return false;
        }
        if (this.referral == null && resp.getReferral() != null) {
            return false;
        }
        if (this.referral != null && resp.getReferral() != null) {
            if (!this.referral.equals(resp.getReferral())) {
                return false;
            }
        }
        return true;
    }
}
