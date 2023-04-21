package com.iver.cit.gvsig.mdb.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.channels.ClosedByInterruptException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.jdesktop.swingworker.SwingWorker;
import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.Index;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.DataType;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

/**
 * Class for converting an Access database to a HSQLDB database,
 * using the <a href="http://jackcess.sourceforge.net/">Jackcess</a> library.
 * The support is limited to Tables, Data, Data Types and Primary Keys (i.e.,
 * validation rules, nullables fields, etc., are not translated).
 * @author Stefano Orlando
 */
public class MDBtoHSQLDB extends JPanel implements IWindow {

    private static final long serialVersionUID = 1718193978442987848L;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JPanel jPanel0;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JProgressBar jProgressBar1;

    private WindowInfo m_viewInfo;

    private static final int WINDOW_WIDTH = 400;

    private static final int WINDOW_HEIGHT = 50;

    private static Map<Integer, String> mapSqlTypes;

    private Database mdbDB;

    private Connection conHSQLDBDBConnection;

    private MDBConverterTask mdbConverterTask;

    boolean canStart = true;

    private class MDBConverterTask extends SwingWorker<Boolean, Void> {

        private volatile String label1Text = "";

        private volatile String label2Text = "";

        private volatile String windowTitle = "";

        private volatile String button1Text = "";

        private boolean result = false;

        private boolean isError = false;

        private boolean toSkip = false;

        public void setLabel1Text(String label1Text) {
            if (this.label1Text.equalsIgnoreCase(label1Text)) return;
            String oldLabel1Text = this.label1Text;
            this.label1Text = label1Text;
            if (getPropertyChangeSupport().hasListeners("label1Text")) {
                firePropertyChange("label1Text", oldLabel1Text, this.label1Text);
            }
        }

        public String getLabel1Text() {
            return label1Text;
        }

        public void setLabel2Text(String label2Text) {
            if (this.label2Text.equalsIgnoreCase(label2Text)) return;
            String oldLabel2Text = this.label2Text;
            this.label2Text = label2Text;
            if (getPropertyChangeSupport().hasListeners("label2Text")) {
                firePropertyChange("label2Text", oldLabel2Text, this.label2Text);
            }
        }

        public String getLabel2Text() {
            return label2Text;
        }

        public void setWindowTitle(String windowTitle) {
            if (this.windowTitle.equalsIgnoreCase(windowTitle)) return;
            String oldWindowTitle = this.windowTitle;
            this.windowTitle = windowTitle;
            if (getPropertyChangeSupport().hasListeners("windowTitle")) {
                firePropertyChange("windowTitle", oldWindowTitle, this.windowTitle);
            }
        }

        public String getWindowTitle() {
            return windowTitle;
        }

        public void setButton1Text(String button1Text) {
            if (this.button1Text.equalsIgnoreCase(button1Text)) return;
            String oldButton1Text = this.button1Text;
            this.button1Text = button1Text;
            if (getPropertyChangeSupport().hasListeners("button1Text")) {
                firePropertyChange("button1Text", oldButton1Text, this.button1Text);
            }
        }

        public String getButton1Text() {
            return button1Text;
        }

        private void printError(String msg) {
            if (isCancelled() == false) System.err.println(PluginServices.getText(this, "unexpected_MDB2HSQLDB_conversion_error") + "\n" + msg);
            isError = true;
        }

        protected Boolean doInBackground() throws Exception {
            Set<String> mdbTableNameList = null;
            PreparedStatement ps = null;
            mdbTableNameList = mdbDB.getTableNames();
            int progress = 0;
            int currTable = 0;
            for (String mdbTableName : mdbTableNameList) {
                setLabel2Text(PluginServices.getText(this, "table") + " [" + (++currTable) + "/" + mdbTableNameList.size() + "]");
                Table mdbTable = null;
                try {
                    mdbTable = mdbDB.getTable(mdbTableName);
                } catch (ClosedByInterruptException ex) {
                    break;
                } catch (IOException ex) {
                    printError(ex.getMessage());
                    break;
                }
                List<Column> mdbTableColumnList = mdbTable.getColumns();
                List<Index> mdbTableIndexList = mdbTable.getIndexes();
                String colList = "(";
                String sql = "CREATE TABLE " + mdbTableName + " (";
                for (Column mdbTableColumn : mdbTableColumnList) {
                    String mdbTableColumnName = mdbTableColumn.getName();
                    if (mdbTableColumnName.indexOf(" ") > -1) {
                        mdbTableColumnName = "\"" + mdbTableColumnName + "\"";
                    }
                    try {
                        sql += mdbTableColumnName + " " + getSQLTypeName(mdbTableColumn.getSQLType());
                    } catch (SQLException ex) {
                        printError(ex.getMessage());
                        toSkip = true;
                        break;
                    }
                    colList += mdbTableColumnName + ", ";
                    if (mdbTableColumn.getType() == DataType.TEXT) {
                        sql += "(" + mdbTableColumn.getType().getMaxSize() / (int) 2 + ")";
                    }
                    sql += ", ";
                }
                if (toSkip) break;
                for (Index mdbTableIndex : mdbTableIndexList) {
                    if (mdbTableIndex.isPrimaryKey()) {
                        sql += "CONSTRAINT " + mdbTableName + "_" + mdbTableIndex.getName() + " PRIMARY KEY (";
                        List<Index.ColumnDescriptor> mdbTableIndexColumnDescriptorList = mdbTableIndex.getColumns();
                        for (Index.ColumnDescriptor mdbTableIndexColumnDescriptor : mdbTableIndexColumnDescriptorList) {
                            String mdbTableIndexColumnDescriptorName = mdbTableIndexColumnDescriptor.getName();
                            if (mdbTableIndexColumnDescriptorName.indexOf(" ") > -1) {
                                mdbTableIndexColumnDescriptorName = "\"" + mdbTableIndexColumnDescriptorName + "\"";
                            }
                            sql += mdbTableIndexColumnDescriptorName + ", ";
                        }
                        sql = sql.substring(0, sql.length() - 2);
                        sql += "), ";
                    }
                }
                sql = sql.substring(0, sql.length() - 2);
                sql += ");";
                colList = colList.substring(0, colList.length() - 2);
                colList += ")";
                Statement s;
                try {
                    s = conHSQLDBDBConnection.createStatement();
                    s.execute(sql);
                } catch (SQLException ex) {
                    printError(ex.getMessage());
                    break;
                }
                sql = "INSERT INTO " + mdbTableName + " " + colList + " VALUES (";
                for (int i = 0; i < mdbTableColumnList.size(); i++) {
                    sql += "?, ";
                }
                sql = sql.substring(0, sql.length() - 2);
                sql += ");";
                try {
                    ps = conHSQLDBDBConnection.prepareStatement(sql);
                } catch (SQLException ex) {
                    printError(ex.getMessage());
                    break;
                }
                Map<String, Object> row = null;
                progress = 0;
                int rowCount = mdbTable.getRowCount();
                if (rowCount == 0) {
                    int i = 0;
                    while (true) {
                        try {
                            row = mdbTable.getNextRow();
                            i++;
                            if (row == null) {
                                rowCount = i - 1;
                                mdbTable.reset();
                                break;
                            }
                        } catch (ClosedByInterruptException ex) {
                            toSkip = true;
                            break;
                        } catch (IOException ex) {
                            printError(ex.getMessage());
                            toSkip = true;
                            break;
                        }
                    }
                }
                if (toSkip) break;
                for (int i = 0; i < rowCount; i++) {
                    ps.clearParameters();
                    if (progress != 100 * (i + 1) / rowCount) {
                        progress = 100 * (i + 1) / rowCount;
                        setProgress(progress);
                    }
                    try {
                        row = mdbTable.getNextRow();
                    } catch (ClosedByInterruptException ex) {
                        toSkip = true;
                        break;
                    } catch (IOException ex) {
                        printError(ex.getMessage());
                        toSkip = true;
                        break;
                    }
                    int j = 0;
                    for (Column mdbTableColumn : mdbTableColumnList) {
                        if (!row.containsKey(mdbTableColumn.getName())) {
                            printError("Column not found: " + mdbTableColumn.getName());
                            toSkip = true;
                            break;
                        }
                        Object value = row.get(mdbTableColumn.getName());
                        try {
                            if (value == null) {
                                ps.setNull(++j, java.sql.Types.NULL);
                                continue;
                            }
                            switch(mdbTableColumn.getType()) {
                                case BINARY:
                                    ps.setBytes(++j, (byte[]) value);
                                    break;
                                case BOOLEAN:
                                    ps.setBoolean(++j, ((Boolean) value).booleanValue());
                                    break;
                                case BYTE:
                                    ps.setByte(++j, ((Byte) value).byteValue());
                                    break;
                                case DOUBLE:
                                    ps.setDouble(++j, ((Double) value).doubleValue());
                                    break;
                                case FLOAT:
                                    ps.setFloat(++j, ((Float) value).floatValue());
                                    break;
                                case GUID:
                                    ps.setString(++j, value.toString());
                                    break;
                                case INT:
                                    ps.setShort(++j, ((Short) value).shortValue());
                                    break;
                                case LONG:
                                    ps.setInt(++j, ((Integer) value).intValue());
                                    break;
                                case MEMO:
                                    ps.setString(++j, value.toString());
                                    break;
                                case MONEY:
                                    ps.setBigDecimal(++j, ((BigDecimal) value));
                                    break;
                                case NUMERIC:
                                    ps.setBigDecimal(++j, ((BigDecimal) value));
                                    break;
                                case OLE:
                                    ps.setBytes(++j, (byte[]) value);
                                    break;
                                case SHORT_DATE_TIME:
                                    ps.setDate(++j, new java.sql.Date(((java.util.Date) value).getTime()));
                                    break;
                                case TEXT:
                                    ps.setString(++j, value.toString());
                                    break;
                                case UNKNOWN_0D:
                                    ps.setBytes(++j, (byte[]) value);
                                    break;
                                case UNKNOWN_11:
                                    ps.setBytes(++j, (byte[]) value);
                                    break;
                            }
                        } catch (SQLException ex) {
                            printError(ex.getMessage());
                            isError = true;
                            toSkip = true;
                            break;
                        }
                    }
                    if (toSkip) break;
                    try {
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        isError = true;
                        toSkip = true;
                        break;
                    }
                }
                if (toSkip) break;
            }
            mdbDB.close();
            jButton1.setName("OK");
            setButton1Text(PluginServices.getText(this, "ok_button"));
            setLabel1Text(PluginServices.getText(this, "converting_database") + " " + PluginServices.getText(this, "done_message"));
            setWindowTitle(PluginServices.getText(this, "converting_completed"));
            setProgress(100);
            if (currTable == 0) {
                JOptionPane.showMessageDialog(null, PluginServices.getText(this, "mdb_empty_warning"), PluginServices.getText(this, "warning_message"), JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (isError == true) {
                JOptionPane.showMessageDialog(null, PluginServices.getText(this, "unexpected_execution_error"), PluginServices.getText(this, "error_message"), JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }

        protected void done() {
            try {
                result = get();
            } catch (CancellationException ex) {
                result = false;
            } catch (InterruptedException ex) {
                result = false;
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                result = false;
                ex.printStackTrace();
            }
        }

        protected Boolean getResult() {
            return result;
        }
    }

    /**
     * Creates a new MDBtoHSQLDB dialog that performs the conversion between the
     * Access database and the HSQLDB database.
     * @param mdbFile - the Access database's file (source)
     * @param conHSQLDBDBConnection - the connection to the HSQLDB database (destination)
     * @author Stefano Orlando
     */
    public MDBtoHSQLDB(File mdbFile, Connection conHSQLDBDBConnection) {
        super();
        this.conHSQLDBDBConnection = conHSQLDBDBConnection;
        try {
            mdbDB = Database.open(mdbFile);
        } catch (IOException ex) {
            String message = null;
            if (ex.getMessage().toLowerCase().startsWith("Unsupported version".toLowerCase())) {
                message = "format_error";
            } else if (ex.getMessage().toLowerCase().startsWith("Empty database".toLowerCase())) {
                message = "format_error";
            } else {
                message = "unexpected_execution_error";
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, PluginServices.getText(this, message), PluginServices.getText(this, "error_message"), JOptionPane.ERROR_MESSAGE);
            canStart = false;
        }
        initComponents();
        initActionListeners();
        makeLayout();
    }

    /**
	 * Returns the setup's status of the conversion class (i.e., if the conversion can start
	 * or not, depending on the result of the opening process of the Access database).
	 * @return <code>true</code> if the conversion can start, <code>false</code> otherwise
	 * @author Stefano Orlando
	 */
    public boolean canStart() {
        return canStart;
    }

    /**
	 * Returns the (java.sql.Types) field's name corresponding to a SQL type constant.
	 * @param sqlType - the constant that identifies the generic SQL type
	 * @return the (java.sql.Types) field's name
	 * @author Stefano Orlando
	 */
    private static String getSQLTypeName(int sqlType) {
        if (mapSqlTypes == null) {
            mapSqlTypes = new HashMap<Integer, String>();
            Field[] fields = java.sql.Types.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    String name = fields[i].getName();
                    Integer value = (Integer) fields[i].get(null);
                    mapSqlTypes.put(value, name);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return (String) mapSqlTypes.get(new Integer(sqlType));
    }

    /**
     * Defines the layout of the window.
     * @author Stefano Orlando
     */
    private void makeLayout() {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        jLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(jLabel1);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        jPanel0.setLayout(new BoxLayout(jPanel0, BoxLayout.LINE_AXIS));
        jLabel2.setAlignmentX(LEFT_ALIGNMENT);
        jPanel0.add(jLabel2);
        jPanel0.add(Box.createHorizontalGlue());
        jPanel0.add(jProgressBar1);
        jPanel0.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(jPanel0);
        mainPanel.add(Box.createVerticalGlue());
        jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.LINE_AXIS));
        jButton1.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPanel1.add(Box.createHorizontalGlue());
        jPanel1.add(jButton1);
        jPanel1.add(Box.createHorizontalGlue());
        jPanel1.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(jPanel1);
        add(mainPanel);
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        add(mainPanel, c);
        gbl.layoutContainer(this);
        double[][] weights = gbl.getLayoutWeights();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = 1.0d;
            }
        }
        gbl.columnWeights = weights[0];
        gbl.rowWeights = weights[1];
    }

    /**
     * Initializes the GUI's components of the window.
     * @author Stefano Orlando
     */
    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        jPanel0 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        int numTables = 0;
        if (mdbDB != null) numTables = mdbDB.getTableNames().size();
        FontMetrics fontMetrics = jLabel2.getFontMetrics(jLabel2.getFont());
        int jLabel2Width = (int) fontMetrics.getStringBounds(PluginServices.getText(this, "table") + " [" + numTables + "/" + numTables + "]", null).getWidth();
        jLabel1.setText(PluginServices.getText(this, "converting_database"));
        jButton1.setName("cancel");
        jButton1.setText(PluginServices.getText(this, "cancel_button"));
        jLabel2.setText(PluginServices.getText(this, "table") + " [0/" + numTables + "]");
        jLabel2.setMinimumSize(new Dimension(jLabel2Width, jLabel2.getMinimumSize().height));
        jLabel2.setPreferredSize(new Dimension(jLabel2Width, jLabel2.getPreferredSize().height));
        jLabel2.setMaximumSize(new Dimension(jLabel2Width, jLabel2.getMaximumSize().height));
        jProgressBar1.setMinimumSize(new Dimension(WINDOW_WIDTH - jLabel2Width - 25, jProgressBar1.getMinimumSize().height));
        jProgressBar1.setPreferredSize(new Dimension(WINDOW_WIDTH - jLabel2Width - 25, jProgressBar1.getPreferredSize().height));
        jProgressBar1.setMaximumSize(new Dimension(WINDOW_WIDTH - jLabel2Width - 25, jProgressBar1.getMaximumSize().height));
    }

    /**
	 * Adds the listener for the jButton.
	 * @author Stefano Orlando
	 */
    private void initActionListeners() {
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
    }

    /**
	 * Starts the conversion of the Access database to the HSQLDB database launching
	 * a new SwingWorker thread.
	 * @author Stefano Orlando
	 */
    public void start() {
        mdbConverterTask = new MDBConverterTask();
        mdbConverterTask.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    int value = (Integer) evt.getNewValue();
                    jProgressBar1.setValue(value);
                }
                if ("label1Text".equals(evt.getPropertyName())) {
                    jLabel1.setText((String) evt.getNewValue());
                }
                if ("label2Text".equals(evt.getPropertyName())) {
                    jLabel2.setText((String) evt.getNewValue());
                }
                if ("windowTitle".equals(evt.getPropertyName())) {
                    m_viewInfo.setTitle((String) evt.getNewValue());
                }
                if ("button1Text".equals(evt.getPropertyName())) {
                    jButton1.setText((String) evt.getNewValue());
                }
            }
        });
        mdbConverterTask.execute();
    }

    /**
	 * Retrieves the result of the SwingWorker thread that has converted
	 * the Access database to the HSQLDB database.
	 * @return <code>true</code> if the conversion is successful,
	 * <code>false</code> otherwise
	 * @author Stefano Orlando
	 */
    public Boolean getResult() {
        if (mdbConverterTask == null) {
            return false;
        }
        Boolean result = mdbConverterTask.getResult();
        mdbConverterTask = null;
        return result;
    }

    /**
	 * Processes the action on the cancel button (jButton1).
	 * @param e - event
	 * @author Stefano Orlando
	 */
    private void jButton1ActionPerformed(ActionEvent e) {
        if (jButton1.getName().equalsIgnoreCase("cancel")) mdbConverterTask.cancel(true);
        PluginServices.getMDIManager().closeWindow(this);
        return;
    }

    /**
	 * Retrieves the informations on the window.
	 * @author Stefano Orlando
	 */
    public WindowInfo getWindowInfo() {
        m_viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
        m_viewInfo.setWidth(WINDOW_WIDTH);
        m_viewInfo.setHeight(WINDOW_HEIGHT);
        m_viewInfo.setTitle(PluginServices.getText(this, PluginServices.getText(this, "converting")));
        return m_viewInfo;
    }

    public Object getWindowProfile() {
        return null;
    }
}
