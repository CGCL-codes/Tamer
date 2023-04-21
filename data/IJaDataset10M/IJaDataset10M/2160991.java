package ru.nsu.ccfit.pm.econ.common.controller.servernet;

import ru.nsu.ccfit.pm.econ.common.engine.roles.IUPersonDescription;

/**
 * Interface to signal connected players.
 * <p>This interface is used <i>only</i> on player connection stage 
 * (i.e. before Teacher starts the game).</p>
 * <p>Components that may use this interface:
 * <ul>
 * <li>Networking</li>
 * </ul>
 * </p>
 * @author dragonfly
 */
public interface IPlayerNetworkPresence {

    /**
	 * Add player to roster.
	 * @param playerData Player data, including it's name.
	 */
    void addPlayer(IUPersonDescription playerData);

    /**
	 * Remove previously added player from roster.
	 * <p>If passed player data does not correspond to any player 
	 * in roster, nothing happens.</p>
	 * @param playerData Player data associated with player.
	 */
    void removePlayer(IUPersonDescription playerData);
}
