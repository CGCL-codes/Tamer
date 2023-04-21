package org.verus.ngl.client.administration.technicalprocessing;

/**
 *
 * @author  PIXEL1
 */
public class ListSubLocationsFrame extends javax.swing.JInternalFrame {

    ListSubLocationsPanel panel = null;

    private static ListSubLocationsFrame instance = null;

    public static ListSubLocationsFrame getInstance() {
        if (instance == null) {
            instance = new ListSubLocationsFrame();
        }
        return instance;
    }

    /** Creates new form ListSubLocationsFrame */
    public ListSubLocationsFrame() {
        initComponents();
        panel = new ListSubLocationsPanel(outputSuggestionsPanel1);
        jPanel2.add(panel);
        this.setTitle(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("ListSubLocations"));
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        bnHelp = new javax.swing.JButton();
        bnClose = new javax.swing.JButton();
        outputSuggestionsPanel1 = new org.verus.ngl.client.guicomponents.OutputSuggestionsPanel();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 40));
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/verus/ngl/client/images/Help16.gif")));
        bnHelp.setMnemonic('h');
        bnHelp.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Help"));
        bnHelp.setPreferredSize(new java.awt.Dimension(75, 26));
        jPanel3.add(bnHelp);
        bnClose.setMnemonic('e');
        bnClose.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Close"));
        bnClose.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Close"));
        bnClose.setPreferredSize(new java.awt.Dimension(75, 26));
        bnClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCloseActionPerformed(evt);
            }
        });
        jPanel3.add(bnClose);
        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        getContentPane().add(outputSuggestionsPanel1, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bnCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private javax.swing.JButton bnClose;

    private javax.swing.JButton bnHelp;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private org.verus.ngl.client.guicomponents.OutputSuggestionsPanel outputSuggestionsPanel1;
}
