package au.edu.mq.itec802.cardGame.blackjack;

import java.util.ArrayList;
import au.edu.mq.itec802.cardGame.Round;

/**
 * The Class BlackjackRound.
 * 
 *         TODO: Handling splits (now it is just temporary fix)
 *               if a game is split, nobody wins anything
 *
 * @author		Tomas Krajca <tomas.krajca@students.mq.edu.au>
 * @version 	$Id: BlackjackRound.java 22/03/2011 jumbo$
 */
public class BlackjackRound implements Round {

    /** The players. */
    private ArrayList<BlackjackAbstractPlayer> players;

    /** The house. */
    private BlackjackAbstractPlayer house;

    /** The results. */
    private String results;

    /**
	 * Instantiates a new blackjack round.
	 * 
	 * @param players
	 *            the players
	 */
    public BlackjackRound(ArrayList<BlackjackAbstractPlayer> players) {
        super();
        this.players = players;
    }

    /**
     * String representation of a round
     *  
	 * @see au.edu.mq.itec802.cardGame.Round#toString()
	 */
    @Override
    public String toString() {
        if (this.results.isEmpty()) return this.evaluate() + "\n" + this.results(); else return this.results;
    }

    /**
	 * Simulates playing a round of Blackjack
	 * 
	 * @see au.edu.mq.itec802.cardGame.Round#play()
	 */
    public void play() {
        int bustCount = 0;
        BlackjackPack pack = new BlackjackPack();
        pack.shuffle(137);
        for (BlackjackAbstractPlayer player : players) if (!player.isHouse()) {
            System.out.println("--- Player " + player.getNumber());
            player.play(pack);
            if (player.score(true) > 21) bustCount++;
        } else {
            this.house = player;
        }
        if (bustCount < players.size() - 1) {
            System.out.println("--- House");
            house.play(pack);
        } else {
            house.emptyHand();
        }
        this.results = this.evaluate() + "\n" + this.results();
        System.out.println();
        System.out.println(this.results);
    }

    /**
	 * Evaluates the player round.
	 * 
	 * @return the results of the round as String
	 */
    private String evaluate() {
        BlackjackAbstractPlayer player = players.get(0);
        int split = 0;
        String output = new String();
        if (player.getScore() > 21) output += "--Bust"; else if (player.getScore() == 21) output += "--Blackjack"; else output += "--" + Integer.toString(player.getScore());
        for (int i = 1; i < players.size(); i++) {
            if ((players.get(i).getScore() <= 21 && players.get(i).getScore() > player.getScore()) || player.getScore() > 21) {
                player = players.get(i);
                split = 0;
            } else if (players.get(i).getScore() == player.getScore()) {
                split++;
            }
            if (players.get(i).isHouse()) output += "-";
            if (players.get(i).getScore() > 21) output += "--Bust"; else if (players.get(i).getScore() == 21) output += "--Blackjack"; else output += "--" + Integer.toString(players.get(i).getScore());
            if (players.get(i).isHouse()) output += "-";
        }
        if (split > 0) {
            output = "Sorry, this is a split " + output;
        } else if (player.isHouse()) {
            output = "House wins! " + output;
            for (BlackjackAbstractPlayer player2 : players) if (!player2.isHouse()) player2.loses();
        } else {
            output = "Player " + player.getNumber() + " wins! " + output;
            for (BlackjackAbstractPlayer player2 : players) if (player != player2) {
                player.wins(player2.getBet());
                player2.loses();
            }
        }
        return output;
    }

    /**
	 * The final amounts (after this round) of individual users.
	 * 
	 * @return the final amounts of individual users as String
	 */
    private String results() {
        String results = new String();
        results += "Amounts: ";
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isHouse()) continue;
            if (i < players.size() - 1 - (players.contains(this.house) ? 1 : 0)) results += "Player " + players.get(i).getNumber() + " total: $" + players.get(i).getAmount() + ", "; else results += "Player " + players.get(i).getNumber() + " total: $" + players.get(i).getAmount();
        }
        results += "\n";
        return results;
    }
}
