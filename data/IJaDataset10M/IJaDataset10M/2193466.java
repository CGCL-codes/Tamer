package be.yildiz.client.graphic.entity;

import be.yildiz.client.engine.graphic.IVector;

/**
 * Interface used for all the movable units in the game.
 *
 * @author Van Den Borre Grégory
 * @version 1.0
 * @since 0.2
 */
public interface Movable {

    /**
     * Move a unit.
     *
     * @return true if the move is not finished.
     */
    boolean move();

    /**
     * Modify the unit destination.
     *
     * @param destination
     *            new destination.
     */
    void setDestination(IVector destination);
}
