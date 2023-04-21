package ch.integis.ili2sql.gui;

import javax.swing.*;
import ch.integis.ili2sql.*;

/**
 * Component representing the TableHandler in the TablePreviewDialog
 * @author Roger T&ouml;nz
 * @version 0.1
 * @since 21.10.2002
 */
public class TableComponent extends JPanel {

    private TableHandler table = null;

    private JTable jTable = null;

    /** Creates new form TableComponent
     * @param table
     */
    public TableComponent(TableHandler table) {
        this.table = table;
        jTable = new JTable();
        initGUI();
    }

    /** This method is called from within the constructor to initialize the form. */
    private void initGUI() {
        setLayout(new java.awt.BorderLayout());
        add(jTable.getTableHeader(), java.awt.BorderLayout.NORTH);
        add(jTable, java.awt.BorderLayout.CENTER);
        jTable.setModel(table);
    }
}
