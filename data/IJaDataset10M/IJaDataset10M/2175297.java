package net.adrianromero.tpv.forms;

import javax.swing.*;
import java.awt.*;
import net.adrianromero.data.gui.JMessageDialog;
import net.adrianromero.data.gui.MessageInf;
import net.adrianromero.tpv.util.Hashcypher;

public class JDlgChangePassword extends javax.swing.JDialog {

    private String m_sOldPassword;

    private String m_sNewPassword;

    /** Creates new form ChangePassword */
    private JDlgChangePassword(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    /** Creates new form ChangePassword */
    private JDlgChangePassword(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }

    private String init(String sOldPassword) {
        initComponents();
        getRootPane().setDefaultButton(jcmdOK);
        m_sOldPassword = sOldPassword;
        m_sNewPassword = null;
        setVisible(true);
        return m_sNewPassword;
    }

    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }

    public static String showMessage(Component parent, String sOldPassword) {
        Window window = getWindow(parent);
        JDlgChangePassword myMsg;
        if (window instanceof Frame) {
            myMsg = new JDlgChangePassword((Frame) window, true);
        } else {
            myMsg = new JDlgChangePassword((Dialog) window, true);
        }
        return myMsg.init(sOldPassword);
    }

    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtxtPasswordOld = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jtxtPasswordNew = new javax.swing.JPasswordField();
        jtxtPasswordRepeat = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("title.changepassword"));
        setResizable(false);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/adrianromero/images/button_ok.png")));
        jcmdOK.setText(AppLocal.getIntString("Button.OK"));
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdOK);
        jcmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/adrianromero/images/button_cancel.png")));
        jcmdCancel.setText(AppLocal.getIntString("Button.Cancel"));
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdCancel);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        jPanel1.setLayout(null);
        jPanel1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 5, 5, 5)));
        jLabel1.setText(AppLocal.getIntString("label.passwordold"));
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 20, 120, 14);
        jPanel1.add(jtxtPasswordOld);
        jtxtPasswordOld.setBounds(140, 20, 180, 20);
        jLabel2.setText(AppLocal.getIntString("label.passwordnew"));
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 50, 120, 14);
        jPanel1.add(jtxtPasswordNew);
        jtxtPasswordNew.setBounds(140, 50, 180, 20);
        jPanel1.add(jtxtPasswordRepeat);
        jtxtPasswordRepeat.setBounds(140, 80, 180, 20);
        jLabel3.setText(AppLocal.getIntString("label.passwordrepeat"));
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 80, 120, 14);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 416) / 2, (screenSize.height - 205) / 2, 416, 205);
    }

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {
        if (new String(jtxtPasswordNew.getPassword()).equals(new String(jtxtPasswordRepeat.getPassword()))) {
            if (Hashcypher.authenticate(new String(jtxtPasswordOld.getPassword()), m_sOldPassword)) {
                m_sNewPassword = Hashcypher.hashString(new String(jtxtPasswordNew.getPassword()));
                dispose();
            } else {
                JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.BadPassword")));
            }
        } else {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.changepassworddistinct")));
        }
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JButton jcmdCancel;

    private javax.swing.JButton jcmdOK;

    private javax.swing.JPasswordField jtxtPasswordNew;

    private javax.swing.JPasswordField jtxtPasswordOld;

    private javax.swing.JPasswordField jtxtPasswordRepeat;
}
