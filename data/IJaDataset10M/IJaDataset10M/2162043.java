package com.cafe.serve.tablemodel;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class SortTable {

    public static void main(String args[]) {
        Runnable runner = new Runnable() {

            public void run() {
                JFrame frame = new JFrame("Sorting JTable");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Object rows[][] = { { "AMZN", "Amazon", 41.28 }, { "EBAY", "eBay", 41.57 }, { "GOOG", "Google", 388.33 }, { "MSFT", "Microsoft", 26.56 }, { "NOK", "Nokia Corp", 17.13 }, { "ORCL", "Oracle Corp.", 12.52 }, { "SUNW", "Sun Microsystems", 3.86 }, { "TWX", "Time Warner", 17.66 }, { "VOD", "Vodafone Group", 26.02 }, { "YHOO", "Yahoo!", 37.69 } };
                String columns[] = { "Symbol", "Name", "Price" };
                TableModel model = new DefaultTableModel(rows, columns) {

                    public Class getColumnClass(int column) {
                        Class returnValue;
                        if ((column >= 0) && (column < getColumnCount())) {
                            returnValue = getValueAt(0, column).getClass();
                        } else {
                            returnValue = Object.class;
                        }
                        return returnValue;
                    }
                };
                JTable table = new JTable(model);
                RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
                table.setRowSorter(sorter);
                JScrollPane pane = new JScrollPane(table);
                frame.add(pane, BorderLayout.CENTER);
                frame.setSize(300, 150);
                frame.setVisible(true);
            }
        };
        EventQueue.invokeLater(runner);
    }
}
