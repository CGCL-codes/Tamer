package org.hardtokenmgmt.core.util;

import java.security.cert.X509Certificate;
import org.hardtokenmgmt.common.Utils;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.settings.BasicGlobalSettings;
import org.hardtokenmgmt.core.settings.GlobalSettings;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.ui.selfservice.SelfServiceProcessor;
import org.hardtokenmgmt.ws.gen.ApprovalException_Exception;
import org.hardtokenmgmt.ws.gen.ApprovalRequestExecutionException_Exception;
import org.hardtokenmgmt.ws.gen.ApprovalRequestExpiredException_Exception;
import org.hardtokenmgmt.ws.gen.AuthorizationDeniedException_Exception;
import org.hardtokenmgmt.ws.gen.EjbcaException_Exception;
import org.hardtokenmgmt.ws.gen.WaitingForApprovalException_Exception;

/**
 * Help class containing help methods for common operations in ToLiMa 
 * Such as manipulating userdata
 * 
 * 
 * @author Philip Vendil 2007 jun 16
 *
 * @version $Id$
 */
public class ToLiMaUtils {

    /**
	 * Method returning the UserDataGenerator, used in administration console.
	 */
    public static UserDataGenerator getUserDataGenerator(GlobalSettings customerDefinedGlobalSettings) {
        UserDataGenerator retval = (UserDataGenerator) customerDefinedGlobalSettings.getCustomInterface("creatingcardcontroller.usernamegenerator");
        retval.init(null, customerDefinedGlobalSettings);
        return retval;
    }

    /**
	 * Method returning the UserDataGenerator, used in administration console.
	 */
    public static SelfServiceProcessor getSelfServiceProcessor(GlobalSettings customerDefinedGlobalSettings) {
        SelfServiceProcessor retval = (SelfServiceProcessor) customerDefinedGlobalSettings.getCustomInterface("selfservice.processor.impl");
        retval.init(customerDefinedGlobalSettings);
        return retval;
    }

    /**
	 * Help method returning true if reactivation functionality should be used.
	 */
    public static boolean useReactivation() {
        boolean retval = false;
        if (InterfaceFactory.getGlobalSettings().getProperties().getProperty("activatecard.usereactivation") != null) {
            retval = InterfaceFactory.getGlobalSettings().getProperties().getProperty("activatecard.usereactivation").trim().equalsIgnoreCase("true");
        }
        return retval;
    }

    /**
	 * Help method returning true if reactivation functionality should be used.
	 */
    public static boolean useRemoteUnblock() {
        boolean retval = false;
        if (InterfaceFactory.getGlobalSettings().getProperties().getProperty("withoutcardcontroller.useremoteunblock") != null) {
            retval = InterfaceFactory.getGlobalSettings().getProperties().getProperty("withoutcardcontroller.useremoteunblock").trim().equalsIgnoreCase("true");
        }
        return retval;
    }

    /**
	 * Help method returning true if non administrator functionality should be used.
	 */
    public static boolean useNonAdministratorFunctionality() {
        return InterfaceFactory.getGlobalSettings().getProperties().getProperty("usenonadminfunctionality", "TRUE").trim().equalsIgnoreCase("TRUE");
    }

    /**
	 * Method returning the serial number for a DN by looking up all the 
	 * different names for SN defined in global.properties.
	 * @param certificate
	 * @return the serial number in DN or null if no defined SN field were found.
	 */
    public static String getPersonalNumber(X509Certificate certificate, BasicGlobalSettings gs) {
        String sDN = CertUtils.getSubjectDN(certificate);
        return getPersonalNumber(sDN, gs);
    }

    /**
	 * Method returning the serial number for a DN by looking up all the 
	 * different names for SN defined in global.properties.
	 * @param subjectDN
	 * @return the serial number in DN or null if no defined SN field were found.
	 */
    public static String getPersonalNumber(String subjectDN, BasicGlobalSettings gs) {
        return Utils.getPersonalNumber(subjectDN, gs);
    }

    /**
	 * Method used to generate a random token SN.
	 * 
	 * The SN is generated from the prefix "token.snprefix" in global.properties and 9 random digits. 
	 * The method checks the database that the serial number is free before returning it.
	 * 
	 * @return a Random and available token serial number.
	 * @throws EjbcaException_Exception 
	 * @throws WaitingForApprovalException_Exception 
	 * @throws AuthorizationDeniedException_Exception 
	 * @throws ApprovalRequestExpiredException_Exception 
	 * @throws ApprovalRequestExecutionException_Exception 
	 * @throws ApprovalException_Exception 
	 */
    public static String genTokenSN() throws EjbcaException_Exception, ApprovalRequestExecutionException_Exception, ApprovalRequestExpiredException_Exception, AuthorizationDeniedException_Exception, WaitingForApprovalException_Exception, ApprovalException_Exception {
        String prefix = InterfaceFactory.getGlobalSettings().getProperties().getProperty("token.snprefix").trim();
        String retval = null;
        do {
            String serialNumber = TokenTools.generateCardSerialNumber(prefix);
            if (!InterfaceFactory.getHTMFAdminInterface().existsHardToken(serialNumber)) {
                retval = serialNumber;
            }
        } while (retval == null);
        return retval;
    }

    /**
	 * Help method that generates a translated header string for the create card title
	 * with correct total step depending on global configuration.
	 * 
	 * @param asCardAdmin true if the create card wizard is run as a card administrator.
	 * @param currentPage the current page.
	 * @return a full translated header string with current and total page.
	 */
    public static String genCreateCardWizardHeader(boolean asCardAdmin, int currentPage, boolean afterSetPIN) {
        String retval;
        int totalPages;
        if (asCardAdmin) {
            retval = UIHelper.getText("createcard.step");
            totalPages = 4;
        } else {
            retval = UIHelper.getText("notadmin.reservcard");
            totalPages = 6;
        }
        if (isSetPINBeforeCardInit()) {
            if (afterSetPIN) {
                currentPage++;
            }
            totalPages++;
        }
        return retval + " " + currentPage + "/" + totalPages;
    }

    /**
	 * Returns true if PIN should be set before initialization of the token.
	 * @return true if PIN should be set before initialization of the token.
	 */
    public static boolean isSetPINBeforeCardInit() {
        return InterfaceFactory.getGlobalSettings().getPropertyAsBoolean("creatingcardcontroller.setpinbeforeinit", false);
    }

    /**
	 * Method used to check if SelfService functionality should be used by the application
	 * 
	 * @true true if self service application should be used.
	 */
    public static boolean useSelfService() {
        return InterfaceFactory.getGlobalSettings().getPropertyAsBoolean("selfservice.use", false);
    }
}
