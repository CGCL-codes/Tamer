package org.jets3t.service.acl;

/**
 * Represents a Group grantee.
 * <p>
 * Only three groups are available in S3:<br>
 * <tt>ALL_USERS</tt>: The general public<br>
 * <tt>AUTHENTICATED_USERS</tt>: Authenticated Amazon S3 users<br> 
 * <tt>LOG_DELIVERY</tt>: Amazon's S3 Log Delivery group, who deliver bucket log files<br> 
 * 
 * @author James Murty
 *
 */
public class GroupGrantee implements GranteeInterface {

    /**
     * The group of all users, represented in S3 by the URI:  
     * http://acs.amazonaws.com/groups/global/AllUsers
     */
    public static final GroupGrantee ALL_USERS = new GroupGrantee("http://acs.amazonaws.com/groups/global/AllUsers");

    /**
     * The group of authenticated users, represented in S3 by the URI:  
     * http://acs.amazonaws.com/groups/global/AuthenticatedUsers
     */
    public static final GroupGrantee AUTHENTICATED_USERS = new GroupGrantee("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");

    /**
     * The group of Bucket Log delivery users, represented in S3 by the URI:  
     * http://acs.amazonaws.com/groups/s3/LogDelivery
     */
    public static final GroupGrantee LOG_DELIVERY = new GroupGrantee("http://acs.amazonaws.com/groups/s3/LogDelivery");

    private String uri = null;

    public GroupGrantee() {
    }

    /**
     * Constructs a group grantee object using the given group URI as an identifier.
     * <p>
     * <b>Note</b>: All possible group types are available as public static variables from this class,
     * so this constructor should rarely be necessary.  
     * 
     * @param groupUri
     */
    public GroupGrantee(String groupUri) {
        this.uri = groupUri;
    }

    public String toXml() {
        return "<Grantee xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"Group\"><URI>" + uri + "</URI></Grantee>";
    }

    /**
     * Set the group grantee's URI.
     */
    public void setIdentifier(String uri) {
        this.uri = uri;
    }

    /**
     * Returns the group grantee's URI.
     */
    public String getIdentifier() {
        return uri;
    }

    public String toString() {
        return "GroupGrantee [" + uri + "]";
    }

    public boolean equals(Object obj) {
        if (obj instanceof GroupGrantee) {
            return uri.equals(((GroupGrantee) obj).uri);
        }
        return false;
    }

    public int hashCode() {
        return uri.hashCode();
    }
}
