package de.jdufner.sudoku.solver.strategy.ywing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.log4j.Logger;
import de.jdufner.sudoku.commands.Command;
import de.jdufner.sudoku.commands.RemoveCandidatesCommand.RemoveCandidatesCommandBuilder;
import de.jdufner.sudoku.common.board.Cell;
import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.common.board.HandlerUtil;
import de.jdufner.sudoku.common.board.Literal;
import de.jdufner.sudoku.common.board.Row;
import de.jdufner.sudoku.common.board.RowHandler;
import de.jdufner.sudoku.common.misc.Level;
import de.jdufner.sudoku.solver.strategy.configuration.StrategyNameEnum;

/**
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 2010-02-20
 * @version $Revision: 122 $
 */
public final class YWingRowStrategy extends AbstractYWingStrategy implements RowHandler, Callable<Collection<Command>> {

    private static final Logger LOG = Logger.getLogger(YWingRowStrategy.class);

    public YWingRowStrategy(final Grid sudoku) {
        super(sudoku);
    }

    @Override
    protected Collection<Command> executeStrategy() {
        HandlerUtil.forEachRow(getSudoku(), this);
        return getCommands();
    }

    @Override
    public Level getLevel() {
        return Level.MITTEL;
    }

    @Override
    public StrategyNameEnum getStrategyName() {
        return StrategyNameEnum.YWING;
    }

    @Override
    public void handleRow(final Row row) {
        final Set<Cell> nonFixed = row.getNonFixed(2);
        if (nonFixed.size() < 2) {
            return;
        }
        for (Cell firstCell : nonFixed) {
            for (Cell secondCell : nonFixed) {
                if (firstCell.getColumnIndex() < secondCell.getColumnIndex()) {
                    compareTwoCellsInRow(firstCell, secondCell);
                }
            }
        }
    }

    @Override
    public Collection<Command> call() throws Exception {
        return executeStrategy();
    }

    /**
   * Prüft ob die zwei Zellen einer Spalte einen gemeinsamen Kandidaten haben.
   * 
   * Erwartet zwei Zellen mit jeweils nur zwei Kandidaten.
   * 
   * Identifiziere einen gemeinsamen Kandidaten und die jeweils
   * unterschiedlichen Kandidaten.
   * 
   * Suche Zelle mit den unterschiedlichen Kandidaten in den Reihe der
   * übergebenen Zellen.
   * 
   * @param firstCell
   * @param secondCell
   */
    private void compareTwoCellsInRow(final Cell firstCell, final Cell secondCell) {
        final Literal commonCandidate = getCommonCandidate(firstCell, secondCell);
        if (commonCandidate == null) {
            return;
        }
        final Literal differentCandidateOfFirstCell = getDifferentCandidate(firstCell, commonCandidate);
        final Literal differentCandidateOfSecondCell = getDifferentCandidate(secondCell, commonCandidate);
        if (differentCandidateOfFirstCell.equals(differentCandidateOfSecondCell)) {
            return;
        }
        final Collection<Literal> searchCandidates = new ArrayList<Literal>();
        searchCandidates.add(differentCandidateOfFirstCell);
        searchCandidates.add(differentCandidateOfSecondCell);
        LOG.debug("Zwei Zellen in einer Zeile mit einem gemeinsamen Kandidaten " + "und jeweils einem unterschiedlichen Kandidaten gefunden: " + firstCell + " und " + secondCell);
        findInterceptCellInColumn(firstCell, secondCell, searchCandidates);
        findInterceptCellInColumn(secondCell, firstCell, searchCandidates);
        findInterceptCellInBlock(firstCell, secondCell, searchCandidates);
        findInterceptCellInBlock(secondCell, firstCell, searchCandidates);
    }

    /**
   * Suche Schnittpunkt zwischen den Zellen einer Spalte und der neuen Zelle
   * einer Reihe.
   * 
   * @param firstCell
   * @param secondCell
   * @param searchCandidates
   */
    private void findInterceptCellInColumn(final Cell firstCell, final Cell secondCell, final Collection<Literal> searchCandidates) {
        final Collection<Cell> cells1 = findCellsByCandidatesInColumn(searchCandidates, firstCell.getColumnIndex());
        if (cells1 != null && !cells1.isEmpty()) {
            LOG.debug("In der Spalte einer gefundenen Zellen wurden folgenden Zellen mit den unterschiedlichen Kandidaten gefunden: " + cells1);
            for (Cell cell : cells1) {
                final Cell foundCell = getSudoku().getCell(cell.getRowIndex(), secondCell.getColumnIndex());
                LOG.debug(foundCell);
                final Literal removableCandidate = getCommonCandidate(cell, secondCell);
                LOG.debug("Entferne Kandidat: " + removableCandidate);
                if (!foundCell.isFixed() && foundCell.getCandidates().contains(removableCandidate)) {
                    LOG.debug("Zelle gefunden: " + foundCell + " Erzeuge Kommando zum Entfernen von " + removableCandidate);
                    LOG.debug("Gefunden in: " + getSudoku());
                    getCommands().add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.YWING, foundCell).addCandidate(removableCandidate).build());
                }
            }
        }
    }

    /**
   * @param firstCell
   * @param secondCell
   * @param searchCandidates
   */
    private void findInterceptCellInBlock(final Cell firstCell, final Cell secondCell, final Collection<Literal> searchCandidates) {
        if (firstCell.getBlockIndex() == secondCell.getBlockIndex()) {
            return;
        }
        final Collection<Cell> cells1 = findCellsByCandidatesInBlock(searchCandidates, firstCell.getBlockIndex());
        if (cells1 != null && !cells1.isEmpty()) {
            LOG.debug("In dem Block einer gefundenen Zellen wurden folgenden Zellen mit den unterschiedlichen Kandidaten gefunden: " + cells1);
            for (Cell cell : cells1) {
                if (cell.getRowIndex() == firstCell.getRowIndex()) {
                    continue;
                }
                final Literal removableCandidate = getCommonCandidate(cell, secondCell);
                LOG.debug("Entferne Kandidat: " + removableCandidate);
                createRemoveCommands(firstCell, cell, removableCandidate);
                createRemoveCommands(cell, secondCell, removableCandidate);
            }
        }
    }

    private void createRemoveCommands(final Cell firstCell, final Cell secondCell, final Literal removableCandidate) {
        final Collection<Cell> cells = getSudoku().getCellByRowAndBlock(firstCell.getRowIndex(), secondCell.getBlockIndex());
        for (Cell foundCell : cells) {
            if (!foundCell.isFixed() && foundCell.getCandidates().contains(removableCandidate)) {
                LOG.debug("Zelle gefunden: " + foundCell + " Erzeuge Kommando zum Entfernen von " + removableCandidate);
                LOG.debug("Gefunden in: " + getSudoku());
                getCommands().add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.YWING, foundCell).addCandidate(removableCandidate).build());
            }
        }
    }
}
