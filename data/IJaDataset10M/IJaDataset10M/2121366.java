package org.mcisb.ui.tracking;

import java.util.*;
import javax.swing.table.*;
import org.mcisb.tracking.*;

/**
 * 
 * @author Neil Swainston
 */
public class SpotTableModel extends DefaultTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private final List values;

    /**
     * 
     * @param numberOfRows
     * @param numberOfColumns
     * @param values
     */
    public SpotTableModel(int numberOfRows, int numberOfColumns, List values) {
        this.values = values;
        dataVector = new Vector(numberOfRows);
        for (int row = 0; row < numberOfRows; row++) {
            final Vector rowVector = new Vector(numberOfColumns + 1);
            rowVector.add(Integer.valueOf(row + 1));
            dataVector.add(rowVector);
        }
        columnIdentifiers = new Vector();
        columnIdentifiers.add("");
        for (int col = 0; col < numberOfColumns; col++) {
            columnIdentifiers.add(Integer.valueOf(col + 1));
        }
        setDataVector(dataVector, columnIdentifiers);
        for (Iterator iterator = values.iterator(); iterator.hasNext(); ) {
            final Spot spot = (Spot) iterator.next();
            setValueAt(spot, spot.getRow() - 1, spot.getColumn());
        }
    }

    /**
     * 
     * @return List
     */
    public List getValues() {
        return values;
    }

    public boolean isCellEditable(int row, int column) {
        return column > 0 && getValueAt(row, column) != null;
    }

    public Class getColumnClass(int columnIndex) {
        return (columnIndex == 0) ? Integer.class : Object.class;
    }
}
