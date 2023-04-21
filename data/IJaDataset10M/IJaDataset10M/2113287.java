package sql;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.Element;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.gui.*;
import org.gjt.sp.jedit.msg.*;
import org.gjt.sp.util.*;
import common.gui.*;
import sql.*;

/**
 *  Description of the Class
 *
 * @author     svu
 */
public class ResultSetPanel extends JPanel {

    protected Runnable repeaterThread;

    protected boolean stopRepeatingQuery;

    protected final String formattedQuery;

    protected JComponent dataComponent;

    protected JLabel recordsCountLabel;

    protected HelpfulJTable dataTable;

    protected static final String MAX_RECS_TO_SHOW_PROP = "sql.maxRecordsToShow";

    protected static final String QUERY_EXEC_PERIOD_PROP = "sql.queryExecutionPeriod";

    protected static final String AUTORESIZE = "sql.autoresizeResult";

    protected static final String CLOSE_WITH_BUFFER = "sql.closeWithBuffer";

    protected int queryExecutionPeriod;

    protected int sortOrder = HelpfulJTable.SORT_OFF;

    protected int sortColumn = -1;

    /**
	 *  Constructor for the ResultSetWindow object
	 *
	 */
    public ResultSetPanel(Data data, final JTabbedPane notebook) {
        super(new BorderLayout());
        final SqlServerRecord sqlServer = data.getServerRecord();
        final String query = data.getQueryText();
        final Pattern[] patterns = getPatterns();
        formattedQuery = formatQuery(query);
        final JPanel p1 = new JPanel(new BorderLayout());
        final JLabel serverLbl = new JLabel(sqlServer.getName(), SwingConstants.LEFT);
        serverLbl.setToolTipText(sqlServer.getServerType().getName());
        final JPanel p2 = new JPanel(new GridLayout(1, 2, 3, 0));
        final JButton closeBtn = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/closebox.png"))));
        closeBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                notebook.remove(ResultSetPanel.this);
                stopRequerying();
            }
        });
        closeBtn.setToolTipText(jEdit.getProperty("sql.resultSet.close.tooltip"));
        p2.add(createRepeatQueryMenu(data));
        p2.add(closeBtn);
        p1.add(BorderLayout.EAST, p2);
        p1.add(BorderLayout.WEST, serverLbl);
        add(BorderLayout.NORTH, p1);
        updateDataSet(data);
    }

    protected static Pattern[] patterns = null;

    protected static Pattern[] getPatterns() {
        if (patterns == null) {
            final String[] keywords = new String[] { "from", "select", "where", "and", "or", "order by", "group by", "like", "distinct", "like", "in", "as" };
            patterns = new Pattern[keywords.length];
            for (int i = patterns.length; --i >= 0; ) patterns[i] = Pattern.compile("\\b(" + keywords[i] + ")\\b", Pattern.CASE_INSENSITIVE);
        }
        return patterns;
    }

    public String formatQuery(String query) {
        final String lq = query.length() > 128 ? query.substring(0, 127) : query;
        String lqf = lq.replaceAll("\n", "<p>");
        for (int i = patterns.length; --i >= 0; ) lqf = patterns[i].matcher(lqf).replaceAll("<b>$1</b>");
        return "<html>" + lqf + "</html>";
    }

    public void setRenderers(JTable table, final SqlServerType type) {
        table.setDefaultRenderer(Object.class, new Renderer(type));
    }

    public void updateDataSet(Data data) {
        if (dataComponent != null) {
            remove(dataComponent);
            dataComponent.setVisible(false);
            dataComponent = null;
            remove(recordsCountLabel);
            recordsCountLabel.setVisible(false);
            recordsCountLabel = null;
        }
        add(BorderLayout.CENTER, createDataView(data));
        add(BorderLayout.SOUTH, createRecCountLabel(data));
        addMouseListener(new MouseHandler(dataTable));
    }

    protected JLabel createRecCountLabel(Data data) {
        final int recCount = data.recCount;
        final Object[] args = { new Integer(recCount) };
        final int maxRecs = getMaxRecordsToShow();
        if (recCount > maxRecs) args[0] = new String(" > " + maxRecs);
        final String lblText = jEdit.getProperty("sql.resultSet.info", args);
        recordsCountLabel = new JLabel(lblText, SwingConstants.LEFT);
        return recordsCountLabel;
    }

    protected JButton createRepeatQueryMenu(final Data data) {
        final JButton repeatQueryBtn = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/repeatSameQuery.png"))));
        repeatQueryBtn.setToolTipText(jEdit.getProperty("sql.resultSet.repeatQuery.tooltip"));
        final JPopupMenu ppm = new JPopupMenu();
        final JMenuItem romi = new JMenuItem(jEdit.getProperty("sql.resultSet.repeatQuery.once.label"));
        romi.setFocusPainted(false);
        romi.setToolTipText(jEdit.getProperty("sql.resultSet.repeatQuery.once.tooltip"));
        ppm.add(romi);
        romi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                SqlTextPublisher.publishText(jEdit.getActiveView(), data.getQueryText(), data.getServerRecord(), ResultSetPanel.this);
            }
        });
        ppm.add(new JSeparator());
        queryExecutionPeriod = getQueryExecutionPeriod();
        final Object[] args = new Object[] { new Integer(queryExecutionPeriod) };
        final JMenuItem remi = new JCheckBoxMenuItem(jEdit.getProperty("sql.resultSet.repeatQuery.every.label", args));
        remi.setFocusPainted(false);
        remi.setToolTipText(jEdit.getProperty("sql.resultSet.repeatQuery.every.tooltip", args));
        ppm.add(remi);
        remi.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent evt) {
                if (remi.getSelectedObjects() != null) {
                    if (repeaterThread == null) {
                        stopRepeatingQuery = false;
                        repeaterThread = new RepeaterThread(data);
                        new Thread(repeaterThread).start();
                    }
                } else {
                    stopRequerying();
                }
            }
        });
        repeatQueryBtn.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                if (ppm.isVisible()) {
                    ppm.setVisible(false);
                    return;
                }
                GUIUtilities.showPopupMenu(ppm, repeatQueryBtn, 0, repeatQueryBtn.getHeight(), false);
            }
        });
        return repeatQueryBtn;
    }

    /**
	 *  Sets the SortOrder attribute of the ResultSetWindow object
	 *
	 * @param  order  The new SortOrder value
	 * @param  table  The new SortOrder value
	 * @param  data   The new SortOrder value
	 */
    protected void setSortOrder(JTable table, Data data, int order) {
        sortOrder = order;
        resort(table, data);
    }

    /**
	 *  Sets the SortColumn attribute of the ResultSetWindow object
	 *
	 * @param  column  The new SortColumn value
	 * @param  table   The new SortColumn value
	 * @param  data    The new SortColumn value
	 */
    protected void setSortColumn(JTable table, Data data, int column) {
        sortColumn = column;
        resort(table, data);
    }

    public String getFormattedQuery() {
        return formattedQuery;
    }

    /**
	 *  Description of the Method
	 *
	 * @param  table  Description of Parameter
	 * @param  data   Description of Parameter
	 */
    protected void resort(JTable table, Data data) {
        if (data == null || data.rowData == null || data.columnNames == null) return;
        if (sortOrder == HelpfulJTable.SORT_OFF) {
            table.setModel(data);
            return;
        }
        if (sortColumn < 0 || sortColumn >= data.columnNames.length) return;
        if (!(sortOrder == HelpfulJTable.SORT_ASCENDING || sortOrder == HelpfulJTable.SORT_DESCENDING)) return;
        final boolean isAscending = sortOrder == HelpfulJTable.SORT_ASCENDING;
        Arrays.sort(data.rowData, new Comparator() {

            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof Object[] && o2 instanceof Object[])) throw new ClassCastException();
                final Object[] row1 = (Object[]) o1;
                final Object[] row2 = (Object[]) o2;
                final Object do1 = row1[sortColumn];
                final Object do2 = row2[sortColumn];
                final int rv = (do1 instanceof Comparable) ? ((Comparable) do1).compareTo(do2 == null ? "" : do2) : ("" + do1).compareTo("" + do2);
                return isAscending ? rv : -rv;
            }

            public boolean equals(Object o) {
                return false;
            }
        });
        table.setModel(data);
    }

    /**
	 *  Description of the Method
	 *
	 * @param  data  Description of Parameter
	 * @return       Description of the Returned Value
	 * @since
	 */
    protected JComponent createDataView(final Data data) {
        dataTable = new HelpfulJTable();
        dataTable.addPropertyChangeListener("sortOrder", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                setSortOrder(dataTable, data, ((Number) evt.getNewValue()).intValue());
            }
        });
        dataTable.addPropertyChangeListener("sortColumn", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                setSortColumn(dataTable, data, ((Number) evt.getNewValue()).intValue());
            }
        });
        setRenderers(dataTable, data.getServerRecord().getServerType());
        if (getAutoResize()) {
            dataTable.setAutoResizeColumns(true);
            dataTable.setAutoResizeWithHeaders(true);
            dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        } else {
            dataTable.setAutoResizeColumns(false);
            dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
        setSortOrder(dataTable, data, HelpfulJTable.SORT_OFF);
        dataTable.addMouseListener(new MouseHandler(dataTable));
        dataTable.setTableHeader(new TableHeader(dataTable, data.columnTypeNames));
        return dataComponent = new JScrollPane(dataTable);
    }

    public void stopRequerying() {
        stopRepeatingQuery = true;
    }

    /**
	 *  Sets the MaxRecordsToShow attribute of the ResultSetWindow class
	 *
	 * @param  maxRecs  The new MaxRecordsToShow value
	 * @since
	 */
    public static final void setMaxRecordsToShow(int maxRecs) {
        SqlPlugin.setGlobalProperty(MAX_RECS_TO_SHOW_PROP, "" + maxRecs);
    }

    /**
	 *  Sets the AutoResize attribute of the ResultSetWindow class
	 *
	 * @param  autoResize  The new AutoResize value
	 * @since
	 */
    public static final void setAutoResize(boolean autoResize) {
        SqlPlugin.setGlobalProperty(AUTORESIZE, "" + autoResize);
    }

    /**
	 *  Sets the CloseWithBuffer attribute of the ResultSetWindow class
	 *
	 * @param  closeWithBuffer  The new CloseWithBuffer value
	 * @since
	 */
    public static final void setCloseWithBuffer(boolean closeWithBuffer) {
        SqlPlugin.setGlobalProperty(CLOSE_WITH_BUFFER, "" + closeWithBuffer);
    }

    /**
	 *  Gets the MaxRecordsToShow attribute of the ResultSetWindow class
	 *
	 * @return    The MaxRecordsToShow value
	 * @since
	 */
    public static final int getMaxRecordsToShow() {
        try {
            return Integer.parseInt(SqlPlugin.getGlobalProperty(MAX_RECS_TO_SHOW_PROP));
        } catch (NumberFormatException ex) {
            return 10;
        } catch (NullPointerException ex) {
            return 10;
        }
    }

    public static final void setQueryExecutionPeriod(int secs) {
        SqlPlugin.setGlobalProperty(QUERY_EXEC_PERIOD_PROP, "" + secs);
    }

    public static final int getQueryExecutionPeriod() {
        try {
            return Integer.parseInt(SqlPlugin.getGlobalProperty(QUERY_EXEC_PERIOD_PROP));
        } catch (NumberFormatException ex) {
            return 3;
        } catch (NullPointerException ex) {
            return 3;
        }
    }

    /**
	 *  Gets the AutoResize attribute of the ResultSetWindow class
	 *
	 * @return    The AutoResize value
	 * @since
	 */
    public static final boolean getAutoResize() {
        try {
            return Boolean.valueOf(SqlPlugin.getGlobalProperty(AUTORESIZE)).booleanValue();
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
	 *  Gets the CloseWithBuffer attribute of the ResultSetWindow class
	 *
	 * @return    The CloseWithBuffer value
	 * @since
	 */
    public static final boolean getCloseWithBuffer() {
        try {
            return Boolean.valueOf(SqlPlugin.getGlobalProperty(CLOSE_WITH_BUFFER)).booleanValue();
        } catch (NullPointerException ex) {
            return false;
        }
    }

    /**
	 *Description of the Method
	 *
	 * @since
	 */
    public static final void clearProperties() {
        SqlPlugin.unsetGlobalProperty(MAX_RECS_TO_SHOW_PROP);
        SqlPlugin.unsetGlobalProperty(QUERY_EXEC_PERIOD_PROP);
        SqlPlugin.unsetGlobalProperty(AUTORESIZE);
        SqlPlugin.unsetGlobalProperty(CLOSE_WITH_BUFFER);
    }

    /**
	 *Description of the Method
	 *
	 * @param  rs                Description of Parameter
	 * @return                   Description of the Returned Value
	 * @exception  SQLException  Description of Exception
	 * @since
	 */
    public static Data prepareModel(SqlServerRecord record, String query, ResultSet rs) throws SQLException {
        int recCount = 0;
        if (rs == null) return null;
        final ResultSetMetaData rsmd = rs.getMetaData();
        final int colNumber = rsmd.getColumnCount();
        final String[] columnNames = new String[colNumber];
        final String[] columnTypeNames = new String[colNumber];
        final int[] columnTypes = new int[colNumber];
        for (int i = colNumber + 1; --i > 0; ) {
            columnNames[i - 1] = rsmd.getColumnName(i);
            String type = rsmd.getColumnTypeName(i);
            columnTypes[i - 1] = rsmd.getColumnType(i);
            try {
                switch(rsmd.getColumnType(i)) {
                    case Types.CLOB:
                    case Types.BLOB:
                        break;
                    default:
                        final int precision = rsmd.getPrecision(i);
                        if (precision != 0) {
                            final int scale = rsmd.getScale(i);
                            type += "[" + precision + ((scale == 0) ? "" : ("." + scale)) + "]";
                        }
                }
            } catch (Exception ex) {
            }
            if (rsmd.columnNoNulls == rsmd.isNullable(i)) type += "/" + jEdit.getProperty("sql.resultSet.colHeaders.notNullable");
            columnTypeNames[i - 1] = type;
        }
        final java.util.List rowData = new ArrayList();
        final int maxRecs = getMaxRecordsToShow();
        while (rs.next()) {
            if (++recCount > maxRecs) break;
            final Object[] aRow = new Object[colNumber];
            int j = 1;
            for (int i = colNumber; --i >= 0; j++) aRow[j - 1] = col2Object(record, rsmd, rs, j);
            rowData.add(aRow);
        }
        Log.log(Log.DEBUG, ResultSetWindow.class, "Got " + rowData.size() + " records in " + columnNames.length + " columns");
        return new Data(record, query, (Object[][]) rowData.toArray(new Object[0][]), columnNames, columnTypeNames, columnTypes, recCount);
    }

    /**
	 *  Description of the Method
	 *
	 * @param  rsmd              Description of Parameter
	 * @param  rs                Description of Parameter
	 * @param  idx               Description of Parameter
	 * @return                   Description of the Returned Value
	 * @exception  SQLException  Description of Exception
	 */
    protected static Object col2Object(SqlServerRecord record, ResultSetMetaData rsmd, ResultSet rs, int idx) throws SQLException {
        return record.getServerType().toObject(rs, rsmd.getColumnType(idx), idx);
    }

    protected class MouseHandler extends MouseAdapter {

        protected JTable table;

        /**
		 *Constructor for the MouseHandler object
		 *
		 * @param  table  Description of Parameter
		 * @since
		 */
        public MouseHandler(JTable table) {
            this.table = table;
        }

        public void mousePressed(MouseEvent evt) {
            final Point p = evt.getPoint();
            if ((evt.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
                final ResultSetWindowPopup rswp = new ResultSetWindowPopup(table, evt.getPoint());
                rswp.show(ResultSetPanel.this, p.x + 1, p.y + 1);
                evt.consume();
                return;
            }
            if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2) {
                final String contents = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
                final JDialog d = new JDialog(jEdit.getActiveView(), "Cell Data");
                final JScrollPane sp = new JScrollPane(new JLabel(contents));
                d.getContentPane().add(sp);
                d.pack();
                d.setVisible(true);
            }
        }
    }

    protected static class TableHeader extends JTableHeader {

        protected String types[];

        /**
		 *Constructor for the TableHeader object
		 *
		 * @param  table  Description of Parameter
		 * @param  types  Description of Parameter
		 * @since
		 */
        public TableHeader(JTable table, String types[]) {
            super(table.getColumnModel());
            this.types = types;
        }

        public String getToolTipText(MouseEvent evt) {
            final Point p = evt.getPoint();
            if (p == null) return null;
            final int colNo = columnAtPoint(p);
            if (colNo == -1) return null;
            return types[colNo];
        }
    }

    protected static class Data extends AbstractTableModel {

        private SqlServerRecord serverRecord;

        private String queryText;

        private Object rowData[][];

        private String columnNames[];

        private String columnTypeNames[];

        private int columnTypes[];

        private int recCount;

        /**
		 *Constructor for the Data object
		 *
		 * @param  rowData      Description of Parameter
		 * @param  recCount     Description of Parameter
		 * @param  columnNames  Description of Parameter
		 * @param  columnTypes  Description of Parameter
		 * @since
		 */
        public Data(SqlServerRecord serverRecord, String queryText, Object rowData[][], String columnNames[], String columnTypeNames[], int columnTypes[], int recCount) {
            this.serverRecord = serverRecord;
            this.queryText = queryText;
            this.rowData = rowData;
            this.columnNames = columnNames;
            this.columnTypeNames = columnTypeNames;
            this.columnTypes = columnTypes;
            this.recCount = recCount;
        }

        public String getQueryText() {
            return queryText;
        }

        public String[] getColumnHeaders() {
            return columnNames;
        }

        public int[] getColumnTypes() {
            return columnTypes;
        }

        public int getRowCount() {
            return rowData.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int r, int c) {
            if (r >= rowData.length || r < 0) return null;
            if (c >= columnNames.length || c < 0) return null;
            return rowData[r][c];
        }

        public String getColumnName(int c) {
            return columnNames[c];
        }

        public boolean isCellEditable(int r, int c) {
            return false;
        }

        public Object[] getRowData(int r) {
            return rowData[r];
        }

        public SqlServerRecord getServerRecord() {
            return serverRecord;
        }
    }

    protected static class Renderer extends DefaultTableCellRenderer {

        protected SqlServerType serverType;

        public Renderer(SqlServerType serverType) {
            this.serverType = serverType;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final String s = serverType.toString(value);
            return super.getTableCellRendererComponent(table, s, isSelected, hasFocus, row, column);
        }
    }

    ;

    protected class RepeaterThread implements Runnable {

        protected Data data;

        public RepeaterThread(Data data) {
            this.data = data;
        }

        public void run() {
            while (!stopRepeatingQuery) {
                SqlTextPublisher.publishText(jEdit.getActiveView(), data.getQueryText(), data.getServerRecord(), ResultSetPanel.this);
                try {
                    Thread.sleep(1000L * queryExecutionPeriod);
                } catch (Exception ex) {
                }
            }
            repeaterThread = null;
        }
    }

    ;
}
