package org.dcm4che2.net.pdu;

/**
 * @author Gunter Zeilinger <gunterze@gmail.com>
 * @version $Revision: 5518 $ $Date: 2007-11-23 07:23:46 -0500 (Fri, 23 Nov 2007) $
 * @since Feb 12, 2007
 */
public class UserIdentityAC {

    private byte[] serverResponse = {};

    public final byte[] getServerResponse() {
        return serverResponse.clone();
    }

    public final void setServerResponse(byte[] serverResponse) {
        this.serverResponse = serverResponse.clone();
    }

    public int length() {
        return 2 + serverResponse.length;
    }

    @Override
    public String toString() {
        return "UserIdentity[serverResponse(" + serverResponse.length + ")]";
    }
}
