package org.jowidgets.impl.event;

import java.util.Collections;
import java.util.Set;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.widgets.controller.ITableColumnMouseEvent;

public class TableColumnMouseEvent extends TableColumnEvent implements ITableColumnMouseEvent {

    private final Set<Modifier> modifier;

    public TableColumnMouseEvent(final int columnIndex, final Set<Modifier> modifier) {
        super(columnIndex);
        this.modifier = Collections.unmodifiableSet(modifier);
    }

    @Override
    public Set<Modifier> getModifiers() {
        return modifier;
    }

    @Override
    public String toString() {
        return "TableColumnMouseEvent [modifier=" + modifier + ", columnIndex=" + getColumnIndex() + "]";
    }
}
