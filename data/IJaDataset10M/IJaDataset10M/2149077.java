package sk.yw.azetclient.gui;

import sk.yw.azetclient.Main;

/**
 *
 * @author  error216
 */
public class AboutDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1393381543l;

    /** Creates new form AboutDialog */
    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Main.getText("aboutDialog.title"));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText(Main.APPLICATION_NAME + " " + Main.VERSION + "\n" + Main.getText("aboutDialog.text"));
        jScrollPane1.setViewportView(jTextArea1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE));
        pack();
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;
}
