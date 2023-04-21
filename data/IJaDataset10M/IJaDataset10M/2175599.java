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
* Implementation of SoRecordCountDAO interface 
* 
*/
public class SoRecordCountDAOImpl implements SoRecordCountDAO {

    public SoRecordCountDAOImpl() {
    }

    public SoRecordCount getRecordCount(Connection con) throws SoRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM SO");
            rs = stmt.executeQuery();
            rs.next();
            SoRecordCount vo = new SoRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new SoRecordCountException(sqle);
        } catch (Exception e) {
            throw new SoRecordCountException(e);
        }
    }

    public SoRecordCount getRecordCount(Connection con, String whereClause) throws SoRecordCountException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT COUNT(*) FROM SO  " + whereClause);
            rs = stmt.executeQuery();
            rs.next();
            SoRecordCount vo = new SoRecordCount();
            vo.setCount(rs.getInt(1));
            return vo;
        } catch (SQLException sqle) {
            throw new SoRecordCountException(sqle);
        } catch (Exception e) {
            throw new SoRecordCountException(e);
        }
    }
}
