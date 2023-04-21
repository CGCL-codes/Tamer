package gov.lanl.TMAuthenTools;

import gov.lanl.TMCryptography.*;
import java.security.*;
import iaik.security.provider.IAIK;

/**
	*  Static class to build a Policy object
	* @version 5/6/99
*/
public class PolicyFactory {

    /**MedDataService crypto class */
    private static TMKeyCrypto PidsCrypto;

    /**The agent for the authenticator*/
    private static AuthenAgent theAgent = null;

    private static Policy accessPolicy = null;

    /**
    * static method to create a Policy object
    * @param parent  CORBA object which will use this policy object
    * @param theorb  The ORB to which the object is attached
    * @param privKeyInfoPath  Path to private key object for authentication
    * @param privPwd   PIN to decrypt and use private key
    * @param certPath  Path to Public certificate
    * @param authenServerName  Name of authentication server to connect to
    * @param loginName  Authentication server loginName (defines the role)
    * @param loginPwd  Authentication server password for loginName  (defines the role)
    * @param minsOfLife is the minutes of life for the policy object
    * @param debug  Debugging variable
    */
    public static Policy createPolicy(org.omg.CORBA.Object parent, org.omg.CORBA.ORB theorb, String privKeyInfoPath, String privPwd, String certPath, String authenServerName, String loginName, String loginPwd, int minsOfLife, boolean debug) {
        IAIK.addAsProvider(true);
        try {
            if (privKeyInfoPath.equalsIgnoreCase("null")) PidsCrypto = new IAIKRSACrypto(); else PidsCrypto = new IAIKRSACrypto(privKeyInfoPath, privPwd, certPath);
        } catch (Exception e) {
            System.out.println(e);
        }
        theAgent = new AuthenAgent(theorb, authenServerName, loginName, loginPwd, PidsCrypto, debug);
        System.out.println("theAgent is: " + theAgent);
        accessPolicy = new Policy(theAgent, parent, minsOfLife, debug);
        return (accessPolicy);
    }

    public static Policy current() {
        return accessPolicy;
    }
}
