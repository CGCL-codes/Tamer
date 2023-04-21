package org.fudaa.fudaa.sipor.ui.resultat.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTabbedPane;
import com.memoire.bu.BuTable;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.image.CtuluImageExport;
import org.fudaa.ctulu.table.CtuluTableExcelWriter;
import org.fudaa.ebli.graphe.BGraphe;
import org.fudaa.fudaa.ressource.FudaaResource;
import org.fudaa.fudaa.sipor.SiporImplementation;
import org.fudaa.fudaa.sipor.algorithmes.SiporAlgorithmeAttentesGenerales;
import org.fudaa.fudaa.sipor.factory.FonctionsSimu;
import org.fudaa.fudaa.sipor.factory.SiporTraduitHoraires;
import org.fudaa.fudaa.sipor.structures.SiporConstantes;
import org.fudaa.fudaa.sipor.structures.SiporDataSimulation;
import org.fudaa.fudaa.sipor.ui.modeles.SiporModeleExcel;
import org.fudaa.fudaa.sipor.ui.tools.SiporBordures;
import org.fudaa.fudaa.sipor.ui.tools.SiporInternalFrame;

/**
 * classe de gestion des resultats de la generation des bateaux propose 2 onglets: le premier propose un affichage
 * 
 * @version $Version$
 * @author Adrien Hadoux
 */
public class SiporResultatsAttenteGeneraleCategories extends SiporInternalFrame {

    /**
   * ensemble des donn�es du tableau sous la forme de data
   */
    Object[][] data;

    /**
   * Graphe associ�e aux r�sultats de la g�n�ration de bateaux
   */
    BGraphe graphe_ = new BGraphe();

    /**
   * histogramme associ�e aux r�sultats de la g�n�ration de bateaux
   */
    BGraphe histo_ = new BGraphe();

    /**
   * panel principal de la fenetre
   */
    BuPanel panelPrincipal_ = new BuPanel();

    /**
   * Panel de selection des preferences
   */
    BuPanel selectionPanel_ = new BuPanel();

    BuPanel selectionPanel1;

    /** Comboliste de selection de l element de d�part */
    String[] listeElt = { "Chenal", "Cercle", "Ecluse", "Quai", "Tous" };

    JComboBox ListeTypesDepart_ = new JComboBox(listeElt);

    JComboBox ListeElementDepart_ = new JComboBox();

    /** Comboliste de selection de l element d'arrivee */
    JComboBox ListeTypesArrivee_ = new JComboBox(listeElt);

    JComboBox ListeElementArrivee_ = new JComboBox();

    /** horaire de d�part */
    String[] chaine_sens = { "Entrant", "Sortant", "Les 2 sens" };

    JComboBox Sens_ = new JComboBox(chaine_sens);

    /**
   * Panel des options: type affichages, colonnes � faire figurer:
   */
    BuPanel optionPanel_ = new BuPanel();

    JCheckBox choixNbNavires_ = new JCheckBox("Nombre navires", true);

    JCheckBox choixTotalAttente_ = new JCheckBox("Attente totale", true);

    JCheckBox choixMarees_ = new JCheckBox("Attentes mar�es", true);

    JCheckBox choixSecurite_ = new JCheckBox("Attentes s�curit�", true);

    JCheckBox choixAcces_ = new JCheckBox("Attentes acc�s", true);

    JCheckBox choixOccupation_ = new JCheckBox("Attentes occupation", true);

    JCheckBox choixPannes_ = new JCheckBox("Attentes indispo", true);

    JCheckBox choixChenal_ = new JCheckBox("Voir chenaux", true);

    JCheckBox choixCercle_ = new JCheckBox("Voir cercles", true);

    JCheckBox choixEcluse_ = new JCheckBox("Voir ecluses", true);

    JCheckBox choixQuai_ = new JCheckBox("Voir quais", true);

    JCheckBox choixMax_ = new JCheckBox("Attente max", true);

    JCheckBox choixMoy_ = new JCheckBox("Attente moyenne", true);

    JCheckBox choixMin_ = new JCheckBox("Attente min", true);

    boolean seuil_ = false;

    JTextField valSeuil_ = new JTextField(6);

    JCheckBox valideSeuil_ = new JCheckBox("Seuil", false);

    float valeurSeuil = 0;

    /**
   * Tableau r�capitulatif des r�sultats de la simulation
   */
    BuTable tableau_;

    String titreTableau_[] = { "Cat�gorie", "Nb.nav.flotte", "Total.Mar�e", "MoyTotal.Mar�e", "NbnavAtt.Mar�e", "MoyAtt.Mar�e", "Total.Secu", "MoyTotal.Secu", "NbnavAtt.Secu", "MoyAtt.Secu", "Total.Acces", "MoyTotal.Acces", "NbnavAtt.Acces", "MoyAtt.Acces", "Total.Occup", "MoyTotal.Occup", "NbnavAtt.Occup", "MoyAtt.Occup", "Total.indispo", "MoyTotal.indispo", "NbnavAtt.Indispo", "MoyAtt.Indispo", "Total.Global", "MoyTotal.Global", "NbnavAtt.Global", "MoyAtt.Global" };

    /**
   * Panel tabbed qui g�re les 2 onglets, ie les 2 versions d'affichage des r�sultats:
   */
    BuTabbedPane panelPrincipalAffichage_ = new BuTabbedPane();

    /**
   * Panel cniotenant le tableau et les boutns de controles
   */
    BuPanel panelGestionTableau_ = new BuPanel();

    /**
   * panel de gestion du tableau et des diff�rents boutons
   */
    BuPanel panelTableau_ = new BuPanel();

    /**
   * panel de gestion des boutons
   */
    BuPanel controlPanel_ = new BuPanel();

    /**
   * Panel de gestion des boutons des courbes
   */
    BuPanel controlPanelCourbes_ = new BuPanel();

    /**
   * Panel de gestion des boutons des histogrammes
   */
    BuPanel controlPanelHisto_ = new BuPanel();

    /**
   * panel de gestion des courbes
   */
    BuPanel panelCourbe_ = new BuPanel();

    /**
   * panel de gestion des histogrammes
   */
    BuPanel panelHisto_ = new BuPanel();

    /**
   * combolist qui permet de selectionenr les lignes deu tableau a etre affich�es:
   */
    JComboBox ListeNavires_ = new JComboBox();

    /**
   * buoton de generation des resultats
   */
    private final BuButton exportationExcel_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_generer"), "Excel");

    final BuButton exportationgraphe_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_generer"), "Exportation image");

    final BuButton exportationHisto_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_generer"), "Exportation image");

    private final BuButton quitter_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_quitter"), "Quitter");

    private final BuButton quitter2_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_quitter"), "Quitter");

    private final BuButton quitter3_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_quitter"), "Quitter");

    private final BuButton lancerRecherche_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "Recherche");

    private final BuButton validerElem_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "Ok");

    Border raisedBevel_ = BorderFactory.createRaisedBevelBorder();

    Border loweredBevel_ = BorderFactory.createLoweredBevelBorder();

    Border compound_ = BorderFactory.createCompoundBorder(raisedBevel_, loweredBevel_);

    Border bordnormal_ = BorderFactory.createEtchedBorder();

    /**
   * donnees de la simulation
   */
    SiporDataSimulation donnees_;

    /**
   * constructeur de la sous fenetre de gestion des resultats:
   */
    public SiporResultatsAttenteGeneraleCategories(final SiporDataSimulation _donnees) {
        super("Attentes par cat�gories", true, true, true, true);
        donnees_ = _donnees;
        setSize(820, 600);
        setBorder(SiporBordures.compound_);
        this.getContentPane().setLayout(new GridLayout(1, 1));
        this.panelPrincipal_.setLayout(new BorderLayout());
        this.getContentPane().add(this.panelPrincipal_);
        this.panelPrincipal_.add(this.selectionPanel_, BorderLayout.NORTH);
        this.panelPrincipal_.add(this.optionPanel_, BorderLayout.WEST);
        this.panelPrincipal_.add(this.panelPrincipalAffichage_, BorderLayout.CENTER);
        panelPrincipalAffichage_.addTab("Tableau", FudaaResource.FUDAA.getIcon("crystal_arbre"), panelGestionTableau_);
        panelPrincipalAffichage_.addTab("Graphe", FudaaResource.FUDAA.getIcon("crystal_graphe"), panelCourbe_);
        panelPrincipalAffichage_.addTab("Histogramme", FudaaResource.FUDAA.getIcon("crystal_graphe"), panelHisto_);
        for (int i = 0; i < donnees_.getCategoriesNavires_().getListeNavires_().size(); i++) {
            this.ListeNavires_.addItem("" + donnees_.getCategoriesNavires_().retournerNavire(i).getNom());
        }
        this.selectionPanel_.setLayout(new GridLayout(2, 1));
        selectionPanel1 = new BuPanel();
        selectionPanel1.add(new JLabel("Attentes � cumuler pour la cat�gorie de navire:"));
        selectionPanel1.add(ListeNavires_);
        selectionPanel1.setBorder(this.bordnormal_);
        this.selectionPanel_.add(selectionPanel1);
        final BuPanel selectionPanel2 = new BuPanel();
        selectionPanel2.add(new JLabel("sens du trajet: "));
        selectionPanel2.add(this.Sens_);
        selectionPanel2.add(lancerRecherche_);
        selectionPanel2.setBorder(this.bordnormal_);
        this.selectionPanel_.add(selectionPanel2);
        this.selectionPanel_.setBorder(this.compound_);
        final ActionListener RemplissageElement = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selection = 0;
                if (e.getSource() == ListeTypesDepart_) {
                    selection = ListeTypesDepart_.getSelectedIndex();
                } else {
                    selection = ListeTypesArrivee_.getSelectedIndex();
                }
                switch(selection) {
                    case 4:
                        if (e.getSource() == ListeTypesDepart_) {
                            ListeElementDepart_.removeAllItems();
                            ListeElementDepart_.validate();
                        }
                        break;
                    case 3:
                        if (e.getSource() == ListeTypesDepart_) {
                            ListeElementDepart_.removeAllItems();
                            for (int i = 0; i < donnees_.getlQuais_().getlQuais_().size(); i++) {
                                ListeElementDepart_.addItem(donnees_.getlQuais_().retournerQuais(i).getNom());
                            }
                            ListeElementDepart_.validate();
                        } else {
                            ListeElementArrivee_.removeAllItems();
                            for (int i = 0; i < donnees_.getlQuais_().getlQuais_().size(); i++) {
                                ListeElementArrivee_.addItem(donnees_.getlQuais_().retournerQuais(i).getNom());
                            }
                            ListeElementArrivee_.validate();
                        }
                        break;
                    case 2:
                        if (e.getSource() == ListeTypesDepart_) {
                            ListeElementDepart_.removeAllItems();
                            for (int i = 0; i < donnees_.getListeEcluse_().getListeEcluses_().size(); i++) {
                                ListeElementDepart_.addItem(donnees_.getListeEcluse_().retournerEcluse(i).getNom_());
                            }
                            ListeElementDepart_.validate();
                        } else {
                            ListeElementArrivee_.removeAllItems();
                            for (int i = 0; i < donnees_.getListeEcluse_().getListeEcluses_().size(); i++) {
                                ListeElementArrivee_.addItem(donnees_.getListeEcluse_().retournerEcluse(i).getNom_());
                            }
                            ListeElementArrivee_.validate();
                        }
                        break;
                    case 0:
                        if (e.getSource() == ListeTypesDepart_) {
                            ListeElementDepart_.removeAllItems();
                            for (int i = 0; i < donnees_.getListeChenal_().getListeChenaux_().size(); i++) {
                                ListeElementDepart_.addItem(donnees_.getListeChenal_().retournerChenal(i).getNom_());
                            }
                            ListeElementDepart_.validate();
                        } else {
                            ListeElementArrivee_.removeAllItems();
                            for (int i = 0; i < donnees_.getListeChenal_().getListeChenaux_().size(); i++) {
                                ListeElementArrivee_.addItem(donnees_.getListeChenal_().retournerChenal(i).getNom_());
                            }
                            ListeElementArrivee_.validate();
                        }
                        break;
                    case 1:
                        if (e.getSource() == ListeTypesDepart_) {
                            ListeElementDepart_.removeAllItems();
                            for (int i = 0; i < donnees_.getListeCercle_().getListeCercles_().size(); i++) {
                                ListeElementDepart_.addItem(donnees_.getListeCercle_().retournerCercle(i).getNom_());
                            }
                            ListeElementDepart_.validate();
                        } else {
                            ListeElementArrivee_.removeAllItems();
                            for (int i = 0; i < donnees_.getListeCercle_().getListeCercles_().size(); i++) {
                                ListeElementArrivee_.addItem(donnees_.getListeCercle_().retournerCercle(i).getNom_());
                            }
                            ListeElementArrivee_.validate();
                        }
                        break;
                }
            }
        };
        this.ListeElementDepart_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
            }
        });
        this.ListeTypesDepart_.addActionListener(RemplissageElement);
        this.ListeTypesArrivee_.addActionListener(RemplissageElement);
        this.ListeTypesDepart_.setSelectedIndex(4);
        this.ListeTypesArrivee_.setSelectedIndex(0);
        this.Sens_.setSelectedIndex(2);
        this.lancerRecherche_.addActionListener(this);
        final Box panoption = Box.createVerticalBox();
        final Box bVert = Box.createVerticalBox();
        panoption.add(bVert);
        bVert.add(new JLabel(""));
        bVert.add(this.choixNbNavires_);
        bVert.setBorder(this.bordnormal_);
        bVert.add(this.choixMarees_);
        bVert.add(this.choixSecurite_);
        bVert.add(this.choixAcces_);
        bVert.add(this.choixOccupation_);
        bVert.add(this.choixPannes_);
        bVert.add(this.choixTotalAttente_);
        final TitledBorder bordure1 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Options Affichage");
        bVert.setBorder(bordure1);
        panoption.add(new JLabel(""));
        panoption.createToolTip();
        final Box bVert2 = Box.createVerticalBox();
        panoption.add(bVert2);
        bVert2.setBorder(this.bordnormal_);
        bVert2.add(this.choixChenal_);
        bVert2.add(this.choixCercle_);
        bVert2.add(this.choixEcluse_);
        bVert2.add(this.choixQuai_);
        final TitledBorder bordure2 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Type �l�ment");
        bVert2.setBorder(bordure2);
        final Box bVert3 = Box.createVerticalBox();
        final TitledBorder bordure3 = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Attente");
        bVert3.setBorder(bordure3);
        bVert3.add(this.choixMin_);
        bVert3.add(this.choixMoy_);
        bVert3.add(this.choixMax_);
        this.choixMin_.addActionListener(this);
        this.choixMoy_.addActionListener(this);
        this.choixMax_.addActionListener(this);
        panoption.add(bVert3);
        this.optionPanel_.setBorder(this.compound_);
        this.optionPanel_.add(panoption);
        this.choixAcces_.addActionListener(this);
        this.choixMarees_.addActionListener(this);
        this.choixSecurite_.addActionListener(this);
        this.choixNbNavires_.addActionListener(this);
        this.choixTotalAttente_.addActionListener(this);
        this.choixOccupation_.addActionListener(this);
        this.choixPannes_.addActionListener(this);
        this.choixChenal_.addActionListener(this);
        this.choixCercle_.addActionListener(this);
        this.choixEcluse_.addActionListener(this);
        this.choixQuai_.addActionListener(this);
        panelGestionTableau_.setLayout(new BorderLayout());
        final JScrollPane asc = new JScrollPane(this.panelTableau_);
        this.panelGestionTableau_.add(asc, BorderLayout.CENTER);
        this.controlPanel_.add(quitter_);
        this.controlPanel_.add(exportationExcel_);
        this.panelGestionTableau_.add(this.controlPanel_, BorderLayout.SOUTH);
        affichageTableau();
        this.validerElem_.addActionListener(this);
        this.exportationExcel_.setToolTipText("Permet d'importer le contenu des donn�es dans un fichier excel que l'on pourra imprimer.");
        exportationExcel_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                File fichier;
                final JFileChooser fc = new JFileChooser();
                final int returnVal = fc.showSaveDialog(SiporResultatsAttenteGeneraleCategories.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fichier = fc.getSelectedFile();
                    final File f = CtuluLibFile.appendExtensionIfNeeded(fichier, "xls");
                    final SiporModeleExcel modele = new SiporModeleExcel();
                    modele.nomColonnes_ = titreTableau_;
                    modele.data_ = new Object[data.length + 2][titreTableau_.length];
                    for (int i = 0; i < titreTableau_.length; i++) {
                        modele.data_[0][i] = titreTableau_[i];
                    }
                    for (int i = 0; i < data.length; i++) {
                        modele.data_[i + 2] = data[i];
                    }
                    modele.setNbRow(data.length + 2);
                    final CtuluTableExcelWriter ecrivain = new CtuluTableExcelWriter(modele, f);
                    try {
                        ecrivain.write(null);
                    } catch (final RowsExceededException _err) {
                        FuLog.error(_err);
                    } catch (final WriteException _err) {
                        FuLog.error(_err);
                    } catch (final IOException _err) {
                        FuLog.error(_err);
                    }
                }
            }
        });
        this.panelCourbe_.setLayout(new BorderLayout());
        final String descriptionGraphe = affichageGraphe();
        this.graphe_.setFluxDonnees(new ByteArrayInputStream(descriptionGraphe.getBytes()));
        this.panelCourbe_.add(this.graphe_, BorderLayout.CENTER);
        exportationgraphe_.setToolTipText("Permet de g�n�rer un fichier image � partir du graphe");
        exportationgraphe_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                CtuluImageExport.exportImageFor(donnees_.getApplication(), graphe_);
            }
        });
        this.controlPanelCourbes_.add(quitter2_);
        this.controlPanelCourbes_.add(exportationgraphe_);
        this.panelCourbe_.add(this.controlPanelCourbes_, BorderLayout.SOUTH);
        this.panelHisto_.setLayout(new BorderLayout());
        final String descriptionHisto = this.affichageHistogramme();
        this.histo_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
        final JScrollPane panneauHisto = new JScrollPane(this.histo_);
        this.panelHisto_.add(panneauHisto, BorderLayout.CENTER);
        exportationHisto_.setToolTipText("Permet de g�n�rer un fichier image � partir de l'histogramme");
        exportationHisto_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                CtuluImageExport.exportImageFor(donnees_.getApplication(), histo_);
            }
        });
        this.valSeuil_.addFocusListener(new FocusAdapter() {

            public void focusGained(final FocusEvent e) {
                valSeuil_.selectAll();
            }

            public void focusLost(final FocusEvent e) {
                if (!valSeuil_.getText().equals("")) {
                    try {
                        final float i = Float.parseFloat(valSeuil_.getText());
                        if (i < 0) {
                            donnees_.getApplication();
                            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! La graine de la simulation est n�gative.\nIl faut entrer un entier positif.").activate();
                            valSeuil_.setText("");
                        }
                    } catch (final NumberFormatException nfe) {
                        donnees_.getApplication();
                        new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Ce nombre n'est pas valide.\nIl faut entrer un entier.").activate();
                        valSeuil_.setText("");
                    }
                }
            }
        });
        valideSeuil_.addActionListener(this);
        this.controlPanelHisto_.add(quitter3_);
        this.controlPanelHisto_.add(exportationHisto_);
        this.controlPanelHisto_.add(new JLabel(" Seuil:"));
        this.controlPanelHisto_.add(valSeuil_);
        this.controlPanelHisto_.add(valideSeuil_);
        this.panelHisto_.add(this.controlPanelHisto_, BorderLayout.SOUTH);
        this.quitter_.setToolTipText(SiporConstantes.toolTipQuitter);
        this.quitter2_.setToolTipText(SiporConstantes.toolTipQuitter);
        this.quitter3_.setToolTipText(SiporConstantes.toolTipQuitter);
        final ActionListener actionQuitter = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SiporResultatsAttenteGeneraleCategories.this.windowClosed();
            }
        };
        this.quitter_.addActionListener(actionQuitter);
        this.quitter2_.addActionListener(actionQuitter);
        this.quitter3_.addActionListener(actionQuitter);
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menuFile = new JMenu("Fichier");
        final JMenuItem menuFileExit = new JMenuItem("Quitter");
        final JMenu menuOption = new JMenu("Options");
        final JMenu menuInfo = new JMenu("A propos de");
        menuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                SiporResultatsAttenteGeneraleCategories.this.windowClosed();
            }
        });
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuBar.add(menuOption);
        menuBar.add(menuInfo);
        setJMenuBar(menuBar);
    }

    /**
   * Methode d'affichage du tableau remarque: cete m�thode sert aussi de rafraichissement du tableau
   * 
   * @param val entier qui indique le num�ro de la cat�gorie de navire � afficher si ce parametre vaut -1 alorso n
   *          affiche la totalit� des navires
   */
    void affichageTableau() {
        int type = -1;
        if (this.ListeTypesDepart_.getSelectedIndex() == 4) {
            type = -1;
        } else {
            type = this.ListeTypesDepart_.getSelectedIndex();
        }
        final int val = this.ListeNavires_.getSelectedIndex();
        data = new Object[this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length][26];
        int indiceTbaleau = 0;
        if (type < 0) {
            for (int i = 0; i < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; i++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 3)) {
                    if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 0) {
                        data[indiceTbaleau][0] = this.donnees_.getListeChenal_().retournerChenal(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].indiceElement).getNom_();
                    } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 1) {
                        data[indiceTbaleau][0] = this.donnees_.getListeCercle_().retournerCercle(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].indiceElement).getNom_();
                    } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].typeElement == 2) {
                        data[indiceTbaleau][0] = this.donnees_.getListeEcluse_().retournerEcluse(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].indiceElement).getNom_();
                    } else {
                        data[indiceTbaleau][0] = this.donnees_.getlQuais_().retournerQuais(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].indiceElement).getNom();
                    }
                    int indiceColonne = 1;
                    if (this.choixNbNavires_.isSelected()) {
                        data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal);
                    }
                    if (this.choixMarees_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMareeTotale == 0) {
                            indiceColonne += 4;
                        } else {
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMareeTotale));
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMareeTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal));
                            data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteMaree) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteMaree / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMareeTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteMaree));
                        }
                    }
                    if (this.choixSecurite_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteSecuTotale == 0) {
                            indiceColonne += 4;
                        } else {
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteSecuTotale));
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteSecuTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal));
                            data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteSecu) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteSecu / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteSecuTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteSecu));
                        }
                    }
                    if (this.choixAcces_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteAccesTotale == 0) {
                            indiceColonne += 4;
                        } else {
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteAccesTotale));
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteAccesTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal));
                            data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteAcces) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteAcces / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteAccesTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteAcces));
                        }
                    }
                    if (this.choixOccupation_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteOccupTotale == 0) {
                            indiceColonne += 4;
                        } else {
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteOccupTotale));
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteOccupTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal));
                            data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAtenteOccup) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAtenteOccup / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteOccupTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAtenteOccup));
                        }
                    }
                    if (this.choixPannes_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attentePanneTotale == 0) {
                            indiceColonne += 4;
                        } else {
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attentePanneTotale));
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal));
                            data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttentePanne) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttentePanne / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttentePanne));
                        }
                    }
                    if (this.choixTotalAttente_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMegaTotale == 0) {
                            indiceColonne += 4;
                        } else {
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMegaTotale));
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal));
                            data[indiceTbaleau][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteTotale) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                            data[indiceTbaleau][indiceColonne++] = "" + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes2((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].attenteMegaTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[i].tableauAttenteCategories[val].nbNaviresAttenteTotale));
                        }
                    }
                    indiceTbaleau++;
                }
            }
        } else if (type != -1) {
            int ELEMENTCHOISI = -1;
            for (int k = 0; k < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; k++) {
                if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[k].typeElement == this.ListeTypesDepart_.getSelectedIndex() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[k].indiceElement == this.ListeElementDepart_.getSelectedIndex()) {
                    ELEMENTCHOISI = k;
                }
            }
            if (ELEMENTCHOISI == -1) {
                donnees_.getApplication();
                new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Nous ne trouvons pas l'el�ment s�lectionn�.").activate();
            }
            data = new Object[1][this.titreTableau_.length];
            if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].typeElement == 0) {
                data[0][0] = this.donnees_.getListeChenal_().retournerChenal(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].indiceElement).getNom_();
            } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].typeElement == 1) {
                data[0][0] = this.donnees_.getListeCercle_().retournerCercle(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].indiceElement).getNom_();
            } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].typeElement == 2) {
                data[0][0] = this.donnees_.getListeEcluse_().retournerEcluse(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].indiceElement).getNom_();
            } else {
                data[0][0] = this.donnees_.getlQuais_().retournerQuais(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].indiceElement).getNom();
            }
            int indiceColonne = 1;
            if (this.choixNbNavires_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
            }
            if (this.choixMarees_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteMareeTotale);
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteMareeTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteMaree) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteMaree / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteMareeTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteMaree);
            }
            if (this.choixSecurite_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteSecuTotale);
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteSecuTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteSecu) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteSecu / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteSecuTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteSecu);
            }
            if (this.choixAcces_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteAccesTotale);
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteAccesTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteAcces) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteAcces / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteAccesTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteAcces);
            }
            if (this.choixOccupation_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteOccupTotale);
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteOccupTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAtenteOccup) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAtenteOccup / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteOccupTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAtenteOccup);
            }
            if (this.choixPannes_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attentePanneTotale);
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttentePanne) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttentePanne / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttentePanne);
            }
            if (this.choixTotalAttente_.isSelected()) {
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteMegaTotale);
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal);
                data[0][indiceColonne++] = "" + (float) FonctionsSimu.diviserSimu(this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteTotale) + " (" + (float) ((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nombreNaviresTotal * 100) + "%)";
                data[0][indiceColonne++] = "" + (float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].attenteMegaTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[ELEMENTCHOISI].tableauAttenteCategories[val].nbNaviresAttenteTotale);
            }
        }
        this.tableau_ = new BuTable(data, this.titreTableau_) {

            public boolean isCellEditable(final int row, final int col) {
                return false;
            }
        };
        for (int i = 0; i < tableau_.getModel().getColumnCount(); i++) {
            TableColumn column = tableau_.getColumnModel().getColumn(i);
            column.setPreferredWidth(150);
        }
        tableau_.revalidate();
        this.panelTableau_.removeAll();
        this.panelTableau_.setLayout(new BorderLayout());
        this.panelTableau_.add(tableau_.getTableHeader(), BorderLayout.PAGE_START);
        this.panelTableau_.add(this.tableau_, BorderLayout.CENTER);
        this.revalidate();
        this.updateUI();
    }

    /**
   * Methode qui permet de d�crire le graphe � afficher.
   * 
   * @return chaine: chaine qui contient la des cription de la chaine de caracteres.
   */
    String affichageGraphe() {
        int nbElements = 0;
        if (this.choixChenal_.isSelected()) {
            nbElements += this.donnees_.getListeChenal_().getListeChenaux_().size();
        }
        if (this.choixCercle_.isSelected()) {
            nbElements += this.donnees_.getListeCercle_().getListeCercles_().size();
        }
        if (this.choixEcluse_.isSelected()) {
            nbElements += this.donnees_.getListeEcluse_().getListeEcluses_().size();
        }
        if (this.choixQuai_.isSelected()) {
            nbElements += this.donnees_.getlQuais_().getlQuais_().size();
        }
        int compteurElem = 0;
        String g = "";
        g += "graphe\n{\n";
        g += "  titre \" Attente de la cat�gorie " + (String) this.ListeNavires_.getSelectedItem();
        if (this.Sens_.getSelectedIndex() == 0) {
            g += " dans le sens entrant";
        } else if (this.Sens_.getSelectedIndex() == 1) {
            g += " dans le sens sortant";
        } else {
            g += " dans les 2 sens";
        }
        g += " \"\n";
        if (this.choixMarees_.isSelected()) {
            g += "  sous-titre \" Attentes de Mar�es \"\n";
        } else if (this.choixSecurite_.isSelected()) {
            g += "  sous-titre \" Attentes de S�curit� \"\n";
        } else if (this.choixAcces_.isSelected()) {
            g += "  sous-titre \" Attentes d' Acc�s \"\n";
        } else if (this.choixOccupation_.isSelected()) {
            g += "  sous-titre \" Attentes Occupation \"\n";
        } else if (this.choixPannes_.isSelected()) {
            g += "  sous-titre \" Attentes de Pannes \"\n";
        } else if (this.choixTotalAttente_.isSelected()) {
            g += "  sous-titre \" Attente totale \"\n";
        }
        g += "  animation non\n";
        g += "  legende " + "oui" + "\n";
        g += "  axe\n  {\n";
        g += "    titre \" cat." + "\"\n";
        g += "    unite \" Categories \"\n";
        g += "    orientation " + "horizontal" + "\n";
        g += "    graduations oui\n";
        g += "    minimum " + 0 + "\n";
        g += "    maximum " + (nbElements + 3) + "\n";
        g += "  }\n";
        g += "  axe\n  {\n";
        g += "    titre \" dur�es" + "\"\n";
        g += "    unite \"" + " H.Min" + "\"\n";
        g += "    orientation " + "vertical" + "\n";
        g += "    graduations oui\n";
        g += "    minimum " + 0 + "\n";
        g += "    maximum " + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) SiporAlgorithmeAttentesGenerales.determineAttenteMax(donnees_)) + "\n";
        g += "  }\n";
        if (this.choixMax_.isSelected()) {
            g += "  courbe\n  {\n";
            g += "    titre \"";
            if (this.choixMarees_.isSelected()) {
                g += "attentes Mar�es maxi";
            } else if (this.choixSecurite_.isSelected()) {
                g += "  attentes S�curit� maxi";
            } else if (this.choixAcces_.isSelected()) {
                g += "attentes Acc�s maxi";
            } else if (this.choixOccupation_.isSelected()) {
                g += "  attentes Occupations maxi";
            } else if (this.choixPannes_.isSelected()) {
                g += "  attentes Indispo maxi";
            } else if (this.choixTotalAttente_.isSelected()) {
                g += "  attentes totale maxi";
            }
            g += "\"\n";
            g += "    type " + "courbe" + "\n";
            g += "    aspect\n {\n";
            g += "contour.largeur 0 \n";
            g += "surface.couleur   BB0000 \n";
            g += "texte.couleur 000000 \n";
            g += "contour.couleur BB0000 \n";
            g += "    }\n";
            g += "    valeurs\n    {\n";
            compteurElem = 0;
            for (int n = 0; n < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; n++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 3)) {
                    g += (compteurElem + 1) + " ";
                    compteurElem++;
                    if (this.choixMarees_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMareeMaxi);
                    } else if (this.choixSecurite_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteSecuMaxi);
                    } else if (this.choixAcces_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteAccesMaxi);
                    } else if (this.choixOccupation_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteOccupMaxi);
                    } else if (this.choixPannes_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attentePanneMaxi);
                    } else if (this.choixTotalAttente_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteTotaleMaxi);
                    }
                    if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) {
                        g += "\n etiquette  \n \"" + this.donnees_.getListeChenal_().retournerChenal(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                    } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) {
                        g += "\n etiquette  \n \"" + this.donnees_.getListeCercle_().retournerCercle(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                    } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) {
                        g += "\n etiquette  \n \"" + this.donnees_.getListeEcluse_().retournerEcluse(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                    } else {
                        g += "\n etiquette  \n \"" + this.donnees_.getlQuais_().retournerQuais(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom();
                    }
                    g += "\" \n" + "\n";
                }
            }
            g += "    }\n";
            g += "  }\n";
        }
        if (this.choixMoy_.isSelected()) {
            g += "  courbe\n  {\n";
            g += "    titre \"";
            if (this.choixMarees_.isSelected()) {
                g += "Attentes Mar�es moyenne";
            } else if (this.choixSecurite_.isSelected()) {
                g += "  Attentes S�curit� moyenne";
            } else if (this.choixAcces_.isSelected()) {
                g += "Attentes Acc�s moyenne";
            } else if (this.choixOccupation_.isSelected()) {
                g += "  Attentes Occupations moyenne";
            } else if (this.choixPannes_.isSelected()) {
                g += "  Attentes Indispo moyenne";
            } else if (this.choixTotalAttente_.isSelected()) {
                g += "  Attentes totale moyenne";
            }
            g += "\"\n";
            g += "    type " + "courbe" + "\n";
            g += "    aspect\n {\n";
            g += "contour.largeur 0 \n";
            g += "surface.couleur BB8800 \n";
            g += "texte.couleur 000000 \n";
            g += "contour.couleur BB8800 \n";
            g += "    }\n";
            g += "    valeurs\n    {\n";
            compteurElem = 0;
            for (int n = 0; n < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; n++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 3)) {
                    g += (compteurElem + 1) + " ";
                    compteurElem++;
                    if (this.choixMarees_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMareeTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteMaree));
                    } else if (this.choixSecurite_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteSecuTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteSecu));
                    } else if (this.choixAcces_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteAccesTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteAcces));
                    } else if (this.choixOccupation_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteOccupTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAtenteOccup));
                    } else if (this.choixPannes_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttentePanne));
                    } else if (this.choixTotalAttente_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMegaTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteTotale));
                    }
                    g += "\n";
                }
            }
            g += "    }\n";
            g += "  }\n";
        }
        if (this.choixMin_.isSelected()) {
            g += "  courbe\n  {\n";
            g += "    titre \"";
            if (this.choixMarees_.isSelected()) {
                g += "attentes Mar�es mini";
            } else if (this.choixSecurite_.isSelected()) {
                g += "  attentes S�curit� mini";
            } else if (this.choixAcces_.isSelected()) {
                g += "attentes Acc�s mini";
            } else if (this.choixOccupation_.isSelected()) {
                g += "  attentes Occupations mini";
            } else if (this.choixPannes_.isSelected()) {
                g += "  attentes Indispo mini";
            } else if (this.choixTotalAttente_.isSelected()) {
                g += "  attentes totale mini";
            }
            g += "\"\n";
            g += "    type " + "courbe" + "\n";
            g += "    aspect\n {\n";
            g += "contour.largeur 0 \n";
            g += "surface.couleur BBCC00 \n";
            g += "texte.couleur 000000 \n";
            g += "contour.couleur BBC00 \n";
            g += "    }\n";
            g += "    valeurs\n    {\n";
            compteurElem = 0;
            for (int n = 0; n < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; n++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 3)) {
                    g += (compteurElem + 1) + " ";
                    compteurElem++;
                    if (this.choixMarees_.isSelected()) {
                        g += (float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMareeMini;
                    } else if (this.choixSecurite_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteSecuMini);
                    } else if (this.choixAcces_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteAccesMini);
                    } else if (this.choixOccupation_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteOccupMini);
                    } else if (this.choixPannes_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attentePanneMini);
                    } else if (this.choixTotalAttente_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteTotaleMini);
                    }
                    g += "\n";
                }
            }
            g += "    }\n";
            g += "  }\n";
        }
        if (seuil_) {
            g += " contrainte\n";
            g += "{\n";
            g += "titre \"seuil \"\n";
            g += " type max\n";
            g += " valeur " + valeurSeuil + CtuluLibString.LINE_SEP_SIMPLE;
            g += " \n }\n";
        }
        return g;
    }

    /**
   * methode qui retoune l histogramme correspondant aux donn�es resultats:
   * 
   * @return
   */
    String affichageHistogramme() {
        int nbElements = 0;
        if (this.choixChenal_.isSelected()) {
            nbElements += this.donnees_.getListeChenal_().getListeChenaux_().size();
        }
        if (this.choixCercle_.isSelected()) {
            nbElements += this.donnees_.getListeCercle_().getListeCercles_().size();
        }
        if (this.choixEcluse_.isSelected()) {
            nbElements += this.donnees_.getListeEcluse_().getListeEcluses_().size();
        }
        if (this.choixQuai_.isSelected()) {
            nbElements += this.donnees_.getlQuais_().getlQuais_().size();
        }
        int compteurElem = 0;
        String g = "";
        g += "graphe\n{\n";
        g += "  titre \" Attente de la cat�gorie " + (String) this.ListeNavires_.getSelectedItem();
        if (this.Sens_.getSelectedIndex() == 0) {
            g += " dans le sens entrant";
        } else if (this.Sens_.getSelectedIndex() == 1) {
            g += " dans le sens sortant";
        } else {
            g += " dans les 2 sens";
        }
        g += " \"\n";
        if (this.choixMarees_.isSelected()) {
            g += "  sous-titre \" Attentes de Mar�es \"\n";
        } else if (this.choixSecurite_.isSelected()) {
            g += "  sous-titre \" Attentes de S�curit� \"\n";
        } else if (this.choixAcces_.isSelected()) {
            g += "  sous-titre \" Attentes d' Acc�s \"\n";
        } else if (this.choixOccupation_.isSelected()) {
            g += "  sous-titre \" Attentes Occupation \"\n";
        } else if (this.choixPannes_.isSelected()) {
            g += "  sous-titre \" Attentes de Indispo \"\n";
        } else if (this.choixTotalAttente_.isSelected()) {
            g += "  sous-titre \" Attente totale \"\n";
        }
        g += "  animation non\n";
        g += "  legende " + "oui" + "\n";
        g += "  axe\n  {\n";
        g += "    titre \" cat." + "\"\n";
        g += "    unite \" Categories \"\n";
        g += "    orientation " + "horizontal" + "\n";
        g += "    graduations oui\n";
        g += "    minimum " + 0 + "\n";
        g += "    maximum " + (nbElements + 3) + "\n";
        g += "  }\n";
        g += "  axe\n  {\n";
        g += "    titre \" dur�es" + "\"\n";
        g += "    unite \"" + " H.Min" + "\"\n";
        g += "    orientation " + "vertical" + "\n";
        g += "    graduations oui\n";
        g += "    minimum " + 0 + "\n";
        g += "    maximum " + SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) SiporAlgorithmeAttentesGenerales.determineAttenteMax(donnees_)) + "\n";
        g += "  }\n";
        if (this.choixMax_.isSelected()) {
            g += "  courbe\n  {\n";
            g += "    titre \"";
            if (this.choixMarees_.isSelected()) {
                g += "attentes Mar�es maxi";
            } else if (this.choixSecurite_.isSelected()) {
                g += "  attentes S�curit� maxi";
            } else if (this.choixAcces_.isSelected()) {
                g += "attentes Acc�s maxi";
            } else if (this.choixOccupation_.isSelected()) {
                g += "  attentes Occupations maxi";
            } else if (this.choixPannes_.isSelected()) {
                g += "  attentes Indispo maxi";
            } else if (this.choixTotalAttente_.isSelected()) {
                g += "  attentes totale maxi";
            }
            g += "\"\n";
            g += "    type " + "histogramme" + "\n";
            g += "    aspect\n {\n";
            g += "contour.largeur " + 1 + " \n";
            g += "surface.couleur   BB0000 \n";
            g += "texte.couleur 000000 \n";
            g += "contour.couleur 000000 \n";
            g += "    }\n";
            g += "    valeurs\n    {\n";
            compteurElem = 0;
            for (int n = 0; n < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; n++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 3)) {
                    g += (compteurElem + 1) + " ";
                    compteurElem++;
                    if (this.choixMarees_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMareeMaxi);
                    } else if (this.choixSecurite_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteSecuMaxi);
                    } else if (this.choixAcces_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteAccesMaxi);
                    } else if (this.choixOccupation_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteOccupMaxi);
                    } else if (this.choixPannes_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attentePanneMaxi);
                    } else if (this.choixTotalAttente_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteTotaleMaxi);
                    }
                    if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) {
                        g += "\n etiquette  \n \"" + this.donnees_.getListeChenal_().retournerChenal(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                    } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) {
                        g += "\n etiquette  \n \"" + this.donnees_.getListeCercle_().retournerCercle(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                    } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) {
                        g += "\n etiquette  \n \"" + this.donnees_.getListeEcluse_().retournerEcluse(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                    } else {
                        g += "\n etiquette  \n \"" + this.donnees_.getlQuais_().retournerQuais(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom();
                    }
                    g += "\" \n" + "\n";
                }
            }
            g += "    }\n";
            g += "  }\n";
        }
        if (this.choixMoy_.isSelected()) {
            g += "  courbe\n  {\n";
            g += "    titre \"";
            if (this.choixMarees_.isSelected()) {
                g += "attentes Mar�es moyenne";
            } else if (this.choixSecurite_.isSelected()) {
                g += "  attentes S�curit� moyenne";
            } else if (this.choixAcces_.isSelected()) {
                g += "attentes Acc�s moyenne";
            } else if (this.choixOccupation_.isSelected()) {
                g += "  attentes Occupations moyenne";
            } else if (this.choixPannes_.isSelected()) {
                g += "  attentes Indispo moyenne";
            } else if (this.choixTotalAttente_.isSelected()) {
                g += "  attentes totale moyenne";
            }
            g += "\"\n";
            g += "    type " + "histogramme" + "\n";
            g += "    aspect\n {\n";
            g += "contour.largeur " + 1 + " \n";
            g += "surface.couleur BB8800 \n";
            g += "texte.couleur 000000 \n";
            g += "contour.couleur 000000 \n";
            g += "    }\n";
            g += "    valeurs\n    {\n";
            compteurElem = 0;
            for (int n = 0; n < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; n++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 3)) {
                    g += (compteurElem + 1) + " ";
                    compteurElem++;
                    if (this.choixMarees_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMareeTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteMaree));
                    } else if (this.choixSecurite_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteSecuTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteSecu));
                    } else if (this.choixAcces_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteAccesTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteAcces));
                    } else if (this.choixOccupation_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteOccupTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAtenteOccup));
                    } else if (this.choixPannes_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attentePanneTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttentePanne));
                    } else if (this.choixTotalAttente_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMegaTotale / this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].nbNaviresAttenteTotale));
                    }
                    if (this.choixMoy_.isSelected() && !this.choixMax_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) {
                            g += "\n etiquette  \n \"" + this.donnees_.getListeChenal_().retournerChenal(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                        } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) {
                            g += "\n etiquette  \n \"" + this.donnees_.getListeCercle_().retournerCercle(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                        } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) {
                            g += "\n etiquette  \n \"" + this.donnees_.getListeEcluse_().retournerEcluse(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                        } else {
                            g += "\n etiquette  \n \"" + this.donnees_.getlQuais_().retournerQuais(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom();
                        }
                    }
                    g += "\n";
                }
            }
            g += "    }\n";
            g += "  }\n";
        }
        if (this.choixMin_.isSelected()) {
            g += "  courbe\n  {\n";
            g += "    titre \"";
            if (this.choixMarees_.isSelected()) {
                g += "attentes Mar�es mini";
            } else if (this.choixSecurite_.isSelected()) {
                g += "  attentes S�curit� mini";
            } else if (this.choixAcces_.isSelected()) {
                g += "attentes Acc�s mini";
            } else if (this.choixOccupation_.isSelected()) {
                g += "  attentes Occupations mini";
            } else if (this.choixPannes_.isSelected()) {
                g += "  attentes Indispo mini";
            } else if (this.choixTotalAttente_.isSelected()) {
                g += "  attentes totale mini";
            }
            g += "\"\n";
            g += "    type " + "histogramme" + "\n";
            g += "    aspect\n {\n";
            g += "contour.largeur " + 1 + " \n";
            g += "surface.couleur BBCC00 \n";
            g += "texte.couleur 000000 \n";
            g += "contour.couleur 000000 \n";
            g += "    }\n";
            g += "    valeurs\n    {\n";
            compteurElem = 0;
            for (int n = 0; n < this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories.length; n++) {
                if ((this.choixChenal_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) || (this.choixCercle_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) || (this.choixEcluse_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) || (this.choixQuai_.isSelected() && this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 3)) {
                    g += (compteurElem + 1) + " ";
                    compteurElem++;
                    if (this.choixMarees_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteMareeMini);
                    } else if (this.choixSecurite_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteSecuMini);
                    } else if (this.choixAcces_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteAccesMini);
                    } else if (this.choixOccupation_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteOccupMini);
                    } else if (this.choixPannes_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attentePanneMini);
                    } else if (this.choixTotalAttente_.isSelected()) {
                        g += SiporTraduitHoraires.traduitMinutesEnHeuresMinutes((float) this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].tableauAttenteCategories[this.ListeNavires_.getSelectedIndex()].attenteTotaleMini);
                    }
                    if (!this.choixMoy_.isSelected() && !this.choixMax_.isSelected() && this.choixMin_.isSelected()) {
                        if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 0) {
                            g += "\n etiquette  \n \"" + this.donnees_.getListeChenal_().retournerChenal(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                        } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 1) {
                            g += "\n etiquette  \n \"" + this.donnees_.getListeCercle_().retournerCercle(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                        } else if (this.donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].typeElement == 2) {
                            g += "\n etiquette  \n \"" + this.donnees_.getListeEcluse_().retournerEcluse(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom_();
                        } else {
                            g += "\n etiquette  \n \"" + this.donnees_.getlQuais_().retournerQuais(donnees_.getParams_().ResultatsCompletsSimulation.AttentesTousElementsToutesCategories[n].indiceElement).getNom();
                        }
                    }
                    g += "\n";
                }
            }
            g += "    }\n";
            g += "  }\n";
        }
        if (seuil_) {
            g += " contrainte\n";
            g += "{\n";
            g += "titre \"seuil \"\n";
            g += " type max\n";
            g += " valeur " + valeurSeuil + CtuluLibString.LINE_SEP_SIMPLE;
            g += " \n }\n";
        }
        return g;
    }

    /**
   * Listener principal des elements de la frame: tres important pour les composant du panel de choix des �l�ments
   * 
   * @param ev evenements qui apelle cette fonction
   */
    public void actionPerformed(final ActionEvent ev) {
        final Object source = ev.getSource();
        final Dimension actuelDim = this.getSize();
        final Point pos = this.getLocation();
        if (source == this.choixTotalAttente_ || source == this.choixNbNavires_ || source == this.choixAcces_ || source == this.choixMarees_ || source == this.choixSecurite_ || source == this.choixOccupation_ || source == this.choixPannes_) {
            if (!this.choixMarees_.isSelected() && !this.choixSecurite_.isSelected() && !this.choixAcces_.isSelected() && !this.choixOccupation_.isSelected() && !this.choixPannes_.isSelected() && !this.choixTotalAttente_.isSelected()) {
                this.choixTotalAttente_.setSelected(true);
            }
            int compteurColonnes = 0;
            if (this.choixAcces_.isSelected()) {
                compteurColonnes += 4;
            }
            if (this.choixMarees_.isSelected()) {
                compteurColonnes += 4;
            }
            if (this.choixSecurite_.isSelected()) {
                compteurColonnes += 4;
            }
            if (this.choixNbNavires_.isSelected()) {
                compteurColonnes++;
            }
            if (this.choixTotalAttente_.isSelected()) {
                compteurColonnes += 4;
            }
            if (this.choixOccupation_.isSelected()) {
                compteurColonnes += 4;
            }
            if (this.choixPannes_.isSelected()) {
                compteurColonnes += 4;
            }
            this.titreTableau_ = new String[compteurColonnes + 1];
            int indiceColonne = 0;
            this.titreTableau_[indiceColonne++] = "Cat�gorie";
            if (this.choixNbNavires_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "Nb.nav.flotte";
            }
            if (this.choixMarees_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "Total.Mar�e";
                this.titreTableau_[indiceColonne++] = "MoyTotal.Mar�e";
                this.titreTableau_[indiceColonne++] = "NbnavAtt.Mar�e";
                this.titreTableau_[indiceColonne++] = "MoyAtt.Mar�e";
            }
            if (this.choixSecurite_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "Total.Secu";
                this.titreTableau_[indiceColonne++] = "MoyTotal.Secu";
                this.titreTableau_[indiceColonne++] = "NbnavAtt.Secu";
                this.titreTableau_[indiceColonne++] = "MoyAtt.Secu";
            }
            if (this.choixAcces_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "Total.Acces";
                this.titreTableau_[indiceColonne++] = "MoyTotal.Acces";
                this.titreTableau_[indiceColonne++] = "NbnavAtt.Acces";
                this.titreTableau_[indiceColonne++] = "MoyAtt.Acces";
            }
            if (this.choixOccupation_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "Total.Occup";
                this.titreTableau_[indiceColonne++] = "MoyTotal.Occup";
                this.titreTableau_[indiceColonne++] = "NbnavAtt.Occup";
                this.titreTableau_[indiceColonne++] = "MoyAtt.Occup";
            }
            if (this.choixPannes_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "Total.Indispo";
                this.titreTableau_[indiceColonne++] = "MoyTotal.Indispo";
                this.titreTableau_[indiceColonne++] = "NbnavAtt.Indispo";
                this.titreTableau_[indiceColonne++] = "MoyAtt.Indispo";
            }
            if (this.choixTotalAttente_.isSelected()) {
                this.titreTableau_[indiceColonne++] = "attente totale";
                this.titreTableau_[indiceColonne++] = "moyenne totale";
                this.titreTableau_[indiceColonne++] = "Nb.nav.Att.totale";
                this.titreTableau_[indiceColonne++] = "moyenne attentes";
            }
            affichageTableau();
            final String descriptionHisto = this.affichageHistogramme();
            this.histo_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
            final String descriptionCourbes = this.affichageGraphe();
            this.graphe_.setFluxDonnees(new ByteArrayInputStream(descriptionCourbes.getBytes()));
        } else if (source == this.lancerRecherche_ || source == validerElem_) {
            SiporAlgorithmeAttentesGenerales.calcul(donnees_, this.Sens_.getSelectedIndex());
            affichageTableau();
            final String descriptionHisto = this.affichageHistogramme();
            this.histo_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
            final String descriptionCourbes = this.affichageGraphe();
            this.graphe_.setFluxDonnees(new ByteArrayInputStream(descriptionCourbes.getBytes()));
        } else if (source == this.valideSeuil_) {
            if (this.valideSeuil_.isSelected() && !this.valSeuil_.getText().equals("")) {
                this.seuil_ = true;
                valeurSeuil = Float.parseFloat(this.valSeuil_.getText());
                final String descriptionHisto = this.affichageHistogramme();
                this.histo_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
            } else {
                this.seuil_ = false;
                final String descriptionHisto = this.affichageHistogramme();
                this.histo_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
            }
        } else if (source == this.choixChenal_ || source == this.choixCercle_ || source == this.choixEcluse_ || source == this.choixQuai_ || source == this.choixMax_ || source == this.choixMoy_ || source == this.choixMin_) {
            affichageTableau();
            String descriptionHisto = this.affichageHistogramme();
            this.histo_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
            descriptionHisto = this.affichageGraphe();
            this.graphe_.setFluxDonnees(new ByteArrayInputStream(descriptionHisto.getBytes()));
        }
        this.setSize(actuelDim);
        this.setLocation(pos);
    }

    /**
   * Methode qui s active lorsque l'on quitte l'application
   */
    protected void windowClosed() {
        dispose();
    }
}
