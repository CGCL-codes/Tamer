package org.fudaa.fudaa.sipor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.fudaa.fudaa.ressource.FudaaResource;
import org.fudaa.fudaa.sig.FSigResource;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuDialogError;
import com.memoire.bu.BuDialogMessage;

/**
 * Panel de saisie des dif�rents Bassins
 * 
 * @author Adrien Hadoux
 */
public class SiporPanelSaisieBassin_ extends JPanel {

    static int nbouverture = 0;

    /**
   * Jtext du nom a saisir
   */
    SiporTextField nom_ = new SiporTextField(10);

    /**
   * Bouton de validation du bassin
   */
    final BuButton validation_ = new BuButton(FudaaResource.FUDAA.getIcon("crystal_oui"), "valider");

    /**
   * donn�es de la simulation
   */
    SiporDataSimulation donnees_;

    /**
   * Fenetre principale de gestion des bassins
   */
    SiporVisualiserBassins MENUBASSINS_;

    /**
   * Booleen qui indique si le panel est en mode modification MODE_MODIFICATION_ON=true =>>> mode modif sinon mode
   * saisie classique Par defaut r�gl� sur mode classique de saisie(booleen=false)
   */
    boolean MODE_MODIFICATION_ON_ = false;

    /**
   * Indice du bassin a modifier:
   */
    int BASSIN_A_MODIFIER_;

    /**
   * Constructeur du panel de saisie des bassins
   * 
   * @param d donn�es de la simulation
   */
    SiporPanelSaisieBassin_(final SiporDataSimulation d, final SiporVisualiserBassins vb) {
        MENUBASSINS_ = vb;
        donnees_ = d;
        nbouverture++;
        this.setLayout(new BorderLayout());
        this.nom_.setText("Bassin " + (donnees_.listebassin_.listeBassins_.size() + 1));
        this.nom_.setToolTipText("Saisissez le nom du bassin ici");
        validation_.setToolTipText("Cliquez sur ce bouton pour valider la saisie");
        validation_.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                System.out.println("validation du noom du bassin:");
                if (nom_.getText().equals("")) {
                    new BuDialogError(donnees_.application_.getApp(), SiporImplementation.isSipor_, "Le bassin n'as pas de nom.").activate();
                } else if (MODE_MODIFICATION_ON_ && donnees_.listebassin_.existeDoublon(nom_.getText(), BASSIN_A_MODIFIER_)) {
                    new BuDialogError(donnees_.application_.getApp(), SiporImplementation.isSipor_, "Le nom est deja utilis� par un autre bassin.").activate();
                } else if (!MODE_MODIFICATION_ON_ && donnees_.listebassin_.existeDoublon(nom_.getText(), -1)) {
                    new BuDialogError(donnees_.application_.getApp(), SiporImplementation.isSipor_, "Erreur!! Le nom est deja utilis� par un autre bassin.").activate();
                } else {
                    donnees_.baisserNiveauSecurite();
                    if (!MODE_MODIFICATION_ON_) {
                        donnees_.listebassin_.ajout(nom_.getText());
                        donnees_.enregistrer();
                    } else {
                        donnees_.listebassin_.modification(BASSIN_A_MODIFIER_, nom_.getText());
                        new BuDialogMessage(donnees_.application_.getApp(), SiporImplementation.isSipor_, "Bassin " + nom_.getText() + " correctement modifi�.").activate();
                    }
                    MENUBASSINS_.affichagePanel.maj(donnees_);
                    nom_.setText("Bassin " + (donnees_.listebassin_.listeBassins_.size() + 1));
                    MODE_MODIFICATION_ON_ = false;
                    MENUBASSINS_.mode_.setText("Saisie: ");
                    validation_.setText("valider");
                    MENUBASSINS_.suppression_.setEnabled(true);
                }
            }
        });
        final JPanel panneau = new JPanel();
        panneau.add(new JLabel(SiporResource.SIPOR.getString("Nom du bassin")));
        panneau.add(this.nom_);
        panneau.setBorder(SiporBordures.bordnormal_);
        this.add(panneau, BorderLayout.CENTER);
        this.add(validation_, BorderLayout.SOUTH);
        this.setBorder(SiporBordures.bordnormal_);
    }

    /**
   * Methode qui transforme le type de la fenetre de saisie en fenetre de modification:
   * 
   * @param numBassin indice du tableau de bassin a modifier
   */
    void MODE_MODIFICATION(final int numBassin) {
        MODE_MODIFICATION_ON_ = true;
        this.nom_.setText(this.donnees_.listebassin_.retournerBassin(numBassin));
        BASSIN_A_MODIFIER_ = numBassin;
        this.MENUBASSINS_.mode_.setText("Modification: ");
        this.validation_.setText("modifier");
        this.MENUBASSINS_.suppression_.setEnabled(false);
    }
}
