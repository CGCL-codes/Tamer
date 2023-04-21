package servizi;

import globali.jcFunzioni;
import globali.jcPostgreSQL;
import globali.jcVariabili;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author papini.sascha
 */
public class jdServizioAggiuntivo extends javax.swing.JDialog {

    public int servAggID = -1, pollID = -1, ipID = -1;

    private int listinoID = -1, riferimentoCliID = -1, servID = -1;

    private boolean passCambiata = false;

    private void aggiornaImmagineStati() {
        if (jcbStatoServizio.getSelectedIndex() >= 0) {
            Object icona[] = (Object[]) jcbStatoServizio.getSelectedItem();
            jlSemaforo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/" + icona[2])));
        }
    }

    private void passwordLetturaErrata() {
        jbOk.setEnabled(false);
        jbOkApparati.setEnabled(false);
        jtaCodici.setEnabled(false);
        jtfPassword.setEnabled(false);
        JOptionPane.showMessageDialog(this, "Password non corretta ! Servizio limitato alla visualizzazione e basta !", "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void caricaDati() {
        jcPostgreSQL.queryDBRo("SELECT s.*, l.nome AS tiposervizio, l.listinoid, sai.ip, sai.servizi_aggiuntivi_ipid FROM servizi_aggiuntivi AS s " + "INNER JOIN listino AS l ON (s.listinoid=l.listinoid) " + "LEFT JOIN servizi_aggiuntivi_ip AS sai ON (s.servizi_aggiuntiviid=sai.servizi_aggiuntiviid) " + "WHERE s.servizi_aggiuntiviid=" + servAggID);
        try {
            if (jcPostgreSQL.query.getObject("prezzo") != null) {
                jcPrezzoSpeciale.setSelected(true);
                jftPrezzo.setValue(jcPostgreSQL.query.getDouble("prezzo"));
                jftIVA.setValue(jcPostgreSQL.query.getDouble("iva"));
                jftPrezzoAttivazione.setValue(jcPostgreSQL.query.getDouble("prezzo_attivazione"));
                jftIVAAttivazione.setValue(jcPostgreSQL.query.getDouble("iva_attivazione"));
            }
            this.setTitle("jMagazzino - Servizio Aggiuntivo ID " + servAggID + " - Riferimento servizio ID " + jcPostgreSQL.query.getInt("serviziid"));
            servID = jcPostgreSQL.query.getInt("serviziid");
            jcbEncryption.setSelected(jcPostgreSQL.query.getBoolean("encryption"));
            jcbComodato.setSelected(jcPostgreSQL.query.getBoolean("comodato"));
            jtNomeServizioAgg.setText(jcPostgreSQL.query.getString("tiposervizio"));
            jtNome.setText(jcFunzioni.formattaStringaNulla(jcPostgreSQL.query.getString("nome")));
            jtUsername.setText(jcFunzioni.formattaStringaNulla(jcPostgreSQL.query.getString("username")));
            listinoID = jcPostgreSQL.query.getInt("listinoid");
            jcbTipoAttivazione.setSelectedIndex(jcPostgreSQL.query.getInt("tipo_attivazione"));
            jcbFatturaServizio.setSelected(jcPostgreSQL.query.getBoolean("in_fatturazione"));
            jftDataCreazione.setValue(jcFunzioni.formattaData(jcPostgreSQL.query.getDate("data_attivazione")));
            jftDataCessazione.setValue(jcPostgreSQL.query.getObject("data_cessazione") == null ? null : jcFunzioni.formattaData(jcPostgreSQL.query.getDate("data_cessazione")));
            jftDataScadenza.setValue(jcPostgreSQL.query.getObject("data_scadenza") == null ? null : jcFunzioni.formattaData(jcPostgreSQL.query.getDate("data_scadenza")));
            jtaNote.setText(jcPostgreSQL.query.getString("note"));
            if (jcPostgreSQL.query.getBoolean("encryption") && (jcPostgreSQL.query.getObject("codici") != null || jcPostgreSQL.query.getObject("password") != null)) {
                jcbEncryption.setSelected(true);
                JFrame frame = new JFrame("Decrypt dati");
                jdPassword mm = new jdPassword(frame, true);
                mm.setVisible(true);
                if (mm.pass.length() > 0) {
                    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                    textEncryptor.setPassword(mm.pass);
                    try {
                        if (jcPostgreSQL.query.getObject("codici") != null) jtaCodici.setText(textEncryptor.decrypt(jcPostgreSQL.query.getString("codici").trim()));
                        if (jcPostgreSQL.query.getObject("password") != null) jtfPassword.setText(textEncryptor.decrypt(jcPostgreSQL.query.getString("password").trim()));
                    } catch (Exception ex) {
                        passwordLetturaErrata();
                        if (jcVariabili.DEBUG) JOptionPane.showMessageDialog(this, "Errore:" + ex.toString(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    passwordLetturaErrata();
                }
            } else {
                jcbEncryption.setSelected(false);
                jtaCodici.setText(jcPostgreSQL.convertiStringaNulla("codici"));
                jtfPassword.setText(jcPostgreSQL.convertiStringaNulla("password"));
            }
            int servID = jcPostgreSQL.query.getInt("servizi_serverid");
            riferimentoCliID = jcPostgreSQL.query.getInt("riferimentoclienteid");
            if (jcPostgreSQL.query.getObject("ip_dinamico") == null) {
                jrbNessunIP.setSelected(true);
            } else if (jcPostgreSQL.query.getBoolean("ip_dinamico")) {
                jrbIpDinamico.setSelected(true);
                pollID = jcPostgreSQL.query.getInt("servizi_aggiuntivi_poolid");
            } else {
                jrbIpStatico.setSelected(true);
                ipID = jcPostgreSQL.query.getObject("ip") == null ? -1 : jcPostgreSQL.query.getInt("servizi_aggiuntivi_ipid");
                jtIP.setText(jcPostgreSQL.query.getObject("ip") == null ? "" : jcPostgreSQL.query.getObject("ip").toString());
            }
            jtIPAgg.setText(jcFunzioni.formattaStringaNulla(jcPostgreSQL.query.getString("ipagg")));
            impostaTastoIP();
            globali.jcFunzioniServizi.caricaServiziStato(jcPostgreSQL.query.getInt("servizi_statoid"), jcbStatoServizio, -1);
            globali.jcFunzioniServizi.caricaServiziServer(servID, jcbServer);
            jtCliente.setText(riferimentoCliID == -1 ? "" : jcPostgreSQL.ottieniNomeCliente(riferimentoCliID));
            globali.jcFunzioniServizi.caricaServiziIpPool(pollID, jcbIpPool);
            passCambiata = false;
        } catch (SQLException ex) {
            Logger.getLogger(jdServizioAggiuntivo.class.getName()).log(Level.SEVERE, null, ex);
            jcFunzioni.erroreSQL(ex.toString());
        }
    }

    private void impostaTastoIP() {
        if (ipID == -1) {
            jbAssegnaAutomaticamente.setText("Assegna automaticamente");
            jbAssegnaAutomaticamente.setBackground(Color.GREEN);
        } else {
            jbAssegnaAutomaticamente.setText("Libera IP");
            jbAssegnaAutomaticamente.setBackground(Color.PINK);
        }
    }

    private boolean erroriCampiObbligatori() {
        String err = "";
        if (jcbStatoServizio.getSelectedIndex() < 0) {
            err += "Devi selzionare uno stato servizio !";
            jcbStatoServizio.setBackground(Color.PINK);
        } else jcbStatoServizio.setBackground(Color.WHITE);
        if (listinoID == -1) {
            jtNomeServizioAgg.setBackground(Color.PINK);
            err += "Devi obbligatoriamente scegliere un servizio aggiuntivo !\n";
        } else jtNomeServizioAgg.setBackground(Color.WHITE);
        if (!jcFunzioni.controllaInserimentoData(jftDataCreazione.getText())) {
            jftDataCreazione.setBackground(Color.PINK);
            err += "Devi obbligatoriamente immettere una data creazione !\n";
        } else jftDataCreazione.setBackground(Color.WHITE);
        if (jcPrezzoSpeciale.isSelected()) {
            if (jftPrezzo.getValue() == null) {
                jftPrezzo.setBackground(Color.PINK);
                err += "Devi obbligatoriamente immettere un prezzo speciale !\n";
            } else jftPrezzo.setBackground(Color.WHITE);
            if (jftIVA.getValue() == null) {
                jftIVA.setBackground(Color.PINK);
                err += "Devi obbligatoriamente immettere un'aliquota IVA !\n";
            } else jftIVA.setBackground(Color.WHITE);
            if (jftPrezzoAttivazione.getValue() == null) {
                jftPrezzoAttivazione.setBackground(Color.PINK);
                err += "Devi obbligatoriamente immettere un prezzo speciale !\n";
            } else jftPrezzoAttivazione.setBackground(Color.WHITE);
            if (jftIVAAttivazione.getValue() == null) {
                jftIVAAttivazione.setBackground(Color.PINK);
                err += "Devi obbligatoriamente immettere un'aliquota IVA !\n";
            } else jftIVAAttivazione.setBackground(Color.WHITE);
        }
        if (jrbIpDinamico.isSelected()) {
            if (pollID == -1) {
                jcbIpPool.setBackground(Color.PINK);
                err += "Devi obbligatoriamente immettere un pool di IP valido !\n";
            } else jcbIpPool.setBackground(Color.WHITE);
        }
        if (jrbIpStatico.isSelected()) {
            if (ipID == -1) {
                jtIP.setBackground(Color.PINK);
                err += "Devi obbligatoriamente immettere un indirizzo IP valido !\n";
            } else jtIP.setBackground(Color.WHITE);
        }
        if (jtUsername.getText().length() > 0) {
            jcPostgreSQL.queryDB("SELECT servizi_aggiuntiviid FROM servizi_aggiuntivi WHERE username='" + jcPostgreSQL.togliApice(jtUsername.getText()) + "'" + (servAggID == -1 ? "" : " AND servizi_aggiuntiviid!=" + servAggID));
            if (jcPostgreSQL.contaRighe() > 0) {
                jtUsername.setBackground(Color.PINK);
                err += "Username già presente nel database !\n";
            } else {
                jtUsername.setBackground(Color.WHITE);
            }
        }
        if (err.length() != 0) {
            JOptionPane.showMessageDialog(null, "Sono stati riscontrati i seguenti errori:\n" + err, "Errore", JOptionPane.ERROR_MESSAGE);
            return true;
        } else return false;
    }

    private void salvaDati(boolean aggiornaSuApparati) {
        String codiciAggiuntivi = "", password = "";
        String nome = jtNome.getText().length() == 0 ? "NULL" : "'" + jcPostgreSQL.togliApice(jtNome.getText()) + "'";
        String username = jtUsername.getText().length() > 0 ? "'" + jcPostgreSQL.togliApice(jtUsername.getText().trim()) + "'" : "NULL";
        String prezzo = jcPrezzoSpeciale.isSelected() ? jftPrezzo.getValue().toString() : "NULL";
        String iva = jcPrezzoSpeciale.isSelected() ? jftIVA.getValue().toString() : "NULL";
        String prezzoAttivazione = jcPrezzoSpeciale.isSelected() ? jftPrezzoAttivazione.getValue().toString() : "NULL";
        String ivaAttivazione = jcPrezzoSpeciale.isSelected() ? jftIVAAttivazione.getValue().toString() : "NULL";
        String ipagg = jtIPAgg.getText().length() > 0 ? "'" + jcPostgreSQL.togliApice(jtIPAgg.getText().trim()) + "'" : "NULL";
        Object stati[] = (Object[]) jcbStatoServizio.getSelectedItem();
        String dataScadenza = jftDataScadenza.getValue() == null ? "NULL" : "'" + jcPostgreSQL.convertiData(jftDataScadenza.getValue().toString()) + "'";
        if (jcbEncryption.isSelected() && (jtfPassword.getText().length() > 0 || jtaCodici.getText().length() > 0)) {
            JFrame frame = new JFrame("Encrypt dati");
            jdPassword mm = new jdPassword(frame, true);
            mm.confermaPassword = true;
            mm.setVisible(true);
            if (mm.pass.length() > 0) {
                BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
                textEncryptor.setPassword(mm.pass);
                codiciAggiuntivi = "'" + jcPostgreSQL.togliApice(textEncryptor.encrypt(jtaCodici.getText().trim())) + "'";
                password = "'" + jcPostgreSQL.togliApice(textEncryptor.encrypt(jtfPassword.getText().trim())) + "'";
            } else {
                JOptionPane.showMessageDialog(this, "Le password non coincidono o è una password vuota ! I dati saranno salvati senza encrypt dati !", "Errore", JOptionPane.ERROR_MESSAGE);
                codiciAggiuntivi = jtaCodici.getText().length() > 0 ? "'" + jcPostgreSQL.togliApice(jtaCodici.getText()) + "'" : "NULL";
                password = jtfPassword.getText().length() > 0 ? "'" + jcPostgreSQL.togliApice(jtfPassword.getText()) + "'" : "NULL";
                jcbEncryption.setSelected(false);
            }
        } else if (!jcbEncryption.isSelected() && (jtfPassword.getText().length() > 0 || jtaCodici.getText().length() > 0)) {
            codiciAggiuntivi = jtaCodici.getText().length() > 0 ? "'" + jcPostgreSQL.togliApice(jtaCodici.getText()) + "'" : "NULL";
            password = jtfPassword.getText().length() > 0 ? "'" + jcPostgreSQL.togliApice(jtfPassword.getText()) + "'" : "NULL";
        } else {
            codiciAggiuntivi = "NULL";
            password = "NULL";
        }
        try {
            Object serverID[] = (Object[]) jcbServer.getSelectedItem();
            String note = jtaNote.getText().length() == 0 ? "NULL" : "'" + jcPostgreSQL.togliApice(jtaNote.getText()) + "'";
            String ipdinamico = jrbNessunIP.isSelected() ? "NULL" : Boolean.toString(jrbIpDinamico.isSelected());
            jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi SET " + "listinoid=" + listinoID + ", " + "nome=" + nome + ", " + "prezzo=" + prezzo + ", " + "iva=" + iva + ", " + "prezzo_attivazione=" + prezzoAttivazione + ", " + "iva_attivazione=" + ivaAttivazione + ", " + "data_attivazione='" + jcPostgreSQL.convertiData(jftDataCreazione.getText()) + "', " + "servizi_statoid=" + stati[0] + ", " + "codici=" + codiciAggiuntivi + ", " + "username=" + username + ", " + "password=" + password + ", " + "encryption=" + jcbEncryption.isSelected() + ", " + "data_scadenza=" + dataScadenza + ", " + "servizi_serverid=" + serverID[0] + ", " + "riferimentoclienteid=" + riferimentoCliID + ", " + "tempo_agg_last=NOW(), " + (aggiornaSuApparati ? "" : "tempo_agg_server=NULL, ") + "ipagg=" + ipagg + ", " + "note=" + note + ", " + "servizi_aggiuntivi_poolid=" + pollID + ", " + "ip_dinamico=" + ipdinamico + ", " + "comodato=" + jcbComodato.isSelected() + ", " + "tipo_attivazione=" + jcbTipoAttivazione.getSelectedIndex() + ", " + "in_fatturazione=" + jcbFatturaServizio.isSelected() + " WHERE servizi_aggiuntiviid=" + servAggID);
            if (jrbIpDinamico.isSelected() && ipID != -1) {
                jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi_ip SET servizi_aggiuntiviid=null WHERE servizi_aggiuntivi_ipid=" + ipID);
                jtIP.setText("");
                ipID = -1;
            }
            globali.jcFunzioniServizi.log(servID, "AGGIORNATO sotto-servizio\n" + " Aggiornato su apparato: " + (aggiornaSuApparati ? "SI" : "NO") + "\n" + " Tipo servizio: " + jcPostgreSQL.togliApice(jtNomeServizioAgg.getText()) + "\n" + " Stato: " + stati[1] + "\n" + " Data creazione: " + jftDataCreazione.getText() + "\n" + " Servizio in fatturazione: " + (jcbFatturaServizio.isSelected() ? "SI" : "NO") + "\n" + " Server configurato: " + serverID[0] + " - " + serverID[1] + "\n" + (jftDataScadenza.getValue() == null ? "" : " Data scadenza: " + jftDataScadenza.getText() + "\n") + " Password cambiata: " + (passCambiata ? "SI" : "NO") + "\n" + " Prezzo speciale impostato: " + (jcPrezzoSpeciale.isSelected() ? "SI con nuovo prezzo: " + prezzo + " e iva: " + iva + "\n prezzo attivazione: " + prezzoAttivazione + " e iva attivazione: " + ivaAttivazione : "NO") + "\n" + (jtNome.getText().length() == 0 ? "" : " Nome: " + jcPostgreSQL.togliApice(jtNome.getText()) + "\n") + (jtUsername.getText().length() == 0 ? "" : " Username: " + jcPostgreSQL.togliApice(jtUsername.getText()) + "\n") + (jtIP.getText().length() == 0 ? "" : " Ip: " + jcPostgreSQL.togliApice(jtIP.getText()) + "\n") + (jtIPAgg.getText().length() == 0 ? "" : " Ip aggiuntivi: " + jcPostgreSQL.togliApice(jtIPAgg.getText())));
            if (!aggiornaSuApparati) javax.swing.JOptionPane.showMessageDialog(null, "Servizio aggiornato !", "Informazione", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(jdServizioAggiuntivo.class.getName()).log(Level.SEVERE, null, ex);
            jcFunzioni.erroreSQL(ex.toString());
        }
    }

    /** Creates new form jdServizioAggiuntivo */
    public jdServizioAggiuntivo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        bgTipoIP = new javax.swing.ButtonGroup();
        jbOk = new javax.swing.JButton();
        jbAnnulla = new javax.swing.JButton();
        jbOkApparati = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpServizio = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtNomeServizioAgg = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtNome = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtCliente = new javax.swing.JTextField();
        jlApriSchedaCliente = new javax.swing.JLabel();
        jlRimuoviDaFatturare = new javax.swing.JLabel();
        jlSemaforo = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jcbStatoServizio = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jftDataCreazione = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jftDataScadenza = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jftDataCessazione = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtaNote = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jcPrezzoSpeciale = new javax.swing.JCheckBox();
        jlNuovoCosto = new javax.swing.JLabel();
        jftPrezzo = new javax.swing.JFormattedTextField();
        jlIVA = new javax.swing.JLabel();
        jftIVA = new javax.swing.JFormattedTextField();
        jlNuovoCostoAttivazione = new javax.swing.JLabel();
        jftPrezzoAttivazione = new javax.swing.JFormattedTextField();
        jlIVAAttivazione = new javax.swing.JLabel();
        jftIVAAttivazione = new javax.swing.JFormattedTextField();
        jcbFatturaServizio = new javax.swing.JCheckBox();
        jcbComodato = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jcbTipoAttivazione = new javax.swing.JComboBox();
        jpCodici = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jtUsername = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jtfPassword = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaCodici = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jcbServer = new javax.swing.JComboBox();
        jbGeneraAutomaticamente = new javax.swing.JButton();
        jcbEncryption = new javax.swing.JCheckBox();
        jbInterrogaPoolIP = new javax.swing.JButton();
        jpIndirizziIP = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtIP = new javax.swing.JTextField();
        jbWeb = new javax.swing.JButton();
        jbPing = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jtIPAgg = new javax.swing.JTextField();
        jrbIpDinamico = new javax.swing.JRadioButton();
        jcbIpPool = new javax.swing.JComboBox();
        jrbIpStatico = new javax.swing.JRadioButton();
        jbAssegnaAutomaticamente = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaRisuInterrogazioni = new javax.swing.JTextArea();
        jbNuovoIPv4 = new javax.swing.JButton();
        jrbNessunIP = new javax.swing.JRadioButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("jMagazzino - Servizio Aggiuntivo");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jbOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/salva_20.png")));
        jbOk.setText("Aggiungi servizio");
        jbOk.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbOkMousePressed(evt);
            }
        });
        jbAnnulla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/rimuovi_20.png")));
        jbAnnulla.setText("Chiudi senza apportare modifiche");
        jbAnnulla.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbAnnullaMousePressed(evt);
            }
        });
        jbOkApparati.setFont(new java.awt.Font("Tahoma", 1, 11));
        jbOkApparati.setForeground(new java.awt.Color(255, 0, 0));
        jbOkApparati.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/salva_20.png")));
        jbOkApparati.setText("Aggiungi servizio e configura sugli apparati");
        jbOkApparati.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbOkApparatiMousePressed(evt);
            }
        });
        jpServizio.setBackground(new java.awt.Color(204, 255, 204));
        jLabel1.setText("Tipo Servizio:");
        jtNomeServizioAgg.setEditable(false);
        jtNomeServizioAgg.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtNomeServizioAggMousePressed(evt);
            }
        });
        jLabel4.setText("Nome servizio:");
        jLabel11.setText("Riferimento:");
        jtCliente.setEditable(false);
        jtCliente.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtClienteMousePressed(evt);
            }
        });
        jlApriSchedaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/utente_20.png")));
        jlApriSchedaCliente.setToolTipText("Apri scheda cliente");
        jlApriSchedaCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlApriSchedaCliente.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jlApriSchedaClienteMousePressed(evt);
            }
        });
        jlRimuoviDaFatturare.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/rimuovi_20.png")));
        jlRimuoviDaFatturare.setToolTipText("Elimina riferimento");
        jlRimuoviDaFatturare.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jlRimuoviDaFatturare.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jlRimuoviDaFatturareMousePressed(evt);
            }
        });
        jlSemaforo.setBackground(new java.awt.Color(255, 255, 255));
        jlSemaforo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/semaforo_spento.gif")));
        jLabel14.setText("Stato Servizio Aggiuntivo");
        jLabel3.setText("Data Creazione:");
        try {
            jftDataCreazione.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jLabel9.setText("Data Scadenza:");
        try {
            jftDataScadenza.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jLabel5.setText("Data Cessazione:");
        jftDataCessazione.setEditable(false);
        try {
            jftDataCessazione.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jLabel15.setText("Note");
        jtaNote.setColumns(20);
        jtaNote.setRows(5);
        jScrollPane3.setViewportView(jtaNote);
        javax.swing.GroupLayout jpServizioLayout = new javax.swing.GroupLayout(jpServizio);
        jpServizio.setLayout(jpServizioLayout);
        jpServizioLayout.setHorizontalGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createSequentialGroup().addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createSequentialGroup().addContainerGap().addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createSequentialGroup().addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jtNome).addGroup(jpServizioLayout.createSequentialGroup().addComponent(jtCliente).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jlApriSchedaCliente).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jlRimuoviDaFatturare)).addComponent(jtNomeServizioAgg, javax.swing.GroupLayout.Alignment.TRAILING))).addGroup(jpServizioLayout.createSequentialGroup().addComponent(jlSemaforo).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createSequentialGroup().addComponent(jLabel14).addGap(326, 326, 326)).addComponent(jcbStatoServizio, 0, 853, Short.MAX_VALUE))).addGroup(jpServizioLayout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftDataCreazione, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftDataScadenza, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftDataCessazione, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)))).addGroup(jpServizioLayout.createSequentialGroup().addGap(20, 20, 20).addComponent(jScrollPane3)).addGroup(jpServizioLayout.createSequentialGroup().addContainerGap().addComponent(jLabel15).addGap(0, 0, Short.MAX_VALUE))).addContainerGap()));
        jpServizioLayout.setVerticalGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createSequentialGroup().addContainerGap().addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jtNomeServizioAgg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(jtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jlRimuoviDaFatturare).addComponent(jlApriSchedaCliente)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jftDataCreazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9).addComponent(jftDataScadenza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel5).addComponent(jftDataCessazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE).addGap(18, 18, 18).addGroup(jpServizioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpServizioLayout.createSequentialGroup().addComponent(jLabel14).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbStatoServizio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jlSemaforo)).addContainerGap()));
        jTabbedPane1.addTab("Servizio", jpServizio);
        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jcPrezzoSpeciale.setBackground(new java.awt.Color(204, 255, 204));
        jcPrezzoSpeciale.setText("Imposta un prezzo speciale pe ril servizio");
        jcPrezzoSpeciale.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcPrezzoSpecialeActionPerformed(evt);
            }
        });
        jlNuovoCosto.setText("Nuovo costo:");
        jlNuovoCosto.setEnabled(false);
        jftPrezzo.setEditable(false);
        jftPrezzo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        jftPrezzo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftPrezzo.setEnabled(false);
        jftPrezzo.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jftPrezzoMousePressed(evt);
            }
        });
        jlIVA.setText("IVA");
        jlIVA.setEnabled(false);
        jftIVA.setEditable(false);
        jftIVA.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftIVA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftIVA.setEnabled(false);
        jftIVA.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jftIVAMousePressed(evt);
            }
        });
        jlNuovoCostoAttivazione.setText("Nuovo costo di attivazione:");
        jlNuovoCostoAttivazione.setEnabled(false);
        jftPrezzoAttivazione.setEditable(false);
        jftPrezzoAttivazione.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        jftPrezzoAttivazione.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftPrezzoAttivazione.setEnabled(false);
        jftPrezzoAttivazione.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jftPrezzoAttivazioneMousePressed(evt);
            }
        });
        jlIVAAttivazione.setText("IVA");
        jlIVAAttivazione.setEnabled(false);
        jftIVAAttivazione.setEditable(false);
        jftIVAAttivazione.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jftIVAAttivazione.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftIVAAttivazione.setEnabled(false);
        jftIVAAttivazione.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jftIVAAttivazioneMousePressed(evt);
            }
        });
        jcbFatturaServizio.setBackground(new java.awt.Color(255, 255, 204));
        jcbFatturaServizio.setSelected(true);
        jcbFatturaServizio.setText("Abilita fatturazione servizio");
        jcbComodato.setBackground(new java.awt.Color(255, 255, 204));
        jcbComodato.setText("Oggetto in comodato d'uso");
        jLabel16.setText("Tipo di attivazione:");
        jcbTipoAttivazione.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nuova", "Portabilità", "Scambio" }));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jcPrezzoSpeciale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jlNuovoCosto).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftPrezzo).addGap(18, 18, 18).addComponent(jlIVA).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftIVA, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jlNuovoCostoAttivazione).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftPrezzoAttivazione, javax.swing.GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jlIVAAttivazione).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jftIVAAttivazione, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jcbFatturaServizio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jcbComodato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbTipoAttivazione, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jcPrezzoSpeciale).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlNuovoCosto).addComponent(jftPrezzo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jlIVA).addComponent(jftIVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jlNuovoCostoAttivazione).addComponent(jftPrezzoAttivazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jlIVAAttivazione).addComponent(jftIVAAttivazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jcbFatturaServizio).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jcbComodato).addGap(18, 18, 18).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel16).addComponent(jcbTipoAttivazione, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(293, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Fatturazione", jPanel1);
        jpCodici.setBackground(new java.awt.Color(204, 255, 204));
        jLabel7.setText("Username:");
        jLabel8.setText("Password:");
        jtfPassword.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtfPasswordMousePressed(evt);
            }
        });
        jLabel2.setText("Codici servizio aggiuntivo");
        jtaCodici.setColumns(20);
        jtaCodici.setRows(5);
        jScrollPane1.setViewportView(jtaCodici);
        jLabel10.setText("Server:");
        jbGeneraAutomaticamente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/run_20.png")));
        jbGeneraAutomaticamente.setText("Genera automaticamente");
        jbGeneraAutomaticamente.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbGeneraAutomaticamenteMousePressed(evt);
            }
        });
        jcbEncryption.setBackground(new java.awt.Color(255, 255, 153));
        jcbEncryption.setFont(new java.awt.Font("Tahoma", 1, 11));
        jcbEncryption.setForeground(new java.awt.Color(255, 0, 0));
        jcbEncryption.setSelected(true);
        jcbEncryption.setText("Encryption dati servizi");
        jbInterrogaPoolIP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/question_20.png")));
        jbInterrogaPoolIP.setText("Interroga");
        jbInterrogaPoolIP.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbInterrogaPoolIPMousePressed(evt);
            }
        });
        javax.swing.GroupLayout jpCodiciLayout = new javax.swing.GroupLayout(jpCodici);
        jpCodici.setLayout(jpCodiciLayout);
        jpCodiciLayout.setHorizontalGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpCodiciLayout.createSequentialGroup().addContainerGap().addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpCodiciLayout.createSequentialGroup().addComponent(jLabel2).addGap(729, 770, Short.MAX_VALUE)).addGroup(jpCodiciLayout.createSequentialGroup().addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jcbEncryption, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpCodiciLayout.createSequentialGroup().addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE).addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jtUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE).addComponent(jtfPassword)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbInterrogaPoolIP, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbGeneraAutomaticamente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpCodiciLayout.createSequentialGroup().addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbServer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()))));
        jpCodiciLayout.setVerticalGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpCodiciLayout.createSequentialGroup().addContainerGap().addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jbGeneraAutomaticamente, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE).addGroup(jpCodiciLayout.createSequentialGroup().addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jtfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jbInterrogaPoolIP, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpCodiciLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(jcbServer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(22, 22, 22).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(jcbEncryption).addContainerGap()));
        jTabbedPane1.addTab("Codici", jpCodici);
        jpIndirizziIP.setBackground(new java.awt.Color(204, 255, 204));
        jLabel6.setText("IP servizio:");
        jtIP.setEditable(false);
        jbWeb.setText("Web");
        jbWeb.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbWebMousePressed(evt);
            }
        });
        jbPing.setText("Ping");
        jbPing.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbPingMousePressed(evt);
            }
        });
        jLabel12.setText("IP aggiuntivi:");
        jrbIpDinamico.setBackground(new java.awt.Color(204, 255, 204));
        bgTipoIP.add(jrbIpDinamico);
        jrbIpDinamico.setText("Utilizza IP dinamico nel pool");
        jcbIpPool.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbIpPool.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbIpPoolActionPerformed(evt);
            }
        });
        jrbIpStatico.setBackground(new java.awt.Color(204, 255, 204));
        bgTipoIP.add(jrbIpStatico);
        jrbIpStatico.setText("Utilizza IP Statici");
        jbAssegnaAutomaticamente.setText("Assegna automaticamente");
        jbAssegnaAutomaticamente.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbAssegnaAutomaticamenteMousePressed(evt);
            }
        });
        jLabel13.setText("Risultato interrogazioni");
        jtaRisuInterrogazioni.setColumns(20);
        jtaRisuInterrogazioni.setRows(5);
        jtaRisuInterrogazioni.setEnabled(false);
        jScrollPane2.setViewportView(jtaRisuInterrogazioni);
        jbNuovoIPv4.setText("Aggiungi un nuovo IP");
        jbNuovoIPv4.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbNuovoIPv4MousePressed(evt);
            }
        });
        jrbNessunIP.setBackground(new java.awt.Color(204, 255, 204));
        bgTipoIP.add(jrbNessunIP);
        jrbNessunIP.setText("Non utilizzare nessun indirizzo IP");
        javax.swing.GroupLayout jpIndirizziIPLayout = new javax.swing.GroupLayout(jpIndirizziIP);
        jpIndirizziIP.setLayout(jpIndirizziIPLayout);
        jpIndirizziIPLayout.setHorizontalGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpIndirizziIPLayout.createSequentialGroup().addContainerGap().addGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpIndirizziIPLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jScrollPane2)).addGroup(jpIndirizziIPLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpIndirizziIPLayout.createSequentialGroup().addComponent(jtIP, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbAssegnaAutomaticamente).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbNuovoIPv4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbWeb).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbPing)).addComponent(jtIPAgg))).addComponent(jrbIpStatico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jpIndirizziIPLayout.createSequentialGroup().addGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jrbNessunIP).addComponent(jLabel13).addComponent(jrbIpDinamico)).addGap(0, 0, Short.MAX_VALUE)).addGroup(jpIndirizziIPLayout.createSequentialGroup().addGap(21, 21, 21).addComponent(jcbIpPool, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(85, 85, 85))).addContainerGap()));
        jpIndirizziIPLayout.setVerticalGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpIndirizziIPLayout.createSequentialGroup().addContainerGap().addComponent(jrbNessunIP).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jrbIpDinamico).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbIpPool, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jrbIpStatico).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jbPing).addComponent(jbWeb).addComponent(jbAssegnaAutomaticamente).addComponent(jbNuovoIPv4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpIndirizziIPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(jtIPAgg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel13).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE).addContainerGap()));
        jTabbedPane1.addTab("Indirizzi IP", jpIndirizziIP);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jTabbedPane1).addGroup(layout.createSequentialGroup().addComponent(jbAnnulla).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jbOkApparati).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbOk))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbOkApparati).addComponent(jbOk).addComponent(jbAnnulla)).addContainerGap()));
        pack();
    }

    private void jbOkMousePressed(java.awt.event.MouseEvent evt) {
        if (jbOk.isEnabled()) {
            if (!erroriCampiObbligatori()) {
                salvaDati(false);
            }
        }
    }

    private void jbAnnullaMousePressed(java.awt.event.MouseEvent evt) {
        this.dispose();
    }

    private void jftPrezzoMousePressed(java.awt.event.MouseEvent evt) {
        if (jcPrezzoSpeciale.isSelected()) jftPrezzo.setValue(Double.parseDouble(JOptionPane.showInputDialog(null, "Inserisci l'importo:"))); else JOptionPane.showMessageDialog(null, "Devi prima selezionare IMPOSTA PREZZO SPECIALE", "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void jftIVAMousePressed(java.awt.event.MouseEvent evt) {
        if (jcPrezzoSpeciale.isSelected()) jftIVA.setValue(Double.parseDouble(JOptionPane.showInputDialog(null, "Inserisci l'IVA:"))); else JOptionPane.showMessageDialog(null, "Devi prima selezionare IMPOSTA PREZZO SPECIALE", "Errore", JOptionPane.ERROR_MESSAGE);
    }

    private void jtNomeServizioAggMousePressed(java.awt.event.MouseEvent evt) {
        JFrame frame = new JFrame("Ricerca Servizio");
        jdRicercaServizio mm = new jdRicercaServizio(frame, true);
        mm.setVisible(true);
        listinoID = mm.listID;
        jtNomeServizioAgg.setText(mm.nome);
    }

    private void impostaCampiPrezzo() {
        jlNuovoCosto.setEnabled(jcPrezzoSpeciale.isEnabled());
        jftPrezzo.setEnabled(jcPrezzoSpeciale.isEnabled());
        jlIVA.setEnabled(jcPrezzoSpeciale.isEnabled());
        jftIVA.setEnabled(jcPrezzoSpeciale.isEnabled());
        jlNuovoCostoAttivazione.setEnabled(jcPrezzoSpeciale.isEnabled());
        jftPrezzoAttivazione.setEnabled(jcPrezzoSpeciale.isEnabled());
        jlIVAAttivazione.setEnabled(jcPrezzoSpeciale.isEnabled());
        jftIVAAttivazione.setEnabled(jcPrezzoSpeciale.isEnabled());
    }

    private void jcPrezzoSpecialeActionPerformed(java.awt.event.ActionEvent evt) {
        impostaCampiPrezzo();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        if (servAggID == -1) {
            javax.swing.JOptionPane.showMessageDialog(null, "Sit è verificato un errore interno !", "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
        jbOk.setText("Aggiorna servizio aggiuntivo");
        jbOkApparati.setText("Aggiorna servizio e configura sugli apparati");
        jftDataCreazione.setEditable(false);
        caricaDati();
        impostaCampiPrezzo();
        jcbIpPool.setRenderer(new globali.renderComboBox.renderCbGenerico());
        jcbStatoServizio.setRenderer(new globali.renderComboBox.renderCbStatoServizi());
        jcbServer.setRenderer(new globali.renderComboBox.renderCbGenerico());
        aggiornaImmagineStati();
        jcbStatoServizio.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent ev) {
                if ((ev.getStateChange() == ItemEvent.SELECTED)) {
                    aggiornaImmagineStati();
                }
            }
        });
    }

    private void jbOkApparatiMousePressed(java.awt.event.MouseEvent evt) {
        if (jbOkApparati.isEnabled()) {
            Object servID[] = (Object[]) jcbServer.getSelectedItem();
            if (!erroriCampiObbligatori()) {
                salvaDati(true);
                if ((Integer) servID[0] != -1) {
                    globali.processi.jcSincronizzaServizioAggiuntivo sync = new globali.processi.jcSincronizzaServizioAggiuntivo();
                    sync.ID = servAggID;
                    sync.SERVER = servID[2].toString();
                    sync.PORT = (Integer) servID[3];
                    sync.start();
                } else javax.swing.JOptionPane.showMessageDialog(null, "Impossibile aggiornare il servizio senza selezionare il server di riferimento !", "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void jbPingMousePressed(java.awt.event.MouseEvent evt) {
        if (jtIP.getText().length() > 0) {
            effettuaPing ping = new effettuaPing();
            ping.start();
        }
    }

    private void jtfPasswordMousePressed(java.awt.event.MouseEvent evt) {
        passCambiata = true;
    }

    private void jbWebMousePressed(java.awt.event.MouseEvent evt) {
        if (jtIP.getText().length() > 0) {
            globali.jcApriBrowserWeb.openURL(jtIP.getText());
        }
    }

    private void jlRimuoviDaFatturareMousePressed(java.awt.event.MouseEvent evt) {
        jtCliente.setText("");
        riferimentoCliID = -1;
    }

    private void jlApriSchedaClienteMousePressed(java.awt.event.MouseEvent evt) {
        if (riferimentoCliID != -1) {
            clienti.jfCliente mm = new clienti.jfCliente();
            mm.clienteID = riferimentoCliID;
            mm.setVisible(true);
        }
    }

    private void jtClienteMousePressed(java.awt.event.MouseEvent evt) {
        clienti.jdRicercaClienti mm = new clienti.jdRicercaClienti(null, true);
        mm.setVisible(true);
        if (mm.clienteID != -1) {
            riferimentoCliID = mm.clienteID;
            jtCliente.setText(mm.nomeCliente);
        }
    }

    private void jbAssegnaAutomaticamenteMousePressed(java.awt.event.MouseEvent evt) {
        if (jrbIpStatico.isSelected()) {
            if (ipID == -1) {
                jcPostgreSQL.queryDB("SELECT servizi_aggiuntivi_ipid, ip FROM servizi_aggiuntivi_ip WHERE servizi_aggiuntiviid IS NULL LIMIT 1");
                if (jcPostgreSQL.contaRighe() > 0) {
                    try {
                        jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi_ip SET servizi_aggiuntiviid=" + servAggID + " WHERE servizi_aggiuntivi_ipid=" + jcPostgreSQL.query.getInt("servizi_aggiuntivi_ipid"));
                        jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi SET ip_dinamico=FALSE WHERE servizi_aggiuntiviid=" + servAggID);
                        ipID = jcPostgreSQL.query.getInt("servizi_aggiuntivi_ipid");
                        jtIP.setText(jcPostgreSQL.query.getString("ip"));
                        jrbIpStatico.setSelected(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(jdServizioAggiuntivo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                try {
                    jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi_ip SET servizi_aggiuntiviid=null WHERE servizi_aggiuntivi_ipid=" + ipID);
                    jtIP.setText("");
                    ipID = -1;
                } catch (SQLException ex) {
                    Logger.getLogger(jdServizioAggiuntivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            impostaTastoIP();
        } else javax.swing.JOptionPane.showMessageDialog(null, "Imposta prima la modalità IP Statico !", "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    private void jcbIpPoolActionPerformed(java.awt.event.ActionEvent evt) {
        if (jcbIpPool.getSelectedItem() instanceof Object[]) {
            Object p[] = (Object[]) jcbIpPool.getSelectedItem();
            pollID = (Integer) p[0];
        }
    }

    private void jbGeneraAutomaticamenteMousePressed(java.awt.event.MouseEvent evt) {
        if (JOptionPane.showConfirmDialog(this, "Sicuro di voler generare automaticamente username e password ?", "Richiesta", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String pattern = jcPostgreSQL.cercavalore("servizi_pattern_username");
            jcPostgreSQL.queryDBRo("SELECT nextval('servizi_aggiuntivi_username_seq') AS num");
            try {
                pattern = pattern.replace("[USERNAME]", Integer.toString(jcPostgreSQL.query.getInt("num")));
                jtUsername.setText(pattern);
            } catch (SQLException ex) {
                Logger.getLogger(jdServizioAggiuntivo.class.getName()).log(Level.SEVERE, null, ex);
            }
            jtfPassword.setText(globali.jcPasswordGenerator.randomPassword(8));
        }
    }

    private void jbNuovoIPv4MousePressed(java.awt.event.MouseEvent evt) {
        if (jrbIpStatico.isSelected()) {
            JFrame frame = new JFrame("Ricerca Cliente");
            jdInserimentoIP mm = new jdInserimentoIP(frame, true);
            mm.setVisible(true);
            if (mm.valido) {
                try {
                    if (ipID != -1) {
                        jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi_ip SET servizi_aggiuntiviid=null WHERE servizi_aggiuntivi_ipid=" + ipID);
                        ipID = -1;
                    }
                    jcPostgreSQL.eseguiDB("INSERT INTO servizi_aggiuntivi_ip (servizi_aggiuntiviid, creato_da, ip) VALUES (" + servAggID + ", " + jcPostgreSQL.adminID + ", " + "'" + mm.jtfIP.getText() + "')");
                    jcPostgreSQL.eseguiDB("UPDATE servizi_aggiuntivi SET ip_dinamico=FALSE, tempo_agg_server=NULL WHERE servizi_aggiuntiviid=" + servAggID);
                    jcPostgreSQL.queryDBRo("SELECT servizi_aggiuntivi_ipid FROM servizi_aggiuntivi_ip WHERE ip='" + mm.jtfIP.getText() + "'");
                    jtIP.setText(mm.jtfIP.getText());
                    ipID = jcPostgreSQL.query.getInt("servizi_aggiuntivi_ipid");
                    impostaTastoIP();
                } catch (SQLException ex) {
                    Logger.getLogger(jdServizioAggiuntivo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else javax.swing.JOptionPane.showMessageDialog(null, "Seleziona la modalità IP statico", "Errore", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    private void jbInterrogaPoolIPMousePressed(java.awt.event.MouseEvent evt) {
        if (jtUsername.getText().length() > 0) {
            String q = jcPostgreSQL.cercavalore("servizi_query_utente");
            q = q.replace("[USERNAME]", jtUsername.getText());
            q = q.replace("[PASSWORD]", jtfPassword.getText());
            globali.jcApriBrowserWeb.openURL(q);
        }
    }

    private void jftPrezzoAttivazioneMousePressed(java.awt.event.MouseEvent evt) {
    }

    private void jftIVAAttivazioneMousePressed(java.awt.event.MouseEvent evt) {
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                jdServizioAggiuntivo dialog = new jdServizioAggiuntivo(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.ButtonGroup bgTipoIP;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JButton jbAnnulla;

    private javax.swing.JButton jbAssegnaAutomaticamente;

    private javax.swing.JButton jbGeneraAutomaticamente;

    private javax.swing.JButton jbInterrogaPoolIP;

    private javax.swing.JButton jbNuovoIPv4;

    private javax.swing.JButton jbOk;

    private javax.swing.JButton jbOkApparati;

    private javax.swing.JButton jbPing;

    private javax.swing.JButton jbWeb;

    private javax.swing.JCheckBox jcPrezzoSpeciale;

    private javax.swing.JCheckBox jcbComodato;

    private javax.swing.JCheckBox jcbEncryption;

    private javax.swing.JCheckBox jcbFatturaServizio;

    private javax.swing.JComboBox jcbIpPool;

    private javax.swing.JComboBox jcbServer;

    private javax.swing.JComboBox jcbStatoServizio;

    private javax.swing.JComboBox jcbTipoAttivazione;

    private javax.swing.JFormattedTextField jftDataCessazione;

    private javax.swing.JFormattedTextField jftDataCreazione;

    private javax.swing.JFormattedTextField jftDataScadenza;

    private javax.swing.JFormattedTextField jftIVA;

    private javax.swing.JFormattedTextField jftIVAAttivazione;

    private javax.swing.JFormattedTextField jftPrezzo;

    private javax.swing.JFormattedTextField jftPrezzoAttivazione;

    private javax.swing.JLabel jlApriSchedaCliente;

    private javax.swing.JLabel jlIVA;

    private javax.swing.JLabel jlIVAAttivazione;

    private javax.swing.JLabel jlNuovoCosto;

    private javax.swing.JLabel jlNuovoCostoAttivazione;

    private javax.swing.JLabel jlRimuoviDaFatturare;

    private javax.swing.JLabel jlSemaforo;

    private javax.swing.JPanel jpCodici;

    private javax.swing.JPanel jpIndirizziIP;

    private javax.swing.JPanel jpServizio;

    private javax.swing.JRadioButton jrbIpDinamico;

    private javax.swing.JRadioButton jrbIpStatico;

    private javax.swing.JRadioButton jrbNessunIP;

    private javax.swing.JTextField jtCliente;

    private javax.swing.JTextField jtIP;

    private javax.swing.JTextField jtIPAgg;

    private javax.swing.JTextField jtNome;

    private javax.swing.JTextField jtNomeServizioAgg;

    private javax.swing.JTextField jtUsername;

    private javax.swing.JTextArea jtaCodici;

    private javax.swing.JTextArea jtaNote;

    private javax.swing.JTextArea jtaRisuInterrogazioni;

    private javax.swing.JTextField jtfPassword;

    class effettuaPing extends Thread {

        public void run() {
            if (jtIP.getText().length() != 0) {
                jbPing.setEnabled(false);
                jbPing.setText("run..");
                String risu = "";
                try {
                    String line;
                    Process p = Runtime.getRuntime().exec(jcVariabili.CMD_PING + " " + jtIP.getText());
                    java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
                    while ((line = input.readLine()) != null) {
                        risu += line + "\n";
                        if (jcVariabili.DEBUG) System.out.println(line);
                    }
                    input.close();
                } catch (Exception err) {
                    if (jcVariabili.DEBUG) jcFunzioni.erroreSQL(err.toString());
                }
                jbPing.setText("ping");
                jtaRisuInterrogazioni.setText(risu);
                jbPing.setEnabled(true);
            }
        }
    }
}
