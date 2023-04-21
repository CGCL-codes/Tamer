package org.triviatime.client;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;

public class Scores extends JScrollPane {

    private JTable information;

    private TTClient delegate;

    public Scores(TTClient d) {
        super();
        delegate = d;
        JScrollPane scrollpane;
        setSize(200, 100);
        setVisible(true);
        TableModel dataModel = new AbstractTableModel() {

            public int getColumnCount() {
                return 2;
            }

            public String getColumnName(int i) {
                if (i == 0) return "Name"; else if (i == 1) return "Score"; else return "IP";
            }

            public int getRowCount() {
                return 8;
            }

            public Object getValueAt(int row, int col) {
                String[] Names = delegate.getPlayerNames();
                String[] Scores = delegate.getPlayerScores();
                if (col == 0) return Names[row]; else if (col == 1) return Scores[row]; else return "Not Supposed to Happen";
            }
        };
        information = new JTable(dataModel);
        information.setSize(200, 100);
        setViewportView(information);
        repaint();
        validate();
    }
}
