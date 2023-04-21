package net.sourceforge.strategema.games.movementrules;

import net.sourceforge.strategema.games.BoardGeometry;
import net.sourceforge.strategema.games.Coordinate;
import net.sourceforge.strategema.games.MovementRule;
import net.sourceforge.strategema.games.PieceInPlay;
import net.sourceforge.strategema.games.VolatileGameState;
import net.sourceforge.strategema.games.capturingrules.DisplacementCapture;
import net.sourceforge.strategema.games.timers.ComputationTimer;
import net.sourceforge.strategema.util.datastructures.Pair;
import java.util.List;
import java.util.Set;

/**
 * A move that can only be performed when the destination square already contains a piece, e.g. to perform a
 * displacement capture.
 * 
 * @author Lizzy
 * 
 */
public class OccupiedSquareMovementRule extends AbstractMovementRule {

    /** The movement that can be performed when the destination is occupied. */
    private final MovementRule movement;

    /**
	 * Constructor
	 * @param movement The movement that can be performed when the destination is occupied.
	 */
    public OccupiedSquareMovementRule(final MovementRule movement) {
        super(movement.maximalCaptures());
        this.movement = movement;
    }

    /**
	 * Convenience method for pieces that capture using displacement using the same style of movement as they move when
	 * not capturing.
	 * @param movement The movement.
	 * @return A movement rule that allows the specified movement plus its displacement captures.
	 */
    public static MovementRule occupiedOrNot(final MovementRule movement) {
        return new CompoundMovementRule(movement.toString(), movement.maximalCaptures(), movement, new CaptureMovementRule(new OccupiedSquareMovementRule(movement), DisplacementCapture.CANNOT_CAPTURE_FRIENDLY));
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getOccupiedSquareMoves(final VolatileGameState state, final BoardGeometry board, final Coordinate<?, ?, ?> position, final PieceInPlay piece, final ComputationTimer timer) {
        return this.movement.getOccupiedSquareMoves(state, board, position, piece, timer);
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getPotentialMoves(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position, final ComputationTimer timer) {
        return this.movement.getOccupiedSquareMoves(state, board, position, piece, timer);
    }

    @Override
    public Pair<PieceInPlay, List<Coordinate<?, ?, ?>>> getContinuation(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final List<Coordinate<?, ?, ?>> move) {
        return this.movement.getContinuation(state, board, piece, move);
    }

    @Override
    public String toString() {
        return this.movement.toString() + " when the destination square is occupied by an enemy piece";
    }
}
