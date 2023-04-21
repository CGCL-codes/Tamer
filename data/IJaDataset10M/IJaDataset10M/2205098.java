package visugraph.gview.event;

import java.util.EventListener;

/**
 * Ce listener reçoit les évènements relatifs aux arêtes.
 * @param <E> type pour les arêtes.
 */
public interface EdgeListener<E> extends EventListener {

    /**
	 * Invoqué quand une arête a été cliquée.
	 */
    void edgeClicked(GraphComponentEvent<?, E> event);

    /**
	 * Invoqué quand une arête a été draggée.
	 */
    void edgeDragged(GraphComponentEvent<?, E> event);

    /**
	 * Invoqué quand une arête a été pressée.
	 */
    void edgePressed(GraphComponentEvent<?, E> event);

    /**
	 * Invoqué quand une arête a été relachée.
	 */
    void edgeReleased(GraphComponentEvent<?, E> event);
}
