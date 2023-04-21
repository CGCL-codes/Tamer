package jlokg.lodger;

import com.toedter.calendar.JCalendar;
import fr.nhb.ResultSetTableModel;
import fr.nhb.ResultSetTableModelFactory;
import fr.nhb.SimpleSQLStatement;
import fr.nhb.SimpleSQLStatementTableModel;
import fr.nhb.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import jlokg.account.LKGAccountLineFacade;
import jlokg.account.LKGFinancialFile;
import jlokg.account.LKGFinancialFileFacade;
import jlokg.account.LKGFinancialFileLine;
import jlokg.account.LKGFogFacade;
import jlokg.mail.LKGLocRappelFacade;
import jlokg.mail.LKGLocRelanceFacade;
import jlokg.parameters.LKGBailIndice;
import jlokg.parameters.LKGEmploi;
import jlokg.parameters.LKGFrequenceRevision;
import jlokg.parameters.LKGNatureEmploi;
import jlokg.parameters.LKGRubrik;
import jlokg.parameters.LKGTypeBail;
import org.hibernate.HibernateException;

/**
 *
 * @author  bruno
 */
public class LKGJDLodgerUI extends javax.swing.JDialog {

    private LKGLodger m_Lodger;

    /** Creates new form JDLodgerUI */
    public LKGJDLodgerUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jCBCivilites = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTFNom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTFPrenom = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jTFReference = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTBLock = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JSeparator();
        jBtLot = new javax.swing.JButton();
        jLbLot = new javax.swing.JLabel();
        jBtProprietaire = new javax.swing.JButton();
        jLbProprietaire = new javax.swing.JLabel();
        jBtImmeuble = new javax.swing.JButton();
        jLbImmeuble = new javax.swing.JLabel();
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        jPanelBail = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLbTypeBail = new javax.swing.JLabel();
        jLbTypeBailCode = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTFBailDuree = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jCBCodeRevision = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jCBIndiceEntree = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jCBFrequenceRevision = new javax.swing.JComboBox();
        jCBDernierIndice = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jChBBailArrivantEcheance = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jTFForfait = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jCBExigible = new javax.swing.JComboBox();
        jChBEnvoiAvisQuittancement = new javax.swing.JCheckBox();
        jChBEnvoiQuittance = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jTFFrequence = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jCBAssurances = new javax.swing.JComboBox();
        jLabel59 = new javax.swing.JLabel();
        jDateChooserDateEcheance = new com.toedter.calendar.JDateChooser();
        jDateChooserDateEntree = new com.toedter.calendar.JDateChooser();
        jDateChooserDateDepart = new com.toedter.calendar.JDateChooser();
        jDateChooserProchaineRevision = new com.toedter.calendar.JDateChooser();
        jDateChooserFinForfait = new com.toedter.calendar.JDateChooser();
        jDateChooser1erQuittancement = new com.toedter.calendar.JDateChooser();
        jDateChooserProchainQuittancement = new com.toedter.calendar.JDateChooser();
        jPanelFicheFinanciere = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTbFinanicalFile = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jBtFinFileAddLine = new javax.swing.JButton();
        jBtFinFileDeleteLine = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel24 = new javax.swing.JLabel();
        jTFRB1 = new javax.swing.JTextField();
        jTFRIB2 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jRBVerseProprietaire = new javax.swing.JRadioButton();
        jRBVerseAgence = new javax.swing.JRadioButton();
        jChBVerseParTiers = new javax.swing.JCheckBox();
        jLabel26 = new javax.swing.JLabel();
        jPanelInformations = new javax.swing.JPanel();
        jTPInfos = new javax.swing.JTabbedPane();
        jPInfosInfos = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jTFRGNomDeJeuneFille = new javax.swing.JTextField();
        jCBRGEmploi = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jCBRGNatureContrat = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        jTFRGDateEmbauche = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTFRGEmployeur = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTARGAdresseEmloyeur = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        jCBRGSituation = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        jDateChooserDateNaissance = new com.toedter.calendar.JDateChooser();
        jPInfosRevenus = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTFRGSalairesNets = new javax.swing.JTextField();
        jTFRGSalairesNetsConjoint = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jTFRGRevenusGerants = new javax.swing.JTextField();
        jTFRGRevenusGerantsConjoint = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTFRGNonCommercial = new javax.swing.JTextField();
        jTFRGNonCommercialConjoint = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTFRGRevenusFonciers = new javax.swing.JTextField();
        jTFRGRevenusFonciersConjoint = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTFRGPensions = new javax.swing.JTextField();
        jTFRGPensionsConjoint = new javax.swing.JTextField();
        jTFRGBIC = new javax.swing.JTextField();
        jTFRGBICConjoint = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jTFRGMoyenneNetteMensuelle = new javax.swing.JTextField();
        jTFRGMoyenneNetteMensuelleConjoint = new javax.swing.JTextField();
        jPInfosAdresse = new javax.swing.JPanel();
        jTFAddrAdresse1 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTFAddrAdresse2 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jTFAddrCodePostal = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTFAddrVille = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jTFAddrTel = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jTFAddrFax = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jTFAddrMobile = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jTFAddrEmail = new javax.swing.JTextField();
        jPGaranties = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTbGarants = new javax.swing.JTable();
        jBtAddGarants = new javax.swing.JButton();
        jBtModifyGarants = new javax.swing.JButton();
        jBtDeleteGarant = new javax.swing.JButton();
        jLabel52 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTbContratsGaranties = new javax.swing.JTable();
        jBtAddContratGarantie = new javax.swing.JButton();
        jBtModifyContratGarantie = new javax.swing.JButton();
        jBtDeleteContratGarantie = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTbCoLodgers = new javax.swing.JTable();
        jBtAddCoLodger = new javax.swing.JButton();
        jBtModifyCoLodger = new javax.swing.JButton();
        jBtDeleteCoLodger = new javax.swing.JButton();
        jPComptabilite = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTbEcritures = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTbBrouillards = new javax.swing.JTable();
        jCBTypeBrouillards = new javax.swing.JComboBox();
        jLabel55 = new javax.swing.JLabel();
        jBtImprimerBrouillard = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTbEcrituresNonValides = new javax.swing.JTable();
        jBTModifierEcriture = new javax.swing.JButton();
        jBtAnnulationEcriture = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jBtFaturation = new javax.swing.JButton();
        jBtReglement = new javax.swing.JButton();
        jBtImpayes = new javax.swing.JButton();
        jBtRemboursement = new javax.swing.JButton();
        jBtCaution = new javax.swing.JButton();
        jPanelNotes = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTAComment = new javax.swing.JTextArea();
        jLabel56 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTbNotes = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTANotes = new javax.swing.JTextArea();
        jBtAddNote = new javax.swing.JButton();
        jBtModifyNotes = new javax.swing.JButton();
        jPanelRelances = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTbRelanceAFaire = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTbRelance = new javax.swing.JTable();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTbRappel = new javax.swing.JTable();
        jBtImprimerRelance = new javax.swing.JButton();
        jBtImprimerToutesLesRelances = new javax.swing.JButton();
        jBtReimprimerRelance = new javax.swing.JButton();
        jBtImprimerRappel = new javax.swing.JButton();
        jBtReimprimerRappel = new javax.swing.JButton();
        jPanelHistorique = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTbHisto = new javax.swing.JTable();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jDateChooserDebutPeriodeHisto = new com.toedter.calendar.JDateChooser();
        jDateChooserFinPeriodeHisto = new com.toedter.calendar.JDateChooser();
        jPanelArrieres = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTbArrieres = new javax.swing.JTable();
        jBtReglerArrieres = new javax.swing.JButton();
        jBtClose = new javax.swing.JButton();
        jBtSave = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        jCBCivilites.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Monsieur", "Madame", "Madame, Monsieur", "Mademoiselle" }));
        jLabel1.setText("Civilité");
        jLabel2.setText("Nom");
        jTFNom.setText(" ");
        jLabel3.setText("Prénom");
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jTFReference.setText("XXXXXXXX");
        jLabel4.setText("Référence");
        jTBLock.setText("jToggleButton1");
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jBtLot.setText("Lot");
        jLbLot.setText("aucun lot sélectionné");
        jBtProprietaire.setText("Propriétaire");
        jLbProprietaire.setText("aucun propriétaire sélectionné");
        jBtImmeuble.setText("Immeuble");
        jLbImmeuble.setText("aucun immeuble sélectionné");
        jLabel5.setText("Type de bail");
        jLbTypeBail.setText("xxxxxx");
        jLbTypeBailCode.setText("xxxx");
        jLabel6.setText("Durée");
        jTFBailDuree.setText("jTextField1");
        jLabel7.setText("Date entrée");
        jLabel8.setText("Date échéance");
        jLabel9.setText("Date de départ");
        jCBCodeRevision.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel10.setText("Code révision");
        jLabel11.setText("Indice d'entrée");
        jCBIndiceEntree.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel12.setText("Fréquence révision");
        jCBFrequenceRevision.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jCBDernierIndice.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel14.setText("Date prochaine révision");
        jLabel15.setText("Date fin forfait");
        jChBBailArrivantEcheance.setText("Bail arrivant à échéance, locataire prévenu");
        jChBBailArrivantEcheance.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jChBBailArrivantEcheance.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel16.setText("Forfait augmentation");
        jLabel17.setText("Date 1er quittancement");
        jLabel18.setText("Date prochain quittancement");
        jLabel19.setText("Exigibilité");
        jCBExigible.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Oui", "Non" }));
        jChBEnvoiAvisQuittancement.setText("Envoi avis de quittancement");
        jChBEnvoiAvisQuittancement.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jChBEnvoiAvisQuittancement.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jChBEnvoiQuittance.setText("Envoi de la quittance");
        jChBEnvoiQuittance.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jChBEnvoiQuittance.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel20.setText("Fréquence");
        jLabel21.setText("Assurance");
        jCBAssurances.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel59.setText("Dernier indice");
        javax.swing.GroupLayout jPanelBailLayout = new javax.swing.GroupLayout(jPanelBail);
        jPanelBail.setLayout(jPanelBailLayout);
        jPanelBailLayout.setHorizontalGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(30, Short.MAX_VALUE)).addGroup(jPanelBailLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLbTypeBail, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLbTypeBailCode).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 425, Short.MAX_VALUE).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFBailDuree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(112, 112, 112)).addGroup(jPanelBailLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 751, Short.MAX_VALUE).addContainerGap(83, Short.MAX_VALUE)).addGroup(jPanelBailLayout.createSequentialGroup().addContainerGap().addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12).addComponent(jLabel14).addComponent(jLabel15).addComponent(jLabel17).addComponent(jLabel18).addComponent(jLabel19)).addGap(31, 31, 31).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCBFrequenceRevision, 0, 110, Short.MAX_VALUE).addComponent(jCBCodeRevision, 0, 110, Short.MAX_VALUE)).addGroup(jPanelBailLayout.createSequentialGroup().addGap(1, 1, 1).addComponent(jDateChooserDateEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jDateChooserProchaineRevision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jDateChooserFinForfait, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jDateChooser1erQuittancement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jDateChooserProchainQuittancement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addGap(57, 57, 57).addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jDateChooserDateEcheance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(44, 44, 44).addComponent(jLabel9).addGap(12, 12, 12).addComponent(jDateChooserDateDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanelBailLayout.createSequentialGroup().addGap(13, 13, 13).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jCBDernierIndice, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jCBIndiceEntree, 0, 178, Short.MAX_VALUE))).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel59, javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jLabel16).addGap(21, 21, 21).addComponent(jTFForfait, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jChBBailArrivantEcheance))))).addGroup(jPanelBailLayout.createSequentialGroup().addGap(13, 13, 13).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jChBEnvoiQuittance).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jChBEnvoiAvisQuittancement).addGap(38, 38, 38).addComponent(jLabel20).addGap(16, 16, 16).addComponent(jTFFrequence, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))).addContainerGap(118, Short.MAX_VALUE)).addGroup(jPanelBailLayout.createSequentialGroup().addComponent(jCBExigible, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))).addGroup(jPanelBailLayout.createSequentialGroup().addContainerGap().addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(30, Short.MAX_VALUE)).addGroup(jPanelBailLayout.createSequentialGroup().addContainerGap().addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 804, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(30, Short.MAX_VALUE)).addGroup(jPanelBailLayout.createSequentialGroup().addContainerGap().addComponent(jLabel21).addGap(119, 119, 119).addComponent(jCBAssurances, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(513, Short.MAX_VALUE)));
        jPanelBailLayout.setVerticalGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addContainerGap().addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jLbTypeBail).addComponent(jLbTypeBailCode).addComponent(jLabel6).addComponent(jTFBailDuree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jLabel8).addComponent(jLabel9)).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jDateChooserDateDepart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jDateChooserDateEcheance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jDateChooserDateEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(jCBCodeRevision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11).addComponent(jCBIndiceEntree, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(jCBFrequenceRevision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCBDernierIndice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel59)).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createSequentialGroup().addGap(14, 14, 14).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(jChBBailArrivantEcheance))).addGroup(jPanelBailLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jDateChooserProchaineRevision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(jLabel16).addComponent(jTFForfait, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jDateChooserFinForfait, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel17).addComponent(jChBEnvoiAvisQuittancement).addComponent(jLabel20).addComponent(jTFFrequence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jDateChooser1erQuittancement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(jChBEnvoiQuittance)).addComponent(jDateChooserProchainQuittancement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(jCBExigible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelBailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel21).addComponent(jCBAssurances, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(37, Short.MAX_VALUE)));
        jTabbedPaneMain.addTab("Bail", jPanelBail);
        jTbFinanicalFile.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane1.setViewportView(jTbFinanicalFile);
        jLabel22.setText("Total");
        jLabel23.setText("nnnnnn");
        jBtFinFileAddLine.setText("Ajouter");
        jBtFinFileAddLine.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtFinFileAddLineActionPerformed(evt);
            }
        });
        jBtFinFileDeleteLine.setText("Supprimer");
        jLabel24.setText("R.I.B 1");
        jTFRB1.setText("RIB1");
        jTFRIB2.setText("RIB2");
        jLabel25.setText("R.I.B 2");
        buttonGroup1.add(jRBVerseProprietaire);
        jRBVerseProprietaire.setText("versée au propriétaire");
        jRBVerseProprietaire.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRBVerseProprietaire.setMargin(new java.awt.Insets(0, 0, 0, 0));
        buttonGroup1.add(jRBVerseAgence);
        jRBVerseAgence.setText("versée à l'agence");
        jRBVerseAgence.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRBVerseAgence.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jChBVerseParTiers.setText("Caution versée par organisme tiers");
        jChBVerseParTiers.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jChBVerseParTiers.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel26.setText("Caution");
        javax.swing.GroupLayout jPanelFicheFinanciereLayout = new javax.swing.GroupLayout(jPanelFicheFinanciere);
        jPanelFicheFinanciere.setLayout(jPanelFicheFinanciereLayout);
        jPanelFicheFinanciereLayout.setHorizontalGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFicheFinanciereLayout.createSequentialGroup().addGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelFicheFinanciereLayout.createSequentialGroup().addContainerGap().addComponent(jLabel24).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFRB1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel25).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFRIB2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelFicheFinanciereLayout.createSequentialGroup().addGap(25, 25, 25).addComponent(jLabel26).addGap(22, 22, 22).addGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRBVerseProprietaire).addComponent(jRBVerseAgence).addComponent(jChBVerseParTiers))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelFicheFinanciereLayout.createSequentialGroup().addContainerGap().addComponent(jBtFinFileAddLine).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtFinFileDeleteLine)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelFicheFinanciereLayout.createSequentialGroup().addContainerGap().addComponent(jSeparator8, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)).addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 828, Short.MAX_VALUE).addGroup(jPanelFicheFinanciereLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(656, 656, 656).addComponent(jLabel22).addGap(14, 14, 14).addComponent(jLabel23)));
        jPanelFicheFinanciereLayout.setVerticalGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelFicheFinanciereLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel22).addComponent(jLabel23).addComponent(jBtFinFileAddLine).addComponent(jBtFinFileDeleteLine)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel24).addComponent(jTFRB1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel25).addComponent(jTFRIB2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelFicheFinanciereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanelFicheFinanciereLayout.createSequentialGroup().addComponent(jRBVerseProprietaire).addGap(11, 11, 11).addComponent(jRBVerseAgence))).addGap(16, 16, 16).addComponent(jChBVerseParTiers).addGap(33, 33, 33)));
        jTabbedPaneMain.addTab("Fiche financière", jPanelFicheFinanciere);
        jLabel27.setText("Date de naissance");
        jLabel28.setText("Nom de jeune fille");
        jCBRGEmploi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel29.setText("Emploi");
        jLabel30.setText("Nature du contrat");
        jCBRGNatureContrat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel31.setText("Date d'embauche");
        jTFRGDateEmbauche.setText("SSAA/MM/JJ");
        jLabel32.setText("Employeur");
        jTARGAdresseEmloyeur.setColumns(20);
        jTARGAdresseEmloyeur.setRows(5);
        jScrollPane2.setViewportView(jTARGAdresseEmloyeur);
        jLabel33.setText("Adresse");
        jCBRGSituation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel34.setText("Situation");
        javax.swing.GroupLayout jPInfosInfosLayout = new javax.swing.GroupLayout(jPInfosInfos);
        jPInfosInfos.setLayout(jPInfosInfosLayout);
        jPInfosInfosLayout.setHorizontalGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosInfosLayout.createSequentialGroup().addContainerGap().addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel27).addComponent(jLabel29).addComponent(jLabel31).addComponent(jLabel32).addComponent(jLabel33).addComponent(jLabel34)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTFRGDateEmbauche, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPInfosInfosLayout.createSequentialGroup().addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jCBRGSituation, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTFRGEmployeur, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCBRGEmploi, javax.swing.GroupLayout.Alignment.LEADING, 0, 197, Short.MAX_VALUE)).addComponent(jDateChooserDateNaissance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPInfosInfosLayout.createSequentialGroup().addComponent(jLabel28).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFRGNomDeJeuneFille)).addGroup(jPInfosInfosLayout.createSequentialGroup().addComponent(jLabel30).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCBRGNatureContrat, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))))).addContainerGap(139, Short.MAX_VALUE)));
        jPInfosInfosLayout.setVerticalGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosInfosLayout.createSequentialGroup().addContainerGap().addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosInfosLayout.createSequentialGroup().addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jDateChooserDateNaissance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel27)).addGap(19, 19, 19).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCBRGEmploi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel29))).addGroup(jPInfosInfosLayout.createSequentialGroup().addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel28).addComponent(jTFRGNomDeJeuneFille, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(19, 19, 19).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel30).addComponent(jCBRGNatureContrat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGap(23, 23, 23).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel31).addComponent(jTFRGDateEmbauche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel32).addComponent(jTFRGEmployeur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel33)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosInfosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel34).addComponent(jCBRGSituation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(41, Short.MAX_VALUE)));
        jTPInfos.addTab("Informations", jPInfosInfos);
        jLabel35.setText("Locataire");
        jLabel36.setText("Conjoint");
        jLabel37.setText("Salaires nets");
        jLabel38.setText("Revenus gérants");
        jLabel39.setText("Revenus non commerciaux");
        jLabel40.setText("Revenus fonciers");
        jLabel41.setText("Pensions");
        jLabel42.setText("BIC");
        jLabel43.setText("Moyenne nette mensuelle");
        javax.swing.GroupLayout jPInfosRevenusLayout = new javax.swing.GroupLayout(jPInfosRevenus);
        jPInfosRevenus.setLayout(jPInfosRevenusLayout);
        jPInfosRevenusLayout.setHorizontalGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosRevenusLayout.createSequentialGroup().addGap(121, 121, 121).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel37).addComponent(jLabel38).addComponent(jLabel39).addComponent(jLabel40).addComponent(jLabel41).addComponent(jLabel42).addComponent(jLabel43)).addGap(42, 42, 42).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTFRGBIC).addComponent(jTFRGRevenusFonciers).addComponent(jTFRGRevenusGerants).addComponent(jLabel35).addComponent(jTFRGNonCommercial).addComponent(jTFRGPensions).addComponent(jTFRGSalairesNets).addGroup(jPInfosRevenusLayout.createSequentialGroup().addComponent(jTFRGMoyenneNetteMensuelle, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTFRGBICConjoint).addComponent(jTFRGPensionsConjoint).addComponent(jTFRGRevenusFonciersConjoint).addComponent(jTFRGNonCommercialConjoint).addComponent(jTFRGRevenusGerantsConjoint).addComponent(jLabel36).addComponent(jTFRGSalairesNetsConjoint).addComponent(jTFRGMoyenneNetteMensuelleConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(281, Short.MAX_VALUE)));
        jPInfosRevenusLayout.setVerticalGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosRevenusLayout.createSequentialGroup().addGap(31, 31, 31).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel35).addComponent(jLabel36)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel37).addComponent(jTFRGSalairesNets, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFRGSalairesNetsConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel38).addComponent(jTFRGRevenusGerants, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jTFRGRevenusGerantsConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel39).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTFRGNonCommercial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFRGNonCommercialConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel40).addComponent(jTFRGRevenusFonciersConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFRGRevenusFonciers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel41).addComponent(jTFRGPensions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFRGPensionsConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel42).addComponent(jTFRGBICConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFRGBIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosRevenusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel43).addComponent(jTFRGMoyenneNetteMensuelle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFRGMoyenneNetteMensuelleConjoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(81, Short.MAX_VALUE)));
        jTPInfos.addTab("Revenus", jPInfosRevenus);
        jLabel44.setText("Adresse");
        jTFAddrAdresse2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFAddrAdresse2ActionPerformed(evt);
            }
        });
        jLabel45.setText("Code postal");
        jLabel46.setText("Ville");
        jLabel47.setText("Tel");
        jLabel48.setText("Fax");
        jLabel49.setText("Mobile");
        jLabel50.setText("Email");
        javax.swing.GroupLayout jPInfosAdresseLayout = new javax.swing.GroupLayout(jPInfosAdresse);
        jPInfosAdresse.setLayout(jPInfosAdresseLayout);
        jPInfosAdresseLayout.setHorizontalGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosAdresseLayout.createSequentialGroup().addGap(189, 189, 189).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel44).addComponent(jLabel45).addComponent(jLabel46).addComponent(jLabel47).addComponent(jLabel49).addComponent(jLabel50)).addGap(21, 21, 21).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTFAddrEmail).addComponent(jTFAddrVille).addComponent(jTFAddrCodePostal).addComponent(jTFAddrAdresse1).addComponent(jTFAddrAdresse2, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE).addGroup(jPInfosAdresseLayout.createSequentialGroup().addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jTFAddrMobile, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTFAddrTel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel48).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFAddrFax))).addContainerGap(256, Short.MAX_VALUE)));
        jPInfosAdresseLayout.setVerticalGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPInfosAdresseLayout.createSequentialGroup().addGap(21, 21, 21).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel44).addComponent(jTFAddrAdresse1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFAddrAdresse2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel45).addComponent(jTFAddrCodePostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel46).addComponent(jTFAddrVille, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(43, 43, 43).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel47).addComponent(jLabel48).addComponent(jTFAddrTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTFAddrFax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(21, 21, 21).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel49).addComponent(jTFAddrMobile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(17, 17, 17).addGroup(jPInfosAdresseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel50).addComponent(jTFAddrEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(48, Short.MAX_VALUE)));
        jTPInfos.addTab("Adresse", jPInfosAdresse);
        jLabel51.setText("Garants");
        jTbGarants.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane3.setViewportView(jTbGarants);
        jBtAddGarants.setText("Ajouter");
        jBtAddGarants.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAddGarantsActionPerformed(evt);
            }
        });
        jBtModifyGarants.setText("Modifier");
        jBtModifyGarants.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtModifyGarantsActionPerformed(evt);
            }
        });
        jBtDeleteGarant.setText("Supprimer");
        jLabel52.setText("Contrats et garanties obligatoires");
        jTbContratsGaranties.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane4.setViewportView(jTbContratsGaranties);
        jBtAddContratGarantie.setText("Ajouter");
        jBtAddContratGarantie.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAddContratGarantieActionPerformed(evt);
            }
        });
        jBtModifyContratGarantie.setText("Modifier");
        jBtModifyContratGarantie.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtModifyContratGarantieActionPerformed(evt);
            }
        });
        jBtDeleteContratGarantie.setText("Supprimer");
        javax.swing.GroupLayout jPGarantiesLayout = new javax.swing.GroupLayout(jPGaranties);
        jPGaranties.setLayout(jPGarantiesLayout);
        jPGarantiesLayout.setHorizontalGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPGarantiesLayout.createSequentialGroup().addContainerGap().addGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE).addGroup(jPGarantiesLayout.createSequentialGroup().addGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel51).addGroup(jPGarantiesLayout.createSequentialGroup().addGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPGarantiesLayout.createSequentialGroup().addComponent(jBtAddGarants).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtModifyGarants).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtDeleteGarant)).addComponent(jLabel52)).addGap(464, 464, 464).addComponent(jSeparator9))).addContainerGap(29, Short.MAX_VALUE)).addGroup(jPGarantiesLayout.createSequentialGroup().addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE).addContainerGap()).addGroup(jPGarantiesLayout.createSequentialGroup().addComponent(jBtAddContratGarantie).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtModifyContratGarantie).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtDeleteContratGarantie).addContainerGap(494, Short.MAX_VALUE)))));
        jPGarantiesLayout.setVerticalGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPGarantiesLayout.createSequentialGroup().addContainerGap().addComponent(jLabel51).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtAddGarants).addComponent(jBtModifyGarants).addComponent(jBtDeleteGarant)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel52)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPGarantiesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtAddContratGarantie).addComponent(jBtModifyContratGarantie).addComponent(jBtDeleteContratGarantie)).addGap(19, 19, 19)));
        jTPInfos.addTab("Garanties", jPGaranties);
        jLabel53.setText("Colocataires");
        jTbCoLodgers.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane5.setViewportView(jTbCoLodgers);
        jBtAddCoLodger.setText("Ajouter");
        jBtAddCoLodger.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtAddCoLodgerActionPerformed(evt);
            }
        });
        jBtModifyCoLodger.setText("Modifier");
        jBtModifyCoLodger.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtModifyCoLodgerActionPerformed(evt);
            }
        });
        jBtDeleteCoLodger.setText("Supprimer");
        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel13Layout.createSequentialGroup().addContainerGap().addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE).addComponent(jLabel53).addGroup(jPanel13Layout.createSequentialGroup().addComponent(jBtAddCoLodger).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtModifyCoLodger).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtDeleteCoLodger))).addContainerGap()));
        jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel13Layout.createSequentialGroup().addContainerGap().addComponent(jLabel53).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtAddCoLodger).addComponent(jBtModifyCoLodger).addComponent(jBtDeleteCoLodger)).addContainerGap(146, Short.MAX_VALUE)));
        jTPInfos.addTab("Colocataires", jPanel13);
        javax.swing.GroupLayout jPanelInformationsLayout = new javax.swing.GroupLayout(jPanelInformations);
        jPanelInformations.setLayout(jPanelInformationsLayout);
        jPanelInformationsLayout.setHorizontalGroup(jPanelInformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelInformationsLayout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jTPInfos, javax.swing.GroupLayout.PREFERRED_SIZE, 742, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(92, 92, 92)));
        jPanelInformationsLayout.setVerticalGroup(jPanelInformationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelInformationsLayout.createSequentialGroup().addGap(22, 22, 22).addComponent(jTPInfos, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE).addContainerGap()));
        jTabbedPaneMain.addTab("Informations", jPanelInformations);
        jLabel54.setText("Ecritures validées");
        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jTbEcritures.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane6.setViewportView(jTbEcritures);
        jTbBrouillards.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jTbBrouillards.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTbBrouillardsMousePressed(evt);
            }
        });
        jScrollPane7.setViewportView(jTbBrouillards);
        jCBTypeBrouillards.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Brouillards non validés", "Brouillards validés" }));
        jCBTypeBrouillards.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBTypeBrouillardsActionPerformed(evt);
            }
        });
        jLabel55.setText("Ecritures non validées");
        jBtImprimerBrouillard.setText("Imprimer le brouilard");
        jTbEcrituresNonValides.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane8.setViewportView(jTbEcrituresNonValides);
        jBTModifierEcriture.setText("Modification");
        jBtAnnulationEcriture.setText("Annulation");
        jBtFaturation.setText("Facturation");
        jBtReglement.setText("Réglement");
        jBtImpayes.setText("Impayés");
        jBtRemboursement.setText("Remboursement");
        jBtCaution.setText("Caution");
        javax.swing.GroupLayout jPComptabiliteLayout = new javax.swing.GroupLayout(jPComptabilite);
        jPComptabilite.setLayout(jPComptabiliteLayout);
        jPComptabiliteLayout.setHorizontalGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPComptabiliteLayout.createSequentialGroup().addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPComptabiliteLayout.createSequentialGroup().addContainerGap().addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator10, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE).addComponent(jLabel54).addGroup(jPComptabiliteLayout.createSequentialGroup().addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jBtImprimerBrouillard, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE).addComponent(jCBTypeBrouillards, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jComboBox10, javax.swing.GroupLayout.Alignment.LEADING, 0, 126, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPComptabiliteLayout.createSequentialGroup().addComponent(jBTModifierEcriture).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtAnnulationEcriture)).addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE).addComponent(jLabel55).addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE))))).addGroup(jPComptabiliteLayout.createSequentialGroup().addGap(149, 149, 149).addComponent(jBtFaturation).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtReglement).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtImpayes).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtRemboursement).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtCaution))).addContainerGap()));
        jPComptabiliteLayout.setVerticalGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPComptabiliteLayout.createSequentialGroup().addContainerGap().addComponent(jLabel54).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel55).addComponent(jCBTypeBrouillards, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14, 14, 14).addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane8, 0, 0, Short.MAX_VALUE).addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtImprimerBrouillard).addComponent(jBTModifierEcriture).addComponent(jBtAnnulationEcriture)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPComptabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtFaturation).addComponent(jBtReglement).addComponent(jBtImpayes).addComponent(jBtRemboursement).addComponent(jBtCaution)).addGap(23, 23, 23)));
        jTabbedPaneMain.addTab("Comptabilité", jPComptabilite);
        jTAComment.setColumns(20);
        jTAComment.setRows(5);
        jScrollPane9.setViewportView(jTAComment);
        jLabel56.setText("Notes");
        jTbNotes.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane10.setViewportView(jTbNotes);
        jTANotes.setColumns(20);
        jTANotes.setRows(5);
        jScrollPane11.setViewportView(jTANotes);
        jBtAddNote.setText("Nouveau");
        jBtModifyNotes.setText("Modifier");
        javax.swing.GroupLayout jPanelNotesLayout = new javax.swing.GroupLayout(jPanelNotes);
        jPanelNotes.setLayout(jPanelNotesLayout);
        jPanelNotesLayout.setHorizontalGroup(jPanelNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelNotesLayout.createSequentialGroup().addContainerGap().addGroup(jPanelNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelNotesLayout.createSequentialGroup().addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelNotesLayout.createSequentialGroup().addComponent(jBtAddNote).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtModifyNotes)).addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE).addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))).addComponent(jLabel56)).addContainerGap()));
        jPanelNotesLayout.setVerticalGroup(jPanelNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelNotesLayout.createSequentialGroup().addGroup(jPanelNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelNotesLayout.createSequentialGroup().addContainerGap().addComponent(jLabel56).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)).addGroup(jPanelNotesLayout.createSequentialGroup().addGap(36, 36, 36).addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanelNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtAddNote).addComponent(jBtModifyNotes)).addGap(7, 7, 7).addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jTabbedPaneMain.addTab("Notes", jPanelNotes);
        jTbRelanceAFaire.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane12.setViewportView(jTbRelanceAFaire);
        jTbRelance.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane13.setViewportView(jTbRelance);
        jTbRappel.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane14.setViewportView(jTbRappel);
        jBtImprimerRelance.setText("Imprimer la relance");
        jBtImprimerToutesLesRelances.setText("Imprimer toutes les relances");
        jBtReimprimerRelance.setText("Réimprimer la relance");
        jBtImprimerRappel.setText("Imprimer un rappel");
        jBtReimprimerRappel.setText("Réimprimer un rappel");
        javax.swing.GroupLayout jPanelRelancesLayout = new javax.swing.GroupLayout(jPanelRelances);
        jPanelRelances.setLayout(jPanelRelancesLayout);
        jPanelRelancesLayout.setHorizontalGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelRelancesLayout.createSequentialGroup().addContainerGap().addGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jBtImprimerToutesLesRelances, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBtReimprimerRelance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBtImprimerRelance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBtImprimerRappel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBtReimprimerRappel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE).addGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE).addComponent(jScrollPane13).addComponent(jScrollPane14)).addContainerGap()));
        jPanelRelancesLayout.setVerticalGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelRelancesLayout.createSequentialGroup().addContainerGap().addGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelRelancesLayout.createSequentialGroup().addComponent(jBtImprimerRelance).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtImprimerToutesLesRelances)).addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(17, 17, 17).addGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jBtReimprimerRelance).addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE).addGroup(jPanelRelancesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelRelancesLayout.createSequentialGroup().addComponent(jBtImprimerRappel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtReimprimerRappel)).addComponent(jScrollPane14, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jTabbedPaneMain.addTab("Relances", jPanelRelances);
        jTbHisto.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane15.setViewportView(jTbHisto);
        jLabel57.setText("Période du");
        jLabel58.setText("Au");
        javax.swing.GroupLayout jPanelHistoriqueLayout = new javax.swing.GroupLayout(jPanelHistorique);
        jPanelHistorique.setLayout(jPanelHistoriqueLayout);
        jPanelHistoriqueLayout.setHorizontalGroup(jPanelHistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHistoriqueLayout.createSequentialGroup().addContainerGap().addGroup(jPanelHistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHistoriqueLayout.createSequentialGroup().addComponent(jLabel57).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jDateChooserDebutPeriodeHisto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel58).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jDateChooserFinPeriodeHisto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)).addContainerGap()));
        jPanelHistoriqueLayout.setVerticalGroup(jPanelHistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelHistoriqueLayout.createSequentialGroup().addContainerGap().addGroup(jPanelHistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanelHistoriqueLayout.createSequentialGroup().addGroup(jPanelHistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel57).addComponent(jLabel58)).addGap(12, 12, 12)).addGroup(jPanelHistoriqueLayout.createSequentialGroup().addGroup(jPanelHistoriqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jDateChooserFinPeriodeHisto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jDateChooserDebutPeriodeHisto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(164, Short.MAX_VALUE)));
        jTabbedPaneMain.addTab("Historique", jPanelHistorique);
        jTbArrieres.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        jScrollPane16.setViewportView(jTbArrieres);
        jBtReglerArrieres.setText("Réglement");
        javax.swing.GroupLayout jPanelArrieresLayout = new javax.swing.GroupLayout(jPanelArrieres);
        jPanelArrieres.setLayout(jPanelArrieresLayout);
        jPanelArrieresLayout.setHorizontalGroup(jPanelArrieresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelArrieresLayout.createSequentialGroup().addContainerGap(223, Short.MAX_VALUE).addGroup(jPanelArrieresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelArrieresLayout.createSequentialGroup().addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(169, 169, 169)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelArrieresLayout.createSequentialGroup().addComponent(jBtReglerArrieres).addGap(375, 375, 375)))));
        jPanelArrieresLayout.setVerticalGroup(jPanelArrieresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanelArrieresLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtReglerArrieres).addContainerGap(167, Short.MAX_VALUE)));
        jTabbedPaneMain.addTab("Arriérés", jPanelArrieres);
        jTabbedPaneMain.getAccessibleContext().setAccessibleName("Bail");
        jBtClose.setText("Fermer");
        jBtClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtCloseActionPerformed(evt);
            }
        });
        jBtSave.setText("Enregistrer");
        jBtSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtSaveActionPerformed(evt);
            }
        });
        jButton4.setText("jButton4");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jLabel2).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTFPrenom).addComponent(jCBCivilites, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jTFNom, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jTFReference, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTBLock, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jBtImmeuble, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBtLot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jBtProprietaire)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLbImmeuble, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLbLot, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLbProprietaire, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(246, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(549, Short.MAX_VALUE).addComponent(jButton4).addGap(79, 79, 79).addComponent(jBtClose).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtSave).addContainerGap()).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPaneMain, javax.swing.GroupLayout.PREFERRED_SIZE, 849, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(6, 6, 6).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTFReference, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTBLock))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLbLot).addComponent(jBtLot)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtProprietaire).addComponent(jLbProprietaire)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLbImmeuble).addComponent(jBtImmeuble))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel1).addComponent(jCBCivilites, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTFNom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTFPrenom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)))).addGap(9, 9, 9)))).addGap(15, 15, 15).addComponent(jTabbedPaneMain, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtSave).addComponent(jBtClose).addComponent(jButton4)).addContainerGap()));
        pack();
    }

    private void jBtFinFileAddLineActionPerformed(java.awt.event.ActionEvent evt) {
        this.addFinancialFileLine();
    }

    private void jBtModifyContratGarantieActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.jTbContratsGaranties.getSelectedRow() < 0) return;
        LKGLocContratGarantie contrat = (LKGLocContratGarantie) this.jTbContratsGaranties.getModel().getValueAt(this.jTbContratsGaranties.getSelectedRow(), -2);
        LKGLocContratGarantieUI ui = new LKGLocContratGarantieUI(this, true);
        ui.display(contrat);
        ui.getJBtSave().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("actionlistener on LKGLocContratGarantieUI jbtsave");
                AbstractTableModel model = (AbstractTableModel) jTbContratsGaranties.getModel();
                model.fireTableDataChanged();
            }
        });
        ui.setVisible(true);
    }

    private void jBtAddContratGarantieActionPerformed(java.awt.event.ActionEvent evt) {
        final LKGLocContratGarantie contrat = new LKGLocContratGarantie();
        contrat.setLodger(this.m_Lodger.getCode());
        LKGLocContratGarantieUI ui = new LKGLocContratGarantieUI(this, true);
        ui.display(contrat);
        ui.getJBtSave().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("actionlistener on jbtsave");
                m_Lodger.getContratsGaranties().add(contrat);
                AbstractTableModel model = (AbstractTableModel) jTbContratsGaranties.getModel();
                model.fireTableDataChanged();
            }
        });
        ui.setVisible(true);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!controlInput()) return;
        updateLodger();
        LKGLodgerFacade facade = new LKGLodgerFacade();
        if (m_Lodger.getCode().equals("")) {
            facade.saveLKGLodger(m_Lodger);
        } else {
            facade.saveOrUpdateLKGLodger(m_Lodger);
        }
        JOptionPane.showMessageDialog(this, "Locataire sauvegard� !", "", JOptionPane.PLAIN_MESSAGE);
    }

    private void jBtSaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (!controlInput()) return;
        updateLodger();
        LKGLodgerFacade facade = new LKGLodgerFacade();
        LKGFinancialFileFacade finFileFacade = new LKGFinancialFileFacade();
        if (m_Lodger.getCode().equals("")) {
            facade.saveLKGLodger(m_Lodger);
            finFileFacade.saveLKGFinFile(m_Lodger.getFinancialFile());
        } else {
            facade.saveOrUpdateLKGLodger(m_Lodger);
            finFileFacade.saveOrUpdateLKGFinFile(m_Lodger.getFinancialFile());
        }
        JOptionPane.showMessageDialog(this, "Locataire sauvegard� !", "", JOptionPane.PLAIN_MESSAGE);
    }

    private void jBtModifyGarantsActionPerformed(java.awt.event.ActionEvent evt) {
        LKGGarant garant = (LKGGarant) this.jTbGarants.getModel().getValueAt(this.jTbGarants.getSelectedRow(), -2);
        LKGGarantUI ui = new LKGGarantUI(this, true);
        ui.display(garant);
        ui.getJBtSaveGarant().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("actionlistener on getJBtSaveGarant");
                AbstractTableModel model = (AbstractTableModel) jTbGarants.getModel();
            }
        });
        ui.setVisible(true);
    }

    private void jBtAddGarantsActionPerformed(java.awt.event.ActionEvent evt) {
        final LKGGarant garant = new LKGGarant();
        garant.setLodger(this.m_Lodger.getCode());
        LKGGarantUI ui = new LKGGarantUI(this, true);
        ui.display(garant);
        ui.getJBtSaveGarant().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("actionlistener on jbtsave");
                m_Lodger.getGarants().add(garant);
                AbstractTableModel model = (AbstractTableModel) jTbGarants.getModel();
                model.fireTableDataChanged();
            }
        });
        ui.setVisible(true);
    }

    private void jBtModifyCoLodgerActionPerformed(java.awt.event.ActionEvent evt) {
        final LKGCoLodger colLodger = (LKGCoLodger) this.jTbCoLodgers.getModel().getValueAt(this.jTbCoLodgers.getSelectedRow(), -2);
        LKGCoLodgerUI ui = new LKGCoLodgerUI(this, true);
        ui.display(colLodger);
        ui.getJBtSave().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("actionlistener on jbtsave");
                m_Lodger.getCoLocataires().add(colLodger);
                AbstractTableModel model = (AbstractTableModel) jTbCoLodgers.getModel();
                model.fireTableDataChanged();
            }
        });
        ui.setVisible(true);
    }

    private void jBtAddCoLodgerActionPerformed(java.awt.event.ActionEvent evt) {
        final LKGCoLodger colLodger = new LKGCoLodger();
        LKGCoLodgerUI ui = new LKGCoLodgerUI(this, true);
        ui.display(colLodger);
        ui.getJBtSave().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("actionlistener on jbtsave");
                m_Lodger.getCoLocataires().add(colLodger);
                AbstractTableModel model = (AbstractTableModel) jTbCoLodgers.getModel();
                model.fireTableDataChanged();
            }
        });
        ui.setVisible(true);
    }

    private void jTbBrouillardsMousePressed(java.awt.event.MouseEvent evt) {
        if (jTbBrouillards.getSelectedRow() > -1) this.DisplayAccountFogLines();
    }

    private void jCBTypeBrouillardsActionPerformed(java.awt.event.ActionEvent evt) {
        this.displayFog();
    }

    private void jTFAddrAdresse2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jBtCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new LKGJDLodgerUI(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.ButtonGroup buttonGroup2;

    private javax.swing.JButton jBTModifierEcriture;

    private javax.swing.JButton jBtAddCoLodger;

    private javax.swing.JButton jBtAddContratGarantie;

    private javax.swing.JButton jBtAddGarants;

    private javax.swing.JButton jBtAddNote;

    private javax.swing.JButton jBtAnnulationEcriture;

    private javax.swing.JButton jBtCaution;

    private javax.swing.JButton jBtClose;

    private javax.swing.JButton jBtDeleteCoLodger;

    private javax.swing.JButton jBtDeleteContratGarantie;

    private javax.swing.JButton jBtDeleteGarant;

    private javax.swing.JButton jBtFaturation;

    private javax.swing.JButton jBtFinFileAddLine;

    private javax.swing.JButton jBtFinFileDeleteLine;

    private javax.swing.JButton jBtImmeuble;

    private javax.swing.JButton jBtImpayes;

    private javax.swing.JButton jBtImprimerBrouillard;

    private javax.swing.JButton jBtImprimerRappel;

    private javax.swing.JButton jBtImprimerRelance;

    private javax.swing.JButton jBtImprimerToutesLesRelances;

    private javax.swing.JButton jBtLot;

    private javax.swing.JButton jBtModifyCoLodger;

    private javax.swing.JButton jBtModifyContratGarantie;

    private javax.swing.JButton jBtModifyGarants;

    private javax.swing.JButton jBtModifyNotes;

    private javax.swing.JButton jBtProprietaire;

    private javax.swing.JButton jBtReglement;

    private javax.swing.JButton jBtReglerArrieres;

    private javax.swing.JButton jBtReimprimerRappel;

    private javax.swing.JButton jBtReimprimerRelance;

    private javax.swing.JButton jBtRemboursement;

    private javax.swing.JButton jBtSave;

    private javax.swing.JButton jButton4;

    private javax.swing.JComboBox jCBAssurances;

    private javax.swing.JComboBox jCBCivilites;

    private javax.swing.JComboBox jCBCodeRevision;

    private javax.swing.JComboBox jCBDernierIndice;

    private javax.swing.JComboBox jCBExigible;

    private javax.swing.JComboBox jCBFrequenceRevision;

    private javax.swing.JComboBox jCBIndiceEntree;

    private javax.swing.JComboBox jCBRGEmploi;

    private javax.swing.JComboBox jCBRGNatureContrat;

    private javax.swing.JComboBox jCBRGSituation;

    private javax.swing.JComboBox jCBTypeBrouillards;

    private javax.swing.JCheckBox jChBBailArrivantEcheance;

    private javax.swing.JCheckBox jChBEnvoiAvisQuittancement;

    private javax.swing.JCheckBox jChBEnvoiQuittance;

    private javax.swing.JCheckBox jChBVerseParTiers;

    private javax.swing.JComboBox jComboBox10;

    private com.toedter.calendar.JDateChooser jDateChooser1erQuittancement;

    private com.toedter.calendar.JDateChooser jDateChooserDateDepart;

    private com.toedter.calendar.JDateChooser jDateChooserDateEcheance;

    private com.toedter.calendar.JDateChooser jDateChooserDateEntree;

    private com.toedter.calendar.JDateChooser jDateChooserDateNaissance;

    private com.toedter.calendar.JDateChooser jDateChooserDebutPeriodeHisto;

    private com.toedter.calendar.JDateChooser jDateChooserFinForfait;

    private com.toedter.calendar.JDateChooser jDateChooserFinPeriodeHisto;

    private com.toedter.calendar.JDateChooser jDateChooserProchainQuittancement;

    private com.toedter.calendar.JDateChooser jDateChooserProchaineRevision;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel30;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JLabel jLabel32;

    private javax.swing.JLabel jLabel33;

    private javax.swing.JLabel jLabel34;

    private javax.swing.JLabel jLabel35;

    private javax.swing.JLabel jLabel36;

    private javax.swing.JLabel jLabel37;

    private javax.swing.JLabel jLabel38;

    private javax.swing.JLabel jLabel39;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel40;

    private javax.swing.JLabel jLabel41;

    private javax.swing.JLabel jLabel42;

    private javax.swing.JLabel jLabel43;

    private javax.swing.JLabel jLabel44;

    private javax.swing.JLabel jLabel45;

    private javax.swing.JLabel jLabel46;

    private javax.swing.JLabel jLabel47;

    private javax.swing.JLabel jLabel48;

    private javax.swing.JLabel jLabel49;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel50;

    private javax.swing.JLabel jLabel51;

    private javax.swing.JLabel jLabel52;

    private javax.swing.JLabel jLabel53;

    private javax.swing.JLabel jLabel54;

    private javax.swing.JLabel jLabel55;

    private javax.swing.JLabel jLabel56;

    private javax.swing.JLabel jLabel57;

    private javax.swing.JLabel jLabel58;

    private javax.swing.JLabel jLabel59;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLbImmeuble;

    private javax.swing.JLabel jLbLot;

    private javax.swing.JLabel jLbProprietaire;

    private javax.swing.JLabel jLbTypeBail;

    private javax.swing.JLabel jLbTypeBailCode;

    private javax.swing.JPanel jPComptabilite;

    private javax.swing.JPanel jPGaranties;

    private javax.swing.JPanel jPInfosAdresse;

    private javax.swing.JPanel jPInfosInfos;

    private javax.swing.JPanel jPInfosRevenus;

    private javax.swing.JPanel jPanel13;

    private javax.swing.JPanel jPanelArrieres;

    private javax.swing.JPanel jPanelBail;

    private javax.swing.JPanel jPanelFicheFinanciere;

    private javax.swing.JPanel jPanelHistorique;

    private javax.swing.JPanel jPanelInformations;

    private javax.swing.JPanel jPanelNotes;

    private javax.swing.JPanel jPanelRelances;

    private javax.swing.JRadioButton jRBVerseAgence;

    private javax.swing.JRadioButton jRBVerseProprietaire;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane10;

    private javax.swing.JScrollPane jScrollPane11;

    private javax.swing.JScrollPane jScrollPane12;

    private javax.swing.JScrollPane jScrollPane13;

    private javax.swing.JScrollPane jScrollPane14;

    private javax.swing.JScrollPane jScrollPane15;

    private javax.swing.JScrollPane jScrollPane16;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JScrollPane jScrollPane6;

    private javax.swing.JScrollPane jScrollPane7;

    private javax.swing.JScrollPane jScrollPane8;

    private javax.swing.JScrollPane jScrollPane9;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator10;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JSeparator jSeparator4;

    private javax.swing.JSeparator jSeparator5;

    private javax.swing.JSeparator jSeparator6;

    private javax.swing.JSeparator jSeparator7;

    private javax.swing.JSeparator jSeparator8;

    private javax.swing.JSeparator jSeparator9;

    private javax.swing.JTextArea jTAComment;

    private javax.swing.JTextArea jTANotes;

    private javax.swing.JTextArea jTARGAdresseEmloyeur;

    private javax.swing.JToggleButton jTBLock;

    private javax.swing.JTextField jTFAddrAdresse1;

    private javax.swing.JTextField jTFAddrAdresse2;

    private javax.swing.JTextField jTFAddrCodePostal;

    private javax.swing.JTextField jTFAddrEmail;

    private javax.swing.JTextField jTFAddrFax;

    private javax.swing.JTextField jTFAddrMobile;

    private javax.swing.JTextField jTFAddrTel;

    private javax.swing.JTextField jTFAddrVille;

    private javax.swing.JTextField jTFBailDuree;

    private javax.swing.JTextField jTFForfait;

    private javax.swing.JTextField jTFFrequence;

    private javax.swing.JTextField jTFNom;

    private javax.swing.JTextField jTFPrenom;

    private javax.swing.JTextField jTFRB1;

    private javax.swing.JTextField jTFRGBIC;

    private javax.swing.JTextField jTFRGBICConjoint;

    private javax.swing.JTextField jTFRGDateEmbauche;

    private javax.swing.JTextField jTFRGEmployeur;

    private javax.swing.JTextField jTFRGMoyenneNetteMensuelle;

    private javax.swing.JTextField jTFRGMoyenneNetteMensuelleConjoint;

    private javax.swing.JTextField jTFRGNomDeJeuneFille;

    private javax.swing.JTextField jTFRGNonCommercial;

    private javax.swing.JTextField jTFRGNonCommercialConjoint;

    private javax.swing.JTextField jTFRGPensions;

    private javax.swing.JTextField jTFRGPensionsConjoint;

    private javax.swing.JTextField jTFRGRevenusFonciers;

    private javax.swing.JTextField jTFRGRevenusFonciersConjoint;

    private javax.swing.JTextField jTFRGRevenusGerants;

    private javax.swing.JTextField jTFRGRevenusGerantsConjoint;

    private javax.swing.JTextField jTFRGSalairesNets;

    private javax.swing.JTextField jTFRGSalairesNetsConjoint;

    private javax.swing.JTextField jTFRIB2;

    private javax.swing.JTextField jTFReference;

    private javax.swing.JTabbedPane jTPInfos;

    private javax.swing.JTabbedPane jTabbedPaneMain;

    private javax.swing.JTable jTbArrieres;

    private javax.swing.JTable jTbBrouillards;

    private javax.swing.JTable jTbCoLodgers;

    private javax.swing.JTable jTbContratsGaranties;

    private javax.swing.JTable jTbEcritures;

    private javax.swing.JTable jTbEcrituresNonValides;

    private javax.swing.JTable jTbFinanicalFile;

    private javax.swing.JTable jTbGarants;

    private javax.swing.JTable jTbHisto;

    private javax.swing.JTable jTbNotes;

    private javax.swing.JTable jTbRappel;

    private javax.swing.JTable jTbRelance;

    private javax.swing.JTable jTbRelanceAFaire;

    public void display(LKGLodger lodger) {
        m_Lodger = lodger;
        display();
    }

    private void display() {
        this.jTFPrenom.setText(m_Lodger.getFirstName());
        this.jTFNom.setText(m_Lodger.getLastName());
        this.jTFReference.setText(m_Lodger.getReference());
        if (m_Lodger.getLot() != null) this.jLbLot.setText(m_Lodger.getLot().getName());
        if (m_Lodger.getOwner() != null) this.jLbProprietaire.setText(m_Lodger.getOwner().getString());
        this.jCBCivilites.setSelectedIndex(m_Lodger.getInfCivilite());
        if (m_Lodger.getBailType() != null) {
            if (jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGTypeBail.class.getName()).containsKey(m_Lodger.getBailType())) {
                LKGTypeBail typeBail = (LKGTypeBail) jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGTypeBail.class.getName()).get(m_Lodger.getBailType());
                this.jLbTypeBail.setText(typeBail.toString());
            }
        }
        initDateChoosers();
        if (m_Lodger.getBailDateSignature() != null) this.jDateChooserDateEntree.setDate(m_Lodger.getBailDateSignature());
        if (m_Lodger.getBailDateDepart() != null) this.jDateChooserDateEntree.setDate(m_Lodger.getBailDateDepart());
        if (m_Lodger.getBailDateEcheance() != null) this.jDateChooserDateEcheance.setDate(m_Lodger.getBailDateEcheance());
        if (m_Lodger.getBailDateProchaineRevision() != null) this.jDateChooserProchaineRevision.setDate(m_Lodger.getBailDateProchaineRevision());
        if (m_Lodger.getBailDernierIndice() != null) {
            if (jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGBailIndice.class.getName()).containsKey(m_Lodger.getBailDernierIndice())) {
                LKGBailIndice bailIndice = (LKGBailIndice) jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGBailIndice.class.getName()).get(m_Lodger.getBailDernierIndice());
                jCBDernierIndice.setSelectedItem(bailIndice);
            }
        }
        this.jTFBailDuree.setText(String.valueOf(m_Lodger.getBailDuree()));
        if (m_Lodger.getBailIndiceEntree() != null) {
            if (jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGBailIndice.class.getName()).containsKey(m_Lodger.getBailIndiceEntree())) {
                LKGBailIndice bailIndice = (LKGBailIndice) jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGBailIndice.class.getName()).get(m_Lodger.getBailIndiceEntree());
                jCBIndiceEntree.setSelectedItem(bailIndice);
            }
        }
        if (m_Lodger.getBailFrequenceRevision() != null) {
            if (jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGFrequenceRevision.class.getName()).containsKey(m_Lodger.getBailFrequenceRevision())) {
                LKGFrequenceRevision frequenceRevision = (LKGFrequenceRevision) jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGFrequenceRevision.class.getName()).get(m_Lodger.getBailFrequenceRevision());
                jCBFrequenceRevision.setSelectedItem(frequenceRevision);
            }
        }
        this.jCBExigible.setSelectedIndex(m_Lodger.getQuittExigibilite());
        this.jChBEnvoiQuittance.setSelected(m_Lodger.isQuittEnvoiQuittance());
        this.jChBEnvoiAvisQuittancement.setSelected(m_Lodger.is_QuittEnvoiAvisQuittance());
        this.jTFFrequence.setText(String.valueOf(m_Lodger.getQuittFrequence()));
        if (m_Lodger.getQuittDate1erQuittancement() != null) {
            this.jDateChooser1erQuittancement.setDate(m_Lodger.getQuittDate1erQuittancement());
        }
        if (m_Lodger.getQuittProchainQuittancement() != null) {
            this.jDateChooserProchainQuittancement.setDate(m_Lodger.getQuittProchainQuittancement());
        }
        this.displayFinancialFile();
        if (m_Lodger.isFiCautionVerseProprio()) {
            this.jRBVerseProprietaire.setSelected(true);
        } else {
            this.jRBVerseAgence.setSelected(true);
        }
        this.jDateChooserDateNaissance.setDate(this.m_Lodger.getRgDateNaissance());
        this.jTFRGNomDeJeuneFille.setText(this.m_Lodger.getRgNomJeuneFille());
        if (this.m_Lodger.getRgEmploi() != null) {
            if (jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGEmploi.class.getName()).containsKey(m_Lodger.getRgEmploi())) {
                LKGEmploi obj = (LKGEmploi) jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGEmploi.class.getName()).get(m_Lodger.getRgEmploi());
                jCBRGEmploi.setSelectedItem(obj);
            }
        }
        if (this.m_Lodger.getRgNatureContrat() != null) {
            if (jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGNatureEmploi.class.getName()).containsKey(m_Lodger.getRgEmploi())) {
                LKGNatureEmploi obj = (LKGNatureEmploi) jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGNatureEmploi.class.getName()).get(m_Lodger.getRgEmploi());
                jCBRGNatureContrat.setSelectedItem(obj);
            }
        }
        this.jTFRGDateEmbauche.setText(Utilities.getShortDate((java.util.Date) this.m_Lodger.getRgDateEmbauche()));
        this.jTFRGEmployeur.setText(this.m_Lodger.getRgEmployeur());
        this.jTFRGBIC.setText(this.m_Lodger.getRgBIC());
        this.jTFRGBICConjoint.setText(this.m_Lodger.getRgBicConjoint());
        this.jTFRGMoyenneNetteMensuelle.setText(String.valueOf(this.m_Lodger.getRgMoyenneMensuellesNet()));
        this.jTFRGNonCommercial.setText(String.valueOf(this.m_Lodger.getRgRevenusNonCommerciaux()));
        this.jTFRGNonCommercialConjoint.setText(String.valueOf(this.m_Lodger.getRgRevenusNonCommConjoint()));
        this.jTFRGPensions.setText(String.valueOf(this.m_Lodger.getRgPensions()));
        this.jTFRGPensionsConjoint.setText(String.valueOf(this.m_Lodger.getRgPensionsConjoints()));
        this.jTFRGRevenusFonciers.setText(String.valueOf(this.m_Lodger.getRgRevenusFonciers()));
        this.jTFRGRevenusFonciersConjoint.setText(String.valueOf(this.m_Lodger.getRgRevenusFonciersConjoint()));
        this.jTFRGRevenusGerants.setText(String.valueOf(this.m_Lodger.getRgRevenusGerant()));
        this.jTFRGRevenusGerantsConjoint.setText(String.valueOf(this.m_Lodger.getRgRevenusGerantConjoint()));
        this.jTFRGSalairesNets.setText(String.valueOf(this.m_Lodger.getRgSalairesNets()));
        this.jTFRGSalairesNetsConjoint.setText(String.valueOf(this.m_Lodger.getRgSalairesNetsConjoint()));
        if (this.m_Lodger.getAddress() != null) {
            this.jTFAddrEmail.setText(this.m_Lodger.getAddress().getEMail());
            this.jTFAddrFax.setText(this.m_Lodger.getAddress().getFax());
            this.jTFAddrMobile.setText(this.m_Lodger.getAddress().getMobile());
            this.jTFAddrTel.setText(this.m_Lodger.getAddress().getTel());
            this.jTFAddrAdresse1.setText(this.m_Lodger.getAddress().getAdresse1());
            this.jTFAddrAdresse2.setText(this.m_Lodger.getAddress().getAdresse2());
            this.jTFAddrVille.setText(this.m_Lodger.getAddress().getVille());
            this.jTFAddrCodePostal.setText(this.m_Lodger.getAddress().getCodePostal());
        }
        this.displayCoLodgers();
        this.displayGarants();
        this.displayContratsGaranties();
        this.displayFog();
        this.DisplaySolde();
        this.DisplayHistorique();
        this.DisplayArrieres();
        this.DisplayRelancesAFaire();
        this.DisplayRelances();
        this.DisplayRappels();
    }

    public void initDateChoosers() {
        this.jDateChooser1erQuittancement.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserDateDepart.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserDateEcheance.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserDateEntree.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserDateNaissance.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserDebutPeriodeHisto.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserFinForfait.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserFinPeriodeHisto.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserProchainQuittancement.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserProchaineRevision.setDateFormatString(Utilities.getSimpleDateFormatString());
    }

    public void initComboBoxes() {
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGBailIndice.class.getName()).values().toArray());
        this.jCBIndiceEntree.setModel(comboModel);
        comboModel = new DefaultComboBoxModel(jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGBailIndice.class.getName()).values().toArray());
        this.jCBDernierIndice.setModel(comboModel);
        comboModel = new DefaultComboBoxModel(jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGFrequenceRevision.class.getName()).values().toArray());
        this.jCBFrequenceRevision.setModel(comboModel);
        comboModel = new DefaultComboBoxModel(jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGNatureEmploi.class.getName()).values().toArray());
        this.jCBRGEmploi.setModel(comboModel);
        comboModel = new DefaultComboBoxModel(jlokg.engine.JLoKGEngine.getJLoKGEngine().getInfCivilites());
        this.jCBCivilites.setModel(comboModel);
    }

    private void displayFinancialFile() {
        LKGFinancialFile financialFile = this.m_Lodger.getFinancialFile();
        final List<LKGFinancialFileLine> linesList = financialFile.getLines();
        AbstractTableModel model = new AbstractTableModel() {

            LKGFinancialFileLine[] lines;

            public int getColumnCount() {
                return 8;
            }

            public int getRowCount() {
                if (lines == null) refreshData();
                return lines.length;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (lines == null) refreshData();
                switch(columnIndex) {
                    case -2:
                        return lines[rowIndex];
                    case -1:
                        return lines[rowIndex];
                    case 0:
                        return lines[rowIndex].getNumber();
                    case 1:
                        return lines[rowIndex].getTitle();
                    case 2:
                        return lines[rowIndex].getHonoraire();
                    case 3:
                        return lines[rowIndex].getConserve();
                    case 4:
                        return lines[rowIndex].getSystematique();
                    case 5:
                        return lines[rowIndex].getAugmentable();
                    case 6:
                        return lines[rowIndex].getAccount();
                    case 7:
                        return lines[rowIndex].getMontant();
                }
                return "";
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }

            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "No";
                    case 1:
                        return "Nom Rubrique";
                    case 2:
                        return "%Hono";
                    case 3:
                        return "Cons";
                    case 4:
                        return "Syst";
                    case 5:
                        return "Augm";
                    case 6:
                        return "Compte";
                    case 7:
                        return "Montant";
                }
                return "";
            }

            public boolean isCellEditable(int row, int column) {
                return true;
            }

            public void setValueAt(Object value, int row, int column) {
                switch(column) {
                    case 0:
                        lines[row].setNumber((Integer) value);
                        break;
                    case 1:
                        lines[row].setTitle((String) value);
                        break;
                    case 2:
                        lines[row].setHonoraire((Double) value);
                        break;
                    case 3:
                        lines[row].setConserve((Boolean) value);
                        break;
                    case 4:
                        lines[row].setSystematique((Boolean) value);
                        break;
                    case 5:
                        lines[row].setAugmentable((Boolean) value);
                        break;
                    case 6:
                        lines[row].setAccount((String) value);
                        break;
                    case 7:
                        lines[row].setMontant((Double) value);
                        break;
                }
            }

            public final void fireTableDataChanged() {
                refreshData();
                super.fireTableDataChanged();
            }

            public void refreshData() {
                lines = linesList.toArray(new LKGFinancialFileLine[linesList.size()]);
            }
        };
        jTbFinanicalFile.setModel(model);
    }

    public void displayCoLodgers() {
        final List<LKGCoLodger> coLodgerList = m_Lodger.getCoLocataires();
        AbstractTableModel model = new AbstractTableModel() {

            LKGCoLodger[] coLodgers;

            public int getColumnCount() {
                return 1;
            }

            public int getRowCount() {
                if (coLodgers == null) refreshData();
                return coLodgers.length;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (coLodgers == null) refreshData();
                switch(columnIndex) {
                    case -2:
                        return coLodgers[rowIndex];
                    case -1:
                        return coLodgers[rowIndex];
                    case 0:
                        return coLodgers[rowIndex].getString();
                }
                return "";
            }

            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Colocataire";
                }
                return "";
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public final void fireTableDataChanged() {
                refreshData();
                super.fireTableDataChanged();
            }

            public void refreshData() {
                coLodgers = coLodgerList.toArray(new LKGCoLodger[coLodgerList.size()]);
            }
        };
        jTbCoLodgers.setModel(model);
    }

    public void displayGarants() {
        final List<LKGGarant> garantsList = m_Lodger.getGarants();
        AbstractTableModel model = new AbstractTableModel() {

            LKGGarant[] garants;

            public int getColumnCount() {
                return 2;
            }

            public int getRowCount() {
                if (garants == null) refreshData();
                return garants.length;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (garants == null) refreshData();
                switch(columnIndex) {
                    case -2:
                        return garants[rowIndex];
                    case -1:
                        return garants[rowIndex];
                    case 0:
                        return garants[rowIndex].getString();
                    case 1:
                        return garants[rowIndex].getPhone();
                }
                return "";
            }

            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Garant";
                }
                return "";
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public final void fireTableDataChanged() {
                refreshData();
                super.fireTableDataChanged();
            }

            public void refreshData() {
                garants = garantsList.toArray(new LKGGarant[garantsList.size()]);
            }
        };
        jTbGarants.setModel(model);
    }

    public void displayContratsGaranties() {
        final List<LKGLocContratGarantie> contratsGarantiesList = m_Lodger.getContratsGaranties();
        AbstractTableModel model = new AbstractTableModel() {

            LKGLocContratGarantie[] contratsGaranties;

            public int getColumnCount() {
                return 2;
            }

            public int getRowCount() {
                if (contratsGaranties == null) refreshData();
                return contratsGaranties.length;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                if (contratsGaranties == null) refreshData();
                switch(columnIndex) {
                    case -2:
                        return contratsGaranties[rowIndex];
                    case -1:
                        return contratsGaranties[rowIndex];
                    case 0:
                        return contratsGaranties[rowIndex].getString();
                    case 1:
                        return Utilities.getShortDateFormat().format(new Date(contratsGaranties[rowIndex].getDateEcheance().getTime()));
                }
                return "";
            }

            public String getColumnName(int columnIndex) {
                switch(columnIndex) {
                    case 0:
                        return "Contrats et Garanties";
                }
                return "";
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public final void fireTableDataChanged() {
                refreshData();
                super.fireTableDataChanged();
            }

            public void refreshData() {
                contratsGaranties = contratsGarantiesList.toArray(new LKGLocContratGarantie[contratsGarantiesList.size()]);
            }
        };
        jTbContratsGaranties.setModel(model);
    }

    public void displayFog() {
        String sql;
        LKGFogFacade facade = new LKGFogFacade();
        if (jCBTypeBrouillards.getSelectedIndex() == 0) {
            sql = facade.getSQLForFogListWithLodger(m_Lodger.getReference(), false, "");
        } else {
            sql = facade.getSQLForFogListWithLodger(m_Lodger.getReference(), true, "");
        }
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 3;
                }
            };
            this.jTbBrouillards.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplayAccountFogLines() {
        LKGAccountLineFacade facade = new LKGAccountLineFacade();
        String fogCode = (String) this.jTbBrouillards.getModel().getValueAt(this.jTbBrouillards.getSelectedRow(), -3);
        String sql = facade.getSQLForFogWithLodger(fogCode, this.m_Lodger.getReference());
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 4;
                }
            };
            this.jTbEcrituresNonValides.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplaySolde() {
        LKGAccountLineFacade facade = new LKGAccountLineFacade();
        String sql = facade.getSQLForCurrentSoldeWithLodger(this.m_Lodger.getReference());
        System.out.println("DisplaySolde >> " + sql);
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 3;
                }
            };
            this.jTbEcritures.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplayHistorique() {
        LKGAccountLineFacade facade = new LKGAccountLineFacade();
        Date debut = jDateChooserDebutPeriodeHisto.getDate();
        Date fin = jDateChooserFinPeriodeHisto.getDate();
        String sql = facade.getSQLForHistorique(this.m_Lodger.getReference(), debut, fin);
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 1;
                }
            };
            this.jTbHisto.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplayArrieres() {
        LKGAccountLineFacade facade = new LKGAccountLineFacade();
        String sql = facade.getSQLForArrieresWithLodger(this.m_Lodger.getReference());
        System.out.println("DisplayArrieres >> " + sql);
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 1;
                }
            };
            this.jTbArrieres.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplayRelancesAFaire() {
        LKGLocContratGarantieFacade facade = new LKGLocContratGarantieFacade();
        String sql = facade.getSQLForLodgerWithDate(this.m_Lodger.getCode(), new Date());
        System.out.println("DisplayRelanceAFaire >> " + sql);
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 1;
                }
            };
            this.jTbRelanceAFaire.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplayRelances() {
        LKGLocRelanceFacade facade = new LKGLocRelanceFacade();
        String sql = facade.getSQLForLodger(this.m_Lodger.getCode());
        System.out.println("DisplayRelances >> " + sql);
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 1;
                }
            };
            this.jTbRelance.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void DisplayRappels() {
        LKGLocRappelFacade facade = new LKGLocRappelFacade();
        String sql = facade.getSQLForLodger(this.m_Lodger.getCode());
        System.out.println("DisplayRappels >> " + sql);
        Statement statement;
        SimpleSQLStatement sqlStatement;
        SimpleSQLStatementTableModel tableModel;
        try {
            statement = jlokg.JLoKGHibernateUtil.currentSession().connection().createStatement();
            sqlStatement = new SimpleSQLStatement(statement, sql);
            tableModel = new SimpleSQLStatementTableModel(sqlStatement) {

                public int getColumnShift() {
                    return 1;
                }
            };
            this.jTbRappel.setModel(tableModel);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addFinancialFileLine() {
        final LKGJDChooseRubrikUI chooseRubrikUI = new LKGJDChooseRubrikUI(this, true);
        chooseRubrikUI.display();
        chooseRubrikUI.getJBtSelect().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LKGRubrik rubrik = (LKGRubrik) chooseRubrikUI.getJCBRubrik().getSelectedItem();
                System.out.println("rubrik : " + rubrik);
                LKGFinancialFileLine finFileLine = new LKGFinancialFileLine();
                finFileLine.setAccount(rubrik.getAccount());
                finFileLine.setAugmentable(rubrik.getAugmentable());
                finFileLine.setConserve(rubrik.getConserve());
                finFileLine.setHonoraire(rubrik.getHonorary());
                finFileLine.setNumber(rubrik.getNbr());
                finFileLine.setTitle(rubrik.getLabel());
                finFileLine.setSystematique(rubrik.getSystematic());
                finFileLine.setMasterFinFile(m_Lodger.getFinancialFile().getCode());
                m_Lodger.getFinancialFile().getLines().add(finFileLine);
                AbstractTableModel model = (AbstractTableModel) jTbFinanicalFile.getModel();
                model.fireTableDataChanged();
            }
        });
        chooseRubrikUI.setVisible(true);
    }

    public void updateLodger() {
        m_Lodger.setLastName(this.jTFNom.getText());
        m_Lodger.setFirstName(this.jTFPrenom.getText());
    }

    public boolean controlInput() {
        return true;
    }
}
