package game.impl.objets.decors;

import game.controler.Action;
import game.controler.EtatJeu;
import game.model.conversation.Info;
import game.model.objets.ElementDecors;

/**
 *
 * @author Gab'z
 */
public class p0_porte_chambre extends ElementDecors {

    private boolean open = false;

    public p0_porte_chambre() {
        super("0", "La porte vers la liberté!", true, 552, 163);
    }

    @Override
    public void executeActionClicVue() {
        System.out.println("action clic vue porte chambre");
        if (EtatJeu.isSelectionne() && "p0_cle_chambre".equals(EtatJeu.getIdSelectionne())) {
            if (!open) {
                open = true;
            }
            Action.inventaireDeselectionner();
            Action.afficherInfo("La porte est ouverte");
        } else if (open) {
            Action.setVue("1");
        } else {
            Action.afficherInfo(Info.INFO_CLE);
        }
    }
}
