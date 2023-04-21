package rino.sudoku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rino
 *
 */
public class Cube extends BaseValues {

    private static final long serialVersionUID = 6013353647037717247L;

    private int m_index;

    private int rowIndex;

    private int colIndex;

    private List<Row> rows;

    private List<Column> cols;

    private GameBoard m_owner;

    /**
	 * @param owner
	 * @param index
	 */
    public Cube(GameBoard owner, int index) {
        super();
        m_owner = owner;
        m_index = index;
        rowIndex = m_index / 3;
        colIndex = m_index % 3;
        add(new Cell(this, 0));
        add(new Cell(this, 1));
        add(new Cell(this, 2));
        add(new Cell(this, 3));
        add(new Cell(this, 4));
        add(new Cell(this, 5));
        add(new Cell(this, 6));
        add(new Cell(this, 7));
        add(new Cell(this, 8));
        rows = new ArrayList<Row>();
        rows.add(new Row(subList(0, 3)));
        rows.add(new Row(subList(3, 6)));
        rows.add(new Row(subList(6, 9)));
        cols = new ArrayList<Column>();
        cols.add(new Column());
        cols.get(0).add(rows.get(0).get(0));
        cols.get(0).add(rows.get(1).get(0));
        cols.get(0).add(rows.get(2).get(0));
        cols.add(new Column());
        cols.get(1).add(rows.get(0).get(1));
        cols.get(1).add(rows.get(1).get(1));
        cols.get(1).add(rows.get(2).get(1));
        cols.add(new Column());
        cols.get(2).add(rows.get(0).get(2));
        cols.get(2).add(rows.get(1).get(2));
        cols.get(2).add(rows.get(2).get(2));
        for (Cell cell : this) {
            cell.addListener(this);
        }
    }

    /**
	 * @return
	 */
    public int getIndex() {
        return m_index;
    }

    /**
	 * @return
	 */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
	 * @return
	 */
    public int getColumnIndex() {
        return colIndex;
    }

    /**
	 * @param rowIndex
	 * @return
	 */
    public Row getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
	 * @param columnIndex
	 * @return
	 */
    public Column getColumn(int columnIndex) {
        return cols.get(columnIndex);
    }

    /**
	 * @return
	 */
    public List<Row> getRows() {
        return rows;
    }

    /**
	 * @return
	 */
    public List<Column> getColumns() {
        return cols;
    }

    /**
	 * @param row
	 * @param col
	 * @return
	 */
    public Cell getCell(int row, int col) {
        return rows.get(row).get(col);
    }

    /**
	 * @param value
	 * @return
	 */
    public List<Cell> getAvailableCells(int value) {
        Row row;
        Column col;
        List<Cell> cells = new ArrayList<Cell>();
        for (Cell cell : this) {
            row = cell.getBoardRow();
            col = cell.getBoardColumn();
            if (!cell.hasValue() && !hasValue(value) && !row.hasValue(value) && !col.hasValue(value)) {
                cells.add(cell);
            }
        }
        return cells;
    }

    /**
	 * @return
	 */
    public GameBoard getBoard() {
        return m_owner;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 0;
        result = prime * result + colIndex;
        result = prime * result + m_index;
        result = prime * result + rowIndex;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        final Cube other = (Cube) obj;
        if (colIndex != other.colIndex) return false;
        if (cols == null) {
            if (other.cols != null) return false;
        } else if (!cols.equals(other.cols)) return false;
        if (m_index != other.m_index) return false;
        if (m_owner == null) {
            if (other.m_owner != null) return false;
        } else if (!m_owner.equals(other.m_owner)) return false;
        if (rowIndex != other.rowIndex) return false;
        if (rows == null) {
            if (other.rows != null) return false;
        } else if (!rows.equals(other.rows)) return false;
        return true;
    }
}
