package net.sf.jawp.gui.client.data;

import java.util.Comparator;
import javax.swing.table.TableCellRenderer;
import net.sf.jawp.gf.api.Entity;

/**
 * Interface for SortableColumn.
 * @see net.sf.jawp.gui.client.data.SortingTableModel
 * @author jarek
 * @version $Revision$
 *
 * @param <ENTITY>
 */
public interface SortableColumn<ENTITY extends Entity> {

    /**
	 * 
	 * @return column name (label)
	 */
    String getName();

    /**
	 * 
	 * @return comparator for entity on given column (ascending)
	 */
    Comparator<ENTITY> getComparator();

    /**
	 * Should retrieve given entity column value.
	 * @param entity 
	 * @return 
	 */
    Object getValue(final ENTITY entity);

    /**
	 * should return true only if there is update needed
	 */
    boolean hasChanged(final ENTITY old, final ENTITY newObject);

    /**
	 * 
	 * @return null if not set
	 */
    TableCellRenderer getCellRenderer();
}
