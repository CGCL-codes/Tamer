package org.datanucleus.dataquality;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DuplicatePane extends JPanel {

    private ReportTable _rt;

    private RowsetTable _rs;

    private Hashtable<String, Integer> _ht;

    private JLabel n_l;

    private String _table;

    private String _query;

    private String _condition;

    private JFrame _parent;

    public DuplicatePane(String query, String condition, String table, JFrame parent) {
        _table = table;
        _query = query;
        _parent = parent;
        _condition = condition;
        runQuery(query);
        createGUI();
    }

    private ReportTable runQuery(String query) {
        try {
            Jdbc_conn123.openConn();
            ResultSet rs = Jdbc_conn123.runQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colc = rsmd.getColumnCount();
            _ht = new Hashtable<String, Integer>();
            for (int i = 1; i < colc; i++) {
                _ht.put(rsmd.getColumnName(i + 1), rsmd.getColumnType(i + 1));
            }
            _rt = SqlTablePanel.getSQLValue(rs, true);
            rs.close();
            Jdbc_conn123.closeConn();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, sqle.getMessage(), "Duplicate Error Dialog", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        _rt.table.getColumnModel().getColumn(0).setCellRenderer(new MyCellRenderer());
        _rt.table.addMouseListener(new MyCellRenderer());
        _rt.table.addMouseMotionListener(new MyCellRenderer());
        return _rt;
    }

    private void createGUI() {
        if (_rt == null) {
            setPreferredSize(new Dimension(250, 250));
            return;
        }
        setLayout(new BorderLayout());
        JPanel tp = new JPanel();
        n_l = new JLabel("<html><body><a href=\"\">Aggregate View  </A><body></html>");
        n_l.setVisible(false);
        n_l.addMouseListener(new LinkMouseListener());
        JLabel c_l = new JLabel("<html><body><a href=\"\">  Close</A><body></html>", JLabel.TRAILING);
        c_l.addMouseListener(new LinkMouseListener());
        tp.add(n_l);
        tp.add(c_l);
        add(tp, BorderLayout.PAGE_START);
        add(_rt, BorderLayout.CENTER);
    }

    private class LinkMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent mouseevent) {
            try {
                mouseevent.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
                String s1 = ((JLabel) mouseevent.getSource()).getText();
                if (s1 != null && s1.equals("<html><body><a href=\"\">Aggregate View  </A><body></html>")) {
                    _rt = runQuery(_query);
                    _rs.close();
                    DuplicatePane.this.remove(_rs);
                    DuplicatePane.this.add(_rt, BorderLayout.CENTER);
                    DuplicatePane.this.revalidate();
                    DuplicatePane.this.repaint();
                    n_l.setVisible(false);
                } else if (s1 != null && s1.equals("<html><body><a href=\"\">  Close</A><body></html>")) {
                    if (_rs != null) _rs.close();
                    _parent.dispose();
                }
            } finally {
                mouseevent.getComponent().setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        }

        public void mouseEntered(MouseEvent mouseevent) {
            mouseevent.getComponent().setCursor(Cursor.getPredefinedCursor(12));
        }

        private LinkMouseListener() {
        }
    }

    private class MyCellRenderer extends DefaultTableCellRenderer implements MouseListener, MouseMotionListener {

        public MyCellRenderer() {
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String text = ((JLabel) c).getText();
            ((JLabel) c).setHorizontalAlignment(JLabel.TRAILING);
            ((JLabel) c).setText("<html><body><a href=\"\">" + text + "</A><body></html>");
            return c;
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
            Point p = e.getPoint();
            int col_i = _rt.table.columnAtPoint(p);
            int row_i = _rt.table.rowAtPoint(p);
            if (_rt.isSorting()) {
                JOptionPane.showMessageDialog(null, "Table in Sorting state", "Sorting Error", JOptionPane.ERROR_MESSAGE);
                _rt.cancelSorting();
                return;
            }
            int n = JOptionPane.showConfirmDialog(null, "Edit Mode will update undelying Database \n Do you want Edit Mode ?", "Edit Option", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.NO_OPTION) return;
            if (row_i >= _rt.table.getRowCount() || row_i < 0) return;
            _rt.table.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int colc = _rt.getModel().getColumnCount();
            Vector<Object> vc_v = new Vector<Object>();
            Vector<Integer> vc_t = new Vector<Integer>();
            Vector<String> vc_s = new Vector<String>();
            for (int i = 1; i < colc; i++) {
                Object obj = _rt.getModel().getValueAt(row_i, i);
                vc_v.add(i - 1, obj);
                String colN = _rt.getModel().getColumnName(i);
                vc_s.add(i - 1, colN);
                int type = _ht.get(colN);
                vc_t.add(i - 1, type);
            }
            QueryBuilder qb = new QueryBuilder(Jdbc_conn123.getHValue("Database_DSN"), _table, Jdbc_conn123.getDBType());
            String query = qb.get_equal_query(vc_s, _condition);
            try {
                _rs = new RowsetTable(query, true, vc_s.get(0), vc_t, vc_v);
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(null, exp.getMessage(), "Rowset Expand Error Dialog", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DuplicatePane.this.remove(_rt);
            DuplicatePane.this.add(_rs, BorderLayout.CENTER);
            DuplicatePane.this.revalidate();
            DuplicatePane.this.repaint();
            n_l.setVisible(true);
        }

        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            int col_i = _rt.table.columnAtPoint(p);
            int row_i = _rt.table.rowAtPoint(p);
            String colName = _rt.table.getColumnName(col_i);
            if (colName.equalsIgnoreCase("COUNT") == true) {
                _rt.table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                _rt.table.setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}
