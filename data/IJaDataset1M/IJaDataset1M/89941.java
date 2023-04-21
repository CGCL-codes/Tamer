package net.sourceforge.jdbcexplorer;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.DefaultCellEditor;
import java.awt.Component;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ResultSetPanel extends JPanel {

    public ResultSetPanel() {
        super(new BorderLayout());
        JTable table = new JTable();
        _model = new ResultSetTableModel();
        table.setModel(_model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollpane = new JScrollPane(table);
        add(scrollpane, BorderLayout.CENTER);
    }

    public void setResultSet(ResultSet rs) throws SQLException {
        _model.setResultSet(rs);
    }

    public void clear() {
        _model.clear();
    }

    public void edit() {
    }

    private class TableListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent ev) {
        }
    }

    private ResultSetTableModel _model;
}
