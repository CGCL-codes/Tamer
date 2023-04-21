package de.jdufner.sudoku.solver.strategy.intersection.removal;

import java.util.Collection;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import de.jdufner.sudoku.commands.Command;
import de.jdufner.sudoku.commands.RemoveCandidatesCommand.RemoveCandidatesCommandBuilder;
import de.jdufner.sudoku.common.board.Box;
import de.jdufner.sudoku.common.board.BoxHandler;
import de.jdufner.sudoku.common.board.Cell;
import de.jdufner.sudoku.common.board.Column;
import de.jdufner.sudoku.common.board.HandlerUtil;
import de.jdufner.sudoku.common.board.Literal;
import de.jdufner.sudoku.common.board.Literal2CellMap;
import de.jdufner.sudoku.common.board.Row;
import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.common.misc.Level;
import de.jdufner.sudoku.solver.strategy.AbstractStrategy;
import de.jdufner.sudoku.solver.strategy.configuration.StrategyNameEnum;

/**
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 120 $
 */
public final class PointingPairStrategy extends AbstractStrategy implements BoxHandler, Callable<Collection<Command>> {

    private static final Logger LOG = Logger.getLogger(PointingPairStrategy.class);

    public PointingPairStrategy(final Grid sudoku) {
        super(sudoku);
    }

    @Override
    public Level getLevel() {
        return Level.MITTEL;
    }

    @Override
    public StrategyNameEnum getStrategyName() {
        return StrategyNameEnum.INTERSECTION_REMOVAL;
    }

    @Override
    public Collection<Command> executeStrategy() {
        HandlerUtil.forEachBlock(getSudoku(), this);
        return getCommands();
    }

    public void handleBlock(final Box block) {
        final Literal2CellMap literal2CellMapBlock = new Literal2CellMap(block.getCells());
        for (Literal testCandidate : block.getCandidates()) {
            checkColumn(literal2CellMapBlock, testCandidate);
            checkRow(literal2CellMapBlock, testCandidate);
        }
    }

    public Collection<Command> call() {
        return executeStrategy();
    }

    private void checkColumn(final Literal2CellMap literal2CellMapBlock, final Literal testCandidate) {
        if (literal2CellMapBlock.getCellsContainingLiteral(testCandidate).size() > 1 && literal2CellMapBlock.getCellsContainingLiteral(testCandidate).size() < getSudoku().getSize().getBoxHeight()) {
            if (areCellsInSameColumn(literal2CellMapBlock.getCellsContainingLiteral(testCandidate))) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Found candidate " + testCandidate + " in column " + getSudoku().getColumn(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getColumnIndex()) + " in block " + getSudoku().getBlock(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getBlockIndex()) + " only");
                }
                removeCandidateInColumnExceptInBlock(testCandidate, getSudoku().getColumn(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getColumnIndex()), getSudoku().getBlock(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getBlockIndex()));
            }
        }
    }

    private boolean areCellsInSameColumn(final Collection<Cell> cells) {
        Column column = null;
        for (Cell cell : cells) {
            if (column == null) {
                column = getSudoku().getColumn(cell.getColumnIndex());
            }
            if (!column.equals(getSudoku().getColumn(cell.getColumnIndex()))) {
                return false;
            }
        }
        return true;
    }

    private void removeCandidateInColumnExceptInBlock(final Literal testCandidate, final Column column, final Box block) {
        for (Cell cell : column.getNonFixed()) {
            if (!getSudoku().getBlock(cell.getBlockIndex()).equals(block) && cell.getCandidates().contains(testCandidate)) {
                getCommands().add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.INTERSECTION_REMOVAL, cell).addCandidate(testCandidate).build());
            }
        }
    }

    private void checkRow(final Literal2CellMap literal2CellMapBlock, final Literal testCandidate) {
        if (literal2CellMapBlock.getCellsContainingLiteral(testCandidate).size() > 1 && literal2CellMapBlock.getCellsContainingLiteral(testCandidate).size() < getSudoku().getSize().getBoxWidth()) {
            if (areCellsInSameRow(literal2CellMapBlock.getCellsContainingLiteral(testCandidate))) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Found candidate " + testCandidate + " in row " + getSudoku().getRow(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getRowIndex()) + " in block " + getSudoku().getBlock(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getBlockIndex()) + " only");
                }
                removeCandidateInRowExceptInBlock(testCandidate, getSudoku().getRow(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getRowIndex()), getSudoku().getBlock(literal2CellMapBlock.getCellsContainingLiteral(testCandidate).first().getBlockIndex()));
            }
        }
    }

    private boolean areCellsInSameRow(final Collection<Cell> cells) {
        Row row = null;
        for (Cell cell : cells) {
            if (row == null) {
                row = getSudoku().getRow(cell.getRowIndex());
            }
            if (!row.equals(getSudoku().getRow(cell.getRowIndex()))) {
                return false;
            }
        }
        return true;
    }

    private void removeCandidateInRowExceptInBlock(final Literal testCandidate, final Row row, final Box block) {
        for (Cell cell : row.getNonFixed()) {
            if (!getSudoku().getBlock(cell.getBlockIndex()).equals(block) && cell.getCandidates().contains(testCandidate)) {
                getCommands().add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.INTERSECTION_REMOVAL, cell).addCandidate(testCandidate).build());
            }
        }
    }
}
