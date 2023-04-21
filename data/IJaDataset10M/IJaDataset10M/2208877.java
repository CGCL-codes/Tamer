package org.chess.quasimodo.event;

import org.chess.quasimodo.domain.logic.Board;

public class BoardMoveEvent extends MoveEvent {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7090148222246613979L;

    public BoardMoveEvent(Board source) {
        super(source);
    }
}
