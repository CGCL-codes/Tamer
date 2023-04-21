package missilecommand;

import java.util.LinkedList;

/**
 * Implements a structure to keep track of a Missile Command game's state.
 * Care must be taken to ensure that multiple threads do not modify the same
 * state instance at the same time.
 **/
public class GameState {

    /**
    * A list of all currently active missiles in the game.
    * Each element of this list should be a {@link Missile} instance.
    **/
    public LinkedList<Missile> missiles;

    /**
    * A list of all currently active explosions in the game.
    * Each element of this list should be an {@link Explosion} instance.
    **/
    public LinkedList<Explosion> explosions;

    /**
    * A list of all currently active (alive) buildings in the game.
    * Each element of this list should be a {@link Building} instance.
    **/
    public LinkedList<Building> buildings;

    /**
    * A list of all unprocessed clicks by the player.  Each element of this
    * list should be a {@link Vector2D} instance.  The coordinates are
    * relative to the users view of the game, i.e., (0,0) is in the lower
    * left and positive x and y coordinates go the right and up,
    * respectively.
    **/
    public LinkedList<Vector2D> playerClicks;

    /** The player's current score. **/
    public int score;

    /**
    * The player's current power level.
    * Should be between 0 and 1, inclusive.
    **/
    public float power;

    /** The width of the game world (in pixels). **/
    public int worldWidth;

    /** The height of the game world (in pixels). **/
    public int worldHeight;

    /**
    * Creates a new instance in which all the public fields are initialized
    * to default values, except for {@link #worldWidth} and {@link
    * #worldHeight}.
    **/
    public GameState() {
        missiles = new LinkedList<Missile>();
        explosions = new LinkedList<Explosion>();
        buildings = new LinkedList<Building>();
        playerClicks = new LinkedList<Vector2D>();
        score = 0;
        power = 0.0F;
    }
}
