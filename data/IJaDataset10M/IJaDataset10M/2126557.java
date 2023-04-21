package com.multimedia.seabattle.service.ships;

import java.util.Map;
import java.util.Set;
import com.multimedia.seabattle.model.beans.Coordinates;
import com.multimedia.seabattle.model.beans.Game;
import com.multimedia.seabattle.model.types.GameShipType;
import com.multimedia.seabattle.model.types.ShipType;
import com.multimedia.seabattle.service.collisions.IShipCollision;

public interface IShipGenerator {

    /**
	 * generate next ship
	 */
    public Map<Coordinates, ShipType> generateShips(IGameShips game_ships, IShipCollision collision_handler, Game game);

    /**
	 * does this generator supports this game type?
	 */
    public boolean supports(Game game);

    /**
	 * get all supported GameShips types for this generator
	 */
    public Set<GameShipType> getSupportedGames();
}
