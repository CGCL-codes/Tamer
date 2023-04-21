package sudokusolver;

/**
 *
 * @author Right
 */
public class CellIndexes {

    byte rowIndex;

    byte columnIndex;

    CellIndexes() {
    }

    CellIndexes(byte rowIndex, byte columnIndex) {
        this();
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }
}
