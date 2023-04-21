package jskat.player.AIPlayerNN;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jskat.data.GameAnnouncement;
import jskat.player.AbstractJSkatPlayer;
import jskat.player.JSkatPlayer;
import jskat.share.Card;
import jskat.share.CardVector;
import jskat.share.SkatConstants.Ranks;
import jskat.share.SkatConstants.Suits;

public class AIPlayerNN extends AbstractJSkatPlayer implements JSkatPlayer {

    private Log log = LogFactory.getLog(AIPlayerNN.class);

    public GameAnnouncement announceGame() {
        return null;
    }

    public boolean bidMore(int currBidValue) {
        log.debug("ASDFJKLÖ");
        return false;
    }

    public void cardPlayed(Card card) {
    }

    public void discloseOuvertCards(CardVector ouvertCards) {
    }

    public boolean isAIPlayer() {
        return false;
    }

    public boolean isHumanPlayer() {
        return false;
    }

    public boolean lookIntoSkat(boolean isRamsch) {
        return false;
    }

    public Card playCard(CardVector trick) {
        return null;
    }

    public Card removeCard(Suits suit, Ranks rank) {
        return null;
    }

    public void setUpBidding(int initialForehandPlayer) {
    }

    public void showTrick(CardVector trick, int trickWinner) {
    }

    public void takeRamschSkat(CardVector skat, boolean jacksAllowed) {
    }

    public void takeSkat(CardVector skat) {
    }
}
