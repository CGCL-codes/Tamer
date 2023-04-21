package net.sourceforge.strategema.games.movementrules;

import net.sourceforge.strategema.games.BoardGeometry;
import net.sourceforge.strategema.games.Coordinate;
import net.sourceforge.strategema.games.MovementRule;
import net.sourceforge.strategema.games.PieceInPlay;
import net.sourceforge.strategema.games.VolatileGameState;
import net.sourceforge.strategema.games.timers.ComputationTimer;
import net.sourceforge.strategema.util.datastructures.Pair;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A movement rule that moves a piece along the number of spaces determined by chance: dice, cards, coin tosses, etc.
 * 
 * @author Lizzy
 * 
 */
public abstract class ChanceMovementRule extends AbstractMovementRule {

    /** Underlying rule to determine the direction of movement. */
    protected final MovementRule movement;

    /** The current value attributed to a chance event. */
    private int chanceValue;

    /** True if {@link #chanceValue} is valid, or false if a new chance event is needed. */
    private boolean chanceValueSet;

    /**
	 * Constructor
	 * @param movement Underlying rule to determine the direction of movement.
	 * @throws NullPointerException If {@code movement} is null.
	 */
    public ChanceMovementRule(final MovementRule movement) throws NullPointerException {
        super(movement.maximalCaptures());
        this.movement = movement;
        this.chanceValue = 0;
        this.chanceValueSet = false;
    }

    @Override
    public boolean isValidMove(final VolatileGameState state, final BoardGeometry board, final PieceInPlay pieceInPlay, final List<Coordinate<?, ?, ?>> move, final ComputationTimer timer) {
        final boolean valid = super.isValidMove(state, board, pieceInPlay, move, timer);
        if (valid) {
            this.chanceValueSet = false;
        }
        return valid;
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getOccupiedSquareMoves(final VolatileGameState state, final BoardGeometry board, final Coordinate<?, ?, ?> position, final PieceInPlay piece, final ComputationTimer timer) {
        if (!this.chanceValueSet) {
            this.chanceValue = this.getValue(state);
            this.chanceValueSet = true;
        }
        final Set<List<Coordinate<?, ?, ?>>> moves = this.movement.getOccupiedSquareMoves(state, board, position, piece, timer);
        final Iterator<List<Coordinate<?, ?, ?>>> movesIter = moves.iterator();
        while (movesIter.hasNext()) {
            final List<Coordinate<?, ?, ?>> move = movesIter.next();
            if (move.size() != this.chanceValue + 1) {
                movesIter.remove();
            }
        }
        return moves;
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getPotentialMoves(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position, final ComputationTimer timer) {
        if (!this.chanceValueSet) {
            this.chanceValue = this.getValue(state);
            this.chanceValueSet = true;
        }
        final Set<List<Coordinate<?, ?, ?>>> moves = this.movement.getPotentialMoves(state, board, piece, position, timer);
        final Iterator<List<Coordinate<?, ?, ?>>> movesIter = moves.iterator();
        while (movesIter.hasNext()) {
            final List<Coordinate<?, ?, ?>> move = movesIter.next();
            if (move.size() != this.chanceValue + 1) {
                movesIter.remove();
            }
        }
        return moves;
    }

    @Override
    public Pair<PieceInPlay, List<Coordinate<?, ?, ?>>> getContinuation(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final List<Coordinate<?, ?, ?>> move) {
        return this.movement.getContinuation(state, board, piece, move);
    }

    /**
	 * Gets the value determined by chance.
	 * @param state The current game state.
	 * @return The chance value.
	 */
    protected abstract int getValue(final VolatileGameState state);
}
