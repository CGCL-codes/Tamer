package com.rb.lottery.analysis.demo;

import java.awt.BorderLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @类功能说明:
 * @类修改者:
 * @修改日期:
 * @修改说明:
 * @作者: robin
 * @创建时间: 2011-11-10 上午09:30:25
 * @版本: 1.0.0
 */
public class TableCheckBoxDemo extends JFrame {

    JPanel panel = (JPanel) getContentPane();

    public JScrollPane scrollPane = new JScrollPane();

    public JTable table = new JTable();

    JComboBox c = new JComboBox();

    public TableCheckBoxDemo() {
        init();
    }

    public void init() {
        c.addItem("FOXCONN ");
        c.addItem("AOPEN ");
        c.addItem("MSI ");
        c.addItem("ACER ");
        table.setModel(setData());
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(c));
        scrollPane.getViewport().add(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private DefaultTableModel setData() {
        Object[][] BookInfo = { { "主板 ", "FOXCONN ", "10 ", new Boolean(false) }, { "CPU ", "MSI ", "200 ", new Boolean(true) } };
        String[] ColumnName = { "货物 ", "客户 ", "数量 ", "是否有出库 " };
        DefaultTableModel myData = new MyTableModel(ColumnName, 0);
        myData.addRow(BookInfo[0]);
        myData.addRow(BookInfo[1]);
        return myData;
    }

    public static void main(String para[]) {
        new TableCheckBoxDemo();
    }
}

class MyTableModel extends DefaultTableModel {

    public MyTableModel(Object[] head, int n) {
        super(head, n);
    }

    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
