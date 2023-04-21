package org.xmlhammer.gui.util;

import javax.swing.JComboBox;
import javax.swing.undo.AbstractUndoableEdit;
import org.xmlhammer.gui.Page;
import org.xmlhammer.gui.history.HistoryComboBox;

public class UndoableComboBoxEdit extends AbstractUndoableEdit {

    private static final long serialVersionUID = 8615885869967750791L;

    private Object current = null;

    private Object previous = null;

    private Page page = null;

    private JComboBox combo = null;

    public UndoableComboBoxEdit(Page page, JComboBox combo, Object current, Object previous) {
        super();
        this.page = page;
        this.combo = combo;
        this.current = current;
        this.previous = previous;
    }

    public void undo() {
        super.undo();
        page.getProjectView().getOverviewPanel().selectNode(page);
        combo.requestFocusInWindow();
        combo.setSelectedItem(previous);
        if (combo.isEditable() && previous != null) {
            combo.getEditor().setItem(previous);
        } else if (previous == null && combo instanceof HistoryComboBox) {
            combo.getEditor().setItem(((HistoryComboBox) combo).getEmptyValue());
        }
    }

    public void redo() {
        super.redo();
        page.getProjectView().getOverviewPanel().selectNode(page);
        combo.requestFocusInWindow();
        combo.setSelectedItem(current);
        if (combo.isEditable() && current != null) {
            combo.getEditor().setItem(current);
        } else if (current == null && combo instanceof HistoryComboBox) {
            combo.getEditor().setItem(((HistoryComboBox) combo).getEmptyValue());
        }
    }

    public String getUndoPresentationName() {
        return "Set " + previous.toString();
    }

    public String getRedoPresentationName() {
        return "Reset " + current.toString();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("ComboBoxEdit Undo ");
        if (previous != null) {
            buffer.append(previous.toString());
        } else {
            buffer.append("null");
        }
        buffer.append(" Redo ");
        if (current != null) {
            buffer.append(current.toString());
        } else {
            buffer.append("null");
        }
        return buffer.toString();
    }
}
