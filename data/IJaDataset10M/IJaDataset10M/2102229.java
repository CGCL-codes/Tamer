package gov.lanl.TMAuthen;

import org.omg.PersonIdService.*;
import org.omg.NamingAuthority.*;
import gov.lanl.PidServer.*;
import gov.lanl.Authenticate.*;
import gov.lanl.TMCryptography.*;
import java.util.*;
import java.io.*;
import java.awt.*;

/**Provides persistent management for a user list
* including managing the list, fetching copies...
* @author $Author: dwforslund $
* @version $Revision: 4 $ $Date: 2000-08-12 00:37:42 -0400 (Sat, 12 Aug 2000) $
*/
public class PUserList {

    /**Debug flag*/
    public boolean debug = false;

    IdentificationComponentImpl body = null;

    /**Selector for indicating name and password search*/
    public static final int USE_NAME_AND_PWD = 0;

    /**Selector for indicating moniker search*/
    public static final int USE_MONIKER = 1;

    /**Selector for indicating encrypted password search*/
    public static final int USE_NAME_AND_ENCRYPTPWD = 2;

    /**Selector for asking for data for the client*/
    public static final int CLIENT_DATA = 0;

    /**Selector for asking for data for the server*/
    public static final int SERVER_DATA = 1;

    /**Selector for asking for data for the owner of the object*/
    public static final int ALL_DATA = 2;

    /**constructors*/
    public PUserList(IdentificationComponentImpl pids) {
        body = pids;
    }

    public PUserList(IdentificationComponentImpl pids, boolean inDebug) {
        body = pids;
        debug = inDebug;
    }

    /**create a copy of the byte array
*@param inbytes the byte array to copy
*/
    byte[] createByteArrayCopy(byte[] inbytes) {
        if (inbytes == null) return new byte[0];
        byte[] retBytes = new byte[inbytes.length];
        for (int i = 0; i < retBytes.length; i++) retBytes[i] = inbytes[i];
        return retBytes;
    }

    /**Internal method for restricting and copying data returned
	*@param whichUser the user data to copy what is allowed
	*@param requesterKind selector for returned data
	*/
    UserData restrictUserData(UserData whichUser, int requesterKind) {
        UserData retUser = new UserData();
        try {
            if (whichUser == null) return null;
            if (whichUser != null) {
                retUser.userID = new String(whichUser.userID);
                byte[] emptybytes = new byte[0];
                retUser.userSD = new SessionData(emptybytes, emptybytes, emptybytes, emptybytes);
                retUser.userNameID = new String(whichUser.userNameID);
                retUser.password = new String("");
                retUser.realName = new String(whichUser.realName);
                retUser.userPolicy = new String(whichUser.userPolicy);
                retUser.userCertificate = createByteArrayCopy(whichUser.userCertificate);
                retUser.userQualName = whichUser.userQualName;
                if (retUser.userQualName == null) {
                    retUser.userQualName = new org.omg.NamingAuthority.QualifiedName(new org.omg.NamingAuthority.AuthorityId(org.omg.NamingAuthority.RegistrationAuthority.DNS, new String("lanl.gov")), new String(whichUser.realName));
                }
                switch(requesterKind) {
                    case CLIENT_DATA:
                        break;
                    case SERVER_DATA:
                        if (whichUser.userSD != null) {
                            retUser.userSD.moniker = createByteArrayCopy(whichUser.userSD.moniker);
                            retUser.userSD.randomKey = createByteArrayCopy(whichUser.userSD.randomKey);
                            retUser.userSD.randomIV = createByteArrayCopy(whichUser.userSD.randomIV);
                            retUser.userSD.algorithm = createByteArrayCopy(whichUser.userSD.algorithm);
                        }
                        break;
                    case ALL_DATA:
                        if (whichUser.userSD != null) {
                            retUser.userSD.moniker = createByteArrayCopy(whichUser.userSD.moniker);
                            retUser.userSD.randomKey = createByteArrayCopy(whichUser.userSD.randomKey);
                            retUser.userSD.randomIV = createByteArrayCopy(whichUser.userSD.randomIV);
                            retUser.userSD.algorithm = createByteArrayCopy(whichUser.userSD.algorithm);
                        }
                        retUser.password = new String(whichUser.password);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return retUser;
    }

    /**Internal method to find the user in the list
	* Must be called inside of a Transaction
	* @param whichUser is PUserData (possibly missing or partilly filled)
	* data about the user being sought
	* @param encryptPwd is password (possible encrypted or empty) to use
	* @how is the selector of how to compare
	*/
    UserData lookupUser(UserData whichUser, byte[] encryptPwd, int how) throws Exception {
        TMKeyCrypto aUserCrypto;
        String tmppwd;
        UserData[] aUser = null;
        UserData retUser = null;
        int i, j;
        boolean allMatched;
        boolean match = false;
        Candidate[] cand;
        try {
            Trait_ ulogin = TraitFactory.current().newTrait();
            ulogin.init((Identity_) null, "NODE", "Login");
            Trait_ userinfo = TraitFactory.current().newTrait();
            userinfo.init(ulogin, "NODE", "UserInfo");
            Trait_ name = TraitFactory.current().newTrait();
            name.init(userinfo, "S", "UserName");
            name.setValue(whichUser.userNameID);
            userinfo.addTrait(name);
            ulogin.addTrait(userinfo);
            Trait newt = (Trait) ulogin.toObject();
            TraitSelector selTrait[] = new TraitSelector[] { new TraitSelector(newt, (float) 0.5) };
            IdState[] searchStates = new IdState[] { IdState.PERMANENT };
            SpecifiedTraits theSpecTraits = new SpecifiedTraits();
            theSpecTraits.traits(new String[] { "NODE/Login", "B/X509Certificate", "HL7/ProviderName" });
            CandidateSeqHolder tmp_cand_seq = new CandidateSeqHolder();
            CandidateSeqHolder tmp_cand_seq2 = new CandidateSeqHolder();
            CandidateIteratorHolder tmp_cand_itr = new CandidateIteratorHolder();
            try {
                body.identify_person().find_candidates(selTrait, searchStates, (float) 1.0, 100, 100, theSpecTraits, tmp_cand_seq, tmp_cand_itr);
            } catch (Exception e) {
                System.out.println(e);
            }
            Trait_ tmp_trait;
            if (tmp_cand_seq != null) {
                cand = tmp_cand_seq.value;
                for (i = 0; i < cand.length; i++) {
                    Trait[] profile = cand[i].profile;
                    byte[] tmp_cert = null;
                    String realName = null;
                    for (j = 0; j < profile.length; j++) {
                        tmp_trait = TraitFactory.current().newTrait();
                        tmp_trait.init((Identity_) null, profile[j]);
                        if (tmp_trait.getName().equals("NODE/Login")) {
                            aUser = new UserData[tmp_trait.getNumber()];
                            for (int k = 0; k < aUser.length; k++) {
                                Trait_[] t = (Trait_[]) tmp_trait.getValue();
                                Trait_[] tmp = (Trait_[]) t[0].getValue();
                                String login_name = "";
                                String password = "";
                                String user_policy = "";
                                for (int l = 0; l < tmp.length; l++) {
                                    if (tmp[l].getName().equals("S/UserName")) login_name = (String) tmp[l].getValue();
                                    if (tmp[l].getName().equals("S/Password")) password = (String) tmp[l].getValue();
                                    if (tmp[l].getName().equals("S/Role")) user_policy = (String) tmp[l].getValue();
                                }
                                aUser[k] = new UserData(cand[i].id, null, login_name, password, null, null, user_policy, null);
                            }
                        } else if (tmp_trait.getName().equals("B/X509Certificate")) {
                            tmp_cert = (byte[]) tmp_trait.getValue();
                        } else if (tmp_trait.getName().equals("HL7/ProviderName")) {
                            realName = (String) tmp_trait.getValue();
                        }
                    }
                    if (tmp_cert != null) for (int l = 0; l < aUser.length; l++) {
                        aUser[l].userCertificate = tmp_cert;
                    }
                    if (realName != null) for (int l = 0; l < aUser.length; l++) {
                        aUser[l].realName = realName;
                    }
                    for (int l = 0; l < aUser.length; l++) {
                        if (aUser[l].userNameID.equals(whichUser.userNameID)) {
                            printUserData(aUser[l]);
                            if (debug) System.out.println("lookupUser " + whichUser + " " + aUser[l].userNameID);
                            if (aUser[l].userCertificate != null) {
                                try {
                                    aUserCrypto = new IAIKRSACrypto(null, aUser[l].userCertificate);
                                    tmppwd = new String(aUserCrypto.decrypt(encryptPwd, TMKeyCrypto.USE_PUBLIC_KEY));
                                    if (debug) System.out.println("  pwd " + tmppwd);
                                    if (aUser[l].password.equals(tmppwd)) {
                                        if (debug) System.out.println("Found user: " + aUser);
                                        retUser = aUser[l];
                                        return retUser;
                                    }
                                } catch (Exception e) {
                                    System.out.println("lookupUser " + e);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exx) {
            exx.printStackTrace();
        }
        if (retUser == null) System.out.println("LookupUser failed");
        return retUser;
    }

    /**Login this user by name and password (possibly encrypted)
	*and return the user data for this user
	*@param name the nume to use
	*@param encryptpwd the possible encrypted password
	*/
    public UserData loginUser(String name, byte[] encryptpwd) {
        UserData aUser = new UserData();
        aUser.userNameID = name;
        System.out.println("calling loginUser: " + name);
        try {
            aUser = lookupUser(aUser, encryptpwd, USE_NAME_AND_ENCRYPTPWD);
            UserData udata = restrictUserData(aUser, SERVER_DATA);
            return udata;
        } catch (Exception e) {
            System.out.println("loginUser failed: " + aUser.toString() + e);
            return null;
        }
    }

    private void addUser(UserData udata) {
        Trait_ ulogin = TraitFactory.current().newTrait();
        ulogin.init(TraitFactory.current().newIdentity(), "NODE", "Login");
        Trait_ userinfo = TraitFactory.current().newTrait();
        userinfo.init(ulogin, "NODE", "UserInfo");
        Trait_ name = TraitFactory.current().newTrait();
        name.init(userinfo, "S", "UserName");
        Trait_ realName = TraitFactory.current().newTrait();
        realName.init(TraitFactory.current().newTrait(), "HL7", "ProviderName");
        realName.setValue(udata.realName);
        Trait_ cert = TraitFactory.current().newTrait();
        cert.init(TraitFactory.current().newIdentity(), "B", "X509Certificate");
        cert.setValue(udata.userCertificate);
        name.setValue(udata.userNameID);
        Trait_ role = TraitFactory.current().newTrait();
        role.init(userinfo, "S", "Role");
        role.setValue(udata.userPolicy);
        Trait_ password = TraitFactory.current().newTrait();
        password.init(userinfo, "S", "Password");
        password.setValue(udata.password);
        userinfo.addTrait(name);
        userinfo.addTrait(password);
        userinfo.addTrait(role);
        ulogin.addTrait(userinfo);
        Trait[] traits = new Trait[] { (Trait) ulogin.toObject(), (Trait) cert.toObject(), (Trait) realName.toObject() };
        Trait[][] ids = new Trait[][] { traits };
        try {
            body.id_mgr().find_or_register_ids(ids);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printUserData(UserData theUser) {
        System.out.println(theUser.userID + " " + "userNameID: " + theUser.userNameID);
    }

    public void addUsers(String userPath) {
        String auser;
        int userID = 2000;
        UserData udata;
        BufferedReader theuserfile = null;
        InputStream instream = null;
        byte[] publicKeyCertificate;
        try {
            theuserfile = new BufferedReader(new FileReader(userPath + "users.txt"));
            do {
                auser = theuserfile.readLine();
                if (auser != null) {
                    if (auser.length() > 0) {
                        System.out.println("Making entry for " + auser);
                        BufferedReader userdata = new BufferedReader(new FileReader(userPath + auser + "/data.txt"));
                        udata = new UserData();
                        udata.userID = "User_" + userID++;
                        udata.userNameID = userdata.readLine();
                        udata.password = userdata.readLine();
                        udata.realName = userdata.readLine();
                        udata.userPolicy = userdata.readLine();
                        userdata.close();
                        instream = new FileInputStream(userPath + auser + "/public");
                        publicKeyCertificate = new byte[instream.available()];
                        instream.read(publicKeyCertificate);
                        instream.close();
                        udata.userCertificate = publicKeyCertificate;
                        addUser(udata);
                    }
                }
            } while (auser != null);
        } catch (Exception e) {
            System.out.println("Error buildValidUsers " + e);
        }
        try {
            if (theuserfile != null) theuserfile.close();
        } catch (Exception e) {
        }
    }
}
