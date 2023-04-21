package ca.genovese.briscola.player;

import java.util.List;
import ca.genovese.briscola.PlayerState;
import ca.genovese.briscola.model.Card;

/**
 * @author angelog
 *
 */
public interface Player {

    /**
	 * Interface asking the player to select a card from ther hand.
	 * Since the callers of the interface cannot assume that the 
	 * implementation retains the list of cards in the player's hand
	 * the call includes the hand as a parameter.
	 * 
	 * The index of the selected card is returned to avoid forcing 
	 * reconcilliation logic on the caller.
	 * 
	 * @param hand A List of cards in the player's hand
	 * @return an integer representing the index within hand of the selected card.
	 */
    public int playCard(List<Card> hand);

    /**
	 * Notifies the player that a card was played.
	 * indicates the card and who played it.
	 * 
	 * note: cards played by this player will prompt notofication.
	 * 
	 * @param playerId index of the player, who played the card, within the payer list provided to notifyPlayerList.
	 * @param card The card played.
	 */
    public void notifyCardPlayed(int playerId, Card card);

    /**
	 * Notifies the player that the trump card has been drawn and what it is.
	 * 
	 * note: This is only called once per game.
	 * 
	 * @param trumpCard The card drawn as the trump card.
	 */
    public void notifyTrumpCard(Card trumpCard);

    public void notifyCardDealt(Card card);

    /**
	 * Notifies the player that another player has had thier card dealt.
	 * 
	 * @param playerId index of the player, who was dealt the card, within the payer list provided to notifyPlayerList. 
	 */
    public void notifyPlayerCardDealt(int playerId);

    /**
	 * Notifies the player of the list of players at the table
	 * 
	 * @param players List of players at the table
	 */
    public void notifyPlayerList(List<PlayerState> players);

    /**
	 * Notifies the player that the round is complete, which player won and how many points were awarded.
	 * 
	 * @param playerId index of the player, who won the round, within the payer list provided to notifyPlayerList.
	 * @param pointsWon number of points won this rond.
	 */
    public void notifyRoundWin(int playerId, int pointsWon);

    /**
	 * Notifies the player that the game has come to an end. 
	 */
    public void notifyEndGame();
}
