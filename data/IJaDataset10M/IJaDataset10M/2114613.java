package listeners;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ProvaSelectListener implements ListSelectionListener {

    private JList lista;

    private JButton add;

    public ProvaSelectListener(JList lista, JButton add) {
        this.lista = lista;
        this.add = add;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (lista.getSelectedIndex() <= 0) {
            add.setEnabled(false);
        } else {
            add.setEnabled(true);
        }
    }
}
