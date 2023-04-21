package saadadb.admintool.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;
import saadadb.admintool.AdminTool;
import saadadb.admintool.components.AdminComponent;
import saadadb.admintool.components.SQLJTable;
import saadadb.admintool.utils.DataTreePath;
import saadadb.exceptions.FatalException;
import saadadb.exceptions.QueryException;
import saadadb.sqltable.SQLTable;
import saadadb.util.Messenger;

public class DataTableWindow extends OuterWindow {

    protected String sqlQuery;

    private DataTreePath dataTreePath;

    private SQLJTable productTable;

    protected JTextArea queryTextArea = new JTextArea(sqlQuery);

    /**
	 * @param rootFrame
	 * @param treePath
	 * @throws QueryException
	 */
    public DataTableWindow(AdminTool rootFrame, TreePath treePath) throws QueryException {
        super(rootFrame);
        this.dataTreePath = new DataTreePath(treePath);
    }

    /**
	 * @param rootFrame
	 * @throws QueryException
	 */
    protected DataTableWindow(AdminTool rootFrame) throws QueryException {
        super(rootFrame);
        this.dataTreePath = null;
    }

    /**
	 * 
	 */
    protected void buidSQL() {
        sqlQuery = "SELECT ";
        title = "??";
        String[] rejected_coll_clos = null;
        String coll_table_name = dataTreePath.collection + "_" + dataTreePath.category.toLowerCase();
        ;
        try {
            rejected_coll_clos = SQLTable.getColumnsExceptsThose(coll_table_name, new String[] { "oidproduct", "access_right", "loaded", "group_oid_csa", "nb_rows_csa", "y_min_csa", "y_max_csa", "y_unit_csa", "y_colname_csa", "shape_csa" });
        } catch (FatalException e) {
            Messenger.trapFatalException(e);
        }
        if (dataTreePath.isCategoryLevel()) {
            for (int i = 0; i < rejected_coll_clos.length; i++) {
                if (i != 0) {
                    sqlQuery += ", ";
                }
                sqlQuery += rejected_coll_clos[i];
            }
            sqlQuery += "\nFROM " + coll_table_name + "\nLIMIT 1000";
            title = dataTreePath.category + " data of collection <" + dataTreePath.collection + "> (truncated to 1000)";
        } else if (dataTreePath.isClassLevel()) {
            for (int i = 0; i < rejected_coll_clos.length; i++) {
                if (i != 0) {
                    sqlQuery += ", ";
                }
                sqlQuery += "coll." + rejected_coll_clos[i];
            }
            String[] rejected_class_clos = null;
            try {
                rejected_class_clos = SQLTable.getColumnsExceptsThose(dataTreePath.classe, new String[] { "oidsaada", "namesaada" });
            } catch (FatalException e) {
                Messenger.trapFatalException(e);
            }
            for (int i = 0; i < rejected_class_clos.length; i++) {
                sqlQuery += ", class." + rejected_class_clos[i];
            }
            sqlQuery += "\nFROM " + dataTreePath.collection + "_" + dataTreePath.category.toLowerCase() + " AS coll, " + dataTreePath.classe + " AS class\nWHERE coll.oidsaada = class.oidsaada\nLIMIT 1000";
            title = "Data (" + dataTreePath.category + ") of class <" + dataTreePath.classe + ">  of collection <" + dataTreePath.collection + "> (truncated to 1000)";
        } else {
            AdminComponent.showFatalError(this, "No datasource selected");
        }
        setTitle(title);
    }

    @Override
    protected void setContent() throws Exception {
        this.buidSQL();
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        productTable = new SQLJTable(rootFrame, dataTreePath, sqlQuery, SQLJTable.PRODUCT_PANEL);
        productTable.setBackground(AdminComponent.LIGHTBACKGROUND);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane jsp = new JScrollPane(productTable);
        jsp.setBackground(AdminComponent.LIGHTBACKGROUND);
        JPanel qp = new JPanel();
        queryTextArea = new JTextArea(sqlQuery);
        qp.setLayout(new BoxLayout(qp, BoxLayout.PAGE_AXIS));
        qp.add(new JScrollPane(queryTextArea));
        JButton jb = new JButton("SUBMIT");
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                refresh(queryTextArea.getText());
            }
        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jsp, qp);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(350);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
        Component c;
        if ((c = this.addCommandComponent()) != null) {
            this.getContentPane().add(c, BorderLayout.SOUTH);
        }
    }

    /**
	 * @return
	 */
    protected Component addCommandComponent() {
        JButton jb = new JButton("SUBMIT");
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                refresh(queryTextArea.getText());
            }
        });
        return jb;
    }

    /**
	 * @param newQuery
	 */
    public void refresh(String newQuery) {
        if (productTable != null) {
            try {
                this.sqlQuery = newQuery;
                this.productTable.setModel(sqlQuery);
            } catch (QueryException e) {
                Messenger.trapQueryException(e);
            }
        }
    }

    /**
	 * 
	 */
    public void refresh() {
        if (productTable != null) {
            try {
                productTable.setModel(sqlQuery);
            } catch (QueryException e) {
                Messenger.trapQueryException(e);
            }
        }
    }

    public JTable getProductTable() {
        return this.productTable;
    }

    public DataTreePath getDataTreePath() {
        return dataTreePath;
    }
}
