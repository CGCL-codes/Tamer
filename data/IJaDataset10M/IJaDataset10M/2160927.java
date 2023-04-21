package org.chess.quasimodo.event;

import org.chess.quasimodo.domain.logic.ChessColor;

/**
 * To be implemented by classes listening for clock events.
 * @author Eugen Covaci
 *
 */
public interface ClockChangedAware {

    /**
	 * Called when clock changes.
	 * @param whiteElapsedTime White elapsed time (seconds).
	 * @param blackElapsedTime Black elapsed time (seconds).
	 */
    void setClockTime(long whiteElapsedTime, long blackElapsedTime);

    /**
	 * Called when game time is over.
	 * @param colorToMove The color to move.
	 */
    void timeOver(ChessColor colorToMove);
}
