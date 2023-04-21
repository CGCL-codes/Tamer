package Commandes;

import Composite.Element;
import fichiers.*;
import javax.swing.JOptionPane;

public class Commande_SupprimerFichierLogique implements Commande {

    private Element aSupprimer;

    public Commande_SupprimerFichierLogique(Element aSupprimer) {
        this.aSupprimer = aSupprimer;
    }

    public boolean execute() {
        String phrase = "Voulez-vous vraiment supprimer ";
        if (aSupprimer.estUnePhoto()) phrase = phrase + " le dossier "; else phrase = phrase + " l\'image ";
        phrase = phrase + aSupprimer.toString() + " de votre arborescence logique ?";
        int rep = JOptionPane.showConfirmDialog(null, phrase, "Confirmer", JOptionPane.YES_NO_OPTION);
        if (rep == JOptionPane.YES_OPTION) {
            this.aSupprimer.getPere().removeChild(this.aSupprimer);
        }
        return false;
    }

    public boolean cancel() {
        return false;
    }

    public String getNomCommande() {
        return new String(" ( Suppression: " + aSupprimer.toString() + " ) ");
    }
}
