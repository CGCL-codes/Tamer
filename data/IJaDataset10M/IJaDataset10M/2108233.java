package org.objectwiz.fxclient.renderer;

import fr.helmet.javafx.table.ObjectToColumnsParser;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class SimpleObjectToColumnParser extends ObjectToColumnsParser {

    private String[] columnNames;

    public SimpleObjectToColumnParser(String columnName) {
        this.columnNames = new String[] { columnName };
    }

    @Override
    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public Object[] getColumns(Object obj) {
        return new Object[] { (obj == null ? "" : obj.toString()) };
    }
}
