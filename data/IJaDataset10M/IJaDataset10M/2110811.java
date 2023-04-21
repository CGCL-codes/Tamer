package net.sourceforge.strategema.games.movementrules;

import net.sourceforge.strategema.games.BoardGeometry;
import net.sourceforge.strategema.games.Coordinate;
import net.sourceforge.strategema.games.MovementRule;
import net.sourceforge.strategema.games.PieceInPlay;
import net.sourceforge.strategema.games.VolatileGameState;
import net.sourceforge.strategema.games.timers.ComputationTimer;
import net.sourceforge.strategema.util.datastructures.Pair;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A movement rule that a piece can only make as the first/second/third/etc. action in their turn.
 * 
 * @author Lizzy
 * 
 */
public class ActionNumberMovementRule implements MovementRule {

    /** The underlying movement rule. */
    private final MovementRule movement;

    /** A bit mask. Bit 0 zero set means the move can be performed as the first action, and so on. */
    private final int actionNumberMask;

    /**
	 * Constructor
	 * @param movement The underlying movement rule.
	 * @param actionNumberMask A bit mask. Bit 0 zero set means the move can be performed as the first action, and so on.
	 */
    public ActionNumberMovementRule(final MovementRule movement, final int actionNumberMask) {
        this.movement = movement;
        this.actionNumberMask = actionNumberMask;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append(this.movement);
        buf.append(" as the ");
        boolean firstActionNum = true;
        int remaining = this.actionNumberMask;
        if ((remaining & 1) != 0) {
            remaining = remaining & ~1;
            buf.append("first");
            firstActionNum = false;
        }
        if ((remaining & 2) != 0) {
            remaining = remaining & ~2;
            if (!firstActionNum) {
                if (remaining != 0) {
                    buf.append(", ");
                } else {
                    buf.append(" or ");
                }
            }
            buf.append("second");
            firstActionNum = false;
        }
        if ((remaining & 4) != 0) {
            remaining = remaining & ~4;
            if (!firstActionNum) {
                if (remaining != 0) {
                    buf.append(", ");
                } else {
                    buf.append(" or ");
                }
            }
            buf.append("third");
            firstActionNum = false;
        }
        for (int i = 3; i < 32; i++) {
            if (remaining == 0) {
                break;
            }
            final int pow = (1 << i);
            if ((remaining & pow) != 0) {
                remaining = remaining & ~pow;
                if (!firstActionNum) {
                    if (remaining != 0) {
                        buf.append(", ");
                    } else {
                        buf.append(" or ");
                    }
                }
                buf.append(i + 1);
                buf.append("th");
                firstActionNum = false;
            }
        }
        buf.append(" action in the turn");
        return buf.toString();
    }

    @Override
    public Pair<PieceInPlay, List<Coordinate<?, ?, ?>>> getContinuation(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final List<Coordinate<?, ?, ?>> move) {
        return this.movement.getContinuation(state, board, piece, move);
    }

    @Override
    public boolean isValidContinuation(final VolatileGameState state, final BoardGeometry board, final PieceInPlay firstPiece, final List<Coordinate<?, ?, ?>> mainMove, final PieceInPlay secondPiece, final List<Coordinate<?, ?, ?>> continuation) throws IndexOutOfBoundsException {
        return this.movement.isValidContinuation(state, board, firstPiece, mainMove, secondPiece, continuation);
    }

    @Override
    public Map<Coordinate<?, ?, ?>, Collection<Pair<Coordinate<?, ?, ?>, PieceInPlay>>> getCapturingMoves(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position, final ComputationTimer timer) {
        if ((this.actionNumberMask & (1 << (state.getActionNumber() - 1))) == 0) {
            return Collections.emptyMap();
        } else {
            return this.movement.getCapturingMoves(state, board, piece, position, timer);
        }
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getOccupiedSquareMoves(final VolatileGameState state, final BoardGeometry board, final Coordinate<?, ?, ?> position, final PieceInPlay piece, final ComputationTimer timer) {
        if ((this.actionNumberMask & (1 << (state.getActionNumber() - 1))) == 0) {
            return Collections.emptySet();
        } else {
            return this.movement.getOccupiedSquareMoves(state, board, position, piece, timer);
        }
    }

    @Override
    public Collection<Pair<Coordinate<?, ?, ?>, PieceInPlay>> getPinnedPieces(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position, final ComputationTimer timer) {
        return this.movement.getPinnedPieces(state, board, piece, position, timer);
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getPossibleMaximalCaptureMoves(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position, final ComputationTimer timer) {
        if ((this.actionNumberMask & (1 << (state.getActionNumber() - 1))) == 0) {
            return Collections.emptySet();
        } else {
            return this.movement.getPossibleMaximalCaptureMoves(state, board, piece, position, timer);
        }
    }

    @Override
    public Set<MovementRule> getPossibleRules(final boolean allRules) {
        return Collections.singleton((MovementRule) this);
    }

    @Override
    public Set<List<Coordinate<?, ?, ?>>> getPotentialMoves(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final Coordinate<?, ?, ?> position, final ComputationTimer timer) {
        if ((this.actionNumberMask & (1 << (state.getActionNumber() - 1))) == 0) {
            return Collections.emptySet();
        } else {
            return this.movement.getPotentialMoves(state, board, piece, position, timer);
        }
    }

    @Override
    public boolean isValidMove(final VolatileGameState state, final BoardGeometry board, final PieceInPlay piece, final List<Coordinate<?, ?, ?>> move, final ComputationTimer timer) throws IndexOutOfBoundsException, NullPointerException {
        if ((this.actionNumberMask & (1 << (state.getActionNumber() - 1))) == 0) {
            return false;
        } else {
            return this.movement.isValidMove(state, board, piece, move, timer);
        }
    }

    @Override
    public boolean maximalCaptures() {
        return this.movement.maximalCaptures();
    }

    @Override
    public ActionNumberMovementRule clone() {
        return this;
    }
}
