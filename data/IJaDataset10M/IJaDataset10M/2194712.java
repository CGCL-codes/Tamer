package free.jin.board;

import java.awt.Color;
import free.chess.Square;

/**
 * Represents an arrow on the board.
 */
public final class Arrow {

    /**
   * The "from" square of the arrow.
   */
    private final Square from;

    /**
   * The "to" square of the arrow.
   */
    private final Square to;

    /**
   * The color of the arrow;
   */
    private final Color color;

    /**
   * Creates a new Arrow with the specified "to" and "from" squares color.
   */
    public Arrow(Square from, Square to, Color color) {
        if ((from == null) || (to == null) || (color == null)) throw new IllegalArgumentException("Null value not allowed");
        this.from = from;
        this.to = to;
        this.color = color;
    }

    /**
   * Returns the "from" square of the arrow.
   */
    public Square getFrom() {
        return from;
    }

    /**
   * Returns the "to" square of the arrow.
   */
    public Square getTo() {
        return to;
    }

    /**
   * Returns the color of the arrow.
   */
    public Color getColor() {
        return color;
    }

    /**
   * Returns whether this arrow is the same as the specified one. Two arrows are
   * the same if they have the same "from" and "to" squares and the same color.
   */
    public boolean equals(Object o) {
        if (!(o instanceof Arrow)) return false;
        Arrow a = (Arrow) o;
        return from.equals(a.from) && to.equals(a.to) && color.equals(a.color);
    }

    /**
   * Returns the hashcode of this arrow.
   */
    public int hashCode() {
        int result = 17;
        result = 37 * result + from.hashCode();
        result = 37 * result + to.hashCode();
        result = 37 * result + color.hashCode();
        return result;
    }
}
