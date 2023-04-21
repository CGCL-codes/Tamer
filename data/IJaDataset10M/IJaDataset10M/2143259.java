package base.jdbs.ui.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import ui.dialog.UserDialog;
import base.jdbs.ConfigurationManager;
import base.jdbs.network.NetworkManager;
import base.util.FileUtil;

/**
 * @author skunk
 * 
 */
@SuppressWarnings("serial")
public class UserPanel extends JPanel {

    private static final transient Logger logger = Logger.getLogger(UserPanel.class.getName());

    private ImageIcon[] flagImageIcon;

    private String[] flagName;

    private JPanel infoPanel;

    private JComboBox locationComboBox;

    private JLabel flagLocationLabel;

    private JPanel peerNamePanel;

    private JTextField peerNameTextField;

    private JButton userInfoButton;

    public UserPanel() {
        initialize();
    }

    protected void initialize() {
        this.setLayout(new GridLayout(2, 1));
        this.setBorder(new TitledBorder("User"));
        this.add(this.getPeerNamePanel());
        this.add(this.getInfoPanel());
    }

    /**
	 * @return Returns the locationComboBox.
	 */
    protected JComboBox getLocationComboBox() {
        if (this.locationComboBox == null) {
            this.locationComboBox = new JComboBox();
            indexFlags();
            for (int i = 0; i < flagName.length; i++) {
                this.locationComboBox.addItem(flagName[i]);
                if (flagName[i].equalsIgnoreCase(ConfigurationManager.getInstance().getUserLocation())) {
                    this.locationComboBox.setSelectedIndex(i);
                    this.getFlagLocationLabel().setIcon(flagImageIcon[i]);
                }
            }
            this.locationComboBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    getFlagLocationLabel().setIcon(flagImageIcon[locationComboBox.getSelectedIndex()]);
                    ConfigurationManager.getInstance().setUserLocation(flagName[locationComboBox.getSelectedIndex()]);
                }
            });
        }
        return locationComboBox;
    }

    protected void indexFlags() {
        File[] icon = FileUtil.allFileContent(new File("lib/gif-1.0/gif16"));
        flagImageIcon = new ImageIcon[icon.length];
        flagName = new String[icon.length];
        for (int i = 0; i < icon.length; i++) {
            logger.debug(icon[i].getAbsolutePath());
            flagImageIcon[i] = new ImageIcon(icon[i].getAbsolutePath());
            flagName[i] = icon[i].getName().replaceAll(".gif", "").toUpperCase();
        }
    }

    /**
	 * @return Returns the infoPanel.
	 */
    protected JPanel getInfoPanel() {
        if (this.infoPanel == null) {
            this.infoPanel = new JPanel();
            this.infoPanel.setLayout(new BorderLayout());
            this.infoPanel.setBorder(new TitledBorder("General User Info"));
            JPanel locationPanel = new JPanel();
            locationPanel.setBorder(new TitledBorder("Location"));
            locationPanel.setLayout(new GridLayout(1, 2));
            locationPanel.add(this.getFlagLocationLabel());
            locationPanel.add(this.getLocationComboBox());
            this.infoPanel.add(locationPanel, BorderLayout.CENTER);
            this.infoPanel.add(this.getUserInfoButton(), BorderLayout.SOUTH);
        }
        return infoPanel;
    }

    /**
	 * @return Returns the flagLocationLabel.
	 */
    protected JLabel getFlagLocationLabel() {
        if (this.flagLocationLabel == null) {
            this.flagLocationLabel = new JLabel("Selected Location");
            this.flagLocationLabel.setVerticalTextPosition(JLabel.NORTH);
        }
        return flagLocationLabel;
    }

    /**
	 * @return Returns the peerNamePanel.
	 */
    protected JPanel getPeerNamePanel() {
        if (this.peerNamePanel == null) {
            this.peerNamePanel = new JPanel();
            this.peerNamePanel.setBorder(new TitledBorder("Peer Name"));
            this.peerNamePanel.setLayout(new BorderLayout());
            this.peerNamePanel.add(this.getPeerNameTextField(), BorderLayout.CENTER);
        }
        return peerNamePanel;
    }

    /**
	 * @return Returns the peerNameTextField.
	 */
    protected JTextField getPeerNameTextField() {
        if (this.peerNameTextField == null) {
            this.peerNameTextField = new JTextField();
            this.peerNameTextField.setEditable(false);
            this.peerNameTextField.setText(NetworkManager.getInstance().getJdbsPeerGroup().getPeerName());
        }
        return peerNameTextField;
    }

    /**
	 * @return Returns the userInfoButton.
	 */
    protected JButton getUserInfoButton() {
        if (this.userInfoButton == null) {
            this.userInfoButton = new JButton("User Data");
            this.userInfoButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    UserDialog userDialog = new UserDialog(ConfigurationManager.getInstance().getUser());
                    userDialog.setModal(true);
                    userDialog.setVisible(true);
                }
            });
        }
        return userInfoButton;
    }
}
