package org.dkppro;

import java.util.*;
import java.sql.*;

public class AddItem {

    public AddItem() throws Exception {
    }

    public int addItem(String itemName, String dkpValue, String stats, String encounterId, ConnectionPool pool) throws Exception {
        Connection conn = null;
        int itemId = 0;
        if (stats == null) stats = "";
        try {
            conn = pool.getConnection();
            Statement statement = conn.createStatement();
            statement.executeQuery("INSERT INTO items (item_name, item_dkp, stats, encounter_id) VALUES(\"" + itemName + "\", " + dkpValue + ", \"" + stats + "\", " + encounterId + ")");
            ResultSet idCheck = statement.executeQuery("select LAST_INSERT_ID();");
            idCheck.first();
            itemId = idCheck.getInt(idCheck.findColumn("last_insert_id()"));
            pool.returnConnection(conn);
            return itemId;
        } catch (SQLException ex) {
            System.out.println(ex);
            pool.returnConnection(conn);
            return 0;
        }
    }
}
