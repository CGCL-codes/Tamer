package de.jassda.util.configeditor.gui.helper.components;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AddEditRemovePanel extends JPanel {

    JButton jbAdd = null;

    JButton jbEdit = null;

    JButton jbRemove = null;

    public AddEditRemovePanel() {
        super(new FlowLayout(FlowLayout.RIGHT));
        addAll();
    }

    private void addAll() {
        JPanel jpButtons = new JPanel(new GridLayout(1, 0));
        jpButtons.add(jbAdd = new JButton("Add"));
        jpButtons.add(jbEdit = new JButton("Edit"));
        jpButtons.add(jbRemove = new JButton("Remove"));
        this.add(jpButtons);
    }

    private void addListeners(ActionListener addListener, ActionListener editListener, ActionListener removeListener) {
        jbAdd.addActionListener(addListener);
        jbEdit.addActionListener(editListener);
        jbRemove.addActionListener(removeListener);
    }

    public void setAddActionListener(ActionListener addListener) {
        ActionListener listeners[] = jbAdd.getActionListeners();
        for (int i = 0; i < listeners.length; ++i) jbAdd.removeActionListener(listeners[i]);
        jbAdd.addActionListener(addListener);
    }

    public void setEditActionListener(ActionListener editListener) {
        ActionListener listeners[] = jbEdit.getActionListeners();
        for (int i = 0; i < listeners.length; ++i) jbEdit.removeActionListener(listeners[i]);
        jbEdit.addActionListener(editListener);
    }

    public void setRemoveActionListener(ActionListener removeListener) {
        ActionListener listeners[] = jbRemove.getActionListeners();
        for (int i = 0; i < listeners.length; ++i) jbRemove.removeActionListener(listeners[i]);
        jbRemove.addActionListener(removeListener);
    }
}
