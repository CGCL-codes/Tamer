package org.fudaa.fudaa.sipor.ui.frame;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuDialogConfirmation;
import com.memoire.bu.BuDialogError;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluLibFile;
import org.fudaa.ctulu.table.CtuluTableExcelWriter;
import org.fudaa.fudaa.ressource.FudaaResource;
import org.fudaa.fudaa.sipor.SiporImplementation;
import org.fudaa.fudaa.sipor.structures.CoupleLoiDeterministe;
import org.fudaa.fudaa.sipor.structures.SiporCercle;
import org.fudaa.fudaa.sipor.structures.SiporChenal;
import org.fudaa.fudaa.sipor.structures.SiporConstantes;
import org.fudaa.fudaa.sipor.structures.SiporDataSimulation;
import org.fudaa.fudaa.sipor.structures.SiporNavire;
import org.fudaa.fudaa.sipor.ui.modeles.SiporModeleExcel;
import org.fudaa.fudaa.sipor.ui.panel.SiporPanelAffichageNavires;
import org.fudaa.fudaa.sipor.ui.panel.SiporPanelSaisieNavires;
import org.fudaa.fudaa.sipor.ui.tools.SiporBordures;
import org.fudaa.fudaa.sipor.ui.tools.SiporInternalFrame;
import org.jdesktop.swingx.ScrollPaneSelector;

/**
 * INterface Graphique principale pour la visualisation de Navires: Cette interface permet de : - visualiser les
 * diffrentes cat�gories de Navires saisis - ajouter une cat�gorie de Navire et ses donnes correspondantes - modifier
 * une cat�gorie de Navire pralablement cree - supprimer une cat�gorie de Navire qui ne convient pas
 * 
 * @author Adrien Hadoux
 */
public class SiporVisualiserNavires extends SiporInternalFrame {

    /**
   * Donnes de la simulation
   */
    SiporDataSimulation donnees_;

    /**
   * Layout cardlayout pour affichage des donnes
   */
    public CardLayout pile_;

    /**
   * Le panel de base qui contient tous les differents panels contient un layout de type CardLayout
   */
    public JPanel principalPanel_;

    /**
   * Panel d'affichage des differents Navires saisis layout classique flow layout ou grid layout
   */
    public SiporPanelAffichageNavires affichagePanel_;

    /**
   * ascenseur pour le panel d'affichage
   */
    JScrollPane ascAff_;

    /**
   * panel qui contient l'ascenceur (OPTIONNEL!!!!!!!!!!)
   */
    public JPanel conteneurAffichage_;

    /**
   * Panel de saisie des donnes relative aux Navires
   */
    public SiporPanelSaisieNavires SaisieNavirePanel_;

    /**
   * Panel de commande: panel qui contient les differnets boutons responsable de: -ajout -suppression -modification des
   * Navires
   */
    public JPanel controlePanel_;

    /**
   * Boutton de selection de la saisie
   */
    private final BuButton boutonSaisie_ = new BuButton(FudaaResource.FUDAA.getIcon("ajouter"), "Ajout");

    private final BuButton boutonAffichage_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_voir"), "Voir");

    private final BuButton modification_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_maj"), "Modif");

    private final BuButton quitter_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "Quitter");

    private final BuButton suppression_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_detruire"), "Suppr");

    private final BuButton duplication_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_ranger"), "Dupliquer");

    private final BuButton impression_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_generer"), "Excel");

    /**
   * Combo de visualisation des Navires deja saisis
   */
    JComboBox listeNaviresSaisis_ = new JComboBox();

    boolean alterneTitre_ = true;

    /**
   * Constructeur de la Jframe
   */
    public SiporVisualiserNavires(final SiporDataSimulation d) {
        super("", true, true, true, true);
        donnees_ = d;
        setTitle("Visualisation des cat�gories");
        setSize(800, 700);
        setBorder(SiporBordures.compound_);
        this.boutonAffichage_.setToolTipText("Permet de visualiser la totalit� des donn�es sous forme d'un tableau");
        this.boutonSaisie_.setToolTipText("Permet de saisir une nouvelle donn�e afin de l'ajouter � l'ensemble des param�tres");
        this.modification_.setToolTipText("Permet de modifier un �l�ment: il faut dabord cliquer sur l'�l�ment � modifier dans le menu \"voir\"");
        this.quitter_.setToolTipText(SiporConstantes.toolTipQuitter);
        this.suppression_.setToolTipText("Permet de supprimer une donn�e: cliquez d'abord sur l'�l�ment � supprimer dans le menu \"voir\"");
        this.duplication_.setToolTipText("Permet de dupliquer une donn�e: cliquez d'abord sur l'�l�ment � dupliquer dans le menu \"voir\"");
        this.impression_.setToolTipText("Permet d'importer le contenu des donn�es dans un fichier excel que l'on pourra par la suite imprimer");
        final Container contenu = getContentPane();
        this.principalPanel_ = new JPanel();
        pile_ = new CardLayout(30, 10);
        this.principalPanel_.setLayout(pile_);
        this.affichagePanel_ = new SiporPanelAffichageNavires(donnees_);
        this.ascAff_ = new JScrollPane(affichagePanel_);
        ScrollPaneSelector.installScrollPaneSelector(this.ascAff_);
        SaisieNavirePanel_ = new SiporPanelSaisieNavires(donnees_, this);
        this.controlePanel_ = new JPanel();
        this.controlePanel_.setLayout(new FlowLayout());
        this.principalPanel_.add(this.ascAff_, "affichage");
        final JScrollPane pasc = new JScrollPane(this.SaisieNavirePanel_);
        ScrollPaneSelector.installScrollPaneSelector(pasc);
        this.principalPanel_.add(pasc, "saisie");
        contenu.add(principalPanel_);
        contenu.add(controlePanel_, "South");
        quitter_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                SiporVisualiserNavires.this.windowClosed();
            }
        });
        controlePanel_.add(quitter_);
        boutonSaisie_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                SaisieNavirePanel_.initialiser();
                pile_.last(principalPanel_);
                setTitle("Saisie d'une nouvelle cat�gorie de navires");
                SaisieNavirePanel_.setBorder(SiporBordures.navire);
                validate();
            }
        });
        controlePanel_.add(boutonSaisie_);
        boutonAffichage_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                pile_.first(principalPanel_);
                alterneTitre_ = true;
                setTitle("Visualisation des cat�gories");
                validate();
            }
        });
        controlePanel_.add(boutonAffichage_);
        modification_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final int numNavire = affichagePanel_.tableau_.getSelectedRow();
                if (numNavire == -1) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Vous devez cliquer sur la cat�gorie de navire �\n modifier dans le tableau d'affichage.").activate();
                } else {
                    pile_.last(principalPanel_);
                    setTitle("Modification d'une cat�gorie de navire");
                    validate();
                    SaisieNavirePanel_.MODE_MODIFICATION(numNavire);
                    SaisieNavirePanel_.setBorder(SiporBordures.navire2);
                }
            }
        });
        controlePanel_.add(modification_);
        suppression_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final int numNavire = affichagePanel_.tableau_.getSelectedRow();
                if (numNavire == -1) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Vous devez cliquer sur la cat�gorie de navire �\n supprimer dans le tableau d'affichage.").activate();
                } else {
                    final int confirmation = new BuDialogConfirmation(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Etes-vous certain de vouloir supprimer la cat�gorie de navire " + donnees_.getCategoriesNavires_().retournerNavire(numNavire).getNom() + " ?").activate();
                    if (confirmation == 0) {
                        donnees_.getCategoriesNavires_().suppression(numNavire);
                        for (int i = 0; i < donnees_.getListeChenal_().getListeChenaux_().size(); i++) {
                            final SiporChenal chenal = donnees_.getListeChenal_().retournerChenal(i);
                            chenal.getReglesNavigation_().suppressionNavire(numNavire);
                        }
                        for (int i = 0; i < donnees_.getListeCercle_().getListeCercles_().size(); i++) {
                            final SiporCercle cercle = donnees_.getListeCercle_().retournerCercle(i);
                            cercle.getReglesNavigation_().suppressionNavire(numNavire);
                            cercle.getRegleGenes().suppressionNavire(numNavire);
                        }
                        donnees_.getReglesDureesParcoursChenal_().suppressionNavire(numNavire);
                        donnees_.getReglesDureesParcoursCercle_().suppressionNavire(numNavire);
                        affichagePanel_.maj(donnees_);
                        d.baisserNiveauSecurite2();
                    }
                }
            }
        });
        controlePanel_.add(suppression_);
        this.duplication_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final int numNavire = affichagePanel_.tableau_.getSelectedRow();
                if (numNavire == -1) {
                    new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Vous devez cliquer sur la cat�gorie de navire �\n dupliquer dans le tableau d'affichage.").activate();
                } else {
                    String confirmation = "";
                    confirmation = JOptionPane.showInputDialog(null, "Nom de la cat�gorie de navire � dupliquer: ");
                    if (donnees_.getCategoriesNavires_().existeDoublon(confirmation, -1)) {
                        new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Nom d�j� pris.").activate();
                        return;
                    }
                    if (!confirmation.equals("")) {
                        final SiporNavire nouveau = new SiporNavire();
                        final SiporNavire navireAdupliquer = donnees_.getCategoriesNavires_().retournerNavire(numNavire);
                        nouveau.setGareDepart_(navireAdupliquer.getGareDepart_());
                        nouveau.setNomGareDepart_(navireAdupliquer.getNomGareDepart_());
                        nouveau.setCadenceQuai(navireAdupliquer.getCadenceQuai());
                        nouveau.setCadenceQuaiPref(navireAdupliquer.getCadenceQuaiPref());
                        nouveau.setDureeAttenteShiftSuivant(navireAdupliquer.getDureeAttenteShiftSuivant());
                        nouveau.setDureeQuaiPref1(navireAdupliquer.getDureeQuaiPref1());
                        nouveau.setDureeQuaiPref2(navireAdupliquer.getDureeQuaiPref2());
                        nouveau.setDureeQuaiPref3(navireAdupliquer.getDureeQuaiPref3());
                        nouveau.setDureeServiceMaxAutresQuais(navireAdupliquer.getDureeServiceMaxAutresQuais());
                        nouveau.setDureeServiceMaxQuaiPref(navireAdupliquer.getDureeServiceMaxQuaiPref());
                        nouveau.setDureeServiceMinAutresQuais(navireAdupliquer.getDureeServiceMinAutresQuais());
                        nouveau.setDureeServiceMinQuaiPref(navireAdupliquer.getDureeServiceMinQuaiPref());
                        nouveau.setHoraireTravailSuivi(navireAdupliquer.getHoraireTravailSuivi());
                        nouveau.setLongueurMax(navireAdupliquer.getLongueurMax());
                        nouveau.setLongueurMin(navireAdupliquer.getLongueurMin());
                        nouveau.setLargeurMax(navireAdupliquer.getLargeurMax());
                        nouveau.setLargeurMin(navireAdupliquer.getLargeurMin());
                        nouveau.getCreneauxJournaliers_().recopie(navireAdupliquer.getCreneauxJournaliers_());
                        nouveau.getCreneauxouverture_().recopie(navireAdupliquer.getCreneauxouverture_());
                        nouveau.setTypeModeChargement(navireAdupliquer.getTypeModeChargement());
                        nouveau.setModeChargement(navireAdupliquer.getModeChargement());
                        nouveau.setNbQuaisPreferentiels(navireAdupliquer.getNbQuaisPreferentiels());
                        nouveau.setOrdreLoiAutresQuais(navireAdupliquer.getOrdreLoiAutresQuais());
                        nouveau.setOrdreLoiQuaiPref(navireAdupliquer.getOrdreLoiQuaiPref());
                        nouveau.setOrdreLoiVariationLongueur_(navireAdupliquer.getOrdreLoiVariationLongueur_());
                        nouveau.setOrdreLoiVariationLargeur_(navireAdupliquer.getOrdreLoiVariationLargeur_());
                        nouveau.setOrdreLoiVariationTonnage(navireAdupliquer.getOrdreLoiVariationTonnage());
                        nouveau.setPriorite(navireAdupliquer.getPriorite());
                        nouveau.setQuaiPreferentiel1(navireAdupliquer.getQuaiPreferentiel1());
                        nouveau.setNomQuaiPreferentiel1(navireAdupliquer.getNomQuaiPreferentiel1());
                        nouveau.setNomQuaiPreferentiel2(navireAdupliquer.getNomQuaiPreferentiel2());
                        nouveau.setNomQuaiPreferentiel3(navireAdupliquer.getNomQuaiPreferentiel3());
                        nouveau.setQuaiPreferentiel2(navireAdupliquer.getQuaiPreferentiel2());
                        nouveau.setQuaiPreferentiel3(navireAdupliquer.getQuaiPreferentiel3());
                        nouveau.setTirantEauEntree(navireAdupliquer.getTirantEauEntree());
                        nouveau.setTirantEauSortie(navireAdupliquer.getTirantEauSortie());
                        nouveau.setTonnageMax(navireAdupliquer.getTonnageMax());
                        nouveau.setTonnageMin(navireAdupliquer.getTonnageMin());
                        nouveau.getHoraireTravailSuivi().recopie(navireAdupliquer.getHoraireTravailSuivi());
                        if (navireAdupliquer.getTypeLoiGenerationNavires_() == 0) {
                            nouveau.setTypeLoiGenerationNavires_(0);
                            nouveau.setNbBateauxattendus(navireAdupliquer.getNbBateauxattendus());
                            nouveau.setEcartMoyenEntre2arrivees(navireAdupliquer.getEcartMoyenEntre2arrivees());
                            nouveau.setLoiErlangGenerationNavire(navireAdupliquer.getLoiErlangGenerationNavire());
                        } else if (navireAdupliquer.getTypeLoiGenerationNavires_() == 1) {
                            nouveau.setTypeLoiGenerationNavires_(1);
                            for (int i = 0; i < navireAdupliquer.getLoiDeterministe_().size(); i++) {
                                final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) navireAdupliquer.getLoiDeterministe_().get(i));
                                nouveau.getLoiDeterministe_().add(c);
                            }
                        } else if (navireAdupliquer.getTypeLoiGenerationNavires_() == 2) {
                            nouveau.setTypeLoiGenerationNavires_(2);
                            for (int i = 0; i < navireAdupliquer.getLoiDeterministe_().size(); i++) {
                                final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) navireAdupliquer.getLoiDeterministe_().get(i));
                                nouveau.getLoiDeterministe_().add(c);
                            }
                        }
                        nouveau.setNom(confirmation);
                        donnees_.getCategoriesNavires_().ajout(nouveau);
                        for (int i = 0; i < donnees_.getListeChenal_().getListeChenaux_().size(); i++) {
                            final SiporChenal chenal = donnees_.getListeChenal_().retournerChenal(i);
                            chenal.getReglesNavigation_().ajoutNavire();
                        }
                        for (int i = 0; i < donnees_.getListeCercle_().getListeCercles_().size(); i++) {
                            final SiporCercle cercle = donnees_.getListeCercle_().retournerCercle(i);
                            cercle.getReglesNavigation_().ajoutNavire();
                            cercle.getRegleGenes().ajoutNavire();
                        }
                        donnees_.getReglesDureesParcoursChenal_().ajoutNavire();
                        donnees_.getReglesDureesParcoursCercle_().ajoutNavire();
                        affichagePanel_.maj(donnees_);
                    }
                }
            }
        });
        this.controlePanel_.add(duplication_);
        impression_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                File fichier;
                final JFileChooser fc = new JFileChooser();
                final int returnVal = fc.showSaveDialog(SiporVisualiserNavires.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fichier = fc.getSelectedFile();
                    final File f = CtuluLibFile.appendExtensionIfNeeded(fichier, "xls");
                    final SiporModeleExcel modele = new SiporModeleExcel();
                    modele.nomColonnes_ = affichagePanel_.titreColonnes_;
                    modele.data_ = new Object[donnees_.getCategoriesNavires_().getListeNavires_().size() + 2][affichagePanel_.titreColonnes_.length];
                    for (int i = 0; i < affichagePanel_.titreColonnes_.length; i++) {
                        modele.data_[0][i] = affichagePanel_.titreColonnes_[i];
                    }
                    for (int i = 0; i < donnees_.getCategoriesNavires_().getListeNavires_().size(); i++) {
                        modele.data_[i + 2] = affichagePanel_.ndata_[i];
                    }
                    modele.setNbRow(donnees_.getCategoriesNavires_().getListeNavires_().size() + 2);
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
        this.controlePanel_.add(impression_);
        for (int j = 0; j < donnees_.getCategoriesNavires_().getListeNavires_().size(); j++) {
            this.listeNaviresSaisis_.addItem(((SiporNavire) donnees_.getCategoriesNavires_().getListeNavires_().get(j)).getNom());
        }
        this.listeNaviresSaisis_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                System.out.println("bateau selectionn!!");
            }
        });
        final JMenuBar menuBar = new JMenuBar();
        final JMenu menuFile = new JMenu("Fichier");
        final JMenuItem menuFileExit = new JMenuItem("Quitter");
        final JMenu menuOption = new JMenu("Options");
        final JMenu menuInfo = new JMenu("A propos de");
        menuFileExit.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                SiporVisualiserNavires.this.windowClosed();
            }
        });
        menuFile.add(menuFileExit);
        menuBar.add(menuFile);
        menuBar.add(menuOption);
        menuBar.add(menuInfo);
        setJMenuBar(menuBar);
        setVisible(true);
    }

    /**
   * Methode qui s active lorsque l'on quitte l'application
   */
    protected void windowClosed() {
        dispose();
    }
}
