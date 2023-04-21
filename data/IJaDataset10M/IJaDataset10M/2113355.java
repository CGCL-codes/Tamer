package com.dukesoftware.utils.swing.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import com.dukesoftware.utils.swing.jframe.JFrameFactory;

public class JTableTest extends JPanel implements ActionListener, TableModelListener {

    private static final long serialVersionUID = 1L;

    private JButton sort1 = new JButton("Sort 1");

    public JTableTest(DefaultTableModel tableModel) {
        super(new BorderLayout());
        tableModel.addTableModelListener(this);
        JTable table = new JTable(tableModel);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        add(scroller, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sort1);
        sort1.addActionListener(this);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sort1) {
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Object[] headers = { "Name", "Number", "Color" };
        Object[][] data = { { "Satoko", "11", "White" }, { null, "12", "Black" }, { "Satoko", "11", "White" } };
        DefaultTableModel model = new DefaultTableModel(data, headers);
        model.addRow(new Object[] { "Satoko", "11", "White" });
        JFrame frame = JFrameFactory.createExitJFrame(new JTableTest(model), "iino");
        frame.setVisible(true);
        while (true) {
            model.addRow(new Object[] { "a", "b", "c" });
            Thread.sleep(1000);
        }
    }

    public void tableChanged(TableModelEvent e) {
        switch(e.getType()) {
            case TableModelEvent.DELETE:
                System.out.println("delete");
                break;
            case TableModelEvent.INSERT:
                System.out.println("insert");
                break;
            case TableModelEvent.UPDATE:
                String text = "Column = " + e.getColumn();
                text += " : FirstRow = " + e.getFirstRow();
                text += " : LastRow = " + e.getLastRow();
                System.out.println("update" + text);
                break;
            default:
        }
    }
}
