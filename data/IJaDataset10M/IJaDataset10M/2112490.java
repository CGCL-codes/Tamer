package gui;

import data.FileNameSafeDocument;

/**
 *
 * @author  fedora
 */
public class PublishDialog extends javax.swing.JDialog {

    private boolean okPressed;

    /** Creates new form PublishDialog */
    public PublishDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setTitle("Publishing");
        initComponents();
        okPressed = false;
        pack();
        setVisible(true);
    }

    public PublishDialog(java.awt.Frame parent, boolean modal, String url, String user, String pass, String deployDir, boolean anon, boolean overwrite) {
        super(parent, modal);
        this.setTitle("Publishing");
        initComponents();
        jtfURL.setText(url);
        jtfUsername.setText(user);
        jpfPassword.setText(pass);
        jckAnon.setSelected(anon);
        jtfDeployDir.setText(deployDir);
        if (anon) {
            jtfUsername.setEnabled(false);
            jpfPassword.setEnabled(false);
        }
        jckOverwrite.setSelected(overwrite);
        jckRemember.setSelected(true);
        okPressed = false;
        pack();
        setVisible(true);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jpfPassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtfURL = new javax.swing.JTextField();
        jtfUsername = new javax.swing.JTextField();
        jckAnon = new javax.swing.JCheckBox();
        jckRemember = new javax.swing.JCheckBox();
        jckOverwrite = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jbtPublish = new javax.swing.JButton();
        jbntCancel = new javax.swing.JButton();
        jtfDeployDir = new javax.swing.JTextField();
        jtfDeployDir.setDocument(new FileNameSafeDocument());
        jLabel5 = new javax.swing.JLabel();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(300, 125));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel4.setText("FTP Publishing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 11, 0);
        jPanel1.add(jLabel4, gridBagConstraints);
        jLabel3.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jpfPassword, gridBagConstraints);
        jLabel1.setText("FTP Server");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);
        jLabel2.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jtfURL, gridBagConstraints);
        jtfUsername.setMinimumSize(new java.awt.Dimension(4, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jtfUsername, gridBagConstraints);
        jckAnon.setText("Anonymous");
        jckAnon.setToolTipText("FTP server allows anonymous access");
        jckAnon.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckAnon.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jckAnon.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jckAnonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 1, 0);
        jPanel1.add(jckAnon, gridBagConstraints);
        jckRemember.setText("Remember");
        jckRemember.setToolTipText("Remember FTP details");
        jckRemember.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckRemember.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jckRemember.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jckRememberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jckRemember, gridBagConstraints);
        jckOverwrite.setText("Overwrite");
        jckOverwrite.setToolTipText("If hypernotes directory already exists, it will be overwritten");
        jckOverwrite.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jckOverwrite.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jckOverwrite.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jckOverwriteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jckOverwrite, gridBagConstraints);
        jbtPublish.setText("Publish");
        jbtPublish.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtPublishActionPerformed(evt);
            }
        });
        jPanel2.add(jbtPublish);
        jbntCancel.setText("Cancel");
        jbntCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbntCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jbntCancel);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel2, gridBagConstraints);
        jtfDeployDir.setToolTipText("Directory notes will be published to. Leave blank for root directory.");
        jtfDeployDir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDeployDirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jtfDeployDir, gridBagConstraints);
        jLabel5.setText("Deploy Dir");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel1, gridBagConstraints);
        pack();
    }

    private void jtfDeployDirActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jckRememberActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jckOverwriteActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jckAnonActionPerformed(java.awt.event.ActionEvent evt) {
        jtfUsername.setEnabled(!jckAnon.isSelected());
        jpfPassword.setEnabled(!jckAnon.isSelected());
    }

    private void jbtPublishActionPerformed(java.awt.event.ActionEvent evt) {
        okPressed = true;
        dispose();
    }

    private void jbntCancelActionPerformed(java.awt.event.ActionEvent evt) {
        okPressed = false;
        dispose();
    }

    public String getUsername() {
        return jtfUsername.getText();
    }

    public String getURL() {
        return jtfURL.getText();
    }

    public String getPassword() {
        char[] arr = jpfPassword.getPassword();
        return String.valueOf(arr);
    }

    public boolean okPressed() {
        return okPressed;
    }

    public boolean isAnon() {
        return jckAnon.isSelected();
    }

    public boolean isOverwrite() {
        return jckOverwrite.isSelected();
    }

    public boolean isRemember() {
        return jckRemember.isSelected();
    }

    public String getDeployDir() {
        return jtfDeployDir.getText();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PublishDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JButton jbntCancel;

    private javax.swing.JButton jbtPublish;

    private javax.swing.JCheckBox jckAnon;

    private javax.swing.JCheckBox jckOverwrite;

    private javax.swing.JCheckBox jckRemember;

    private javax.swing.JPasswordField jpfPassword;

    private javax.swing.JTextField jtfDeployDir;

    private javax.swing.JTextField jtfURL;

    private javax.swing.JTextField jtfUsername;
}
