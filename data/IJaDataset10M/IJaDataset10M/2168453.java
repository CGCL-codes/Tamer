package org.jahia.sqlprofiler.gui;

import javax.swing.table.*;
import java.util.*;
import java.text.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;

/**
 * <p>Title: SQL Profiler</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Jahia Ltd</p>
 * @author Serge Huber
 * @version 1.0
 */
public class ProfileResultTableModel extends AbstractTableModel implements Comparator {

    private ArrayList profileResults = new ArrayList();

    protected int currCol = 0;

    protected Vector ascendCol = new Vector();

    protected Integer one = new Integer(1);

    protected Integer minusOne = new Integer(-1);

    private static final String[] COLUMN_NAMES = { "%", "Time[ms]", "Count", "Table(s)", "Column(s)" };

    public ProfileResultTableModel() {
        for (int i = 0; i < COLUMN_NAMES.length; i++) {
            ascendCol.add(one);
        }
    }

    public int getRowCount() {
        return profileResults.size();
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public String getColumnName(int aCol) {
        return COLUMN_NAMES[aCol];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ProfileReportResult curResult = (ProfileReportResult) profileResults.get(rowIndex);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        Object result = null;
        switch(columnIndex) {
            case 0:
                result = nf.format(curResult.getPercentage());
                break;
            case 1:
                result = new Long(curResult.getTotalElapsedTime());
                break;
            case 2:
                result = new Integer(curResult.getOccurences());
                break;
            case 3:
                StringBuffer resultBuf = new StringBuffer();
                Iterator tableNameIter = curResult.getTableNames().iterator();
                while (tableNameIter.hasNext()) {
                    String curTableName = (String) tableNameIter.next();
                    resultBuf.append(curTableName);
                    if (tableNameIter.hasNext()) {
                        resultBuf.append(",");
                    }
                }
                result = resultBuf.toString();
                break;
            case 4:
                StringBuffer resultBuf2 = new StringBuffer();
                Iterator columnNameIter = curResult.getColumnNames().iterator();
                while (columnNameIter.hasNext()) {
                    String curColumnName = (String) columnNameIter.next();
                    resultBuf2.append(curColumnName);
                    if (columnNameIter.hasNext()) {
                        resultBuf2.append(",");
                    }
                }
                result = resultBuf2.toString();
                break;
        }
        return result;
    }

    public void clear() {
        profileResults.clear();
        currCol = 0;
        fireTableDataChanged();
    }

    public void addProfileReportResult(ProfileReportResult profileReportResult) {
        profileResults.add(profileReportResult);
    }

    public int compare(Object v1, Object v2) {
        int ascending = ((Integer) ascendCol.get(currCol)).intValue();
        if (v1 == null && v2 == null) {
            return 0;
        } else if (v2 == null) {
            return 1 * ascending;
        } else if (v1 == null) {
            return -1 * ascending;
        }
        ProfileReportResult left = (ProfileReportResult) v1;
        ProfileReportResult right = (ProfileReportResult) v2;
        Object o1 = null;
        Object o2 = null;
        switch(currCol) {
            case 0:
                o1 = new Double(left.getPercentage());
                o2 = new Double(right.getPercentage());
                break;
            case 1:
                o1 = new Long(left.getTotalElapsedTime());
                o2 = new Long(right.getTotalElapsedTime());
                break;
            case 2:
                o1 = new Integer(left.getOccurences());
                o2 = new Integer(right.getOccurences());
                break;
            case 3:
                o1 = left.getTableNames();
                o2 = right.getTableNames();
                break;
            case 4:
                o1 = left.getColumnNames();
                o2 = right.getColumnNames();
                break;
        }
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o2 == null) {
            return 1 * ascending;
        } else if (o1 == null) {
            return -1 * ascending;
        }
        if (o1 instanceof Number && o2 instanceof Number) {
            Number n1 = (Number) o1;
            double d1 = n1.doubleValue();
            Number n2 = (Number) o2;
            double d2 = n2.doubleValue();
            if (d1 == d2) {
                return 0;
            } else if (d1 > d2) {
                return 1 * ascending;
            } else {
                return -1 * ascending;
            }
        } else if (o1 instanceof Boolean && o2 instanceof Boolean) {
            Boolean bool1 = (Boolean) o1;
            boolean b1 = bool1.booleanValue();
            Boolean bool2 = (Boolean) o2;
            boolean b2 = bool2.booleanValue();
            if (b1 == b2) {
                return 0;
            } else if (b1) {
                return 1 * ascending;
            } else {
                return -1 * ascending;
            }
        } else {
            if (o1 instanceof Comparable && o2 instanceof Comparable) {
                Comparable c1 = (Comparable) o1;
                Comparable c2 = (Comparable) o2;
                try {
                    return c1.compareTo(c2) * ascending;
                } catch (ClassCastException cce) {
                }
            }
            String s1 = o1.toString();
            String s2 = o2.toString();
            return s1.compareTo(s2) * ascending;
        }
    }

    public void sort() {
        Collections.sort(profileResults, this);
        Integer val = (Integer) ascendCol.get(currCol);
        ascendCol.remove(currCol);
        if (val.equals(one)) ascendCol.add(currCol, minusOne); else ascendCol.add(currCol, one);
    }

    public void sortByColumn(int column) {
        this.currCol = column;
        sort();
        fireTableDataChanged();
    }

    public void addMouseListenerToHeaderInTable(JTable table) {
        final ProfileResultTableModel sorter = this;
        final JTable tableView = table;
        tableView.setColumnSelectionAllowed(false);
        MouseAdapter listMouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);
                if (e.getClickCount() == 1 && column != -1) {
                    int shiftPressed = e.getModifiers() & InputEvent.SHIFT_MASK;
                    boolean ascending = (shiftPressed == 0);
                    sorter.sortByColumn(column);
                }
            }
        };
        JTableHeader th = tableView.getTableHeader();
        th.addMouseListener(listMouseListener);
    }
}
