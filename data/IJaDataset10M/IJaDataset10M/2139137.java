package org.verus.ngl.client.technicalprocessing;

import org.verus.ngl.client.guicomponents.NGLComponentDecorator;

/**
 *
 * @author  Verus
 */
public class MarcFixedFieldDialog extends javax.swing.JDialog {

    private boolean mode = false;

    public static String st = "";

    NGLComponentDecorator nglComponentDecorator = null;

    /** Creates new form MarcFixedFieldDialog */
    public MarcFixedFieldDialog(java.awt.Frame frame) {
        super(frame);
        this.setTitle("FixedField Dialog");
        this.setSize(400, 200);
        initComponents();
        nglComponentDecorator = NGLComponentDecorator.getInstance();
        nglComponentDecorator.setNGLDecorator(this);
    }

    public MarcFixedFieldDialog(javax.swing.JDialog frame) {
        super(frame);
        this.setTitle("FixedField Dialog");
        this.setSize(400, 200);
        initComponents();
        nglComponentDecorator = NGLComponentDecorator.getInstance();
        nglComponentDecorator.setNGLDecorator(this);
    }

    public String getNGLFixedField() {
        return taFixedString.getText();
    }

    public void setNGLFixedField(String st) {
        taFixedString.setText(st);
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        taFixedString = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        bnOk = new javax.swing.JButton();
        bnHelp = new javax.swing.JButton();
        bnCancel = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 150));
        taFixedString.setColumns(20);
        taFixedString.setRows(5);
        jScrollPane1.setViewportView(taFixedString);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 40));
        bnOk.setMnemonic('o');
        bnOk.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Ok"));
        bnOk.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Ok"));
        bnOk.setPreferredSize(new java.awt.Dimension(75, 26));
        bnOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnOkActionPerformed(evt);
            }
        });
        jPanel2.add(bnOk);
        bnHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/verus/ngl/client/images/Help16.gif")));
        bnHelp.setMnemonic('h');
        bnHelp.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Help"));
        bnHelp.setPreferredSize(new java.awt.Dimension(75, 26));
        jPanel2.add(bnHelp);
        bnCancel.setMnemonic('c');
        bnCancel.setText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Cancel"));
        bnCancel.setToolTipText(org.verus.ngl.client.main.NGLResourceBundle.getInstance().getString("Cancel"));
        bnCancel.setPreferredSize(new java.awt.Dimension(75, 26));
        bnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnCancelActionPerformed(evt);
            }
        });
        jPanel2.add(bnCancel);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void bnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void bnOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.setMode(true);
        this.setVisible(false);
    }

    private javax.swing.JButton bnCancel;

    private javax.swing.JButton bnHelp;

    private javax.swing.JButton bnOk;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea taFixedString;
}
