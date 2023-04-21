package CommandesEnregistrable;

import editeur.EditeurPlus;
import Commande.Commande;
import IHM.IHM;
import Memento.Memento;
import Mementos.meminsererdate;

/**
 * @(#) inserer.java
 */
public class inserer_date implements Commande {

    private IHM ihm;

    private EditeurPlus edit;

    public inserer_date(EditeurPlus ed, IHM i) {
        edit = ed;
        ihm = i;
    }

    public void execute() {
        ihm.debog("Commandes.inserer_date");
        Memento commande;
        if (ihm.getcurseur_pos() <= edit.get_buffer_taille()) {
            commande = new meminsererdate(edit, ihm);
            edit.retenir(commande);
            commande.execute();
        }
    }

    public String toString() {
        return "inserer_date";
    }
}
