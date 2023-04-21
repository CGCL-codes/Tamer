package org.jcvi.common.core.align.pairwise;

import org.jcvi.common.core.align.SequenceAlignment;
import org.jcvi.common.core.symbol.Sequence;
import org.jcvi.common.core.symbol.residue.Residue;
import org.jcvi.common.core.util.MathUtil;

/**
 * {@code AbstractSmithWatermanAligner} 
 * implements methods in {@link AbstractPairwiseAligner} 
 * with  Smith-Waterman (with Gotoh improvements) specific implementations.
 * 
 * @author dkatzel
 *
 * @param <R> the type of {@link Residue} used in this aligner.
 * @param <S> the {@link Sequence} type input into this aligner.
 * @param <A> the {@link SequenceAlignment} type returned by this aligner.
 * @param <P> the {@link PairwiseSequenceAlignment} type returned by this aligner.
 */
abstract class AbstractSmithWatermanAligner<R extends Residue, S extends Sequence<R>, A extends SequenceAlignment<R, S>, P extends PairwiseSequenceAlignment<R, S>> extends AbstractPairwiseAligner<R, S, A, P> {

    protected AbstractSmithWatermanAligner(Sequence<R> query, Sequence<R> subject, ScoringMatrix<R> matrix, float openGapPenalty, float extendGapPenalty, ResiduePairwiseStrategy<R, S, A, P> pairwiseStrategy) {
        super(query, subject, matrix, openGapPenalty, extendGapPenalty, pairwiseStrategy);
    }

    /**
	 * All initial gap scores are set to {@literal 0}.
	 * </p>
	 * {@inheritDoc}
	 */
    @Override
    protected float[] getInitialGapScores(int length, float openGapPenalty, float extendGapPenalty) {
        return new float[length];
    }

    /**
	 * Since Smith-Waterman is a local alignment all 
	 * values in the initial row should terminate the alignment
	 * if the query and subject don't align at that base.
	 * 
	 * </p>
	 * {@inheritDoc}
	 * @return TracebackDirection#TERMINAL
	 */
    @Override
    protected TracebackDirection getInitialRowTracebackValue() {
        return TracebackDirection.TERMINAL;
    }

    /**
	 * Since Smith-Waterman is a local alignment all 
	 * values in the initial column should terminate the alignment
	 * if the query and subject don't align at that base.
	 * </p>
	 * {@inheritDoc}
	 * @return TracebackDirection#TERMINAL
	 */
    @Override
    protected TracebackDirection getInitialColTracebackValue() {
        return TracebackDirection.TERMINAL;
    }

    /**
	 * Returns a {@link WalkBack} using the max of the 3 input values and 
	 * zero.  The value Zero denotes a terminal traceback so 
	 * no chosen score can ever be less than that.
	 * <p/>
	 * {@inheritDoc}
	 */
    @Override
    protected WalkBack computeBestWalkBack(float alignmentScore, float horrizontalGapPenalty, float verticalGapPenalty) {
        float bestScore = MathUtil.maxOf(alignmentScore, horrizontalGapPenalty, verticalGapPenalty, 0F);
        final TracebackDirection dir;
        if (bestScore == 0) {
            dir = TracebackDirection.TERMINAL;
        } else if (bestScore == alignmentScore) {
            dir = TracebackDirection.DIAGNOL;
        } else if (bestScore == horrizontalGapPenalty) {
            dir = TracebackDirection.HORIZONTAL;
        } else {
            dir = TracebackDirection.VERTICAL;
        }
        return new WalkBack(bestScore, dir);
    }

    /**
	 * Only update the current {@link StartPoint}
	 * if the given score is greater than the 
	 * current starting point's score.
	 * <p/>
	 * {@inheritDoc}
	 */
    @Override
    protected StartPoint updateCurrentStartPoint(float currentCellScore, StartPoint currentStartPoint, int i, int j) {
        if (currentCellScore > currentStartPoint.getScore()) {
            return new StartPoint(i, j, currentCellScore);
        }
        return currentStartPoint;
    }
}
