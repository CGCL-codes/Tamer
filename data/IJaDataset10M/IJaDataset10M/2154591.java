package org.radrails.db.internal.ui.editors.dbtable;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author mkent
 *
 */
public class DbTableCellModifier implements ICellModifier {

    public boolean canModify(Object element, String property) {
        Row r = (Row) element;
        Field f = r.getField(property);
        boolean modify = false;
        if (f != null) {
            modify = !f.isPk();
        }
        return modify;
    }

    public Object getValue(Object element, String property) {
        Row r = (Row) element;
        Field f = r.getField(property);
        return f.getCurrentValue();
    }

    public void modify(Object element, String property, Object value) {
        if (element instanceof TableItem) {
            TableItem ti = (TableItem) element;
            Row r = (Row) ti.getData();
            Field f = r.getField(property);
            if (!f.getOriginalValue().equals(value)) {
                f.setCurrentValue(value);
            }
        }
    }
}
