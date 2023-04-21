package game.impl.conversation.conversation;

import game.model.conversation.Dialogue;
import java.util.ArrayList;

/**
 *
 * @author Gab'z
 */
public class dp_med_2 extends Dialogue {

    public dp_med_2() {
        super(null, null, "p0_medecin_intro", "Pnj");
        ArrayList<String> idRep = new ArrayList<String>(0);
        idRep.add("rp_medecin_intro_2");
        this.idReponses = idRep;
        ArrayList<String> phrases = new ArrayList<String>(0);
        phrases.add("Il semblerait que je ne vous ai jamais rencontré, vous... ");
        this.listePhrases = phrases;
    }

    @Override
    public void doActionPostAffichageConversation() {
    }
}
