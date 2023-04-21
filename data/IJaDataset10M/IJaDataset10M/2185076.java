package org.hardtokenmgmt.ui.createcard;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import org.ejbca.core.protocol.ws.client.gen.UserDataVOWS;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.token.IToken;

/**
 * 
 * Default implementation of the username generator
 * where a defined prefix "creatingcardusernameprefix" set in global.properties along
 * with the personal number is returned as username.
 * 
 * @author Philip Vendil 2007 maj 23
 *
 * @see org.hardtokenmgmt.ui.createcard.UserDataGenerator
 * @version $Id$
 */
public class DefaultUserDataGenerator implements UserDataGenerator {

    public String generateUsername(String personalNumber) {
        String prefix = InterfaceFactory.getGlobalSettings().getProperties().getProperty("creatingcardcontroller.usernameprefix");
        if (prefix == null) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: DefaultUsernameGenerator is missconfigured, the property creatingcardcontroller.usernameprefix isn't set");
        }
        prefix = prefix.trim();
        return prefix + personalNumber;
    }

    public UserDataVOWS getUserDataVOWS(String username, String serialNumber, String commonName, UserDataVOWS userDataVo) {
        UserDataVOWS tokenUser1 = new UserDataVOWS();
        tokenUser1.setUsername(username);
        tokenUser1.setPassword(getRandomPassword());
        tokenUser1.setClearPwd(true);
        tokenUser1.setStatus(10);
        if (userDataVo != null) {
            tokenUser1.setSubjectDN(userDataVo.getSubjectDN());
            tokenUser1.setCaName(userDataVo.getCaName());
            tokenUser1.setEmail(userDataVo.getEmail());
            tokenUser1.setSubjectAltName(userDataVo.getSubjectAltName());
            tokenUser1.setTokenType(userDataVo.getTokenType());
            tokenUser1.setEndEntityProfileName(userDataVo.getEndEntityProfileName());
            tokenUser1.setCertificateProfileName(userDataVo.getCertificateProfileName());
        } else {
            tokenUser1.setSubjectDN("CN=" + commonName + ", SN=" + serialNumber + "," + getControllerSetting("basedn").trim());
            tokenUser1.setCaName(getCANames().get(0));
            tokenUser1.setEmail(getControllerSetting("defaultemail"));
            if (getControllerSetting("upndomain") != null) {
                tokenUser1.setSubjectAltName("upn=" + serialNumber + "@" + getControllerSetting("upndomain"));
            }
            tokenUser1.setTokenType("USERGENERATED");
            tokenUser1.setEndEntityProfileName(getControllerSetting("defaultententityprofile"));
            tokenUser1.setCertificateProfileName(getCertificateProfileNames().get(0));
        }
        return tokenUser1;
    }

    /**
	 * Simulates the CreatingCardController getControllerSetting method
	 */
    private String getControllerSetting(String key) {
        return InterfaceFactory.getGlobalSettings().getProperties().getProperty("creatingcardcontroller." + key);
    }

    private String getRandomPassword() {
        String ret = "";
        Random rand = new Random();
        while (ret.length() < 16) {
            ret += "" + rand.nextInt();
        }
        ret = ret.substring(ret.length() - 16);
        return ret;
    }

    /**
	 * Method returning an arraylist of certificateProfileNames to use
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
                    } else {
                        if (types[i].equalsIgnoreCase("sign")) {
                            keyTypes.add(IToken.KEYTYPE_SIGN);
                        } else {
                            LocalLog.getLogger().log(Level.SEVERE, "Error: only the values 'basic' and 'sign' is supported in the setting creatingcardcontroller.keytypes ");
                        }
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
	 * If the first two character isn't '19' they are replaced with '19';
	 */
    public String getSpecialSerialNumber(String serialNumber) {
        String retval = serialNumber;
        if (!retval.startsWith("19")) {
            retval = retval.substring(2);
            retval = "19" + retval;
        }
        return retval;
    }

    public String isLegalSerialNumber(String serialNumber) {
        if (serialNumber == null) return "tooltip.serialnumber";
        if (serialNumber.length() != 12) {
            return "tooltip.serialnumber";
        }
        char c[] = serialNumber.toCharArray();
        int len = c.length;
        for (int i = 0; i < len; i++) {
            if (c[i] < '0' || c[i] > '9') return "tooltip.serialnumber";
        }
        return null;
    }
}
