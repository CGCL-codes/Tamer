package net.sf.component.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

/**
 * 在 BeanBindedTableModel 里面，每一行代表一个 JavaBean，这里就是它的 Modifier
 * $Id: BeanBindedTableCellModifier.java 844 2005-09-12 03:18:14Z yflei $
 * Created Date: 2005-9-9
 * @author SimonLei
 */
public class BeanBindedTableCellModifier implements ICellModifier {

    private IBindedTableModel model;

    private IBindedTableViewer tableViewer;

    public BeanBindedTableCellModifier(IBindedTableModel model, IBindedTableViewer tableViewer) {
        this.model = model;
        this.tableViewer = tableViewer;
    }

    public boolean canModify(Object element, String property) {
        return model.getColumn(property).isEditable(element);
    }

    public Object getValue(Object element, String property) {
        try {
            return model.getColumn(property).getValue(BeanBindedTableModel.getPropertyByReflect(element, property));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void modify(Object element, String property, Object value) {
        if (element instanceof Item) {
            element = ((Item) element).getData();
        }
        final BindedTableColumn column = model.getColumn(property);
        Object modifiedValue = column.getModifiedValue(value);
        try {
            Method method = element.getClass().getMethod("set" + property, new Class[] { column.getValueClass() });
            method.invoke(element, new Object[] { modifiedValue });
        } catch (Exception e) {
            try {
                setProperty(element, property, modifiedValue);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        model.setDirty(true);
        tableViewer.refresh(true);
    }

    private static void setProperty(Object element, String propertyName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        final Field declaredField = element.getClass().getDeclaredField(propertyName);
        declaredField.set(element, value);
    }
}
