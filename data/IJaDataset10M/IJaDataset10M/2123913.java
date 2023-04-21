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
* Implementation of Modulo_tiene_operacionRecordCountDAO interface 
* 
*/
public class Modulo_tiene_operacionRecordCountDAOImpl implements Modulo_tiene_operacionRecordCountDAO {

    public Modulo_tiene_operacionRecordCountDAOImpl() {
    }

    public Modulo_tiene_operacionRecordCount getRecordCount(Connection con) throws Modulo_tiene_operacionRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM MODULO_TIENE_OPERACION");
            rs = stmt.executeQuery();
            rs.next();
            Modulo_tiene_operacionRecordCount vo = new Modulo_tiene_operacionRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Modulo_tiene_operacionRecordCountException(sqle);
        } catch (Exception e) {
            throw new Modulo_tiene_operacionRecordCountException(e);
        }
    }

    public Modulo_tiene_operacionRecordCount getRecordCount(Connection con, String whereClause) throws Modulo_tiene_operacionRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM MODULO_TIENE_OPERACION  " + whereClause);
            rs = stmt.executeQuery();
            rs.next();
            Modulo_tiene_operacionRecordCount vo = new Modulo_tiene_operacionRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Modulo_tiene_operacionRecordCountException(sqle);
        } catch (Exception e) {
            throw new Modulo_tiene_operacionRecordCountException(e);
        }
    }
}
