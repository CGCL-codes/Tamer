package eveskillwatch;

import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.util.prefs.Preferences;
import javax.swing.SwingUtilities;

/**
 *
 * @author  alexanagnos
 */
public class LoginDialog extends javax.swing.JDialog {

    /** Creates new form NewJDialog */
    public LoginDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        UserIDField.setText(prefs.get(EveSkillWatch.USER_ID_KEY, EveSkillWatch.USER_ID_DEFUALT));
        CharacerIDField.setText(prefs.get(EveSkillWatch.CHARACTER_ID_KEY, EveSkillWatch.CHARACTER_ID_DEFUALT));
        APIKeyField.setText(prefs.get(EveSkillWatch.API_KEY_KEY, EveSkillWatch.API_KEY_DEFUALT));
    }

    public void showGetInfo() {
        this.setVisible(true);
        Thread t = new Thread() {

            public void run() {
                while (isVisible()) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ex) {
                        EveSkillWatch.Error("Login Dialog Sleep Failed.");
                    }
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            EveSkillWatch.Error("LoginForm Thread Join failed");
        }
    }

    private void initComponents() {
        character_id_label = new javax.swing.JLabel();
        user_id_label = new javax.swing.JLabel();
        api_key_label = new javax.swing.JLabel();
        CharacerIDField = new javax.swing.JTextField();
        UserIDField = new javax.swing.JTextField();
        APIKeyField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");
        character_id_label.setText("Character ID");
        user_id_label.setText("User ID");
        api_key_label.setText("API Key");
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(user_id_label).addComponent(api_key_label).addComponent(character_id_label)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(UserIDField, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE).addComponent(CharacerIDField, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE).addComponent(APIKeyField, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addComponent(cancelButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(okButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(api_key_label).addComponent(APIKeyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(character_id_label).addComponent(CharacerIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(user_id_label).addComponent(UserIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okButton).addComponent(cancelButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.put(EveSkillWatch.USER_ID_KEY, UserIDField.getText());
        prefs.put(EveSkillWatch.CHARACTER_ID_KEY, CharacerIDField.getText());
        prefs.put(EveSkillWatch.API_KEY_KEY, APIKeyField.getText());
        this.setVisible(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new LoginDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JTextField APIKeyField;

    private javax.swing.JTextField CharacerIDField;

    private javax.swing.JTextField UserIDField;

    private javax.swing.JLabel api_key_label;

    private javax.swing.JButton cancelButton;

    private javax.swing.JLabel character_id_label;

    private javax.swing.JButton okButton;

    private javax.swing.JLabel user_id_label;
}
