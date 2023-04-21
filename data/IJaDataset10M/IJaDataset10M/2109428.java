package jskat.player.AIPlayerRND;

import org.apache.log4j.Logger;
import jskat.share.Card;
import jskat.share.CardVector;
import jskat.share.SkatConstants;
import jskat.share.SkatRules;
import jskat.data.GameAnnouncement;
import jskat.player.JSkatPlayerImpl;
import jskat.player.JSkatPlayer;

public class AIPlayerRND extends JSkatPlayerImpl implements JSkatPlayer {

    private static final Logger log = Logger.getLogger(AIPlayerRND.class);

    /** Creates a new instance of SkatPlayer */
    public AIPlayerRND(int playerID) {
        super();
        log.debug("Constructing new AI player.");
        setPlayerID(playerID);
        setPlayerName("Nobody");
    }

    /** Creates a new instance of SkatPlayer */
    public AIPlayerRND() {
        super();
        log.debug("Constructing new AIPlayerRND");
    }

    /** Creates a new instance of SkatPlayer */
    public AIPlayerRND(int playerID, String playerName) {
        super();
        log.debug("Constructing new AIPlayerRND");
        setPlayerID(playerID);
        setPlayerName(playerName);
    }

    public void takeRamschSkat(CardVector skat, boolean jacksAllowed) {
    }

    public boolean lookIntoSkat(boolean isRamsch) {
        return true;
    }

    public GameAnnouncement announceGame() {
        GameAnnouncement newGame = new GameAnnouncement();
        newGame.setGameType(SkatConstants.SUIT);
        newGame.setTrump(cards.getMostFrequentSuitColor());
        return newGame;
    }

    public boolean bidMore(int currBidValue) {
        return false;
    }

    public Card playCard(CardVector trick) {
        int index = -1;
        CardVector possibleCards = new CardVector();
        if (trick.size() > 0) {
            for (int i = 0; i < cards.size(); i++) {
                if (SkatRules.isCardAllowed(cards.getCard(i), cards, trick.getCard(0), currGameType, currTrump)) {
                    possibleCards.add(cards.getCard(i));
                }
            }
        } else {
            possibleCards = cards;
        }
        log.debug("found " + possibleCards.size() + " possible cards");
        int rand = new Double(Math.random() * possibleCards.size()).intValue();
        log.debug("choosing card " + rand);
        index = cards.getIndexOf(possibleCards.getCard(rand).getSuit(), possibleCards.getCard(rand).getValue());
        log.debug("as player " + playerID + ": " + cards.getCard(index));
        return cards.remove(index);
    }

    public void showTrick(CardVector trick, int trickWinner) {
    }

    public boolean isAIPlayer() {
        return true;
    }

    public boolean isHumanPlayer() {
        return false;
    }
}
