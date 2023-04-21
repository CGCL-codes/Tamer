package au.gov.naa.digipres.rollingchecker.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import au.gov.naa.digipres.rollingchecker.CheckerConstants;

/**
 * Simple dialog for settings for the CheckerClient.
 * There are two settings which can be modified: server hostname and server port.
 * @author Justin Waddell
 *
 */
public class CheckerClientSettingsDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField checkerServerField;

    private JTextField checkerPortField;

    private boolean accepted = false;

    private static Preferences prefs;

    static {
        prefs = Preferences.userNodeForPackage(CheckerClientSettingsDialog.class);
    }

    /**
	 * @param owner
	 * @param modal
	 */
    public CheckerClientSettingsDialog(Frame owner) {
        super(owner, "Checksum Checker Client Settings", true);
        initGUI();
        pack();
        loadSettings();
    }

    private void initGUI() {
        JLabel checkerServerLabel = new JLabel("Checker Server:");
        checkerServerField = new JTextField(40);
        JLabel checkerPortLabel = new JLabel("Checker Port:");
        checkerPortField = new JTextField(5);
        JPanel mainPanel = new JPanel(new GridBagLayout());
        addToGridBag(mainPanel, checkerServerLabel, 0, 0, GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 25, 5, 5), 10, 0);
        addToGridBag(mainPanel, checkerServerField, 1, 0, GridBagConstraints.REMAINDER, GridBagConstraints.RELATIVE, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 5, 25), 10, 0);
        addToGridBag(mainPanel, checkerPortLabel, 0, 1, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 0.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(20, 25, 5, 5), 10, 0);
        addToGridBag(mainPanel, checkerPortField, 1, 1, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 5, 25), 10, 0);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (saveSettings()) {
                    accepted = true;
                    CheckerClientSettingsDialog.this.setVisible(false);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                accepted = false;
                CheckerClientSettingsDialog.this.setVisible(false);
            }
        });
        setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
	 * Populate the dialog fields from the stored settings in Preferences
	 */
    private void loadSettings() {
        checkerServerField.setText(prefs.get(CheckerConstants.CHECKER_SERVER_HOST_KEY, ""));
        checkerPortField.setText(Integer.toString(prefs.getInt(CheckerConstants.CHECKER_SERVER_PORT_KEY, -1)));
    }

    /**
	 * Persist the dialog fields by setting them in Preferences
	 * @return
	 */
    private boolean saveSettings() {
        if ("".equals(checkerServerField.getText()) || "".equals(checkerPortField.getText())) {
            JOptionPane.showMessageDialog(this, "Please ensure a value has been entered for all settings.", "Missing Field", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            int portNumber = Integer.parseInt(checkerPortField.getText());
            prefs.putInt(CheckerConstants.CHECKER_SERVER_PORT_KEY, portNumber);
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "Please ensure the checker server port is a valid number.", "Invalid Checker Server Port", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        prefs.put(CheckerConstants.CHECKER_SERVER_HOST_KEY, checkerServerField.getText());
        return true;
    }

    /**
	 * Persist the given settings by setting them in Preferences
	 * @param hostName
	 * @param portNumber
	 * @return
	 */
    public static boolean saveSettings(String hostName, int portNumber) {
        prefs.put(CheckerConstants.CHECKER_SERVER_HOST_KEY, hostName);
        prefs.putInt(CheckerConstants.CHECKER_SERVER_PORT_KEY, portNumber);
        return true;
    }

    /**
	 * Retrieve the server hostname setting from the stored Preferences
	 * @return
	 */
    public static String getServerHostSetting() {
        return prefs.get(CheckerConstants.CHECKER_SERVER_HOST_KEY, "");
    }

    /**
	 * Retrieve the server port setting from the stored Preferences
	 * @return
	 */
    public static int getServerPortSetting() {
        return prefs.getInt(CheckerConstants.CHECKER_SERVER_PORT_KEY, -1);
    }

    /**
	 * Convenience method for adding a component to a container
	 * which is using a GridBagLayout
	 * 
	 * @param container
	 * @param component
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param gridheight
	 * @param weightx
	 * @param weighty
	 * @param anchor
	 * @param fill
	 * @param insets
	 * @param ipadx
	 * @param ipady
	 */
    private void addToGridBag(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady);
        container.add(component, gbc);
    }

    /**
	 * @return the accepted
	 */
    public boolean isAccepted() {
        return accepted;
    }
}
