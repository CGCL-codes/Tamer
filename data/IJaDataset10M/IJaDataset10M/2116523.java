package com.techjedi.dragonbot;

import java.util.List;

/**
 * Interface to the service managing I/O for the players.
 * 
 * @author Doug Bateman
 */
public interface PlayerService {

    /**
	 * Registers the player into the game.
	 * 
	 * @param name
	 *            The name of the player to add.
	 * @return The newly created Player object.
	 */
    Player addPlayer(String name);

    /**
	 * Case insensative search for the given player.
	 * 
	 * @param name
	 *            The name of the player to find.
	 * @return The player with the given name, or null if not found.
	 */
    Player getPlayer(String name);

    /**
	 * Returns all players registered in the game.
	 * 
	 * @return A list of all players in the game.
	 */
    List<Player> getAllPlayers();

    /**
	 * Returns a collection of all players at or above the given ManagerType.
	 * 
	 * @param type
	 * @return A list of all players at or above the given ManagerType.
	 */
    List<Player> getAllPlayers(ManagerType type);

    /**
	 * Get the list of available player levels.
	 * 
	 * @return a list of available player levels, lowest to highest.
	 */
    List<Level> getLevels();

    /**
	 * Ensures changes to Players are written to storage, if they haven't been
	 * already. This is useful when working with implementations which may
	 * prefer to save all the players in batch, rather than as each player
	 * changes.
	 */
    void commitChanges();
}
