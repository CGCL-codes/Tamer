package net.sourceforge.nattable.support;

import net.sourceforge.nattable.renderer.ICellRenderer;

public interface IBulkUpdateRequestHandler {

    BulkUpdateResponse bulkUpdate(String fieldName, ICellRenderer bodyCellRenderer, int row, int column);
}
