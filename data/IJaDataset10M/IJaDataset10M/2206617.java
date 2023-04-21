package corina.db;

import corina.Sample;
import corina.Element;
import corina.gui.Layout;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class DBBrowser extends JPanel {

    public DBBrowser(Connection conn) throws SQLException {
        this.c = conn;
        setLayout(new BorderLayout());
        add(new JLabel("Connecting to database..."));
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                Vector sites;
                try {
                    long t1, t2;
                    System.out.print("listing sites...");
                    t1 = System.currentTimeMillis();
                    sites = getSites();
                    t2 = System.currentTimeMillis();
                    System.out.println("done!  dt=" + (t2 - t1) + " ms");
                } catch (SQLException se) {
                    System.out.println("exception! -- " + se);
                    return;
                }
                final JList sitesList = new JList(sites);
                add(new JScrollPane(sitesList), BorderLayout.WEST);
                final DB db = new DB(c);
                sitesList.addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) return;
                        String site = (String) sitesList.getSelectedValue();
                        System.out.println("site " + site + " selected");
                        try {
                            long t1, t2;
                            t1 = System.currentTimeMillis();
                            List samples;
                            {
                                samples = db.getElements(site);
                            }
                            t2 = System.currentTimeMillis();
                            System.out.println("got elements (dt=" + (t2 - t1) + ")");
                            for (int i = 0; i < samples.size(); i++) {
                                Element el = (Element) samples.get(i);
                                System.out.println("got sample title=" + el.details.get("id"));
                            }
                        } catch (SQLException se) {
                            System.out.println("error querying: " + se);
                            se.printStackTrace();
                        }
                    }
                });
                remove(0);
                JTextField field = new JTextField("", 10);
                add(Layout.flowLayoutL("Search: ", field), BorderLayout.EAST);
                invalidate();
                repaint();
            }
        });
    }

    private Connection c;

    Vector getSites() throws SQLException {
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT m.site FROM meta m GROUP BY site;");
        Vector list = new Vector();
        while (rs.next()) list.add(rs.getString(1));
        stmt.close();
        return list;
    }

    List getSamples(String site) throws SQLException {
        PreparedStatement stmt = c.prepareStatement("SELECT sid FROM meta WHERE UPPER(site) = ?;");
        stmt.setString(1, site.toUpperCase());
        ResultSet rs = stmt.executeQuery();
        rs.last();
        int num = rs.getRow();
        List list = new ArrayList();
        rs.first();
        for (int i = 0; i < num; i++) {
            list.add(new Integer(rs.getInt(1)));
            rs.next();
        }
        stmt.close();
        return list;
    }
}
