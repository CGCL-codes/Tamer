package astcentric.editor.common.dialog.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of useful functions concering {@link RowTraversable}.
 */
public class RowTool {

    /**
   * Traverses the specified row traverable and sends each cell to the
   * specified cell handler.
   */
    public static void traverseAllCells(RowTraversable rowTraversable, final CellHandler cellHandler) {
        rowTraversable.traverseRows(new TableRowHandler() {

            public boolean handle(TableRow row) {
                row.traverseCells(cellHandler);
                return false;
            }
        });
    }

    /**
   * Collects all rows of the specified row traverable in a list.
   */
    public static List<TableRow> collect(RowTraversable rowTraversable) {
        final List<TableRow> list = new ArrayList<TableRow>();
        rowTraversable.traverseRows(new TableRowHandler() {

            public boolean handle(TableRow row) {
                list.add(row);
                return false;
            }
        });
        return list;
    }

    private RowTool() {
    }
}
