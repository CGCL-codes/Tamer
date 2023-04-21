package de.jdufner.sudoku.solver.strategy.intersection.removal;

import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.common.misc.Level;
import de.jdufner.sudoku.solver.strategy.AbstractParallelStrategy;
import de.jdufner.sudoku.solver.strategy.configuration.StrategyNameEnum;

/**
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 120 $
 */
public final class IntersectionRemovalParallelStrategy extends AbstractParallelStrategy {

    public IntersectionRemovalParallelStrategy(final Grid sudoku) {
        super(sudoku);
        getCallables().add(new BoxLineReductionColumnStrategy(sudoku));
        getCallables().add(new BoxLineReductionRowStrategy(sudoku));
        getCallables().add(new PointingPairStrategy(sudoku));
    }

    @Override
    public Level getLevel() {
        return Level.MITTEL;
    }

    @Override
    public StrategyNameEnum getStrategyName() {
        return StrategyNameEnum.INTERSECTION_REMOVAL;
    }
}
