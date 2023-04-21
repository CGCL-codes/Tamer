package net.sf.doolin.gui.field.table;

import java.util.List;
import net.sf.doolin.gui.field.table.support.GUITableField;

/**
 * Generates the column list for a table.
 * 
 * @author Damien Coraboeuf
 * 
 * @param <V>
 *            Type of object for the view
 * @param <E>
 *            Type of item in the table
 */
public interface ColumnGenerator<V, E> {

    /**
	 * Generates the columns
	 * 
	 * @param tableField
	 *            Associated table field
	 * @return List of columns
	 */
    List<Column> generateColumns(GUITableField<V, E> tableField);
}
