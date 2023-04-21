package org.hardtokenmgmt.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.hardtokenmgmt.core.ui.BaseView;
import org.hardtokenmgmt.core.ui.UIHelper;
import javax.swing.SwingConstants;

/**
 * View of the Other Actions page
 * 
 * 
 * @author Philip Vendil 2007 feb 16
 *
 * @version $Id$
 */
public class OtherActionsView extends BaseView {

    private static final long serialVersionUID = 1L;

    private JLabel logoLabel = null;

    private JLabel titleLabel = null;

    private JButton backButton = null;

    private JLabel cardStatusLabel = null;

    private JButton manageCertsButton = null;

    private JButton unlockAndSetPIN = null;

    private JButton issueNewCardButton = null;

    private JButton revokeAndEreaseButton = null;

    private JButton reActivateCard = null;

    private JButton EditSettings = null;

    private JButton renewButton;

    /**
	 * Default constuct
	 *
	 */
    public OtherActionsView() {
        super();
        initialize();
    }

    @Override
    protected void initialize() {
        cardStatusLabel = new JLabel();
        cardStatusLabel.setBounds(new Rectangle(17, 194, 763, 17));
        cardStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardStatusLabel.setText(UIHelper.getText("otheractions.cardstatus") + " " + UIHelper.getText("otheractions.unknownstatus"));
        this.setSize(new Dimension(UIHelper.getAppWidth(), UIHelper.getAppHeight()));
        this.setLayout(null);
        logoLabel = new JLabel();
        logoLabel.setBounds(ToLiMaGUI.getLogoPos());
        logoLabel.setIcon(UIHelper.getLogo());
        titleLabel = new JLabel();
        titleLabel.setBounds(new Rectangle(16, 140, 764, 43));
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setText(UIHelper.getText("otheractions.title"));
        this.add(logoLabel, null);
        this.add(titleLabel, null);
        this.add(getBackButton(), null);
        this.add(cardStatusLabel, null);
        this.add(getManageCertsButton(), null);
        this.add(getUnlockAndSetPIN(), null);
        this.add(getIssueNewCardButton(), null);
        this.add(getRevokeAndEreaseButton(), null);
        this.add(getReActivateCard(), null);
        this.add(getEditSettings(), null);
        this.add(getRenewButton(), null);
    }

    /**
	 * This method initializes backButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getBackButton() {
        if (backButton == null) {
            backButton = new JButton(UIHelper.getText("common.back"));
            backButton.setBounds(ToLiMaGUI.getBackButtonPos());
            backButton.setIcon(UIHelper.getImage("back.gif"));
        }
        return backButton;
    }

    /**
	 * This method initializes manageCertsButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getManageCertsButton() {
        if (manageCertsButton == null) {
            manageCertsButton = new JButton();
            manageCertsButton.setBounds(new Rectangle(91, 242, 283, 45));
            manageCertsButton.setText(UIHelper.getText("otheractions.managecerts"));
            manageCertsButton.setHorizontalAlignment(SwingConstants.LEFT);
            manageCertsButton.setIcon(UIHelper.getImage("cert_view.gif"));
        }
        return manageCertsButton;
    }

    /**
	 * This method initializes unlockAndSetPIN	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getUnlockAndSetPIN() {
        if (unlockAndSetPIN == null) {
            unlockAndSetPIN = new JButton();
            unlockAndSetPIN.setBounds(new Rectangle(387, 242, 277, 45));
            unlockAndSetPIN.setText(UIHelper.getText("otheractions.unlockthecard"));
            unlockAndSetPIN.setHorizontalAlignment(SwingConstants.LEFT);
            unlockAndSetPIN.setIcon(UIHelper.getImage("locked_card.gif"));
        }
        return unlockAndSetPIN;
    }

    /**
	 * This method initializes issueNewCardButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getIssueNewCardButton() {
        if (issueNewCardButton == null) {
            issueNewCardButton = new JButton();
            issueNewCardButton.setBounds(new Rectangle(91, 304, 282, 45));
            issueNewCardButton.setText(UIHelper.getText("otheractions.issuenewcard"));
            issueNewCardButton.setHorizontalAlignment(SwingConstants.LEFT);
            issueNewCardButton.setIcon(UIHelper.getImage("makecard.gif"));
        }
        return issueNewCardButton;
    }

    /**
	 * This method initializes revokeAndEreaseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getRevokeAndEreaseButton() {
        if (revokeAndEreaseButton == null) {
            revokeAndEreaseButton = new JButton();
            revokeAndEreaseButton.setBounds(new Rectangle(387, 304, 279, 45));
            revokeAndEreaseButton.setText(UIHelper.getText("otheractions.revokeanderase"));
            revokeAndEreaseButton.setHorizontalAlignment(SwingConstants.LEFT);
            revokeAndEreaseButton.setIcon(UIHelper.getImage("blockcard.gif"));
        }
        return revokeAndEreaseButton;
    }

    /**
	 * This method initializes reActivateCard	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getReActivateCard() {
        if (reActivateCard == null) {
            reActivateCard = new JButton();
            reActivateCard.setBounds(new Rectangle(91, 365, 282, 45));
            reActivateCard.setHorizontalAlignment(SwingConstants.LEFT);
            reActivateCard.setText(UIHelper.getText("otheractions.reactivatecard"));
            reActivateCard.setIcon(UIHelper.getImage("reactivate.gif"));
        }
        return reActivateCard;
    }

    public JLabel getCardStatusLabel() {
        return cardStatusLabel;
    }

    /**
	 * This method initializes EditSettings	
	 * 	
	 * @return javax.swing.JButton	
	 */
    JButton getEditSettings() {
        if (EditSettings == null) {
            EditSettings = new JButton();
            EditSettings.setBounds(new Rectangle(91, 420, 282, 45));
            EditSettings.setIcon(UIHelper.getImage("saveprops.gif"));
            EditSettings.setText(UIHelper.getText("otheractions.editsettings"));
            EditSettings.setHorizontalAlignment(SwingConstants.LEFT);
        }
        return EditSettings;
    }

    /**
	 * This method initializes renewButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getRenewButton() {
        if (renewButton == null) {
            renewButton = new JButton(UIHelper.getImage("renewcert.gif"));
            renewButton.setBounds(new Rectangle(387, 365, 279, 45));
            renewButton.setHorizontalAlignment(SwingConstants.LEFT);
            renewButton.setText(UIHelper.getText("renewcert.buttontext"));
            renewButton.setToolTipText(UIHelper.getText("investigatecard.renewtooltip"));
        }
        return renewButton;
    }
}
