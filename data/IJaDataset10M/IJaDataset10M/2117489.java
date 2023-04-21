package org.hardtokenmgmt.ui.pinlocked;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import org.hardtokenmgmt.core.ui.BaseView;
import org.hardtokenmgmt.core.ui.CustomFocusTraversalPolicy;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.ui.ToLiMaGUI;

/**
 * View of the PIN locked page
 * 
 * 
 * @author Philip Vendil 2007 feb 16
 *
 * @version $Id$
 */
public class PINLockedView extends BaseView {

    private static final long serialVersionUID = 1L;

    private JLabel logoLabel = null;

    public JLabel statuslabel = null;

    public JLabel cardIconLabel = null;

    private String iconname = "locked_card.gif";

    public JLabel cardBelongsToLabel = null;

    public JLabel validTomLabel = null;

    public JButton unlockAndSetPINButton = null;

    private JButton otherOperationsButton = null;

    public String username = "";

    public String validTom = "";

    public JPasswordField newPIN1 = null;

    public JPasswordField newPIN2 = null;

    public JLabel enterPIN1 = null;

    private JLabel enterPIN2 = null;

    public JLabel pinEnterErrorLabel = null;

    public JLabel pinRulesInfo = null;

    JLabel titleLabel = null;

    /**
	 * Default constuct
	 *
	 */
    public PINLockedView() {
        super();
        initialize();
    }

    @Override
    protected void initialize() {
        titleLabel = new JLabel();
        titleLabel.setBounds(ToLiMaGUI.getTitleLabelPos());
        titleLabel.setText(UIHelper.getText("pinlocked.title"));
        titleLabel.setFont(UIHelper.getTitleFont());
        pinRulesInfo = new JLabel();
        pinRulesInfo.setBounds(new Rectangle(288, 317, 214, 23));
        pinRulesInfo.setText("");
        pinEnterErrorLabel = new JLabel();
        pinEnterErrorLabel.setBounds(new Rectangle(288, 296, 214, 22));
        pinEnterErrorLabel.setText("");
        enterPIN2 = new JLabel();
        enterPIN2.setBounds(new Rectangle(288, 390, 170, 21));
        enterPIN2.setText(UIHelper.getText("cardinfo.enterpin2"));
        enterPIN1 = new JLabel();
        enterPIN1.setBounds(new Rectangle(288, 341, 170, 21));
        enterPIN1.setText(UIHelper.getText("cardinfo.enterbasicpin1"));
        validTomLabel = new JLabel();
        validTomLabel.setBounds(new Rectangle(288, 259, 283, 21));
        String validTomText = UIHelper.getText("cardinfo.validtom");
        validTomLabel.setText(validTomText);
        cardBelongsToLabel = new JLabel();
        cardBelongsToLabel.setBounds(new Rectangle(288, 236, 477, 21));
        String cardBelongsToText = UIHelper.getText("cardinfo.belongsto");
        cardBelongsToLabel.setText(cardBelongsToText);
        cardIconLabel = new JLabel();
        cardIconLabel.setBounds(ToLiMaGUI.getIconLabelPos());
        cardIconLabel.setIcon(UIHelper.getImage(iconname));
        statuslabel = new JLabel();
        statuslabel.setBounds(ToLiMaGUI.getStatusLabelPos());
        String statusText = UIHelper.getText("status.pin_locked");
        statuslabel.setText(statusText);
        logoLabel = new JLabel();
        logoLabel.setBounds(ToLiMaGUI.getLogoPos());
        logoLabel.setIcon(UIHelper.getLogo());
        this.setSize(new Dimension(UIHelper.getAppWidth(), UIHelper.getAppHeight()));
        this.setLayout(null);
        this.add(logoLabel, null);
        this.add(statuslabel, null);
        this.add(cardIconLabel, null);
        this.add(cardBelongsToLabel, null);
        this.add(validTomLabel, null);
        this.add(getUnlockAndSetPINButton(), null);
        this.add(getOtherOperationsButton(), null);
        this.add(getNewPIN1(), null);
        this.add(getNewPIN2(), null);
        this.add(enterPIN1, null);
        this.add(enterPIN2, null);
        this.add(pinEnterErrorLabel, null);
        this.add(pinRulesInfo, null);
        this.add(titleLabel, null);
        Vector<Component> v = new Vector<Component>();
        v.add(getNewPIN1());
        v.add(getNewPIN2());
        v.add(getUnlockAndSetPINButton());
        v.add(getOtherOperationsButton());
        CustomFocusTraversalPolicy defaultFocusPolicy = new CustomFocusTraversalPolicy(v);
        this.setFocusTraversalPolicy(defaultFocusPolicy);
    }

    /**
	 * This method initializes unlockAndSetPINButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getUnlockAndSetPINButton() {
        if (unlockAndSetPINButton == null) {
            unlockAndSetPINButton = new JButton(UIHelper.getText("otheractions.unlockthecard"));
            unlockAndSetPINButton.setBounds(ToLiMaGUI.getNextButtonPos());
            unlockAndSetPINButton.setIcon(UIHelper.getImage("smartcard_h.gif"));
            unlockAndSetPINButton.addKeyListener(new java.awt.event.KeyAdapter() {

                private boolean consume = false;

                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (consume) {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                        unlockAndSetPINButton.transferFocusBackward();
                        consume = true;
                    }
                    if (consume) {
                        e.consume();
                    }
                }

                public void keyReleased(KeyEvent e) {
                    if (consume) {
                        e.consume();
                    }
                    consume = false;
                }
            });
        }
        return unlockAndSetPINButton;
    }

    /**
	 * This method initializes otherOperationsButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    JButton getOtherOperationsButton() {
        if (otherOperationsButton == null) {
            otherOperationsButton = new JButton(UIHelper.getText("cardinfo.otheroperations"));
            otherOperationsButton.setBounds(ToLiMaGUI.getOtherActionsButtonPos());
            otherOperationsButton.setIcon(UIHelper.getImage("roadsign.gif"));
            otherOperationsButton.addKeyListener(new java.awt.event.KeyAdapter() {

                private boolean consume = false;

                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (consume) {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                        otherOperationsButton.transferFocusBackward();
                        consume = true;
                    }
                    if (consume) {
                        e.consume();
                    }
                }

                public void keyReleased(KeyEvent e) {
                    if (consume) {
                        e.consume();
                    }
                    consume = false;
                }
            });
        }
        return otherOperationsButton;
    }

    /**
	 * This method initializes newPIN1	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    public JPasswordField getNewPIN1() {
        if (newPIN1 == null) {
            newPIN1 = new JPasswordField();
            newPIN1.setToolTipText(UIHelper.getText("pininfo.policytooltip"));
            newPIN1.setBounds(new Rectangle(288, 365, 147, 21));
            newPIN1.addKeyListener(new java.awt.event.KeyAdapter() {

                private boolean consume = false;

                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        newPIN1.transferFocus();
                    }
                    if (consume) {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ADD) {
                        newPIN1.setText("");
                        consume = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                        newPIN1.transferFocusBackward();
                        consume = true;
                    }
                    if (consume) {
                        e.consume();
                    }
                }

                public void keyReleased(KeyEvent e) {
                    if (consume) {
                        e.consume();
                    }
                    consume = false;
                }
            });
        }
        return newPIN1;
    }

    /**
	 * This method initializes newPIN2	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
    public JPasswordField getNewPIN2() {
        if (newPIN2 == null) {
            newPIN2 = new JPasswordField();
            newPIN2.setBounds(new Rectangle(288, 415, 147, 21));
            newPIN2.addKeyListener(new java.awt.event.KeyAdapter() {

                private boolean consume = false;

                public void keyTyped(java.awt.event.KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        newPIN2.transferFocus();
                    }
                    if (consume) {
                        e.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ADD) {
                        newPIN2.setText("");
                        consume = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_SUBTRACT) {
                        newPIN2.transferFocusBackward();
                        consume = true;
                    }
                    if (consume) {
                        e.consume();
                    }
                }

                public void keyReleased(KeyEvent e) {
                    if (consume) {
                        e.consume();
                    }
                    consume = false;
                }
            });
        }
        return newPIN2;
    }
}
