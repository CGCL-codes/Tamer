package diet.task.collabMinitaskProceduralComms;

import diet.debug.Debug;
import diet.server.Participant;

/**
 *
 * @author sre
 */
public class MoveONLY extends Move {

    Participant pWhoHasTo;

    String name;

    public MoveONLY(Participant p, String name) {
        this.pWhoHasTo = p;
        this.name = name;
    }

    public String getWordForParticipantAsHTML(Participant p) {
        if (p == pWhoHasTo) return name;
        return null;
    }

    public String getWordForParticipant(Participant p) {
        if (p == pWhoHasTo) return name;
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    public String evaluate(Participant p, String name, boolean isLastSelection) {
        if (!isLastSelection & p == pWhoHasTo && this.isSuccessful()) {
            return Moves.correctWORDReselected;
        } else if (!isLastSelection & p == pWhoHasTo && !this.isSuccessful()) {
            return Moves.errorInterface;
        } else if (!isLastSelection & p != pWhoHasTo && this.isSuccessful()) {
            return Moves.cantGoBackToPriorState_TriedToSelectOthersWord;
        } else if (p == pWhoHasTo && !this.isSuccessful()) {
            this.setSuccessful();
            return Moves.correctWORD;
        } else if (p == pWhoHasTo && this.isSuccessful()) {
            this.setSuccessful();
            return Moves.errorWordIsAlreadySelectedByRequestor;
        } else {
            return Moves.errorTriedToSelectOther;
        }
    }
}
