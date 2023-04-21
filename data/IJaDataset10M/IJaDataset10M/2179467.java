package org.carabiner.examples;

import javax.swing.JLabel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

class DataModelListener implements ListDataListener {

    private JLabel label;

    DataModelListener(JLabel textLabel) {
        label = textLabel;
    }

    public void contentsChanged(ListDataEvent e) {
        updateLabel((MyDataModel) e.getSource());
    }

    public void intervalAdded(ListDataEvent e) {
        updateLabel((MyDataModel) e.getSource());
    }

    public void intervalRemoved(ListDataEvent e) {
        updateLabel((MyDataModel) e.getSource());
    }

    private void updateLabel(MyDataModel model) {
        label.setText(model.getStringData());
    }
}
