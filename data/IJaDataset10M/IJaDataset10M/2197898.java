package org.fudaa.fudaa.sipor.ui.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuDialogMessage;
import org.fudaa.fudaa.sipor.SiporImplementation;
import org.fudaa.fudaa.sipor.structures.CoupleLoiDeterministe;
import org.fudaa.fudaa.sipor.structures.SiporDataSimulation;
import org.fudaa.fudaa.sipor.ui.modeles.LoiDeterministeTableModel;
import org.fudaa.fudaa.sipor.ui.tools.SiporInternalFrame;
import org.jdesktop.swingx.ScrollPaneSelector;

/**
 * Panel de saisie des couples pour la loi deterministe presentation souss forme de tableau: ajout , mise a jour,
 * affichage en temps reel une fois de plus....
 * 
 * @author Adrien Hadoux
 */
public class SiporFrameSaisieLoiDeterministe extends SiporInternalFrame {

    /**
   * JCombo qui permettra de choisir pour chaque chenal les gares amont et gares avales
   */
    JComboBox ComboGare;

    /**
   * Descriptif des elements des colonnes
   */
    String[] titreColonnes = { "Jour", "Horaire" };

    /**
   * Tableau de type JTable qui contiendra les donn�es des bassins
   */
    JTable tableau;

    /**
   * Bouton de validation des donn�es topolgiques saisies pour le chenal
   */
    JButton validation = new JButton("Valider");

    /**
   * Fenetre qui contiendra le panel
   */
    JPanel global = new JPanel();

    /**
   * composant qui recevra le focus a la destructuion de la frame
   */
    Component composant_ = null;

    Border borduretab = BorderFactory.createLoweredBevelBorder();

    /**
   * donn�es de la loi deterministe
   */
    ArrayList loiDeterministe_ = new ArrayList();

    SiporDataSimulation donnees_;

    LoiDeterministeTableModel modeleTableau_;

    /**
   * constructeur du panel d'affichage des bassins
   * 
   * @param d donn�es de la simulation
   * @param _loiDeterministe liste de donn�es de la loi deeterministe
   * @param c composant qui recevra le focus des la validation de la fenetre
   */
    public SiporFrameSaisieLoiDeterministe(final SiporDataSimulation _donnees, final ArrayList _loiDeterministe, final Component c) {
        super("", true, true, true, true);
        loiDeterministe_ = _loiDeterministe;
        composant_ = c;
        donnees_ = _donnees;
        modeleTableau_ = new LoiDeterministeTableModel(loiDeterministe_, donnees_);
        global.setLayout(new BorderLayout());
        this.remplissage();
        this.affichage();
        this.validation.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                new BuDialogMessage(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "les donn�es ont �t� correctement saisies: " + loiDeterministe_.size() + " couples saisis! ").activate();
                dispose();
            }
        });
        setTitle("saisie de la loi d�terministe");
        setSize(200, 300);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        getContentPane().setLayout(new BorderLayout());
        final JScrollPane ascenceur = new JScrollPane(global);
        ScrollPaneSelector.installScrollPaneSelector(ascenceur);
        getContentPane().add(ascenceur, BorderLayout.CENTER);
        final JPanel controlPanel = new JPanel();
        controlPanel.add(validation);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    /**
   * Methode de remplissage des JComboBox et des donn�es par d�fauts pour chaque objet.
   */
    void remplissage() {
    }

    /**
   * Methode d affichage des composants du jtable et du tableau de combo Cette methode est a impl�menter dans les
   * classes d�riv�es pour chaque composants
   */
    void affichage() {
        this.tableau = new JTable(modeleTableau_);
        tableau.setBorder(this.borduretab);
        tableau.revalidate();
        this.global.add(tableau.getTableHeader(), BorderLayout.PAGE_START);
        this.global.add(tableau, BorderLayout.CENTER);
        final JPanel controlePanel = new JPanel();
        controlePanel.add(validation);
        global.add(controlePanel, BorderLayout.SOUTH);
        this.revalidate();
        this.updateUI();
    }

    /**
   * Methode qui permet de verifier la pertinence des donn�es saisies: IE: v�rifie que les gares choisies en amont et
   * avales sont bien diff�rentes pour un m�me chenal
   * 
   * @return true si les donn�es sont coh�rentes sinon retourne false et surtout indique a quel endroit se situe
   *         l'erreur de logique de la saisie
   */
    boolean verificationCoherence() {
        String jour = "";
        String horaire = "";
        for (int i = 0; i < 300; i++) {
            if ((String) this.tableau.getModel().getValueAt(i, 0) != null) {
                jour = (String) this.tableau.getModel().getValueAt(i, 0);
            } else {
                jour = "";
            }
            if ((String) this.tableau.getModel().getValueAt(i, 1) != null) {
                horaire = (String) this.tableau.getModel().getValueAt(i, 1);
            } else {
                horaire = "";
            }
            if (!jour.equals("")) {
                if (!horaire.equals("")) {
                    try {
                        final int nj = Integer.parseInt(jour);
                        final float nh = Float.parseFloat(horaire);
                        if (nh < 0) {
                            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! L'horaire ligne " + i + " est negatif!").activate();
                            return false;
                        } else if (nj < 0) {
                            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Le jour ligne " + i + " est negatif!").activate();
                            return false;
                        } else if (nj > donnees_.getParams_().donneesGenerales.nombreJours) {
                            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! Le jour ligne " + i + " est sup�rieur au nombre total \nde jours de simulation: " + donnees_.getParams_().donneesGenerales.nombreJours + "!\n Vous pouvez modifier le nombre de jours de la simulation \n en allant dans l'onglet donn�es g�n�rales.\nFermez cette fen�tre et ouvrez la pour mettre � jour les donn�es.").activate();
                            return false;
                        } else if (nh > 24) {
                            new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "Erreur! L'horaire ligne " + i + " est invalide: sup�rieur � 24!").activate();
                            return false;
                        } else {
                            System.out.println("donnees saisies: jour " + nj + "/ horaire " + nh);
                            if (i >= this.loiDeterministe_.size()) {
                                this.loiDeterministe_.add(new CoupleLoiDeterministe(nj, nh));
                            } else {
                                this.loiDeterministe_.set(i, new CoupleLoiDeterministe(nj, nh));
                            }
                        }
                    } catch (final NumberFormatException nfe) {
                        new BuDialogError(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "  l'horaire ligne " + i + " saisi n'existe pas!").activate();
                        return false;
                    }
                } else {
                    new BuDialogMessage(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "les donn�es ont �t� correctement saisies: " + i + " couples saisis!! ").activate();
                    dispose();
                    return true;
                }
            }
        }
        new BuDialogMessage(donnees_.getApplication().getApp(), SiporImplementation.INFORMATION_SOFT, "les donn�es ont �t� correctement saisies: " + this.loiDeterministe_.size() + " couples saisis!! ").activate();
        dispose();
        if (this.composant_ != null) {
            this.composant_.requestFocus();
        }
        return true;
    }

    /**
   * Methode qui permet de mettre a jour les gares saiies en amont et vaales pour chacun des cheneaux: v�rifie dans un
   * premier temps l coh�rence des donn�es:
   */
    void miseAjourSaisieDeterministe() {
        this.verificationCoherence();
    }
}
