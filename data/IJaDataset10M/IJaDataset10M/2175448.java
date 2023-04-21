package game.impl.objets.decors;

import game.controler.Action;
import game.model.objets.ElementDecors;

/**
 *
 * @author Gab'z
 */
public class p4_photo extends ElementDecors {

    private boolean photoClique = false;

    public p4_photo() {
        super("4", "Une photo", true, 516, 266);
    }

    @Override
    public void executeActionClicVue() {
        if (photoClique) {
            Action.afficherInfo(getDescription());
        } else {
            photoClique = true;
            Action.afficherInfo("J'ai jamais compris le truc d'avoir une photo de famille dans son bureau.\n" + "Les gens se plaignent de leur famille dès qu'ils sont chez eux mais ils persistent à\n" + "les avoir sous les yeux toute la journée, ils font jamais de pause ?");
        }
    }
}
