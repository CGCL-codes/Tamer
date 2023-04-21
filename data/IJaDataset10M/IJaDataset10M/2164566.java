package org.viewaframework.swing.treetable;

import javax.swing.table.TableCellRenderer;
import org.viewaframework.swing.table.DynamicTableColumn;

/**
 * @author mgg
 *
 */
public class DynamicTreeTableColumn extends DynamicTableColumn {

    private boolean grouping;

    public DynamicTreeTableColumn() {
        super();
    }

    public DynamicTreeTableColumn(String propertyName, Integer order, Integer width, TableCellRenderer renderer) {
        super(propertyName, order, width, renderer);
    }

    public DynamicTreeTableColumn(String propertyName, Integer order, Integer width) {
        super(propertyName, order, width);
    }

    public DynamicTreeTableColumn(String propertyName, Integer order, Integer width, String title, boolean grouping) {
        super(propertyName, order, width);
        this.setTitle(title);
        this.setGrouping(grouping);
    }

    public boolean isGrouping() {
        return grouping;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
    }
}
