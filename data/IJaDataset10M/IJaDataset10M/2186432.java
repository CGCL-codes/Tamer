package org.digitall.projects.apps.dbadmin_091.interfases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.digitall.common.components.combos.JCombo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicCheckList;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicList;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CancelDataButton;
import org.digitall.lib.components.buttons.ReloadButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.apps.dbadmin_091.SystemConfiguration;

public class TableManager extends BasicPanel {

    private SaveDataButton saveGroupsByTable = new SaveDataButton();

    private AssignButton excludeTable = new AssignButton();

    private ReloadButton resetExcludedTables = new ReloadButton();

    private CancelDataButton cancelGroupsByTable = new CancelDataButton();

    private BasicScrollPane spGroupsByTable = new BasicScrollPane();

    private BasicScrollPane spTablesByScheme = new BasicScrollPane();

    private BasicList tablesByScheme = new BasicList();

    private BasicCheckList groupsByTable = new BasicCheckList(new DefaultListModel());

    private BasicLabel lblTablesRed = new BasicLabel();

    private BasicLabel lblTablesYellow = new BasicLabel();

    private BasicLabel lblTablesGreen = new BasicLabel();

    private BasicLabel lblScheme = new BasicLabel();

    private BasicLabel lblTables = new BasicLabel();

    private BasicLabel lblGroups = new BasicLabel();

    private JCombo cbTablesSchemes = new JCombo();

    private Vector<DBRole> groupVector = new Vector<DBRole>();

    private DBAdminPanel dbAdminPanel;

    public TableManager(DBAdminPanel _dbAdminPanel) {
        dbAdminPanel = _dbAdminPanel;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(660, 486));
        spGroupsByTable.setBounds(new Rectangle(295, 75, 253, 345));
        spGroupsByTable.setSize(new Dimension(253, 345));
        spTablesByScheme.setBounds(new Rectangle(20, 75, 253, 345));
        spTablesByScheme.setSize(new Dimension(253, 345));
        lblTablesRed.setText("ADMIN");
        lblTablesRed.setBounds(new Rectangle(20, 435, 70, 25));
        lblTablesRed.setBackground(Color.red);
        lblTablesRed.setOpaque(true);
        lblTablesRed.setHorizontalAlignment(SwingConstants.CENTER);
        lblTablesRed.setForeground(Color.black);
        lblTablesYellow.setText("USER");
        lblTablesYellow.setBounds(new Rectangle(100, 435, 70, 25));
        lblTablesYellow.setOpaque(true);
        lblTablesYellow.setBackground(Color.yellow);
        lblTablesYellow.setHorizontalAlignment(SwingConstants.CENTER);
        lblTablesYellow.setForeground(Color.black);
        lblTablesGreen.setText("QUERY");
        lblTablesGreen.setBounds(new Rectangle(180, 435, 70, 25));
        lblTablesGreen.setOpaque(true);
        lblTablesGreen.setBackground(Color.green);
        lblTablesGreen.setHorizontalAlignment(SwingConstants.CENTER);
        lblTablesGreen.setForeground(Color.black);
        groupsByTable.setCellRenderer(new BasicCellRenderer());
        tablesByScheme.setBounds(new Rectangle(0, 0, 251, 338));
        tablesByScheme.setSize(new Dimension(251, 339));
        tablesByScheme.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (tablesByScheme.getSelectedIndex() > -1) {
                        try {
                            loadGroupsByTable(tablesByScheme.getSelectedValue().toString());
                        } catch (NullPointerException x) {
                        }
                    }
                }
            }
        });
        groupsByTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                if (groupsByTable.getSelectedValues().length > 0 && me.getButton() == MouseEvent.BUTTON3) {
                    for (int i = 0; i < groupsByTable.getSelectedValues().length; i++) {
                        DBRole _group = (DBRole) groupsByTable.getSelectedValues()[i];
                        _group.incPrivilege();
                        _group.setSelected(true);
                    }
                    spGroupsByTable.repaint();
                }
            }
        });
        saveGroupsByTable.setBounds(new Rectangle(570, 205, 70, 60));
        saveGroupsByTable.setSize(new Dimension(70, 60));
        excludeTable.setBounds(new Rectangle(555, 75, 105, 60));
        resetExcludedTables.setBounds(new Rectangle(555, 140, 105, 60));
        resetExcludedTables.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                resetExcludedTables_actionPerformed(e);
            }
        });
        excludeTable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                excludeTable_actionPerformed(e);
            }
        });
        saveGroupsByTable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveGroupsByTable_actionPerformed(e);
            }
        });
        saveGroupsByTable.setText("Guardar\ncambios");
        cancelGroupsByTable.setBounds(new Rectangle(555, 270, 105, 60));
        cancelGroupsByTable.setText("Cancelar \ncambios");
        excludeTable.setText("Excluir\n tabla");
        excludeTable.setToolTipText("Excluir Tabla");
        resetExcludedTables.setText("Limpiar listado\n de Exclusiones");
        resetExcludedTables.setToolTipText("Limpiar lista de Tablas excluidas");
        lblScheme.setText("Esquema");
        lblScheme.setBounds(new Rectangle(20, 5, 75, 15));
        lblTables.setText("Tablas");
        lblTables.setBounds(new Rectangle(20, 55, 75, 15));
        lblGroups.setText("Grupos");
        lblGroups.setBounds(new Rectangle(295, 55, 75, 14));
        this.add(lblGroups, null);
        this.add(lblTables, null);
        this.add(lblScheme, null);
        spGroupsByTable.getViewport().add(groupsByTable);
        add(spGroupsByTable, null);
        add(lblTablesGreen, null);
        add(lblTablesYellow, null);
        add(lblTablesRed, null);
        add(cbTablesSchemes, null);
        spTablesByScheme.getViewport().add(tablesByScheme);
        add(spTablesByScheme, null);
        add(saveGroupsByTable, null);
        add(excludeTable, null);
        add(resetExcludedTables, null);
        add(cancelGroupsByTable, null);
        cbTablesSchemes.setBounds(new Rectangle(20, 25, 200, 20));
        cbTablesSchemes.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (cbTablesSchemes.getSelectedIndex() > -1) {
                        loadTablesByScheme(cbTablesSchemes.getSelectedItem().toString());
                    }
                }
            }
        });
    }

    public void boot() {
        cbTablesSchemes.loadJCombo("SELECT oid, nspname, 0 FROM pg_namespace WHERE nspname NOT LIKE 'pg_%' AND nspname != 'information_schema' ORDER BY nspname");
        cbTablesSchemes.setSelectedIndex(0);
        loadGroupsList();
        if (cbTablesSchemes.getModel().getSize() > 0) {
            loadTablesByScheme(cbTablesSchemes.getSelectedItem().toString());
        }
    }

    /**2009-12-30(moraless)*/
    public void boot(ResultSet _schemes) {
        cbTablesSchemes.loadJCombo(_schemes);
        cbTablesSchemes.setSelectedIndex(0);
        loadGroupsList();
        if (cbTablesSchemes.getModel().getSize() > 0) {
            loadTablesByScheme(cbTablesSchemes.getSelectedItem().toString());
        }
    }

    public void setEnabledTab(boolean _enabled) {
        cbTablesSchemes.setEnabled(_enabled);
        tablesByScheme.setEnabled(_enabled);
        groupsByTable.setEnabled(_enabled);
        saveGroupsByTable.setEnabled(_enabled);
        cancelGroupsByTable.setEnabled(_enabled);
    }

    /**2009-12-29(moraless)*/
    private void loadGroupsByTable(String _table) {
        loadGroup();
        ResultSet result = LibSQL.exQuery("(SELECT array_to_string(relacl,',') AS acl FROM pg_class INNER JOIN pg_namespace esquema ON esquema.oid = pg_class .relnamespace WHERE pg_class.relkind = 'r' AND esquema.nspname = '" + cbTablesSchemes.getSelectedItem().toString() + "' AND relname = '" + _table + "')");
        try {
            if (result.next()) {
                String[] grupos = getGroupByTable(result.getString("acl"));
                for (int j = 0; j < grupos.length; j++) {
                    if (grupos[j].startsWith("=r/")) {
                        for (int k = 0; k < groupVector.size(); k++) {
                            DBRole role = (DBRole) ((DefaultListModel) groupsByTable.getModel()).getElementAt(k);
                            if (!((role.getPrivilege() == SystemConfiguration.ADMIN_PRIV) || (role.getPrivilege() == SystemConfiguration.USER_PRIV))) {
                                role.setPrivilege(SystemConfiguration.QUERY_PRIV);
                            }
                        }
                    } else {
                        if (grupos[j].indexOf("=r/") > -1) {
                            int i = 0;
                            while ((i < groupVector.size()) && (!grupos[j].substring(0, grupos[j].indexOf("=")).equals(groupVector.elementAt(i).toString()))) {
                                i++;
                            }
                            if (i < groupVector.size()) {
                                DBRole role = (DBRole) ((DefaultListModel) groupsByTable.getModel()).getElementAt(i);
                                role.setPrivilege(SystemConfiguration.QUERY_PRIV);
                            }
                        }
                    }
                    int i = 0;
                    while ((i < groupVector.size()) && (!grupos[j].substring(0, grupos[j].indexOf("=")).equals(groupVector.elementAt(i).toString()))) {
                        i++;
                    }
                    if (i < groupVector.size()) {
                        DBRole role = (DBRole) ((DefaultListModel) groupsByTable.getModel()).getElementAt(i);
                        if (grupos[j].indexOf("=ar/") > -1) {
                            role.setPrivilege(SystemConfiguration.USER_PRIV);
                        } else {
                            if (grupos[j].indexOf("=arwd") > -1) {
                                role.setPrivilege(SystemConfiguration.ADMIN_PRIV);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String[] getGroupByTable(String _acl) {
        String[] com = _acl.split(",");
        return com;
    }

    private void loadTablesByScheme(String _scheme) {
        String query = "SELECT relname as name FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid AND ns.nspname = '" + _scheme + "' AND relkind = 'r' ORDER BY relname";
        ResultSet rs = LibSQL.exQuery(query);
        Vector _tablesByScheme = new Vector();
        try {
            while (rs.next()) {
                _tablesByScheme.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tablesByScheme.setListData(_tablesByScheme);
        tablesByScheme.setSelectedIndex(0);
    }

    private void saveGroupsByTable_actionPerformed(ActionEvent e) {
        String query = "";
        for (int i = 0; i < groupsByTable.getModel().getSize(); i++) {
            DBRole _group = (DBRole) groupsByTable.getModel().getElementAt(i);
            if (_group.isSelected()) {
                for (int j = 0; j < tablesByScheme.getSelectedValues().length; j++) {
                    String _table = cbTablesSchemes.getSelectedItem().toString() + "." + tablesByScheme.getSelectedValues()[j].toString();
                    String priv = "'" + _group.getText() + "','" + _table + "',";
                    String queryPriv = priv + "'SELECT'";
                    String adminPriv = priv + "'UPDATE'";
                    String userPriv = priv + "'INSERT'";
                    if (((LibSQL.getBoolean("has_table_privilege", queryPriv) || LibSQL.getBoolean("has_table_privilege", adminPriv) || LibSQL.getBoolean("has_table_privilege", userPriv)))) {
                        query += "REVOKE ALL PRIVILEGES ON " + _table + " FROM GROUP " + _group.getText() + ";\n";
                    }
                    if (_group.getPrivilege() == SystemConfiguration.QUERY_PRIV) {
                        query += "GRANT SELECT ON " + _table + " TO GROUP " + _group.getText() + ";\n";
                    } else if (_group.getPrivilege() == SystemConfiguration.USER_PRIV) {
                        query += "GRANT SELECT, INSERT ON " + _table + " TO GROUP " + _group.getText() + ";\n";
                    } else if (_group.getPrivilege() == SystemConfiguration.ADMIN_PRIV) {
                        query += "GRANT ALL PRIVILEGES ON " + _table + " TO GROUP " + _group.getText() + ";\n";
                    }
                }
                _group.setSelected(false);
                groupsByTable.updateUI();
            }
        }
        if (LibSQL.exActualizar('a', query)) {
        } else {
            Advisor.messageBox("Error al intentar asignar los permisos", "Error");
        }
    }

    /**2009-12-30(moraless)*/
    public void setGroupVector(Vector<DBRole> _groupVector) {
    }

    private void loadGroupsList() {
        String query = "SELECT distinct grosysid as gid, groname as name FROM pg_group order by groname";
        ResultSet rs = LibSQL.exQuery(query);
        Vector _groups = new Vector();
        try {
            while (rs.next()) {
                _groups.add(new DBRole(rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        groupVector = _groups;
    }

    private void excludeTable_actionPerformed(ActionEvent e) {
        for (int j = 0; j < tablesByScheme.getSelectedValues().length; j++) {
            SystemConfiguration.tablesExclusionList.add(cbTablesSchemes.getSelectedItem().toString() + "." + tablesByScheme.getSelectedValues()[j].toString());
        }
        System.out.println("listado de tablas excluidas");
        System.out.println(SystemConfiguration.tablesExclusionList);
    }

    private void resetExcludedTables_actionPerformed(ActionEvent e) {
        SystemConfiguration.tablesExclusionList.removeAllElements();
    }

    private void loadGroup() {
        ((DefaultListModel) groupsByTable.getModel()).removeAllElements();
        for (int i = 0; i < groupVector.size(); i++) {
            DBRole _group = groupVector.elementAt(i);
            _group.setPrivilege(SystemConfiguration.ZERO_PRIV);
            ((DefaultListModel) groupsByTable.getModel()).addElement(_group);
        }
    }
}
