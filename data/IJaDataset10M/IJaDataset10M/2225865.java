package msa2snp.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import msa2snp.util.struct.MyInteger;

public class SelectAction implements ActionListener {

    private MyInteger id;

    public SelectAction(MyInteger id) {
        this.id = id;
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        id.setValue(cb.getSelectedIndex());
    }
}
