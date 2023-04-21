package org.hardtokenmgmt.ui.nocard;

import java.awt.Color;
import java.security.cert.X509Certificate;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.ui.BaseController;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.core.util.ControllerCache;
import org.hardtokenmgmt.core.util.ToLiMaUtils;
import org.hardtokenmgmt.core.util.UserDataGenerator;

/**
 * 
 * Actions without cards main controller displaying the menu and
 * the user serial Id textfield input.
 * 
 * It then redirects to the WSActionController for both actions
 * 
 * <p>Controller Memory Settings:
 * <ul>
 * <li>WCACTION     : Describing the type of action performed
 * <li>SERIALNUMBER : The serial number entered in the testfield.
 * </ul>
 * 
 * @author Philip Vendil 
 *
 * @version $Id$
 */
public class EnterSerialNumberController extends BaseController {

    private UserDataGenerator userDataGenerator;

    public EnterSerialNumberController() {
        super(new EnterSerialNumberView());
        getEnterSerialNumberView().getNextButton().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                String sn = getEnterSerialNumberView().getSerialNumberTextField().getText();
                sn = userDataGenerator.getNormalizedSerialNumber(sn);
                if (sn != null && userDataGenerator.isLegalSerialNumber(sn) == null) {
                    getControllerMemory().putData("SERIALNUMBER", sn);
                    switchControlTo(WCActionController.class.getName());
                } else {
                    getEnterSerialNumberView().statuslabel.setText(UIHelper.getText(userDataGenerator.isLegalSerialNumber(sn)));
                }
            }
        });
        getEnterSerialNumberView().getBackButton().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                getEnterSerialNumberView().getSerialNumberTextField().setText("");
                switchControlTo(WithoutCardController.class.getName());
            }
        });
    }

    private EnterSerialNumberView getEnterSerialNumberView() {
        return (EnterSerialNumberView) getView();
    }

    /**
	 * Method called by the Main applet when it's time for this
	 * controller to take control
	 * 
	 * @see org.hardtokenmgmt.core.ui.IController#getControl(String)
	 */
    public void getControl(String callingController) {
        userDataGenerator = ToLiMaUtils.getUserDataGenerator(InterfaceFactory.getGlobalSettings());
        ControllerCache.removeControllerInstance(WCActionController.class.getName());
        if (callingController != null && callingController.equals(WCActionController.class.getName())) {
            String text = (String) getControllerMemory().getData("USERDATANOTEXISTMSG");
            getEnterSerialNumberView().statuslabel.setText(text);
            getEnterSerialNumberView().statuslabel.setForeground(Color.red);
        } else {
            getControllerMemory().putData("USERDATANOTEXISTMSG", "");
            getEnterSerialNumberView().statuslabel.setText("");
            getEnterSerialNumberView().getSerialNumberTextField().setText("");
        }
        getEnterSerialNumberView().getSerialNumberTextField().grabFocus();
    }

    /**
     * Method called by the main applet to check that
     * the administrator is authorized to this controller
     * 
     * @see org.hardtokenmgmt.core.ui.IController#isAuthorizedToController(X509Certificate)
     */
    public boolean isAuthorizedToController(X509Certificate admin) {
        return isAdmin();
    }
}
