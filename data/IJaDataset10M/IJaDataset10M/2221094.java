package com.evver.games.nopeeking.simulate;

import static com.evver.evvercards.events.Event.Classification.NEXT_PLAYER;
import static com.evver.evvercards.events.Event.Classification.PLAYER_READY;
import static com.evver.evvercards.events.Event.Classification.PLAYER_REMOVED;
import static com.evver.evvercards.events.Event.Classification.SESSION_STARTED;
import java.util.List;
import com.evver.evvercards.Player;
import com.evver.evvercards.events.Event;
import com.evver.evvercards.events.EventListener;
import com.evver.evvercards.service.Control;

public class NPPlayer extends Player implements EventListener {

    private static final long serialVersionUID = 1L;

    private Control control = null;

    private Memory memory = null;

    private boolean isInitialized = false;

    /**
	 * Construct a golf player
	 * @param control the game control
	 */
    public NPPlayer(String name, Control control) {
        super(name);
        this.control = control;
        this.control.addListener(this);
    }

    @Override
    public Boolean getAI() {
        return true;
    }

    /**
	 * Gets the players memory
	 * @return the memory
	 */
    public Memory getMemory() {
        if (this.memory == null) this.memory = new Memory();
        return this.memory;
    }

    /**
	 * Listens for golf events
	 */
    public void onServiceEvent(Long gameId, Event event) {
        try {
            if (control.getGameID().equals(gameId) && event.getEventClassification().equals(NEXT_PLAYER)) {
                if (getUsername().equals(event.getEventData())) {
                    isInitialized = false;
                    Memory mem = getMemory();
                    AIUtils.smartPlayerMove(this, mem, control);
                }
            } else if (control.getGameID().equals(gameId) && event.getEventClassification().equals(PLAYER_READY)) {
                if (!getUsername().equals(event.getEventData()) && !isInitialized) {
                    isInitialized = true;
                    control.start(this);
                }
            } else if (control.getGameID().equals(gameId) && event.getEventClassification().equals(PLAYER_REMOVED)) {
                if (!getUsername().equals(event.getEventData())) {
                    boolean onlyAI = true;
                    List<Player> players = control.getPlayers();
                    for (Player gp : players) {
                        if (!gp.getAI()) {
                            onlyAI = false;
                            break;
                        }
                    }
                    if (onlyAI) {
                        control.removeListener(this);
                        control.end(this);
                    }
                }
            } else if (control.getGameID().equals(gameId) && event.getEventClassification().equals(SESSION_STARTED)) {
                memory = null;
                memory = new Memory();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
