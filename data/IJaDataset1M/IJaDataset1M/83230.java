package totalpos;

import java.awt.Frame;
import java.sql.SQLException;
import javax.swing.JDialog;

/**
 *
 * @author shidalgo
 */
public class PasswordNeeded extends JDialog {

    User user;

    private boolean isOk = false;

    Frame parent;

    /** Creates new form PasswordNeeded */
    public PasswordNeeded(java.awt.Frame parent, boolean modal, User u) {
        super(parent, modal);
        initComponents();
        this.user = u;
        this.parent = parent;
        descriptionLabel.setText(("Introduzca la contraseña " + (user.getNombre() != null && !user.getNombre().isEmpty() ? " para " + user.getNombre() : "")));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        descriptionLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(Constants.appName);
        setResizable(false);
        descriptionLabel.setFont(new java.awt.Font("Courier New", 1, 14));
        descriptionLabel.setText("Introduzca la contraseña");
        descriptionLabel.setName("descriptionLabel");
        passwordField.setName("passwordField");
        passwordField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE).addComponent(descriptionLabel)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(descriptionLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            ConnectionDrivers.login(this.user.getLogin(), passwordField.getPassword());
            isOk = true;
            Shared.userInsertedPasswordOk(this.user.getLogin());
            this.setVisible(false);
            dispose();
        } catch (SQLException ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, "No se pudo establecer conexión con la base de datos.");
            msg.show(this);
        } catch (Exception ex) {
            MessageBox msg = new MessageBox(MessageBox.SGN_CAUTION, ex.getMessage(), ex);
            msg.show(this);
            if (ex.getMessage().equals(Constants.wrongPasswordMsg)) {
                try {
                    Shared.userTrying(this.user.getLogin());
                } catch (Exception ex1) {
                    msg = new MessageBox(MessageBox.SGN_CAUTION, (ex1.getMessage().equals(Constants.userLocked) ? Constants.userLocked : ex1.getMessage()), ex1);
                    msg.show(null);
                    this.dispose();
                    Shared.reload();
                }
            } else {
                msg = new MessageBox(MessageBox.SGN_CAUTION, ex.getMessage(), ex);
                msg.show(null);
            }
        }
    }

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JPasswordField passwordField;

    boolean isPasswordOk() {
        return isOk;
    }
}