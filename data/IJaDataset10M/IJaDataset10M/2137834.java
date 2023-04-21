package com.ruanko.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import com.ruanko.model.Singer;

public class SingerManagerPanel extends JPanel implements ActionListener {

    int flag = 0;

    JTable table;

    DefaultTableModel tableModel;

    Vector<Singer> ans = new Vector<Singer>();

    public SingerManagerPanel() throws SQLException {
        this.setLayout(null);
        System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
        try {
            UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
            JRootPane rootPane = this.getRootPane();
            rootPane.setWindowDecorationStyle(JRootPane.FRAME);
            rootPane.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
            rootPane.putClientProperty("Quaqua.RootPane.isVertical", Boolean.TRUE);
            rootPane.putClientProperty("Quaqua.RootPane.isPalette", Boolean.TRUE);
            rootPane.putClientProperty("windowModified", Boolean.TRUE);
        } catch (Exception e) {
        }
        SingerRightPanel rp = new SingerRightPanel(this);
        table = new JTable() {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tableModel = (DefaultTableModel) table.getModel();
        FitTableColumns();
        tableModel.addColumn("���ֱ��");
        tableModel.addColumn("�������");
        tableModel.addColumn("����ƴ��");
        tableModel.addColumn("��������");
        tableModel.addColumn("������Ŀ��");
        tableModel.addColumn("ͼƬ·��");
        this.ShowList(null, null, null, null, null);
        this.add(scrollPane);
        this.add(rp);
        this.setSize(750, 600);
        scrollPane.setBounds(10, 10, 550, 500);
        rp.setLocation(570, 10);
    }

    public void ShowList(String name, String pinYin, String type, Integer count, String picPath) throws SQLException {
        tableModel.getDataVector().removeAllElements();
        ans = ServerPD.QuerySinger(null, name, pinYin, type, count, picPath);
        for (int i = 0; i < ans.size(); i++) {
            Singer s = ans.get(i);
            tableModel.addRow(new Object[] { s.ID, s.Name, s.PinYin, s.Type, s.Count, s.PicPath });
        }
        table.invalidate();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D gp2 = (Graphics2D) g;
        ImageIcon icon = new ImageIcon("images\\toolbarBg.png");
        gp2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gp2.drawImage(icon.getImage(), 0, 0, this.getSize().width, this.getSize().height, 0, 0, icon.getIconWidth(), icon.getIconHeight(), this);
    }

    public void actionPerformed(ActionEvent event) {
    }

    public void FitTableColumns() {
        JTableHeader header = table.getTableHeader();
        int rowCount = table.getRowCount();
        Enumeration columns = table.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) table.getCellRenderer(row, col).getTableCellRendererComponent(table, table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth) + 10;
            }
            header.setResizingColumn(column);
            column.setWidth(width + table.getIntercellSpacing().width);
        }
    }
}
