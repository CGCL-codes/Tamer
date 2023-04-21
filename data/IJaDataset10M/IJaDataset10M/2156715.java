package com.nex.content.xtm;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

/**
 * This class based on a similar one in the
 * HypersonicSQL distribution
 * This class is not finished, but it works just fine as is
 */
public class HsqlManager extends JDialog {

    JMenuBar jMenuBar1 = new JMenuBar();

    JMenu fileMenu = new JMenu();

    JMenu viewMenu = new JMenu();

    JMenu commandMenu = new JMenu();

    JMenu optionMenu = new JMenu();

    BorderLayout borderLayout1 = new BorderLayout();

    JSplitPane jSplitPane1 = new JSplitPane();

    JPanel jPanel1 = new JPanel();

    JPanel jPanel2 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();

    JScrollPane jScrollPane1 = new JScrollPane();

    JTree propTree = new JTree();

    BorderLayout borderLayout3 = new BorderLayout();

    JPanel jPanel3 = new JPanel();

    BorderLayout borderLayout4 = new BorderLayout();

    JScrollPane jScrollPane2 = new JScrollPane();

    JTextArea commandField = new JTextArea();

    JButton execButton = new JButton();

    TitledBorder titledBorder1;

    Border border1;

    TitledBorder titledBorder2;

    JPanel jPanel4 = new JPanel();

    TitledBorder titledBorder3;

    Border border2;

    TitledBorder titledBorder4;

    JScrollPane jScrollPane3 = new JScrollPane();

    JTable resultTable = new JTable();

    DefaultTableModel resultModel = new DefaultTableModel();

    BorderLayout borderLayout5 = new BorderLayout();

    TitledBorder titledBorder5;

    Border border3;

    TitledBorder titledBorder6;

    Connection cConn;

    DatabaseMetaData dMeta;

    Statement sStatement;

    long lTime;

    public HsqlManager(Connection con) {
        try {
            jbInit();
            connect(con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(600, 480);
        titledBorder1 = new TitledBorder("");
        border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140));
        titledBorder2 = new TitledBorder(border1, "Command");
        titledBorder3 = new TitledBorder("");
        border2 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140));
        titledBorder4 = new TitledBorder(border2, "Result");
        titledBorder5 = new TitledBorder("");
        border3 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140));
        titledBorder6 = new TitledBorder(border3, "Properties");
        fileMenu.setText("File");
        viewMenu.setText("View");
        commandMenu.setText("Command");
        optionMenu.setText("Options");
        this.getContentPane().setLayout(borderLayout1);
        jPanel1.setLayout(borderLayout2);
        jPanel2.setLayout(borderLayout3);
        jPanel3.setLayout(borderLayout4);
        execButton.setBorder(BorderFactory.createRaisedBevelBorder());
        execButton.setToolTipText("Run command");
        execButton.setText("Execute");
        execButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                execButton_actionPerformed(e);
            }
        });
        jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setBorder(BorderFactory.createRaisedBevelBorder());
        jPanel3.setBorder(titledBorder2);
        jPanel4.setBorder(titledBorder4);
        jPanel4.setLayout(borderLayout5);
        resultTable.setBorder(BorderFactory.createRaisedBevelBorder());
        propTree.setBorder(BorderFactory.createRaisedBevelBorder());
        jPanel1.setBorder(titledBorder6);
        this.setTitle("HSQL Manager");
        jMenuBar1.add(fileMenu);
        jMenuBar1.add(viewMenu);
        jMenuBar1.add(commandMenu);
        jMenuBar1.add(optionMenu);
        this.setJMenuBar(jMenuBar1);
        this.getContentPane().add(jSplitPane1, BorderLayout.CENTER);
        jSplitPane1.add(jPanel1, JSplitPane.LEFT);
        jPanel1.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(propTree, null);
        jSplitPane1.add(jPanel2, JSplitPane.RIGHT);
        jPanel2.add(jPanel3, BorderLayout.NORTH);
        jPanel3.add(jScrollPane2, BorderLayout.CENTER);
        jPanel3.add(execButton, BorderLayout.EAST);
        jPanel2.add(jPanel4, BorderLayout.CENTER);
        jPanel4.add(jScrollPane3, BorderLayout.CENTER);
        jScrollPane3.getViewport().add(resultTable, null);
        jScrollPane2.getViewport().add(commandField, null);
        resultTable.setModel(resultModel);
    }

    void connect(Connection c) {
        if (c == null) {
            return;
        }
        if (cConn != null) {
            try {
                cConn.close();
            } catch (SQLException e) {
            }
        }
        cConn = c;
        try {
            dMeta = cConn.getMetaData();
            sStatement = cConn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTree();
    }

    private void refreshTree() {
        DefaultMutableTreeNode root = null;
        DefaultMutableTreeNode props = new DefaultMutableTreeNode("Properties");
        try {
            int color_table = Color.yellow.getRGB();
            int color_column = Color.orange.getRGB();
            int color_index = Color.red.getRGB();
            System.out.println("Connect dMetaURL " + dMeta.getURL());
            root = new DefaultMutableTreeNode(dMeta.getURL());
            String usertables[] = { "TABLE" };
            ResultSet result = dMeta.getTables(null, null, null, usertables);
            Vector tables = new Vector();
            while (result.next()) {
                tables.addElement(result.getString(3));
            }
            result.close();
            for (int i = 0; i < tables.size(); i++) {
                String name = (String) tables.elementAt(i);
                String key = "tab-" + name + "-";
                root.add(new DefaultMutableTreeNode(key));
                ResultSet col = dMeta.getColumns(null, null, name, null);
                while (col.next()) {
                    String c = col.getString(4);
                    String k1 = key + "col-" + c + "-";
                    root.add(new DefaultMutableTreeNode(k1));
                    String type = col.getString(6);
                    root.add(new DefaultMutableTreeNode(k1 + "t " + "Type: " + type));
                    boolean nullable = col.getInt(11) != DatabaseMetaData.columnNoNulls;
                    root.add(new DefaultMutableTreeNode(k1 + "n " + "Nullable: " + nullable));
                }
                col.close();
                root.add(new DefaultMutableTreeNode(key));
                ResultSet ind = dMeta.getIndexInfo(null, null, name, false, false);
                String oldiname = null;
                while (ind.next()) {
                    boolean nonunique = ind.getBoolean(4);
                    String iname = ind.getString(6);
                    String k2 = key + "ind-" + iname + "-";
                    if ((oldiname == null || !oldiname.equals(iname))) {
                        root.add(new DefaultMutableTreeNode(k2));
                        root.add(new DefaultMutableTreeNode(k2));
                        oldiname = iname;
                    }
                    String c = ind.getString(9);
                    root.add(new DefaultMutableTreeNode(k2));
                }
                ind.close();
            }
            root.add(props);
            props.add(new DefaultMutableTreeNode("User: " + dMeta.getUserName()));
            props.add(new DefaultMutableTreeNode("ReadOnly: " + cConn.isReadOnly()));
            props.add(new DefaultMutableTreeNode("AutoCommit: " + cConn.getAutoCommit()));
            props.add(new DefaultMutableTreeNode("Driver: " + dMeta.getDriverName()));
            props.add(new DefaultMutableTreeNode("Product: " + dMeta.getDatabaseProductName()));
            props.add(new DefaultMutableTreeNode("Version: " + dMeta.getDatabaseProductVersion()));
        } catch (SQLException e) {
            root.add(new DefaultMutableTreeNode("Error getting metadata:"));
            root.add(new DefaultMutableTreeNode("-" + e.getMessage()));
            root.add(new DefaultMutableTreeNode("-" + e.getSQLState()));
        }
        propTree.setModel(new DefaultTreeModel(root));
    }

    void execButton_actionPerformed(ActionEvent e) {
        String sCmd = commandField.getText();
        System.out.println("Executing " + sCmd);
        String g[] = new String[1];
        try {
            lTime = System.currentTimeMillis();
            sStatement.execute(sCmd);
            int r = sStatement.getUpdateCount();
            System.out.println("Result " + r);
            if (r == -1) {
                formatResultSet(sStatement.getResultSet());
            } else {
                g[0] = "update count";
                resultModel.setColumnIdentifiers(g);
                g[0] = "" + r;
                resultModel.addRow(g);
            }
            lTime = System.currentTimeMillis() - lTime;
        } catch (SQLException sqle) {
            System.out.println("SQL Error " + sqle.getMessage());
        }
        refreshTree();
    }

    void formatResultSet(ResultSet r) {
        resultModel.setRowCount(0);
        if (r == null) {
            String g[] = new String[1];
            g[0] = "Result";
            resultModel.setColumnIdentifiers(g);
            g[0] = "(empty)";
            resultModel.addRow(g);
            return;
        }
        try {
            ResultSetMetaData m = r.getMetaData();
            int col = m.getColumnCount();
            String h[] = new String[col];
            for (int i = 1; i <= col; i++) {
                h[i - 1] = m.getColumnLabel(i);
            }
            resultModel.setColumnIdentifiers(h);
            while (r.next()) {
                for (int i = 1; i <= col; i++) {
                    h[i - 1] = r.getString(i);
                    if (r.wasNull()) {
                        h[i - 1] = "(null)";
                    }
                }
                resultModel.addRow(h);
            }
            r.close();
        } catch (SQLException e) {
        }
    }
}
