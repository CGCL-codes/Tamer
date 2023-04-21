package org.gwtoolbox.widget.client.table.datagrid.column.renderer;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Anchor;
import org.gwtoolbox.widget.client.table.datagrid.column.CellRenderer;

/**
 * @author Uri Boness
 */
public class UrlCellRenderer implements CellRenderer<String> {

    public Widget render(String link) {
        return new Anchor(link);
    }
}
