package game.gui.graph3d;

import java.util.List;

/**
 * Base
 * 
 * @author Pavel Stehlik (stehlp2@fel.cvut.cz)
 */
class Base {

    private List<Column> columns;

    public Base(List<Column> columns) {
        setColumns(columns);
    }

    public List<Column> getColumns() {
        return columns;
    }

    void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
