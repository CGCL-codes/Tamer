package org.scotlandyard.engine.json.objects;

import org.scotlandyard.engine.Game;
import org.scotlandyard.engine.GameException;
import org.scotlandyard.engine.player.Player;

/**
 * <p>a json object that holds information about a player</p> 
 
 * @author Hussain Al-Mutawa
 * @version 1.0
 * @since Sun Sep 24, 2011
 */
public class JsonPlayer extends JsonUser {

    public String name;

    public String email;

    public String position;

    public Boolean hasTurn;

    /**
	 * constructs an empty json player data type
	 */
    public JsonPlayer() {
        super();
    }

    /**
	 * constructs the json player and its fields
	 * 
	 * @param player
	 * @throws GameException 
	 */
    public JsonPlayer(Player player, Game game) throws GameException {
        this();
        if (player == null) {
            throw new GameException("player can not be null");
        }
        if (game == null) {
            throw new GameException("game can not be null");
        }
        if (game.hasPlayer(player.getEmail()) == false) {
            throw new GameException("this player is not a memeber of the supplied game");
        }
        if (player.getPosition(game) == null) {
            throw new GameException("this player position is unnkown");
        }
        this.name = player.getName();
        this.email = player.getEmail();
        this.position = player.getPosition(game).getLabel();
        this.hasTurn = player.equals(game.getTurn());
    }

    /**
	 * constructs a plyer with email and name only
	 * @param name
	 * @param email
	 */
    public JsonPlayer(String name, String email) {
        super(name, email);
    }

    public JsonPlayer(String name, String email, String position, Boolean hasTurn) {
        this();
        this.name = name;
        this.email = email;
        this.position = position;
        this.hasTurn = hasTurn;
    }
}
