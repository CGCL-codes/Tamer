package gui.swing;

import gui.MainSystray;
import gui.listener.PopupMenuTextFieldsListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import resources.Messages;
import core.crypto.RSA;

/**
 *
 * @author Glauber Magalh�es Pires
 */
public final class EncryptPasswordGUI extends javax.swing.JDialog {

    /**
     *
     */
    private static final long serialVersionUID = 4433371431545617466L;

    private RSA rsa = new RSA();

    /** Creates new form EncritarSenhaGUI */
    public EncryptPasswordGUI(MainGUI parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle(Messages.message.getString("main.menu.file.encryptPassword"));
        this.getRootPane().setDefaultButton(jButtonEncriptar);
        gui.swing.Constantes.addKeyAndContainerListenerRecursively(this, keylistener);
    }

    KeyListener keylistener = new java.awt.event.KeyAdapter() {

        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) dispose();
        }
    };

    private void initComponents() {
        jButtonEncriptar = new javax.swing.JButton();
        jLabelOriginalPassword = new javax.swing.JLabel();
        jLabelEncryptedPassword = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaOriginalPasswords = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaEncryptedPasswords = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jButtonEncriptar.setText("Encriptar");
        jButtonEncriptar.setText(Messages.message.getString("encryptPassword.button.encrypt"));
        jButtonEncriptar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEncriptarActionPerformed(evt);
            }
        });
        jLabelOriginalPassword.setText("Senha Original");
        jLabelOriginalPassword.setText(Messages.message.getString("encryptPassword.label.originalPassword"));
        jLabelEncryptedPassword.setText("Senha Encriptada");
        jLabelEncryptedPassword.setText(Messages.message.getString("encryptPassword.label.encryptedPassword"));
        jTextAreaOriginalPasswords.setPreferredSize(new java.awt.Dimension(102, 92));
        jTextAreaOriginalPasswords.setToolTipText(Messages.message.getString("encryptPassword.label.onePasswordPerLine"));
        jTextAreaOriginalPasswords.addMouseListener(new PopupMenuTextFieldsListener());
        jScrollPane1.setViewportView(jTextAreaOriginalPasswords);
        jTextAreaEncryptedPasswords.setEditable(false);
        jTextAreaEncryptedPasswords.setLineWrap(true);
        jTextAreaEncryptedPasswords.addMouseListener(new PopupMenuTextFieldsListener());
        jScrollPane2.setViewportView(jTextAreaEncryptedPasswords);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabelOriginalPassword).add(jLabelEncryptedPassword)).add(14, 14, 14).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)).addContainerGap()).add(layout.createSequentialGroup().add(188, 188, 188).add(jButtonEncriptar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE).add(209, 209, 209)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabelOriginalPassword).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabelEncryptedPassword).add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButtonEncriptar).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 511) / 2, (screenSize.height - 295) / 2, 511, 295);
    }

    private void jButtonEncriptarActionPerformed(java.awt.event.ActionEvent evt) {
        String passwordsOriginais[] = jTextAreaOriginalPasswords.getText().split("\n");
        boolean hasPasswords = false;
        boolean hasWhiteSpacesOnBeginningOrEnd = false;
        StringBuilder stb = new StringBuilder();
        for (String passwordOriginal : passwordsOriginais) {
            if (passwordOriginal.length() != passwordOriginal.trim().length()) hasWhiteSpacesOnBeginningOrEnd = true;
            if (passwordOriginal.length() != 0) {
                hasPasswords = true;
                stb.append(rsa.encriptar(passwordOriginal) + "\n");
            }
        }
        if (!hasPasswords) {
            MainSystray.guiFactory.getMessageDisplayer().displayErrorMessage(Messages.exception.getString("encryptPassword.withoutPassword"));
            return;
        }
        if (hasWhiteSpacesOnBeginningOrEnd) {
            MainSystray.guiFactory.getMessageDisplayer().displayWarningMessage(Messages.exception.getString("encryptPassword.spaceAtBeginningOrEnd"));
        }
        jTextAreaEncryptedPasswords.setText(stb.toString());
        jTextAreaEncryptedPasswords.selectAll();
    }

    private javax.swing.JButton jButtonEncriptar;

    private javax.swing.JLabel jLabelEncryptedPassword;

    private javax.swing.JLabel jLabelOriginalPassword;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextArea jTextAreaEncryptedPasswords;

    private javax.swing.JTextArea jTextAreaOriginalPasswords;
}
