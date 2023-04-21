package game.impl.conversation.conversation;

import game.controler.gestionnaires.elementjeu.GestionnaireDecors;
import game.controler.gestionnaires.elementjeu.GestionnairePnj;
import game.model.conversation.Monologue;
import java.util.ArrayList;

/**
 *
 * @author Gab'z
 */
public class mp_gardes_1 extends Monologue {

    public mp_gardes_1() {
        super(null, "p3_gardes_peur", null, "Pnj");
        ArrayList<String> phrases = new ArrayList<String>(0);
        phrases.add("\"A priori la voie est libre\"");
        this.listePhrases = phrases;
    }

    @Override
    public void doActionPostAffichageConversation() {
        GestionnairePnj.getGpnj().getPNJ("p3_gardes_peur").setVisible(false);
        GestionnaireDecors.getGdecors().getElementDecors("p3_garde_ko").setVisible(true);
    }
}
