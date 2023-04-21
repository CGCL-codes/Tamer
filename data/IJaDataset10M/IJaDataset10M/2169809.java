package Mediator;

import java.sql.*;
import Foundation.FDBConnect;

public class MReader {

    private Connection conn;

    private Statement stmt;

    private ResultSet result;

    public MReader() {
    }

    public ResultSet select(String sql) {
        try {
            FDBConnect dbc = new FDBConnect();
            conn = dbc.getConn();
            stmt = conn.createStatement();
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            this.stmt.close();
            this.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
