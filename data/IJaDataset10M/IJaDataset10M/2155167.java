package de.jdufner.sudoku.commands;

import de.jdufner.sudoku.common.board.Literal;
import de.jdufner.sudoku.common.board.Grid;
import de.jdufner.sudoku.solver.strategy.configuration.StrategyNameEnum;

/**
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 123 $
 */
public final class UnsetValueCommand extends AbstractSingleValueCommand {

    private static final String JAVASCRIPT_COMMAND_NAME = "UV";

    private UnsetValueCommand(final StrategyNameEnum strategyNameEnum, final int row, final int column, final Literal value) {
        super(strategyNameEnum);
        this.rowIndex = row;
        this.columnIndex = column;
        this.value = value;
    }

    @Override
    public void executeCommand(final Grid sudoku) {
        if (sudoku.getCell(rowIndex, columnIndex).isFixed()) {
            sudoku.getCell(rowIndex, columnIndex).setValue(Literal.EMPTY);
            successfully = true;
        } else {
            successfully = false;
        }
    }

    @Override
    public void unexecuteCommand(final Grid sudoku) {
        sudoku.getCell(rowIndex, columnIndex).setValue(value);
    }

    @Override
    public boolean reversible() {
        return true;
    }

    @Override
    public String toString() {
        return getStrategyName() + ": Entferne Wert " + value + " aus Zelle (" + rowIndex + ", " + columnIndex + ")";
    }

    @Override
    protected String toString(final Grid sudoku) {
        return getStrategyName() + ": Entferne Wert " + value + " aus Zelle " + getCell(sudoku);
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof SetCandidateCommand) {
            return super.equals(other);
        }
        return false;
    }

    @Override
    public String toJavascriptString() {
        return getJavascriptCellNumber() + JAVASCRIPT_COMMAND_NAME + value.getValue();
    }

    public static class UnsetValueCommandBuilder {

        private final transient StrategyNameEnum strategyNameEnum;

        private final transient int rowIndex;

        private final transient int columnIndex;

        private final transient Literal value;

        public UnsetValueCommandBuilder(final StrategyNameEnum strategyNameEnum, final int rowIndex, final int columnIndex, final Literal value) {
            this.strategyNameEnum = strategyNameEnum;
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
            this.value = value;
        }

        public UnsetValueCommand build() {
            return new UnsetValueCommand(strategyNameEnum, rowIndex, columnIndex, value);
        }
    }
}
