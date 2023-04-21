package ogv.gui.dialogs;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ogv.OGV;
import ogv.gui.tables.Column;
import ogv.gui.tables.DataTable;
import ogv.util.ConfigNode;
import ogv.util.SwingUtils;

public class ColumnsManager extends MouseAdapter implements ListSelectionListener, ActionListener {

    private final ConfigNode config = OGV.getConfig().subnode("Dialog.ColumnManager");

    private JButton btnShow;

    private JButton btnHide;

    private JButton btnUp;

    private JButton btnDown;

    private JButton btnEdit;

    private JButton btnNew;

    private JButton btnRemove;

    private final JList availableJList = new ColumnsList();

    private final JList activeJList = new ColumnsList();

    private JList currentJList = activeJList;

    private JPanel panel;

    private final List<Column> allColumns;

    private final List<Column> active;

    private final List<Column> available;

    private final List<Column> deleted;

    private final DataTable<?> dataTable;

    private ColumnsManager(DataTable<?> dataTable, List<Column> act, List<Column> allCols) {
        this.dataTable = dataTable;
        active = new ArrayList<Column>(act);
        allColumns = new ArrayList<Column>(allCols);
        available = new ArrayList<Column>();
        deleted = new ArrayList<Column>();
        for (Column c : allColumns) if (!active.contains(c)) available.add(c);
        initGUI();
        assignActions();
        setModels();
        activeJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        availableJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        btnHide.setEnabled(false);
        btnShow.setEnabled(false);
        btnUp.setEnabled(false);
        btnDown.setEnabled(false);
        btnRemove.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void initGUI() {
        panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(activeJList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(config.subnode("Current").getString(SwingUtils.TITLE)));
        setSize(scrollPane, 100, 300);
        panel.add(scrollPane, BorderLayout.LINE_START);
        scrollPane = new JScrollPane(availableJList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(config.subnode("Available").getString(SwingUtils.TITLE)));
        setSize(scrollPane, 100, 300);
        panel.add(scrollPane, BorderLayout.LINE_END);
        ConfigNode node = config.subnode("buttons");
        btnUp = SwingUtils.makeButton(node.subnode("Up"));
        btnDown = SwingUtils.makeButton(node.subnode("Down"));
        btnShow = SwingUtils.makeButton(node.subnode("Show"));
        btnHide = SwingUtils.makeButton(node.subnode("Hide"));
        btnEdit = SwingUtils.makeButton(node.subnode("Edit"));
        btnNew = SwingUtils.makeButton(node.subnode("New"));
        btnRemove = SwingUtils.makeButton(node.subnode("Remove"));
        setSize(btnUp, 80, 25);
        setSize(btnDown, 80, 25);
        setSize(btnShow, 80, 25);
        setSize(btnHide, 80, 25);
        setSize(btnEdit, 80, 25);
        setSize(btnNew, 80, 25);
        setSize(btnRemove, 80, 25);
        Box vbox = SwingUtils.makeVBox(null, SwingUtils.makeHBox(null, btnShow, null), SwingUtils.makeHBox(null, btnHide, null), null, SwingUtils.makeHBox(null, btnUp, null), SwingUtils.makeHBox(null, btnDown, null), null, SwingUtils.makeHBox(null, btnEdit, null), SwingUtils.makeHBox(null, btnNew, null), SwingUtils.makeHBox(null, btnRemove, null), null);
        setSize(vbox, 100, 300);
        panel.add(vbox, BorderLayout.CENTER);
    }

    private static void setSize(Component c, int x, int y) {
        c.setPreferredSize(new Dimension(x, y));
        c.setMaximumSize(new Dimension(x, y));
    }

    private void assignActions() {
        activeJList.addListSelectionListener(this);
        availableJList.addListSelectionListener(this);
        btnShow.addActionListener(this);
        btnHide.addActionListener(this);
        btnUp.addActionListener(this);
        btnDown.addActionListener(this);
        btnNew.addActionListener(this);
        btnRemove.addActionListener(this);
        btnEdit.addActionListener(this);
        activeJList.addMouseListener(this);
        availableJList.addMouseListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnShow) {
            move(available, availableJList, active, activeJList);
        } else if (e.getSource() == btnHide) {
            move(active, activeJList, available, availableJList);
        } else if (e.getSource() == btnUp) {
            int[] ind = activeJList.getSelectedIndices();
            if (ind.length == 0 || ind[0] == 0) return;
            int i = ind[0] - 1;
            Column c = active.get(i);
            active.remove(i);
            active.add(i + ind.length, c);
            setModels();
            for (i = 0; i < ind.length; i++) ind[i]--;
            activeJList.setSelectedIndices(ind);
        } else if (e.getSource() == btnDown) {
            int[] ind = activeJList.getSelectedIndices();
            if (ind.length == 0 || ind[ind.length - 1] == active.size() - 1) return;
            int i = ind[ind.length - 1] + 1;
            Column c = active.get(i);
            active.remove(i);
            active.add(ind[0], c);
            setModels();
            for (i = 0; i < ind.length; i++) ind[i]++;
            activeJList.setSelectedIndices(ind);
        } else if (e.getSource() == btnNew) {
            Column c = dataTable.createCustomColumn();
            if (!dataTable.editColumn(c)) return;
            allColumns.add(c);
            available.add(c);
            setModels();
            availableJList.setSelectedIndex(available.size() - 1);
        } else if (e.getSource() == btnRemove) {
            List<Column> del = getSelectedItems(currentJList);
            if (del.isEmpty()) return;
            for (Column c : del) if (c.isCustom()) {
                allColumns.remove(c);
                available.remove(c);
                deleted.add(c);
            }
            setModels();
        } else if (e.getSource() == btnEdit) {
            edit();
        }
    }

    private void edit() {
        List<Column> sel = getSelectedItems(currentJList);
        if (sel.size() != 1) return;
        Column c = sel.get(0);
        if (!dataTable.editColumn(c)) return;
        currentJList.repaint();
    }

    private void move(List<Column> from, JList fromList, List<Column> to, JList toList) {
        List<Column> c = getSelectedItems(fromList);
        if (c.isEmpty()) return;
        to.addAll(c);
        from.removeAll(c);
        setModels();
        int[] ind = new int[c.size()];
        for (int i = 0; i < ind.length; i++) ind[i] = to.indexOf(c.get(i));
        toList.setSelectedIndices(ind);
    }

    private static List<Column> getSelectedItems(JList jList) {
        ListModel model = jList.getModel();
        List<Column> columns = new ArrayList<Column>();
        for (int i : jList.getSelectedIndices()) columns.add((Column) model.getElementAt(i));
        return columns;
    }

    private void setModels() {
        activeJList.setListData(active.toArray());
        availableJList.setListData(available.toArray());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1 || e.getClickCount() <= 1) return;
        edit();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        currentJList = (JList) e.getSource();
        ListSelectionModel activeSelection = activeJList.getSelectionModel();
        btnHide.setEnabled(!activeJList.isSelectionEmpty());
        btnShow.setEnabled(!availableJList.isSelectionEmpty());
        btnUp.setEnabled(!activeJList.isSelectionEmpty() && activeSelection.getMinSelectionIndex() > 0);
        btnDown.setEnabled(!activeJList.isSelectionEmpty() && activeSelection.getMaxSelectionIndex() < active.size() - 1);
        boolean del = false;
        for (Column c : getSelectedItems(currentJList)) if (c.isCustom()) {
            del = true;
            break;
        }
        btnRemove.setEnabled(del);
        btnEdit.setEnabled(!currentJList.isSelectionEmpty());
    }

    public static void showDialog(DataTable<?> dataTable, List<Column> act, List<Column> allCols) {
        ColumnsManager manager = new ColumnsManager(dataTable, act, allCols);
        if (!SwingUtils.okCancelDialog(dataTable, manager.panel, manager.config.getString(SwingUtils.TITLE))) return;
        dataTable.saveColumns(manager.active, manager.allColumns, manager.deleted);
    }

    private static class ColumnsList extends JList {

        @Override
        public String getToolTipText(MouseEvent e) {
            int i = locationToIndex(e.getPoint());
            if (i < 0) return null;
            Column c = (Column) getModel().getElementAt(i);
            return c.getShortDescription();
        }
    }
}
