package net.sf.component.table;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.SWT;

/**
 * $Id: CheckBindedTableColumn.java 844 2005-09-12 03:18:14Z yflei $
 * Created Date: 2005-7-19
 * @author SimonLei
 */
public class CheckBindedTableColumn extends BindedTableColumn {

    public CheckBindedTableColumn(String name) {
        super(name);
        this.setStyle(SWT.CHECK);
    }

    @Override
    protected CellEditor getDefaultCellEditor() {
        final CheckboxCellEditor checkboxCellEditor = new CheckboxCellEditor();
        checkboxCellEditor.setStyle(SWT.CHECK);
        return checkboxCellEditor;
    }

    @Override
    public Object getValue(Object obj) {
        return obj == null ? new Boolean(true) : obj;
    }

    @Override
    public Object getModifiedValue(Object obj) {
        return obj;
    }

    private boolean boxed = false;

    /**
	 * 列所对应的属性是 Boolean 还是 boolean?
	 * @param boxed 如果true 则表示列对应属性是 Boolean，否则是 boolean
	 */
    public void setBoxed(boolean boxed) {
        this.boxed = boxed;
    }

    @Override
    public Class getValueClass() {
        return boxed ? Boolean.class : boolean.class;
    }
}
