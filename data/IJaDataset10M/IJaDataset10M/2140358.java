package Client;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * ����� ��� ����� ������������ � �������
 * 
 * @author ������ �������
 */
public class ClientLogin extends javax.swing.JFrame {

    private static final long serialVersionUID = 3832899979189957937L;

    /**
     * �������� �����, ������������� �����������
     * 
     * @param port
     *            ����, �� �������� ����� ������������ � �������
     * @param host
     *            IP-����� �������
     */
    public ClientLogin(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        initComponents();
    }

    private void initComponents() {
        jPasswordField = new javax.swing.JPasswordField();
        jTextFieldJID = new javax.swing.JTextField();
        jCheckBoxCreateNew = new javax.swing.JCheckBox();
        jButtonLogin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("jLite");
        setAlwaysOnTop(true);
        setName("frameLogin");
        setResizable(false);
        jCheckBoxCreateNew.setText("Создать новый аккаунт");
        jCheckBoxCreateNew.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxCreateNew.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBoxCreateNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCreateNewActionPerformed(evt);
            }
        });
        jButtonLogin.setText("Войти");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });
        jLabel1.setText("Идентификатор пользователя");
        jLabel2.setText("Пароль");
        jLabel3.setText("Ник");
        jTextFieldName.setEnabled(false);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jTextFieldJID, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).add(jLabel3).add(jTextFieldName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).add(jCheckBoxCreateNew).add(layout.createSequentialGroup().add(44, 44, 44).add(jButtonLogin)).add(jPasswordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE).add(jLabel2)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextFieldJID, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextFieldName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPasswordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBoxCreateNew).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButtonLogin).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    /**
     * ����������, ����� ������������ ������ ��������� ����� � �������� ������
     * ������������
     * 
     * @param evt
     *            ��������� �������
     */
    private void jCheckBoxCreateNewActionPerformed(java.awt.event.ActionEvent evt) {
        jTextFieldName.setEnabled(jCheckBoxCreateNew.isSelected());
    }

    /**
     * ���������� ��� ������� �� ������ "OK"
     * 
     * @param evt
     *            ��������� �������
     */
    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {
        String jid = jTextFieldJID.getText();
        String pass = new String(jPasswordField.getPassword());
        String name = jTextFieldName.getText();
        new Client(jid, name, pass, jCheckBoxCreateNew.isSelected(), host, port).setVisible(true);
        this.setVisible(false);
    }

    /**
     * ����� ����� � ���������
     * 
     * @param args
     *            ��������� ��������� ������ (���� � ����)
     */
    public static void main(String args[]) {
        new ClientLogin(args[0], Integer.parseInt(args[1])).setVisible(true);
    }

    /**
     * ������ "��"
     */
    private javax.swing.JButton jButtonLogin;

    /**
     * ������ ��� �������� ������ ������������
     */
    private javax.swing.JCheckBox jCheckBoxCreateNew;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    /**
     * ���� ��� ����� ������
     */
    private javax.swing.JPasswordField jPasswordField;

    /**
     * ���� ��� ����� ��������������
     */
    private javax.swing.JTextField jTextFieldJID;

    /**
     * ���� ��� ����� �����
     */
    private javax.swing.JTextField jTextFieldName;

    /**
     * IP-����� �������
     */
    private String host;

    /**
     * ����, �� �������� ����� ������������ � �������
     */
    private int port;
}
