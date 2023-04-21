package tgreiner.amy.reversi.engine;

/**
 * A position in a reversi game.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class Position {

    /** Bitboard of white's stones. */
    private long whiteMask;

    /** Bitboard of black's stones. */
    private long blackMask;

    /** The side to move. */
    private boolean wtm;

    /** The score. */
    private int score;

    /** Indicates whether the score has been set. */
    boolean scoreSet = false;

    /** The tree size below this position (e.g. after mini-maxing the tree) */
    private int size = 1;

    /**
     * Create a position.
     *
     * @param whiteMask bitboard of white's stones
     * @param blackMask bitboard of black's stones
     * @param wtm the side to move
     */
    public Position(final long whiteMask, final long blackMask, final boolean wtm) {
        this.whiteMask = whiteMask;
        this.blackMask = blackMask;
        this.wtm = wtm;
    }

    /** @see java.lang.Object#equals */
    public boolean equals(final Object o) {
        if (!(o instanceof Position)) {
            return false;
        }
        Position p = (Position) o;
        return (p.whiteMask == whiteMask) && (p.blackMask == blackMask) && (p.wtm == wtm);
    }

    /** @see java.lang.Object#hashCode */
    public int hashCode() {
        return (int) (whiteMask ^ (blackMask >> 32));
    }

    /**
     * Get white stones.
     *
     * @return bitboard of white's stones
     */
    public long getWhite() {
        return whiteMask;
    }

    /**
     * Get black stones.
     *
     * @return bitboard of black's stones
     */
    public long getBlack() {
        return blackMask;
    }

    /**
     * Get the side to move.
     *
     * @return the side to move
     */
    public boolean isWtm() {
        return wtm;
    }

    /**
     * Set the score.
     *
     * @param theScore the score
     */
    public void setScore(final int theScore) {
        if (scoreSet && (theScore != this.score)) {
            throw new RuntimeException("Trying to set score twice...");
        }
        this.score = theScore;
        scoreSet = true;
    }

    /**
     * Get the score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
	 * Get the tree size.
	 *
	 * @return the tree size.
	 */
    public int getSize() {
        return size;
    }

    /**
	 * Set the tree size.
	 *
	 * @param newSize the new size.
	 *
	 */
    public void setSize(final int newSize) {
        size = newSize;
    }
}
