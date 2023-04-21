package gridrm.agents.user.gui;

/**
 * @author Mateusz Dominiak <mateusz.dominiak@gmail.com>
 *
 */
public class JResourceEdit extends javax.swing.JDialog {

    public static final String UnitaryComputer = "Unitary Computer";

    public static final String Cluster = "Cluster";

    /** Creates new form JResourceEdit */
    public JResourceEdit(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        cmbResourceType.addItem(UnitaryComputer);
        cmbResourceType.addItem(Cluster);
    }

    private void initComponents() {
        plnCPU = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nmrCpuCount = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        txtCpuClockSpeed = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbCpuVendors = new javax.swing.JComboBox();
        cmbCpuArch = new javax.swing.JComboBox();
        pnlOS = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmbOSType = new javax.swing.JComboBox();
        txtOSName = new javax.swing.JTextField();
        txtOSVersion = new javax.swing.JTextField();
        pnlSoftware = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSoftwareName = new javax.swing.JTextField();
        txtSoftwareVersion = new javax.swing.JTextField();
        pnlSpace = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtMemoryAmount = new javax.swing.JTextField();
        txtFileSystemCapacity = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbResourceType = new javax.swing.JComboBox();
        nmrResourceCount = new javax.swing.JSpinner();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        plnCPU.setBorder(javax.swing.BorderFactory.createTitledBorder("CPU"));
        jLabel1.setText("Count");
        nmrCpuCount.setValue(1);
        jLabel2.setText("Clock Speed (Mhz)");
        txtCpuClockSpeed.setText("100");
        jLabel7.setText("Vendor");
        jLabel7.setEnabled(false);
        jLabel8.setText("Architecture");
        jLabel8.setEnabled(false);
        cmbCpuVendors.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "any", "Intel", "AMD" }));
        cmbCpuVendors.setEnabled(false);
        cmbCpuArch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "any", "x86", "x86-64", "AMD64", "EMT64", "Alpha", "IA-64", "MIPS", "PA-RISC", "PowerPC", "SPARC" }));
        cmbCpuArch.setEnabled(false);
        javax.swing.GroupLayout plnCPULayout = new javax.swing.GroupLayout(plnCPU);
        plnCPU.setLayout(plnCPULayout);
        plnCPULayout.setHorizontalGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(plnCPULayout.createSequentialGroup().addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(plnCPULayout.createSequentialGroup().addContainerGap().addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel7).addComponent(jLabel2).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtCpuClockSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(nmrCpuCount, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbCpuVendors, 0, 156, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, plnCPULayout.createSequentialGroup().addGap(39, 39, 39).addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmbCpuArch, 0, 156, Short.MAX_VALUE))).addContainerGap()));
        plnCPULayout.setVerticalGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(plnCPULayout.createSequentialGroup().addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(nmrCpuCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(txtCpuClockSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(cmbCpuVendors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(plnCPULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(cmbCpuArch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(11, 11, 11)));
        pnlOS.setBorder(javax.swing.BorderFactory.createTitledBorder("Operating System"));
        pnlOS.setEnabled(false);
        jLabel9.setText("Type");
        jLabel9.setEnabled(false);
        jLabel10.setText("Name");
        jLabel10.setEnabled(false);
        jLabel11.setText("Version");
        jLabel11.setEnabled(false);
        cmbOSType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "any", "Unix", "Linux", "Windows" }));
        cmbOSType.setEnabled(false);
        txtOSName.setEnabled(false);
        txtOSVersion.setEnabled(false);
        javax.swing.GroupLayout pnlOSLayout = new javax.swing.GroupLayout(pnlOS);
        pnlOS.setLayout(pnlOSLayout);
        pnlOSLayout.setHorizontalGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlOSLayout.createSequentialGroup().addGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlOSLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel10).addComponent(jLabel9))).addGroup(pnlOSLayout.createSequentialGroup().addGap(16, 16, 16).addComponent(jLabel11))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtOSVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE).addComponent(cmbOSType, 0, 147, Short.MAX_VALUE).addComponent(txtOSName, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)).addGap(66, 66, 66)));
        pnlOSLayout.setVerticalGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlOSLayout.createSequentialGroup().addContainerGap().addGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(cmbOSType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(txtOSName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(txtOSVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pnlSoftware.setBorder(javax.swing.BorderFactory.createTitledBorder("Software"));
        pnlSoftware.setEnabled(false);
        jLabel5.setText("Name:");
        jLabel5.setEnabled(false);
        jLabel6.setText("Version:");
        jLabel6.setEnabled(false);
        txtSoftwareName.setEnabled(false);
        txtSoftwareVersion.setEnabled(false);
        javax.swing.GroupLayout pnlSoftwareLayout = new javax.swing.GroupLayout(pnlSoftware);
        pnlSoftware.setLayout(pnlSoftwareLayout);
        pnlSoftwareLayout.setHorizontalGroup(pnlSoftwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlSoftwareLayout.createSequentialGroup().addContainerGap().addGroup(pnlSoftwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel5).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlSoftwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtSoftwareVersion, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE).addComponent(txtSoftwareName, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)).addContainerGap()));
        pnlSoftwareLayout.setVerticalGroup(pnlSoftwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlSoftwareLayout.createSequentialGroup().addContainerGap().addGroup(pnlSoftwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(txtSoftwareName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlSoftwareLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(txtSoftwareVersion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pnlSpace.setBorder(javax.swing.BorderFactory.createTitledBorder("Space"));
        jLabel12.setText("Memory (MB)");
        jLabel13.setText("FileSystem (MB)");
        txtMemoryAmount.setText("128");
        txtFileSystemCapacity.setText("128");
        javax.swing.GroupLayout pnlSpaceLayout = new javax.swing.GroupLayout(pnlSpace);
        pnlSpace.setLayout(pnlSpaceLayout);
        pnlSpaceLayout.setHorizontalGroup(pnlSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlSpaceLayout.createSequentialGroup().addContainerGap().addGroup(pnlSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel12).addComponent(jLabel13)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(pnlSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtFileSystemCapacity).addComponent(txtMemoryAmount, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)).addContainerGap(14, Short.MAX_VALUE)));
        pnlSpaceLayout.setVerticalGroup(pnlSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlSpaceLayout.createSequentialGroup().addGroup(pnlSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(txtMemoryAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlSpaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel13).addComponent(txtFileSystemCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));
        jLabel3.setText("Type:");
        jLabel4.setText("Count:");
        jLabel4.setEnabled(false);
        nmrResourceCount.setEnabled(false);
        nmrResourceCount.setValue(1);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap(19, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(nmrResourceCount, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbResourceType, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(cmbResourceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(nmrResourceCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pnlSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(plnCPU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(55, 55, 55).addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pnlOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(pnlSpace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(pnlSoftware, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pnlOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(plnCPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(26, 26, 26).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnCancel).addComponent(btnOK)))).addGap(29, 29, 29)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(228, Short.MAX_VALUE).addComponent(pnlSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {
        okPressed = true;
        setVisible(false);
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        okPressed = false;
        setVisible(false);
    }

    boolean okPressed = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                JResourceEdit dialog = new JResourceEdit(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    public String getResourceType() {
        return cmbResourceType.getSelectedItem().toString();
    }

    public int getCpuCount() {
        return Integer.valueOf(nmrCpuCount.getValue().toString());
    }

    public int getCpuClockSpeed() {
        return Integer.valueOf(txtCpuClockSpeed.getText());
    }

    public String getSoftwareName() {
        return txtSoftwareName.getText();
    }

    public String getSoftwareVersion() {
        return txtSoftwareVersion.getText();
    }

    public int getMemoryAmount() {
        return Integer.valueOf(txtMemoryAmount.getText());
    }

    public int getFileSystemCapacity() {
        return Integer.valueOf(txtFileSystemCapacity.getText());
    }

    public String getOSName() {
        return txtOSName.getText();
    }

    public String getOSVersion() {
        return txtOSVersion.getName();
    }

    public boolean openDialog() {
        this.setVisible(true);
        return okPressed;
    }

    private javax.swing.JButton btnCancel;

    private javax.swing.JButton btnOK;

    private javax.swing.JComboBox cmbCpuArch;

    private javax.swing.JComboBox cmbCpuVendors;

    private javax.swing.JComboBox cmbOSType;

    private javax.swing.JComboBox cmbResourceType;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JSpinner nmrCpuCount;

    private javax.swing.JSpinner nmrResourceCount;

    private javax.swing.JPanel plnCPU;

    private javax.swing.JPanel pnlOS;

    private javax.swing.JPanel pnlSoftware;

    private javax.swing.JPanel pnlSpace;

    private javax.swing.JTextField txtCpuClockSpeed;

    private javax.swing.JTextField txtFileSystemCapacity;

    private javax.swing.JTextField txtMemoryAmount;

    private javax.swing.JTextField txtOSName;

    private javax.swing.JTextField txtOSVersion;

    private javax.swing.JTextField txtSoftwareName;

    private javax.swing.JTextField txtSoftwareVersion;
}
