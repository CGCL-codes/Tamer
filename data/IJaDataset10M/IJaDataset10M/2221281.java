package tico.editor.actions;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import org.jgraph.graph.AttributeMap;
import tico.board.TBoardConstants;
import tico.board.components.TGrid;
import tico.board.components.TGridCell;
import tico.configuration.TLanguage;
import tico.editor.TEditor;

/**
 * Action wich adds a new full grid cells row to a grid of the previous
 * last row length.
 * 
 * @author Pablo Muñoz
 * @version 1.0 Nov 20, 2006
 */
public class TAddGridRowAction extends TEditorAbstractAction {

    private TGrid grid;

    /**
	 * Constructor for TAddGridRowAction.
	 * 
	 * @param editor The boards' editor
	 * @param grid The grid in which the new TGrid row is going to be inserted.
	 */
    public TAddGridRowAction(TEditor editor, TGrid grid) {
        super(editor, TLanguage.getString("TAddGridRowAction.NAME"));
        this.grid = grid;
    }

    public void actionPerformed(ActionEvent e) {
        int numNewCells = grid.getColumnsCount();
        TGridCell[] newCells = new TGridCell[numNewCells];
        AttributeMap currentAttributes = editor.getCurrentAttributes();
        int lastColumnRow;
        Rectangle2D newCellDimension;
        double verticalDistance;
        for (int i = 1; i <= numNewCells; i++) {
            lastColumnRow = grid.getLastRowColumn(i);
            newCells[i - 1] = new TGridCell(lastColumnRow + 1, i);
            newCells[i - 1].getAttributes().applyMap(currentAttributes);
            newCellDimension = TBoardConstants.getBounds(grid.getCell(lastColumnRow, i).getAttributes());
            if (grid.getCell(lastColumnRow - 1, i) == null) verticalDistance = TGrid.DEFAULT_VERTICAL_GAP + newCellDimension.getHeight(); else {
                Rectangle2D previousRowCell = (Rectangle2D) TBoardConstants.getBounds(grid.getCell(lastColumnRow - 1, i).getAttributes());
                verticalDistance = newCellDimension.getY() - previousRowCell.getY() - previousRowCell.getHeight() + newCellDimension.getHeight();
            }
            TBoardConstants.setBounds(newCells[i - 1].getAttributes(), new Rectangle.Double(newCellDimension.getX(), newCellDimension.getY() + verticalDistance, newCellDimension.getWidth(), newCellDimension.getHeight()));
        }
        getEditor().getCurrentBoard().getGraphLayoutCache().insertGroup(grid, newCells);
    }
}
