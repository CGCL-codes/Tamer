package net.cattaka.rdbassistant.gui.sql;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.StringWriter;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import net.cattaka.rdbassistant.RdbaMessageConstants;
import net.cattaka.rdbassistant.bean.RdbaSingletonBundle;
import net.cattaka.rdbassistant.config.RdbaConfig;
import net.cattaka.rdbassistant.core.RdbaConnection;
import net.cattaka.rdbassistant.core.SqlEditorSelection;
import net.cattaka.rdbassistant.gui.RdbaGuiInterface;
import net.cattaka.rdbassistant.gui.RdbaMessage;
import net.cattaka.rdbassistant.gui.RdbaTextInterface;
import net.cattaka.rdbassistant.gui.table.JPopupMenuForColumnList;
import net.cattaka.rdbassistant.gui.table.JTableForDisplay;
import net.cattaka.rdbassistant.gui.table.ResultSetTableModel;
import net.cattaka.rdbassistant.jspf.core.JspfBase;
import net.cattaka.rdbassistant.jspf.core.RdbaJspfBundle;
import net.cattaka.rdbassistant.jspf.core.JspfException;
import net.cattaka.rdbassistant.jspf.core.JspfMessageException;
import net.cattaka.rdbassistant.jspf.core.JspfNotFoundException;
import net.cattaka.rdbassistant.jspf.core.JspfSelectionInfo;
import net.cattaka.rdbassistant.jspf.core.bean.DatabaseInfo;
import net.cattaka.rdbassistant.util.ConnectionWrapper;
import net.cattaka.rdbassistant.util.RdbaGuiUtil;
import net.cattaka.swing.StdScrollPane;
import net.cattaka.swing.event.MouseEventUtil;
import net.cattaka.swing.util.ButtonsBundle;
import net.cattaka.util.MessageBundle;
import net.cattaka.util.PrintWriterEx;
import net.cattaka.util.ExceptionHandler;

public class RdbaSqlSelectPanel extends JPanel implements RdbaGuiInterface {

    private static final long serialVersionUID = 1L;

    private RdbaGuiInterface parentComponent;

    private JSplitPane splitPane;

    private JComboBox databaseComboBox;

    private JComboBox objectTypeComboBox;

    private JTableForTableList tableList;

    private JTabbedPane tableDetailPanel;

    private RdbaConnection rdbaConnection;

    private SqlEditorSelection sqlEditorSelection;

    private ResultSetTableModel currentPropertyTable;

    private String currentDatabase;

    private String currentObjectType;

    private String currentTable;

    private RdbaSqlEditorTabbedPanel rdbaSqlEditorPanel;

    class JTableForTableList extends JTableForDisplay {

        private static final long serialVersionUID = 1L;

        private String schemaName;

        public JTableForTableList(JPopupMenuForColumnList popupMenu) {
            super(popupMenu);
        }

        public void valueChanged(ListSelectionEvent e) {
            super.valueChanged(e);
            Object value = tableList.getValueAt(tableList.getSelectedRow(), 0);
            if (value != null) {
                if (currentTable != value.toString()) {
                    currentTable = value.toString();
                    updateTableDetail();
                }
            }
            this.setRdbaTextInterface(new RdbaTextAdapter());
        }

        @Override
        public String getFilteredString(int rowIndex, int columnIndex) {
            if (schemaName == null) {
                return super.getFilteredString(rowIndex, columnIndex);
            } else {
                return schemaName + "." + super.getFilteredString(rowIndex, columnIndex);
            }
        }

        public String getSchemaName() {
            return schemaName;
        }

        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }
    }

    class RdbaTextAdapter implements RdbaTextInterface {

        public void appendString(String str) {
            if (rdbaSqlEditorPanel != null) {
                rdbaSqlEditorPanel.appendString(str);
            }
        }
    }

    class ActionListenerImpl implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("refleshTableList")) {
                sendRdbaMessage(new RdbaMessage(RdbaMessageConstants.RDBASQLEDITORPANEL_REFLESH_ASSIST_PANEL, null, RdbaSqlSelectPanel.this, null));
            }
        }
    }

    class ActionListenerForDefaultJspf implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JspfBase target = null;
            {
                RdbaJspfBundle jspfBundle = getRdbaSingletonBundle().getDefaultJspfBundle();
                if (jspfBundle != null) {
                    JspfBase[] jspfBases = jspfBundle.getJspfList();
                    for (JspfBase jspfBase : jspfBases) {
                        if (e.getActionCommand().equals(jspfBase.getName())) {
                            target = jspfBase;
                            break;
                        }
                    }
                }
            }
            if (target != null) {
                try {
                    execJspf(target);
                } catch (SQLException exc) {
                    JOptionPane.showMessageDialog(RdbaGuiUtil.getParentFrame(RdbaSqlSelectPanel.this), exc.getMessage());
                    ExceptionHandler.error(exc);
                } catch (JspfException exc) {
                    JOptionPane.showMessageDialog(RdbaGuiUtil.getParentFrame(RdbaSqlSelectPanel.this), exc.getMessage());
                    ExceptionHandler.error(exc);
                }
            }
        }
    }

    class TableDetailPanel extends JPanel {

        private static final long serialVersionUID = 1L;

        private JTableForDisplay propertyTable;

        private StdScrollPane propertyTableScrollPane;

        public TableDetailPanel() {
        }

        public void createTable(ResultSetTableModel rstm) {
            Font fontForTable = getRdbaConfig().getFontForTable();
            this.propertyTable = new JTableForDisplay(createPopupForTableDetail());
            propertyTable.setFont(getRdbaConfig().getFontForTable());
            propertyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            propertyTable.setResultSetTableModel(rstm);
            propertyTable.setRdbaTextInterface(new RdbaTextAdapter());
            propertyTable.setFont(fontForTable);
            propertyTable.setColumnSelectionAllowed(true);
            this.propertyTableScrollPane = new StdScrollPane(propertyTable);
            this.propertyTableScrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            this.setLayout(new GridLayout());
            this.add(this.propertyTableScrollPane);
        }

        public JTableForDisplay getPropertyTable() {
            return propertyTable;
        }
    }

    public RdbaSqlSelectPanel(RdbaGuiInterface parentComponent) {
        this.parentComponent = parentComponent;
        makeLayout();
    }

    private void makeLayout() {
        JPanel tableListPanel;
        JPanel propertyTablePanel;
        this.databaseComboBox = new JComboBox();
        this.databaseComboBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        this.objectTypeComboBox = new JComboBox();
        this.objectTypeComboBox.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        ActionListenerImpl al = new ActionListenerImpl();
        {
            tableListPanel = new JPanel();
            JPopupMenuForColumnList tableListPopupMenu = new JPopupMenuForColumnList(false);
            JMenuItem refleshItem = new JMenuItem();
            ButtonsBundle.applyMenuDifinition(refleshItem, "reflesh");
            refleshItem.addActionListener(al);
            refleshItem.setActionCommand("refleshTableList");
            tableListPopupMenu.createMenuItems();
            tableListPopupMenu.add(new JSeparator());
            tableListPopupMenu.add(refleshItem);
            StdScrollPane tableListScrollPane;
            this.tableList = new JTableForTableList(tableListPopupMenu);
            this.tableList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.tableList.setColumnSelectionAllowed(true);
            tableListScrollPane = new StdScrollPane(this.tableList);
            tableListScrollPane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            MouseEventUtil.createMouseEventPipe(tableListScrollPane, tableList);
            tableListPanel.setLayout(new BoxLayout(tableListPanel, BoxLayout.PAGE_AXIS));
            tableListPanel.add(new JLabel(MessageBundle.getMessage("table")));
            tableListPanel.add(tableListScrollPane);
        }
        {
            tableDetailPanel = new JTabbedPane();
            tableDetailPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            propertyTablePanel = new JPanel();
            propertyTablePanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            propertyTablePanel.setLayout(new BoxLayout(propertyTablePanel, BoxLayout.PAGE_AXIS));
            propertyTablePanel.add(new JLabel(MessageBundle.getMessage("table_detail")));
            propertyTablePanel.add(tableDetailPanel);
        }
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableListPanel, propertyTablePanel);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(new JLabel(MessageBundle.getMessage("database")));
        this.add(databaseComboBox);
        this.add(new JLabel(MessageBundle.getMessage("object_type")));
        this.add(objectTypeComboBox);
        this.add(splitPane);
        {
            this.databaseComboBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (databaseComboBox.getSelectedItem() instanceof String) {
                        if (databaseComboBox.getSelectedItem() != e.getItem()) {
                            currentDatabase = (String) databaseComboBox.getSelectedItem();
                            updateTableList();
                        }
                    }
                }
            });
            this.objectTypeComboBox.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent e) {
                    if (objectTypeComboBox.getSelectedItem() instanceof String) {
                        if (objectTypeComboBox.getSelectedItem() != e.getItem()) {
                            currentObjectType = (String) objectTypeComboBox.getSelectedItem();
                            updateTableList();
                        }
                    }
                }
            });
        }
    }

    private void updateTableList() {
        if (currentDatabase != null && currentObjectType != null) {
            if (currentDatabase.equals(sqlEditorSelection.getDefaultDatabase())) {
                tableList.setSchemaName(null);
            } else {
                tableList.setSchemaName(currentDatabase);
            }
            currentPropertyTable = this.sqlEditorSelection.getTableList(currentDatabase, currentObjectType);
            this.tableList.setResultSetTableModel(currentPropertyTable);
        }
    }

    private void updateTableDetail() {
        if (currentDatabase != null && currentObjectType != null && currentTable != null) {
            List<ResultSetTableModel> lpt = this.sqlEditorSelection.getTableProperties(currentDatabase, currentObjectType, currentTable);
            this.tableDetailPanel.removeAll();
            for (ResultSetTableModel rstm : lpt) {
                TableDetailPanel tableDetailPanel = new TableDetailPanel();
                tableDetailPanel.createTable(rstm);
                this.tableDetailPanel.add(rstm.getTableName(), tableDetailPanel);
            }
        }
    }

    private JPopupMenuForColumnList createPopupForTableDetail() {
        JPopupMenuForColumnList popup = new JPopupMenuForColumnList(true);
        RdbaJspfBundle jspfDefaultBundle = getRdbaSingletonBundle().getDefaultJspfBundle();
        if (jspfDefaultBundle != null) {
            ActionListenerForDefaultJspf al = new ActionListenerForDefaultJspf();
            popup.addSeparator();
            JspfBase[] jspfBases = jspfDefaultBundle.getJspfList();
            for (JspfBase jspfBase : jspfBases) {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setText(jspfBase.getDisplayName());
                menuItem.setActionCommand(jspfBase.getName());
                menuItem.addActionListener(al);
                popup.add(menuItem);
            }
        }
        RdbaJspfBundle jspfUserBundle = null;
        if (jspfUserBundle != null) {
            JMenu templateMenu = new JMenu(MessageBundle.getMessage("template"));
            ActionListenerForDefaultJspf al = new ActionListenerForDefaultJspf();
            popup.addSeparator();
            JspfBase[] jspfBases = jspfUserBundle.getJspfList();
            for (JspfBase jspfBase : jspfBases) {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setText(jspfBase.getDisplayName());
                menuItem.setActionCommand(jspfBase.getName());
                menuItem.addActionListener(al);
                templateMenu.add(menuItem);
            }
            popup.add(templateMenu);
        }
        return popup;
    }

    /**
	 * JSPFを実行する。
	 * @param jspfBase
	 * @throws SQLException
	 */
    public void execJspf(JspfBase jspfBase) throws SQLException, JspfNotFoundException, JspfMessageException {
        ConnectionWrapper conn = this.rdbaConnection.getConnection();
        DatabaseMetaData dmd = conn.getMetaData();
        DatabaseInfo databaseInfo = new DatabaseInfo(dmd, this.databaseComboBox.getSelectedItem().toString());
        jspfBase.setDatabaseInfo(databaseInfo);
        jspfBase.setSelectionInfo(createJspfSelectionInfo());
        StringWriter sw = new StringWriter();
        PrintWriterEx out = new PrintWriterEx(sw);
        jspfBase.runJspf(out);
        out.flush();
        this.rdbaSqlEditorPanel.appendString(sw.toString());
    }

    /**
	 * {@link #execJspf(JspfBase)}
	 * @return
	 */
    public JspfSelectionInfo createJspfSelectionInfo() {
        JspfSelectionInfo jsi = new JspfSelectionInfo();
        String[] databases = new String[] { databaseComboBox.getSelectedItem().toString() };
        String[] types = new String[] { objectTypeComboBox.getSelectedItem().toString() };
        String[] tables;
        {
            int[] selectedRows = tableList.getSelectedRows();
            tables = new String[selectedRows.length];
            for (int i = 0; i < selectedRows.length; i++) {
                Object obj = tableList.getValueAt(selectedRows[i], 0).toString();
                tables[i] = (obj != null) ? obj.toString() : "";
            }
        }
        String[] columns = new String[0];
        {
            JTableForDisplay table = getCurrentTableDetail();
            if (table == null) {
                columns = new String[0];
            } else {
                int[] selectedRows = table.getSelectedRows();
                columns = new String[selectedRows.length];
                for (int i = 0; i < selectedRows.length; i++) {
                    Object obj = table.getValueAt(selectedRows[i], 0).toString();
                    columns[i] = (obj != null) ? obj.toString() : "";
                }
            }
        }
        jsi.setDatabases(databases);
        jsi.setTypes(types);
        jsi.setTables(tables);
        jsi.setColumns(columns);
        return jsi;
    }

    /**
	 * 現在選択中のTableDetailテーブルを取得する。
	 * (タブで分離されているから全面の物を取得する。)
	 * @return
	 */
    private JTableForDisplay getCurrentTableDetail() {
        JTableForDisplay result = null;
        Component comp = this.tableDetailPanel.getSelectedComponent();
        if (comp instanceof TableDetailPanel) {
            result = ((TableDetailPanel) comp).getPropertyTable();
        }
        return result;
    }

    public void doGuiLayout() {
        this.splitPane.setDividerLocation(0.5);
        if (currentPropertyTable != null) {
            this.tableList.setResultSetTableModel(currentPropertyTable);
        }
    }

    public RdbaConfig getRdbaConfig() {
        return this.parentComponent.getRdbaConfig();
    }

    public void reloadRdbaConfig() {
        this.tableList.setFont(getRdbaConfig().getFontForTable());
    }

    public void sendRdbaMessage(RdbaMessage rdbaMessage) {
        this.parentComponent.sendRdbaMessage(rdbaMessage);
    }

    public void relayRdbaMessage(RdbaMessage rdbaMessage) {
        if (rdbaMessage.getMessage().equals(RdbaMessageConstants.RDBASQLEDITORPANEL_REFLESH_ASSIST_PANEL)) {
            this.setSqlEditorSelection(this.rdbaConnection.getSqlEditorSelection(), this.rdbaConnection.getDefaultDatabase());
        }
    }

    public RdbaSingletonBundle getRdbaSingletonBundle() {
        return this.parentComponent.getRdbaSingletonBundle();
    }

    public void setRdbaConnection(RdbaConnection rdbaConnection) {
        this.rdbaConnection = rdbaConnection;
        this.setSqlEditorSelection(rdbaConnection.getSqlEditorSelection(), rdbaConnection.getDefaultDatabase());
    }

    private void setSqlEditorSelection(SqlEditorSelection sqlEditorSelection, String defaultDatabase) {
        this.sqlEditorSelection = sqlEditorSelection;
        this.databaseComboBox.setModel(new DefaultComboBoxModel(sqlEditorSelection.getDatabaseList().toArray()));
        this.objectTypeComboBox.setModel(new DefaultComboBoxModel(sqlEditorSelection.getObjectTypeList().toArray()));
        this.databaseComboBox.setSelectedItem(defaultDatabase);
        currentDatabase = (String) (this.databaseComboBox.getSelectedItem());
        currentObjectType = (String) (this.objectTypeComboBox.getSelectedItem());
        updateTableList();
    }

    public void setRdbaSqlEditorTabbedPanel(RdbaSqlEditorTabbedPanel rdbaSqlEditorPanel) {
        this.rdbaSqlEditorPanel = rdbaSqlEditorPanel;
    }
}
