package bahamontes;

import javax.swing.ImageIcon;

/**
 *
 * @author  Onno Kluyt
 */
public class About extends javax.swing.JDialog {

    /** Creates new form About */
    public About(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ClassLoader c = this.getClass().getClassLoader();
        iconLabel.setIcon(new ImageIcon(c.getResource("TheAuthor.jpg")));
    }

    private void initComponents() {
        okButton = new javax.swing.JButton();
        label1 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        iconLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        okButton.setText("OK");
        okButton.setSelected(true);
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        label1.setText("Written by Onno Kluyt");
        label2.setText("Enjoy!");
        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        jLabel1.setText("Bahamontes, Ride Analysis, v1.5");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1).add(label1).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(okButton).add(label2)).add(204, 204, 204).add(iconLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(label1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(label2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(okButton)).add(iconLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)).addContainerGap()));
        pack();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private javax.swing.JLabel iconLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel label1;

    private javax.swing.JLabel label2;

    private javax.swing.JButton okButton;
}
