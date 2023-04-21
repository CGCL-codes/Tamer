package org.paja.group.capa.visual.alfa;

import java.awt.event.ItemEvent;
import java.util.prefs.Preferences;
import org.jdesktop.swingx.JXImagePanel;
import org.paja.group.logica.alfa.usuario.dao.Login;
import org.paja.group.util.capa.visual.util.JOptionPaneLoader;

/**
 *
 * @author  Claver Isaac
 */
public class InicioSesion extends javax.swing.JFrame {

    private JOptionPaneLoader optionPane;

    private Preferences prefs = Preferences.userNodeForPackage(InicioSesion.class);

    private int recordar_contrasenna;

    /** Creates new form InicioSesion */
    public InicioSesion() {
        initComponents();
        optionPane = new JOptionPaneLoader(this, "Alfa Solutions - Paja's Group");
        cargarPreferencias();
        pnlImage.revalidate();
        pnlImage.repaint();
    }

    private void cargarPreferencias() {
        recordar_contrasenna = prefs.getInt("recordar_contrasenna", 0);
        chkRecordar.setSelected(recordar_contrasenna == 1);
        txtPass.setText(prefs.get("contrasenna", ""));
        txtUser.setText(prefs.get("usuario", ""));
    }

    private void initComponents() {
        pnlImage = new JXImagePanel(getClass().getResource("/images/inicio_sesion.jpg"));
        ;
        jLabel1 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        chkRecordar = new javax.swing.JCheckBox();
        lnkPass = new com.l2fprod.common.swing.JLinkButton();
        btnCancelar = new javax.swing.JButton();
        btnIniciar = new javax.swing.JButton();
        txtPass = new javax.swing.JPasswordField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Alfa Solutions - Paja's Group");
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));
        jLabel1.setText("Usuario:");
        txtUser.setText("jayerdis");
        txtUser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });
        jLabel2.setText("Contraseña:");
        chkRecordar.setFont(new java.awt.Font("DejaVu Sans", 0, 12));
        chkRecordar.setText("Recordarme en este equipo");
        chkRecordar.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkRecordarItemStateChanged(evt);
            }
        });
        lnkPass.setForeground(new java.awt.Color(0, 51, 204));
        lnkPass.setText("¿Ha Olvidado su Contraseña?");
        lnkPass.setFont(new java.awt.Font("DejaVu Sans", 0, 10));
        lnkPass.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lnkPass.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lnkPassActionPerformed(evt);
            }
        });
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        btnIniciar.setText("Iniciar Sesión");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        txtPass.setText("jayerdis");
        txtPass.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout pnlImageLayout = new javax.swing.GroupLayout(pnlImage);
        pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlImageLayout.createSequentialGroup().addGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlImageLayout.createSequentialGroup().addGap(100, 100, 100).addComponent(btnIniciar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlImageLayout.createSequentialGroup().addContainerGap(72, Short.MAX_VALUE).addComponent(lnkPass, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlImageLayout.createSequentialGroup().addContainerGap(124, Short.MAX_VALUE).addGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(txtPass, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(chkRecordar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(txtUser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE).addComponent(jLabel1)))).addContainerGap()));
        pnlImageLayout.setVerticalGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlImageLayout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addGap(3, 3, 3).addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(chkRecordar).addGap(4, 4, 4).addComponent(lnkPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnIniciar).addComponent(btnCancelar)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        getContentPane().add(pnlImage);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 345) / 2, (screenSize.height - 227) / 2, 345, 227);
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        login();
    }

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {
        login();
    }

    private void txtPassActionPerformed(java.awt.event.ActionEvent evt) {
        login();
    }

    private void chkRecordarItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) prefs.putInt("recordar_contrasenna", 1); else prefs.putInt("recordar_contrasenna", 0);
    }

    private void lnkPassActionPerformed(java.awt.event.ActionEvent evt) {
        String mensaje = "Ud no tiene acceso a internet o la conexion es muy limitada.\n" + "Para hacer uso de esta opcion consulte con su administrador";
        JOptionPaneLoader.JOptionPane_Error(mensaje);
    }

    private void login() {
        String usuario = txtUser.getText();
        if (usuario.trim().equals("")) {
            JOptionPaneLoader.JOptionPane_Error("Favor coloque un usuario");
            return;
        }
        char[] contrasenna = txtPass.getPassword();
        if (contrasenna.length == 0) {
            JOptionPaneLoader.JOptionPane_Error("No puede dejar cotraseña vacia");
            return;
        }
        if (Login.login(usuario, contrasenna)) {
            new AlfaInicio().setVisible(true);
            if (chkRecordar.isSelected()) {
                prefs.put("contrasenna", new String(contrasenna));
                prefs.put("usuario", usuario);
            } else {
                prefs.put("contrasenna", "");
                prefs.put("usuario", "");
            }
            this.dispose();
        } else {
            JOptionPaneLoader.JOptionPane_Error("Error Usuario o Contraseña, erronea");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Login.preload();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new InicioSesion().setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnIniciar;

    private javax.swing.JCheckBox chkRecordar;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private com.l2fprod.common.swing.JLinkButton lnkPass;

    private org.jdesktop.swingx.JXImagePanel pnlImage;

    private javax.swing.JPasswordField txtPass;

    private javax.swing.JTextField txtUser;
}
