package com.fangr.servers.email.smtp.configurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VRFYPanel extends JPanel {

    AddDialog adddlg;

    RemoveDialog removedlg;

    public VRFYPanel(Configuration config) {
        setLayout(new VrfyLayout());
        VrfyTableModel model = new VrfyTableModel(config);
        JTable tbl = new JTable(model);
        add(tbl, VrfyLayout.TBL);
        adddlg = new AddDialog(model, tbl);
        adddlg.setVisible(false);
        removedlg = new RemoveDialog(model, tbl);
        removedlg.setVisible(false);
        JButton addbn = new JButton("Add user");
        addbn.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                adddlg.setVisible(true);
            }
        });
        add(addbn, VrfyLayout.ADD);
        JButton remove = new JButton("Remove user");
        remove.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                removedlg.setVisible(true);
            }
        });
        add(remove, VrfyLayout.REMOVE);
    }
}

class VrfyLayout implements LayoutManager {
}

class AddDialog extends JDialog {

    VrfyTableModel model;

    JTable tbl;

    public AddDialog(VrfyTableModel model, JTable tbl) {
        this.model = model;
        this.tbl = tbl;
    }
}

class RemoveDialog extends JDialog {

    VrfyTableModel model;

    JTable tbl;

    public RemoveDialog(VrfyTableModel model, JTable tbl) {
        this.model = model;
        this.tbl = tbl;
    }
}

class VrfyTableModel extends AbstractTableModel {

    public int getRowCount() {
        return 2;
    }

    public int getColumnCount() {
        return users.getSize();
    }

    public Object getValueAt(int row, int column) {
        if (column == 0) {
            ((VrfyUser) users.elementAt(row)).getName();
        } else {
            ((VrfyUser) users.elementAt(row)).getUserName();
        }
    }
}
