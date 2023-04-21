package org.visu;

import java.awt.*;
import org.visu.sui.*;
import org.visu.ui.*;
import java.sql.*;
import javax.swing.*;

public class Main {

    boolean packFrame = false;

    public Main() {
        MainFrame frame = MainFrame.getInstance();
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        VPG.logInfo("Starting VPG (Visual Postgres) ...");
        if (args.length > 0 && args[0].equalsIgnoreCase("swt")) {
            startSwt();
        } else {
            startSwing();
        }
    }

    private static void startSwing() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        new Main();
    }

    private static void startSwt() {
        SMainFrame.main(null);
    }

    public static void mainTest() throws Throwable {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/visual", "Administrator", "");
        DatabaseMetaData meta = con.getMetaData();
        ResultSet prs = meta.getPrimaryKeys(null, "public", "table3");
        while (prs.next()) {
            System.err.print("COL_NAME " + prs.getString(4));
            System.err.print(" KEY_SEQ  " + prs.getShort(5));
            System.err.println(" PK_NAME  " + prs.getString(6));
        }
    }
}
