package org.openconcerto.ui.list;

import java.awt.Component;
import javax.swing.CellEditor;
import javax.swing.JList;

public interface ListCellEditor extends CellEditor {

    Component getListCellEditorComponent(JList list, Object value, boolean isSelected, int index);
}
