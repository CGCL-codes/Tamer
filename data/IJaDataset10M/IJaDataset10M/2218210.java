package jmagazzino;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import globali.jcFunzioni;
import globali.jcPostgreSQL;
import globali.jcVariabili;
import javax.swing.JFrame;

/**
 *
 * @author  sasch
 */
public class jfLogin extends javax.swing.JFrame {

    /** Creates new form jfLogin */
    public jfLogin() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jpfPassword = new javax.swing.JPasswordField();
        jbLogin = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jbInfoConnessione = new javax.swing.JLabel();
        jbSmartCard = new javax.swing.JButton();
        jlVersione = new javax.swing.JLabel();
        jmbPrincipale = new javax.swing.JMenuBar();
        jmPrincipale = new javax.swing.JMenu();
        jmiChiudi = new javax.swing.JMenuItem();
        jmImpServer = new javax.swing.JMenu();
        jmiCambiaNomeDB = new javax.swing.JMenuItem();
        jmiCambiaPortaDB = new javax.swing.JMenuItem();
        jmiCambiaIP = new javax.swing.JMenuItem();
        jcbmiProtocolloSSL = new javax.swing.JCheckBoxMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jmiInstallaDatabase = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jmiInformazioniSistema = new javax.swing.JMenuItem();
        jmiInfojMagazzino = new javax.swing.JMenuItem();
        jmiAttivaDebug = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("jMagazzino - LOGIN");
        setBackground(new java.awt.Color(204, 255, 204));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nome Utente");
        jtUser.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                jtUserFocusGained(evt);
            }
        });
        jtUser.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtUserKeyPressed(evt);
            }
        });
        jLabel2.setText("Password");
        jpfPassword.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                jpfPasswordFocusGained(evt);
            }
        });
        jpfPassword.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jpfPasswordKeyPressed(evt);
            }
        });
        jbLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/procedi.png")));
        jbLogin.setText("Accedi al sistema");
        jbLogin.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbLoginMouseClicked(evt);
            }
        });
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/logo_jMagazzino.jpg")));
        jbInfoConnessione.setFont(new java.awt.Font("Lucida Grande", 0, 10));
        jbInfoConnessione.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jbInfoConnessione.setText("jLabel5");
        jbSmartCard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/fedelity_card_20.png")));
        jbSmartCard.setText("Accedi al sistema tramite smart card");
        jbSmartCard.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbSmartCardMousePressed(evt);
            }
        });
        jlVersione.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlVersione.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlVersione.setText("Versione 5.x rilasciata sotto licenza GPL V3");
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel2).addContainerGap(482, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(44, 44, 44).add(jtUser, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE).addContainerGap()).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel1).addContainerGap(465, Short.MAX_VALUE)).add(jbInfoConnessione, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE).add(jPanel1Layout.createSequentialGroup().add(44, 44, 44).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(jbLogin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE).add(18, 18, 18).add(jbSmartCard)).add(jpfPassword, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)).addContainerGap()).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE).addContainerGap()).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jlVersione, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 227, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jlVersione, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jpfPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(12, 12, 12).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jbSmartCard).add(jbLogin)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jbInfoConnessione)));
        jmPrincipale.setText("Principale");
        jmiChiudi.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        jmiChiudi.setText("Chiudi programma");
        jmiChiudi.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiChiudiMousePressed(evt);
            }
        });
        jmPrincipale.add(jmiChiudi);
        jmbPrincipale.add(jmPrincipale);
        jmImpServer.setText("Impostazione Server");
        jmiCambiaNomeDB.setText("Cambia nome database");
        jmiCambiaNomeDB.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiCambiaNomeDBMousePressed(evt);
            }
        });
        jmImpServer.add(jmiCambiaNomeDB);
        jmiCambiaPortaDB.setText("Cambia porta database");
        jmiCambiaPortaDB.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiCambiaPortaDBMousePressed(evt);
            }
        });
        jmImpServer.add(jmiCambiaPortaDB);
        jmiCambiaIP.setText("Cambia indirizzo IP");
        jmiCambiaIP.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiCambiaIPMousePressed(evt);
            }
        });
        jmImpServer.add(jmiCambiaIP);
        jcbmiProtocolloSSL.setText("Attiva protocollo SSL");
        jcbmiProtocolloSSL.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbmiProtocolloSSLActionPerformed(evt);
            }
        });
        jmImpServer.add(jcbmiProtocolloSSL);
        jmbPrincipale.add(jmImpServer);
        jMenu2.setText("Database");
        jmiInstallaDatabase.setText("Installa database");
        jmiInstallaDatabase.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiInstallaDatabaseMousePressed(evt);
            }
        });
        jMenu2.add(jmiInstallaDatabase);
        jmbPrincipale.add(jMenu2);
        jMenu1.setText("?");
        jmiInformazioniSistema.setText("Ottieni informazione sistema");
        jmiInformazioniSistema.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiInformazioniSistemaMousePressed(evt);
            }
        });
        jMenu1.add(jmiInformazioniSistema);
        jmiInfojMagazzino.setText("Informazioni su jMagazzino");
        jmiInfojMagazzino.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiInfojMagazzinoMousePressed(evt);
            }
        });
        jMenu1.add(jmiInfojMagazzino);
        jmiAttivaDebug.setText("Attiva modalità DEBUG");
        jmiAttivaDebug.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jmiAttivaDebugMousePressed(evt);
            }
        });
        jMenu1.add(jmiAttivaDebug);
        jmbPrincipale.add(jMenu1);
        setJMenuBar(jmbPrincipale);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void connettiAlDB() {
        boolean err = false;
        if (jtUser.getText().length() <= 0) {
            JOptionPane.showMessageDialog(null, "Devi obbligatoriamente inserire un nome utente !!");
            err = true;
        }
        if (jcFunzioni.convertiPasswordJava(jpfPassword.getPassword()).length() <= 0 && !err) {
            JOptionPane.showMessageDialog(null, "Devi obbligatoriamente inserire una password !!");
            err = true;
        }
        if (!err) {
            try {
                jcPostgreSQL.USER = jtUser.getText().trim();
                jcPostgreSQL.PASS = jcFunzioni.convertiPasswordJava(jpfPassword.getPassword());
                jcPostgreSQL.connetti();
                if (!jcPostgreSQL.myDb.isClosed()) {
                    jcPostgreSQL.queryDB("SELECT adminid, nome, cognome, centralino_protocollo, centralino_estensione FROM administrators WHERE " + " username='" + jtUser.getText().trim() + "' AND " + " password= md5('" + jcFunzioni.convertiPasswordJava(jpfPassword.getPassword()) + "')");
                    if (jcPostgreSQL.contaRighe() == 1) {
                        jcPostgreSQL.adminID = jcPostgreSQL.query.getInt("adminid");
                        jcVariabili.ASTERISK_PROTOCOLLO = jcPostgreSQL.convertiStringaNulla("centralino_protocollo");
                        jcVariabili.ASTERISK_INTERNO = jcPostgreSQL.convertiStringaNulla("centralino_estensione");
                        jcVariabili.ASTERISK_SOURCE = jcVariabili.ASTERISK_PROTOCOLLO + "/" + jcVariabili.ASTERISK_INTERNO;
                        jfLoading mm = new jfLoading();
                        mm.jlNomeAdmin.setText(jcPostgreSQL.query.getString("nome").trim() + " " + jcPostgreSQL.query.getString("cognome").trim());
                        mm.setVisible(true);
                        this.setVisible(false);
                    } else JOptionPane.showMessageDialog(null, jtUser.getText().trim() + " NON risulti amministratore di questo Database !!");
                }
            } catch (SQLException ex) {
                Logger.getLogger(jfLogin.class.getName()).log(Level.SEVERE, null, ex);
                jcFunzioni.erroreSQL(ex.toString());
            }
        }
    }

    private void jbLoginMouseClicked(java.awt.event.MouseEvent evt) {
        connettiAlDB();
    }

    private void jmiCambiaNomeDBMousePressed(java.awt.event.MouseEvent evt) {
        String db = JOptionPane.showInputDialog("Inserisci il nuovo nome del serer su cui connettersi: ");
        if (db.length() > 0) {
            jcPostgreSQL.DB = db;
        } else JOptionPane.showMessageDialog(null, "NON puoi lasciare il nome del server bianco !!");
        aggiornaInformazioni();
    }

    private void jmiChiudiMousePressed(java.awt.event.MouseEvent evt) {
        System.exit(-1);
    }

    private void jmiCambiaIPMousePressed(java.awt.event.MouseEvent evt) {
        String ip = JOptionPane.showInputDialog("Inserisci il nuovo nome del serer su cui connettersi: ");
        if (ip.length() > 0) {
            jcPostgreSQL.HOST = ip;
        } else JOptionPane.showMessageDialog(null, "NON puoi lasciare il nome del server bianco !!");
        aggiornaInformazioni();
    }

    private void jpfPasswordKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            connettiAlDB();
        }
    }

    private void jtUserKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            connettiAlDB();
        }
    }

    private void aggiornaInformazioni() {
        jbInfoConnessione.setText("Connessione a " + jcPostgreSQL.HOST + ":" + jcPostgreSQL.PORT + " - SSL " + (jcPostgreSQL.SSL ? "on" : "off") + " - Database " + jcPostgreSQL.DB + " - java " + System.getProperty("java.version"));
        jcbmiProtocolloSSL.setSelected(jcPostgreSQL.SSL);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        jlVersione.setText("Versione " + jcVariabili.VERSIONE + " rilasciata sotto licenza GPL V3");
        aggiornaInformazioni();
    }

    private void jmiInformazioniSistemaMousePressed(java.awt.event.MouseEvent evt) {
        JFrame frame = new JFrame("Informazione sistema");
        jdInfoSistema mm = new jdInfoSistema(frame, true);
        mm.setVisible(true);
    }

    private void jmiInfojMagazzinoMousePressed(java.awt.event.MouseEvent evt) {
        jfSviluppatori mm = new jfSviluppatori();
        mm.setVisible(true);
    }

    private void jbSmartCardMousePressed(java.awt.event.MouseEvent evt) {
        globali.smartcard.jcSmartCardFunzioni.accediAllaSchedaPrivata();
        if (jcVariabili.CARD.memoryPrivataOK) {
            jtUser.setText(jcVariabili.CARD.leggiDatiScheda(0x02, 0x00, 0x1E).replace(jcVariabili.CARD.CARATTERE_RIEMPIMENTO, ""));
            jpfPassword.setText(jcVariabili.CARD.leggiDatiScheda(0x02, 0x1E, 0x1E).replace(jcVariabili.CARD.CARATTERE_RIEMPIMENTO, ""));
            connettiAlDB();
        }
    }

    private void jtUserFocusGained(java.awt.event.FocusEvent evt) {
        jtUser.selectAll();
    }

    private void jpfPasswordFocusGained(java.awt.event.FocusEvent evt) {
        jpfPassword.selectAll();
    }

    private void jmiInstallaDatabaseMousePressed(java.awt.event.MouseEvent evt) {
        jfInstallazione mm = new jfInstallazione();
        mm.setVisible(true);
    }

    private void jmiCambiaPortaDBMousePressed(java.awt.event.MouseEvent evt) {
        String porta = JOptionPane.showInputDialog("Inserisci la nuova porta del serer su cui connettersi: ");
        if (jcFunzioni.soloNumeri(porta)) {
            jcPostgreSQL.PORT = porta;
        } else JOptionPane.showMessageDialog(null, "La porta deve solo numeri !", "Errore", JOptionPane.ERROR_MESSAGE);
        aggiornaInformazioni();
    }

    private void jmiAttivaDebugMousePressed(java.awt.event.MouseEvent evt) {
        jcVariabili.DEBUG = true;
    }

    private void jcbmiProtocolloSSLActionPerformed(java.awt.event.ActionEvent evt) {
        jcPostgreSQL.SSL = jcbmiProtocolloSSL.isSelected();
        aggiornaInformazioni();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new jfLogin().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel jbInfoConnessione;

    private javax.swing.JButton jbLogin;

    private javax.swing.JButton jbSmartCard;

    private javax.swing.JCheckBoxMenuItem jcbmiProtocolloSSL;

    private javax.swing.JLabel jlVersione;

    private javax.swing.JMenu jmImpServer;

    private javax.swing.JMenu jmPrincipale;

    private javax.swing.JMenuBar jmbPrincipale;

    private javax.swing.JMenuItem jmiAttivaDebug;

    private javax.swing.JMenuItem jmiCambiaIP;

    private javax.swing.JMenuItem jmiCambiaNomeDB;

    private javax.swing.JMenuItem jmiCambiaPortaDB;

    private javax.swing.JMenuItem jmiChiudi;

    private javax.swing.JMenuItem jmiInfojMagazzino;

    private javax.swing.JMenuItem jmiInformazioniSistema;

    private javax.swing.JMenuItem jmiInstallaDatabase;

    private javax.swing.JPasswordField jpfPassword;

    private javax.swing.JTextField jtUser;
}
