package org.hardtokenmgmt.initsuperadmin.ui;

import iaik.pkcs.pkcs11.TokenException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.cert.X509Certificate;
import org.hardtokenmgmt.core.token.IToken;
import org.hardtokenmgmt.core.token.OperationNotSupportedException;
import org.hardtokenmgmt.core.token.IToken.InitializationRequirements;
import org.hardtokenmgmt.core.ui.BaseController;
import org.hardtokenmgmt.core.util.ControllerMemory;
import org.hardtokenmgmt.ui.ErrorController;

/**
 * 
 * Controller used for to prompt card insertion of super administrator
 * card.
 * 
 * <p>Controller Memory Settings:
 * <ul>
 * <li>SOPIN the entered current SOPIN of the card or null if no SOPIN is required to initialize.
 * </ul>
 * 
 * @author Philip Vendil 
 *
 * @version $Id$
 */
public class CurrentSOPINController extends BaseController {

    public CurrentSOPINController() {
        super(new CurrentSOPINView());
        getCurrentSOPINView().getNextButton().addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                getControllerMemory().putData("SOPIN", null);
                getCurrentSOPINView().clearErrorMessages();
                String sSOPIN = getCurrentSOPINView().getSOPINField().getText();
                String verifySSOPIN = getCurrentSOPINView().getVerifySOPINField().getText();
                if (sSOPIN.trim().equals("")) {
                    getCurrentSOPINView().displayErrorMessage("initsuperadmincard.notempty");
                } else {
                    if (!sSOPIN.equals(verifySSOPIN)) {
                        getCurrentSOPINView().displayErrorMessage("initsuperadmincard.nomatch");
                    } else {
                        try {
                            if (verifySSOPin(sSOPIN)) {
                                IToken token = getProcessableToken();
                                String[] pintypes = token.getSupportedPINTypes();
                                String[] sSOPINs = new String[pintypes.length];
                                for (int i = 0; i < sSOPINs.length; i++) {
                                    sSOPINs[i] = sSOPIN;
                                }
                                getControllerMemory().putData("SOPIN", sSOPINs);
                                switchControlTo(InitSuperAdminController.class.getName());
                            } else {
                                getCurrentSOPINView().displayErrorMessage("initsuperadmincard.faultysopin");
                            }
                        } catch (Exception e) {
                            error("Error when verifying SSO PIN for the super administrator card, the error is : ", e);
                            ControllerMemory.getInstance().putData(ErrorController.CCERROREXCEPTION, e);
                            switchControlTo(ErrorController.class.getName());
                        }
                    }
                }
            }

            private boolean verifySSOPin(String ssopin) throws OperationNotSupportedException, TokenException {
                return getProcessableToken().checkSSOPIN(getProcessableToken().getSupportedPINTypes()[0], ssopin);
            }
        });
    }

    private CurrentSOPINView getCurrentSOPINView() {
        return (CurrentSOPINView) getView();
    }

    /**
	 * Method called by the Main applet when it's time for this
	 * controller to take control
	 * 
	 * @see org.hardtokenmgmt.core.ui.IController#getControl(String)
	 */
    public void getControl(String callingController) {
        getControllerMemory().putData("ISADMIN", null);
        getCurrentSOPINView().clearErrorMessages();
        try {
            IToken token = getProcessableToken();
            if (token.getInitializationRequirements() == InitializationRequirements.NOSOPINREQUIRED) {
                switchControlTo(InitSuperAdminController.class.getName());
            }
        } catch (Exception e) {
            error("Error when verifying SSO PIN for the super administrator card, the error is : ", e);
            ControllerMemory.getInstance().putData(ErrorController.CCERROREXCEPTION, e);
            switchControlTo(ErrorController.class.getName());
        }
    }

    /**
     * Method called by the main applet to check that
     * the administrator is authorized to this controller
     * 
     * @see org.hardtokenmgmt.core.ui.IController#isAuthorizedToController(X509Certificate)
     */
    public boolean isAuthorizedToController(X509Certificate admin) {
        return true;
    }
}
