package pulsarhunter.jreaper.gui;

import javax.swing.DefaultComboBoxModel;
import pulsarhunter.jreaper.DataLibraryType;
import pulsarhunter.jreaper.Main;

/**
 *
 * @author  mkeith
 */
public class NewDataLibraryFrame extends javax.swing.JFrame {

    /** Creates new form NewDataLibraryFrame */
    public NewDataLibraryFrame(DataLibraryType[] dataLibraryTypes) {
        initComponents();
        this.jComboBox_dataLibraryTypes.setModel(new DefaultComboBoxModel(dataLibraryTypes));
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jComboBox_dataLibraryTypes = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabel1.setText("Create New DataLibrary");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 240, -1));
        jLabel2.setText("Some Plugins define their own DataLibrary types");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        jLabel3.setText("These add functionality that is useful for some data types");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
        jLabel4.setText("Here are the DataLibrary types avaliable for you:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));
        jComboBox_dataLibraryTypes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel1.add(jComboBox_dataLibraryTypes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 320, -1));
        jButton1.setText("Create this type!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, -1, -1));
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 410) / 2, (screenSize.height - 252) / 2, 410, 252);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        ((DataLibraryType) jComboBox_dataLibraryTypes.getSelectedItem()).showCreationDialog();
        this.setVisible(false);
        this.dispose();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        this.dispose();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JComboBox jComboBox_dataLibraryTypes;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;
}
