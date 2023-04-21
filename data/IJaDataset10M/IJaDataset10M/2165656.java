package userinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import mobiledesktopserver.*;

/**
 *
 * @author  manish
 */
public class AdminAdministrationPanel extends javax.swing.JPanel implements ActionListener {

    /** Creates new form UserAdministration */
    public AdminAdministrationPanel(UI ui) {
        this.ui = ui;
        this.setBounds(10, 10, 430, 380);
        initComponents();
    }

    private void initComponents() {
        auth = new Authorize();
        handler = new RadioButtonHandler();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jRadioButton1 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JPasswordField();
        buttonGroup1.add(jRadioButton1);
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
        jRadioButton1.addItemListener(handler);
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JPasswordField();
        buttonGroup1.add(jRadioButton2);
        jTextField3.setEditable(false);
        jTextField4.setEditable(false);
        jTextField5.setEditable(false);
        jRadioButton2.addItemListener(handler);
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JPasswordField();
        buttonGroup1.add(jRadioButton3);
        jTextField6.setEditable(false);
        jTextField7.setEditable(false);
        jRadioButton3.addItemListener(handler);
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jRadioButton1.setText("Create User");
        jLabel1.setText("Username");
        jLabel2.setText("Password");
        jRadioButton2.setText("Update User");
        jLabel3.setText("Username");
        jLabel4.setText("Old Password");
        jLabel5.setText("New Password");
        jRadioButton3.setText("Remove User");
        jLabel6.setText("Username");
        jLabel7.setText("Password");
        jButton1.setText("Cancel");
        jButton1.addActionListener(this);
        jButton2.setText("OK");
        jButton2.addActionListener(this);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jRadioButton1)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jRadioButton2)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jRadioButton3)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(40, 40, 40).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jLabel1))).addGroup(layout.createSequentialGroup().addGap(44, 44, 44).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel3).addComponent(jLabel5))).addGroup(layout.createSequentialGroup().addGap(48, 48, 48).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel6)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(jTextField6, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jButton1).addGap(18, 18, 18).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(21, 21, 21))))).addContainerGap(27, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jRadioButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jRadioButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton2).addComponent(jButton1)).addContainerGap()));
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JRadioButton jRadioButton1;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JRadioButton jRadioButton3;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JPasswordField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JPasswordField jTextField4;

    private javax.swing.JPasswordField jTextField5;

    private javax.swing.JTextField jTextField6;

    private javax.swing.JPasswordField jTextField7;

    private UI ui = null;

    private String username = "";

    private String oldPassword = "";

    private String newPassword = "";

    private RadioButtonHandler handler = null;

    private Authorize auth = null;

    private class RadioButtonHandler implements ItemListener {

        public void itemStateChanged(ItemEvent event) {
            if (event.getItem().equals(jRadioButton1)) {
                jTextField1.setEditable(true);
                jTextField2.setEditable(true);
                jTextField3.setEditable(false);
                jTextField4.setEditable(false);
                jTextField5.setEditable(false);
                jTextField6.setEditable(false);
                jTextField7.setEditable(false);
            } else if (event.getItem().equals(jRadioButton2)) {
                jTextField3.setEditable(true);
                jTextField4.setEditable(true);
                jTextField5.setEditable(true);
                jTextField1.setEditable(false);
                jTextField2.setEditable(false);
                jTextField6.setEditable(false);
                jTextField7.setEditable(false);
            } else if (event.getItem().equals(jRadioButton3)) {
                jTextField5.setEditable(false);
                jTextField6.setEditable(true);
                jTextField7.setEditable(true);
                jTextField1.setEditable(false);
                jTextField2.setEditable(false);
                jTextField3.setEditable(false);
                jTextField4.setEditable(false);
            }
        }
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("OK")) {
            if (jRadioButton1.isSelected()) {
                username = jTextField1.getText();
                oldPassword = new String(jTextField2.getPassword());
                if (username != "" && oldPassword != "") {
                    auth.addUser(username, oldPassword, ".server.mds");
                }
            } else if (jRadioButton2.isSelected()) {
                username = jTextField3.getText();
                oldPassword = new String(jTextField4.getPassword());
                newPassword = new String(jTextField5.getPassword());
                System.out.println("I am called");
                if (username != "" && oldPassword != "" && newPassword != "") {
                    if (auth.authorize(username, oldPassword, ".server.mds")) {
                        System.out.println("I am also called");
                        auth.updateUser(username, newPassword, ".server.mds");
                    }
                }
            } else if (jRadioButton3.isSelected()) {
                username = jTextField6.getText();
                oldPassword = new String(jTextField7.getPassword());
                if (username != "" && oldPassword != "") {
                    if (auth.authorize(username, oldPassword, ".server.mds")) {
                        auth.removeUser(username, newPassword, ".server.mds");
                    }
                }
            }
            ui.closeAdvanceDialog();
        } else if (event.getActionCommand().equals("Cancel")) {
            ui.closeAdvanceDialog();
        }
    }
}
