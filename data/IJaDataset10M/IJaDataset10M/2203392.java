package com.akcess.impl;

import java.math.*;
import java.sql.*;
import java.net.URL;
import java.util.*;
import com.akcess.exception.*;
import com.akcess.vo.*;
import com.akcess.dao.*;

/**
* 
* Implementation of Log_reservasRecordCountDAO interface 
* 
*/
public class Log_reservasRecordCountDAOImpl implements Log_reservasRecordCountDAO {

    public Log_reservasRecordCountDAOImpl() {
    }

    public Log_reservasRecordCount getRecordCount(Connection con) throws Log_reservasRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM LOG_RESERVAS");
            rs = stmt.executeQuery();
            rs.next();
            Log_reservasRecordCount vo = new Log_reservasRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Log_reservasRecordCountException(sqle);
        } catch (Exception e) {
            throw new Log_reservasRecordCountException(e);
        }
    }

    public Log_reservasRecordCount getRecordCount(Connection con, String whereClause) throws Log_reservasRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM LOG_RESERVAS  " + whereClause);
            rs = stmt.executeQuery();
            rs.next();
            Log_reservasRecordCount vo = new Log_reservasRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Log_reservasRecordCountException(sqle);
        } catch (Exception e) {
            throw new Log_reservasRecordCountException(e);
        }
    }
}
