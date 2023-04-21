package Pretzealz.Server.Player;

import Pretzealz.Server.util.ActualPlayerHandler;
import Pretzealz.Server.Player.*;

/**
 * This is the abstract base of what the player can do.
 * This class extends everything thats always True.
 * This class then has all veriables that can change.
 * @author Sonis
 */
public abstract class Player extends AlwaysTrue {

    /**
	 * This is the handler to give ease of use to get to the actual Player Handler
	 */
    public static ActualPlayerHandler Derefrence;

    public PlayerDetails Details = new PlayerDetails();

    public Ranks PlayerRanks = new Ranks();

    public Location PlayerAbs = new Location().x(0).y(0).z(0);

    public Player() {
    }

    public Player(int i) {
        ;
    }
}
