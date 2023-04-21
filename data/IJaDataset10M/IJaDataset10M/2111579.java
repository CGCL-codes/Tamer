package graphlab.ui.components.GPropertyEditor;

import graphlab.gui.core.utils.StaticUtils;
import graphlab.main.attribute.XAttribute;
import graphlab.ui.components.GPropertyEditor.editors.GStringEditor;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.HashMap;

/**
 * Author: Azin Azadi
 * Email : aazadi@gmail.com
 */
public class GCellEditor extends AbstractCellEditor implements TableCellEditor, EditingFinishedListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7943480654474872421L;

    protected static HashMap<Class, GBasicCellEditor> knownEditors = new HashMap<Class, GBasicCellEditor>();

    private GBasicCellEditor editor;

    protected XAttribute atr;

    public static void registerEditor(Class clazz, GBasicCellEditor editor) {
        knownEditors.put(clazz, editor);
    }

    public GCellEditor() {
    }

    public void cancelCellEditing() {
        super.cancelCellEditing();
        if (lastEditor != null) lastEditor.cancelEditing();
    }

    GBasicCellEditor lastEditor;

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String name = atr.getNameAt(row);
        if (!atr.isEditable(name)) return null;
        if (value == null) {
            return null;
        }
        return getEditor(value);
    }

    private Component getEditor(Object value) {
        editor = getEditorFor(value);
        if (editor == null) return null;
        editor.setEditingFinishedListener(this);
        return editor.getEditorComponent(value);
    }

    private static ObjectEditor oe = new ObjectEditor();

    /**
     * gets an editor for the given object, the editor should be registered before,...
     * several editors are registered as default
     */
    public static GBasicCellEditor getEditorFor(Object value) {
        GBasicCellEditor editor = null;
        Class c = value.getClass();
        while (editor == null && c != Object.class) {
            editor = knownEditors.get(c);
            c = c.getSuperclass();
        }
        if (editor == null) {
            Class cc[] = value.getClass().getInterfaces();
            for (int i = 0; i < cc.length && editor == null; i++) editor = knownEditors.get(cc[i]);
        }
        if (editor == null) {
            try {
                StaticUtils.fromString(value.getClass().getName(), value.toString());
            } catch (Exception e) {
                return null;
            }
            return oe;
        }
        return editor;
    }

    public Object getCellEditorValue() {
        return editor.getEditorValue();
    }

    public void editingFinished(Object editorValue) {
        stopCellEditing();
    }

    void setAtr(XAttribute attribute) {
        this.atr = attribute;
    }
}

class ObjectEditor extends GStringEditor {

    Class clazz;

    public Component getEditorComponent(Object value) {
        clazz = value.getClass();
        return super.getEditorComponent(value);
    }

    protected void finishEdit() {
        jt.setText(getEditorValue() + "");
        super.finishEdit();
    }

    public Object getEditorValue() {
        String s = super.getEditorValue() + "";
        Object o = null;
        try {
            o = StaticUtils.fromString(clazz.getName(), s);
        } catch (Exception e) {
            return StaticUtils.fromString(clazz.getName(), initVal);
        }
        return o;
    }
}
