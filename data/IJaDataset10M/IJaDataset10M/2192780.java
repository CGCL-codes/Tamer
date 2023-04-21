package org.jmesa.view.renderer;

import org.jmesa.view.component.Table;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public interface TableRenderer {

    public Table getTable();

    public void setTable(Table table);

    public Object render();
}
