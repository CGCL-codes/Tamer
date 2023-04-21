package it.timehero.util;

import it.timehero.world.MovementNotify;
import it.timehero.world.WorldEngine;
import java.util.Observable;

/**
 * Gestisce gli input dell'agente e li notifica al WorldEngine
 * 
 * @author AM
 * @project Timehero0.2
 */
public class AgentInputController extends Observable {

    public AgentInputController(WorldEngine we) {
        addObserver(we);
    }

    /**
	 * All'input dell'agente notifica al WorldEngine la sua mossa
	 * 
	 * @param container
	 * @param game
	 * @param delta
	 */
    public void getInput(String direction, int delta, String ID) {
        boolean keyRight = direction.equalsIgnoreCase(Helper.RIGHT_DIR);
        boolean keyLeft = direction.equalsIgnoreCase(Helper.LEFT_DIR);
        boolean keyUp = direction.equalsIgnoreCase(Helper.UP_DIR);
        boolean keyDown = direction.equalsIgnoreCase(Helper.DOWN_DIR);
        if (keyRight) {
            setChanged();
            notifyObservers(new MovementNotify(ID, Helper.RIGHT_DIR, delta));
        }
        if (keyLeft) {
            setChanged();
            notifyObservers(new MovementNotify(ID, Helper.LEFT_DIR, delta));
        }
        if (keyUp) {
            setChanged();
            notifyObservers(new MovementNotify(ID, Helper.UP_DIR, delta));
        }
        if (keyDown) {
            setChanged();
            notifyObservers(new MovementNotify(ID, Helper.DOWN_DIR, delta));
        }
    }
}
