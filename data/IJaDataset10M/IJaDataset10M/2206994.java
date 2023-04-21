package net.sourceforge.appgen.support;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import net.sourceforge.appgen.model.Entity;

/**
 * @author Byeongkil Woo
 */
public class EntityBaseNameEditingSupport extends EditingSupport {

    private CellEditor editor;

    public EntityBaseNameEditingSupport(ColumnViewer viewer) {
        super(viewer);
        editor = new TextCellEditor(((TableViewer) viewer).getTable());
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return editor;
    }

    @Override
    protected Object getValue(Object element) {
        Entity entity = (Entity) element;
        return entity.getBaseName() == null ? "" : entity.getBaseName();
    }

    @Override
    protected void setValue(Object element, Object value) {
        Entity entity = (Entity) element;
        entity.setBaseName(String.valueOf(value).trim());
        getViewer().update(element, null);
    }
}
