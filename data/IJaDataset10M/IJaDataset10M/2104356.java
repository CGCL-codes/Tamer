package view;

import model.DatabaseModel;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;

public class FlightInfoView extends JFrame {

    private Connection con;

    private Statement smt;

    private ResultSet rs;

    private ResultSetMetaData rsmd;

    private JTable BookingViewTable;

    private JButton BookingViewBackButton;

    public FlightInfoView() {
        initComponents();
        this.setTitle("Flight Information");
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
        this.setSize(960, 480);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        BookingViewBackButton = new JButton("Back");
        BookingViewBackButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        con = DatabaseModel.createConnection();
        Vector columnHeads = new Vector();
        Vector rows = new Vector();
        try {
            String query = "SELECT * FROM ticket_info";
            smt = con.createStatement();
            rs = smt.executeQuery(query);
            rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                columnHeads.addElement(rsmd.getColumnName(i));
            }
            while (rs.next()) {
                Vector currentRow = new Vector();
                for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                    currentRow.addElement(rs.getString(i));
                }
                rows.addElement(currentRow);
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        BookingViewTable = new JTable(rows, columnHeads);
        BookingViewTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        try {
            for (int i = 0; i < rsmd.getColumnCount(); ++i) {
                TableColumn Col1 = BookingViewTable.getColumnModel().getColumn(i);
                Col1.setPreferredWidth(95);
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
        JScrollPane scroller = new JScrollPane(BookingViewTable);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        c.add(scroller, BorderLayout.CENTER);
        c.add(BookingViewBackButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == BookingViewBackButton) {
            this.dispose();
            UserView n_w = new UserView();
            n_w.setVisible(true);
        }
    }
}
