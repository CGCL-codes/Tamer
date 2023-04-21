package org.gjt.universe.gui;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.gjt.universe.*;

class GUIGalaxyListJFC extends UniverseJFrame {

    private JButton OKbutton;

    private JButton filterbutton;

    private GUIDisplayTreeJFC displayTree;

    private JTable displayTable;

    private CivID player;

    private JTabbedPane tabbedPane;

    private VectorDisplayReturn VDRtable;

    private DisplayGalaxyFilter filter;

    GUIGalaxyListJFC(CivID in_player) {
        this(in_player, new DisplayGalaxyFilter(DisplayGalaxyFilter.All, DisplayGalaxyFilter.Alphabetical));
    }

    GUIGalaxyListJFC(CivID in_player, DisplayGalaxyFilter filter) {
        player = in_player;
        this.filter = filter;
        newFilter(filter);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        getContentPane().setLayout(gridbag);
        OKbutton = new JButton("Close");
        OKbutton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0.0;
        c.ipadx = 5;
        c.ipady = 5;
        c.anchor = GridBagConstraints.WEST;
        gridbag.setConstraints(OKbutton, c);
        getContentPane().add(OKbutton);
        filterbutton = new JButton("Sort/Filter");
        filterbutton.addActionListener(new FilterButtonAction(this));
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = 1;
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.weighty = 0.0;
        c.ipadx = 5;
        c.ipady = 5;
        c.anchor = GridBagConstraints.EAST;
        gridbag.setConstraints(filterbutton, c);
        getContentPane().add(filterbutton);
        updatePanels();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridheight = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.ipadx = 5;
        c.ipady = 5;
        gridbag.setConstraints(tabbedPane, c);
        getContentPane().add(tabbedPane);
        setTitle("Galaxy List");
        pack();
        setSize(400, 400);
        show();
    }

    JPanel createTreePanel() {
        JPanel returnPanel = new JPanel();
        displayTree = new GUIDisplayTreeJFC(player, new DisplayNode(player, filter));
        returnPanel.setLayout(new GridLayout(1, 1));
        returnPanel.add(displayTree);
        return returnPanel;
    }

    JPanel createTablePanel() {
        JPanel returnPanel = new JPanel();
        JTable table = new JTable(new GalaxyTableModel());
        table.getTableHeader().setReorderingAllowed(true);
        table.setRowHeight(20);
        table.setRowSelectionAllowed(true);
        JScrollPane scrollpane = new JScrollPane(table);
        returnPanel.setLayout(new GridLayout(1, 1));
        returnPanel.add(scrollpane);
        table.addMouseListener(new TableMouseListener(table));
        return returnPanel;
    }

    class TableMouseListener extends MouseAdapter {

        private JTable table;

        TableMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point pt = e.getPoint();
                int row = table.rowAtPoint(pt);
                int col = table.columnAtPoint(pt);
                DisplayReturn DR = VDRtable.get(row);
                GUIFactoryJFC.spawnSubWindow(player, DR.getSystem());
            }
        }
    }

    class GalaxyTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return VDRtable.get(0).getNumColumns();
        }

        public int getRowCount() {
            return VDRtable.size();
        }

        public String getColumnName(int col) {
            return VDRtable.get(0).getColumnHeader(col);
        }

        public Object getValueAt(int row, int col) {
            DisplayReturn DR = VDRtable.get(row);
            return DR.getColumn(col);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    public void newFilter(DisplayGalaxyFilter filter) {
        this.filter = filter;
        VDRtable = DisplayGalaxy.get(player, filter);
        updatePanels();
    }

    private void updatePanels() {
        if (tabbedPane != null) {
            tabbedPane.removeAll();
        } else {
            tabbedPane = new JTabbedPane();
        }
        tabbedPane.addTab("Tree", null, createTreePanel(), "Tree");
        tabbedPane.addTab("Table", null, createTablePanel(), "Table");
    }

    class FilterButtonAction implements ActionListener {

        private GUIGalaxyListJFC parent;

        FilterButtonAction(GUIGalaxyListJFC parent) {
            this.parent = parent;
        }

        public void actionPerformed(ActionEvent e) {
            new GUIGalaxyFilterJFC(parent);
        }
    }
}
