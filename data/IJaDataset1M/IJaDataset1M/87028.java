package game.impl.conversation.conversation;

import game.model.conversation.Dialogue;
import java.util.ArrayList;

/**
 *
 * @author Gab'z
 */
public class dp_med_6 extends Dialogue {

    public dp_med_6() {
        super(null, null, "p0_medecin_intro", "Pnj");
        ArrayList<String> idRep = new ArrayList<String>(0);
        idRep.add("rp_medecin_intro_6");
        this.idReponses = idRep;
        ArrayList<String> phrases = new ArrayList<String>(0);
        phrases.add("En noir et blanc? Je pense que votre personnalité “médiatrice” a pris /n" + "le choix d’être neutre et normale, peut-être que votre cerveau bloque les/n" + "couleurs pour que vous soyez le plus neutre possible?");
        this.listePhrases = phrases;
    }

    @Override
    public void doActionPostAffichageConversation() {
    }
}
