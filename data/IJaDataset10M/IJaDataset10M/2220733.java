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
* Implementation of Recurso_humanoRecordCountDAO interface 
* 
*/
public class Recurso_humanoRecordCountDAOImpl implements Recurso_humanoRecordCountDAO {

    public Recurso_humanoRecordCountDAOImpl() {
    }

    public Recurso_humanoRecordCount getRecordCount(Connection con) throws Recurso_humanoRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM RECURSO_HUMANO");
            rs = stmt.executeQuery();
            rs.next();
            Recurso_humanoRecordCount vo = new Recurso_humanoRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Recurso_humanoRecordCountException(sqle);
        } catch (Exception e) {
            throw new Recurso_humanoRecordCountException(e);
        }
    }

    public Recurso_humanoRecordCount getRecordCount(Connection con, String whereClause) throws Recurso_humanoRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM RECURSO_HUMANO  " + whereClause);
            rs = stmt.executeQuery();
            rs.next();
            Recurso_humanoRecordCount vo = new Recurso_humanoRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new Recurso_humanoRecordCountException(sqle);
        } catch (Exception e) {
            throw new Recurso_humanoRecordCountException(e);
        }
    }
}
