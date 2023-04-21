package test.xito.dialog;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.xito.dialog.*;

public class DynamicRowsLayout {

    public static void main(String args[]) {
        JFrame f = new JFrame("DynamicRowsLayout");
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setSize(400, 400);
        final TableLayout layout = new TableLayout();
        TableLayout.Row r = new TableLayout.Row(TableLayout.PREFERRED);
        r.addCol(new TableLayout.Column("add_btn", TableLayout.PREFERRED));
        layout.addRow(r);
        final TablePanel tablePanel = new TablePanel(layout);
        f.getContentPane().add(tablePanel);
        JButton addBtn = new JButton("Add Row");
        addBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                TableLayout.Row r = new TableLayout.Row();
                String name = "row" + (layout.getRowCount() + 1);
                TableLayout.Column col = new TableLayout.Column(name, 0.999f);
                col.colSpan = 1;
                col.hAlign = TableLayout.FULL;
                r.addCol(col);
                layout.addRow(r);
                tablePanel.add(name, new JTextField(name));
                tablePanel.revalidate();
                tablePanel.repaint();
            }
        });
        tablePanel.add("add_btn", addBtn);
        f.setVisible(true);
    }
}
