package foobar1914.server;

import foobar1914.shared.World;
import java.util.LinkedList;

/**
 * The is the game class the server uses to differentiate games
 *
 * @author Marc
 */
public class Game {

    private LinkedList<Player> players;

    private World world;

    private Server parent;

    private String id;

    private String title;

    private IDGenerator idGen;

    /**
   * Simple constructor that also retrieves an id
   * 
   * @param newParent
   */
    public Game(Server newParent) {
        this.parent = newParent;
        this.id = this.parent.getGameID();
        idGen = new IDGenerator();
    }

    public IDGenerator getIDGen() {
        return this.idGen;
    }

    public void setIDGen(IDGenerator newIDGen) {
        this.idGen = newIDGen;
    }
}
