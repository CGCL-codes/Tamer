package da.squares.items;

import da.squares.player.Player;

/**
 * Represents a single square within a Dots and Squares board. The object is
 * attached to a <code>Board</code> and only provides methods to validate the
 * square's state. The state itself is updated as <code>Lines</code> are
 * created (drawn/played).
 *
 * @author Dominique Archambault
 */
public final class Square {

    private Line topLine;

    private Line leftLine;

    private Line rightLine;

    private Line bottomLine;

    private Player owner;

    /**
	 * Creates a new square with the provided top, left, right and bottom
	 * <code>Line</code>s.
	 *
	 * @param topLine Top line of the square
	 * @param leftLine Left line of the square
	 * @param rightLine Right line of the square
	 * @param bottomLine Bottom line of the square
	 */
    Square(Line topLine, Line leftLine, Line rightLine, Line bottomLine) {
        super();
        this.topLine = topLine;
        topLine.associate(this);
        this.leftLine = leftLine;
        leftLine.associate(this);
        this.rightLine = rightLine;
        rightLine.associate(this);
        this.bottomLine = bottomLine;
        bottomLine.associate(this);
    }

    /**
	 * Creates a new square for a cloned <code>Board</code>, with the provided
	 * top, left, right and bottom <code>Line</code>s, and possibly owned by the
	 * provided <code>owner</code> <code>Player</code>.
	 *
	 * @param topLine Top line of the square
	 * @param leftLine Left line of the square
	 * @param rightLine Right line of the square
	 * @param bottomLine Bottom line of the square
	 * @param owner Player that owns the original copy of this square
	 */
    Square(Line topLine, Line leftLine, Line rightLine, Line bottomLine, Player owner) {
        this(topLine, leftLine, rightLine, bottomLine);
        this.owner = owner;
    }

    /**
	 * Called by a <code>Line</code> when it has been updated, whether it was
	 * drawn or cleared. This method will update the square's state accordingly,
	 * also updating the square's owner. It will return <code>true</code> if the
	 * square is complete as a result of a drawn line, and <code>false</code>
	 * otherwise.
	 *
	 * @param player Player that drew the line, and who may now own the square.
	 *
	 * @return boolean <code>true</code> if square is complete, and <code>false
	 *         </code> otherwise.
	 */
    boolean lineUpdated(Player player) {
        if (topLine.isCreated() && leftLine.isCreated() && rightLine.isCreated() && bottomLine.isCreated()) owner = player; else owner = null;
        return isComplete();
    }

    /**
	 * Returns the owning <code>Player</code> if square is complete, or returns
	 * <code>null</code> if the square is not yet claimed.
	 *
	 * @return Player Owning player, or <code>null</code> if not claimed
	 */
    public Player getOwner() {
        return owner;
    }

    /**
	 * Returns whether or not this square is complete and thus has been claimed.
	 *
	 * @return boolean <code>true</code> if square is claimed, and <code>false
	 *         </code> otherwise.
	 */
    public boolean isComplete() {
        return owner != null;
    }

    /**
	 * Top line of the square. Shared with the upper square if any.
	 *
	 * @return Line
	 */
    public Line getTopLine() {
        return topLine;
    }

    /**
	 * Left line of the square. Shared with the left square if any.
	 *
	 * @return Line
	 */
    public Line getLeftLine() {
        return leftLine;
    }

    /**
	 * Right line of the square. Shared with the right square if any.
	 *
	 * @return Line
	 */
    public Line getRightLine() {
        return rightLine;
    }

    /**
	 * Bottom line of the square. Shared with the lower square if any.
	 *
	 * @return Line
	 */
    public Line getBottomLine() {
        return bottomLine;
    }
}
