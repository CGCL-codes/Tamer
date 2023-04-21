package org.retro.gis;

import java.util.*;
import java.io.*;
import java.sql.*;

public class MyDBConfig {

    private Connection conn = null;

    public MyDBConfig() {
    }

    public void insertConnect() throws IOException, SQLException {
        if (conn == null) throw new IOException("$$$ My-Error $$$ - The connection is null");
        Statement _stmt = null;
        String _query = null;
        ResultSet _rSet = null;
        PreparedStatement _pstmt = null;
        try {
            _stmt = conn.createStatement();
            System.out.println("$$[My-SQL]$$-----------------------------------------");
            _query = "SELECT * FROM BOT_CONNECT_LOG";
            _rSet = _stmt.executeQuery(_query);
            while (_rSet.next()) {
            }
            System.out.println("-----------------------------------------");
            _pstmt = conn.prepareStatement("INSERT INTO BOT_CONNECT_LOG (CUR_USER, CUR_DESC) VALUES (?,?)");
            _pstmt.clearParameters();
            _pstmt.setString(1, "e");
            _pstmt.setString(2, "f");
            boolean _bres = _pstmt.execute();
            _pstmt.close();
            _pstmt = null;
            _stmt.close();
            _stmt = null;
            System.out.println("$$$ Add-Res: " + _bres);
        } catch (SQLException _e) {
            _e.printStackTrace();
        } finally {
            if (_stmt != null) {
                try {
                    _stmt.close();
                } catch (Exception _nothing) {
                }
            }
            if (_pstmt != null) {
                try {
                    _pstmt.close();
                } catch (Exception _nothing) {
                }
            }
        }
    }

    public void load() throws IOException, SQLException {
        conn = BotMyPool.getFirstConnection();
        if (conn == null) throw new IOException("$$$ MySQL-Error $$$ - The connection is null");
        System.out.println(" Database loaded...[ OK ]");
        Statement _stmt = null;
        String _query = null;
        HashMap map = DatabaseContainer.getMyList();
        if (map == null) {
            throw new SQLException("$$$ error: The database container map has not been created. $$$");
        }
        try {
            System.out.println("$$[My-SQL load]$$-----------------------------------------");
            _stmt = conn.createStatement();
            for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                System.out.println(e.getKey() + " | is being created....");
                String _obj[] = (String[]) e.getValue();
                try {
                    _stmt.executeUpdate(_obj[1]);
                    System.out.println("[PASSED]" + _obj[2]);
                } catch (SQLException ee) {
                    ee.printStackTrace();
                }
            }
        } catch (SQLException _e) {
            _e.printStackTrace();
        } finally {
            if (_stmt != null) {
                try {
                    _stmt.close();
                } catch (Exception _nothing) {
                }
            }
        }
    }
}
