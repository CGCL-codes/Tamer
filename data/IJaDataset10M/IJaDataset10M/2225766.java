package com.egantt.swing.cell.editor.adapter;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import com.egantt.swing.cell.CellEditor;
import com.egantt.swing.cell.renderer.adapter.JTableRendererAdapter;

public class JTableEditorAdapter extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 5181381787657020316L;

    protected final JTableRendererAdapter rendererAdapter;

    protected final CellEditor editor;

    public JTableEditorAdapter(CellEditor editor) {
        this.editor = editor;
        this.rendererAdapter = new JTableRendererAdapter(editor, true, false);
    }

    public boolean isCellEditable(EventObject e) {
        return editor.isCellEditable(e);
    }

    public Component getTableCellEditorComponent(JTable source, Object value, boolean isSelected, int row, int column) {
        Component component = rendererAdapter.getTableCellRendererComponent(source, value, isSelected, source.hasFocus(), row, column);
        return component;
    }

    public Object getCellEditorValue() {
        return editor.getValue();
    }

    @Override
    public void cancelCellEditing() {
        editor.cancelEditing();
        super.cancelCellEditing();
    }

    @Override
    public boolean stopCellEditing() {
        editor.stopEditing();
        return super.stopCellEditing();
    }
}
