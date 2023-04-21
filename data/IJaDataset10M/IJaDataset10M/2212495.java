package com.mrroman.linksender.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import com.mrroman.linksender.history.HistoryKeeper;
import com.mrroman.linksender.ioc.In;
import com.mrroman.linksender.ioc.Init;
import com.mrroman.linksender.ioc.Locales;
import com.mrroman.linksender.ioc.Log;
import com.mrroman.linksender.ioc.Name;
import com.mrroman.linksender.sender.Message;

/**
 *
 * @author gorladam
 */
@Name("gui.History")
public class History {

    @Locales
    private ResourceBundle messages;

    @Log
    private Logger logger;

    @In
    private HistoryKeeper historyKeeper;

    private JTable table;

    private JTextField filterText;

    private TableRowSorter<MyTableModel> sorter;

    private JFrame mainFrame;

    @Init
    public void prepareFrame() {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout(10, 10));
        MyTableModel model = new MyTableModel();
        sorter = new TableRowSorter<MyTableModel>(model);
        table = new JTable(model);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(200 * (i / 2) + 150);
        }
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 600));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(true);
        JScrollPane scrollPane = new JScrollPane(table);
        content.add(scrollPane);
        JPanel form = new JPanel(new BorderLayout(10, 0));
        JLabel l1 = new JLabel(messages.getString("filter") + ":", SwingConstants.TRAILING);
        form.add(l1, BorderLayout.WEST);
        filterText = new JTextField();
        filterText.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }
        });
        l1.setLabelFor(filterText);
        form.add(filterText, BorderLayout.CENTER);
        content.add(form, BorderLayout.SOUTH);
        content.setOpaque(true);
        mainFrame = new JFrame(messages.getString("history"));
        mainFrame.setContentPane(content);
        mainFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
    }

    /**
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(filterText.getText());
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    class MyTableModel extends AbstractTableModel {

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public int getRowCount() {
            return historyKeeper.size();
        }

        @Override
        public String getColumnName(int col) {
            switch(col) {
                case 0:
                    return messages.getString("history_col_date");
                case 1:
                    return messages.getString("history_col_sender");
                case 2:
                    return messages.getString("history_col_message");
            }
            return null;
        }

        @Override
        public Object getValueAt(int row, int col) {
            Message s = historyKeeper.get(row).getMessage();
            switch(col) {
                case 0:
                    return SimpleDateFormat.getDateTimeInstance().format(s.getDate());
                case 1:
                    return s.getSender();
                case 2:
                    return s.getMessage();
            }
            return null;
        }

        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
    }

    public void showHistory() {
        mainFrame.setVisible(true);
    }
}
