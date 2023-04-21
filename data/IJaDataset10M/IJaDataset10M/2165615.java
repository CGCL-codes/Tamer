package com.antlersoft.query.environment.ui;

import java.awt.event.*;
import java.util.Enumeration;
import javax.swing.*;

public class HistoryList extends JList {

    private DefaultListModel dlm;

    private JTextArea text;

    JPopupMenu popup_menu;

    int popup_index;

    public HistoryList(JTextArea toChange) {
        super(new DefaultListModel());
        popup_index = -1;
        text = toChange;
        dlm = (DefaultListModel) getModel();
        setVisibleRowCount(2);
        popup_menu = new JPopupMenu();
        popup_menu.add(new SelectAction());
        popup_menu.add(new RemoveAction());
        addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    synchronized (dlm) {
                        int index = locationToIndex(e.getPoint());
                        text.setText((String) dlm.elementAt(index));
                        moveToTop(index);
                    }
                }
            }

            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }
        });
    }

    void showPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup_index = locationToIndex(e.getPoint());
            if (popup_index >= 0) popup_menu.show(this, e.getX(), e.getY());
        }
    }

    public void addQuery(String toAdd) {
        synchronized (dlm) {
            int j = 0;
            for (Enumeration i = dlm.elements(); i.hasMoreElements(); j++) {
                if (toAdd.equals(i.nextElement())) {
                    moveToTop(j);
                    return;
                }
            }
            dlm.insertElementAt(toAdd, 0);
        }
    }

    public Enumeration getContents() {
        return dlm.elements();
    }

    private void moveToTop(int index) {
        Object o = dlm.elementAt(index);
        dlm.remove(index);
        dlm.insertElementAt(o, 0);
        setSelectedIndex(0);
    }

    class SelectAction extends AbstractAction {

        SelectAction() {
            super("Select");
        }

        public void actionPerformed(ActionEvent ae) {
            synchronized (dlm) {
                if (popup_index >= 0) {
                    text.setText((String) dlm.elementAt(popup_index));
                    moveToTop(popup_index);
                    popup_index = 0;
                }
            }
        }
    }

    class RemoveAction extends AbstractAction {

        RemoveAction() {
            super("Delete");
        }

        public void actionPerformed(ActionEvent ae) {
            synchronized (dlm) {
                if (popup_index >= 0) {
                    dlm.remove(popup_index);
                    popup_index = -1;
                }
            }
        }
    }
}
