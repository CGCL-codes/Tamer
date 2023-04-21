package org.paccman.ui.main;

/**
 *
 * @author  joao
 */
public class PaccmanAboutDlg extends javax.swing.JDialog {

    /** Creates new form PaccmanAboutDlg
     * @param parent
     */
    public PaccmanAboutDlg(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        paccmanLbl = new javax.swing.JLabel();
        paccmanDescLbl = new javax.swing.JLabel();
        versionLbl = new javax.swing.JLabel();
        closeBtn = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        paccmanLbl.setFont(new java.awt.Font("DejaVu Sans", 1, 18));
        paccmanLbl.setText("PAccMan");
        paccmanDescLbl.setFont(new java.awt.Font("DejaVu Sans", 1, 18));
        paccmanDescLbl.setText("Personal ACCount MANager");
        versionLbl.setText("Version: " + ContextMain.getPaccmanVersion());
        closeBtn.setText("Close");
        closeBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(109, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(paccmanDescLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(paccmanLbl).addComponent(versionLbl)).addGap(11, 11, 11)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(closeBtn).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(paccmanLbl).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(paccmanDescLbl).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(versionLbl).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(closeBtn).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dialogSize = getSize();
        setLocation((screenSize.width - dialogSize.width) / 2, (screenSize.height - dialogSize.height) / 2);
    }

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private javax.swing.JButton closeBtn;

    private javax.swing.JLabel paccmanDescLbl;

    private javax.swing.JLabel paccmanLbl;

    private javax.swing.JLabel versionLbl;
}
