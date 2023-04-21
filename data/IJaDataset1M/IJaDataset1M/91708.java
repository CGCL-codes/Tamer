package gov.nasa.gsfc.visbard.util;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JAddRemoveItemList extends JPanel {

    private JAddRemoveItemListListener fListener;

    private Vector fItems = new Vector();

    private JList fList;

    private JButton fAdd = new JButton("Add");

    private JButton fRemove = new JButton("Remove");

    private JPanel fButtonPanel = new JPanel(new GridBagLayout());

    public JAddRemoveItemList() {
        initGUI();
    }

    public JAddRemoveItemList(String addButtonText, String removeButtonText) {
        fAdd.setText(addButtonText);
        fRemove.setText(removeButtonText);
        initGUI();
    }

    private void initGUI() {
        this.setLayout(new GridBagLayout());
        fList = new JList();
        this.add(new JScrollPane(fList), new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        fAdd.setFocusPainted(false);
        fButtonPanel.add(fAdd, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        fRemove.setFocusPainted(false);
        fButtonPanel.add(fRemove, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
        this.add(fButtonPanel, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
        this.addListeners();
        this.synchButtonState();
    }

    private void addListeners() {
        fAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Object newitem = fListener.itemRequested();
                if (newitem != null) {
                    int idx = fList.getSelectedIndex();
                    if (idx >= fItems.size()) idx = fItems.size() - 1;
                    if (idx < 0) idx = 0;
                    fItems.insertElementAt(newitem, idx);
                    fList.setListData(fItems);
                    fireEvent(JAddRemoveItemListEvent.LIST_ITEM_ADDED, null);
                }
            }
        });
        fRemove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                int idx = fList.getSelectedIndex();
                if ((idx >= 0) && (idx < fItems.size())) {
                    fItems.removeElementAt(idx);
                    fList.setListData(fItems);
                    fireEvent(JAddRemoveItemListEvent.LIST_ITEM_REMOVED, null);
                }
            }
        });
        fList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent arg0) {
                fireEvent(JAddRemoveItemListEvent.LIST_ITEM_SELECTED, null);
                synchButtonState();
            }
        });
    }

    public void addButton(final JButton button) {
        fButtonPanel.add(button, new GridBagConstraints(0, GridBagConstraints.RELATIVE, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 0, 0), 0, 0));
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                fireEvent(JAddRemoveItemListEvent.BUTTON_PRESSED, button);
            }
        });
    }

    private void fireEvent(final int type, final JButton button) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JAddRemoveItemListEvent e = new JAddRemoveItemListEvent(type, button, fList.getSelectedIndex());
                fListener.eventOccured(e);
            }
        });
    }

    private void synchButtonState() {
        if (fList.getSelectedIndex() == -1) {
            fRemove.setEnabled(false);
        } else {
            fRemove.setEnabled(true);
        }
    }

    public JList getList() {
        return fList;
    }

    public Vector getItems() {
        return new Vector(fItems);
    }

    public void setItems(Vector items) {
        fItems = items;
        fList.setListData(fItems);
    }

    /**
     * Registers a listener.
     */
    public void setListener(JAddRemoveItemListListener source) {
        fListener = source;
    }
}
