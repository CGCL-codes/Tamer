package org.hardtokenmgmt.ui.nocard;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ejbca.core.model.ca.crl.RevokedCertInfo;
import org.ejbca.core.protocol.ws.common.CertificateHelper;
import org.ejbca.core.protocol.ws.common.HardTokenConstants;
import org.ejbca.util.query.BasicMatch;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.ui.BaseController;
import org.hardtokenmgmt.core.ui.CertSelector;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.core.util.CertUtils;
import org.hardtokenmgmt.core.util.ControllerMemory;
import org.hardtokenmgmt.core.util.ToLiMaUtils;
import org.hardtokenmgmt.core.util.TokenTools;
import org.hardtokenmgmt.core.util.UserDataGenerator;
import org.hardtokenmgmt.ui.ErrorController;
import org.hardtokenmgmt.ui.activatecard.ActivateCardCertSelector;
import org.hardtokenmgmt.ui.nocard.WithoutCardController.ActionType;
import org.hardtokenmgmt.ui.subview.CardInfoScrollPanel;
import org.hardtokenmgmt.ui.subview.CardInfoView;
import org.hardtokenmgmt.ui.subview.CardViewVO;
import org.hardtokenmgmt.ws.gen.AlreadyRevokedException_Exception;
import org.hardtokenmgmt.ws.gen.AuthorizationDeniedException_Exception;
import org.hardtokenmgmt.ws.gen.Certificate;
import org.hardtokenmgmt.ws.gen.EjbcaException_Exception;
import org.hardtokenmgmt.ws.gen.HardTokenDataWS;
import org.hardtokenmgmt.ws.gen.HardTokenDoesntExistsException_Exception;
import org.hardtokenmgmt.ws.gen.PinDataWS;
import org.hardtokenmgmt.ws.gen.RevokeStatus;
import org.hardtokenmgmt.ws.gen.UserDataVOWS;
import org.hardtokenmgmt.ws.gen.UserMatch;

/**
 * 
 * The action controller performing the actual blocking or activation depending
 * on the memory setting in WCACTION.
 * 
 * <p>Controller Memory Settings:
 * <ul>
 * <li>USERDATANOTEXISTMSG    : Error message sent back to and displayed on the menu page.
 * <li>WC_REVOKEDTOKEN        : Sets the tokendata of the revoked card for the WSRevokeResponseController
 * <li>WC_ACTIVATEDTOKEN      : Sets the tokendata of the reactivated card for the WSActivateResponseController
 * <li>WC_UNBLOCKPUK          : The basic PIN PUK used for remote unblock actions
 * <li>WC_TOKENIMPLEMENTATION : The token implementation used to calculate the unblock response.
 * </ul>
 *       
 * 
 * @author Philip Vendil
 *
 * @version $Id$
 */
public class WCActionController extends BaseController {

    private CertSelector certSelector = null;

    private CardViewVO cardViewVOs[] = null;

    private String userserialnumber = null;

    private UserDataGenerator userDataGenerator;

    public WCActionController() {
        super(new WCActionView());
        certSelector = getGlobalSettings().getCertSelector();
        getWCActionView().getActionButton().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                ActionType action = (ActionType) getControllerMemory().getData("WCACTION");
                switch(action) {
                    case REVOKE_CARD:
                        revokeCard();
                        break;
                    case ACTIVATE_CARD:
                        activateCard();
                        break;
                    case UNBLOCK_CARD:
                        unblockCard();
                        break;
                    case VIEW_CARD:
                        viewCard();
                        break;
                }
            }
        });
        getWCActionView().getBackButton().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                switchControlTo(EnterSerialNumberController.class.getName());
            }
        });
    }

    private WCActionView getWCActionView() {
        return (WCActionView) getView();
    }

    /**
	 * Method called by the Main applet when it's time for this
	 * controller to take control
	 * 
	 * @see org.hardtokenmgmt.core.ui.IController#getControl(String)
	 */
    public void getControl(String callingController) {
        userDataGenerator = ToLiMaUtils.getUserDataGenerator(InterfaceFactory.getGlobalSettings());
        cardViewVOs = null;
        getControllerMemory().putData("USERDATANOTEXISTMSG", "");
        ActionType action = (ActionType) getControllerMemory().getData("WCACTION");
        try {
            if (action.equals(ActionType.REVOKE_CARD)) {
                setInitialRevoke();
                if (!fetchUserData(action)) {
                    String serial = (String) getControllerMemory().getData("SERIALNUMBER");
                    String text = UIHelper.getText("withoutcard.noactivecardexists");
                    text = text.replaceAll("YYYYMMDDNNNN", serial);
                    getControllerMemory().putData("USERDATANOTEXISTMSG", text);
                    switchControlTo(callingController);
                }
            } else if (action.equals(ActionType.ACTIVATE_CARD)) {
                setInitialActivate();
                if (!fetchUserData(action)) {
                    String serial = (String) getControllerMemory().getData("SERIALNUMBER");
                    String text = UIHelper.getText("withoutcard.ordinarycardexists");
                    text = text.replaceAll("YYYYMMDDNNNN", serial);
                    getControllerMemory().putData("USERDATANOTEXISTMSG", text);
                    switchControlTo(callingController);
                }
            } else if (action.equals(ActionType.UNBLOCK_CARD)) {
                setInitialUnblock();
                if (!fetchUserData(action)) {
                    String serial = (String) getControllerMemory().getData("SERIALNUMBER");
                    String text = UIHelper.getText("withoutcard.noactivecardexists");
                    text = text.replaceAll("YYYYMMDDNNNN", serial);
                    getControllerMemory().putData("USERDATANOTEXISTMSG", text);
                    switchControlTo(callingController);
                }
            } else if (action.equals(ActionType.VIEW_CARD)) {
                setInitialViewCard();
                if (!fetchUserData(action)) {
                    String serial = (String) getControllerMemory().getData("SERIALNUMBER");
                    String text = UIHelper.getText("withoutcard.noactivecardexists");
                    text = text.replaceAll("YYYYMMDDNNNN", serial);
                    getControllerMemory().putData("USERDATANOTEXISTMSG", text);
                    switchControlTo(callingController);
                } else {
                    for (int i = 0; i < getWCActionView().cardInfoScrollPanel.cardInfoViewers.length; i++) {
                        getWCActionView().cardInfoScrollPanel.cardInfoViewers[i].selectTokenRadioButton.setVisible(false);
                    }
                }
            }
        } catch (Exception e) {
            error("Error when remote managing the card for user : " + userserialnumber + ", error is ", e);
            ControllerMemory.getInstance().putData(ErrorController.CCERROREXCEPTION, e);
            ControllerMemory.getInstance().putData(ErrorController.CCBACKCONTROLLER, WithoutCardController.class.getName());
            switchControlTo(ErrorController.class.getName());
        }
    }

    private void viewCard() {
    }

    private void revokeCard() {
        try {
            boolean removeUser = getWCActionView().getDeleteUserCheckbox().isSelected();
            if (removeUser) {
                String[] allPossibleUsernames = userDataGenerator.genererateAllPossibleUsernames(userserialnumber);
                for (String username : allPossibleUsernames) {
                    UserMatch userMatch = new UserMatch();
                    userMatch.setMatchtype(BasicMatch.MATCH_TYPE_EQUALS);
                    userMatch.setMatchwith(org.ejbca.util.query.UserMatch.MATCH_WITH_USERNAME);
                    userMatch.setMatchvalue(username);
                    List<UserDataVOWS> userDatas = getHTMFAdminInterface().findUser(userMatch);
                    if (userDatas.size() != 0) {
                        try {
                            getHTMFAdminInterface().revokeUser(username, RevokedCertInfo.REVOKATION_REASON_PRIVILEGESWITHDRAWN, false);
                        } catch (AlreadyRevokedException_Exception e) {
                        }
                    }
                    ControllerMemory.getInstance().putData("WC_REVOKEDTOKEN", cardViewVOs);
                }
                getHTMFAdminInterface().deleteUserDataFromSource(getGlobalSettings().getUserDataSourceNames(), userserialnumber, false);
            } else {
                String cardSerialnumber = getWCActionView().cardInfoScrollPanel.getSelectedHardTokenSN();
                try {
                    getHTMFAdminInterface().revokeToken(cardSerialnumber, RevokedCertInfo.REVOKATION_REASON_UNSPECIFIED);
                } catch (AlreadyRevokedException_Exception e) {
                }
                for (int i = 0; i < cardViewVOs.length; i++) {
                    if (cardViewVOs[i].getSerialNumber().equals(getWCActionView().cardInfoScrollPanel.getSelectedHardTokenSN())) {
                        CardViewVO[] cVVOs = new CardViewVO[1];
                        cVVOs[0] = cardViewVOs[i];
                        ControllerMemory.getInstance().putData("WC_REVOKEDTOKEN", cVVOs);
                    }
                }
            }
            switchControlTo(WCRevokeResponseController.class.getName());
        } catch (Exception e) {
            error("Error when revoking without the card for user : " + userserialnumber + ", error is ", e);
            ControllerMemory.getInstance().putData(ErrorController.CCERROREXCEPTION, e);
            switchControlTo(ErrorController.class.getName());
        }
    }

    private void activateCard() {
        X509Certificate activateCert = null;
        HardTokenDataWS currentHardTokenDataWS = null;
        String currentUsername = null;
        for (int i = 0; i < cardViewVOs.length; i++) {
            if (cardViewVOs[i].getSerialNumber().equals(getWCActionView().cardInfoScrollPanel.getSelectedHardTokenSN())) {
                ControllerMemory.getInstance().putData("WC_ACTIVATEDTOKEN", cardViewVOs[i]);
                currentHardTokenDataWS = (HardTokenDataWS) cardViewVOs[i].getTokenData();
                currentUsername = cardViewVOs[i].getUsername();
                activateCert = ((ActivateCardCertSelector) getGlobalSettings().getCustomInterface("activatecard.certselector")).getActivationCertificate(currentHardTokenDataWS);
            }
        }
        if (activateCert != null) {
            try {
                List<HardTokenDataWS> activeTokens = getHTMFAdminInterface().getHardTokenDatas(currentUsername, true);
                Iterator<HardTokenDataWS> iter = activeTokens.iterator();
                while (iter.hasNext()) {
                    HardTokenDataWS activeToken = iter.next();
                    if (!currentHardTokenDataWS.getHardTokenSN().equals(activeToken.getHardTokenSN())) {
                        try {
                            getHTMFAdminInterface().revokeToken(activeToken.getHardTokenSN(), RevokedCertInfo.REVOKATION_REASON_UNSPECIFIED);
                        } catch (AlreadyRevokedException_Exception e) {
                        }
                    }
                }
                RevokeStatus revokationStatus = getHTMFAdminInterface().checkRevokationStatus(CertUtils.getIssuerDN(activateCert), activateCert.getSerialNumber().toString(16));
                if (revokationStatus.getReason() == RevokedCertInfo.REVOKATION_REASON_CERTIFICATEHOLD || revokationStatus.getReason() == RevokedCertInfo.NOT_REVOKED) {
                    List<Certificate> tokenCerts = currentHardTokenDataWS.getCertificates();
                    for (Certificate tokenCert : tokenCerts) {
                        X509Certificate x509Cert = (X509Certificate) CertificateHelper.getCertificate(tokenCert.getCertificateData());
                        RevokeStatus certStatus = getHTMFAdminInterface().checkRevokationStatus(CertUtils.getIssuerDN(x509Cert), x509Cert.getSerialNumber().toString(16));
                        if (certStatus.getReason() == RevokedCertInfo.REVOKATION_REASON_CERTIFICATEHOLD) {
                            getHTMFAdminInterface().revokeCert(CertUtils.getIssuerDN(x509Cert), x509Cert.getSerialNumber().toString(16), RevokedCertInfo.NOT_REVOKED);
                        }
                    }
                    if (getGlobalSettings().getPropertyAsBoolean("activatecardcontroller.activateinauthsys", false)) {
                        getHTMFAdminInterface().republishCertificate(activateCert.getSerialNumber().toString(16), CertUtils.getIssuerDN(activateCert));
                    }
                    switchControlTo(WCActivateResponseController.class.getName());
                } else {
                    error(UIHelper.getText("activatecard.cardrevoked"));
                    ControllerMemory.getInstance().putData(ErrorController.CCERRORMSG, UIHelper.getText("activatecard.cardrevoked"));
                    switchControlTo(ErrorController.class.getName());
                }
            } catch (Exception e) {
                error("Error when activating without the card for user : " + userserialnumber + ", error is ", e);
                ControllerMemory.getInstance().putData(ErrorController.CCERROREXCEPTION, e);
                switchControlTo(ErrorController.class.getName());
            }
        }
    }

    private void unblockCard() {
        HardTokenDataWS currentHardTokenDataWS = null;
        for (int i = 0; i < cardViewVOs.length; i++) {
            if (cardViewVOs[i].getSerialNumber().equals(getWCActionView().cardInfoScrollPanel.getSelectedHardTokenSN())) {
                ControllerMemory.getInstance().putData("WC_ACTIVATEDTOKEN", cardViewVOs[i]);
                currentHardTokenDataWS = (HardTokenDataWS) cardViewVOs[i].getTokenData();
                try {
                    HardTokenDataWS hardTokenPukDataWS = getHTMFAdminInterface().getHardTokenData(currentHardTokenDataWS.getHardTokenSN(), true, true, null, null, true);
                    String basicPUK = null;
                    for (PinDataWS pINData : hardTokenPukDataWS.getPinDatas()) {
                        if (pINData.getType() == HardTokenConstants.PINTYPE_BASIC) {
                            basicPUK = pINData.getPUK();
                        }
                    }
                    if (basicPUK != null) {
                        getControllerMemory().putData("WC_UNBLOCKPUK", basicPUK);
                        getControllerMemory().putData("WC_TOKENIMPLEMENTATION", hardTokenPukDataWS.getTokenImplementation());
                        switchControlTo(EnterChallengeController.class.getName());
                    } else {
                        throw new HardTokenDoesntExistsException_Exception("Basic PUK not found for card with SN :" + currentHardTokenDataWS.getHardTokenSN(), null);
                    }
                } catch (Exception e) {
                    error("Error when generating unblock response without the card for user : " + userserialnumber + ", error is ", e);
                    ControllerMemory.getInstance().putData(ErrorController.CCERROREXCEPTION, e);
                    switchControlTo(ErrorController.class.getName());
                }
                break;
            }
        }
    }

    private void setInitialRevoke() {
        getWCActionView().setCardIconImage("blockcard.gif");
        getWCActionView().getActionButton().setText(UIHelper.getText("withoutcard.blockcard"));
        getWCActionView().titleLabel.setText(UIHelper.getText("withoutcard.revokecardremotely"));
        getWCActionView().getDeleteUserCheckbox().setVisible(true);
        getWCActionView().userQuitInfoLabel.setVisible(true);
    }

    private void setInitialActivate() {
        getWCActionView().setCardIconImage("reactivate.gif");
        getWCActionView().getActionButton().setText(UIHelper.getText("otheractions.reactivatecard"));
        getWCActionView().getActionButton().setIcon(UIHelper.getImage("reactivate.gif"));
        getWCActionView().titleLabel.setText(UIHelper.getText("withoutcard.reactivateordinary"));
        getWCActionView().getDeleteUserCheckbox().setVisible(false);
        getWCActionView().userQuitInfoLabel.setVisible(false);
    }

    private void setInitialViewCard() {
        getWCActionView().userQuitInfoLabel.setVisible(false);
        getWCActionView().getActionButton().setVisible(false);
        getWCActionView().getDeleteUserCheckbox().setVisible(false);
        getWCActionView().titleLabel.setText(UIHelper.getText("withoutcard.viewcardtitle"));
        getWCActionView().setCardIconImage("viewmag.png");
    }

    private void setInitialUnblock() {
        getWCActionView().setCardIconImage("locked_card.gif");
        getWCActionView().getActionButton().setText(UIHelper.getText("withoutcard.unblockcard"));
        getWCActionView().getActionButton().setIcon(UIHelper.getImage("locked_card.gif"));
        getWCActionView().titleLabel.setText(UIHelper.getText("withoutcard.genresponse"));
        getWCActionView().getDeleteUserCheckbox().setVisible(false);
        getWCActionView().userQuitInfoLabel.setVisible(false);
    }

    /**
	 * Method fetching HardTokenDatas for the revoke or activate actions
	 * @param action The type of action to fetch hardtoken datas for.
	 * @return true if the user have hard tokens to manage for the specified operation
	 */
    private boolean fetchUserData(ActionType action) throws AuthorizationDeniedException_Exception, EjbcaException_Exception, CertificateException {
        boolean retval = false;
        userserialnumber = (String) getControllerMemory().getData("SERIALNUMBER");
        List<CardViewVO> hardTokens = new ArrayList<CardViewVO>();
        String[] usernames = userDataGenerator.genererateAllPossibleUsernames(userserialnumber);
        for (String username : usernames) {
            if (action == ActionType.REVOKE_CARD) {
                List<HardTokenDataWS> allHardTokens = getHTMFAdminInterface().getHardTokenDatas(username, true);
                for (HardTokenDataWS dataWS : allHardTokens) {
                    if (dataWS.getCertificates().size() > 0) {
                        hardTokens.add(TokenTools.toCardViewVO(dataWS, username));
                    }
                }
            } else if (action == ActionType.ACTIVATE_CARD) {
                List<HardTokenDataWS> allHardTokens = getHTMFAdminInterface().getHardTokenDatas(username, false);
                for (HardTokenDataWS dataWS : allHardTokens) {
                    for (org.hardtokenmgmt.ws.gen.Certificate c : dataWS.getCertificates()) {
                        X509Certificate cert = (X509Certificate) CertificateHelper.getCertificate(c.getCertificateData());
                        RevokeStatus revokeStatus = getHTMFAdminInterface().checkRevokationStatus(CertUtils.getIssuerDN(cert), cert.getSerialNumber().toString(16));
                        if (revokeStatus.getReason() == RevokedCertInfo.REVOKATION_REASON_CERTIFICATEHOLD) {
                            hardTokens.add(TokenTools.toCardViewVO(dataWS, username));
                            break;
                        }
                    }
                }
            } else if (action == ActionType.UNBLOCK_CARD) {
                List<HardTokenDataWS> allHardTokens = getHTMFAdminInterface().getHardTokenDatas(username, true);
                for (HardTokenDataWS dataWS : allHardTokens) {
                    if (dataWS.isSupportsRemoteUnblock()) {
                        if (dataWS.getCertificates().size() > 0) {
                            hardTokens.add(TokenTools.toCardViewVO(dataWS, username));
                        }
                    }
                }
            } else if (action == ActionType.VIEW_CARD) {
                List<HardTokenDataWS> allHardTokens = getHTMFAdminInterface().getHardTokenDatas(username, false);
                for (HardTokenDataWS dataWS : allHardTokens) {
                    if (dataWS.getCertificates().size() > 0) {
                        hardTokens.add(TokenTools.toCardViewVO(dataWS, username));
                    }
                }
            }
        }
        if (hardTokens.size() != 0) {
            cardViewVOs = hardTokens.toArray(new CardViewVO[hardTokens.size()]);
        }
        if (cardViewVOs != null) {
            getWCActionView().cardInfoScrollPanel = new CardInfoScrollPanel(cardViewVOs, true, false, certSelector);
            getWCActionView().addScrollPane();
            retval = true;
        }
        return retval;
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
