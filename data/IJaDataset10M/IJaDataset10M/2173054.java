package datastructure.board.elements;

import common.Position;

/**
 * User: Lars Christian Ersly
 * Date: 03-06-11
 * Time: 14:03
 */
public class LaserEmitterBoardElement extends BoardElement {

    @Override
    public String toString() {
        return "pos: " + getPositionInGrid() + " length: " + getLength();
    }

    private int length;

    /**
     * creates a new BoardElement
     *
     * @param pos       the position
     * @param length    the length of the laser
     */
    public LaserEmitterBoardElement(Position pos, int length) {
        super(pos);
        this.length = length;
    }

    /**
     * @return the length of the laser beam
     */
    public int getLength() {
        return length;
    }
}
