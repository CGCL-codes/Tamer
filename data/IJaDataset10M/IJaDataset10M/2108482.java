package com.andrewswan.lostcities.domain.player.strategy;

import com.andrewswan.lostcities.domain.Game;
import com.andrewswan.lostcities.domain.move.CardDraw;
import com.andrewswan.lostcities.domain.move.CardPlay;
import com.andrewswan.lostcities.domain.player.ComputerPlayer;
import com.andrewswan.lostcities.domain.player.Player;

/**
 * A {@link ComputerPlayer}'s strategy (an AI) for playing Lost Cities
 *
 * @author Andrew
 */
public interface Strategy {

    /**
	 * Returns the name of this strategy
	 *
	 * @return a non-blank name
	 */
    String getName();

    /**
	 * Returns the next card draw to be made in executing this strategy
	 *
	 * @param game the game being played; can't be <code>null</code>
	 * @param player the player using this strategy; can't be <code>null</code>
	 * @return a non-<code>null</code> move
	 */
    CardDraw getDrawCardMove(Game game, Player player);

    /**
	 * Returns the next card play to be made in executing this strategy
	 *
	 * @param game the game being played; can't be <code>null</code>
	 * @param player the player using this strategy; can't be <code>null</code>
	 * @return a non-<code>null</code> move
	 */
    CardPlay getPlayCardMove(Game game, Player player);
}
