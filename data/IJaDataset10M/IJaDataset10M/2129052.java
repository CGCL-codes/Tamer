package tgreiner.amy.chess.engine;

/**
 * An implementation of a simple transposition table.
 *
 * @author Thorsten Greiner
 */
public class TransTableImpl implements TransTable {

    /** Size of the transposition table. */
    private int size;

    /** Mask for the hashkey. */
    private int mask;

    /** The transposition table. */
    private TTEntry[] table;

    /**
     * Create a TranspositionTable.
     *
     * @param bits the number of entries in the table. This implementation
     *             requires size to be a power of two.
     */
    public TransTableImpl(final int bits) {
        size = (1 << bits);
        mask = size - 1;
        table = new TTEntry[size];
        for (int i = 0; i < size; i++) {
            table[i] = new TTEntry();
        }
    }

    /**
     * Retrieve an entry from the transposition table.
     *
     * @param hashkey the hashkey of the entry.
     * @return the entry found or <code>null</code>.
     */
    public TTEntry get(final long hashkey) {
        int idx = (int) (hashkey & mask);
        TTEntry entry = table[idx];
        if (entry != null && entry.hashkey != hashkey) {
            entry = null;
        }
        return entry;
    }

    /**
     * Store an entry in the transposition table.
     *
     * @param hashkey the hashkey.
     * @param move the move.
     * @param depth the search depth.
     * @param score the score.
     * @param alpha the alpha value.
     * @param beta the beta value.
     */
    public void store(final long hashkey, final int move, final int depth, final int score, final int alpha, final int beta) {
        int idx = (int) (hashkey & mask);
        TTEntry entry = table[idx];
        if (depth > entry.getDepth()) {
            entry.set(hashkey, move, depth, score, alpha, beta);
        }
    }

    /**
     * Clear the transposition table.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            table[i].set(0, 0, 0, 0, 0, 0);
        }
    }
}
