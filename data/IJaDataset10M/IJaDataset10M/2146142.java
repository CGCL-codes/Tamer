package uk.ac.essex.common.gui;

import javax.swing.*;
import javax.swing.plaf.IconUIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * This is a utility class to show the keys available from the UIDefaults
 *
 * @author camickr (from sun forums) modified by Laurence Smith
 *
 * You should have received a copy of GNU public license with this code.
 * If not please visit <a href="www.gnu.org/copyleft/gpl.html">this site </a>
 */
public class ShowUIDefaults extends JFrame implements ActionListener {

    JFrame frame;

    JTabbedPane tabbedPane;

    JButton metal;

    JButton windows;

    JButton motif;

    SampleRenderer sampleRenderer;

    public ShowUIDefaults(String title) {
        super(title);
        frame = this;
        getContentPane().setLayout(new BorderLayout());
        tabbedPane = getTabbedPane();
        getContentPane().add(tabbedPane);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 3));
        getContentPane().add(buttons, BorderLayout.SOUTH);
        metal = new JButton("Metal");
        metal.addActionListener(this);
        buttons.add(metal);
        windows = new JButton("Windows");
        windows.addActionListener(this);
        buttons.add(windows);
        motif = new JButton("Motif");
        motif.addActionListener(this);
        buttons.add(motif);
    }

    public void actionPerformed(ActionEvent e) {
        String laf = "";
        Object o = e.getSource();
        if (o == metal) laf = "javax.swing.plaf.metal.MetalLookAndFeel"; else if (o == windows) laf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; else if (o == motif) laf = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        try {
            UIManager.setLookAndFeel(laf);
        } catch (Exception e2) {
            System.out.println(e2);
        }
        getContentPane().remove(tabbedPane);
        tabbedPane = getTabbedPane();
        getContentPane().add(tabbedPane);
        SwingUtilities.updateComponentTreeUI(frame);
        frame.pack();
    }

    private JTabbedPane getTabbedPane() {
        Map components = new TreeMap();
        UIDefaults defaults = UIManager.getDefaults();
        for (Enumeration enumeration = defaults.keys(); enumeration.hasMoreElements(); ) {
            Object key = enumeration.nextElement();
            Object value = defaults.get(key);
            Map componentMap = getComponentMap(components, key.toString());
            if (componentMap != null) componentMap.put(key, value);
        }
        JTabbedPane pane = new JTabbedPane(SwingConstants.BOTTOM);
        pane.setPreferredSize(new Dimension(800, 400));
        addComponentTabs(pane, components);
        return pane;
    }

    private Map getComponentMap(Map components, String key) {
        if (key.startsWith("class") | key.startsWith("javax")) return null;
        String componentName;
        int pos = key.indexOf(".");
        if (pos == -1) if (key.endsWith("UI")) componentName = key.substring(0, key.length() - 2); else componentName = "System Colors"; else componentName = key.substring(0, pos);
        Object componentMap = components.get(componentName);
        if (componentMap == null) {
            componentMap = new TreeMap();
            components.put(componentName, componentMap);
        }
        return (Map) componentMap;
    }

    private void addComponentTabs(JTabbedPane pane, Map components) {
        sampleRenderer = new SampleRenderer();
        String[] colName = { "Key", "Value", "Sample" };
        Set c = components.keySet();
        for (Iterator ci = c.iterator(); ci.hasNext(); ) {
            String component = (String) ci.next();
            Map attributes = (Map) components.get(component);
            Object[][] rowData = new Object[attributes.size()][3];
            int i = 0;
            Set a = attributes.keySet();
            for (Iterator ai = a.iterator(); ai.hasNext(); i++) {
                String attribute = (String) ai.next();
                rowData[i][0] = attribute;
                Object o = attributes.get(attribute);
                if (o != null) {
                    rowData[i][1] = o.toString();
                    rowData[i][2] = "";
                    if (o instanceof Font) rowData[i][2] = (Font) o;
                    if (o instanceof Color) rowData[i][2] = (Color) o;
                    if (o instanceof IconUIResource) rowData[i][2] = (Icon) o;
                } else {
                    rowData[i][1] = "";
                    rowData[i][2] = "";
                }
            }
            MyTableModel myModel = new MyTableModel(rowData, colName);
            JTable table = new JTable(myModel);
            table.setDefaultRenderer(sampleRenderer.getClass(), sampleRenderer);
            table.getColumnModel().getColumn(0).setPreferredWidth(250);
            table.getColumnModel().getColumn(1).setPreferredWidth(500);
            table.getColumnModel().getColumn(2).setPreferredWidth(50);
            pane.addTab(component, new JScrollPane(table));
        }
    }

    class MyTableModel extends AbstractTableModel {

        private String[] columnNames;

        private Object[][] rowData;

        public MyTableModel(Object[][] rowData, String[] columnNames) {
            this.rowData = rowData;
            this.columnNames = columnNames;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return rowData.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return rowData[row][col];
        }

        public Class getColumnClass(int c) {
            Object o;
            if (c == 2) o = sampleRenderer; else o = getValueAt(0, c);
            return o.getClass();
        }

        public void setValueAt(Object value, int row, int col) {
            rowData[row][col] = value;
            fireTableCellUpdated(row, col);
        }
    }

    class SampleRenderer extends JLabel implements TableCellRenderer {

        public SampleRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object sample, boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(null);
            setIcon(null);
            setText("");
            if (sample instanceof Color) {
                setBackground((Color) sample);
            }
            if (sample instanceof Font) {
                setText("Sample");
                setFont((Font) sample);
            }
            if (sample instanceof Icon) {
                setIcon((Icon) sample);
            }
            return this;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame f = new ShowUIDefaults("UI Defaults");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
