package de.jdufner.sudoku.common.board;

/**
 * Der CellHandler wird von Iteratoren verwendet, um eine Operation auf allen
 * Feldern auszuführen.
 * 
 * TODO Evtl. Rückgabewert einbauen mittels Generics.
 * 
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 0.1
 * @version $Revision: 120 $
 * @see Cell
 */
public interface CellHandler {

    void initialize();

    void handleCell(Cell cell);
}
