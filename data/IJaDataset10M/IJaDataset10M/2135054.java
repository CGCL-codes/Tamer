package org.mitre.rt.client.ui.users;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.mitre.rt.client.core.DataManager;
import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.client.xml.UserHelper;
import org.mitre.rt.client.exceptions.RTClientException;
import org.mitre.rt.client.util.GlobalUITools;
import org.mitre.rt.rtclient.RTDocument.RT;
import org.mitre.rt.rtclient.UserType;

/**
 *
 * @author  BAKERJ
 */
public class UserNewJDialog extends javax.swing.JDialog {

    private static final Logger logger = Logger.getLogger(UserNewJDialog.class.getPackage().getName());

    private UserType user = null;

    private UserInfoPanel userInfoPanel = null;

    private final UserHelper userHelper = new UserHelper();

    /** Creates new form UserNewJDialog */
    public UserNewJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.createBlankUser();
        initComponents();
        userInfoPanel = new org.mitre.rt.client.ui.users.UserInfoPanel(this.user);
        datajPanel.add(userInfoPanel, BorderLayout.CENTER);
        ImageIcon icon = new ImageIcon(getClass().getResource(GlobalUITools.ICON_IMAGE_FILE));
        setIconImage(icon.getImage());
        this.setLocation();
    }

    private void createBlankUser() {
        try {
            RT rt = DataManager.instance().getRTDocument().getRT();
            this.user = userHelper.getNewItem(rt);
            this.user.setDeleted(false);
            this.user.setAdmin(false);
        } catch (Exception ex) {
        }
    }

    private void setLocation() {
        this.setLocationRelativeTo(MetaManager.getMainWindow());
    }

    private void closeWindow() {
        this.dispose();
    }

    private List<String> validateContents() {
        List<String> errors = new LinkedList<String>();
        errors.addAll(this.userInfoPanel.validateUser());
        return errors;
    }

    private boolean saveContents() {
        boolean created = false;
        this.userInfoPanel.saveData();
        if (this.userInfoPanel.isChanged()) {
            try {
                userHelper.add(this.user);
                created = true;
                this.firePropertyChange("added_user", null, null);
            } catch (Exception ex) {
                logger.fatal("Unable to create new user.", ex);
                JOptionPane.showMessageDialog(this, "Unable to create new user", "Invalid User", JOptionPane.ERROR_MESSAGE);
            }
        }
        return created;
    }

    private void displayError(List<String> errorList) {
        String msg = errorList.remove(0);
        for (String error : errorList) {
            msg += "\n" + error;
        }
        JOptionPane.showMessageDialog(this, msg, "Validation Errors", JOptionPane.ERROR_MESSAGE);
    }

    private void initComponents() {
        buttonjPanel = new javax.swing.JPanel();
        buttonsEastjPanel = new javax.swing.JPanel();
        addjButton = new javax.swing.JButton();
        canceljButton = new javax.swing.JButton();
        datajPanel = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New User Information");
        setMinimumSize(new java.awt.Dimension(300, 330));
        setResizable(false);
        buttonjPanel.setPreferredSize(new java.awt.Dimension(300, 30));
        buttonjPanel.setLayout(new java.awt.BorderLayout());
        buttonsEastjPanel.setMinimumSize(new java.awt.Dimension(155, 33));
        buttonsEastjPanel.setPreferredSize(new java.awt.Dimension(155, 30));
        addjButton.setText("Add");
        addjButton.setMaximumSize(new java.awt.Dimension(65, 23));
        addjButton.setMinimumSize(new java.awt.Dimension(65, 23));
        addjButton.setPreferredSize(new java.awt.Dimension(65, 23));
        addjButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addjButtonActionPerformed(evt);
            }
        });
        buttonsEastjPanel.add(addjButton);
        canceljButton.setText("Cancel");
        canceljButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canceljButtonActionPerformed(evt);
            }
        });
        buttonsEastjPanel.add(canceljButton);
        buttonjPanel.add(buttonsEastjPanel, java.awt.BorderLayout.EAST);
        getContentPane().add(buttonjPanel, java.awt.BorderLayout.SOUTH);
        datajPanel.setPreferredSize(new java.awt.Dimension(300, 330));
        datajPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(datajPanel, java.awt.BorderLayout.CENTER);
        pack();
    }

    private void addjButtonActionPerformed(java.awt.event.ActionEvent evt) {
        List<String> errors = this.validateContents();
        if (errors.isEmpty()) {
            if (this.saveContents()) {
                DataManager.setModified(true);
                this.closeWindow();
            }
        } else {
            this.displayError(errors);
        }
    }

    private void canceljButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.closeWindow();
    }

    private javax.swing.JButton addjButton;

    private javax.swing.JPanel buttonjPanel;

    private javax.swing.JPanel buttonsEastjPanel;

    private javax.swing.JButton canceljButton;

    private javax.swing.JPanel datajPanel;
}
