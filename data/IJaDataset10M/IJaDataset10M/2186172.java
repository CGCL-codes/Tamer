package fuentes;

import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import ventanas.*;

/**
 *
 * @author Administrador
 */
public class Login extends javax.swing.JDialog {

    private JLabel LabelUser;

    private JLabel LabelPassword;

    private JPasswordField TEXTPass;

    private JTextField TEXTUsuario;

    private JButton ButtonIniciar;

    private JButton ButtonCancelar;

    public Login(java.awt.Frame parent, boolean modal, boolean sesion) {
        super(parent, modal);
        dibujar_dialogo_login(sesion);
        Image icono = new ImageIcon(getClass().getResource("/images/ingreso.png")).getImage();
        this.setIconImage(icono);
        setResizable(false);
    }

    private void dibujar_dialogo_login(boolean sesion) {
        LabelUser = new javax.swing.JLabel();
        LabelPassword = new javax.swing.JLabel();
        TEXTPass = new javax.swing.JPasswordField();
        TEXTUsuario = new javax.swing.JTextField();
        ButtonIniciar = new javax.swing.JButton();
        ButtonCancelar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        if (sesion) {
            setTitle("Exit");
            ButtonIniciar.setText("Aceptar");
            ButtonCancelar.setText("Cancelar");
        } else {
            setTitle("Login");
            ButtonIniciar.setText("Iniciar");
            ButtonCancelar.setText("Salir");
        }
        setName("Login");
        LabelUser.setFont(new java.awt.Font("Arial", 0, 16));
        LabelUser.setText("Username");
        LabelPassword.setFont(new java.awt.Font("Arial", 0, 16));
        LabelPassword.setText("Password");
        TEXTPass.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TEXTPassActionPerformed(evt);
            }

            private void TEXTPassActionPerformed(ActionEvent evt) {
                if (TEXTUsuario.getText().length() > 0 && TEXTPass.getPassword().length > 2) {
                    ButtonIniciar.doClick();
                } else {
                    if (TEXTUsuario.getText().isEmpty()) {
                        TEXTUsuario.requestFocus();
                    } else {
                        TEXTPass.requestFocus();
                    }
                }
            }
        });
        TEXTUsuario.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TEXTUsuarioActionPerformed(evt);
            }

            private void TEXTUsuarioActionPerformed(ActionEvent evt) {
                TEXTPass.requestFocus();
            }
        });
        ButtonIniciar.setFont(new java.awt.Font("Arial", 1, 11));
        ButtonIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Button_aceptar.png")));
        if (!sesion) {
            ButtonIniciar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonIniciarActionPerformed(evt);
                }

                private void ButtonIniciarActionPerformed(ActionEvent evt) {
                    ImageIcon icono = new ImageIcon(getClass().getResource("/images/input.png"));
                    dispose();
                    JOptionPane.showMessageDialog(null, "<html> <fonnt size 4 > <B> Wellcome", "successful entry", JOptionPane.INFORMATION_MESSAGE, icono);
                    Principal.switchOnOffPanel(true, 1);
                }
            });
        } else {
            ButtonIniciar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonIniciarActionPerformed(evt);
                }

                private void ButtonIniciarActionPerformed(ActionEvent evt) {
                    ImageIcon icono = new ImageIcon(getClass().getResource("/images/input.png"));
                    dispose();
                    JOptionPane.showMessageDialog(null, "<html> <fonnt size 4 > <B> Bye Bye", "Until then", JOptionPane.INFORMATION_MESSAGE, icono);
                    System.exit(0);
                }
            });
        }
        ButtonCancelar.setFont(new java.awt.Font("Arial", 1, 11));
        ButtonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/button_cancelar.png")));
        if (!sesion) {
            ButtonCancelar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonCancelarActionPerformed(evt);
                }

                private void ButtonCancelarActionPerformed(ActionEvent evt) {
                    System.exit(0);
                }
            });
        } else {
            ButtonCancelar.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    ButtonCancelarActionPerformed(evt);
                }

                private void ButtonCancelarActionPerformed(ActionEvent evt) {
                    dispose();
                }
            });
        }
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(LabelUser).addComponent(LabelPassword)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(TEXTUsuario).addComponent(TEXTPass, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addGap(40, 40, 40).addComponent(ButtonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(ButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(26, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(LabelUser).addComponent(TEXTUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(55, 55, 55).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(LabelPassword).addComponent(TEXTPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(ButtonCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ButtonIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(24, 24, 24)));
        pack();
    }
}
