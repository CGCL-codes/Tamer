package it.diamonds.grid;

import it.diamonds.GemAction;
import it.diamonds.gems.BigGem;
import it.diamonds.gems.BigGemList;
import it.diamonds.gems.Droppable;
import it.diamonds.gems.Cell;

public class CreateNewBigGemsAction implements GemAction {

    private Grid grid;

    private BigGemList bigGems;

    public CreateNewBigGemsAction(Grid grid, BigGemList bigGems) {
        this.grid = grid;
        this.bigGems = bigGems;
    }

    public void applyOn(Droppable gem) {
        Cell gridObject = gem.getCell();
        int row = gridObject.getRow();
        int column = gridObject.getColumn();
        if (isGemNotValidForBigGem(gem)) {
            return;
        }
        if (isGemNeighbourValidForBigGem(gem, -1, 0) && isGemNeighbourValidForBigGem(gem, 0, 1) && isGemNeighbourValidForBigGem(gem, -1, 1)) {
            BigGem bigGem = new BigGem(row, column, gem.getColor());
            bigGem.addGem(gem);
            bigGem.addGem(grid.getGemAt(row - 1, column));
            bigGem.addGem(grid.getGemAt(row, column + 1));
            bigGem.addGem(grid.getGemAt(row - 1, column + 1));
            bigGems.add(bigGem);
        }
    }

    private boolean isGemNotValidForBigGem(Droppable gem) {
        Cell gridObject = gem.getCell();
        if (grid.isCellInABigGem(gridObject.getRow(), gridObject.getColumn())) {
            return true;
        }
        if (!gem.getType().isGem()) {
            return true;
        }
        return false;
    }

    private boolean isGemNeighbourValidForBigGem(Droppable gem, int rowOffset, int columnOffset) {
        Cell cell = gem.getCell();
        int otherColumn = cell.getColumn() + columnOffset;
        int otherRow = cell.getRow() + rowOffset;
        if (!grid.isValidCell(otherRow, otherColumn)) {
            return false;
        }
        if (!grid.isGemAt(otherRow, otherColumn)) {
            return false;
        }
        if (grid.isCellInABigGem(otherRow, otherColumn)) {
            return false;
        }
        Droppable otherGem = grid.getGemAt(otherRow, otherColumn);
        if (otherGem.isFalling()) {
            return false;
        }
        if (gem.isSameOf(otherGem)) {
            return true;
        }
        return false;
    }
}
