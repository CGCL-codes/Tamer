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
* Implementation of Tipo_recursoRecordCountDAO interface 
* 
*/
public class Tipo_recursoRecordCountDAOImpl implements Tipo_recursoRecordCountDAO {

    public Tipo_recursoRecordCountDAOImpl() {
    }

    public Tipo_recursoRecordCount getRecordCount(Connection con) throws Tipo_recursoRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM TIPO_RECURSO");
            rs = stmt.executeQuery();
            rs.next();
            Tipo_recursoRecordCount vo = new Tipo_recursoRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Tipo_recursoRecordCountException(sqle);
        } catch (Exception e) {
            throw new Tipo_recursoRecordCountException(e);
        }
    }

    public Tipo_recursoRecordCount getRecordCount(Connection con, String whereClause) throws Tipo_recursoRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM TIPO_RECURSO  " + whereClause);
            rs = stmt.executeQuery();
            rs.next();
            Tipo_recursoRecordCount vo = new Tipo_recursoRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Tipo_recursoRecordCountException(sqle);
        } catch (Exception e) {
            throw new Tipo_recursoRecordCountException(e);
        }
    }
}
