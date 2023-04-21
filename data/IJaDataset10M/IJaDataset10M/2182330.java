package org.dcm4che2.net.pdu;

/**
 * @author gunter zeilinger(gunterze@gmail.com)
 * @version $Reversion$ $Date: 2007-11-23 06:42:30 -0500 (Fri, 23 Nov 2007) $
 * @since Sep 15, 2005
 */
public class AAssociateAC extends AAssociateRQAC {

    protected UserIdentityAC userIdentity;

    @Override
    public String toString() {
        return super.toString("A-ASSOCIATE-AC");
    }

    public final UserIdentityAC getUserIdentity() {
        return userIdentity;
    }

    public final void setUserIdentity(UserIdentityAC userIdentity) {
        this.userIdentity = userIdentity;
    }

    @Override
    public int userInfoLength() {
        int len = super.userInfoLength();
        if (userIdentity != null) len += 4 + userIdentity.length();
        return len;
    }

    @Override
    protected void appendUserIdentity(StringBuffer sb) {
        if (userIdentity != null) sb.append("\n  ").append(userIdentity);
    }
}
