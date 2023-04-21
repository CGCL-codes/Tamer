package org.homedns.krolain.MochaJournal.Protocol;

import org.homedns.krolain.XMLRPC.*;

/**
 *
 * @author  jsmith
 */
public class XMLRPCLJ extends XMLRPCObject {

    private static String[] LJMembers = { "username", "auth_method", "password", "hpassword", "auth_challenge", "auth_response", "ver" };

    public String m_username = null;

    public String m_auth_method = null;

    public String m_password = null;

    public String m_hpassword = null;

    public String m_auth_challenge = null;

    public String m_auth_response = null;

    public Integer m_ver = null;

    /** Creates a new instance of XMLRPCLJ */
    public XMLRPCLJ(String[] members) {
        super(LJMembers);
        if (members != null) m_Members.addAll(java.util.Arrays.asList(members));
    }
}
