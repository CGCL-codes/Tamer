package net.sourceforge.obexftpfrontend.gui;

import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import net.sourceforge.obexftpfrontend.model.OBEXElement;

/**
 * Dialog used to show information about the selected file in the
 * file system tree component.
 * @author Daniel F. Martins
 */
public class FilePropertiesDialog extends javax.swing.JDialog {

    /** UI helper. */
    private FilePropertiesDialogHelper helper;

    /**
     * Creates a new instance FilePropertiesDialog.
     * @param parent Parent frame.
     */
    public FilePropertiesDialog(Window parent) {
        super(parent, ModalityType.MODELESS);
        helper = new FilePropertiesDialogHelper(this);
        initComponents();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Get the accessedTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The accessedTextField object.
     */
    protected JTextField getAccessedTextField() {
        return accessedTextField;
    }

    /**
     * Get the createdTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The createdTextField object.
     */
    protected JTextField getCreatedTextField() {
        return createdTextField;
    }

    /**
     * Get the descriptionLabel object. This method should be
     * used only by the ViewHelper object.
     * @return The descriptionLabel object.
     */
    protected JLabel getDescriptionLabel() {
        return descriptionLabel;
    }

    /**
     * Get the groupTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The groupTextField object.
     */
    protected JTextField getGroupTextField() {
        return groupTextField;
    }

    /**
     * Get the groupPermTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The groupPermTextField object.
     */
    protected JTextField getGroupPermTextField() {
        return groupPermTextField;
    }

    /**
     * Get the locationTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The locationTextField object.
     */
    protected JTextField getLocationTextField() {
        return locationTextField;
    }

    /**
     * Get the modifiedTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The modifiedTextField object.
     */
    protected JTextField getModifiedTextField() {
        return modifiedTextField;
    }

    /**
     * Get the okButton object. This method should be
     * used only by the ViewHelper object.
     * @return The okButton object.
     */
    protected JButton getOkButton() {
        return okButton;
    }

    /**
     * Get the otherPermTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The otherPermTextField object.
     */
    protected JTextField getOtherPermTextField() {
        return otherPermTextField;
    }

    /**
     * Get the ownerTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The ownerTextField object.
     */
    protected JTextField getOwnerTextField() {
        return ownerTextField;
    }

    /**
     * Get the ownerPermTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The ownerPermTextField object.
     */
    protected JTextField getOwnerPermTextField() {
        return ownerPermTextField;
    }

    /**
     * Get the sizeTextField object. This method should be
     * used only by the ViewHelper object.
     * @return The sizeTextField object.
     */
    protected JTextField getSizeTextField() {
        return sizeTextField;
    }

    /**
     * Get the tabbedPane object. This method should be
     * used only by the ViewHelper object.
     * @return The tabbedPane object.
     */
    protected JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    @Override
    public void setVisible(boolean b) {
        helper.prepareWindow();
        super.setVisible(b);
    }

    /**
     * Method to set the OBEXElement which you want to display
     * in this frame.
     * @param file OBEXElement to be displayed.
     */
    public void setFile(OBEXElement file) {
        helper.setFile(file);
    }

    private void initComponents() {
        descriptionLabel = new javax.swing.JLabel();
        descriptionSeparator = new javax.swing.JSeparator();
        okButton = new javax.swing.JButton();
        tabbedPane = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        sizeTextField = new javax.swing.JTextField();
        locationTextField = new javax.swing.JTextField();
        createdTextField = new javax.swing.JTextField();
        modifiedTextField = new javax.swing.JTextField();
        accessedTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ownerTextField = new javax.swing.JTextField();
        groupTextField = new javax.swing.JTextField();
        groupPermTextField = new javax.swing.JTextField();
        ownerPermTextField = new javax.swing.JTextField();
        otherPermTextField = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("File properties");
        descriptionLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/top-description.png")));
        descriptionLabel.setText("Properties of filename...");
        descriptionLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/ok.png")));
        okButton.setMnemonic('o');
        okButton.setText("OK");
        okButton.setToolTipText("Close this dialog");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        tabbedPane.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel19.setDisplayedMnemonic('p');
        jLabel19.setLabelFor(locationTextField);
        jLabel19.setText("Full path:");
        jLabel2.setDisplayedMnemonic('s');
        jLabel2.setLabelFor(sizeTextField);
        jLabel2.setText("File size:");
        jLabel4.setDisplayedMnemonic('c');
        jLabel4.setLabelFor(createdTextField);
        jLabel4.setText("Created:");
        jLabel5.setDisplayedMnemonic('m');
        jLabel5.setLabelFor(modifiedTextField);
        jLabel5.setText("Last modified:");
        jLabel6.setDisplayedMnemonic('a');
        jLabel6.setLabelFor(accessedTextField);
        jLabel6.setText("Last accessed:");
        sizeTextField.setEditable(false);
        sizeTextField.setText("-");
        locationTextField.setEditable(false);
        locationTextField.setText("-");
        createdTextField.setEditable(false);
        createdTextField.setText("-");
        modifiedTextField.setEditable(false);
        modifiedTextField.setText("-");
        accessedTextField.setEditable(false);
        accessedTextField.setText("-");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(sizeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE).addComponent(locationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE).addComponent(createdTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE).addComponent(modifiedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE).addComponent(accessedTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(sizeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(locationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(createdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(modifiedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(accessedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(30, Short.MAX_VALUE)));
        tabbedPane.addTab("General", jPanel2);
        jLabel1.setDisplayedMnemonic('w');
        jLabel1.setLabelFor(ownerTextField);
        jLabel1.setText("Owner:");
        jLabel3.setDisplayedMnemonic('g');
        jLabel3.setLabelFor(groupTextField);
        jLabel3.setText("Group:");
        jLabel7.setText("Permission:");
        jLabel8.setText("Permission:");
        jLabel11.setDisplayedMnemonic('p');
        jLabel11.setLabelFor(otherPermTextField);
        jLabel11.setText("Permission:");
        jLabel9.setText("Others");
        ownerTextField.setEditable(false);
        ownerTextField.setText("-");
        groupTextField.setEditable(false);
        groupTextField.setText("-");
        groupPermTextField.setEditable(false);
        groupPermTextField.setText("-");
        ownerPermTextField.setEditable(false);
        ownerPermTextField.setText("-");
        otherPermTextField.setEditable(false);
        otherPermTextField.setText("-");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(groupTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE).addComponent(ownerTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel7).addComponent(jLabel8))).addComponent(jLabel11)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(otherPermTextField).addComponent(groupPermTextField).addComponent(ownerPermTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(ownerPermTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7).addComponent(ownerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(groupPermTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8).addComponent(groupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(otherPermTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11)).addContainerGap(78, Short.MAX_VALUE)));
        tabbedPane.addTab("Permissions", jPanel3);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE).addComponent(descriptionSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE).addComponent(okButton)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(descriptionLabel).addGap(0, 0, 0).addComponent(descriptionSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE).addComponent(okButton).addContainerGap()));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 454) / 2, (screenSize.height - 341) / 2, 454, 341);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private javax.swing.JTextField accessedTextField;

    private javax.swing.JTextField createdTextField;

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JSeparator descriptionSeparator;

    private javax.swing.JTextField groupPermTextField;

    private javax.swing.JTextField groupTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JTextField locationTextField;

    private javax.swing.JTextField modifiedTextField;

    private javax.swing.JButton okButton;

    private javax.swing.JTextField otherPermTextField;

    private javax.swing.JTextField ownerPermTextField;

    private javax.swing.JTextField ownerTextField;

    private javax.swing.JTextField sizeTextField;

    private javax.swing.JTabbedPane tabbedPane;
}
