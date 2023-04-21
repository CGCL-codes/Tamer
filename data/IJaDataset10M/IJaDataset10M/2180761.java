package org.fudaa.fudaa.sipor.ui.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuDialogMessage;
import com.memoire.fu.FuLog;
import org.fudaa.fudaa.ressource.FudaaResource;
import org.fudaa.fudaa.sipor.SiporImplementation;
import org.fudaa.fudaa.sipor.structures.CoupleLoiDeterministe;
import org.fudaa.fudaa.sipor.structures.SiporChenal;
import org.fudaa.fudaa.sipor.structures.SiporDataSimulation;
import org.fudaa.fudaa.sipor.structures.SiporHoraire;
import org.fudaa.fudaa.sipor.ui.frame.SiporFrameSaisieHorairesResume;
import org.fudaa.fudaa.sipor.ui.frame.SiporFrameSaisieLoiDeterministe;
import org.fudaa.fudaa.sipor.ui.frame.SiporFrameSaisieLoiJournaliere;
import org.fudaa.fudaa.sipor.ui.frame.SiporVisualiserChenal;
import org.fudaa.fudaa.sipor.ui.tools.SiporBordures;
import org.fudaa.fudaa.sipor.ui.tools.SiporTextField;
import org.fudaa.fudaa.sipor.ui.tools.SiporTextFieldDuree;
import org.fudaa.fudaa.sipor.ui.tools.SiporTextFieldFloat;
import org.fudaa.fudaa.sipor.ui.tools.SiporTextFieldInteger;

/**
 * Panel de saisie des donnees du chenal
 * 
 * @author Adrien Hadoux
 */
public class SiporPanelSaisieChenal extends JPanel {

    static int nbouvertures = 0;

    SiporFrameSaisieLoiJournaliere fenetreLoiJournaliere_ = null;

    SiporFrameSaisieLoiDeterministe fenetreLoideter_ = null;

    SiporTextFieldDuree dureeIndispo_ = new SiporTextFieldDuree(3);

    SiporTextFieldInteger frequenceMoyenne_ = new SiporTextFieldInteger(3);

    SiporTextFieldInteger frequenceMoyenne2_ = new SiporTextFieldInteger(3);

    String[] tabloi_ = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

    JComboBox loiProbaDuree_ = new JComboBox(tabloi_);

    JComboBox loiProbaFrequence_ = new JComboBox(tabloi_);

    String[] choixLoi_ = { "Erlang", "D�terministe" };

    JComboBox choixLoiFrequence = new JComboBox(choixLoi_);

    /** variable contenant le tableau des couples pour la loi deterministe */
    ArrayList loiDeterministe_ = new ArrayList();

    /**
   * Identificateur
   */
    SiporTextField nom_ = new SiporTextField(10);

    SiporTextFieldFloat longueur = new SiporTextFieldFloat(3);

    SiporTextFieldFloat profondeur = new SiporTextFieldFloat(3);

    SiporTextFieldFloat vitesseMax = new SiporTextFieldFloat(3);

    String[] choix = { "non", "oui" };

    JComboBox soumisMaree = new JComboBox(choix);

    final BuButton creneau_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_continuer"), "Cr�neau");

    final BuButton validation_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "Valider");

    /**
   * Horaire specifique au chenal qui sera saisi lors de la creation de la frame de saisie d'horaire et sera compl�t�e
   * dans la methode de validation des donn�es via le bouton de validation
   */
    SiporHoraire horaire_ = new SiporHoraire();

    /**
   * Mode modification si mode est a true alors le mode de saisie est un mode de modification: par defaut est mis sur
   * false
   */
    public boolean MODE_MODIFICATION_ON_ = false;

    /**
   * indice du chenal a modifier dans le cas du mode modification:
   */
    int CHENAL_A_MODIFIER_;

    /**
   * Parametres de saisies de la simulation
   */
    SiporDataSimulation donnees_;

    /**
   * Fenetre principal de gestion des cheneaux
   */
    SiporVisualiserChenal MENUCHENAL_;

    /**
   * Constructeur de la frame
   */
    public SiporPanelSaisieChenal(final SiporDataSimulation d, final SiporVisualiserChenal vc) {
        donnees_ = d;
        MENUCHENAL_ = vc;
        nbouvertures++;
        this.nom_.setToolTipText("Veuillez entrer le nom du chenal ici: ");
        this.nom_.setText("chenal " + (donnees_.getListeChenal_().getListeChenaux_().size() + 1));
        this.soumisMaree.setToolTipText("cochez si le chenal est soumis � la mar�e, d�cochez sinon.");
        this.longueur.setToolTipText("Saisissez la longueur en m�tres du chenal.");
        this.profondeur.setToolTipText("Saisissez la profondeur en m�tres du chenal.");
        this.vitesseMax.setToolTipText("Saisissez la vitesse maximum en Kilom�tres par heure du chenal.");
        this.creneau_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                donnees_.getApplication().addInternalFrame(new SiporFrameSaisieHorairesResume(horaire_));
            }
        });
        this.validation_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                creation_chenal();
            }
        });
        this.choixLoiFrequence.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                final int choixLoi = choixLoiFrequence.getSelectedIndex();
                if (choixLoi == 0) {
                    frequenceMoyenne_.setEnabled(true);
                    frequenceMoyenne2_.setEnabled(true);
                    loiProbaFrequence_.setEnabled(true);
                } else if (choixLoi == 1) {
                    frequenceMoyenne_.setEnabled(false);
                    frequenceMoyenne2_.setEnabled(false);
                    loiProbaFrequence_.setEnabled(false);
                    if (fenetreLoideter_ == null) {
                        FuLog.debug("interface nulle");
                        fenetreLoideter_ = new SiporFrameSaisieLoiDeterministe(donnees_, loiDeterministe_, dureeIndispo_);
                        fenetreLoideter_.setVisible(true);
                        donnees_.getApplication().addInternalFrame(fenetreLoideter_);
                    } else {
                        FuLog.debug("interface ferm�e");
                        if (fenetreLoideter_.isClosed()) {
                            fenetreLoideter_ = new SiporFrameSaisieLoiDeterministe(donnees_, loiDeterministe_, dureeIndispo_);
                            donnees_.getApplication().addInternalFrame(fenetreLoideter_);
                        } else {
                            FuLog.debug("interface cas de figur restant autre que null et fermeture");
                            fenetreLoideter_ = new SiporFrameSaisieLoiDeterministe(donnees_, loiDeterministe_, dureeIndispo_);
                            donnees_.getApplication().activateInternalFrame(fenetreLoideter_);
                            donnees_.getApplication().addInternalFrame(fenetreLoideter_);
                        }
                    }
                } else if (choixLoi == 2) {
                    frequenceMoyenne_.setEnabled(false);
                    loiProbaFrequence_.setEnabled(false);
                    frequenceMoyenne2_.setEnabled(false);
                    if (fenetreLoiJournaliere_ == null) {
                        FuLog.debug("interface nulle");
                        fenetreLoiJournaliere_ = new SiporFrameSaisieLoiJournaliere(donnees_, loiDeterministe_, dureeIndispo_);
                        fenetreLoiJournaliere_.setVisible(true);
                        donnees_.getApplication().addInternalFrame(fenetreLoiJournaliere_);
                    } else {
                        FuLog.debug("interface ferm�e");
                        if (fenetreLoiJournaliere_.isClosed()) {
                            fenetreLoiJournaliere_ = new SiporFrameSaisieLoiJournaliere(donnees_, loiDeterministe_, dureeIndispo_);
                            donnees_.getApplication().addInternalFrame(fenetreLoiJournaliere_);
                        } else {
                            FuLog.debug("interface cas de figur restant autre que null et fermeture");
                            fenetreLoiJournaliere_ = new SiporFrameSaisieLoiJournaliere(donnees_, loiDeterministe_, dureeIndispo_);
                            donnees_.getApplication().activateInternalFrame(fenetreLoiJournaliere_);
                            donnees_.getApplication().addInternalFrame(fenetreLoiJournaliere_);
                        }
                    }
                }
            }
        });
        JPanel contenu = new JPanel(new BorderLayout());
        this.setBorder(SiporBordures.chenaux);
        this.add(contenu);
        Box total = Box.createVerticalBox();
        contenu.add(total, BorderLayout.CENTER);
        final JPanel sc1 = new JPanel();
        sc1.setBorder(SiporBordures.createTitledBorder("G�n�ral"));
        sc1.add(new JLabel("Nom du chenal: "));
        sc1.add(this.nom_);
        contenu.add(sc1, BorderLayout.NORTH);
        JPanel dimension = new JPanel(new GridLayout(1, 2));
        dimension.setBorder(SiporBordures.createTitledBorder("Dimensions"));
        total.add(dimension);
        final JPanel sc21 = new JPanel();
        sc21.setBorder(SiporBordures.bordnormal_);
        sc21.add(new JLabel("Profondeur: "));
        sc21.add(this.profondeur);
        sc21.add(new JLabel("M�tres "));
        dimension.add(sc21);
        JPanel sc22 = new JPanel();
        sc22.add(new JLabel("Soumis � la mar�e: "));
        sc22.setBorder(SiporBordures.bordnormal_);
        sc22.add(this.soumisMaree);
        dimension.add(sc22);
        JPanel indispoCreneau = new JPanel(new GridLayout(1, 2));
        total.add(indispoCreneau);
        Box loiIndispo = Box.createVerticalBox();
        loiIndispo.setBorder(SiporBordures.createTitledBorder("Loi indisponibilit�"));
        indispoCreneau.add(loiIndispo);
        final JPanel p31 = new JPanel();
        p31.add(new JLabel("Type de loi: "));
        p31.add(choixLoiFrequence);
        p31.setBorder(SiporBordures.bordnormal_);
        loiIndispo.add(p31);
        final JPanel p32 = new JPanel();
        p32.add(new JLabel("Ecart moyen:"));
        p32.add(this.frequenceMoyenne_);
        p32.add(new JLabel("Jours"));
        p32.add(this.frequenceMoyenne2_);
        p32.add(new JLabel("Heures"));
        p32.setBorder(SiporBordures.bordnormal_);
        loiIndispo.add(p32);
        final JPanel p33 = new JPanel();
        p33.add(new JLabel("Ordre loi d'Erlang fr�quence: "));
        p33.add(this.loiProbaFrequence_);
        p33.setBorder(SiporBordures.bordnormal_);
        loiIndispo.add(p33);
        Box dureeIndispoCrenaux = Box.createVerticalBox();
        indispoCreneau.add(dureeIndispoCrenaux);
        Box dureeIndispo = Box.createVerticalBox();
        dureeIndispo.setBorder(SiporBordures.createTitledBorder("Dur�e indisponibilit�"));
        dureeIndispoCrenaux.add(dureeIndispo);
        final JPanel p21 = new JPanel();
        p21.add(new JLabel("Dur�e moyenne:"));
        p21.add(this.dureeIndispo_);
        p21.add(new JLabel("Heures.Minutes "));
        p21.setBorder(SiporBordures.bordnormal_);
        dureeIndispo.add(p21);
        final JPanel p23 = new JPanel();
        p23.add(new JLabel("Ordre loi d'Erlang de la dur�e: "));
        p23.add(this.loiProbaDuree_);
        p23.setBorder(SiporBordures.bordnormal_);
        dureeIndispo.add(p23);
        JPanel p24 = new JPanel();
        p24.setBorder(SiporBordures.createTitledBorder("Cr�neaux horaires d'acc�s"));
        p24.add(this.creneau_);
        dureeIndispoCrenaux.add(p24);
        final JPanel sc5 = new JPanel();
        sc5.add(this.validation_);
        sc5.setBorder(SiporBordures.compound_);
        contenu.add(sc5, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
   * Methode de verification des coherences des donn�es
   * 
   * @return true si toutes les donn�es sont coh�rentes
   */
    boolean controle_creationChenal() {
        if (this.nom_.getText().equals("")) {
            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Nom manquant.").activate();
            return false;
        } else if (this.MODE_MODIFICATION_ON_ && this.donnees_.getListeChenal_().existeDoublon(this.nom_.getText(), this.CHENAL_A_MODIFIER_)) {
            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Nom d�j� utilis�.").activate();
            return false;
        } else if (!this.MODE_MODIFICATION_ON_ && this.donnees_.getListeChenal_().existeDoublon(this.nom_.getText(), -1)) {
            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Nom d�j� utilis�.").activate();
            return false;
        }
        if (this.profondeur.getText().equals("")) {
            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Profondeur manquante.").activate();
            return false;
        }
        if (this.horaire_.semaineCreneau1HeureArrivee == -1 || this.horaire_.semaineCreneau1HeureDep == -1 || this.horaire_.semaineCreneau2HeureArrivee == -1 || this.horaire_.semaineCreneau2HeureDep == -1) {
            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Cr�neaux non saisis correctement.").activate();
            return false;
        }
        if (this.dureeIndispo_.getText().equals("")) {
            new BuDialogError(donnees_.getApplication().getApp(), donnees_.getApplication().INFORMATION_SOFT, "Dur�e d'indisponibilit� manquante.").activate();
            return false;
        }
        if (choixLoiFrequence.getSelectedIndex() == 0) {
            if (this.frequenceMoyenne_.getText().equals("") && this.frequenceMoyenne2_.getText().equals("")) {
                new BuDialogError(donnees_.getApplication().getApp(), donnees_.getApplication().INFORMATION_SOFT, "Fr�quence moyenne manquante.").activate();
                return false;
            }
        }
        return true;
    }

    /**
   * 
   */
    void creation_chenal() {
        if (controle_creationChenal()) {
            horaire_.affichage();
            final SiporChenal q = new SiporChenal(this.donnees_.getCategoriesNavires_().getListeNavires_().size());
            q.setNom_(this.nom_.getText());
            if (((String) this.soumisMaree.getSelectedItem()).equals("oui")) {
                q.setSoumisMaree_(true);
            } else {
                q.setSoumisMaree_(false);
            }
            q.setProfondeur_(Double.parseDouble(this.profondeur.getText()));
            q.setDureeIndispo_(Float.parseFloat(this.dureeIndispo_.getText()));
            if (choixLoiFrequence.getSelectedIndex() == 0) {
                q.setTypeLoi_(0);
                float moy = 0;
                if (!this.frequenceMoyenne_.getText().equals("")) moy += Float.parseFloat(this.frequenceMoyenne_.getText()) * 24;
                if (!this.frequenceMoyenne2_.getText().equals("")) moy += Float.parseFloat(this.frequenceMoyenne2_.getText());
                q.setFrequenceMoyenne_(moy);
                q.setLoiFrequence_(Integer.parseInt((String) this.loiProbaFrequence_.getSelectedItem()));
            } else if (choixLoiFrequence.getSelectedIndex() == 1) {
                q.setTypeLoi_(1);
                for (int i = 0; i < this.loiDeterministe_.size(); i++) {
                    final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) this.loiDeterministe_.get(i));
                    q.getLoiDeterministe_().add(c);
                }
            } else if (choixLoiFrequence.getSelectedIndex() == 2) {
                q.setTypeLoi_(2);
                for (int i = 0; i < this.loiDeterministe_.size(); i++) {
                    final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) this.loiDeterministe_.get(i));
                    q.getLoiDeterministe_().add(c);
                }
            }
            q.setLoiIndispo_(Integer.parseInt((String) this.loiProbaDuree_.getSelectedItem()));
            q.getH_().recopie(horaire_);
            q.getReglesNavigation_().affichage();
            if (MODE_MODIFICATION_ON_ == false) {
                donnees_.getListeChenal_().ajout(q);
                new BuDialogMessage(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Le chenal " + this.nom_.getText() + " a �t� ajout� avec succ�s.").activate();
                donnees_.getReglesDureesParcoursChenal_().ajoutLigne(donnees_.getCategoriesNavires_().getListeNavires_().size());
            } else {
                q.setReglesNavigation_(donnees_.getListeChenal_().retournerChenal(CHENAL_A_MODIFIER_).getReglesNavigation_());
                q.setGareAmont_(donnees_.getListeChenal_().retournerChenal(CHENAL_A_MODIFIER_).getGareAmont_());
                q.setGareAval_(donnees_.getListeChenal_().retournerChenal(CHENAL_A_MODIFIER_).getGareAval_());
                donnees_.getListeChenal_().modification(CHENAL_A_MODIFIER_, q);
                new BuDialogMessage(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Le chenal " + this.nom_.getText() + " a �t� modifi� avec succ�s.").activate();
            }
            donnees_.baisserNiveauSecurite();
            donnees_.enregistrer();
            this.MENUCHENAL_.getPile_().first(this.MENUCHENAL_.principalPanel_);
            initialiser();
        }
    }

    /**
   * ******************************* METHODE QUI INDIQUE QUE LA FENETRE DE SAISIE D UN QUAI DEVIENT UNE FENETRE DE
   * MODIFICATION D UN chenal
   * 
   * @param numChenal indice du chenal dans la liste des cheneaux a modifier
   */
    public void MODE_MODIFICATION(final int numChenal) {
        this.MODE_MODIFICATION_ON_ = true;
        this.CHENAL_A_MODIFIER_ = numChenal;
        final SiporChenal q = this.donnees_.getListeChenal_().retournerChenal(this.CHENAL_A_MODIFIER_);
        this.nom_.setText(q.getNom_());
        this.longueur.setText("" + (float) q.getLongueur_());
        this.vitesseMax.setText("" + (float) q.getVitesse_());
        this.profondeur.setText("" + (float) q.getProfondeur_());
        this.validation_.setText("Modifier");
        if (q.isSoumisMaree_() == true) {
            this.soumisMaree.setSelectedIndex(1);
        } else {
            this.soumisMaree.setSelectedIndex(0);
        }
        this.horaire_.recopie(q.getH_());
        this.dureeIndispo_.setText("" + (float) q.getDureeIndispo_());
        this.loiProbaDuree_.setSelectedIndex(q.getLoiIndispo_() - 1);
        this.frequenceMoyenne_.setText("");
        this.frequenceMoyenne2_.setText("");
        this.loiProbaFrequence_.setSelectedIndex(0);
        if (q.getTypeLoi_() == 0) {
            this.frequenceMoyenne_.setText("" + ((int) q.getFrequenceMoyenne_()) / 24);
            this.frequenceMoyenne2_.setText("" + ((int) q.getFrequenceMoyenne_()) % 24);
            this.loiProbaFrequence_.setSelectedIndex(q.getLoiFrequence_() - 1);
            this.choixLoiFrequence.setSelectedIndex(0);
        } else if (q.getTypeLoi_() == 1) {
            for (int i = 0; i < q.getLoiDeterministe_().size(); i++) {
                final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) q.getLoiDeterministe_().get(i));
                if (i >= this.loiDeterministe_.size()) {
                    this.loiDeterministe_.add(c);
                } else {
                    this.loiDeterministe_.set(i, c);
                }
            }
            this.choixLoiFrequence.setSelectedIndex(1);
        } else if (q.getTypeLoi_() == 2) {
            for (int i = 0; i < q.getLoiDeterministe_().size(); i++) {
                final CoupleLoiDeterministe c = new CoupleLoiDeterministe((CoupleLoiDeterministe) q.getLoiDeterministe_().get(i));
                if (i >= this.loiDeterministe_.size()) {
                    this.loiDeterministe_.add(c);
                } else {
                    this.loiDeterministe_.set(i, c);
                }
            }
            this.choixLoiFrequence.setSelectedIndex(2);
        }
        if (this.fenetreLoideter_ != null) {
            fenetreLoideter_.setVisible(false);
        }
        if (this.fenetreLoiJournaliere_ != null) {
            fenetreLoiJournaliere_.setVisible(false);
        }
    }

    /**
   * methode d'initialisation des champs de la frame
   */
    public void initialiser() {
        fenetreLoideter_ = null;
        this.horaire_ = new SiporHoraire();
        this.nom_.setText("chenal " + (donnees_.getListeChenal_().getListeChenaux_().size() + 1));
        this.longueur.setText("");
        this.profondeur.setText("");
        this.vitesseMax.setText("");
        this.validation_.setText("Valider");
        this.MENUCHENAL_.affichagePanel_.maj(donnees_);
        this.nom_.requestFocus();
        this.nom_.selectAll();
        this.frequenceMoyenne_.setText("");
        this.frequenceMoyenne2_.setText("");
        this.dureeIndispo_.setText("");
        this.loiProbaDuree_.setSelectedIndex(0);
        this.loiProbaFrequence_.setSelectedIndex(0);
        this.choixLoiFrequence.setSelectedIndex(0);
        this.loiDeterministe_ = new ArrayList();
    }

    public SiporTextField getNom_() {
        return nom_;
    }

    public void setNom_(SiporTextField nom_) {
        this.nom_ = nom_;
    }
}
