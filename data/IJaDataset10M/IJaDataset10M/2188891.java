package com.explosion.utilities.preferences.editandrender.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import com.explosion.utilities.GeneralUtils;
import com.explosion.utilities.exception.ExceptionManagerFactory;
import com.explosion.utilities.preferences.Preference;
import com.explosion.utilities.preferences.dialogs.EditCollectionPreferenceDialog;

public class CollectionPreferenceEditor extends DefaultCellEditor {

    private Preference preference = null;

    private final EditCollectionPreferenceDialog dialog = null;

    public CollectionPreferenceEditor(JButton b, Component owner) {
        super(new JCheckBox());
        editorComponent = b;
        setClickCountToStart(1);
        ((JButton) editorComponent).addActionListener(new ButtonActionListener(owner, this));
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    public Object getCellEditorValue() {
        return preference;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        preference = (Preference) value;
        ((JButton) editorComponent).setText("..");
        return ((JButton) editorComponent);
    }

    public Preference getPreference() {
        return preference;
    }
}

class ButtonActionListener implements ActionListener {

    private EditCollectionPreferenceDialog dialog;

    private Component owner;

    private CollectionPreferenceEditor editor;

    /**
     *  
     */
    public ButtonActionListener(Component owner, CollectionPreferenceEditor editor) {
        this.owner = owner;
        this.editor = editor;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (owner instanceof JDialog) dialog = new EditCollectionPreferenceDialog((JDialog) owner, "Edit values", true, editor.getPreference()); else if (owner instanceof JFrame) {
                dialog = new EditCollectionPreferenceDialog((JFrame) owner, "Edit values", true, editor.getPreference());
            }
            dialog.setSize(new Dimension(400, 400));
            GeneralUtils.centreWindowInParent(dialog);
            dialog.setVisible(true);
        } catch (Exception e1) {
            ExceptionManagerFactory.getExceptionManager().manageException(e1, "Exception caught while managing collection.");
        } finally {
            editor.fireEditingStopped();
        }
    }
}
