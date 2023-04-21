package com.homeautomate.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import uk.ltd.getahead.dwr.util.Logger;
import com.mysql.jdbc.PreparedStatement;

public class Database {

    Logger log = Logger.getLogger(this.getClass());

    private static Database instance;

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur à la récupération de la connection à la base");
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/gorilla", "homeautomate", "homeautomate");
        } catch (SQLException e) {
            System.out.println("Erreur à la récupération de la connection à la base");
            e.printStackTrace();
        }
        return conn;
    }

    public static Database getInstance() {
        if (instance == null) try {
            instance = new Database();
            instance.getConnection();
        } catch (Exception e) {
            System.out.println("Erreur à la récupération de la connection à la base");
            e.printStackTrace();
        }
        return instance;
    }

    public boolean executeSQL(String sql) throws SQLException {
        Boolean retour = false;
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = getConnection();
            pstm = (PreparedStatement) conn.prepareStatement(sql.toString());
            pstm.execute();
            retour = true;
        } catch (Exception e) {
            System.out.println("(HomeLogger.TYPE_ERROR, HomeLogger.APPENDER_LOGONLY, \"Erreur dans execution SQL " + e.getMessage() + " " + e.getCause() + ", Database.class");
        } finally {
            if (pstm != null) pstm.close();
            if (conn != null) conn.close();
        }
        return retour;
    }
}
