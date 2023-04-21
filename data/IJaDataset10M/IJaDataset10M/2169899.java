package au.edu.mq.itec802.cardGame.blackjack;

import au.edu.mq.itec802.cardGame.Card;
import au.edu.mq.itec802.cardGame.Hand;
import au.edu.mq.itec802.cardGame.Player;

/**
 * The Class BlackjackAbstractPlayer.
 *
 * @author		Tomas Krajca <tomas.krajca@students.mq.edu.au>
 * @version 	$Id: BlackjackAbstractPlayer.java 20/03/2011 jumbo$
 */
public abstract class BlackjackAbstractPlayer implements Player {

    /** The cards in player's hand. */
    protected Hand hand;

    /** The bet. */
    protected int bet;

    /**
	 * Simulates a playing player.
	 * 
	 * @param pack of cards to play with
	 */
    public abstract void play(BlackjackPack pack);

    /**
	 * Score. Inspired by Gaurav Gupta <gaurav.gupta@mq.edu.au>
	 * 
	 * @param acesAsOne determines if aces should be counted only as ones
	 * @return the total value of user's cards
	 */
    protected final int score(boolean acesAsOne) {
        int restTotal = 0;
        int aceCount = 0;
        for (Card card : hand) if (card.isAce()) aceCount++; else restTotal += card.getValue();
        if (restTotal <= (11 - aceCount) && !acesAsOne && aceCount > 0) {
            restTotal += 11;
            aceCount--;
        }
        return restTotal + aceCount;
    }

    /**
	 * Gets the score.
	 * 
	 * @return player's score
	 */
    public final int getScore() {
        return this.score(false);
    }

    /**
	 * Checks if is house.
	 * 
	 * @return true, if is house
	 */
    public boolean isHouse() {
        return false;
    }

    /**
	 * Player wins -- his amount is raised.
	 * 
	 * @param bet the bet this player has won on another player
	 */
    public void wins(int bet) {
    }

    /**
	 * Player loses. His bet is subtracted from his amount.
	 */
    public void loses() {
    }

    /**
	 * Gets the amount.
	 * 
	 * @return the current player's amount
	 */
    public abstract int getAmount();

    /**
	 * Gets the init amount.
	 * 
	 * @return the player's initial amount
	 */
    public abstract int getInitAmount();

    /**
	 * Gets the bet.
	 * 
	 * @return the bet
	 */
    public final int getBet() {
        return this.bet;
    }

    /**
	 * Sets the bet.
	 * 
	 * @param bet
	 *            the new bet
	 */
    public final void setBet(int bet) {
        this.bet = bet;
    }

    /**
	 * Gets the player's number.
	 * 
	 * @return the player's number
	 */
    public abstract int getNumber();

    /**
	 * Empty hand -- empties player's hand.
	 */
    public final void emptyHand() {
        this.hand = new Hand();
    }
}
