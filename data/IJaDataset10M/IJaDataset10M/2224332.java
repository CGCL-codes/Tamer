package org.apache.harmony.auth;

/** 
 * A principal which holds information about NT user basing on its sid.
 */
public class NTSidUserPrincipal extends NTSid {

    private static final long serialVersionUID = -76980455882379611L;

    /**
     * A constructor which takes user SID as its only argument. 
     * @param sid user SID
     */
    public NTSidUserPrincipal(String sid) {
        super(sid);
    }

    /**
     * A constructor which takes an extended set of information - user SID, 
     * its name and its domain name 
     * @param sid user SID
     * @param objName user name
     * @param objDomain name of user's domain
     */
    public NTSidUserPrincipal(String sid, String objName, String objDomain) {
        super(sid, objName, objDomain);
    }
}
