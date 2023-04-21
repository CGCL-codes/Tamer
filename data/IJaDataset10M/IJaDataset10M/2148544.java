package org.openaion.gameserver.controllers.movement;

/**
 * @author ATracer
 *
 */
public class StartMovingListener extends ActionObserver {

    private boolean effectorMoved = false;

    public StartMovingListener() {
        super(ObserverType.MOVE);
    }

    /**
	 * @return the effectorMoved
	 */
    public boolean isEffectorMoved() {
        return effectorMoved;
    }

    @Override
    public void moved() {
        effectorMoved = true;
    }
}
