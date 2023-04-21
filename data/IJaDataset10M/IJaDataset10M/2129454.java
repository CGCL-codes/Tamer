package com.evver.evvercards.service;

import static com.evver.evvercards.events.EventType.GAME_JOINED;
import static com.evver.evvercards.events.EventType.GAME_CREATED;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import com.evver.evvercards.Card;
import com.evver.evvercards.Game;
import com.evver.evvercards.Description;
import com.evver.evvercards.Player;
import com.evver.evvercards.Position;
import com.evver.evvercards.Score;
import com.evver.evvercards.Status;
import com.evver.evvercards.View;
import com.evver.evvercards.events.Event;
import com.evver.evvercards.events.EventListener;
import com.evver.evvercards.events.Message;
import com.evver.evvercards.events.EventType;

public abstract class AbstractService implements Service, Serializable {

    private static final long serialVersionUID = 1L;

    protected static final Logger logger = Logger.getLogger(AbstractService.class.getName());

    protected CopyOnWriteArrayList<EventListener> serviceListeners = null;

    /**
	 * Construct the remote handler
	 */
    protected AbstractService() {
        this.serviceListeners = new CopyOnWriteArrayList<EventListener>();
    }

    /**
	 * Gets the game engine
	 * @return the game engine
	 */
    public abstract AbstractEngine getEngine();

    /**
	 * Gets the game control
	 * @return the control object
	 */
    public AbstractControl getControl(Long id) {
        if (getEngine() != null) return getEngine().getControl(id); else return null;
    }

    /**
     * Add the passed listener to the list of listeners to notify
     * @param l a listener
     */
    public void addListener(EventListener l) {
        if (l != null) {
            logger.finest("Adding service listener...");
            serviceListeners.add(l);
        }
    }

    /**
     * Remove listeners from list
     * @param l the listener to remove
     */
    public void removeListener(EventListener l) {
        if (l != null) {
            logger.finest("Removing service listener...");
            serviceListeners.remove(l);
        }
    }

    /**
     * Propagate the event to all listeners
     * @param id the game id
     * @param e the event to fire
     */
    public void sendEvent(Long id, Event e) {
        if (e != null) {
            for (EventListener l : serviceListeners) {
                logger.finest("Sending service event " + e.getEventType() + " to listener...");
                l.onServiceEvent(id, e);
            }
        }
    }

    /**
	 * Sends the specified engine event
	 * @param id the game id of the event
	 * @param type the event classification
	 * @param data any data to send with the event
	 */
    public void sendEvent(Long id, EventType type, Serializable data) {
        Event event = new Event(type, data);
        sendEvent(id, event);
    }

    /**
	 * @see com.evver.evvercards.service.Engine#createGame(Player, Description)
	 */
    public Long createGame(Player p, Description gd) throws IllegalAccessException {
        if (getEngine() != null) {
            Long id = getEngine().createGame(p, gd);
            sendEvent(id, GAME_CREATED, gd);
            return id;
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Engine#joinGame(Player, Long)
	 */
    public Boolean joinGame(Player p, Long id) throws IllegalAccessException {
        if (getEngine() != null) {
            Boolean bool = getEngine().joinGame(p, id);
            if (bool) {
                sendEvent(id, GAME_JOINED, getGameDescription(id));
            }
            return bool;
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getGameDescription()
	 */
    public Description getGameDescription(Long id) {
        if (getControl(id) != null) return getControl(id).getGameDescription(); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Engine#getGameDescriptions()
	 */
    public List<Description> getGameDescriptions() throws IllegalAccessException {
        if (getEngine() != null) return getEngine().getGameDescriptions(); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#start(Player p)
	 */
    public Boolean start(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).start(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#end(Player p)
	 */
    public Boolean end(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) {
            Boolean bool = getControl(id).end(p);
            Game game = getControl(id).getGame();
            if (game == null || game.getEnded()) {
                getEngine().removeControl(id);
            }
            return bool;
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#isGameStarted()
	 */
    public Boolean isGameStarted(Long id) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).isGameStarted(); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#isGameCompleted()
	 */
    public Boolean isGameCompleted(Long id) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).isGameCompleted(); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getGameStatus(Player)
	 */
    public Status getGameStatus(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) {
            return getControl(id).getGameStatus(p);
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getDiscardCard()
	 */
    public Card getDiscardCard(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getDiscardCard(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getSourceCard()
	 */
    public Card getSourceCard(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getSourceCard(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getHeldCards(Long id, Player p)
	 */
    public List<Card> getHeldCards(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getHeldCards(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getBoardCard(Player, Position)
	 */
    public Card getBoardCard(Long id, Player p, Position pos) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getBoardCard(p, pos); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getBoardPosition(Player, Card)
	 */
    public Position getBoardPosition(Long id, Player p, Card card) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getBoardPosition(p, card); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getBoardPositions(Player)
	 */
    public List<Position> getBoardPositions(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getBoardPositions(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#drawFromDiscard(Player)
	 */
    public Card drawFromDiscard(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).drawFromDiscard(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#drawFromSource(Player)
	 */
    public Card drawFromSource(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).drawFromSource(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#drawFromHand(Player, Integer)
	 */
    public Card drawFromHand(Long id, Player p, Integer index) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).drawFromHand(p, index); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#playCard(Player, Position, Card)
	 */
    public Boolean playCard(Long id, Player p, Position pos, Card card) throws IllegalAccessException {
        if (getControl(id) != null) {
            return getControl(id).playCard(p, pos, card);
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#discardCard(Player, Card)
	 */
    public Boolean discardCard(Long id, Player p, Card card) throws IllegalAccessException {
        if (getControl(id) != null) {
            return getControl(id).discardCard(p, card);
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#holdCard(Player, Card, Integer)
	 */
    public Boolean holdCard(Long id, Player p, Card card, Integer index) throws IllegalAccessException {
        if (getControl(id) != null) {
            return getControl(id).holdCard(p, card, index);
        } else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#revealCard(Player, Position)
	 */
    public Card revealCard(Long id, Player p, Position pos) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).revealCard(p, pos); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#hideCard(Player, Position)
	 */
    public Boolean hideCard(Long id, Player p, Position pos) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).hideCard(p, pos); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#isPositionRevealed(Player, Position)
	 */
    public Boolean isPositionRevealed(Long id, Player p, Position pos) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).isPositionRevealed(p, pos); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#isPositionLocked(Player, Position)
	 */
    public Boolean isPositionLocked(Long id, Player p, Position pos) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).isPositionLocked(p, pos); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#sendMessage(Player, Message) 
	 */
    public Boolean sendMessage(Long id, Player p, Message m) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).sendMessage(p, m); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#sendSignal(Player, Object)
	 */
    public Boolean sendSignal(Long id, Player p, Object signal) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).sendSignal(p, signal); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#isSessionStarted(Player)
	 */
    public Boolean isSessionStarted(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).isSessionStarted(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#isSessionCompleted(Player)
	 */
    public Boolean isSessionCompleted(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).isSessionCompleted(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getSessionCount(Player)
	 */
    public Integer getSessionCount(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getSessionCount(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getSessionView(Player, String)
	 */
    public View getSessionView(Long id, Player p, String userID) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getSessionView(p, userID); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getSessionState(Player)
	 */
    public Integer getSessionState(Long id, Player p, String userID) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getSessionState(p, userID); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getSessionScore(Player, String)
	 */
    public Score getSessionScore(Long id, Player p, String userID) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getSessionScore(p, userID); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getSessionScores(Player)
	 */
    public List<Score> getSessionScores(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getSessionScores(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getGameScore(Player, String)
	 */
    public Score getGameScore(Long id, Player p, String userID) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getGameScore(p, userID); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getGameScores(Player)
	 */
    public List<Score> getGameScores(Long id, Player p) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getGameScores(p); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getCurrentUserID()
	 */
    public String getCurrentUserID(Long id) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getCurrentUserID(); else return null;
    }

    /**
	 * @see com.evver.evvercards.service.Control#getPlayers()
	 */
    public List<Player> getPlayers(Long id) throws IllegalAccessException {
        if (getControl(id) != null) return getControl(id).getPlayers(); else return null;
    }
}
