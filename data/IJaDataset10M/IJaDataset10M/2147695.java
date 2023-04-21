package crm.client.ui.panes.db;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import crm.client.ui.panes.LogView;
import crm.server.db.DBConnector;

/**
 * @author Maxim Tolstyh
 * @version 1.0, 24/08/2010
 */
public class DBAdminPane extends JPanel implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * list_db is a databases list which exists in server
	 */
    private ArrayList<String> list_db;

    public ArrayList<String> list_table;

    public ArrayList<ArrayList<String>> list_fields;

    private DatabaseView db_view;

    private DBTableView tb_view;

    private LogView log_panel;

    private DBConnector dbc;

    private TableStructureView ts_view;

    private JSplitPane splpane;

    private JSplitPane leftsplit;

    public DBAdminPane() {
    }

    public DBAdminPane(DBConnector _dbc, boolean is_local) {
        dbc = _dbc;
        list_table = new ArrayList<String>();
        list_fields = new ArrayList<ArrayList<String>>();
        setLayout(new BorderLayout(0, 0));
        create_db_view(is_local);
        tb_view = new DBTableView(this);
        tb_view.setBorder(BorderFactory.createTitledBorder("DB Table view"));
        tb_view.desc_tbl.addActionListener(this);
        tb_view.show_tbl.addActionListener(this);
        ts_view = new TableStructureView();
        ts_view.setBorder(BorderFactory.createTitledBorder("Table structure view"));
        splpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, db_view, tb_view);
        leftsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splpane, ts_view);
        leftsplit.setSize(WIDTH, 300);
        log_panel = new LogView();
        JSplitPane vsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        vsplit.add(leftsplit);
        vsplit.setBottomComponent(log_panel);
        vsplit.setAutoscrolls(true);
        add(vsplit, BorderLayout.CENTER);
        tb_view.btn_add.addActionListener(this);
        tb_view.btn_del.addActionListener(this);
        if (is_local) setEnableAdminPane(false);
    }

    public void setEnableAdminPane(boolean arg0) {
        db_view.setAllEnable(arg0);
        tb_view.setAllEnable(arg0);
        ts_view.setAllEnable(arg0);
        log_panel.setAllEnable(arg0);
    }

    public void create_db_view(boolean is_local) {
        list_db = new ArrayList<String>();
        if (!is_local) {
            dbc.executeSQL("SHOW DATABASES");
            try {
                while (dbc.res.next()) {
                    list_db.add(dbc.res.getString("Database"));
                }
                System.out.println(list_db.toString());
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        db_view = new DatabaseView(this, list_db.toArray());
        db_view.setBorder(BorderFactory.createTitledBorder("DB view"));
        db_view.btn_add.addActionListener(this);
        db_view.btn_del.addActionListener(this);
        db_view.use_db.addActionListener(this);
    }

    public void clear_table() {
        DefaultTableModel model = (DefaultTableModel) ts_view.table.getModel();
        int i = 0, count = model.getRowCount();
        while (i != count) {
            model.removeRow(0);
            i++;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "") {
        }
        if (e.getActionCommand() == "adddb") {
            log_panel.addString("Creating new database...\n");
        }
        if (e.getActionCommand() == "deldb") {
            log_panel.addString("Deleting database...\n");
        }
        if (e.getActionCommand() == "usedb") {
            log_panel.addString("Using [" + db_view.db_list.getSelectedValue().toString() + "] database...\n");
            dbc.executeSQL("USE " + db_view.db_list.getSelectedValue().toString());
            dbc.executeSQL("SHOW TABLES");
            list_table.clear();
            try {
                while (dbc.res.next()) {
                    list_table.add(dbc.res.getString(1));
                }
                if (!list_table.isEmpty()) {
                    tb_view.list.setListData(list_table.toArray());
                }
                dbc.releaseQ();
            } catch (SQLException ex) {
                log_panel.addString(ex.toString());
            }
        }
        if (e.getActionCommand() == "desctbl") {
            log_panel.addString("Opening [" + tb_view.list.getSelectedValue().toString() + "] table...\n");
            dbc.releaseQ();
            clear_table();
            dbc.executeSQL("DESC " + tb_view.list.getSelectedValue().toString());
            try {
                while (dbc.res.next()) {
                    ArrayList<String> array = new ArrayList<String>();
                    array.add(dbc.res.getString(1));
                    array.add(dbc.res.getString(2));
                    array.add(dbc.res.getString(3));
                    array.add(dbc.res.getString(4));
                    array.add(dbc.res.getString(5));
                    array.add(dbc.res.getString(6));
                    list_fields.add(array);
                }
                if (!list_fields.isEmpty()) {
                    DefaultTableModel model = (DefaultTableModel) ts_view.table.getModel();
                    for (int k = 0; k < list_fields.size(); k++) model.addRow(list_fields.get(k).toArray());
                    list_fields.clear();
                    dbc.releaseQ();
                }
            } catch (SQLException ex) {
                log_panel.addString(ex.toString());
            }
        }
        if (e.getActionCommand() == "showtbl") {
            log_panel.addString("Showing [" + tb_view.list.getSelectedValue().toString() + "] table...\n");
            dbc.releaseQ();
            JFrame tab_frame = new JFrame(tb_view.list.getSelectedValue().toString());
            JTable tab = new JTable();
            JScrollPane scrp;
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(BorderFactory.createTitledBorder("Data in table"));
            scrp = new JScrollPane(tab, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            p.add(tab.getTableHeader(), BorderLayout.PAGE_START);
            p.add(scrp, BorderLayout.CENTER);
            tab_frame.add(p);
            tab.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            ArrayList<String> array = new ArrayList<String>();
            DefaultTableModel mod = (DefaultTableModel) tab.getModel();
            dbc.executeSQL("DESC " + tb_view.list.getSelectedValue().toString());
            int i;
            try {
                while (dbc.res.next()) {
                    array.add(dbc.res.getString(1));
                }
                for (int j = 0; j < array.size(); j++) mod.addColumn(array.get(j));
            } catch (SQLException ex) {
                log_panel.addString(ex.toString());
            }
            i = array.size();
            dbc.releaseQ();
            dbc.executeSQL("SELECT * FROM " + tb_view.list.getSelectedValue().toString());
            try {
                ArrayList<String> marray = new ArrayList<String>();
                while (dbc.res.next()) {
                    for (int j = 1; j <= i; j++) marray.add(dbc.res.getString(j));
                    mod.addRow(marray.toArray());
                }
            } catch (SQLException ex) {
                log_panel.addString(ex.toString());
            }
            tab_frame.setSize(500, 300);
            tab_frame.setLocation(200, 200);
            tab_frame.setVisible(true);
        }
    }
}
