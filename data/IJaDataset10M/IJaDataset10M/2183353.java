package es.atareao.queensboro.gui;

import java.awt.Frame;
import java.io.File;
import javax.swing.JFileChooser;
import es.atareao.queensboro.db.ConectorH2;
import es.atareao.alejandria.gui.ErrorDialog;

/**
 *
 * @author  Protactino
 */
public class ConnectDialogH2 extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;

    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    /** Creates new form MarcoDocumento */
    public ConnectDialogH2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setSize(460, 330);
        this.setLocationRelativeTo(null);
    }

    public ConnectDialogH2() {
        this(new Frame(), true);
    }

    private void initComponents() {
        jAceptar = new javax.swing.JButton();
        jCancelar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jModo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jCifrado = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jDatabase = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jServidor = new javax.swing.JTextField();
        jPuerto = new javax.swing.JTextField();
        jUsuario = new javax.swing.JTextField();
        jPassword = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Conexión");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/protactino/queensboro/img/button_ok.png")));
        jAceptar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAceptarActionPerformed(evt);
            }
        });
        getContentPane().add(jAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 260, 40, 40));
        jCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/protactino/queensboro/img/button_cancel.png")));
        jCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelarActionPerformed(evt);
            }
        });
        getContentPane().add(jCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, 40, 40));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jLabel1.setText("Modo:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 60, 20));
        jModo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FILE", "MEMORY", "TCP", "SSL" }));
        jModo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jModoItemStateChanged(evt);
            }
        });
        jPanel1.add(jModo, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 200, 20));
        jLabel2.setText("Cifrado:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));
        jCifrado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NO", "AES", "XTEA" }));
        jPanel1.add(jCifrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 200, 20));
        jLabel3.setText("Database:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 20));
        jDatabase.setText("C:\\java\\src\\Tizona\\dbs\\faxescartas");
        jPanel1.add(jDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 80, 170, -1));
        jLabel4.setText("Servidor:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, 20));
        jLabel5.setText("Puerto:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 20));
        jLabel6.setText("Usuario:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, 20));
        jLabel7.setText("Password:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, 20));
        jServidor.setEnabled(false);
        jPanel1.add(jServidor, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 200, -1));
        jPuerto.setEnabled(false);
        jPanel1.add(jPuerto, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 200, -1));
        jUsuario.setText("lcarbone");
        jPanel1.add(jUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 200, -1));
        jPanel1.add(jPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 200, 200, 20));
        jButton1.setText("..");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {

            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }

            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                jButton1AncestorResized(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 30, 20));
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 310, 240));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/protactino/queensboro/img/password.png")));
        jLabel8.setText("jLabel1");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, 160));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jFileChooser = new JFileChooser();
        int returnVal = jFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
            String database = file.getPath();
            if (database.indexOf(".") > 0) {
                database = database.substring(0, database.indexOf("."));
            }
            try {
                jDatabase.setText(database);
            } catch (Exception ex) {
                ErrorDialog.manejaError(ex, false);
            }
        }
    }

    private void jButton1AncestorResized(java.awt.event.HierarchyEvent evt) {
    }

    private void jModoItemStateChanged(java.awt.event.ItemEvent evt) {
        switch(jModo.getSelectedIndex()) {
            case 0:
                jServidor.setEnabled(false);
                jPuerto.setEnabled(false);
                break;
            case 1:
                jServidor.setEnabled(false);
                jPuerto.setEnabled(false);
                break;
            case 2:
                jServidor.setEnabled(true);
                jPuerto.setEnabled(true);
                break;
            case 3:
                jServidor.setEnabled(true);
                jPuerto.setEnabled(true);
                break;
        }
    }

    private void jCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.setConector(null);
        this.setReturnStatus(RET_CANCEL);
        this.dispose();
    }

    private void jAceptarActionPerformed(java.awt.event.ActionEvent evt) {
        ConectorH2 conector;
        String user = jUsuario.getText();
        char[] password = jPassword.getPassword();
        String database = jDatabase.getText();
        String servidor = jServidor.getText();
        String puerto = jPuerto.getText();
        switch(jModo.getSelectedIndex()) {
            case 0:
                switch(jCifrado.getSelectedIndex()) {
                    case 0:
                        conector = new ConectorH2(user, password, database, false, ConectorH2.CIPHER_NO, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 1:
                        conector = new ConectorH2(user, password, database, false, ConectorH2.CIPHER_AES, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 2:
                        conector = new ConectorH2(user, password, database, false, ConectorH2.CIPHER_XTEA, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                }
                break;
            case 1:
                switch(jCifrado.getSelectedIndex()) {
                    case 0:
                        conector = new ConectorH2(user, password, database, true, ConectorH2.CIPHER_NO, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 1:
                        conector = new ConectorH2(user, password, database, true, ConectorH2.CIPHER_AES, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 2:
                        conector = new ConectorH2(user, password, database, true, ConectorH2.CIPHER_XTEA, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                }
                break;
            case 2:
                switch(jCifrado.getSelectedIndex()) {
                    case 0:
                        conector = new ConectorH2(user, password, database, servidor, puerto, false, ConectorH2.CIPHER_NO, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 1:
                        conector = new ConectorH2(user, password, database, servidor, puerto, false, ConectorH2.CIPHER_AES, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 2:
                        conector = new ConectorH2(user, password, database, servidor, puerto, false, ConectorH2.CIPHER_XTEA, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                }
                break;
            case 3:
                switch(jCifrado.getSelectedIndex()) {
                    case 0:
                        conector = new ConectorH2(user, password, database, servidor, puerto, true, ConectorH2.CIPHER_NO, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 1:
                        conector = new ConectorH2(user, password, database, servidor, puerto, true, ConectorH2.CIPHER_AES, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                    case 2:
                        conector = new ConectorH2(user, password, database, servidor, puerto, true, ConectorH2.CIPHER_XTEA, ConectorH2.FILE_LOCK_FILE, ConectorH2.CREATE_YES);
                        this.setConector(conector);
                        break;
                }
                break;
        }
        this.setReturnStatus(RET_OK);
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ConnectDialogH2(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JButton jAceptar;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jCancelar;

    private javax.swing.JComboBox jCifrado;

    private javax.swing.JTextField jDatabase;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JComboBox jModo;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPasswordField jPassword;

    private javax.swing.JTextField jPuerto;

    private javax.swing.JTextField jServidor;

    private javax.swing.JTextField jUsuario;

    private int _returnStatus = RET_CANCEL;

    private ConectorH2 _conector;

    public ConectorH2 getConector() {
        return _conector;
    }

    public void setConector(ConectorH2 conector) {
        this._conector = conector;
    }

    public int getReturnStatus() {
        return _returnStatus;
    }

    public void setReturnStatus(int returnStatus) {
        this._returnStatus = returnStatus;
    }
}
