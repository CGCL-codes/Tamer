package at.momberban2.me.game;

import at.momberban2.me.gamecontext.M2Player;
import at.syncme.framework.Algorithm;
import at.syncme.framework.Event;
import at.syncme.framework.EventHandler;
import at.syncme.framework.legacy.WrapperCache;

/**
 * handle events if game is configured as server side
 * 
 * @author Daniel Rudigier
 */
public class M2Server implements EventHandler {

    private M2Game game;

    private M2GameState m2state;

    private Algorithm algo;

    /**
     * constructor
     * 
     * @param game
     */
    public M2Server(M2Game game) {
        this.game = game;
        this.m2state = game.m2state;
        this.algo = game.getAlgo();
    }

    /**
     * override
     * 
     * @see at.syncme.framework.EventHandler#gameEvent(
     *      at.syncme.framework.Event)
     */
    public void gameEvent(Event e) {
        final short id = e.getId();
        if (id == Event.PLAYER_CHALLANGE) {
            playerChallenge(e);
        } else if (id == Event.GAME_STATE) {
            sendGameState(e);
        } else if (id == Event.PLAYER_DISCONNECTED) {
            playerLeft(e);
        } else if (id == Event.SETUP_START) {
            game.start();
        } else {
            System.out.println("server doesn't understand my friend");
        }
    }

    /**
     * broadcast player left
     * 
     * @param e
     */
    private void playerLeft(Event e) {
        algo.sendEvent(Event.create(Event.PLAYER_DISCONNECTED, game.getPlayer(e), Event.PRIORITY_HIGH));
    }

    /**
     * send the game state to the caller back
     * 
     * @param e
     */
    private void sendGameState(Event e) {
        System.out.println("sending gamestate");
        e.setComplex(this.m2state);
        algo.acknowledge(e);
    }

    /**
     * handle a player challenge, return the player id in the payload
     * 
     * @param e
     */
    private void playerChallenge(Event e) {
        if (m2state.canJoin()) {
            Byte id = m2state.newPlayer();
            if (id != null) {
                e.setComplex(id);
                algo.acknowledge(e);
                return;
            }
        }
        algo.reject(e);
    }

    /**
     * override
     * 
     * @see at.syncme.framework.EventHandler#setupEvent(at.syncme.framework.Event)
     */
    public void setupEvent(Event arg0) {
    }
}
