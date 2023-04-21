package fr.univ_tln.inf9.exaplanning.api.salle.event;

import java.util.EventObject;

/**
 * @author pivi
 *
 */
public class Position_yChangedEvent extends EventObject {

    private float newPosition_y;

    public Position_yChangedEvent(Object source, float f) {
        super(source);
        this.newPosition_y = f;
    }

    /**
	 * @return the newType
	 */
    public float getNewPosition_y() {
        return newPosition_y;
    }
}
