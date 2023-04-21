package org.hardtokenmgmt.core.util;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import org.hardtokenmgmt.ws.gen.EjbcaException_Exception;
import org.hardtokenmgmt.ws.gen.UserDataVOWS;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.settings.GlobalSettings;
import org.hardtokenmgmt.core.token.IToken;
import org.hardtokenmgmt.core.ui.ComboBoxItem;

/**
 * 
 * Default implementation of the username generator
 * where a defined prefix "creatingcardusernameprefix" set in global.properties along
 * with the personal number is returned as username.
 * 
 * @author Philip Vendil 2007 maj 23
 *
 * @see org.hardtokenmgmt.core.util.UserDataGenerator
 * @version $Id$
 */
public class DefaultUserDataGenerator implements UserDataGenerator {

    private static final String ADMININSTRATORCARD_POSTFIX = "_ADM";

    private String profile;

    private GlobalSettings gs;

    public void init(String profile, GlobalSettings gs) {
        this.profile = profile;
        this.gs = gs;
    }

    public String generateUsername(String serialNumber) {
        return generateUsername(serialNumber, true);
    }

    public UserDataVOWS getUserDataVOWS(String username, String serialNumber, String commonName, UserDataVOWS userDataVo) {
        UserDataVOWS tokenUser1 = new UserDataVOWS();
        tokenUser1.setUsername(username);
        tokenUser1.setPassword(getRandomPassword());
        tokenUser1.setClearPwd(true);
        tokenUser1.setStatus(10);
        if (userDataVo != null) {
            if (profile != null && profile.equals("profile.admincard") && ToLiMaUtils.getPersonalNumber(userDataVo.getSubjectDN(), gs) != null) {
                String personalNumber = ToLiMaUtils.getPersonalNumber(userDataVo.getSubjectDN(), gs);
                tokenUser1.setSubjectDN(userDataVo.getSubjectDN().replaceAll("=" + personalNumber + ",", "=" + personalNumber + "ADM,"));
            } else {
                tokenUser1.setSubjectDN(userDataVo.getSubjectDN());
            }
            if (profile != null && profile.equals("profile.admincard") && userDataVo.getSubjectAltName() != null && CertUtils.getPartFromDN(userDataVo.getSubjectAltName(), CertUtils.UPN) != null) {
                String upn = CertUtils.getPartFromDN(userDataVo.getSubjectAltName(), CertUtils.UPN);
                String upnName = upn.substring(0, upn.indexOf("@"));
                tokenUser1.setSubjectAltName(userDataVo.getSubjectAltName().replaceAll(upn, upnName + "adm" + upn.substring(upn.indexOf("@"))));
            } else {
                tokenUser1.setSubjectAltName(userDataVo.getSubjectAltName());
            }
            tokenUser1.setCaName(userDataVo.getCaName());
            tokenUser1.setEmail(userDataVo.getEmail());
            tokenUser1.setTokenType(userDataVo.getTokenType());
            tokenUser1.setEndEntityProfileName(userDataVo.getEndEntityProfileName());
            tokenUser1.setCertificateProfileName(userDataVo.getCertificateProfileName());
        } else {
            if (profile != null && profile.equals("profile.admincard")) {
                tokenUser1.setSubjectDN("CN=" + commonName + ", SN=" + serialNumber + "ADM," + getControllerSetting("basedn").trim());
            } else {
                tokenUser1.setSubjectDN("CN=" + commonName + ", SN=" + serialNumber + "," + getControllerSetting("basedn").trim());
            }
            if (getControllerSetting("upndomain") != null) {
                if (profile != null && profile.equals("profile.admincard")) {
                    tokenUser1.setSubjectAltName("upn=" + serialNumber + "adm" + "@" + getControllerSetting("upndomain"));
                } else {
                    tokenUser1.setSubjectAltName("upn=" + serialNumber + "@" + getControllerSetting("upndomain"));
                }
            }
            tokenUser1.setCaName(getCANames().get(0));
            tokenUser1.setEmail(getControllerSetting("defaultemail"));
            tokenUser1.setTokenType("USERGENERATED");
            tokenUser1.setEndEntityProfileName(getControllerSetting("defaultendentityprofile"));
            tokenUser1.setCertificateProfileName(getCertificateProfileNames().get(0));
        }
        return tokenUser1;
    }

    /**
	 * Simulates the CreatingCardController getControllerSetting method
	 */
    private String getControllerSetting(String key) {
        return gs.getProperties().getProperty("creatingcardcontroller." + key);
    }

    public static String getRandomPassword() {
        String ret = "";
        Random rand = new Random();
        while (ret.length() < 16) {
            ret += "" + rand.nextInt();
        }
        ret = ret.substring(ret.length() - 16);
        return ret;
    }

    /**
	 * Method returning an array list of certificateProfileNames to use
	 */
    public ArrayList<String> getCertificateProfileNames() {
        if (certificateProfileNames == null) {
            if (getControllerSetting("certprofilenames") == null) {
                LocalLog.getLogger().log(Level.SEVERE, "Error: the required setting creatingcardcontroller.certprofilenames isn't set in the global.properties");
            } else {
                String[] names = getControllerSetting("certprofilenames").split(",");
                certificateProfileNames = new ArrayList<String>();
                for (int i = 0; i < names.length; i++) {
                    certificateProfileNames.add(names[i].trim());
                }
            }
        }
        return certificateProfileNames;
    }

    private ArrayList<String> certificateProfileNames = null;

    /**
	 * Method returning an arraylist of certificateProfileNames to use
	 */
    public ArrayList<String> getCANames() {
        if (cANames == null) {
            if (getControllerSetting("canames") == null) {
                LocalLog.getLogger().log(Level.SEVERE, "Error: the required setting creatingcardcontroller.canames isn't set in the global.properties");
            } else {
                String[] names = getControllerSetting("canames").split(",");
                cANames = new ArrayList<String>();
                for (int i = 0; i < names.length; i++) {
                    cANames.add(names[i].trim());
                }
            }
        }
        return cANames;
    }

    private ArrayList<String> cANames = null;

    /**
	 * Method returning an arraylist of keytypes that 
	 * should be used to generate the PKCS10. 
	 * Must be one of th IToken.KEYTYPE_ constants
	 * 
	 * Currently are only IToken.KEYTYPE_AUTH and IToken.KEYTYPE_SIGN supported.
	 */
    public ArrayList<String> getKeyTypes() {
        if (keyTypes == null) {
            if (getControllerSetting("keytypes") == null) {
                LocalLog.getLogger().log(Level.SEVERE, "Error: the required setting creatingcardcontroller.keytypes isn't set in the global.properties");
            } else {
                String[] types = getControllerSetting("keytypes").split(",");
                keyTypes = new ArrayList<String>();
                for (int i = 0; i < types.length; i++) {
                    if (types[i].equalsIgnoreCase("basic")) {
                        keyTypes.add(IToken.KEYTYPE_AUTH);
                    } else if (types[i].equalsIgnoreCase("sign")) {
                        keyTypes.add(IToken.KEYTYPE_SIGN);
                    } else if (types[i].equalsIgnoreCase("secondaryauth")) {
                        keyTypes.add(IToken.KEYTYPE_SECONDARY_AUTH);
                    } else {
                        LocalLog.getLogger().log(Level.SEVERE, "Error: only the values 'basic', 'sign' and 'secondaryauth' is supported in the setting creatingcardcontroller.keytypes ");
                    }
                }
            }
        }
        return keyTypes;
    }

    private ArrayList<String> keyTypes = null;

    /**
	 * Method returning an arraylist of certificate labels to use
	 */
    public ArrayList<String> getCertLabels() {
        if (certLabels == null) {
            if (getControllerSetting("certlabels") == null) {
                LocalLog.getLogger().log(Level.SEVERE, "Error: the required setting creatingcardcontroller.certlabels isn't set in the global.properties");
            } else {
                String[] names = getControllerSetting("certlabels").split(",");
                certLabels = new ArrayList<String>();
                for (int i = 0; i < names.length; i++) {
                    certLabels.add(names[i].trim());
                }
            }
        }
        return certLabels;
    }

    private ArrayList<String> certLabels = null;

    /**
	 * Removes the '-' character and adds 19 if it doesn't exists.
	 * If the first two character isn't '19' they are replaced with '19';
	 */
    public String getNormalizedSerialNumber(String serialNumber) {
        String retval = serialNumber;
        if (serialNumber == null || serialNumber.length() < 2) {
            return serialNumber;
        }
        String type = gs.getProperty("creatingcardcontroller.normalizedsn.type", "newswedishpersonalnumber");
        if (type.equalsIgnoreCase("newswedishpersonalnumber")) {
            retval = retval.replaceAll("-", "");
            if (retval.length() == 10) {
                retval = "19" + retval;
            }
        }
        if (type.equalsIgnoreCase("oldswedishpersonalnumber")) {
            if (serialNumber.length() == 12) {
                retval = serialNumber.substring(2, 8) + "-" + serialNumber.substring(8);
            }
            if (serialNumber.length() == 13) {
                retval = serialNumber.substring(2);
            }
        }
        return retval;
    }

    public String isLegalSerialNumber(String serialNumber) {
        if (serialNumber == null || serialNumber.length() == 0) return "tooltip.serialnumber";
        if (serialNumber.length() == 12) {
            char c[] = serialNumber.toCharArray();
            int len = c.length;
            for (int i = 0; i < len; i++) {
                if (c[i] < '0' || c[i] > '9') {
                    return "tooltip.serialnumber";
                }
            }
        } else {
            if (serialNumber.length() == 13) {
                char c[] = serialNumber.toCharArray();
                for (int i = 0; i < 8; i++) {
                    if (c[i] < '0' || c[i] > '9') {
                        return "tooltip.serialnumber";
                    }
                }
                if (c[8] != '-') {
                    return "tooltip.serialnumber";
                }
                for (int i = 9; i < 12; i++) {
                    if (c[i] < '0' || c[i] > '9') {
                        return "tooltip.serialnumber";
                    }
                }
            } else {
                if (serialNumber.length() == 11) {
                    char c[] = serialNumber.toCharArray();
                    for (int i = 0; i < 6; i++) {
                        if (c[i] < '0' || c[i] > '9') {
                            return "tooltip.serialnumber";
                        }
                    }
                    if (c[6] != '-') {
                        return "tooltip.serialnumber";
                    }
                    for (int i = 9; i < 10; i++) {
                        if (c[i] < '0' || c[i] > '9') {
                            return "tooltip.serialnumber";
                        }
                    }
                } else {
                    return "tooltip.serialnumber";
                }
            }
        }
        return null;
    }

    public ComboBoxItem[] getAuthorizedProfiles() {
        ComboBoxItem[] retval = { new ComboBoxItem("profile.regularuser") };
        boolean issueAdminCards = false;
        try {
            issueAdminCards = InterfaceFactory.getHTMFAdminInterface().isAuthorized("/hardtoken_functionality/issue_hardtoken_administrators");
        } catch (EjbcaException_Exception e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error when checking authorized profiles ", e);
        }
        if (issueAdminCards) {
            retval = new ComboBoxItem[] { new ComboBoxItem("profile.regularuser"), new ComboBoxItem("profile.admincard") };
        }
        return retval;
    }

    public String[] genererateAllPossibleUsernames(String serialNumber) {
        String[] usernames = new String[2];
        usernames[0] = generateUsername(serialNumber, false);
        usernames[1] = usernames[0] + ADMININSTRATORCARD_POSTFIX;
        return usernames;
    }

    public String getSerialNumberFromUsername(String username) {
        String serialNumber, prefix;
        prefix = gs.getProperties().getProperty("creatingcardcontroller.usernameprefix");
        if (prefix == null) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: DefaultUsernameGenerator is missconfigured, the property creatingcardcontroller.usernameprefix isn't set");
        }
        prefix = prefix.trim();
        serialNumber = username.substring(prefix.length());
        if (profile != null && profile.equals("profile.admincard")) {
            serialNumber = serialNumber.substring(0, serialNumber.length() - ADMININSTRATORCARD_POSTFIX.length());
        }
        return serialNumber;
    }

    private String generateUsername(String serialNumber, boolean supportProfile) {
        String prefix = gs.getProperties().getProperty("creatingcardcontroller.usernameprefix");
        if (prefix == null) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: DefaultUsernameGenerator is missconfigured, the property creatingcardcontroller.usernameprefix isn't set");
        }
        prefix = prefix.trim();
        String username = prefix + getNormalizedSerialNumber(serialNumber);
        if (supportProfile && profile != null && profile.equals("profile.admincard")) {
            username += ADMININSTRATORCARD_POSTFIX;
        }
        return username;
    }
}
