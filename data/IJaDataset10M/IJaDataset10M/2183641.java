package admin.astor.tools;

import fr.esrf.Tango.*;
import fr.esrf.TangoDs.*;
import fr.esrf.tangoatk.widget.util.ErrorPane;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class PopupTable extends JDialog {

    /**
	 *	Events Table
	 */
    private DataTableModel model;

    /**
	 *	Names of the columns in the table
	 */
    private static String[] col_names;

    /**
	 *	An array of String array for data to be displayed
	 */
    private String[][] data;

    private java.awt.Window parent;

    private boolean from_appli = true;

    private boolean sort_available = true;

    public PopupTable(JFrame parent, String filename) throws SecurityException, IOException, DevFailed {
        this(parent, filename, (Dimension) null);
    }

    public PopupTable(JFrame parent, String filename, Dimension dim) throws SecurityException, IOException, DevFailed {
        super(parent, false);
        this.parent = parent;
        readDataFile(filename);
        buildObject(filename, col_names, data, dim);
        if (parent.getWidth() == 0) from_appli = false;
    }

    public PopupTable(JFrame parent, String title, String filename) throws SecurityException, IOException, DevFailed {
        this(parent, title, filename, null);
    }

    public PopupTable(JFrame parent, String title, String filename, Dimension dim) throws SecurityException, IOException, DevFailed {
        super(parent, false);
        this.parent = parent;
        readDataFile(filename);
        buildObject(title, col_names, data, dim);
        if (parent.getWidth() == 0) from_appli = false;
    }

    public PopupTable(JDialog parent, String title, String[] col, String[][] array) throws DevFailed {
        this(parent, title, col, array, null);
    }

    public PopupTable(JDialog parent, String title, String[] col, String[][] array, Dimension dim) throws DevFailed {
        super(parent, false);
        this.parent = parent;
        buildObject(title, col, array, dim);
    }

    public PopupTable(JFrame parent, String title, String[] col, String[][] array) throws DevFailed {
        this(parent, title, col, array, null);
    }

    public PopupTable(JFrame parent, String title, String[] col, String[][] array, Dimension dim) throws DevFailed {
        super(parent, false);
        this.parent = parent;
        buildObject(title, col, array, dim);
        if (parent.getWidth() == 0) from_appli = false;
    }

    private void readDataFile(String filename) throws SecurityException, IOException, DevFailed {
        FileInputStream fid = new FileInputStream(filename);
        int nb = fid.available();
        byte[] inStr = new byte[nb];
        int nb1 = fid.read(inStr);
        if (nb1 == 0) return;
        String str = new String(inStr);
        fid.close();
        Vector<String> v = new Vector<String>();
        StringTokenizer stk = new StringTokenizer(str, "\n");
        while (stk.hasMoreTokens()) v.add(stk.nextToken());
        System.out.println(v.size() + " lines");
        data = new String[v.size() - 1][];
        for (int i = 0; i < v.size(); i++) {
            String line = v.elementAt(i);
            stk = new StringTokenizer(line, "\t");
            if (i == 0) {
                col_names = new String[stk.countTokens()];
                for (int j = 0; stk.hasMoreTokens(); j++) col_names[j] = stk.nextToken();
            } else {
                data[i - 1] = new String[stk.countTokens()];
                for (int j = 0; stk.hasMoreTokens(); j++) data[i - 1][j] = stk.nextToken();
            }
        }
    }

    private void buildObject(String title, String[] col, String[][] array, Dimension dim) throws DevFailed {
        col_names = col;
        initComponents();
        data = array;
        initMyComponents(dim);
        titleLabel.setText(title);
        if (parent.getWidth() > 0) {
            Point p = parent.getLocationOnScreen();
            p.x += 10;
            p.y += 10;
            setLocation(p);
        }
        pack();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        cancelBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        cancelBtn.setText("Dismiss");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        jPanel1.add(cancelBtn);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        titleLabel.setFont(new java.awt.Font("Dialog", 1, 18));
        titleLabel.setText("Dialog Title");
        jPanel2.add(titleLabel);
        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);
        pack();
    }

    private JTable my_table;

    private JScrollPane scrollPane;

    private void initMyComponents(Dimension dim) throws DevFailed {
        try {
            model = new DataTableModel();
            final JTable table = new JTable(model);
            table.setRowSelectionAllowed(true);
            table.setColumnSelectionAllowed(true);
            table.setDragEnabled(true);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getTableHeader().setFont(new java.awt.Font("Dialog", 1, 14));
            table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tableActionPerformed(evt);
                }
            });
            scrollPane = new JScrollPane(table);
            if (dim == null) {
                int heith = 18 + 18 * data.length;
                if (heith > 400) heith = 400;
                scrollPane.setPreferredSize(new Dimension(650, heith));
            } else scrollPane.setPreferredSize(dim);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
            my_table = table;
        } catch (Exception e) {
            e.printStackTrace();
            Except.throw_exception("INIT_ERROR", e.toString(), "PopupTable.initMyComponents()");
        }
        model.fireTableDataChanged();
    }

    public void setSortAvailable(boolean b) {
        sort_available = b;
    }

    private void tableActionPerformed(java.awt.event.MouseEvent evt) {
        int column = my_table.getTableHeader().columnAtPoint(new Point(evt.getX(), evt.getY()));
        if (sort_available) new UsedData().sort(column);
        model.fireTableDataChanged();
    }

    public void setColumnWidth(int[] width) {
        final Enumeration cenum = my_table.getColumnModel().getColumns();
        TableColumn tc;
        int sp_width = 0;
        for (int i = 0; cenum.hasMoreElements(); i++) {
            tc = (TableColumn) cenum.nextElement();
            tc.setPreferredWidth(width[i]);
            sp_width += width[i];
        }
        scrollPane.setPreferredSize(new Dimension(sp_width, 450));
        pack();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        doClose();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose();
    }

    private void doClose() {
        setVisible(false);
        dispose();
        if (!from_appli) System.exit(0);
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton cancelBtn;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel titleLabel;

    class UsedData extends Vector<String[]> {

        UsedData() {
            for (String[] datum : data) add(datum);
        }

        void sort(int column) {
            this.column = column;
            MyCompare compare = new MyCompare();
            Collections.sort(this, compare);
            for (int i = 0; i < size(); i++) data[i] = elementAt(i);
        }

        private int column;

        class MyCompare implements Comparator {

            public int compare(Object o1, Object o2) {
                String[] a1 = (String[]) o1;
                String[] a2 = (String[]) o2;
                String s1 = a1[column];
                String s2 = a2[column];
                try {
                    double d1 = Double.parseDouble(s1);
                    double d2 = Double.parseDouble(s2);
                    return ((d1 > d2) ? 1 : 0);
                } catch (NumberFormatException e) {
                }
                return ((s1.compareToIgnoreCase(s2) > 0) ? 1 : 0);
            }
        }
    }

    public class ResultTableRowRenderer extends DefaultTableCellRenderer {

        ResultTableRowRenderer() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        }
    }

    public class DataTableModel extends AbstractTableModel {

        public int getColumnCount() {
            return data[0].length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int aCol) {
            if (aCol >= getColumnCount()) return col_names[getColumnCount() - 1]; else return col_names[aCol];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("File's name to find data ?");
            System.exit(0);
        }
        try {
            new PopupTable(new JFrame(), args[0]).setVisible(true);
        } catch (Exception e) {
            ErrorPane.showErrorMessage(new JFrame(), null, e);
            e.printStackTrace();
            System.exit(0);
        }
    }
}
