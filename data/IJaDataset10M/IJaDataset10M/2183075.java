package com.usoog.commons.gamecore.gamephase;

import com.usoog.commons.gamecore.gamegrid.GameGrid;
import com.usoog.commons.gamecore.gameloop.GameLoop;
import com.usoog.commons.gamecore.gamemanager.GameManager;
import com.usoog.commons.gamecore.gamestate.GameState;
import com.usoog.commons.gamecore.player.Player;
import com.usoog.commons.gamecore.tile.Tile;

/**
 * This is an abstract implementation of the GamePhase.
 *
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 * @param <Ti> The Tile type to use.
 * @param <P> The Player type to use.
 * @param <Gr> The GameGrid type to use.
 * @param <GM> The GameManager type to use.
 * @param <GS> The GameState type to use.
 */
public abstract class AbstractGamePhase<Ti extends Tile<Ti, P, Gr, GM, GS>, P extends Player<Ti, P, Gr, GM, GS>, Gr extends GameGrid<Ti, P, Gr, GM, GS>, GM extends GameManager<Ti, P, Gr, GM, GS>, GS extends GameState<Ti, P, Gr, GM, GS>> implements GamePhase<Ti, P, Gr, GM, GS> {

    /**
	 * The loop to be returned later.
	 */
    private GameLoop<Ti, P, Gr, GM, GS> loop;

    /**
	 * This indicates if this GamePhase requires painting.
	 */
    private boolean painting = false;

    /**
	 * This indicates if we require a network and want to send messages.
	 */
    private boolean network = false;

    /**
	 * This is the unique key.
	 */
    private String key;

    /**
	 * Default constructor.
	 *
	 * @param key The Identifier for this GamePhase.
	 */
    public AbstractGamePhase(String key) {
        this.key = key;
    }

    @Override
    public GameLoop<Ti, P, Gr, GM, GS> getGameLoop() {
        return loop;
    }

    @Override
    public void setGameLoop(GameLoop<Ti, P, Gr, GM, GS> loop) {
        this.loop = loop;
    }

    @Override
    public boolean isRequiringPainting() {
        return painting;
    }

    /**
	 * Method to set if we need network or not.
	 *
	 * @param network If we need network.
	 */
    protected void setRequiresNetwork(boolean network) {
        this.network = network;
    }

    @Override
    public boolean isRequiringNetwork() {
        return network;
    }

    /**
	 * This will set the unique key.
	 *
	 * @param key The unique key to be set.
	 */
    protected void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    /**
	 * Method to set if we are requireing painting for this GamePhase.
	 *
	 * @param painting True if required, otherwise false.
	 */
    protected void setRequiresPainting(boolean painting) {
        this.painting = painting;
    }

    @Override
    public void setUp() {
    }

    @Override
    public void tearDown() {
    }
}
