package org.fudaa.fudaa.sipor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.fudaa.fudaa.ressource.FudaaResource;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuDialogMessage;
import com.memoire.bu.BuDialogWarning;
import com.memoire.fu.FuLog;

/**
 * Panel de saisie des Navire Utilise un systeme d'onglet pour pouvoir saisir la totalit des donnes relative aux navires
 * 
 * @author Adrien Hadoux
 */
public class SiporPanelSaisieNavires extends JPanel {

    /**
	 * Donnees de la simulation
	 */
    SiporDataSimulation donnees_;

    /**
	 * Panel des Informations Gnrales
	 */
    JPanel panelDonneesGenerales_ = new JPanel();

    static int nbouverture = 0;

    String[] tabloi_ = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

    String[] tabh_ = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24" };

    String[] tabm_ = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };

    String[] listeHoraires;

    SiporFrameSaisieLoiJournaliere fenetreLoiJournaliere_ = null;

    SiporFrameSaisieLoiDeterministe fenetreLoideter_ = null;

    SiporTextField dgNom_ = new SiporTextField(10);

    /**
	 * loi de generation des navires
	 */
    String[] choixLoi_ = { "Erlang", "Deterministe", "Journaliere" };

    JComboBox choixLoiGenerationNav_ = new JComboBox(choixLoi_);

    SiporTextFieldInteger nbBateauxAttendus_ = new SiporTextFieldInteger(3);

    SiporTextFieldFloat ecartMoyenEntre2Arrivees_ = new SiporTextFieldFloat(3);

    JComboBox loiGenerationNavErlang_ = new JComboBox(tabloi_);

    /**
	 * variable contenant le tableau des couples pour la loi deterministe
	 */
    ArrayList loiDeterministe_ = new ArrayList();

    String[] choixPriorite_ = { "normale", "forte" };

    JComboBox dgPriorite_ = new JComboBox(choixPriorite_);

    JComboBox dgGareDepart = new JComboBox();

    ;

    SiporTextFieldFloat dgLongMin_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat dgLongMoy_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat dgLargMin_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat dgLargMax_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat dgLongMax_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat dgTirantEntree_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat dgTirantSortie_ = new SiporTextFieldFloat(3);

    JButton dgSuivant_ = new JButton("Etape Suivante");

    final BuButton creneauG_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_continuer"), "");

    final BuButton creneauPM_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_continuer"), "");

    /**
	 * Panel de la liste des quais accessibles par oredre preferentiel
	 */
    JPanel panelListeQuais_ = new JPanel();

    JComboBox lqQuaiPref1_;

    JComboBox lqQuaiPref2_;

    JComboBox lqQuaiPref3_;

    SiporTextFieldFloat lqDuree1_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat lqDuree2_ = new SiporTextFieldFloat(3);

    SiporTextFieldFloat lqDuree3_ = new SiporTextFieldFloat(3);

    /**
	 * Panel des operations de chargement, dchargement
	 */
    JPanel panelOperationsCD_ = new JPanel();

    /**
	 * Panels qui vont etre cre selon le choix de l'utilisateur
	 */
    JInternalFrame modeLoi_, modeShift_;

    JRadioButton cdShift = new JRadioButton("Mode Shift");

    JRadioButton cdLoi = new JRadioButton("Mode Loi de service");

    BuButton pshift_ = new BuButton("Parametres...");

    BuButton ploi_ = new BuButton("Parametres...");

    JLabel messNbaviresAttendus = new JLabel("Nb Navires attendus sur la duree de la simulation:");

    String[] truc = { "mega top", "merde", "oups" };

    JComboBox cdHoraires_ = new JComboBox();

    JCheckBox choixHorairesNavires_ = new JCheckBox("");

    JButton cdRevoirHoraires_ = new JButton("?");

    JComboBox cdDureeProchainShiftheure_ = new JComboBox(tabh_);

    JComboBox cdDureeProchainShiftminute_ = new JComboBox(tabm_);

    SiporTextFieldDuree cdDureeProchainShift = new SiporTextFieldDuree(3);

    SiporTextFieldFloat cdTonnageMin_ = new SiporTextFieldFloat(5);

    SiporTextFieldFloat cdTonnageMax_ = new SiporTextFieldFloat(5);

    SiporTextFieldFloat cdCadenceQuaiPref_ = new SiporTextFieldFloat(4);

    SiporTextFieldFloat cdCadenceAutreQuai_ = new SiporTextFieldFloat(4);

    final BuButton cdvalidationShift_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "valider");

    final BuButton cdvalidationLoi_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "valider");

    final BuButton creneau_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_continuer"), "saisir horaire");

    SiporTextFieldDuree cdDureeServiceMinQuaiPref_ = new SiporTextFieldDuree(4);

    SiporTextFieldDuree cdDureeServiceMaxQuaiPref_ = new SiporTextFieldDuree(4);

    SiporTextFieldDuree cdDureeServiceMinAutreQuai_ = new SiporTextFieldDuree(4);

    SiporTextFieldDuree cdDureeServiceMaxAutreQuai_ = new SiporTextFieldDuree(4);

    /**
	 * Bouton de validation de la saisie de la cagorie de navire
	 */
    final BuButton validerNavire_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "valider");

    String chtemp_;

    /**
	 * Panel principal de gestion des navires c est a partir de ce panel que l on peut effectuer toutes les operations de
	 * mise a jour d affichage par exemple.
	 */
    SiporVisualiserNavires MENUNAVIRES_;

    /**
	 * BOOLEEN qui precise si le panel de saisie d'une catégorie de navire est en mode ajout ou en mode modification si
	 * il est en mode modification: le booleen est a true sinon il est a false par defaut le mode modification est a false
	 * donc en mode saisie classique
	 */
    boolean MODE_MODIFICATION_ON_ = false;

    /**
	 * indice dans la liste des quais du quai a modifier dans le cas ou l on est en mode modificaiton
	 */
    int NAVIRE_A_MODIFIER_;

    /**
	 * Horaire qui sera saisie par l'interface de saisie des horaires
	 */
    SiporHoraire horaire_ = new SiporHoraire();

    SiporHoraire horaireG_ = new SiporHoraire();

    SiporHoraire horairePM_ = new SiporHoraire();

    Color vert = new Color(241, 241, 241);

    Color rouge = new Color(157, 194, 143);

    private boolean premierAvertissment = true;

    /**
	 * Constructeur du Panel de saisie:
	 */
    SiporPanelSaisieNavires(final SiporDataSimulation d, final SiporVisualiserNavires vn) {
        donnees_ = d;
        MENUNAVIRES_ = vn;
        nbouverture++;
        dgNom_.setToolTipText("Veuillez entrer le Nom qui correspond  la catgorie de Navires");
        this.dgPriorite_.setToolTipText("Veuillez choisir le degr de priorit de cette catgorie de Navire");
        this.dgLongMin_.setToolTipText("NOMBRE ENTIER correspondant  la taille minimale de cette catgorie");
        this.dgLongMax_.setToolTipText("NOMBRE Reel correspondant  la taille maximale de cette catgorie \n ATTENTION: doit etre superieur  la taille Minimale");
        this.dgLargMin_.setToolTipText("NOMBRE ENTIER correspondant  la largeur minimale de cette catgorie");
        this.dgLargMax_.setToolTipText("NOMBRE Reel correspondant  la largeur maximale de cette catgorie \n ATTENTION: doit etre superieur  la taille Minimale");
        this.dgTirantEntree_.setToolTipText("NOMBRE REEL en mtres correspondant au tirant d'eau en entre dans le port");
        this.dgTirantSortie_.setToolTipText("NOMBRE REEL en mtres correspondant au tirant d'eau en sortie du port");
        this.lqQuaiPref1_ = new JComboBox();
        this.lqQuaiPref1_.setToolTipText("Choisissez le quai preferentiel ou Aucun si c'est le cas");
        this.lqQuaiPref2_ = new JComboBox();
        this.lqQuaiPref2_.setToolTipText("Choisissez le quai preferentiel ou Aucun si c'est le cas");
        this.lqQuaiPref3_ = new JComboBox();
        this.lqQuaiPref3_.setToolTipText("Choisissez le quai preferentiel ou Aucun si c'est le cas");
        this.majSaisie();
        this.lqDuree1_.setToolTipText("NOMBRE REEL en heures.minutes correspondant  la dure pendant laquelle ce qui reste prfrentiel numero 1.    Exemple: pour rentrer 8h36, tapez 8.36");
        this.lqDuree2_.setToolTipText("NOMBRE REEL en heures.minutes correspondant  la dure pendant laquelle ce qui reste prfrentiel numero 2.    Exemple: pour rentrer 8h36, tapez 8.36");
        this.lqDuree3_.setToolTipText("NOMBRE REEL en heures.minutes correspondant  la dure pendant laquelle ce qui reste prfrentiel numero 3.    Exemple: pour rentrer 8h36, tapez 8.36");
        this.cdDureeProchainShift.setToolTipText("temps residuel de traitement avant transfert au shift suivant");
        this.cdTonnageMin_.setToolTipText("NOMBRE REEL en tonnes correspondant au tonnage minimum.    Exemple:pour rentrer 8000 tonnes, tapez 8000");
        this.cdTonnageMax_.setToolTipText("NOMBRE REEL en tonnes correspondant au tonnage maximum.    Exemple:pour rentrer 8000 tonnes, tapez 8000");
        this.cdCadenceQuaiPref_.setToolTipText("NOMBRE REEL  correspondant  la cadence prfrentielle en tonnes par heures T/H.");
        this.cdCadenceAutreQuai_.setToolTipText("NOMBRE REEL  correspondant a la cadence preferentielle en tonnes par heures T/H.");
        this.cdHoraires_.setToolTipText("L'ensemble des horaires saisis prcdemment sont repertoris ici");
        this.cdDureeProchainShiftheure_.setToolTipText("choisissez la dure d'attente avant le prochain Shift");
        this.cdDureeProchainShiftminute_.setToolTipText("choisissez la dure d'attente avant le prochain Shift");
        this.cdvalidationShift_.setToolTipText("Validez les valeurs saisies via ce bouton");
        this.cdDureeServiceMinQuaiPref_.setToolTipText("NOMBRE REEL  correspondant  la dure de service minimum du quai prfrentiel");
        this.cdDureeServiceMaxQuaiPref_.setToolTipText("NOMBRE REEL  correspondant  la dure de service maximum du quai prfrentiel");
        this.cdDureeServiceMaxAutreQuai_.setToolTipText("NOMBRE REEL  correspondant  la dure de service maximum des autres quais");
        this.cdDureeServiceMinAutreQuai_.setToolTipText("NOMBRE REEL  correspondant  la dure de service minimum des autres quais");
        this.cdvalidationShift_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                System.out.println("Fermeture de la fenetre de saisie du mode shift");
                modeShift_.dispose();
                System.out.println("le tonnage max est:" + cdTonnageMax_.getText());
            }
        });
        this.cdvalidationLoi_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                System.out.println("Fermeture de la fenetre de saisie du mode Loi");
                modeLoi_.dispose();
                System.out.println("la duree de service max au quai preferentiel est:" + cdTonnageMax_.getText());
            }
        });
        this.validerNavire_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                creationNavire();
            }
        });
        creneau_.setToolTipText("saisissez les horaires en cliquant sur ce bouton");
        this.creneau_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                donnees_.application_.addInternalFrame(new SiporFrameSaisieHorairesCompletSemaine(horaire_));
            }
        });
        creneauG_.setToolTipText("saisissez les cr�neaux journaliers en cliquant sur ce bouton");
        this.creneauG_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                donnees_.application_.addInternalFrame(new SiporFrameSaisieHorairesResume(horaireG_));
            }
        });
        creneauPM_.setToolTipText("saisissez les cr�neaux d'ouverture par rapport � la pleine mer, en cliquant sur ce bouton");
        this.creneauPM_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                donnees_.application_.addInternalFrame(new SiporFrameSaisieHoraires(horairePM_));
            }
        });
        choixLoiGenerationNav_.setToolTipText("veuillez choisir le type deloi de generation de navires ");
        this.choixLoiGenerationNav_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final int choixLoi = choixLoiGenerationNav_.getSelectedIndex();
                if (choixLoi == 0) {
                    messNbaviresAttendus.setForeground(Color.RED);
                    nbBateauxAttendus_.setEnabled(true);
                    loiGenerationNavErlang_.setEnabled(true);
                } else if (choixLoi == 1) {
                    messNbaviresAttendus.setForeground(Color.BLACK);
                    nbBateauxAttendus_.setEnabled(false);
                    loiGenerationNavErlang_.setEnabled(false);
                    if (fenetreLoideter_ == null) {
                        FuLog.debug("interface nulle");
                        fenetreLoideter_ = new SiporFrameSaisieLoiDeterministe(donnees_, loiDeterministe_, null);
                        fenetreLoideter_.setVisible(true);
                        donnees_.application_.addInternalFrame(fenetreLoideter_);
                    } else {
                        FuLog.debug("interface ferm�e");
                        if (fenetreLoideter_.isClosed()) {
                            donnees_.application_.addInternalFrame(fenetreLoideter_);
                        } else {
                            FuLog.debug("interface cas de figur restant autre que null et fermeture");
                            donnees_.application_.activateInternalFrame(fenetreLoideter_);
                            donnees_.application_.addInternalFrame(fenetreLoideter_);
                        }
                    }
                } else if (choixLoi == 2) {
                    messNbaviresAttendus.setForeground(Color.BLACK);
                    nbBateauxAttendus_.setEnabled(false);
                    loiGenerationNavErlang_.setEnabled(false);
                    if (fenetreLoiJournaliere_ == null) {
                        FuLog.debug("interface nulle");
                        fenetreLoiJournaliere_ = new SiporFrameSaisieLoiJournaliere(donnees_, loiDeterministe_, null);
                        fenetreLoiJournaliere_.setVisible(true);
                        donnees_.application_.addInternalFrame(fenetreLoiJournaliere_);
                    } else {
                        FuLog.debug("interface ferm�e");
                        if (fenetreLoiJournaliere_.isClosed()) {
                            donnees_.application_.addInternalFrame(fenetreLoiJournaliere_);
                        } else {
                            FuLog.debug("interface cas de figur restant autre que null et fermeture");
                            donnees_.application_.activateInternalFrame(fenetreLoiJournaliere_);
                            donnees_.application_.addInternalFrame(fenetreLoiJournaliere_);
                        }
                    }
                }
            }
        });
        ButtonGroup group = new ButtonGroup();
        group.add(cdShift);
        group.add(cdLoi);
        cdShift.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                ploi_.setEnabled(false);
                pshift_.setEnabled(true);
                modeShift_.setVisible(true);
                donnees_.application_.addInternalFrame(modeShift_);
            }
        });
        cdLoi.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                ploi_.setEnabled(true);
                pshift_.setEnabled(false);
                modeLoi_.setVisible(true);
                donnees_.application_.addInternalFrame(modeLoi_);
            }
        });
        pshift_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                modeShift_.setVisible(true);
                donnees_.application_.addInternalFrame(modeShift_);
            }
        });
        ploi_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                modeLoi_.setVisible(true);
                donnees_.application_.addInternalFrame(modeLoi_);
            }
        });
        choixHorairesNavires_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (choixHorairesNavires_.isSelected()) cdHoraires_.setEnabled(true); else cdHoraires_.setEnabled(false);
            }
        });
        cdHoraires_.setEnabled(false);
        final JPanel total = new JPanel();
        this.setBorder(SiporBordures.navire);
        total.setLayout(new BorderLayout());
        this.panelDonneesGenerales_.setLayout(new GridLayout(1, 1));
        final JPanel pdg1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pdg1.add(new JLabel("Nom de la categorie: "));
        pdg1.add(this.dgNom_);
        pdg1.add(new JLabel("Priorite: "));
        pdg1.add(this.dgPriorite_);
        pdg1.add(new JLabel("Gare depart: "));
        pdg1.add(this.dgGareDepart);
        this.panelDonneesGenerales_.add(pdg1);
        panelDonneesGenerales_.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "G�n�ral"));
        total.add(this.panelDonneesGenerales_, BorderLayout.NORTH);
        Box centre = Box.createVerticalBox();
        total.add(centre, BorderLayout.CENTER);
        JPanel panelDimension = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelDimension.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Dimensions"));
        centre.add(panelDimension);
        Box boxLongueurs = Box.createVerticalBox();
        boxLongueurs.setBorder(BorderFactory.createTitledBorder(SiporBordures.bordnormal_, "Longueurs", 0, 0, null, SiporImplementation.bleuClairSipor));
        panelDimension.add(boxLongueurs);
        final JPanel pdg21 = new JPanel();
        pdg21.add(new JLabel("Minimum: "));
        pdg21.add(this.dgLongMin_);
        pdg21.add(new JLabel("METRES	"));
        boxLongueurs.add(pdg21);
        final JPanel pdg22 = new JPanel();
        pdg22.add(new JLabel("Maximum: "));
        pdg22.add(this.dgLongMax_);
        pdg22.add(new JLabel("METRES	"));
        boxLongueurs.add(pdg22);
        Box boxLargeurs = Box.createVerticalBox();
        boxLargeurs.setBorder(BorderFactory.createTitledBorder(SiporBordures.bordnormal_, "Largeurs", 0, 0, null, SiporImplementation.bleuClairSipor));
        panelDimension.add(boxLargeurs);
        final JPanel pdg31 = new JPanel();
        pdg31.add(new JLabel("Minimum: "));
        pdg31.add(this.dgLargMin_);
        pdg31.add(new JLabel("METRES	"));
        boxLargeurs.add(pdg31);
        final JPanel pdg32 = new JPanel();
        pdg32.add(new JLabel("Maximum: "));
        pdg32.add(this.dgLargMax_);
        pdg32.add(new JLabel("METRES	"));
        boxLargeurs.add(pdg32);
        JPanel paneltirantQuai = new JPanel(new GridLayout(2, 1));
        centre.add(paneltirantQuai);
        JPanel tirantEau = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tirantEau.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Tirant d'eau"));
        paneltirantQuai.add(tirantEau);
        final JPanel plqa = new JPanel();
        plqa.add(new JLabel("Entree: "));
        plqa.add(this.dgTirantEntree_);
        plqa.add(new JLabel("METRES"));
        plqa.setBorder(SiporBordures.bordnormal_);
        tirantEau.add(plqa);
        final JPanel plqb = new JPanel();
        plqb.add(new JLabel("Sortie: "));
        plqb.add(this.dgTirantSortie_);
        plqb.add(new JLabel("METRES"));
        plqb.setBorder(SiporBordures.bordnormal_);
        tirantEau.add(plqb);
        JPanel Quai = new JPanel(new FlowLayout(FlowLayout.CENTER));
        Quai.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Quais preferentiels"));
        paneltirantQuai.add(Quai);
        final JPanel plq11 = new JPanel();
        plq11.add(new JLabel("Quai 1:"));
        plq11.add(lqQuaiPref1_);
        plq11.setBorder(SiporBordures.bordnormal_);
        Quai.add(plq11);
        final JPanel plq21 = new JPanel();
        plq21.add(new JLabel("Quai 2:"));
        plq21.add(lqQuaiPref2_);
        plq21.setBorder(SiporBordures.bordnormal_);
        Quai.add(plq21);
        final JPanel plq31 = new JPanel();
        plq31.add(new JLabel("Quai 3:"));
        plq31.add(lqQuaiPref3_);
        plq31.setBorder(SiporBordures.bordnormal_);
        Quai.add(plq31);
        JPanel creneauCharge = new JPanel(new GridLayout(1, 2));
        centre.add(creneauCharge);
        Box panelCreneaux = Box.createVerticalBox();
        panelCreneaux.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Creneaux"));
        creneauCharge.add(panelCreneaux);
        final JPanel plq771 = new JPanel();
        plq771.add(new JLabel("Creneaux horaire d'acc�s"));
        plq771.add(this.creneauG_);
        plq771.setBorder(SiporBordures.bordnormal_);
        panelCreneaux.add(plq771);
        final JPanel plq772 = new JPanel();
        plq772.add(new JLabel("Creneaux Pleine Mer"));
        plq772.add(this.creneauPM_);
        plq772.setBorder(SiporBordures.bordnormal_);
        panelCreneaux.add(plq772);
        JPanel panelCharge = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCharge.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Mode chargement/dechargement"));
        creneauCharge.add(panelCharge);
        Box boxcd = Box.createVerticalBox();
        panelCharge.add(boxcd);
        JPanel pshift = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pshift.add(cdShift);
        pshift.add(pshift_);
        boxcd.add(pshift);
        JPanel ploi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ploi.add(cdLoi);
        ploi.add(ploi_);
        boxcd.add(ploi);
        JPanel panelLoigeneration = new JPanel(new GridLayout(2, 1));
        panelLoigeneration.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Loi generation de navires"));
        centre.add(panelLoigeneration);
        final JPanel mscdtype3 = new JPanel();
        mscdtype3.add(new JLabel("type de loi:"));
        mscdtype3.add(this.choixLoiGenerationNav_);
        mscdtype3.add(new JLabel("Ordre loi d'Erlang:"));
        mscdtype3.add(this.loiGenerationNavErlang_);
        panelLoigeneration.add(mscdtype3);
        final JPanel mscdtype4 = new JPanel();
        messNbaviresAttendus = new JLabel("Nb Navires attendus sur la duree de la simulation:");
        messNbaviresAttendus.setForeground(Color.RED);
        mscdtype4.add(messNbaviresAttendus);
        mscdtype4.add(this.nbBateauxAttendus_);
        panelLoigeneration.add(mscdtype4);
        JPanel panelValidation = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centre.add(panelValidation);
        panelValidation.setBorder(SiporBordures.compound_);
        panelValidation.add(this.validerNavire_);
        modeShift_ = new JInternalFrame("", true, true, true, true);
        modeShift_.setBorder(SiporBordures.compound_);
        modeShift_.setSize(500, 340);
        modeShift_.setTitle("Mode de chargement dechargement Shift");
        modeShift_.getContentPane().setLayout(new BorderLayout());
        final JPanel msD = new JPanel();
        msD.add(new JLabel("Duree avant Shift suivant:"));
        msD.add(this.cdDureeProchainShift);
        msD.add(new JLabel("H.MIN"));
        msD.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "General"));
        modeShift_.getContentPane().add(msD, BorderLayout.NORTH);
        final JPanel msL = new JPanel();
        msL.add(cdvalidationShift_);
        msL.setBorder(SiporBordures.compound_);
        modeShift_.getContentPane().add(msL, BorderLayout.SOUTH);
        Box mscentre = Box.createVerticalBox();
        mscentre.setBorder(SiporBordures.compound_);
        modeShift_.getContentPane().add(mscentre, BorderLayout.CENTER);
        JPanel cdsh = new JPanel(new GridLayout(2, 1));
        cdsh.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Horaire de travail"));
        mscentre.add(cdsh);
        final JPanel mscentre1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mscentre1.add(new JLabel("Saisir:"));
        mscentre1.add(this.creneau_);
        cdsh.add(mscentre1);
        final JPanel mscentre2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mscentre2.add(new JLabel("Ou choisir dans la liste:"));
        mscentre2.add(choixHorairesNavires_);
        mscentre2.add(this.cdHoraires_);
        cdsh.add(mscentre2);
        JPanel cadenceTonnage = new JPanel(new GridLayout(1, 2));
        mscentre.add(cadenceTonnage);
        final JPanel msT = new JPanel(new GridLayout(2, 1));
        msT.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Tonnage"));
        cadenceTonnage.add(msT);
        final JPanel msT11 = new JPanel();
        msT11.add(new JLabel("Min:"));
        msT11.add(this.cdTonnageMin_);
        msT11.add(new JLabel("Tonnes"));
        msT11.setBorder(SiporBordures.bordnormal_);
        msT.add(msT11);
        final JPanel msT12 = new JPanel();
        msT12.add(new JLabel("Max:"));
        msT12.add(this.cdTonnageMax_);
        msT12.add(new JLabel("Tonnes"));
        msT12.setBorder(SiporBordures.bordnormal_);
        msT.add(msT12);
        final JPanel msC = new JPanel(new GridLayout(2, 1));
        msC.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Cadence aux quais"));
        cadenceTonnage.add(msC);
        final JPanel msC11 = new JPanel();
        msC11.add(new JLabel("Quai preferentiel:"));
        msC11.add(this.cdCadenceQuaiPref_);
        msC11.add(new JLabel("T/H"));
        msC11.setBorder(SiporBordures.bordnormal_);
        msC.add(msC11);
        final JPanel msC12 = new JPanel();
        msC12.add(new JLabel("Autres quais:"));
        msC12.add(this.cdCadenceAutreQuai_);
        msC12.add(new JLabel("T/H"));
        msC12.setBorder(SiporBordures.bordnormal_);
        msC.add(msC12);
        modeLoi_ = new JInternalFrame("", true, true, true, true);
        modeLoi_.setTitle("mode de chargement Loi de probabilite");
        modeLoi_.setSize(450, 260);
        final JPanel ml = new JPanel();
        ml.setLayout(new BorderLayout());
        ml.setBorder(SiporBordures.bordnormal_);
        final JPanel centreml = new JPanel();
        centreml.setLayout(new GridLayout(2, 1));
        centreml.setBorder(SiporBordures.bordnormal_);
        ml.add(centreml, BorderLayout.CENTER);
        Box box1 = Box.createVerticalBox();
        JPanel ml2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ml2.add(new JLabel("Minimum "));
        ml2.add(this.cdDureeServiceMinQuaiPref_);
        ml2.add(new JLabel("Heures.minutes"));
        JPanel ml22 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ml22.add(new JLabel("Maximum "));
        ml22.add(this.cdDureeServiceMaxQuaiPref_);
        ml22.add(new JLabel("Heures.minutes"));
        box1.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Duree de service au quai preferentiel"));
        box1.add(ml2);
        box1.add(ml22);
        centreml.add(box1);
        Box box2 = Box.createVerticalBox();
        final JPanel ml4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ml4.add(new JLabel("Minimum"));
        ml4.add(this.cdDureeServiceMinAutreQuai_);
        ml4.add(new JLabel("Heures.minutes"));
        final JPanel ml44 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ml44.add(new JLabel("Maximum"));
        ml44.add(this.cdDureeServiceMaxAutreQuai_);
        ml44.add(new JLabel("Heures.minutes"));
        box2.setBorder(BorderFactory.createTitledBorder(SiporBordures.compound_, "Duree de service aux autre quais"));
        box2.add(ml4);
        box2.add(ml44);
        centreml.add(box2);
        final JPanel pval = new JPanel();
        pval.add(new JLabel("validez la saisie: "));
        pval.add(cdvalidationLoi_);
        pval.setBorder(SiporBordures.compound_);
        ml.add(pval, BorderLayout.SOUTH);
        ml.setBorder(SiporBordures.compound_);
        modeLoi_.getContentPane().add(ml);
        modeLoi_.setBorder(SiporBordures.compound_);
        this.add(total);
        this.initialiser();
        this.dgNom_.requestFocus();
        this.dgNom_.selectAll();
    }

    /**
	 * Methode tres importante de mise a jour des donnes importantes telle que la liste des quais METHODE TRES PUISSANTE
	 * ET IMPORTANTE SI MODIFICATION DE AUTRE PARAMETRES ILS SERONT REMIS A JOUR POUR LA SAISIE DES NAVIRES VIA CETTE
	 * INTERFACE!!
	 */
    void majSaisie() {
        this.dgGareDepart.removeAllItems();
        for (int i = 0; i < this.donnees_.listeGare_.listeGares_.size(); i++) {
            this.dgGareDepart.addItem(this.donnees_.listeGare_.retournerGare(i));
        }
        for (int i = 0; i < this.donnees_.categoriesNavires_.listeNavires_.size(); i++) {
            if (i >= this.cdHoraires_.getItemCount() && donnees_.categoriesNavires_.retournerNavire(i).ModeChargement == 0) {
                this.cdHoraires_.addItem("horaire " + (donnees_.categoriesNavires_.retournerNavire(i)).nom);
            }
        }
        this.cdHoraires_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                if (cdHoraires_.getItemCount() != 0) {
                    final int var = cdHoraires_.getSelectedIndex();
                    final String selec = (String) cdHoraires_.getSelectedItem();
                    System.out.println("********dedans!! selection: " + var + "et selec=" + selec + " *********");
                    horaire_.recopie((SiporHoraire) donnees_.categoriesNavires_.retournerNavire(var).horaireTravailSuivi);
                }
            }
        });
        this.lqQuaiPref1_.removeAllItems();
        this.lqQuaiPref2_.removeAllItems();
        this.lqQuaiPref3_.removeAllItems();
        this.lqQuaiPref2_.addItem("aucun");
        this.lqQuaiPref3_.addItem("aucun");
        for (int i = 0; i < this.donnees_.lQuais_.lQuais_.size(); i++) {
            this.lqQuaiPref1_.addItem(((SiporQuais) this.donnees_.lQuais_.lQuais_.get(i)).Nom_);
            this.lqQuaiPref2_.addItem(((SiporQuais) this.donnees_.lQuais_.lQuais_.get(i)).Nom_);
            this.lqQuaiPref3_.addItem(((SiporQuais) this.donnees_.lQuais_.lQuais_.get(i)).Nom_);
        }
        this.lqQuaiPref1_.validate();
        this.lqQuaiPref2_.validate();
        this.lqQuaiPref3_.validate();
    }

    /**
	 * Methode qui controle toutes les donnes saisies pour dfinir une nouvelle catgorie de Navire
	 */
    boolean controle_creationNavire() {
        if (dgNom_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Nom manquant.").activate();
            return false;
        } else if (this.MODE_MODIFICATION_ON_ && donnees_.categoriesNavires_.existeDoublon(dgNom_.getText(), this.NAVIRE_A_MODIFIER_)) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Nom deja pris.").activate();
            return false;
        } else if (!this.MODE_MODIFICATION_ON_ && donnees_.categoriesNavires_.existeDoublon(dgNom_.getText(), -1)) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Nom deja pris.").activate();
            return false;
        }
        if (choixLoiGenerationNav_.getSelectedIndex() == 0) {
            if (this.nbBateauxAttendus_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Nombre de bateaux attendus manquant.").activate();
                return false;
            }
        }
        if (this.dgLongMin_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Longueur Minimum manquante.").activate();
            return false;
        } else if (this.dgLongMax_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Longueur maximum manquante.").activate();
            return false;
        } else {
            float a = Float.parseFloat(this.dgLongMin_.getText());
            float b = Float.parseFloat(this.dgLongMax_.getText());
            if (b < a) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Longueur maximum inf�rieure � minimum.").activate();
                return false;
            }
        }
        if (this.dgLargMax_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Largeur maximum manquante.").activate();
            return false;
        } else if (this.dgLargMin_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Largeur minimum manquante.").activate();
            return false;
        } else {
            float a = Float.parseFloat(this.dgLargMin_.getText());
            float b = Float.parseFloat(this.dgLargMax_.getText());
            if (b < a) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Largeur maximum inf�rieure � minimum").activate();
                return false;
            }
        }
        if (this.dgTirantEntree_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Tirant d eau en entree manquant").activate();
            return false;
        }
        if (this.dgTirantSortie_.getText().equals("")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Tirant d eau en sortie manquant.").activate();
            return false;
        }
        if (this.horaireG_.semaineCreneau1HeureArrivee == -1 || this.horaireG_.semaineCreneau1HeureDep == -1 || this.horaireG_.semaineCreneau2HeureArrivee == -1 || this.horaireG_.semaineCreneau2HeureDep == -1) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Cr�neaux journaliers non saisi correctement").activate();
            return false;
        }
        if (this.horairePM_.semaineCreneau1HeureArrivee == -1 && this.horairePM_.semaineCreneau1HeureDep == -1 && this.horairePM_.semaineCreneau2HeureArrivee == -1 && this.horairePM_.semaineCreneau2HeureDep == -1) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Cr�neaux d'ouverture par rapport a la pleine mer non saisi correctement").activate();
            return false;
        }
        if (this.cdShift.isSelected()) {
            System.out.print("mode shift choisi!!");
            if (this.cdDureeProchainShift.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Shift: duree avant shift suivant manquante").activate();
                donnees_.application_.addInternalFrame(modeShift_);
                return false;
            }
            if (this.cdTonnageMin_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Shift: Tonnage Minimum manquant.").activate();
                donnees_.application_.addInternalFrame(modeShift_);
                return false;
            } else if (this.cdTonnageMax_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Shift: Tonnage Mximum manquant.").activate();
                donnees_.application_.addInternalFrame(modeShift_);
                return false;
            } else {
                float a = Float.parseFloat(this.cdTonnageMin_.getText());
                float b = Float.parseFloat(this.cdTonnageMax_.getText());
                if (b < a) {
                    new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Shift: Tonnage Maximum inferieur au tonnage minimum.").activate();
                    donnees_.application_.addInternalFrame(modeShift_);
                    return false;
                }
            }
            if (this.cdCadenceQuaiPref_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Shift: Cadence quai preferentiel manquante.").activate();
                donnees_.application_.addInternalFrame(modeShift_);
                return false;
            }
            if (this.cdCadenceAutreQuai_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Shift: Cadence autres quais manquante.").activate();
                donnees_.application_.addInternalFrame(modeShift_);
                return false;
            }
            if (premierAvertissment) {
                float cad1 = Float.parseFloat(cdCadenceAutreQuai_.getText());
                float cad2 = Float.parseFloat(cdCadenceQuaiPref_.getText());
                if (cad2 < cad1) {
                    new BuDialogWarning(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Attention: \nLa cadence au quai pr�f�rentiel est inf�rieure\n � la cadence aux autres quais\nCeci n'est pas une erreur mais un avertissement. \nCliquez de nouveau sur le bouton de validation pour valider").activate();
                    premierAvertissment = false;
                    donnees_.application_.addInternalFrame(modeShift_);
                    return false;
                }
            }
            if ((!choixHorairesNavires_.isSelected()) && (this.horaire_.lundiCreneau1HeureArrivee == -1 || this.horaire_.lundiCreneau1HeureDep == -1 || this.horaire_.lundiCreneau2HeureArrivee == -1 || this.horaire_.lundiCreneau2HeureDep == -1)) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Cr�neaux du mode shift non saisi correctement.").activate();
                donnees_.application_.addInternalFrame(modeShift_);
                return false;
            }
        } else {
            System.out.print("mode loi choisi!!");
            if (this.cdDureeServiceMinQuaiPref_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre chargement :loi de probabilite: \nDuree de service minimum quai preferentiel manquant.").activate();
                donnees_.application_.addInternalFrame(modeLoi_);
                return false;
            } else if (this.cdDureeServiceMaxQuaiPref_.getText().equals("")) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre chargement :loi de probabilite: \nDuree de service maximum quai preferentiel manquant.").activate();
                donnees_.application_.addInternalFrame(modeLoi_);
                return false;
            } else {
                float a = Float.parseFloat(cdDureeServiceMinQuaiPref_.getText());
                float b = Float.parseFloat(cdDureeServiceMaxQuaiPref_.getText());
                if (b < a) {
                    new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Loi: dur�e service Maximum inferieur � la dur�e de service minimum.").activate();
                    donnees_.application_.addInternalFrame(modeLoi_);
                    return false;
                }
            }
            if (this.cdDureeServiceMinAutreQuai_.getText().equals("") && this.lqQuaiPref2_.getSelectedIndex() != 0) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre chargement :loi de probabilite:\n Duree de service minimum autres quais  manquant.").activate();
                donnees_.application_.addInternalFrame(modeLoi_);
                return false;
            } else if (this.cdDureeServiceMaxAutreQuai_.getText().equals("") && this.lqQuaiPref2_.getSelectedIndex() != 0) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre chargement :loi de probabilite: \nDuree de service maximum aux autres quais manquant.").activate();
                donnees_.application_.addInternalFrame(modeLoi_);
                return false;
            } else {
                float a = Float.parseFloat(cdDureeServiceMinAutreQuai_.getText());
                float b = Float.parseFloat(cdDureeServiceMaxAutreQuai_.getText());
                if (b < a) {
                    new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Fenetre Loi: dur�e service Maximum inferieur � la dur�e de service minimum.").activate();
                    donnees_.application_.addInternalFrame(modeLoi_);
                    return false;
                }
            }
        }
        final String nomQuai = (String) this.lqQuaiPref1_.getSelectedItem();
        final String nomQuai2 = (String) this.lqQuaiPref2_.getSelectedItem();
        final String nomQuai3 = (String) this.lqQuaiPref3_.getSelectedItem();
        if (nomQuai.equals(nomQuai2)) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Quais preferentiels:\n le quai preferentiel 1 doit etre different du quai 2.").activate();
            return false;
        }
        if (nomQuai.equals(nomQuai3)) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Quais preferentiels:\n le quai preferentiel 1 doit etre different du quai 3.").activate();
            return false;
        }
        if (nomQuai2.equals(nomQuai3) && !nomQuai2.equals("aucun")) {
            new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Quais preferentiels:\n le quai preferentiel 2 doit etre different du quai 3.").activate();
            return false;
        }
        final int quaiPreferentiel1 = donnees_.lQuais_.retournerIndiceQuai((String) this.lqQuaiPref1_.getSelectedItem());
        final int quaiPreferentiel2 = donnees_.lQuais_.retournerIndiceQuai(((String) this.lqQuaiPref2_.getSelectedItem()));
        final int quaiPreferentiel3 = donnees_.lQuais_.retournerIndiceQuai(((String) this.lqQuaiPref3_.getSelectedItem()));
        final float longueurMax = Float.parseFloat(dgLongMax_.getText());
        if (quaiPreferentiel1 != -1) {
            final float longueurQuai = (float) this.donnees_.lQuais_.retournerQuais(quaiPreferentiel1).longueur_;
            if (longueurQuai < longueurMax) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "Le navire est trop grand pour le quai\n" + "la longueur Max du navire etant: " + longueurMax + "\n" + "et la longueur Max du quai Preferentiel 1\n" + " " + donnees_.lQuais_.retournerQuais(quaiPreferentiel1).Nom_ + " etant: " + longueurQuai + "\n" + " Il est impossible que ce Navire obtienne ce quai.").activate();
                return false;
            }
        }
        if (quaiPreferentiel2 != -1) {
            final float longueurQuai = (float) this.donnees_.lQuais_.retournerQuais(quaiPreferentiel2).longueur_;
            if (longueurQuai < longueurMax) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "" + "La longueur Max du navire etant: " + longueurMax + "\n" + "et la longueur Max du quai Preferentiel 2\n" + " " + donnees_.lQuais_.retournerQuais(quaiPreferentiel2).Nom_ + " etant: " + longueurQuai + "\n" + " Il est impossible que ce Navire obtienne ce quai.").activate();
                return false;
            }
        }
        if (quaiPreferentiel3 != -1) {
            final float longueurQuai = (float) this.donnees_.lQuais_.retournerQuais(quaiPreferentiel3).longueur_;
            if (longueurQuai < longueurMax) {
                new BuDialogError(donnees_.application_.getApp(), donnees_.application_.isSipor_, "" + "La longueur Max du navire etant: " + longueurMax + "\n" + "et la longueur Max du quai Preferentiel 1\n" + " " + donnees_.lQuais_.retournerQuais(quaiPreferentiel3).Nom_ + " etant: " + longueurQuai + "\n" + " Il est impossible que ce Navire obtienne ce quai.").activate();
                return false;
            }
        }
        return true;
    }

    void creationNavire() {
        if (controle_creationNavire()) {
            System.out.print("Yes! donnes robustes et cohrentes!!");
            final SiporNavire nouveau = new SiporNavire();
            nouveau.nom = this.dgNom_.getText();
            nouveau.priorite = this.dgPriorite_.getSelectedIndex();
            nouveau.longueurMax = Float.parseFloat(this.dgLongMax_.getText());
            nouveau.longueurMin = Float.parseFloat(this.dgLongMin_.getText());
            nouveau.largeurMax = Float.parseFloat(this.dgLargMax_.getText());
            nouveau.largeurMin = Float.parseFloat(this.dgLargMin_.getText());
            nouveau.gareDepart_ = this.dgGareDepart.getSelectedIndex();
            nouveau.NomGareDepart_ = this.donnees_.listeGare_.retournerGare(nouveau.gareDepart_);
            nouveau.quaiPreferentiel1 = donnees_.lQuais_.retournerIndiceQuai((String) this.lqQuaiPref1_.getSelectedItem());
            nouveau.quaiPreferentiel2 = donnees_.lQuais_.retournerIndiceQuai(((String) this.lqQuaiPref2_.getSelectedItem()));
            nouveau.quaiPreferentiel3 = donnees_.lQuais_.retournerIndiceQuai(((String) this.lqQuaiPref3_.getSelectedItem()));
            nouveau.nomQuaiPreferentiel1 = (String) this.lqQuaiPref1_.getSelectedItem();
            nouveau.nomQuaiPreferentiel2 = (String) this.lqQuaiPref2_.getSelectedItem();
            nouveau.nomQuaiPreferentiel3 = (String) this.lqQuaiPref3_.getSelectedItem();
            nouveau.creneauxJournaliers_.recopie(this.horaireG_);
            nouveau.creneauxouverture_.recopie(this.horairePM_);
            if (cdShift.isSelected()) nouveau.ModeChargement = 0; else nouveau.ModeChargement = 1;
            if (nouveau.ModeChargement == 0) {
                nouveau.typeModeChargement = "shift";
            } else {
                nouveau.typeModeChargement = "loi";
            }
            nouveau.tirantEauEntree = Double.parseDouble(this.dgTirantEntree_.getText());
            nouveau.tirantEauSortie = Double.parseDouble(this.dgTirantSortie_.getText());
            if (!choixHorairesNavires_.isSelected() || cdHoraires_.getSelectedIndex() < -1 || cdHoraires_.getSelectedIndex() >= donnees_.categoriesNavires_.listeNavires_.size()) nouveau.horaireTravailSuivi.recopie(this.horaire_); else {
                int indice = cdHoraires_.getSelectedIndex();
                nouveau.horaireTravailSuivi.recopie(donnees_.categoriesNavires_.retournerNavire(indice).horaireTravailSuivi);
            }
            if (nouveau.ModeChargement == 0) {
                nouveau.cadenceQuai = Double.parseDouble(this.cdCadenceAutreQuai_.getText());
                nouveau.cadenceQuaiPref = Double.parseDouble(this.cdCadenceQuaiPref_.getText());
                nouveau.dureeAttenteShiftSuivant = Double.parseDouble(this.cdDureeProchainShift.getText());
                nouveau.tonnageMax = Double.parseDouble(this.cdTonnageMax_.getText());
                nouveau.tonnageMin = Double.parseDouble(this.cdTonnageMin_.getText());
            } else {
                nouveau.dureeServiceMaxAutresQuais = Double.parseDouble(this.cdDureeServiceMaxAutreQuai_.getText());
                nouveau.dureeServiceMaxQuaiPref = Double.parseDouble(this.cdDureeServiceMaxQuaiPref_.getText());
                nouveau.dureeServiceMinAutresQuais = Double.parseDouble(this.cdDureeServiceMinAutreQuai_.getText());
                nouveau.dureeServiceMinQuaiPref = Double.parseDouble(this.cdDureeServiceMinQuaiPref_.getText());
            }
            if (choixLoiGenerationNav_.getSelectedIndex() == 0) {
                nouveau.typeLoiGenerationNavires_ = 0;
                nouveau.nbBateauxattendus = Integer.parseInt(this.nbBateauxAttendus_.getText());
                nouveau.loiErlangGenerationNavire = Integer.parseInt((String) this.loiGenerationNavErlang_.getSelectedItem());
            } else if (choixLoiGenerationNav_.getSelectedIndex() == 1) {
                nouveau.typeLoiGenerationNavires_ = 1;
                for (int i = 0; i < this.loiDeterministe_.size(); i++) {
                    final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) this.loiDeterministe_.get(i));
                    nouveau.loiDeterministe_.add(c);
                }
            } else if (choixLoiGenerationNav_.getSelectedIndex() == 2) {
                nouveau.typeLoiGenerationNavires_ = 2;
                for (int i = 0; i < this.loiDeterministe_.size(); i++) {
                    final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) this.loiDeterministe_.get(i));
                    nouveau.loiDeterministe_.add(c);
                }
            }
            if (MODE_MODIFICATION_ON_ == false) {
                this.donnees_.categoriesNavires_.ajout(nouveau);
                new BuDialogMessage(donnees_.application_.getApp(), donnees_.application_.isSipor_, "categorie de navire " + nouveau.nom + " correctement ajoutee.").activate();
                for (int i = 0; i < this.donnees_.listeChenal_.listeChenaux_.size(); i++) {
                    final SiporChenal chenal = this.donnees_.listeChenal_.retournerChenal(i);
                    chenal.reglesNavigation_.ajoutNavire();
                }
                for (int i = 0; i < this.donnees_.listeCercle_.listeCercles_.size(); i++) {
                    final SiporCercle cercle = this.donnees_.listeCercle_.retournerCercle(i);
                    cercle.reglesNavigation_.ajoutNavire();
                }
                this.donnees_.reglesDureesParcoursChenal_.ajoutNavire();
                this.donnees_.reglesDureesParcoursCercle_.ajoutNavire();
            } else {
                donnees_.categoriesNavires_.modification(NAVIRE_A_MODIFIER_, nouveau);
                new BuDialogMessage(donnees_.application_.getApp(), donnees_.application_.isSipor_, "categorie de navire " + nouveau.nom + " correctement modifiee.").activate();
            }
            donnees_.baisserNiveauSecurite();
            donnees_.enregistrer();
            this.initialiser();
            this.MENUNAVIRES_.pile_.first(this.MENUNAVIRES_.principalPanel_);
            this.initialiser();
        }
    }

    /**
	 * ******************************* METHODE QUI INDIQUE QUE LA FENETRE DE SAISIE D UNE CATEGORIE DE NAVIRE DEVIENT UNE
	 * FENETRE DE MODIFICATION D UNE CATEGORIE
	 * 
	 * @param numNavire indice de la categorie de navire dans la liste des categories a modifier
	 */
    void MODE_MODIFICATION(final int numNavire) {
        majSaisie();
        this.MODE_MODIFICATION_ON_ = true;
        this.NAVIRE_A_MODIFIER_ = numNavire;
        final SiporNavire nav = this.donnees_.categoriesNavires_.retournerNavire(this.NAVIRE_A_MODIFIER_);
        this.dgLongMax_.setText("" + (float) nav.longueurMax);
        this.dgLongMin_.setText("" + (float) nav.longueurMin);
        this.dgLargMax_.setText("" + (float) nav.largeurMax);
        this.dgLargMin_.setText("" + (float) nav.largeurMin);
        this.dgNom_.setText(nav.nom);
        this.dgTirantEntree_.setText("" + (float) nav.tirantEauEntree);
        this.dgTirantSortie_.setText("" + (float) nav.tirantEauSortie);
        this.validerNavire_.setText("modifier");
        this.dgGareDepart.setSelectedIndex(nav.gareDepart_);
        this.dgPriorite_.setSelectedIndex(nav.priorite);
        this.lqQuaiPref1_.setSelectedIndex(nav.quaiPreferentiel1);
        this.lqQuaiPref2_.setSelectedIndex(nav.quaiPreferentiel2 + 1);
        this.lqQuaiPref3_.setSelectedIndex(nav.quaiPreferentiel3 + 1);
        if (nav.ModeChargement == 0) {
            this.cdShift.setSelected(true);
            this.cdCadenceAutreQuai_.setText("" + (float) nav.cadenceQuai);
            this.cdCadenceQuaiPref_.setText("" + (float) nav.cadenceQuaiPref);
            this.cdDureeProchainShift.setText("" + (float) nav.dureeAttenteShiftSuivant);
            this.cdTonnageMax_.setText("" + (float) nav.tonnageMax);
            this.cdTonnageMin_.setText("" + (float) nav.tonnageMin);
        } else {
            this.cdLoi.setSelected(true);
            this.cdDureeServiceMaxAutreQuai_.setText("" + (float) nav.dureeServiceMaxAutresQuais);
            this.cdDureeServiceMaxQuaiPref_.setText("" + (float) nav.dureeServiceMaxQuaiPref);
            this.cdDureeServiceMinAutreQuai_.setText("" + (float) nav.dureeServiceMinAutresQuais);
            this.cdDureeServiceMinQuaiPref_.setText("" + (float) nav.dureeServiceMinQuaiPref);
        }
        if (nav.typeLoiGenerationNavires_ == 0) {
            this.nbBateauxAttendus_.setText("" + nav.nbBateauxattendus);
            this.loiGenerationNavErlang_.setSelectedIndex(nav.loiErlangGenerationNavire - 1);
            this.choixLoiGenerationNav_.setSelectedIndex(0);
        } else if (nav.typeLoiGenerationNavires_ == 1) {
            for (int i = 0; i < nav.loiDeterministe_.size(); i++) {
                final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) nav.loiDeterministe_.get(i));
                if (i >= this.loiDeterministe_.size()) {
                    this.loiDeterministe_.add(c);
                } else {
                    this.loiDeterministe_.set(i, c);
                }
            }
            this.choixLoiGenerationNav_.setSelectedIndex(1);
        } else if (nav.typeLoiGenerationNavires_ == 2) {
            for (int i = 0; i < nav.loiDeterministe_.size(); i++) {
                final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) nav.loiDeterministe_.get(i));
                if (i >= this.loiDeterministe_.size()) {
                    this.loiDeterministe_.add(c);
                } else {
                    this.loiDeterministe_.set(i, c);
                }
            }
            this.choixLoiGenerationNav_.setSelectedIndex(2);
        }
        this.horaireG_.recopie(nav.creneauxJournaliers_);
        this.horairePM_.recopie(nav.creneauxouverture_);
        this.horaire_.recopie(nav.horaireTravailSuivi);
        this.modeShift_.setVisible(false);
        this.modeLoi_.setVisible(false);
        if (this.fenetreLoideter_ != null) {
            fenetreLoideter_.setVisible(false);
        }
        if (this.fenetreLoiJournaliere_ != null) {
            fenetreLoiJournaliere_.setVisible(false);
        }
        this.dgNom_.requestFocus();
        this.dgNom_.selectAll();
    }

    /**
	 * Methode d'initialisation du contenu des donn�es:
	 */
    void initialiser() {
        majSaisie();
        fenetreLoideter_ = null;
        this.premierAvertissment = true;
        this.horaire_ = new SiporHoraire();
        this.horaireG_ = new SiporHoraire();
        this.horairePM_ = new SiporHoraire();
        if (this.dgGareDepart.getItemCount() != 0) {
            this.dgGareDepart.setSelectedIndex(0);
        }
        this.dgNom_.setText("categorie " + (donnees_.categoriesNavires_.listeNavires_.size() + 1));
        this.dgLongMax_.setText("");
        this.dgLongMin_.setText("");
        this.lqDuree1_.setText("");
        this.lqDuree2_.setText("");
        this.lqDuree3_.setText("");
        this.dgTirantEntree_.setText("");
        this.dgTirantSortie_.setText("");
        this.validerNavire_.setText("valider");
        this.dgLargMax_.setText("");
        this.dgLargMin_.setText("");
        this.dgPriorite_.setSelectedIndex(0);
        this.lqQuaiPref1_.setSelectedIndex(0);
        this.lqQuaiPref2_.setSelectedIndex(0);
        this.lqQuaiPref3_.setSelectedIndex(0);
        this.cdCadenceAutreQuai_.setText("");
        this.cdCadenceQuaiPref_.setText("");
        this.cdDureeServiceMinAutreQuai_.setText("0");
        this.cdTonnageMax_.setText("");
        this.cdTonnageMin_.setText("");
        this.cdDureeServiceMinQuaiPref_.setText("");
        this.cdDureeProchainShift.setText("");
        this.cdDureeServiceMaxAutreQuai_.setText("0");
        this.cdDureeServiceMaxQuaiPref_.setText("");
        choixLoiGenerationNav_.setSelectedIndex(0);
        this.loiGenerationNavErlang_.setSelectedIndex(0);
        nbBateauxAttendus_.setText("");
        nbBateauxAttendus_.setEnabled(true);
        this.loiDeterministe_ = new ArrayList();
        this.MENUNAVIRES_.affichagePanel_.maj(donnees_);
        this.MODE_MODIFICATION_ON_ = false;
        this.dgNom_.requestFocus();
        this.dgNom_.selectAll();
    }
}
