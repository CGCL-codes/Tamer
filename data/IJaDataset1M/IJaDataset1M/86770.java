package de.qnerd.rpgBot.database;

import java.sql.Connection;

/**
 * @author Sven Kuhnert
 *
 * 
 * 
 */
public class DatabaseManager_old {

    public static Connection conn = null;

    public static void init() {
        DatabaseConnector dc = DatabaseConnector.getInstance();
        dc.initDb();
        conn = dc.conn;
    }
}
