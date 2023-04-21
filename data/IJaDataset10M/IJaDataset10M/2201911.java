package de.shandschuh.jaolt.gui.listener.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class MoveTextModuleActionListener implements ActionListener {

    private JList list;

    private int direction;

    private DefaultListModel listModel;

    public MoveTextModuleActionListener(JList list, int direction) {
        this.list = list;
        this.direction = direction;
        listModel = (DefaultListModel) list.getModel();
    }

    public void actionPerformed(ActionEvent e) {
        int index = list.getSelectedIndex();
        Object object = listModel.remove(index);
        listModel.add(index - direction, object);
        list.setSelectedIndex(index - direction);
    }
}
