package org.wcb.autohome.util;

import org.wcb.autohome.util.ui.LightRender;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.DefaultCellEditor;

public class LightEditor extends DefaultCellEditor {

    LightRender render = new LightRender();

    public String currentString = null;

    public LightEditor(JButton b) {
        super(new JCheckBox());
        editorComponent = b;
        setClickCountToStart(1);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    public Object getCellEditorValue() {
        return currentString;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ((JButton) editorComponent).setText(value.toString());
        currentString = (String) value;
        return editorComponent;
    }
}
